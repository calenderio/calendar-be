/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services.impl;

import com.io.fastmeet.core.security.jwt.JWTService;
import com.io.fastmeet.entitites.LinkedCalendar;
import com.io.fastmeet.entitites.User;
import com.io.fastmeet.enums.CalendarProviderType;
import com.io.fastmeet.mappers.CalendarMapper;
import com.io.fastmeet.models.remotes.google.GoogleCalendarEventsRequest;
import com.io.fastmeet.models.remotes.google.TokenRefreshResponse;
import com.io.fastmeet.models.requests.calendar.CalendarEventsRequest;
import com.io.fastmeet.repositories.LinkedCalendarRepository;
import com.io.fastmeet.services.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CalendarServiceImpl implements CalendarService {

    @Autowired
    private GoogleServiceImpl googleService;

    @Autowired
    private LinkedCalendarRepository linkedCalendarRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CalendarMapper calendarMapper;

    //TODO multi calendar support
    @Override
    public void getCalendars(CalendarEventsRequest request, String userToken) {
        User user = jwtService.getUserFromToken(userToken);
        Set<LinkedCalendar> calendars = user.getCalendars();
        getGoogleCalendars(request, calendars, user.getEmail());
    }

    private void getGoogleCalendars(CalendarEventsRequest request, Set<LinkedCalendar> calendars, String userName) {
        List<LinkedCalendar> filtered = calendars.stream().filter(item -> CalendarProviderType.GOOGLE.equals(item.getType()))
                .collect(Collectors.toList());
        if (!filtered.isEmpty()) {
            LinkedCalendar selected = filtered.get(0);
            checkAndCreateToken(filtered, selected);
            GoogleCalendarEventsRequest eventsRequest = calendarMapper.mapToGoogle(request);
            eventsRequest.setUserName(userName);
            eventsRequest.setAccessToken(selected.getAccessToken());
            googleService.getCalendarEvents(eventsRequest);
        }
    }

    private void checkAndCreateToken(List<LinkedCalendar> filtered, LinkedCalendar selected) {
        if (!LocalDateTime.now().plusSeconds(10).isBefore(filtered.get(0).getExpireDate())) {
            LocalDateTime callTime = LocalDateTime.now();
            TokenRefreshResponse response = googleService.getNewAccessToken(selected.getRefreshToken());
            selected.setExpireDate(callTime.plusSeconds(response.getExpiresIn()));
            selected.setAccessToken(response.getAccessToken());
            linkedCalendarRepository.save(selected);
        }
    }

}
