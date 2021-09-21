/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services.impl;

import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.core.security.encrypt.TokenEncryptor;
import com.io.fastmeet.entitites.Event;
import com.io.fastmeet.entitites.Invitation;
import com.io.fastmeet.entitites.LinkedCalendar;
import com.io.fastmeet.enums.AppProviderType;
import com.io.fastmeet.enums.DurationType;
import com.io.fastmeet.models.internals.AdditionalTime;
import com.io.fastmeet.models.internals.SchedulerTime;
import com.io.fastmeet.models.remotes.google.TokenRefreshResponse;
import com.io.fastmeet.models.remotes.microsoft.CalendarResponse;
import com.io.fastmeet.models.remotes.microsoft.MicrosoftCalendarEventsRequest;
import com.io.fastmeet.repositories.InvitationRepository;
import com.io.fastmeet.repositories.LinkedCalendarRepository;
import com.io.fastmeet.services.GoogleService;
import com.io.fastmeet.services.MicrosoftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Service
public class CalendarServiceImpl {

    private static final String TIME_RANGE_NOT_AVAILABLE = "Time range not available";
    private static final String RANGE_NOT_AVAILABLE = "RANGE_NOT_AVAILABLE";
    @Autowired
    private GoogleService googleService;

    @Autowired
    private MicrosoftService microsoftService;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private TokenEncryptor tokenEncryptor;

    @Autowired
    private LinkedCalendarRepository linkedCalendarRepository;

    @PostConstruct
    private void itet() {
        getAllCalendars(2021, 9, "47a3aafca7714192905dad62eb637cf4GYBeZyDcQGoConRxxa", "GMT+3");
    }

    public void getAllCalendars(Integer year, Integer month, String invitationId, String timeZone) {
        Invitation invitation = invitationRepository.findByInvitationIdAndScheduledIsFalse(invitationId).orElseThrow(() ->
                new CalendarAppException(HttpStatus.BAD_REQUEST, "No event found", "NO_EVENT"));
        Event event = invitation.getEvent();
        LocalDateTime starDate = getMinDate(year, month, event);
        LocalDateTime endDate = getMaxDate(year, month, event);
        getAvailableHours(starDate, endDate, event);
        Set<LinkedCalendar> linkedCalendarSet = invitation.getUser().getCalendars();
        List<LinkedCalendar> filteredMicrosoft = linkedCalendarSet.stream().filter(item -> AppProviderType.MICROSOFT.equals(item.getType())).toList();
        if (!filteredMicrosoft.isEmpty()) {
            LinkedCalendar selected = filteredMicrosoft.get(0);
            checkAndCreateToken(filteredMicrosoft, selected, AppProviderType.MICROSOFT);
            MicrosoftCalendarEventsRequest eventsRequest = new MicrosoftCalendarEventsRequest();
            eventsRequest.setTimeZone(timeZone);
            eventsRequest.setAccessToken(selected.getAccessToken());
            eventsRequest.setTimeMin(starDate);
            eventsRequest.setTimeMax(endDate);
            eventsRequest.setAccessToken(tokenEncryptor.getDecryptedString(selected.getAccessToken()));
            CalendarResponse response = microsoftService.getCalendarEvents(eventsRequest);
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

    private void checkForNow(Event event) {
        LocalDate localDate = LocalDate.now();
        if (localDate.isBefore(event.getStartDate()) ||
                localDate.isAfter(event.getEndDate())) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, TIME_RANGE_NOT_AVAILABLE, RANGE_NOT_AVAILABLE);
        }
    }

