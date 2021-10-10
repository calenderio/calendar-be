/*
 * @author : Oguz Kahraman
 * @since : 7.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers.impl;

import com.io.collige.controllers.MeetController;
import com.io.collige.mappers.MeetingMapper;
import com.io.collige.models.internals.event.AvailableDatesDetails;
import com.io.collige.models.internals.file.AttachmentModel;
import com.io.collige.models.internals.scheduler.ScheduleMeetingRequest;
import com.io.collige.models.requests.meet.GetAvailableDateRequest;
import com.io.collige.models.responses.meeting.AvailableDateResponse;
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
    public ResponseEntity<AvailableDateResponse> getAvailableDates(@Valid GetAvailableDateRequest request) {
        AvailableDatesDetails details = calendarService.getAvailableDates(request);
        return ResponseEntity.ok(meetingMapper.detailsToModel(details));
    }

    @Override
    public ResponseEntity<Void> scheduleMeeting(@Valid com.io.collige.models.requests.calendar.ScheduleMeetingRequest request, String invitationId, MultipartFile files) {
        ScheduleMeetingRequest details = setDetails(request, invitationId, files);
        meetingService.validateAndScheduleMeeting(details);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> updateMeeting(@Valid com.io.collige.models.requests.calendar.ScheduleMeetingRequest request, String invitationId, MultipartFile files) {
        ScheduleMeetingRequest details = setDetails(request, invitationId, files);
        meetingService.updateMeetingRequest(details);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteMeeting(String invitationId) {
        ScheduleMeetingRequest details = new ScheduleMeetingRequest();
        details.setInvitationId(invitationId);
        meetingService.deleteMeetingRequest(details);
        return ResponseEntity.noContent().build();
    }

    private ScheduleMeetingRequest setDetails(com.io.collige.models.requests.calendar.ScheduleMeetingRequest request, String invitationId, MultipartFile files) {
        ScheduleMeetingRequest details = new ScheduleMeetingRequest();
        details.setRequest(request);
        details.setInvitationId(invitationId);
        if (files != null) {
            List<AttachmentModel> modelList = attachmentUtil.checkAttachments(Collections.singletonList(files));
            details.getModels().add(modelList.get(0));
        }
        return details;
    }

}
