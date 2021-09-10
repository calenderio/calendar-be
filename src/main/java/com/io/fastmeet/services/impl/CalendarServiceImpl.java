/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services.impl;

import com.io.fastmeet.core.security.encrypt.TokenEncryptor;
import com.io.fastmeet.core.security.jwt.JWTService;
import com.io.fastmeet.entitites.LinkedCalendar;
import com.io.fastmeet.entitites.User;
import com.io.fastmeet.enums.AppProviderType;
import com.io.fastmeet.mappers.CalendarMapper;
import com.io.fastmeet.models.remotes.google.TokenRefreshResponse;
import com.io.fastmeet.models.remotes.microsoft.MicrosoftCalendarEventsRequest;
import com.io.fastmeet.models.requests.calendar.CalendarEventsRequest;
import com.io.fastmeet.repositories.LinkedCalendarRepository;
import com.io.fastmeet.services.CalendarService;
import com.io.fastmeet.services.MicrosoftService;
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
    private MicrosoftService microsoftService;

    @Autowired
    private LinkedCalendarRepository linkedCalendarRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CalendarMapper calendarMapper;

    @Autowired
    private TokenEncryptor tokenEncryptor;

    //TODO multi calendar support
    @Override
    public void getCalendars(CalendarEventsRequest request, String userToken) {
        User user = jwtService.getUserFromToken(userToken);
        Set<LinkedCalendar> calendars = user.getCalendars();
        getGoogleCalendars(request, calendars, user.getEmail());
    }

    private void getGoogleCalendars(CalendarEventsRequest request, Set<LinkedCalendar> calendars, String userName) {
//        List<LinkedCalendar> filtered = calendars.stream().filter(item -> AppProviderType.GOOGLE.equals(item.getType()))
//                .collect(Collectors.toList());
//        if (!filtered.isEmpty()) {
//            LinkedCalendar selected = filtered.get(0);
//            checkAndCreateToken(filtered, selected, AppProviderType.GOOGLE);
//            GoogleCalendarEventsRequest eventsRequest = calendarMapper.mapToGoogle(request);
//            eventsRequest.setUserName(userName);
//            eventsRequest.setAccessToken(tokenEncryptor.getDecryptedString(selected.getAccessToken()));
//            googleService.getCalendarEvents(eventsRequest);
//        }
        List<LinkedCalendar> filteredMicrosft = calendars.stream().filter(item -> AppProviderType.MICROSOFT.equals(item.getType()))
                .collect(Collectors.toList());
        if (!filteredMicrosft.isEmpty()) {
            LinkedCalendar selected = filteredMicrosft.get(0);
            checkAndCreateToken(filteredMicrosft, selected, AppProviderType.MICROSOFT);
            MicrosoftCalendarEventsRequest eventsRequest = calendarMapper.mapToMicrosoft(request);
            eventsRequest.setAccessToken(tokenEncryptor.getDecryptedString(selected.getAccessToken()));
            microsoftService.getCalendarEvents(eventsRequest);
        }
    }

    private void checkAndCreateToken(List<LinkedCalendar> filtered, LinkedCalendar selected, AppProviderType type) {
        if (!LocalDateTime.now().plusSeconds(10).isBefore(filtered.get(0).getExpireDate())) {
            LocalDateTime callTime = LocalDateTime.now();
            TokenRefreshResponse response = new TokenRefreshResponse();
            if (AppProviderType.GOOGLE.equals(type)) {
                response = googleService.getNewAccessToken(tokenEncryptor.getDecryptedString(selected.getRefreshToken()));
            } else if (AppProviderType.MICROSOFT.equals(type)) {
                response = microsoftService.getNewAccessToken(tokenEncryptor.getDecryptedString(selected.getRefreshToken()));
            }
            selected.setExpireDate(callTime.plusSeconds(response.getExpiresIn()));
            selected.setAccessToken(tokenEncryptor.getEncryptedString(response.getAccessToken()));
            linkedCalendarRepository.save(selected);
        }
    }

}
