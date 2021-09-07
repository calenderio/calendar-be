/*
 * @author : Oguz Kahraman
 * @since : 3.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.core.security.handler;

import com.io.fastmeet.core.exception.UnknownException;
import com.io.fastmeet.core.security.encrypt.TokenEncryptor;
import com.io.fastmeet.core.security.jwt.JWTService;
import com.io.fastmeet.core.security.jwt.JWTUtil;
import com.io.fastmeet.entitites.User;
import com.io.fastmeet.enums.CalendarProviderType;
import com.io.fastmeet.models.internals.requests.SocialUserCreateRequest;
import com.io.fastmeet.models.responses.user.UserResponse;
import com.io.fastmeet.services.GoogleService;
import com.io.fastmeet.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
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
        String userName;
        HttpSession session = request.getSession(false);
        String authorizer = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        if (authentication.isAuthenticated()) {
            UserResponse userResponse;
            DefaultOAuth2User oicdUser = (DefaultOAuth2User) authentication.getPrincipal();
            if (authorizer.equals("google")) {
                OAuth2AuthorizedClient oauth2User = clientService.loadAuthorizedClient("google", oicdUser.getName());
                userName = checkAndLinkAccount(request, session, oicdUser, CalendarProviderType.GOOGLE, oauth2User, "email");
                userResponse = createOrSaveUser(userName, oicdUser, oauth2User, CalendarProviderType.GOOGLE);
            } else if (authorizer.equals("microsoft")) {
                OAuth2AuthorizedClient oauth2User = clientService.loadAuthorizedClient("microsoft", oicdUser.getName());
                userName = checkAndLinkAccount(request, session, oicdUser, CalendarProviderType.MICROSOFT, oauth2User, "preferred_username");
                userResponse = createOrSaveUser(userName, oicdUser, oauth2User, CalendarProviderType.MICROSOFT);
            } else {
                userResponse = new UserResponse();
            }
            getRedirectStrategy().sendRedirect(request, response,
                    String.format(redirectUri, userResponse.getToken().replace(jwtUtil.getTokenPrefix(), StringUtils.EMPTY)));
        }
    }

    private String checkAndLinkAccount(HttpServletRequest request, HttpSession session, DefaultOAuth2User oicdUser, CalendarProviderType type,
                                       OAuth2AuthorizedClient oauth2User, String preferredUsername) {
        String userName;
        Object sessionObject = session.getAttribute(request.getParameter("state"));
        if (sessionObject != null && !StringUtils.isBlank(sessionObject.toString())) {
            try {
                User user = jwtService.getUserFromToken(jwtUtil.getTokenPrefix() + sessionObject);
                SocialUserCreateRequest createRequest = createSocialRequest(user.getName(), oicdUser, oauth2User, type);
                createRequest.setSocialMediaMail(oicdUser.getAttribute(preferredUsername));
                userService.addNewLinkToUser(user, createRequest);
                userName = user.getEmail();
            } catch (ExpiredJwtException e) {
                googleService.revokeToken(Objects.requireNonNull(oauth2User.getRefreshToken()).getTokenValue());
                throw new UnknownException(StringUtils.EMPTY);
            }
        } else {
            userName = oicdUser.getAttribute(preferredUsername);
        }
        return userName;
    }

    private UserResponse createOrSaveUser(String userName, DefaultOAuth2User oicdUser, OAuth2AuthorizedClient oauth2User, CalendarProviderType google) {
        UserResponse userResponse;
        if (userService.ifUserExist(userName)) {
            userResponse = userService.findByMail(userName);
        } else {
            userResponse = createSocialSignup(userName, oicdUser, oauth2User, google);
        }
        return userResponse;
    }

    private UserResponse createSocialSignup(String userName, DefaultOAuth2User oicdUser, OAuth2AuthorizedClient user, CalendarProviderType providerType) {
        UserResponse userResponse;
        SocialUserCreateRequest createRequest = createSocialRequest(userName, oicdUser, user, providerType);
        createRequest.setSocialMediaMail(userName);
        userResponse = userService.socialSignUp(createRequest);
        return userResponse;
    }

    private SocialUserCreateRequest createSocialRequest(String userName, DefaultOAuth2User oicdUser, OAuth2AuthorizedClient user, CalendarProviderType providerType) {
        SocialUserCreateRequest createRequest = new SocialUserCreateRequest();
        createRequest.setEmail(userName);
        if (oicdUser.getAttributes().get("picture") != null) {
            createRequest.setPictureUrl(oicdUser.getAttributes().get("picture").toString());
        }
        createRequest.setName(oicdUser.getAttributes().get("name").toString());
        createRequest.setPassword(UUID.randomUUID().toString());
        createRequest.setToken(tokenEncryptor.getEncryptedString(user.getAccessToken().getTokenValue()));
        createRequest.setRefreshToken(tokenEncryptor.getEncryptedString(Objects.requireNonNull(user.getRefreshToken()).getTokenValue()));
        createRequest.setExpireDate(LocalDateTime.ofInstant(Objects.requireNonNull(user.getAccessToken().getExpiresAt()), ZoneId.of("UTC")));
        createRequest.setType(providerType);
        return createRequest;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        log.info("Logout Success");
    }
}
