/*
 * @author : Oguz Kahraman
 * @since : 3.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.core.security.handler;

import com.io.collige.core.exception.UnknownException;
import com.io.collige.core.security.encrypt.TokenEncryptor;
import com.io.collige.core.security.jwt.JWTService;
import com.io.collige.core.security.jwt.JWTUtil;
import com.io.collige.entitites.User;
import com.io.collige.enums.AppProviderType;
import com.io.collige.models.internals.SocialUser;
import com.io.collige.models.responses.user.UserResponse;
import com.io.collige.services.GoogleService;
import com.io.collige.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

@Component
@Slf4j
@SuppressWarnings("java:S1121")
public class LoginHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler, LogoutSuccessHandler {

    @Autowired
    private OAuth2AuthorizedClientService clientService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private TokenEncryptor tokenEncryptor;

    @Autowired
    private GoogleService googleService;

    @Value("${system.redirect.url}")
    private String redirectUri;

    public LoginHandler() {
        setUseReferer(true);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        HttpSession session = request.getSession(false);
        String authorizer = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        if (authentication.isAuthenticated()) {
            UserResponse userResponse;
            DefaultOAuth2User oicdUser = (DefaultOAuth2User) authentication.getPrincipal();
            switch (authorizer) {
                case "google" -> userResponse = checkAndLinkAccount(request, session, oicdUser, AppProviderType.GOOGLE);
                case "microsoft" -> userResponse = checkAndLinkAccount(request, session, oicdUser, AppProviderType.MICROSOFT);
                case "zoom" -> userResponse = checkAndLinkAccount(request, session, oicdUser, AppProviderType.ZOOM);
                default -> userResponse = new UserResponse();
            }
            getRedirectStrategy().sendRedirect(request, response,
                    String.format(redirectUri, userResponse.getToken().replace(jwtUtil.getTokenPrefix(), StringUtils.EMPTY)));
        }
    }

    private UserResponse checkAndLinkAccount(HttpServletRequest request, HttpSession session, DefaultOAuth2User oicdUser,
                                             AppProviderType type) {
        String userName;
        OAuth2AuthorizedClient oauth2User = clientService.loadAuthorizedClient(type.value, oicdUser.getName());
        Object sessionObject = session.getAttribute(request.getParameter("state"));
        try {
            if (sessionObject != null && !StringUtils.isBlank(sessionObject.toString())) {
                User user = jwtService.getUserFromToken(jwtUtil.getTokenPrefix() + sessionObject);
                SocialUser createRequest = createSocialRequest(user.getName(), oicdUser, oauth2User, type);
                createRequest.setSocialMediaMail(oicdUser.getAttribute(type.preferredUsername));
                userService.addNewLinkToUser(user, createRequest);
                userName = user.getEmail();
            } else {
                if (AppProviderType.ZOOM.equals(type)) {
                    throw new UnknownException(StringUtils.EMPTY);
                }
                userName = oicdUser.getAttribute(type.preferredUsername);
            }
            return createOrSaveUser(userName, oicdUser, oauth2User, type);
        } catch (Exception e) {
            e.printStackTrace();
            if (AppProviderType.GOOGLE.equals(type)) {
                googleService.revokeToken(Objects.requireNonNull(oauth2User.getRefreshToken()).getTokenValue());
            }
            throw new UnknownException(StringUtils.EMPTY);
        }
    }

    private UserResponse createOrSaveUser(String userName, DefaultOAuth2User oicdUser, OAuth2AuthorizedClient oauth2User, AppProviderType providerType) {
        UserResponse userResponse;
        if (userService.ifUserExist(userName)) {
            if (oauth2User.getRefreshToken() != null) {
                SocialUser createRequest = createSocialRequest(userName, oicdUser, oauth2User, providerType);
                userService.updateToken(createRequest);
            }
            userResponse = userService.findByMail(userName);
        } else {
            userResponse = createSocialSignup(userName, oicdUser, oauth2User, providerType);
        }
        return userResponse;
    }

    private UserResponse createSocialSignup(String userName, DefaultOAuth2User oicdUser, OAuth2AuthorizedClient user, AppProviderType providerType) {
        UserResponse userResponse;
        SocialUser createRequest = createSocialRequest(userName, oicdUser, user, providerType);
        createRequest.setSocialMediaMail(oicdUser.getAttribute(providerType.preferredUsername));
        userResponse = userService.socialSignUp(createRequest);
        return userResponse;
    }

    private SocialUser createSocialRequest(String userName, DefaultOAuth2User oicdUser, OAuth2AuthorizedClient user, AppProviderType providerType) {
        SocialUser createRequest = new SocialUser();
        createRequest.setEmail(userName);
        if (oicdUser.getAttributes().get("picture") != null) {
            createRequest.setPictureUrl(oicdUser.getAttributes().get("picture").toString());
        }
        if (AppProviderType.ZOOM.equals(providerType)) {
            createRequest.setName(oicdUser.getAttributes().get("first_name").toString() + " " +
                    oicdUser.getAttributes().get("last_name").toString());
        } else {
            createRequest.setName(oicdUser.getAttributes().get("name").toString());
        }
        createRequest.setPassword(UUID.randomUUID().toString());
        createRequest.setToken(tokenEncryptor.getEncryptedString(user.getAccessToken().getTokenValue()));
        createRequest.setRefreshToken(tokenEncryptor.getEncryptedString(Objects.requireNonNull(user.getRefreshToken()).getTokenValue()));
        createRequest.setExpireDate(LocalDateTime.ofInstant(Objects.requireNonNull(user.getAccessToken().getExpiresAt()), ZoneId.of("UTC")));
        createRequest.setSocialMediaMail(oicdUser.getAttribute(providerType.preferredUsername));
        createRequest.setType(providerType);
        return createRequest;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        log.info("Logout Success");
    }
}
