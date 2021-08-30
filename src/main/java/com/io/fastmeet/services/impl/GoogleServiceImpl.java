/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services.impl;

import com.google.gson.Gson;
import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.core.i18n.Translator;
import com.io.fastmeet.enums.CalendarProviderType;
import com.io.fastmeet.models.remotes.google.GoogleCalendarEventResponse;
import com.io.fastmeet.models.remotes.google.GoogleCalendarEventsRequest;
import com.io.fastmeet.models.remotes.google.TokenRefreshResponse;
import com.io.fastmeet.services.GoogleService;
import com.io.fastmeet.utils.GeneralMessageUtil;
import com.io.fastmeet.utils.GenericProviderUtil;
import com.io.fastmeet.utils.GoogleApiURL;
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
public class GoogleServiceImpl implements GoogleService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Override
    public void getCalendarEvents(GoogleCalendarEventsRequest request) {
        String timeZone = StringUtils.defaultIfBlank(request.getTimeZone(), "UTC");

        UriComponentsBuilder builder = UriComponentsBuilder.
                fromUriString(String.format(GoogleApiURL.GET_CALENDAR_EVENTS, request.getUserName()));
        if (request.getTimeMax() != null) {
            builder.queryParam("timeMax", formatter.format(ZonedDateTime.of(request.getTimeMax(), ZoneId.of(timeZone))).replace('+', '-'));
        }
        if (request.getTimeMin() != null) {
            builder.queryParam("timeMin", formatter.format(ZonedDateTime.of(request.getTimeMin(), ZoneId.of(timeZone))).replace('+', '-'));
        }
        if (request.getTimeZone() != null) {
            builder.queryParam("timeZone", timeZone);
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(builder.build().toString()))
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(GenericProviderUtil.AUTHORIZATION,
                        "Bearer " + request.getAccessToken()
                )
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            GoogleCalendarEventResponse googleResponse = new Gson().fromJson(response.body(), GoogleCalendarEventResponse.class);
        } catch (Exception e) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageUtil.EXTERNAL_APP_MSG, CalendarProviderType.GOOGLE.name()),
                    GeneralMessageUtil.EXTERNAL_APP);
        }
    }

    @Override
    public TokenRefreshResponse getNewAccessToken(String refreshToken) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", clientId);
        parameters.put("client_secret", clientSecret);
        parameters.put("grant_type", "refresh_token");
        parameters.put("refresh_token", refreshToken);
        String form = parameters.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GoogleApiURL.REFRESH_TOKEN))
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), TokenRefreshResponse.class);
        } catch (Exception e) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageUtil.EXTERNAL_APP_MSG, CalendarProviderType.GOOGLE.name()),
                    GeneralMessageUtil.EXTERNAL_APP);
        }
    }

    @Override
    public void revokeToken(String refreshToken) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format(GoogleApiURL.REVOKE_TOKEN, refreshToken)))
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageUtil.EXTERNAL_APP_MSG, CalendarProviderType.GOOGLE.name()),
                    GeneralMessageUtil.EXTERNAL_APP);
        }
    }

}
