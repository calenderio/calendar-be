/*
 * @author : Oguz Kahraman
 * @since : 7.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.controllers.impl;

import com.io.fastmeet.controllers.MeetController;
import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.entitites.Invitation;
import com.io.fastmeet.mappers.InvitationMapper;
import com.io.fastmeet.mappers.MeetingMapper;
import com.io.fastmeet.models.internals.AttachmentModel;
import com.io.fastmeet.models.internals.AvailableDatesDetails;
import com.io.fastmeet.models.internals.MeetInvitationDetailRequest;
import com.io.fastmeet.models.internals.ScheduleMeetingDetails;
import com.io.fastmeet.models.requests.calendar.ScheduleMeetingRequest;
import com.io.fastmeet.models.requests.meet.MeetInvitationRequest;
import com.io.fastmeet.models.requests.meet.MeetingDateRequest;
import com.io.fastmeet.models.responses.InvitationResponse;
import com.io.fastmeet.models.responses.meeting.ScheduledMeetingResponse;
import com.io.fastmeet.services.CalendarService;
import com.io.fastmeet.services.EventService;
import com.io.fastmeet.services.InvitationService;
import com.io.fastmeet.services.MeetingService;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<Void> sendMeetingInvite(MeetInvitationRequest request, List<MultipartFile> files) {
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
    public ResponseEntity<ScheduledMeetingResponse> getAvailableDates(MeetingDateRequest request) {
        AvailableDatesDetails details = calendarService.getAvailableDates(request.getLocalDate(), request.getInvitationId(), request.getTimeZone());
        return ResponseEntity.ok(meetingMapper.detailsToModel(details));
    }

    @Override
    public ResponseEntity<Void> scheduleMeeting(ScheduleMeetingRequest request, String invitationId, MultipartFile files) {
        ScheduleMeetingDetails details = new ScheduleMeetingDetails();
        details.setRequest(request);
        details.setInvitationId(invitationId);
        if (files != null) {
            List<AttachmentModel> modelList = checkAttachments(Collections.singletonList(files));
            details.setModel(modelList.get(0));
        }
        meetingService.validateAndScheduleMeeting(details);
        return ResponseEntity.noContent().build();
    }

    private List<AttachmentModel> checkAttachments(List<MultipartFile> files) {
        Tika tika = new Tika();
        List<AttachmentModel> modelList = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                try {
                    String type = tika.detect(file.getBytes());
                    if (!allowedFileTypes.contains(type)) {
                        throw new CalendarAppException(HttpStatus.NOT_ACCEPTABLE, "Not allowed file type", "NOT_ALLOWED_FILE");
                    }
                    modelList.add(new AttachmentModel(file.getBytes(), file.getOriginalFilename(), type));
                } catch (Exception e) {
                    throw new CalendarAppException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception", "EXCEPTION");
                }
            }
        }
        return modelList;
    }
}
