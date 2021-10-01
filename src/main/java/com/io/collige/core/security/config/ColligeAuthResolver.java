/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.core.security.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.io.collige.constants.GenericProviderConstants.APP_TOKEN;

public class ColligeAuthResolver implements OAuth2AuthorizationRequestResolver {
    private final OAuth2AuthorizationRequestResolver defaultAuthorizationRequestResolver;

    public ColligeAuthResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.defaultAuthorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        final OAuth2AuthorizationRequest authorizationRequest = this.defaultAuthorizationRequestResolver.resolve(request);
        return getAuth2AuthorizationRequest(request, authorizationRequest);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        final OAuth2AuthorizationRequest authorizationRequest = this.defaultAuthorizationRequestResolver.resolve(request, clientRegistrationId);
        return getAuth2AuthorizationRequest(request, authorizationRequest);
    }

    private OAuth2AuthorizationRequest getAuth2AuthorizationRequest(HttpServletRequest request, OAuth2AuthorizationRequest authorizationRequest) {
        String token = request.getParameter(APP_TOKEN) != null ? request.getParameter(APP_TOKEN) : StringUtils.EMPTY;
        if (authorizationRequest != null && authorizationRequest.getState() != null) {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute(authorizationRequest.getState(), token);
        }
        return authorizationRequest != null ? customAuthorizationRequest(token, authorizationRequest) : null;
    }

    private OAuth2AuthorizationRequest customAuthorizationRequest(String userToken, OAuth2AuthorizationRequest authorizationRequest) {
        Map<String, Object> additionalParameters = new LinkedHashMap<>(authorizationRequest.getAdditionalParameters());
        additionalParameters.put("access_type", "offline");
        additionalParameters.put("userToken", userToken);
        if ("microsoft".equals(authorizationRequest.getAttribute("registration_id"))) {
            additionalParameters.put("grant_type", "authorization_code");
        }
        return OAuth2AuthorizationRequest.from(authorizationRequest)
                .additionalParameters(additionalParameters)
                .build();
    }
}
