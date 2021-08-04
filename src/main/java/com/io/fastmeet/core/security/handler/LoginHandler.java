/*
 * @author : Oguz Kahraman
 * @since : 3.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.core.security.handler;

import com.google.gson.Gson;
import com.io.fastmeet.core.security.jwt.JWTService;
import com.io.fastmeet.models.requests.user.UserCreateRequest;
import com.io.fastmeet.models.responses.user.UserResponse;
import com.io.fastmeet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
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
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    public LoginHandler() {
        setUseReferer(true);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String userName;
        UserResponse userResponse = new UserResponse();
        if (authentication.isAuthenticated()) {
            if (request.getParameter("scope") != null &&
                    request.getParameter("scope").contains("google")) {
                DefaultOAuth2User oicdUser = (DefaultOAuth2User) authentication.getPrincipal();
                OAuth2AuthorizedClient user = clientService.loadAuthorizedClient("google", oicdUser.getName());
                userName = oicdUser.getAttribute("email");
                if (userService.ifUserExist(userName)) {
                    userResponse = userService.findByMail(userName);
                } else {
                    UserCreateRequest createRequest = new UserCreateRequest();
                    createRequest.setEmail(userName);
                    createRequest.setName(oicdUser.getAttributes().get("name").toString());
                    createRequest.setPassword(UUID.randomUUID().toString());
                    userResponse = userService.createIndividualUser(createRequest);
                    OAuth2RefreshToken refreshToken = user.getRefreshToken();
                }
            }
            response.getWriter().write(new Gson().toJson(userResponse));
        }
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

    }
}
