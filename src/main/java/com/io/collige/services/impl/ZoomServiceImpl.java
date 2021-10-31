/**
 * @author : Oguz Kahraman
 * @since : 31 Eki 2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.services.impl;

import com.cloudinary.api.exceptions.BadRequest;
import com.google.gson.Gson;
import com.io.collige.constants.GeneralMessageConstants;
import com.io.collige.constants.GenericHttpRequestConstants;
import com.io.collige.constants.ZoomApiConstants;
import com.io.collige.core.exception.CalendarAppException;
import com.io.collige.core.i18n.Translator;
import com.io.collige.enums.AppProviderType;
import com.io.collige.models.remotes.google.TokenRefreshResponse;
import com.io.collige.models.remotes.zoom.ZoomLinkCreateRequest;
import com.io.collige.models.remotes.zoom.ZoomLinkResponse;
import com.io.collige.services.ZoomService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ZoomServiceImpl implements ZoomService {

    @Value("${spring.security.oauth2.client.registration.zoom.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.zoom.client-secret}")
    private String clientSecret;

    @Override
    public ZoomLinkResponse createZoomLink(ZoomLinkCreateRequest request, String accessToken) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(String.format(ZoomApiConstants.CREATE_MEETING)))
                .header(GenericHttpRequestConstants.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(GenericHttpRequestConstants.AUTHORIZATION, "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(request)))
                .build();
        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 201) {
                throw new BadRequest("Unknown");
            }
            return new Gson().fromJson(response.body(), ZoomLinkResponse.class);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageConstants.EXTERNAL_APP_MSG, AppProviderType.ZOOM.name()),
                    GeneralMessageConstants.EXTERNAL_APP);
        }
    }

    @Override
    public TokenRefreshResponse getNewAccessToken(String refreshToken) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("refresh_token", refreshToken);
        parameters.put("grant_type", "refresh_token");
        String form = parameters.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ZoomApiConstants.REFRESH_TOKEN))
                .header(GenericHttpRequestConstants.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(GenericHttpRequestConstants.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes()))
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), TokenRefreshResponse.class);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageConstants.EXTERNAL_APP_MSG, AppProviderType.ZOOM.name()),
                    GeneralMessageConstants.EXTERNAL_APP);
        }
    }
}
