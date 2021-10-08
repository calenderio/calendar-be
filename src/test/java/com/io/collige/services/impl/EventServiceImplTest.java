/*
 * @author : Oguz Kahraman
 * @since : 26.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.io.collige.core.security.jwt.JWTService;
import com.io.collige.entitites.Event;
import com.io.collige.entitites.FileLink;
import com.io.collige.entitites.Invitation;
import com.io.collige.entitites.Question;
import com.io.collige.entitites.Scheduler;
import com.io.collige.entitites.User;
import com.io.collige.mappers.MailRequestMapper;
import com.io.collige.models.internals.CreateEventRequest;
import com.io.collige.models.internals.GenericMailRequest;
import com.io.collige.models.internals.MeetInvitationDetailRequest;
import com.io.collige.models.internals.UpdateEventRequest;
import com.io.collige.models.requests.meet.InvitationResendRequest;
import com.io.collige.repositories.EventFileLinkRepository;
import com.io.collige.repositories.EventRepository;
import com.io.collige.repositories.FileLinkRepository;
import com.io.collige.services.InvitationService;
import com.io.collige.services.MailService;
import com.io.collige.services.SchedulerService;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
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

    @Mock
    private FileLinkRepository fileLinkRepository;

    @Mock
    private EventFileLinkRepository eventFileLinkRepository;

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
        when(fileLinkRepository.findByUserIdAndIdIn(anyLong(), any())).thenReturn(Collections.singletonList(new FileLink()));
        Event response = eventService.createEvent(new CreateEventRequest(event, Collections.singleton(1L)));
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
        Event response = eventService.updateEvent(new UpdateEventRequest(event, Collections.singleton(1L), 1L));
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
    void sendEventInvitation() throws IOException {
        FileLink fileLink = new FileLink();
        fileLink.setUserId(0L);
        fileLink.setName("");
        fileLink.setLink("http://www.google.com");
        fileLink.setType("");
        fileLink.setSize(0L);
        fileLink.setId(0L);
        MockedStatic<IOUtils> ioUtilsMockedStatic = Mockito.mockStatic(IOUtils.class);
        ioUtilsMockedStatic.when(() -> IOUtils.toByteArray(any(URL.class))).thenReturn("".getBytes());
        User user = new User();
        user.setName("Example");
        user.setId(1L);
        MeetInvitationDetailRequest request = new MeetInvitationDetailRequest("example", "example", "example",
                "example", 1L, null, null, Collections.singleton(1L));
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(fileLinkRepository.findByUserIdAndIdIn(anyLong(), any())).thenReturn(Collections.singletonList(fileLink));
        when(mapper.meetingRequestToMail(request)).thenReturn(new GenericMailRequest());
        eventService.sendEventInvitation(request);
        verify(mailService, times(1)).sendInvitationMail(any());
        ioUtilsMockedStatic.close();
    }

    @Test
    void resendInvitation() throws IOException {
        User user = new User();
        user.setName("Example");
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(invitationService.findInvitationByUserIdAndCheckLimit(anyLong())).thenReturn(new Invitation());
        when(mapper.invitationToMail(any())).thenReturn(new GenericMailRequest());
        eventService.resendInvitation(1L, new InvitationResendRequest());
        verify(mailService, times(1)).sendInvitationMail(any());
    }
}