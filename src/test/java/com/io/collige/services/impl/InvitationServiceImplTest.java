/*
 * @author : Oguz Kahraman
 * @since : 26.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.io.collige.constants.CacheConstants;
import com.io.collige.core.exception.CalendarAppException;
import com.io.collige.core.security.jwt.JWTService;
import com.io.collige.core.services.CacheService;
import com.io.collige.entitites.Event;
import com.io.collige.entitites.Invitation;
import com.io.collige.entitites.Licence;
import com.io.collige.entitites.User;
import com.io.collige.enums.LicenceTypes;
import com.io.collige.models.internals.AttachmentModel;
import com.io.collige.models.internals.MeetInvitationDetailRequest;
import com.io.collige.repositories.EventRepository;
import com.io.collige.repositories.InvitationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvitationServiceImplTest {

    @Mock
    private JWTService jwtService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private CacheService cacheService;

    @Mock
    private InvitationRepository invitationRepository;

    @InjectMocks
    private InvitationServiceImpl invitationService;

    @Test
    void saveInvitation() {
        MeetInvitationDetailRequest request = new MeetInvitationDetailRequest();
        request.setBcc(Collections.singletonList("example"));
        request.setCc(Collections.singletonList("example"));
        request.setEventId(1L);
        Event event = new Event();
        event.setId(1L);
        User user = new User();
        user.setId(1L);
        Licence licence = new Licence();
        licence.setLicenceType(LicenceTypes.FREE_TRIAL);
        user.setLicence(licence);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(eventRepository.findByUserIdAndId(1L, 1L)).thenReturn(Optional.of(event));
        String id = invitationService.saveInvitation(request);
        assertEquals(50, id.length());
    }

    @Test
    void saveInvitation_Exception() {
        MeetInvitationDetailRequest request = new MeetInvitationDetailRequest();
        request.setBcc(Collections.singletonList("example"));
        request.setEventId(1L);
        Event event = new Event();
        event.setId(1L);
        User user = new User();
        user.setId(1L);
        Licence licence = new Licence();
        licence.setLicenceType(LicenceTypes.FREE);
        user.setLicence(licence);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(eventRepository.findByUserIdAndId(1L, 1L)).thenReturn(Optional.of(event));
        CalendarAppException exception = assertThrows(CalendarAppException.class, () -> invitationService.saveInvitation(request));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("CC_LIMIT", exception.getCause().getMessage());
    }

    @Test
    void saveInvitation_CCException() {
        MeetInvitationDetailRequest request = new MeetInvitationDetailRequest();
        request.setCc(Collections.singletonList("example"));
        request.setEventId(1L);
        Event event = new Event();
        event.setId(1L);
        User user = new User();
        user.setId(1L);
        Licence licence = new Licence();
        licence.setLicenceType(LicenceTypes.FREE);
        user.setLicence(licence);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(eventRepository.findByUserIdAndId(1L, 1L)).thenReturn(Optional.of(event));
        CalendarAppException exception = assertThrows(CalendarAppException.class, () -> invitationService.saveInvitation(request));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("CC_LIMIT", exception.getCause().getMessage());
    }

    @Test
    void saveInvitation_AttachException() {
        AttachmentModel model = new AttachmentModel();
        model.setName("example");
        model.setType("text");
        model.setData("text".getBytes());
        MeetInvitationDetailRequest request = new MeetInvitationDetailRequest();
        request.setAttachments(Collections.singletonList(model));
        request.setEventId(1L);
        Event event = new Event();
        event.setId(1L);
        User user = new User();
        user.setId(1L);
        Licence licence = new Licence();
        licence.setLicenceType(LicenceTypes.FREE);
        user.setLicence(licence);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(eventRepository.findByUserIdAndId(1L, 1L)).thenReturn(Optional.of(event));
        CalendarAppException exception = assertThrows(CalendarAppException.class, () -> invitationService.saveInvitation(request));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("ATTACHMENT_LIMIT", exception.getCause().getMessage());
    }

    @Test
    void saveInvitation_IndiAttachException() {
        AttachmentModel model = new AttachmentModel();
        model.setName("example");
        model.setType("text");
        model.setData("text".getBytes());
        List<AttachmentModel> attachmentModelList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            attachmentModelList.add(model);
        }
        MeetInvitationDetailRequest request = new MeetInvitationDetailRequest();
        request.setAttachments(attachmentModelList);
        request.setEventId(1L);
        Event event = new Event();
        event.setId(1L);
        User user = new User();
        user.setId(1L);
        Licence licence = new Licence();
        licence.setLicenceType(LicenceTypes.INDIVIDUAL);
        user.setLicence(licence);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(eventRepository.findByUserIdAndId(1L, 1L)).thenReturn(Optional.of(event));
        when(cacheService.getIntegerCacheValue(CacheConstants.ATTACHMENT_LIMIT_IND)).thenReturn(2);
        CalendarAppException exception = assertThrows(CalendarAppException.class, () -> invitationService.saveInvitation(request));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("ATTACHMENT_LIMIT", exception.getCause().getMessage());
    }

    @Test
    void saveInvitation_CommAttachException() {
        AttachmentModel model = new AttachmentModel();
        model.setName("example");
        model.setType("text");
        model.setData("text".getBytes());
        List<AttachmentModel> attachmentModelList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            attachmentModelList.add(model);
        }
        MeetInvitationDetailRequest request = new MeetInvitationDetailRequest();
        request.setAttachments(attachmentModelList);
        request.setEventId(1L);
        Event event = new Event();
        event.setId(1L);
        User user = new User();
        user.setId(1L);
        Licence licence = new Licence();
        licence.setLicenceType(LicenceTypes.COMMERCIAL);
        user.setLicence(licence);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(eventRepository.findByUserIdAndId(1L, 1L)).thenReturn(Optional.of(event));
        when(cacheService.getIntegerCacheValue(CacheConstants.ATTACHMENT_LIMIT_COMMERCIAL)).thenReturn(2);
        CalendarAppException exception = assertThrows(CalendarAppException.class, () -> invitationService.saveInvitation(request));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("ATTACHMENT_LIMIT", exception.getCause().getMessage());
    }

    @Test
    void findInvitationByUserIdAndCheckLimit() {
        User user = new User();
        user.setId(1L);
        Licence licence = new Licence();
        licence.setLicenceType(LicenceTypes.COMMERCIAL);
        Invitation invitation = new Invitation();
        invitation.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(invitationRepository.findByIdAndUserAndScheduledIsFalse(1L, user)).thenReturn(Optional.of(invitation));
        Invitation response = invitationService.findInvitationByUserIdAndCheckLimit(1L, null);
        assertEquals(response.getId(), invitation.getId());
    }

    @Test
    void findInvitations() {
        User user = new User();
        user.setId(1L);
        Invitation invitation = new Invitation();
        invitation.setId(1L);
        List<Invitation> invitations = new ArrayList<>();
        invitations.add(invitation);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(invitationRepository.findByUserAndEvent_Id(user, 1L)).thenReturn(invitations);
        List<Invitation> response = invitationService.findInvitations(1L);
        assertFalse(response.isEmpty());
        assertEquals(response.get(0).getId(), invitation.getId());
    }

    @Test
    void deleteInvitation() {
        User user = new User();
        user.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        invitationService.deleteInvitation(1L);
        verify(invitationRepository, times(1)).deleteInvitation(1L, 1L);
    }

    @Test
    void deleteInvitationByEvent() {
        User user = new User();
        user.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        invitationService.deleteInvitationByEvent(1L);
        verify(invitationRepository, times(1)).deleteInvitationByEvent(1L, 1L);
    }
}