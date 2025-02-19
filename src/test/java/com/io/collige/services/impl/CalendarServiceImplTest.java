/*
 * @author : Oguz Kahraman
 * @since : 26.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.io.collige.core.exception.CalendarAppException;
import com.io.collige.core.security.encrypt.TokenEncryptor;
import com.io.collige.core.security.jwt.JWTService;
import com.io.collige.entitites.Event;
import com.io.collige.entitites.Invitation;
import com.io.collige.entitites.LinkedCalendar;
import com.io.collige.entitites.Meeting;
import com.io.collige.entitites.Scheduler;
import com.io.collige.entitites.User;
import com.io.collige.enums.AppProviderType;
import com.io.collige.enums.DurationType;
import com.io.collige.models.internals.event.AdditionalTime;
import com.io.collige.models.internals.event.AvailableDatesDetails;
import com.io.collige.models.internals.scheduler.SchedulerTime;
import com.io.collige.models.remotes.google.CalendarEventDateType;
import com.io.collige.models.remotes.google.GoogleCalendarEventItem;
import com.io.collige.models.remotes.google.GoogleCalendarEventResponse;
import com.io.collige.models.remotes.google.TokenRefreshResponse;
import com.io.collige.models.remotes.microsoft.CalendarEventItem;
import com.io.collige.models.remotes.microsoft.DateType;
import com.io.collige.models.remotes.microsoft.MicrosoftCalendarResponse;
import com.io.collige.models.requests.calendar.CalendarGetRequest;
import com.io.collige.models.requests.meet.GetAvailableDateRequest;
import com.io.collige.models.responses.calendar.CalendarResponse;
import com.io.collige.repositories.InvitationRepository;
import com.io.collige.repositories.LinkedCalendarRepository;
import com.io.collige.repositories.MeetingRepository;
import com.io.collige.services.GoogleService;
import com.io.collige.services.MicrosoftService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalendarServiceImplTest {

    @Mock
    private GoogleService googleService;

    @Mock
    private MicrosoftService microsoftService;

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private TokenEncryptor tokenEncryptor;

    @Mock
    private LinkedCalendarRepository linkedCalendarRepository;

    @Mock
    private JWTService jwtService;

    @Mock
    private MeetingRepository meetingRepository;

    @InjectMocks
    private CalendarServiceImpl calendarService;

    @Test
    void getAvailableDates_DateException() {
        GetAvailableDateRequest request = new GetAvailableDateRequest();
        request.setInvitationId("1L");
        request.setTimeZone("UTC");
        request.setLocalDate(null);
        Invitation invitation = new Invitation();
        Event event = new Event();
        event.setStartDate(LocalDate.now().plusDays(1));
        event.setEndDate(LocalDate.now().plusDays(1));
        invitation.setEvent(event);
        when(invitationRepository.findByInvitationId("1L")).thenReturn(Optional.of(invitation));
        CalendarAppException exception = assertThrows(CalendarAppException.class, () ->
                calendarService.getAvailableDates(request));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("RANGE_NOT_AVAILABLE", exception.getCause().getMessage());
    }

    @Test
    void getAvailableDates_InvitationException() {
        GetAvailableDateRequest request = new GetAvailableDateRequest();
        request.setInvitationId("1L");
        request.setTimeZone("UTC");
        request.setLocalDate(null);
        when(invitationRepository.findByInvitationId("1L")).thenReturn(Optional.empty());
        CalendarAppException exception = assertThrows(CalendarAppException.class, () ->
                calendarService.getAvailableDates(request));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("NO_EVENT", exception.getCause().getMessage());
    }

    @Test
    void getAvailableDates() {
        GetAvailableDateRequest request = new GetAvailableDateRequest();
        request.setInvitationId("1L");
        request.setTimeZone("UTC");
        request.setLocalDate(null);
        Invitation invitation = new Invitation();
        AdditionalTime additionalTime = new AdditionalTime();
        additionalTime.setDate(LocalDate.now());
        additionalTime.setTime(Collections.singleton(new SchedulerTime("01:00", "23:00")));
        User user = new User();
        Event event = new Event();
        Scheduler scheduler = generateSchedulerDays();
        scheduler.setUnavailable(Collections.singletonList(LocalDate.now().toString()));
        scheduler.setAdditionalTime(Collections.singleton(additionalTime));
        event.setStartDate(LocalDate.now().minusDays(1));
        event.setEndDate(LocalDate.now().plusDays(30));
        event.setTimeZone("UTC");
        event.setScheduler(scheduler);
        event.setDuration(30);
        event.setDurationType(DurationType.MIN);
        event.setId(1L);
        invitation.setEvent(event);
        invitation.setUser(user);
        when(invitationRepository.findByInvitationId("1L")).thenReturn(Optional.of(invitation));
        when(meetingRepository.findUserEventMeetings(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(getMeeting());
        AvailableDatesDetails details = calendarService.getAvailableDates(request);
        assertFalse(details.getAvailableDates().isEmpty());
    }

    @Test
    void getAvailableDates_WithDate() {
        GetAvailableDateRequest request = new GetAvailableDateRequest();
        request.setInvitationId("1L");
        request.setTimeZone("UTC");
        request.setLocalDate(LocalDate.now().plusMonths(1));
        TokenRefreshResponse response = new TokenRefreshResponse();
        response.setAccessToken("12313");
        response.setExpiresIn(Integer.MAX_VALUE);
        Invitation invitation = new Invitation();
        AdditionalTime additionalTime = new AdditionalTime();
        additionalTime.setDate(LocalDate.now().plusMonths(1));
        additionalTime.setTime(Collections.singleton(new SchedulerTime("01:00", "23:00")));
        User user = new User();
        Event event = new Event();
        Scheduler scheduler = generateSchedulerDays();
        scheduler.setAdditionalTime(Collections.singleton(additionalTime));
        scheduler.setUnavailable(Collections.singletonList(LocalDate.now().plusMonths(1).toString()));
        user.setCalendars(createLinkedCalendar());
        event.setStartDate(LocalDate.now().minusDays(1));
        event.setEndDate(LocalDate.now().plusDays(30));
        event.setTimeZone("UTC");
        event.setScheduler(scheduler);
        event.setDuration(30);
        event.setDurationType(DurationType.MIN);
        invitation.setEvent(event);
        invitation.setUser(user);
        when(invitationRepository.findByInvitationId("1L")).thenReturn(Optional.of(invitation));
        when(tokenEncryptor.getDecryptedString(anyString())).thenReturn("12313");
        when(googleService.getNewAccessToken("12313")).thenReturn(response);
        when(microsoftService.getCalendarEvents(any())).thenReturn(microsoftResponse());
        when(googleService.getCalendarEvents(any())).thenReturn(googleResponse());
        when(microsoftService.getNewAccessToken(anyString())).thenReturn(response);
        AvailableDatesDetails details = calendarService.getAvailableDates(request);
        assertFalse(details.getAvailableDates().isEmpty());
    }

    @Test
    void getAllCalendars() {
        CalendarGetRequest itemsRequest = new CalendarGetRequest();
        itemsRequest.setEndDate(LocalDateTime.now());
        itemsRequest.setStarDate(LocalDateTime.now());
        itemsRequest.setTimeZone("UTC");
        TokenRefreshResponse response = new TokenRefreshResponse();
        response.setAccessToken("12313");
        response.setExpiresIn(Integer.MAX_VALUE);
        User user = new User();
        user.setId(1L);
        user.setCalendars(createLinkedCalendar());
        when(tokenEncryptor.getDecryptedString(anyString())).thenReturn("12313");
        when(googleService.getNewAccessToken("12313")).thenReturn(response);
        when(microsoftService.getCalendarEvents(any())).thenReturn(microsoftResponse());
        when(googleService.getCalendarEvents(any())).thenReturn(googleResponse());
        when(microsoftService.getNewAccessToken(anyString())).thenReturn(response);
        when(meetingRepository.findUserMeetings(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(getMeeting());
        when(jwtService.getLoggedUser()).thenReturn(user);
        CalendarResponse details = calendarService.getAllCalendars(itemsRequest);
        assertEquals(3, details.getItems().size());

    }

    private Scheduler generateSchedulerDays() {
        Scheduler scheduler = new Scheduler();
        scheduler.setSat(Collections.singleton(new SchedulerTime("09:00", "18:00")));
        scheduler.setSun(Collections.singleton(new SchedulerTime("09:00", "18:00")));
        scheduler.setFri(Collections.singleton(new SchedulerTime("09:00", "18:00")));
        scheduler.setMon(Collections.singleton(new SchedulerTime("09:00", "18:00")));
        scheduler.setWed(Collections.singleton(new SchedulerTime("09:00", "18:00")));
        scheduler.setTue(Collections.singleton(new SchedulerTime("09:00", "18:00")));
        scheduler.setThu(Collections.singleton(new SchedulerTime("09:00", "18:00")));
        return scheduler;
    }

    private Set<LinkedCalendar> createLinkedCalendar() {
        Set<LinkedCalendar> calendarSet = new HashSet<>();
        LinkedCalendar calendar = new LinkedCalendar();
        calendar.setType(AppProviderType.GOOGLE);
        calendar.setAccessToken("!@3");
        calendar.setRefreshToken("!@3");
        calendar.setExpireDate(LocalDateTime.MIN);
        LinkedCalendar calendar2 = new LinkedCalendar();
        calendar2.setType(AppProviderType.MICROSOFT);
        calendar2.setAccessToken("!@3");
        calendar2.setRefreshToken("!@3");
        calendar2.setExpireDate(LocalDateTime.MIN);
        calendarSet.add(calendar2);
        calendarSet.add(calendar);
        return calendarSet;
    }

    private MicrosoftCalendarResponse microsoftResponse() {
        MicrosoftCalendarResponse response = new MicrosoftCalendarResponse();
        List<CalendarEventItem> items = new ArrayList<>();
        CalendarEventItem item = new CalendarEventItem();
        item.setStart(new DateType(LocalDateTime.now().plusMonths(1).toString(), "UTC"));
        item.setEnd(new DateType(LocalDateTime.now().plusMonths(1).toString(), "UTC"));
        items.add(item);
        response.setValue(items);
        return response;
    }

    private GoogleCalendarEventResponse googleResponse() {
        GoogleCalendarEventResponse response = new GoogleCalendarEventResponse();
        List<GoogleCalendarEventItem> items = new ArrayList<>();
        GoogleCalendarEventItem item = new GoogleCalendarEventItem();
        item.setStart(new CalendarEventDateType(ZonedDateTime.now().plusMonths(1).toString(), "UTC"));
        item.setEnd(new CalendarEventDateType(ZonedDateTime.now().plusMonths(1).plusDays(1).toString(), "UTC"));
        items.add(item);
        response.setItems(items);
        response.setTimeZone("UTC");
        return response;
    }

    private List<Meeting> getMeeting() {
        Meeting response = new Meeting();
        response.setStartDate(LocalDateTime.now().minusMinutes(10));
        response.setEndDate(LocalDateTime.now());
        response.setTimeZone("UTC");
        return Collections.singletonList(response);
    }

}