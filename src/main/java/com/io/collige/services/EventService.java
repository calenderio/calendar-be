/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services;

import com.io.collige.entitites.Event;
import com.io.collige.models.internals.event.ResendInvitationRequest;
import com.io.collige.models.requests.events.EventCreateRequest;
import com.io.collige.models.requests.events.EventUpdateRequest;
import com.io.collige.models.requests.meet.SendInvitationRequest;
import com.io.collige.models.responses.calendar.EventTypeResponse;

import java.io.IOException;
import java.util.List;

public interface EventService {
    EventTypeResponse createEvent(EventCreateRequest request);

    EventTypeResponse updateEvent(EventUpdateRequest request);

    void deleteEvent(Long eventId);

    List<EventTypeResponse> getEvents();

    EventTypeResponse getEvent(Long eventId);

    void sendEventInvitation(SendInvitationRequest request) throws IOException;

    void resendInvitation(ResendInvitationRequest request) throws IOException;

    List<Event> findEventsByScheduler(Long schedulerId);

    void deleteEventsByScheduler(Long schedulerId);

    void deleteEventFileLinks(Long eventId);
}
