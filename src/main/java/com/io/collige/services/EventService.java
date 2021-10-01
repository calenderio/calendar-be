/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services;

import com.io.collige.entitites.Event;
import com.io.collige.models.internals.AttachmentModel;
import com.io.collige.models.internals.MeetInvitationDetailRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EventService {

    Event createEvent(Event event);

    @Transactional
    Event updateEvent(Event event, Long eventId);

    @Transactional
    void deleteEvent(Long eventId);

    List<Event> getEvents();

    void sendEventInvitation(MeetInvitationDetailRequest request);

    void resendInvitation(Long invitationId, List<AttachmentModel> attachments);

}
