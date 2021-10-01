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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GoogleServiceImplTest {

    @InjectMocks
    private GoogleServiceImpl googleService;

    private HttpClient client = mock(HttpClient.class);
    private HttpRequest.Builder builder = mock(HttpRequest.Builder.class);
    private MockedStatic<HttpClient> clientMockedStatic;
    private MockedStatic<HttpRequest> translatorMockedStatic;

    @BeforeAll
    void setUp() {
        clientMockedStatic = Mockito.mockStatic(HttpClient.class);
        clientMockedStatic.when(HttpClient::newHttpClient).thenReturn(client);

        translatorMockedStatic = Mockito.mockStatic(HttpRequest.class);
        translatorMockedStatic.when(HttpRequest::newBuilder).thenReturn(builder);
    }

    @AfterAll
    void delete() {
        clientMockedStatic.close();
        translatorMockedStatic.close();
    }

    @Test
    void getCalendarEvents() throws IOException, InterruptedException {
        ReflectionTestUtils.setField(googleService, "clientId", "value");
        ReflectionTestUtils.setField(googleService, "clientSecret", "value");
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
        assertNull(response);
    }

    @Test
    void getNewAccessToken() throws IOException, InterruptedException {
        ReflectionTestUtils.setField(googleService, "clientId", "value");
        ReflectionTestUtils.setField(googleService, "clientSecret", "value");
        when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mock(HttpResponse.class));
        when(builder.uri(any())).thenReturn(builder);
        when(builder.header(anyString(), anyString())).thenReturn(builder);
        when(builder.POST(any())).thenReturn(builder);
        when(builder.build()).thenReturn(mock(HttpRequest.class));
        TokenRefreshResponse response = googleService.getNewAccessToken("request");
        assertNull(response);
    }

    @Test
    void revokeToken() {
        ReflectionTestUtils.setField(googleService, "clientId", "value");
        ReflectionTestUtils.setField(googleService, "clientSecret", "value");
        when(builder.uri(any())).thenReturn(builder);
        when(builder.header(anyString(), anyString())).thenReturn(builder);
        when(builder.POST(any())).thenReturn(builder);
        when(builder.build()).thenReturn(mock(HttpRequest.class));
        googleService.revokeToken("request");
        assertTrue(Boolean.TRUE);
    }
}