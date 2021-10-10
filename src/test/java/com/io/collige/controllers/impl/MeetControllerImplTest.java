/*
 * @author : Oguz Kahraman
 * @since : 27.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers.impl;

import com.io.collige.mappers.MeetingMapper;
import com.io.collige.models.internals.event.AvailableDatesDetails;
import com.io.collige.models.internals.file.AttachmentModel;
import com.io.collige.models.internals.scheduler.ScheduleMeetingRequest;
import com.io.collige.models.requests.meet.GetAvailableDateRequest;
import com.io.collige.models.responses.meeting.AvailableDateResponse;
import com.io.collige.services.CalendarService;
import com.io.collige.services.MeetingService;
import com.io.collige.utils.AttachmentUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeetControllerImplTest {

    @Mock
    private CalendarService calendarService;

    @Mock
    private MeetingService meetingService;

    @Mock
    private MeetingMapper meetingMapper;

    @Mock
    private AttachmentUtil attachmentUtil;

    @InjectMocks
    private MeetControllerImpl meetController;

    @Test
    void getAvailableDates() {
        GetAvailableDateRequest request = new GetAvailableDateRequest();
        request.setLocalDate(LocalDate.MAX);
        request.setTimeZone("UTC");
        AvailableDateResponse availableDateResponse = new AvailableDateResponse();
        availableDateResponse.setEmail("Example");
        when(meetingMapper.detailsToModel(any())).thenReturn(availableDateResponse);
        when(calendarService.getAvailableDates(any())).thenReturn(new AvailableDatesDetails());
        ResponseEntity<AvailableDateResponse> responseEntity = meetController.getAvailableDates(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void scheduleMeeting() {
        com.io.collige.models.requests.calendar.ScheduleMeetingRequest request = new com.io.collige.models.requests.calendar.ScheduleMeetingRequest();
        MockMultipartFile file = new MockMultipartFile("data", "filename.txt",
                "application/octet-stream", "some xml".getBytes());
        when(attachmentUtil.checkAttachments(any())).thenReturn(Collections.singletonList(new AttachmentModel()));
        ResponseEntity<Void> responseEntity = meetController.scheduleMeeting(request, "1L", file);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(meetingService, times(1)).validateAndScheduleMeeting(any());
        verify(attachmentUtil, times(1)).checkAttachments(any());
    }

    @Test
    void updateMeeting() {
        com.io.collige.models.requests.calendar.ScheduleMeetingRequest request = new com.io.collige.models.requests.calendar.ScheduleMeetingRequest();
        ResponseEntity<Void> responseEntity = meetController.updateMeeting(request, "1L", null);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(meetingService, times(1)).updateMeetingRequest(any());
    }

    @Test
    void deleteMeeting() {
        ScheduleMeetingRequest details = new ScheduleMeetingRequest();
        details.setInvitationId("1L");
        ResponseEntity<Void> responseEntity = meetController.deleteMeeting("1L");
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(meetingService, times(1)).deleteMeetingRequest(details);
    }

}