/*
 * @author : Oguz Kahraman
 * @since : 27.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers.impl;

import com.io.collige.core.exception.CalendarAppException;
import com.io.collige.mappers.InvitationMapper;
import com.io.collige.mappers.MeetingMapper;
import com.io.collige.models.internals.AvailableDatesDetails;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeetControllerImplTest {

    @Mock
    private EventService eventService;

    @Mock
    private InvitationService invitationService;

    @Mock
    private CalendarService calendarService;

    @Mock
    private MeetingService meetingService;

    @Mock
    private InvitationMapper mapper;

    @Mock
    private MeetingMapper meetingMapper;

    @InjectMocks
    private MeetControllerImpl meetController;

    @Test
    void sendMeetingInvite() {
        addTypes();
        MeetInvitationRequest request = new MeetInvitationRequest();
        MockMultipartFile file = new MockMultipartFile("data", "filename.txt",
                "application/octet-stream", "some xml".getBytes());
        ResponseEntity<Void> responseEntity = meetController.sendMeetingInvite(request, Collections.singletonList(file));
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(eventService, times(1)).sendEventInvitation(any());
    }

    @Test
    void resendMeeting() {
        addTypes();
        ResponseEntity<Void> responseEntity = meetController.resendMeeting(1L, null);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(eventService, times(1)).resendInvitation(any(), any());
    }

    @Test
    void getMeetings() {
        when(mapper.mapEntityListToModelList(any())).thenReturn(new ArrayList<>());
        ResponseEntity<List<InvitationResponse>> responseEntity = meetController.getMeetings(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void deleteMapping() {
        ResponseEntity<Void> responseEntity = meetController.deleteMapping(1L);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(invitationService, times(1)).deleteInvitation(1L);
    }

    @Test
    void getAvailableDates() {
        MeetingDateRequest request = new MeetingDateRequest();
        request.setLocalDate(LocalDate.MAX);
        request.setTimeZone("UTC");
        ScheduledMeetingResponse scheduledMeetingResponse = new ScheduledMeetingResponse();
        scheduledMeetingResponse.setEmail("Example");
        when(meetingMapper.detailsToModel(any())).thenReturn(scheduledMeetingResponse);
        when(calendarService.getAvailableDates(any(), any(), any())).thenReturn(new AvailableDatesDetails());
        ResponseEntity<ScheduledMeetingResponse> responseEntity = meetController.getAvailableDates(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void scheduleMeeting() {
        addTypes();
        ScheduleMeetingRequest request = new ScheduleMeetingRequest();
        MockMultipartFile file = new MockMultipartFile("data", "filename.txt",
                "application/octet-stream", "some xml".getBytes());
        ResponseEntity<Void> responseEntity = meetController.scheduleMeeting(request, "1L", file);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(meetingService, times(1)).validateAndScheduleMeeting(any());
    }

    @Test
    void scheduleMeeting_err() {
        addTypesError();
        ScheduleMeetingRequest request = new ScheduleMeetingRequest();
        MockMultipartFile file = new MockMultipartFile("data", "filename.txt",
                "application/octet-stream", "some xml".getBytes());
        CalendarAppException exception = assertThrows(CalendarAppException.class, () ->
                meetController.scheduleMeeting(request, "1L", file));
        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatus());
    }

    @Test
    void updateMeeting() {
        ScheduleMeetingRequest request = new ScheduleMeetingRequest();
        ResponseEntity<Void> responseEntity = meetController.updateMeeting(request, "1L", null);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(meetingService, times(1)).updateMeetingRequest(any());
    }

    @Test
    void deleteMeeting() {
        ScheduleMeetingDetails details = new ScheduleMeetingDetails();
        details.setInvitationId("1L");
        ResponseEntity<Void> responseEntity = meetController.deleteMeeting("1L");
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(meetingService, times(1)).deleteMeetingRequest(details);
    }

    private void addTypes() {
        String values = "application/vnd.openxmlformats-officedocument.wordprocessingml.document," +
                "application/msword,application/pdf,application/x-tika-ooxml,application/x-tika-msoffice," +
                "application/octet-stream,text/plain";
        ReflectionTestUtils.setField(meetController, "allowedFileTypes", Arrays.asList(values.split(",")));
    }

    private void addTypesError() {
        String values = "application/vnd.openxmlformats-officedocument.wordprocessingml.document," +
                "application/msword,application/pdf,application/x-tika-ooxml,application/x-tika-msoffice," +
                "application/octet-stream";
        ReflectionTestUtils.setField(meetController, "allowedFileTypes", Arrays.asList(values.split(",")));
    }
}