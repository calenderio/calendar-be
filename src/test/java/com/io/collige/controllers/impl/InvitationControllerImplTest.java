/**
 * @author : Oguz Kahraman
 * @since : 10/6/2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.controllers.impl;

import com.io.collige.mappers.InvitationMapper;
import com.io.collige.models.requests.meet.MeetInvitationRequest;
import com.io.collige.models.responses.meeting.InvitationResponse;
import com.io.collige.services.EventService;
import com.io.collige.services.InvitationService;
import com.io.collige.utils.AttachmentUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvitationControllerImplTest {

    @Mock
    private EventService eventService;

    @Mock
    private InvitationService invitationService;

    @Mock
    private InvitationMapper mapper;

    @Mock
    private AttachmentUtil attachmentUtil;

    @InjectMocks
    private InvitationControllerImpl invitationController;

    @Test
    void sendMeetingInvite() {
        MeetInvitationRequest request = new MeetInvitationRequest();
        MockMultipartFile file = new MockMultipartFile("data", "filename.txt",
                "application/octet-stream", "some xml".getBytes());
        ResponseEntity<Void> responseEntity = invitationController.sendMeetingInvite(request, Collections.singletonList(file));
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(eventService, times(1)).sendEventInvitation(any());
    }

    @Test
    void resendMeeting() {
        ResponseEntity<Void> responseEntity = invitationController.resendMeeting(1L, null);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(eventService, times(1)).resendInvitation(any(), any());
    }

    @Test
    void getMeetings() {
        when(mapper.mapEntityListToModelList(any())).thenReturn(new ArrayList<>());
        ResponseEntity<List<InvitationResponse>> responseEntity = invitationController.getMeetings(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void deleteInvitation() {
        ResponseEntity<Void> responseEntity = invitationController.deleteInvitation(1L);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(invitationService, times(1)).deleteInvitation(1L);
    }

}