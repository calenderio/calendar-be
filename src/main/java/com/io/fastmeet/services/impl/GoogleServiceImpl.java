/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services.impl;

import com.google.gson.Gson;
import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.models.remotes.google.GoogleCalendarEventResponse;
import com.io.fastmeet.models.remotes.google.GoogleCalendarEventsRequest;
import com.io.fastmeet.models.remotes.google.TokenRefreshResponse;
import com.io.fastmeet.services.GoogleService;
import com.io.fastmeet.utils.GoogleApiURL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoogleServiceImpl implements GoogleService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Override
    public void getCalendarEvents(GoogleCalendarEventsRequest request) {
        UriComponentsBuilder builder = UriComponentsBuilder.
                fromUriString(String.format(GoogleApiURL.GET_CALENDAR_EVENTS, request.getUserName()));
        if (request.getTimeMax() != null) {
            builder.queryParam("timeMax", formatter.format(request.getTimeMax()));
        }
        if (request.getTimeMin() != null) {
            builder.queryParam("timeMin", formatter.format(request.getTimeMin()));
        }
        if (request.getTimeZone() != null) {
            builder.queryParam("timeZone", request.getTimeZone());
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(builder.build().toString()))
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header("Authorization",
                        "Bearer " + request.getAccessToken()
                )
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            GoogleCalendarEventResponse googleResponse = new Gson().fromJson(response.body(), GoogleCalendarEventResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public TokenRefreshResponse getNewAccessToken(String refreshToken) {
        TokenRefreshResponse result;
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
            result = new Gson().fromJson(response.body(), TokenRefreshResponse.class);
        } catch (Exception e) {
            throw new CalendarAppException(HttpStatus.UNAUTHORIZED, "JWT token is invalid", "JWT_TOKEN_INVALID");
        }
        return result;
    }

}
