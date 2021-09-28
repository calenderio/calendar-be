/*
 * @author : Oguz Kahraman
 * @since : 5.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.google.gson.Gson;
import com.io.collige.constants.GeneralMessageConstants;
import com.io.collige.constants.GenericHttpRequestConstants;
import com.io.collige.constants.GenericProviderConstants;
import com.io.collige.constants.MicrosoftGraphURLConstants;
import com.io.collige.core.exception.CalendarAppException;
import com.io.collige.core.i18n.Translator;
import com.io.collige.enums.AppProviderType;
import com.io.collige.models.remotes.google.TokenRefreshResponse;
import com.io.collige.models.remotes.microsoft.MicrosoftCalendarResponse;
import com.io.collige.models.remotes.microsoft.MicrosoftCalendarEventsRequest;
import com.io.collige.services.MicrosoftService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MicrosoftServiceImpl implements MicrosoftService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    @Value("${spring.security.oauth2.client.registration.microsoft.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.microsoft.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.microsoft.scope}")
    private String scopes;
    @Value("${spring.security.oauth2.client.registration.microsoft.redirect-uri}")
    private String redirectUri;

    @Override
    public MicrosoftCalendarResponse getCalendarEvents(MicrosoftCalendarEventsRequest request) {
        String timeZone = StringUtils.defaultIfBlank(request.getTimeZone(), "UTC");
        UriComponentsBuilder builder;
        if (request.getTimeMin() == null || request.getTimeMax() == null) {
            builder = UriComponentsBuilder.
                    fromUriString(String.format(MicrosoftGraphURLConstants.CALENDAR_ALL_URL));
        } else {
            builder = UriComponentsBuilder.
                    fromUriString(String.format(MicrosoftGraphURLConstants.CALENDAR_URL));
            if (request.getTimeMax() != null) {
                builder.queryParam("enddatetime", formatter.format(ZonedDateTime.of(request.getTimeMax(), ZoneId.of(timeZone)))
                        .replace('+', '-'));
            }
            if (request.getTimeMin() != null) {
                builder.queryParam("startdatetime", formatter.format(ZonedDateTime.of(request.getTimeMin(), ZoneId.of(timeZone)))
                        .replace('+', '-'));
            }
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(builder.build().toString()))
                .header(GenericProviderConstants.AUTHORIZATION,
                        "Bearer " + request.getAccessToken()
                )
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), MicrosoftCalendarResponse.class);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageConstants.EXTERNAL_APP_MSG, AppProviderType.GOOGLE.name()),
                    GeneralMessageConstants.EXTERNAL_APP);
        }
    }

    @Override
    public TokenRefreshResponse getNewAccessToken(String refreshToken) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", clientId);
        parameters.put("client_secret", clientSecret);
        parameters.put("grant_type", "refresh_token");
        parameters.put("scope", scopes);
        parameters.put("refresh_token", refreshToken);
        parameters.put("redirect_uri", redirectUri);
        String form = parameters.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MicrosoftGraphURLConstants.REFRESH_TOKEN))
                .header(GenericHttpRequestConstants.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), TokenRefreshResponse.class);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageConstants.EXTERNAL_APP_MSG, AppProviderType.GOOGLE.name()),
                    GeneralMessageConstants.EXTERNAL_APP);
        }
    }

}
