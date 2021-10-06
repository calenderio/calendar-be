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
import com.io.collige.models.internals.AttachmentModel;
import com.io.collige.models.internals.MeetInvitationDetailRequest;
import com.io.collige.models.requests.meet.MeetInvitationRequest;
import com.io.collige.models.responses.meeting.InvitationResponse;
import com.io.collige.services.EventService;
import com.io.collige.services.InvitationService;
import com.io.collige.utils.AttachmentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
public class InvitationControllerImpl implements InvitationController {

    @Autowired
    private EventService eventService;

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private InvitationMapper mapper;

    @Autowired
    private AttachmentUtil attachmentUtil;

    @Override
    public ResponseEntity<Void> sendMeetingInvite(@Valid MeetInvitationRequest request, List<MultipartFile> files) {
        List<AttachmentModel> modelList = attachmentUtil.checkAttachments(files);
        eventService.sendEventInvitation(new MeetInvitationDetailRequest(request.getUserMail(), request.getName(), request.getTitle(), request.getDescription()
                , request.getEventId(), modelList, request.getCcUsers(), request.getBccUsers()));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> resendMeeting(Long meetingId, List<MultipartFile> files) {
        List<AttachmentModel> modelList = attachmentUtil.checkAttachments(files);
        eventService.resendInvitation(meetingId, modelList);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<InvitationResponse>> getMeetings(Long eventId) {
        List<Invitation> invitations = invitationService.findInvitations(eventId);
        return ResponseEntity.ok(mapper.mapEntityListToModelList(invitations));
    }

    @Override
    public ResponseEntity<Void> deleteInvitation(Long invitationId) {
        invitationService.deleteInvitation(invitationId);
        return ResponseEntity.noContent().build();
    }


}
