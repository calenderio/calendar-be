/**
 * @author : Oguz Kahraman
 * @since : 10/6/2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.controllers.impl;

import com.io.collige.controllers.InvitationController;
import com.io.collige.entitites.Invitation;
import com.io.collige.mappers.InvitationMapper;
import com.io.collige.models.internals.event.ResendInvitationRequest;
import com.io.collige.models.requests.meet.InvitationResendRequest;
import com.io.collige.models.requests.meet.SendInvitationRequest;
import com.io.collige.models.responses.meeting.InvitationResponse;
import com.io.collige.services.EventService;
import com.io.collige.services.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
public class InvitationControllerImpl implements InvitationController {

    @Autowired
    private EventService eventService;

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private InvitationMapper mapper;

    @Override
    public ResponseEntity<Void> sendInvitation(@Valid SendInvitationRequest request) throws IOException {
        eventService.sendEventInvitation(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> resendInvitation(Long invitationId, InvitationResendRequest request) throws IOException {
        eventService.resendInvitation(new ResendInvitationRequest(invitationId, request));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<InvitationResponse>> getInvitations(Long invitationId) {
        List<Invitation> invitations = invitationService.findInvitations(invitationId);
        return ResponseEntity.ok(mapper.mapEntityListToModelList(invitations));
    }

    @Override
    public ResponseEntity<Void> deleteInvitation(Long invitationId) {
        invitationService.deleteInvitation(invitationId);
        return ResponseEntity.noContent().build();
    }


}
