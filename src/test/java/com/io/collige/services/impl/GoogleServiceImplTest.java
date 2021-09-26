/*
 * @author : Oguz Kahraman
 * @since : 26.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.io.collige.models.remotes.google.GoogleCalendarEventResponse;
import com.io.collige.models.remotes.google.GoogleCalendarEventsRequest;
import com.io.collige.models.remotes.google.TokenRefreshResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleServiceImplTest {

    @InjectMocks
    private GoogleServiceImpl googleService;

    private HttpClient client = mock(HttpClient.class);
    private HttpRequest.Builder builder = mock(HttpRequest.Builder.class);

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(googleService, "clientId", "value");
        ReflectionTestUtils.setField(googleService, "clientSecret", "value");

        try (MockedStatic<HttpClient> clientMockedStatic = Mockito.mockStatic(HttpClient.class)) {
            clientMockedStatic.when(HttpClient::newHttpClient).thenReturn(client);
        }
        try (MockedStatic<HttpRequest> translatorMockedStatic = Mockito.mockStatic(HttpRequest.class)) {
            translatorMockedStatic.when(HttpRequest::newBuilder).thenReturn(builder);
        }

    }

    @Test
    void getCalendarEvents() throws IOException, InterruptedException {
        GoogleCalendarEventsRequest request = new GoogleCalendarEventsRequest();
        request.setTimeMax(LocalDateTime.MAX);
        request.setTimeMin(LocalDateTime.MIN);
        request.setTimeZone("UTC");
        request.setAccessToken("123");
        when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mock(HttpResponse.class));
        when(builder.uri(any())).thenReturn(builder);
        when(builder.header(anyString(), anyString())).thenReturn(builder);
        when(builder.GET()).thenReturn(builder);
        when(builder.build()).thenReturn(mock(HttpRequest.class));
        GoogleCalendarEventResponse response = googleService.getCalendarEvents(request);
        assertNull(response.getItems());
    }

    @Test
    void getNewAccessToken() throws IOException, InterruptedException {
        when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mock(HttpResponse.class));
        when(builder.uri(any())).thenReturn(builder);
        when(builder.header(anyString(), anyString())).thenReturn(builder);
        when(builder.POST(any())).thenReturn(builder);
        when(builder.build()).thenReturn(mock(HttpRequest.class));
        TokenRefreshResponse response = googleService.getNewAccessToken("request");
        assertNull(response.getAccessToken());
    }

    @Test
    void revokeToken() {
        when(builder.uri(any())).thenReturn(builder);
        when(builder.header(anyString(), anyString())).thenReturn(builder);
        when(builder.POST(any())).thenReturn(builder);
        when(builder.build()).thenReturn(mock(HttpRequest.class));
        googleService.revokeToken("request");
        assertTrue(Boolean.TRUE);
    }
}