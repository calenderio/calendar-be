/*
 * @author : Oguz Kahraman
 * @since : 31.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.io.collige.core.security.encrypt.TokenEncryptor;
import com.io.collige.entitites.Event;
import com.io.collige.entitites.Invitation;
import com.io.collige.entitites.LinkedCalendar;
import com.io.collige.entitites.Meeting;
import com.io.collige.entitites.User;
import com.io.collige.enums.AppProviderType;
import com.io.collige.enums.DurationType;
import com.io.collige.enums.EventLocation;
import com.io.collige.models.remotes.google.TokenRefreshResponse;
import com.io.collige.models.remotes.zoom.ZoomLinkResponse;
import com.io.collige.repositories.LinkedCalendarRepository;
import com.io.collige.services.ZoomService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeetingLocationServiceImplTest {

    @Mock
    private TokenEncryptor tokenEncryptor;

    @Mock
    private LinkedCalendarRepository linkedCalendarRepository;

    @Mock
    private ZoomService zoomService;

    @InjectMocks
    private MeetingLocationServiceImpl meetingLocationService;

    @Test
    void getLocationLink() {
        ZoomLinkResponse response = new ZoomLinkResponse();
        response.setId(1L);
        response.setJoin_url("example_url");
        LinkedCalendar calendar = new LinkedCalendar();
        calendar.setType(AppProviderType.ZOOM);
        calendar.setExpireDate(LocalDateTime.now().minusMinutes(10));
        calendar.setRefreshToken("Example Token");
        User user = new User();
        user.setCalendars(Collections.singleton(calendar));
        Invitation invitation = new Invitation();
        invitation.setUser(user);
        Meeting meeting = new Meeting();
        meeting.setInvitation(invitation);
        Event event = new Event();
        event.setDuration(45);
        event.setDurationType(DurationType.MIN);
        event.setLocation(EventLocation.ZOOM);
        meeting.setEvent(event);
        meeting.setStartDate(LocalDateTime.now());
        when(zoomService.createZoomLink(any(), any())).thenReturn(response);
        when(zoomService.getNewAccessToken(any())).thenReturn(new TokenRefreshResponse("123", 444));
        meetingLocationService.getLocationLink(meeting);
        assertEquals("example_url", meeting.getMeetingLink());
        assertEquals("1", meeting.getLocationId());
    }
}