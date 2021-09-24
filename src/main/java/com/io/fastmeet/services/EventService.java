/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services;

import com.io.fastmeet.entitites.Event;
import com.io.fastmeet.models.internals.AttachmentModel;
import com.io.fastmeet.models.internals.MeetInvitationDetailRequest;

import java.util.List;

public interface EventService {

    Event createEvent(Event event);

    List<Event> getEvents();

    void sendEventInvitation(MeetInvitationDetailRequest request);

    void resendInvitation(Long invitationId, List<AttachmentModel> attachments);

}
