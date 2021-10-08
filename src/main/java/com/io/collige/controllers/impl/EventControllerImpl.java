/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers.impl;

import com.io.collige.controllers.EventController;
import com.io.collige.entitites.Event;
import com.io.collige.mappers.EventMapper;
import com.io.collige.mappers.SchedulerMapper;
import com.io.collige.models.internals.CreateEventRequest;
import com.io.collige.models.internals.UpdateEventRequest;
import com.io.collige.models.requests.calendar.EventTypeCreateRequest;
import com.io.collige.models.responses.calendar.EventTypeResponse;
import com.io.collige.services.EventService;
import com.io.collige.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class EventControllerImpl implements EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private SchedulerMapper schedulerMapper;

    @Autowired
    private FileService fileService;

    @Override
    public ResponseEntity<EventTypeResponse> createEventType(@Valid EventTypeCreateRequest request) {
        Event event = eventMapper.mapRequestToEntity(request);
        event.setScheduler(schedulerMapper.mapDetailsToEntity(request.getSchedule(), request.getTimezone()));
        Event event1 = eventService.createEvent(new CreateEventRequest(event, request.getFileLinks()));
        EventTypeResponse response = eventMapper.mapEntityToModel(event1);
        response.setSchedule(schedulerMapper.mapEntityToModel(event1.getScheduler()));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<EventTypeResponse> updateEvent(@Valid EventTypeCreateRequest request, Long eventId) {
        Event event = eventMapper.mapRequestToEntity(request);
        event.setScheduler(schedulerMapper.mapDetailsToEntity(request.getSchedule(), request.getTimezone()));
        Event event1 = eventService.updateEvent(new UpdateEventRequest(event, request.getFileLinks(), eventId));
        EventTypeResponse response = eventMapper.mapEntityToModel(event1);
        response.setSchedule(schedulerMapper.mapEntityToModel(event1.getScheduler()));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteEvent(Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<EventTypeResponse>> getEventTypes() {
        List<Event> list = eventService.getEvents();
        List<EventTypeResponse> responseList = new ArrayList<>();
        for (Event event : list) {
            EventTypeResponse response = eventMapper.mapEntityToModel(event);
            response.setSchedule(schedulerMapper.mapEntityToModel(event.getScheduler()));
            responseList.add(response);
        }
        return ResponseEntity.ok(responseList);
    }

    @Override
    public ResponseEntity<EventTypeResponse> getEventDetail(Long eventId) {
        Event event = eventService.getEvent(eventId);
        EventTypeResponse response = eventMapper.mapEntityToModel(event);
        response.setSchedule(schedulerMapper.mapEntityToModel(event.getScheduler()));
        response.setFileList(fileService.getEventFiles(eventId));
        return ResponseEntity.ok(response);
    }

}