/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers.impl;

import com.io.collige.controllers.EventController;
import com.io.collige.models.requests.events.EventCreateRequest;
import com.io.collige.models.requests.events.EventUpdateRequest;
import com.io.collige.models.responses.calendar.EventTypeResponse;
import com.io.collige.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class EventControllerImpl implements EventController {

    @Autowired
    private EventService eventService;

    @Override
    public ResponseEntity<EventTypeResponse> createEventType(@Valid EventCreateRequest request) {
        return ResponseEntity.ok(eventService.createEvent(request));
    }

    @Override
    public ResponseEntity<EventTypeResponse> updateEvent(@Valid EventCreateRequest request, Long eventId) {
        EventUpdateRequest updateRequest = new EventUpdateRequest();
        updateRequest.setDetails(request);
        updateRequest.setEventId(eventId);
        return ResponseEntity.ok(eventService.updateEvent(updateRequest));
    }

    @Override
    public ResponseEntity<Void> deleteEvent(Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<EventTypeResponse>> getEventTypes() {
        return ResponseEntity.ok(eventService.getEvents());
    }

    @Override
    public ResponseEntity<EventTypeResponse> getEventDetail(Long eventId) {
        return ResponseEntity.ok(eventService.getEvent(eventId));
    }

}