/*
 * @author : Oguz Kahraman
 * @since : 7.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers.impl;

import com.io.collige.controllers.MeetController;
import com.io.collige.core.exception.CalendarAppException;
import com.io.collige.entitites.Invitation;
import com.io.collige.mappers.InvitationMapper;
import com.io.collige.mappers.MeetingMapper;
import com.io.collige.models.internals.AttachmentModel;
import com.io.collige.models.internals.AvailableDatesDetails;
import com.io.collige.models.internals.MeetInvitationDetailRequest;
import com.io.collige.models.internals.ScheduleMeetingDetails;
import com.io.collige.models.requests.calendar.ScheduleMeetingRequest;
import com.io.collige.models.requests.meet.MeetInvitationRequest;
import com.io.collige.models.requests.meet.MeetingDateRequest;
import com.io.collige.models.responses.meeting.InvitationResponse;
import com.io.collige.models.responses.meeting.ScheduledMeetingResponse;
import com.io.collige.services.CalendarService;
import com.io.collige.services.EventService;
import com.io.collige.services.InvitationService;
import com.io.collige.services.MeetingService;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class MeetControllerImpl implements MeetController {

    @Autowired
    private EventService eventService;

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private InvitationMapper mapper;

    @Autowired
    private MeetingMapper meetingMapper;

    @Value("${spring.allowed.file.extensions}")
    private List<String> allowedFileTypes;

    @Override
    public ResponseEntity<Void> sendMeetingInvite(@Valid MeetInvitationRequest request, List<MultipartFile> files) {
        List<AttachmentModel> modelList = checkAttachments(files);
        eventService.sendEventInvitation(new MeetInvitationDetailRequest(request.getUserMail(), request.getName(), request.getTitle(), request.getDescription()
                , request.getEventId(), modelList, request.getCcUsers(), request.getBccUsers()));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> resendMeeting(Long meetingId, List<MultipartFile> files) {
        List<AttachmentModel> modelList = checkAttachments(files);
        eventService.resendInvitation(meetingId, modelList);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<InvitationResponse>> getMeetings(Long eventId) {
        List<Invitation> invitations = invitationService.findInvitations(eventId);
        return ResponseEntity.ok(mapper.mapEntityListToModelList(invitations));
    }

    @Override
    public ResponseEntity<Void> deleteMapping(Long meetingId) {
        invitationService.deleteInvitation(meetingId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ScheduledMeetingResponse> getAvailableDates(@Valid MeetingDateRequest request) {
        AvailableDatesDetails details = calendarService.getAvailableDates(request.getLocalDate(), request.getInvitationId(), request.getTimeZone());
        return ResponseEntity.ok(meetingMapper.detailsToModel(details));
    }

    @Override
    public ResponseEntity<Void> scheduleMeeting(@Valid ScheduleMeetingRequest request, String invitationId, MultipartFile files) {
        ScheduleMeetingDetails details = setDetails(request, invitationId, files);
        meetingService.validateAndScheduleMeeting(details);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> updateMeeting(@Valid ScheduleMeetingRequest request, String invitationId, MultipartFile files) {
        ScheduleMeetingDetails details = setDetails(request, invitationId, files);
        meetingService.updateMeetingRequest(details);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteMeeting(String invitationId) {
        ScheduleMeetingDetails details = new ScheduleMeetingDetails();
        details.setInvitationId(invitationId);
        meetingService.deleteMeetingRequest(details);
        return ResponseEntity.noContent().build();
    }

    private ScheduleMeetingDetails setDetails(ScheduleMeetingRequest request, String invitationId, MultipartFile files) {
        ScheduleMeetingDetails details = new ScheduleMeetingDetails();
        details.setRequest(request);
        details.setInvitationId(invitationId);
        if (files != null) {
            List<AttachmentModel> modelList = checkAttachments(Collections.singletonList(files));
            details.getModels().add(modelList.get(0));
        }
        return details;
    }

    private List<AttachmentModel> checkAttachments(List<MultipartFile> files) {
        Tika tika = new Tika();
        List<AttachmentModel> modelList = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                String type;
                try {
                    type = tika.detect(file.getBytes());
                } catch (Exception e) {
                    throw new CalendarAppException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception", "EXCEPTION");
                }
                if (!allowedFileTypes.contains(type)) {
                    throw new CalendarAppException(HttpStatus.NOT_ACCEPTABLE, "Not allowed file type", "NOT_ALLOWED_FILE");
                }
                try {
                    modelList.add(new AttachmentModel(file.getBytes(), file.getOriginalFilename(), type));
                } catch (Exception e) {
                    throw new CalendarAppException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception", "EXCEPTION");
                }
            }
        }
        return modelList;
    }
}
