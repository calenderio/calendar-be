/*
 * @author : Oguz Kahraman
 * @since : 26.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.io.collige.core.security.jwt.JWTService;
import com.io.collige.entitites.Event;
import com.io.collige.entitites.Invitation;
import com.io.collige.entitites.Question;
import com.io.collige.entitites.Scheduler;
import com.io.collige.entitites.User;
import com.io.collige.mappers.MailRequestMapper;
import com.io.collige.models.internals.GenericMailRequest;
import com.io.collige.models.internals.MeetInvitationDetailRequest;
import com.io.collige.repositories.EventRepository;
import com.io.collige.services.InvitationService;
import com.io.collige.services.MailService;
import com.io.collige.services.SchedulerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private InvitationService invitationService;

    @Mock
    private JWTService jwtService;

    @Mock
    private SchedulerService schedulerService;

    @Mock
    private MailRequestMapper mapper;

    @Mock
    private MailService mailService;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void createEvent() {
        Event event = new Event();
        event.setScheduler(new Scheduler());
        event.setQuestions(Collections.singletonList(new Question()));
        User user = new User();
        user.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(eventRepository.save(any())).thenReturn(event);
        Event response = eventService.createEvent(event);
        verify(eventRepository, times(1)).save(any());
        assertEquals(1L, response.getUserId());
    }

    @Test
    void updateEvent() {
        Event event = new Event();
        event.setPreDefinedSchedulerId(1L);
        event.setQuestions(Collections.singletonList(new Question()));
        User user = new User();
        user.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(eventRepository.save(any())).thenReturn(event);
        when(eventRepository.findByUserIdAndId(1L, 1L)).thenReturn(Optional.of(event));
        Event response = eventService.updateEvent(event, 1L);
        verify(eventRepository, times(1)).save(any());
        assertEquals(1L, response.getPreDefinedSchedulerId());
    }

    @Test
    void deleteEvent() {
        User user = new User();
        user.setId(1L);
        Event event = new Event();
        Scheduler scheduler = new Scheduler();
        scheduler.setId(1L);
        event.setScheduler(scheduler);
        event.setPreDefinedSchedulerId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(eventRepository.findByUserIdAndId(1L, 1L)).thenReturn(Optional.of(event));
        eventService.deleteEvent(1L);
        verify(eventRepository, times(1)).delete(any());
        verify(invitationService, times(1)).deleteInvitationByEvent(1L);
        verify(schedulerService, times(1)).deleteEventScheduler(1L);
        verify(eventRepository, times(1)).delete(any());
    }

    @Test
    void getEvents() {
        Event event = new Event();
        Scheduler scheduler = new Scheduler();
        scheduler.setId(1L);
        event.setScheduler(scheduler);
        event.setUserId(1L);
        User user = new User();
        user.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(eventRepository.findByUserId(1L)).thenReturn(Collections.singletonList(event));
        List<Event> events = eventService.getEvents();
        assertFalse(events.isEmpty());
        assertEquals(1L, events.size());
        assertEquals(1L, events.get(0).getScheduler().getId());
        assertEquals(1L, events.get(0).getUserId());
    }

    @Test
    void sendEventInvitation() {
        User user = new User();
        user.setName("Example");
        MeetInvitationDetailRequest request = new MeetInvitationDetailRequest("example", "example", "example",
                "example", 1L, null, null, null);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(mapper.meetingRequestToMail(request)).thenReturn(new GenericMailRequest());
        eventService.sendEventInvitation(request);
        verify(mailService, times(1)).sendInvitationMail(any());
    }

    @Test
    void resendInvitation() {
        User user = new User();
        user.setName("Example");
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(invitationService.findInvitationByUserIdAndCheckLimit(anyLong(), anyList())).thenReturn(new Invitation());
        when(mapper.invitationToMail(any())).thenReturn(new GenericMailRequest());
        eventService.resendInvitation(1L, new ArrayList<>());
        verify(mailService, times(1)).sendInvitationMail(any());
    }
}