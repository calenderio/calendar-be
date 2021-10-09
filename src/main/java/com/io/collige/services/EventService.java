/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services;

import com.io.collige.entitites.Event;
import com.io.collige.models.internals.CreateEventRequest;
import com.io.collige.models.internals.MeetInvitationDetailRequest;
import com.io.collige.models.internals.UpdateEventRequest;
import com.io.collige.models.requests.meet.InvitationResendRequest;

import java.io.IOException;
import java.util.List;

public interface EventService {

    Event createEvent(CreateEventRequest request);

    Event updateEvent(UpdateEventRequest request);

    void deleteEvent(Long eventId);

    List<Event> getEvents();

    Event getEvent(Long eventId);

    void sendEventInvitation(MeetInvitationDetailRequest request) throws IOException;

    void resendInvitation(Long invitationId, InvitationResendRequest request) throws IOException;

    List<Event> findEventsByScheduler(Long schedulerId);

    void deleteEventsByScheduler(Long schedulerId);

    void deleteEventFileLinks(Long eventId);
}
