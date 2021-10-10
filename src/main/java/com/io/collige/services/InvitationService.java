/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services;

import com.io.collige.entitites.Invitation;
import com.io.collige.models.requests.meet.SendInvitationRequest;

import java.util.List;

public interface InvitationService {
    String saveInvitation(SendInvitationRequest request);

    Invitation findInvitationByUserIdAndCheckLimit(Long id);

    List<Invitation> findInvitations(Long eventId);

    void deleteInvitation(Long meetingId);

    void deleteInvitationByEvent(Long eventId);
}
