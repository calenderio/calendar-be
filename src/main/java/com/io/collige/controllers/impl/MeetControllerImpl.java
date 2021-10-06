/*
 * @author : Oguz Kahraman
 * @since : 7.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers.impl;

import com.io.collige.controllers.MeetController;
import com.io.collige.mappers.MeetingMapper;
import com.io.collige.models.internals.AttachmentModel;
import com.io.collige.models.internals.AvailableDatesDetails;
import com.io.collige.models.internals.ScheduleMeetingDetails;
import com.io.collige.models.requests.calendar.ScheduleMeetingRequest;
import com.io.collige.models.requests.meet.MeetingDateRequest;
import com.io.collige.models.responses.meeting.ScheduledMeetingResponse;
import com.io.collige.services.CalendarService;
import com.io.collige.services.MeetingService;
import com.io.collige.utils.AttachmentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
public class MeetControllerImpl implements MeetController {

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private MeetingMapper meetingMapper;

    @Autowired
    private AttachmentUtil attachmentUtil;

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
            List<AttachmentModel> modelList = attachmentUtil.checkAttachments(Collections.singletonList(files));
            details.getModels().add(modelList.get(0));
        }
        return details;
    }

}
