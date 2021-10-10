/*
 * @author : Oguz Kahraman
 * @since : 27.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers.impl;

import com.io.collige.models.internals.scheduler.SchedulerDetails;
import com.io.collige.models.internals.scheduler.SchedulerObject;
import com.io.collige.models.requests.events.EventCreateRequest;
import com.io.collige.models.responses.calendar.EventTypeResponse;
import com.io.collige.services.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventControllerImplTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventControllerImpl eventController;

    @Test
    void createEventType() {
        EventCreateRequest request = new EventCreateRequest();
        request.setSchedule(new SchedulerDetails());
        request.setTimezone("UTC");
        when(eventService.createEvent(any())).thenReturn(new EventTypeResponse());
        ResponseEntity<EventTypeResponse> responseEntity = eventController.createEventType(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void updateEvent() {
        EventCreateRequest request = new EventCreateRequest();
        request.setSchedule(new SchedulerDetails());
        request.setTimezone("UTC");
        when(eventService.updateEvent(any())).thenReturn(new EventTypeResponse());
        ResponseEntity<EventTypeResponse> responseEntity = eventController.updateEvent(request, 1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void deleteEvent() {
        ResponseEntity<Void> responseEntity = eventController.deleteEvent(1L);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(eventService, times(1)).deleteEvent(1L);
    }

    @Test
    void getEventTypes() {
        List<EventTypeResponse> eventList = new ArrayList<>();
        EventTypeResponse event = new EventTypeResponse();
        event.setSchedule(new SchedulerObject());
        eventList.add(event);
        when(eventService.getEvents()).thenReturn(eventList);
        ResponseEntity<List<EventTypeResponse>> responseEntity = eventController.getEventTypes();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getEventDetail() {
        EventTypeResponse event = new EventTypeResponse();
        event.setSchedule(new SchedulerObject());
        when(eventService.getEvent(1L)).thenReturn(event);
        ResponseEntity<EventTypeResponse> responseEntity = eventController.getEventDetail(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}