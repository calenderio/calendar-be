/*
 * @author : Oguz Kahraman
 * @since : 31.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.io.collige.core.i18n.Translator;
import com.io.collige.models.remotes.google.TokenRefreshResponse;
import com.io.collige.models.remotes.zoom.ZoomLinkCreateRequest;
import com.io.collige.models.remotes.zoom.ZoomLinkResponse;
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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ZoomServiceImplTest {

    private static MockedStatic<Translator> translatorStatic;
    @InjectMocks
    private ZoomServiceImpl zoomService;
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

        translatorStatic = Mockito.mockStatic(Translator.class);
        translatorStatic.when(() -> Translator.getMessage(any())).thenReturn("Error");
    }

    @AfterAll
    void delete() {
        clientMockedStatic.close();
        translatorMockedStatic.close();
        translatorStatic.close();
    }

    @Test
    void createZoomLink() throws IOException, InterruptedException {
        ReflectionTestUtils.setField(zoomService, "clientId", "value");
        ReflectionTestUtils.setField(zoomService, "clientSecret", "value");
        ZoomLinkCreateRequest request = new ZoomLinkCreateRequest();
        request.setTopic("");
        request.setType(0);
        request.setStartTime("");
        request.setDuration(0);
        request.setTimezone("");
        request.setDefaultPassword(false);
        HttpResponse response = mock(HttpResponse.class);
        when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);
        when(response.statusCode()).thenReturn(201);
        when(builder.uri(any())).thenReturn(builder);
        when(builder.header(anyString(), anyString())).thenReturn(builder);
        when(builder.POST(any())).thenReturn(builder);
        when(builder.build()).thenReturn(mock(HttpRequest.class));
        ZoomLinkResponse serviceResponse = zoomService.createZoomLink(request, "example");
        assertNull(serviceResponse);
    }

    @Test
    void getNewAccessToken() throws IOException, InterruptedException {
        ReflectionTestUtils.setField(zoomService, "clientId", "value");
        ReflectionTestUtils.setField(zoomService, "clientSecret", "value");
        when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mock(HttpResponse.class));
        when(builder.uri(any())).thenReturn(builder);
        when(builder.header(anyString(), anyString())).thenReturn(builder);
        when(builder.POST(any())).thenReturn(builder);
        when(builder.build()).thenReturn(mock(HttpRequest.class));
        TokenRefreshResponse response = zoomService.getNewAccessToken("request");
        assertNull(response);
    }
}