    private LocalDateTime getMinDate(Integer year, Integer month, Event event) {
        if (event.getStartDate().getMonthValue() == month && event.getStartDate().getYear() == year) {
            return event.getStartDate().atTime(0, 0, 0);
        }
        LocalDateTime localDateTime = LocalDateTime.of(year, month, 1, 0, 0, 0);
        if (localDateTime.toLocalDate().isBefore(event.getStartDate()) ||
                localDateTime.toLocalDate().isAfter(event.getEndDate())) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, TIME_RANGE_NOT_AVAILABLE, RANGE_NOT_AVAILABLE);
        }
        return localDateTime;
    }

    private LocalDateTime getMaxDate(Integer year, Integer month, Event event) {
        if (event.getEndDate().getMonthValue() == month && event.getEndDate().getYear() == year) {
            return event.getEndDate().atTime(23, 59, 59);
        }
        LocalDateTime localDateTime = LocalDateTime.of(year, month, 31, 23, 59, 59);
        if (localDateTime.toLocalDate().isBefore(event.getStartDate()) ||
                localDateTime.toLocalDate().isAfter(event.getEndDate())) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, TIME_RANGE_NOT_AVAILABLE, RANGE_NOT_AVAILABLE);
        }
        return localDateTime;
    }

    private Map<LocalDate, Set<LocalTime>> getAvailableHours(LocalDateTime startDate, LocalDateTime endDate, Event event) {
        Map<LocalDate, Set<LocalTime>> availableDates = new HashMap<>();
        int multiply = DurationType.HOUR.equals(event.getDurationType()) ? 60 : 1;
        int minutes = event.getDuration() * multiply;
        addDailyTimes(startDate, endDate, event, availableDates, minutes);
        addAdditional(startDate, endDate, event, availableDates, minutes);
        deleteUnavailableDates(startDate, endDate, event, availableDates);
        return availableDates;
    }

    private void deleteUnavailableDates(LocalDateTime startDate, LocalDateTime endDate, Event event, Map<LocalDate, Set<LocalTime>> availableDates) {
        List<String> times = event.getScheduler().getUnavailable();
        if (times != null && !times.isEmpty()) {
            List<LocalDate> sorted = times.stream().map(LocalDate::parse)
                    .filter(item -> !item.isAfter(endDate.toLocalDate()) && !item.isBefore(startDate.toLocalDate()))
                    .toList();
            for (LocalDate selected : sorted) {
                availableDates.remove(selected);
            }
        }
    }

    private void addAdditional(LocalDateTime startDate, LocalDateTime endDate, Event event, Map<LocalDate, Set<LocalTime>> availableDates, int minutes) {
        Set<AdditionalTime> times = event.getScheduler().getAdditionalTime();
        if (times != null && !times.isEmpty()) {
            List<AdditionalTime> sorted = times.stream()
                    .filter(item -> !item.getDate().isAfter(endDate.toLocalDate()) && !item.getDate().isBefore(startDate.toLocalDate()))
                    .toList();
            for (AdditionalTime selected : sorted) {
                Set<LocalTime> availableTimes = new TreeSet<>();
                for (SchedulerTime time : selected.getTime()) {
                    setTimes(minutes, availableTimes, time);
                }
                addAdditional(availableDates, selected, availableTimes);
            }
        }
    }

    private void addAdditional(Map<LocalDate, Set<LocalTime>> availableDates, AdditionalTime selected, Set<LocalTime> availableTimes) {
        if (!availableTimes.isEmpty()) {
            if (availableDates.containsKey(selected.getDate())) {
                availableDates.get(selected.getDate()).addAll(availableTimes);
            } else {
                availableDates.put(selected.getDate(), availableTimes);
            }
        }
    }

    private void addDailyTimes(LocalDateTime startDate, LocalDateTime endDate, Event event, Map<LocalDate, Set<LocalTime>> availableDates, int minutes) {
        LocalDate tempDate = startDate.toLocalDate();
        if (tempDate.isBefore(LocalDate.now())) {
            tempDate = LocalDate.now();
        }
        while (!tempDate.isAfter(endDate.toLocalDate())) {
            Set<SchedulerTime> selected;
            int day = tempDate.getDayOfWeek().getValue();
            selected = switch (day) {
                case 1 -> event.getScheduler().getMon();
                case 2 -> event.getScheduler().getTue();
                case 3 -> event.getScheduler().getWed();
                case 4 -> event.getScheduler().getThu();
                case 5 -> event.getScheduler().getFri();
                case 6 -> event.getScheduler().getSat();
                case 7 -> event.getScheduler().getSun();
                default -> new HashSet<>();
            };
            if (selected != null) {
                Set<LocalTime> availableTimes = new TreeSet<>();
                for (SchedulerTime time : selected) {
                    setTimes(minutes, availableTimes, time);
                }
                if (!availableTimes.isEmpty()) {
                    availableDates.put(tempDate, availableTimes);
                }
            }
            tempDate = tempDate.plusDays(1);
        }
    }

    private void setTimes(int minutes, Set<LocalTime> availableTimes, SchedulerTime time) {
        String[] startStrings = time.getStart().split(":");
        String[] endStrings = time.getEnd().split(":");
        LocalTime startTime = LocalTime.of(Integer.parseInt(startStrings[0]), Integer.parseInt(startStrings[1]));
        LocalTime endTime = LocalTime.of(Integer.parseInt(endStrings[0]), Integer.parseInt(endStrings[1]));
        while (!startTime.plusMinutes(minutes).isAfter(endTime)) {
            availableTimes.add(startTime);
            startTime = startTime.plusMinutes(minutes);
        }
    }

}
