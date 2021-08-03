/*
 * @author : Oguz Kahraman
 * @since : 3.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.core.security.handler;

import com.google.gson.Gson;
import com.io.fastmeet.core.security.jwt.JWTService;
import com.io.fastmeet.models.responses.user.UserResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler, LogoutSuccessHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final OAuth2AuthorizedClientService clientService;
    private final JWTService jwtService;

    public LoginHandler(OAuth2AuthorizedClientService clientService, JWTService jwtService) {
        this.clientService = clientService;
        this.jwtService = jwtService;
        setUseReferer(true);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String userName;
        UserResponse userResponse = new UserResponse();
        if (request.getParameter("scope") != null &&
                request.getParameter("scope").contains("google")) {
            DefaultOAuth2User oicdUser = (DefaultOAuth2User) authentication.getPrincipal();
            OAuth2AuthorizedClient user = clientService.loadAuthorizedClient("google", oicdUser.getName());
            OAuth2RefreshToken refreshToken = user.getRefreshToken();
            userName = oicdUser.getAttribute("email");
            userResponse.setToken(jwtService.createToken(userName, 1L));
        }
        response.getWriter().write(new Gson().toJson(userResponse));
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

    }
}
