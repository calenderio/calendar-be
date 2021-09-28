/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services;

import com.io.collige.entitites.Invitation;
import com.io.collige.models.internals.AttachmentModel;
import com.io.collige.models.internals.MeetInvitationDetailRequest;

import java.util.List;

public interface InvitationService {
    String saveInvitation(MeetInvitationDetailRequest request);

    Invitation findInvitationByUserIdAndCheckLimit(Long id, List<AttachmentModel> attachments);

    List<Invitation> findInvitations(Long eventId);

    void deleteInvitation(Long meetingId);

    void deleteInvitationByEvent(Long eventId);
}
