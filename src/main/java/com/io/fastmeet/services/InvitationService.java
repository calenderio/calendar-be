/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services;

import com.io.fastmeet.entitites.Invitation;
import com.io.fastmeet.models.internals.AttachmentModel;
import com.io.fastmeet.models.internals.MeetInvitationDetailRequest;

import java.util.List;

public interface InvitationService {
    String saveInvitation(MeetInvitationDetailRequest request);

    Invitation findInvitationByUserId(Long id, List<AttachmentModel> attachments);

    List<Invitation> findInvitations(Long eventId);

    void deleteInvitation(Long meetingId);

    void deleteInvitationByEvent(Long eventId);
}
