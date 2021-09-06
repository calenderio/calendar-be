/*
 * @author : Oguz Kahraman
 * @since : 3.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.core.security.handler;

import com.google.gson.Gson;
import com.io.fastmeet.enums.CalendarProviderType;
import com.io.fastmeet.models.internals.requests.SocialUserCreateRequest;
import com.io.fastmeet.models.responses.user.UserResponse;
import com.io.fastmeet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class LoginHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler, LogoutSuccessHandler {

    @Autowired
    private OAuth2AuthorizedClientService clientService;

    @Autowired
    private UserService userService;

    public LoginHandler() {
        setUseReferer(true);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String userName;
        UserResponse userResponse = new UserResponse();
        String authorizer = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        if (authentication.isAuthenticated()) {
            DefaultOAuth2User oicdUser = (DefaultOAuth2User) authentication.getPrincipal();
            if (authorizer.equals("google")) {
                userName = oicdUser.getAttribute("email");
                OAuth2AuthorizedClient user = clientService.loadAuthorizedClient("google", oicdUser.getName());
                if (userService.ifUserExist(userName)) {
                    userResponse = userService.findByMail(userName);
                } else {
                    userResponse = createSocialSignup(userName, oicdUser, user, CalendarProviderType.GOOGLE);
                }
            } else if (authorizer.equals("microsoft")) {
                userName = oicdUser.getAttribute("preferred_username");
                OAuth2AuthorizedClient user = clientService.loadAuthorizedClient("microsoft", oicdUser.getName());
                if (userService.ifUserExist(userName)) {
                    userResponse = userService.findByMail(userName);
                } else {
                    userResponse = createSocialSignup(userName, oicdUser, user, CalendarProviderType.MICROSOFT);
                }
            }
            response.getWriter().write(new Gson().toJson(userResponse));
        }
    }

    private UserResponse createSocialSignup(String userName, DefaultOAuth2User oicdUser, OAuth2AuthorizedClient user, CalendarProviderType providerType) {
        UserResponse userResponse;
        SocialUserCreateRequest createRequest = new SocialUserCreateRequest();
        createRequest.setEmail(userName);
        createRequest.setName(oicdUser.getAttributes().get("name").toString());
        createRequest.setPassword(UUID.randomUUID().toString());
        createRequest.setToken(user.getAccessToken().getTokenValue());
        createRequest.setRefreshToken(user.getAccessToken().getTokenValue());
        createRequest.setType(providerType);
        userResponse = userService.socialSignUp(createRequest);
        return userResponse;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

    }
}
