/*
 * @author : Oguz Kahraman
 * @since : 27.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers.impl;

import com.io.collige.entitites.Event;
import com.io.collige.entitites.Scheduler;
import com.io.collige.mappers.EventMapper;
import com.io.collige.mappers.SchedulerMapper;
import com.io.collige.models.internals.SchedulerDetails;
import com.io.collige.models.internals.SchedulerObject;
import com.io.collige.models.requests.calendar.EventTypeCreateRequest;
import com.io.collige.models.responses.calendar.EventTypeResponse;
import com.io.collige.services.EventService;
import com.io.collige.services.FileService;
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

    @Mock
    private EventMapper eventMapper;

    @Mock
    private SchedulerMapper schedulerMapper;

    @Mock
    private FileService fileService;

    @InjectMocks
    private EventControllerImpl eventController;

    @Test
    void createEventType() {
        EventTypeCreateRequest request = new EventTypeCreateRequest();
        request.setSchedule(new SchedulerDetails());
        request.setTimezone("UTC");
        when(eventMapper.mapRequestToEntity(any())).thenReturn(new Event());
        when(eventService.createEvent(any())).thenReturn(new Event());
        when(eventMapper.mapEntityToModel(any())).thenReturn(new EventTypeResponse());
        when(schedulerMapper.mapEntityToModel(any())).thenReturn(new SchedulerObject());
        ResponseEntity<EventTypeResponse> responseEntity = eventController.createEventType(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void updateEvent() {
        EventTypeCreateRequest request = new EventTypeCreateRequest();
        request.setSchedule(new SchedulerDetails());
        request.setTimezone("UTC");
        when(eventMapper.mapRequestToEntity(any())).thenReturn(new Event());
        when(eventService.updateEvent(any())).thenReturn(new Event());
        when(eventMapper.mapEntityToModel(any())).thenReturn(new EventTypeResponse());
        when(schedulerMapper.mapEntityToModel(any())).thenReturn(new SchedulerObject());
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
        List<Event> eventList = new ArrayList<>();
        Event event = new Event();
        event.setScheduler(new Scheduler());
        eventList.add(event);
        when(eventService.getEvents()).thenReturn(eventList);
        when(eventMapper.mapEntityToModel(any())).thenReturn(new EventTypeResponse());
        when(schedulerMapper.mapEntityToModel(any())).thenReturn(new SchedulerObject());
        ResponseEntity<List<EventTypeResponse>> responseEntity = eventController.getEventTypes();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getEventDetail() {
        Event event = new Event();
        event.setScheduler(new Scheduler());
        when(eventService.getEvent(1L)).thenReturn(event);
        when(eventMapper.mapEntityToModel(any())).thenReturn(new EventTypeResponse());
        when(schedulerMapper.mapEntityToModel(any())).thenReturn(new SchedulerObject());
        ResponseEntity<EventTypeResponse> responseEntity = eventController.getEventDetail(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}