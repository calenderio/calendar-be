/**
 * @author : Oguz Kahraman
 * @since : 10/6/2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.controllers.impl;

import com.io.collige.mappers.InvitationMapper;
import com.io.collige.models.requests.meet.InvitationResendRequest;
import com.io.collige.models.requests.meet.SendInvitationRequest;
import com.io.collige.models.responses.meeting.InvitationResponse;
import com.io.collige.services.EventService;
import com.io.collige.services.InvitationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
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

    @InjectMocks
    private InvitationControllerImpl invitationController;

    @Test
    void sendMeetingInvite() throws IOException {
        SendInvitationRequest request = new SendInvitationRequest();
        ResponseEntity<Void> responseEntity = invitationController.sendInvitation(request);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(eventService, times(1)).sendEventInvitation(any());
    }

    @Test
    void resendMeeting() throws IOException {
        ResponseEntity<Void> responseEntity = invitationController.resendInvitation(1L, new InvitationResendRequest());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(eventService, times(1)).resendInvitation(any());
    }

    @Test
    void getMeetings() {
        when(mapper.mapEntityListToModelList(any())).thenReturn(new ArrayList<>());
        ResponseEntity<List<InvitationResponse>> responseEntity = invitationController.getInvitations(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void deleteInvitation() {
        ResponseEntity<Void> responseEntity = invitationController.deleteInvitation(1L);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(invitationService, times(1)).deleteInvitation(1L);
    }

}