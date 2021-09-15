/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services.impl;

import com.io.fastmeet.core.security.encrypt.TokenEncryptor;
import com.io.fastmeet.core.security.jwt.JWTService;
import com.io.fastmeet.entitites.Event;
import com.io.fastmeet.entitites.LinkedCalendar;
import com.io.fastmeet.entitites.Scheduler;
import com.io.fastmeet.entitites.User;
import com.io.fastmeet.enums.AppProviderType;
import com.io.fastmeet.models.remotes.google.TokenRefreshResponse;
import com.io.fastmeet.models.requests.calendar.CalendarEventsRequest;
import com.io.fastmeet.repositories.EventRepository;
import com.io.fastmeet.repositories.LinkedCalendarRepository;
import com.io.fastmeet.services.EventService;
import com.io.fastmeet.services.MicrosoftService;
import com.io.fastmeet.services.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private GoogleServiceImpl googleService;

    @Autowired
    private MicrosoftService microsoftService;

    @Autowired
    private LinkedCalendarRepository linkedCalendarRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private TokenEncryptor tokenEncryptor;

    //TODO multi calendar support
    @Override
    public void getCalendars(CalendarEventsRequest request, String userToken) {
        User user = jwtService.getLoggedUser();
        Set<LinkedCalendar> calendars = user.getCalendars();
        getGoogleCalendars(request, calendars, user.getEmail());
    }

    @Override
    public Event createCalendarType(Event event) {
        User user = jwtService.getLoggedUser();
        event.setUserId(user.getId());
        if (event.getPreDefinedSchedulerId() != null) {
            event.setScheduler(schedulerService.getUserSchedulerById(event.getPreDefinedSchedulerId(), user.getId()));
        } else {
            Scheduler scheduler = event.getScheduler();
            scheduler.setName(event.getName());
            event.setScheduler(schedulerService.saveCalendarTypeScheduler(scheduler, user.getId()));
        }
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getCalendarTypes() {
        User user = jwtService.getLoggedUser();
        return eventRepository.findByUserId(user.getId());
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
//        if (!filteredMicrosft.isEmpty()) {
//            LinkedCalendar selected = filteredMicrosft.get(0);
//            checkAndCreateToken(filteredMicrosft, selected, AppProviderType.MICROSOFT);
//            MicrosoftCalendarEventsRequest eventsRequest = calendarMapper.mapToMicrosoft(request);
//            eventsRequest.setAccessToken(tokenEncryptor.getDecryptedString(selected.getAccessToken()));
//            microsoftService.getCalendarEvents(eventsRequest);
//        }
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
