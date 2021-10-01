/*
 * @author : Oguz Kahraman
 * @since : 28.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers.impl;

import com.io.collige.models.requests.calendar.UserCalendarItemsRequest;
import com.io.collige.models.responses.calendar.CalendarItemResponse;
import com.io.collige.models.responses.calendar.CalendarResponse;
import com.io.collige.services.CalendarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalendarControllerImplTest {

    @Mock
    private CalendarService calendarService;

    @InjectMocks
    private CalendarControllerImpl calendarController;

    @Test
    void getUserCalendars() {
        CalendarResponse response = new CalendarResponse();
        response.setItems(Collections.singletonList(new CalendarItemResponse()));
        when(calendarService.getAllCalendars(any())).thenReturn(response);
        ResponseEntity<CalendarResponse> responseEntity = calendarController.getUserCalendars(new UserCalendarItemsRequest());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, Objects.requireNonNull(responseEntity.getBody()).getItems().size());
    }
}