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
import com.io.fastmeet.entitites.Meeting;
import com.io.fastmeet.enums.AppProviderType;
import com.io.fastmeet.enums.DurationType;
import com.io.fastmeet.models.internals.AdditionalTime;
import com.io.fastmeet.models.internals.AvailableDatesDetails;
import com.io.fastmeet.models.internals.SchedulerTime;
import com.io.fastmeet.models.remotes.google.GoogleCalendarEventItem;
import com.io.fastmeet.models.remotes.google.GoogleCalendarEventResponse;
import com.io.fastmeet.models.remotes.google.GoogleCalendarEventsRequest;
import com.io.fastmeet.models.remotes.google.TokenRefreshResponse;
import com.io.fastmeet.models.remotes.microsoft.CalendarEventItem;
import com.io.fastmeet.models.remotes.microsoft.CalendarResponse;
import com.io.fastmeet.models.remotes.microsoft.MicrosoftCalendarEventsRequest;
import com.io.fastmeet.repositories.InvitationRepository;
import com.io.fastmeet.repositories.LinkedCalendarRepository;
import com.io.fastmeet.repositories.MeetingRepository;
import com.io.fastmeet.services.CalendarService;
import com.io.fastmeet.services.GoogleService;
import com.io.fastmeet.services.MicrosoftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class CalendarServiceImpl implements CalendarService {

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

    @Autowired
    private MeetingRepository meetingRepository;

    @Override
    public AvailableDatesDetails getAvailableDates(LocalDate localDate, String invitationId, String timeZone) {
        Invitation invitation = invitationRepository.findByInvitationId(invitationId).orElseThrow(() ->
                new CalendarAppException(HttpStatus.BAD_REQUEST, "No event found", "NO_EVENT"));
        AvailableDatesDetails details = new AvailableDatesDetails();
        Event event = invitation.getEvent();
        int year;
        int month;
        if (localDate == null) {
            checkForNow(event);
            year = ZonedDateTime.now(ZoneId.of(timeZone)).getYear();
            month = ZonedDateTime.now(ZoneId.of(timeZone)).getMonthValue();
        } else {
            year = localDate.getYear();
            month = localDate.getMonthValue();
        }
        LocalDateTime starDate = getMinDate(year, month, event, timeZone);
        LocalDateTime endDate = getMaxDate(year, month, event, timeZone);
        Map<LocalDate, Set<LocalTime>> availableDates = getAvailableHours(starDate, endDate, event);
        Set<LinkedCalendar> linkedCalendarSet = invitation.getUser().getCalendars();
        List<LinkedCalendar> filteredMicrosoft = linkedCalendarSet.stream().filter(item -> AppProviderType.MICROSOFT.equals(item.getType())).collect(Collectors.toList());
        List<LinkedCalendar> filteredGoogle = linkedCalendarSet.stream().filter(item -> AppProviderType.GOOGLE.equals(item.getType())).collect(Collectors.toList());
        microsoftCalendarMap(event.getTimeZone(), event, starDate, endDate, availableDates, filteredMicrosoft);
        googleCalendarMap(event.getTimeZone(), event, starDate, endDate, availableDates, filteredGoogle);
        meetingMap(event, starDate, endDate, availableDates);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(event.getTimeZone()));
        if (availableDates.containsKey(now.toLocalDate())) {
            availableDates.get(now.toLocalDate()).removeIf(time -> time.isBefore(now.toLocalTime()));
        }
        availableDates.entrySet().removeIf(time -> time.getValue().isEmpty());
        details.setAvailableDates(availableDates);
        details.setInvitation(invitation);
        return details;
    }

    private void googleCalendarMap(String timeZone, Event event, LocalDateTime starDate, LocalDateTime endDate, Map<LocalDate, Set<LocalTime>> availableDates, List<LinkedCalendar> filteredGoogle) {
        if (!filteredGoogle.isEmpty()) {
            for (LinkedCalendar selected : filteredGoogle) {
                checkAndCreateToken(filteredGoogle, selected, AppProviderType.GOOGLE);
                GoogleCalendarEventsRequest eventsRequest = new GoogleCalendarEventsRequest();
                eventsRequest.setAccessToken(tokenEncryptor.getDecryptedString(selected.getAccessToken()));
                eventsRequest.setUserName(selected.getSocialMail());
                eventsRequest.setTimeZone(timeZone);
                eventsRequest.setTimeMin(starDate);
                eventsRequest.setTimeMax(endDate);
                GoogleCalendarEventResponse response = googleService.getCalendarEvents(eventsRequest);
                for (GoogleCalendarEventItem item : response.getItems()) {
                    int multiply = DurationType.HOUR.equals(event.getDurationType()) ? 60 : 1;
                    int minutes = event.getDuration() * multiply;
                    ZonedDateTime startTime = ZonedDateTime.parse(item.getStart().getDateTime())
                            .withZoneSameInstant(ZoneId.of(event.getTimeZone()));
                    ZonedDateTime endTime = ZonedDateTime.parse(item.getEnd().getDateTime())
                            .withZoneSameInstant(ZoneId.of(event.getTimeZone()));
                    genericFilter(availableDates, minutes, startTime, endTime);
                }
            }
        }
    }

    private void meetingMap(Event event, LocalDateTime starDate, LocalDateTime endDate, Map<LocalDate, Set<LocalTime>> availableDates) {
        List<Meeting> meetings = meetingRepository.findUserEventMeetings(event.getId(), starDate, endDate);
        for (Meeting item : meetings) {
            int multiply = DurationType.HOUR.equals(event.getDurationType()) ? 60 : 1;
            int minutes = event.getDuration() * multiply;
            ZonedDateTime startTime = ZonedDateTime.of(item.getStartDate(), ZoneId.of(item.getTimeZone()))
                    .withZoneSameInstant(ZoneId.of(event.getTimeZone()));
            ZonedDateTime endTime = ZonedDateTime.of(item.getEndDate(), ZoneId.of(item.getTimeZone()))
                    .withZoneSameInstant(ZoneId.of(event.getTimeZone()));
            genericFilter(availableDates, minutes, startTime, endTime);
        }
    }

    private void microsoftCalendarMap(String timeZone, Event event, LocalDateTime starDate, LocalDateTime endDate, Map<LocalDate, Set<LocalTime>> availableDates, List<LinkedCalendar> filteredMicrosoft) {
        if (!filteredMicrosoft.isEmpty()) {
            for (LinkedCalendar selected : filteredMicrosoft) {
                checkAndCreateToken(filteredMicrosoft, selected, AppProviderType.MICROSOFT);
                MicrosoftCalendarEventsRequest eventsRequest = new MicrosoftCalendarEventsRequest();
                eventsRequest.setTimeZone(timeZone);
                eventsRequest.setAccessToken(selected.getAccessToken());
                eventsRequest.setTimeMin(starDate);
                eventsRequest.setTimeMax(endDate);
                eventsRequest.setAccessToken(tokenEncryptor.getDecryptedString(selected.getAccessToken()));
                CalendarResponse response = microsoftService.getCalendarEvents(eventsRequest);
                List<CalendarEventItem> items = response.getValue();
                for (CalendarEventItem item : items) {
                    int multiply = DurationType.HOUR.equals(event.getDurationType()) ? 60 : 1;
                    ZonedDateTime startTime = LocalDateTime.parse(item.getStart().getDateTime())
                            .atZone(ZoneId.of(item.getStart().getTimeZone())).withZoneSameInstant(ZoneId.of(event.getTimeZone()));
                    ZonedDateTime endTime = LocalDateTime.parse(item.getEnd().getDateTime())
                            .atZone(ZoneId.of(item.getEnd().getTimeZone())).withZoneSameInstant(ZoneId.of(event.getTimeZone()));
                    genericFilter(availableDates, multiply, startTime, endTime);
                }
            }
        }
    }

    private void genericFilter(Map<LocalDate, Set<LocalTime>> availableDates, int multiply, ZonedDateTime startTime, ZonedDateTime endTime) {
        if (!startTime.toLocalDate().isEqual(endTime.toLocalDate())) {
            availableDates.entrySet().removeIf(time -> time.getKey().isAfter(startTime.toLocalDate()) && time.getKey().isBefore(endTime.toLocalDate()));
            if (availableDates.containsKey(startTime.toLocalDate())) {
                availableDates.get(startTime.toLocalDate())
                        .removeIf(time -> startTime.toLocalTime().minusMinutes(multiply).isBefore(time)
                                && endTime.toLocalTime().withHour(23).withMinute(59).isAfter(time));
            }
            if (availableDates.containsKey(endTime.toLocalDate())) {
                availableDates.get(endTime.toLocalDate())
                        .removeIf(time -> endTime.toLocalTime().withMinute(0).withHour(0).isBefore(time)
                                && endTime.toLocalTime().isAfter(time));
            }
        } else {
            if (availableDates.containsKey(startTime.toLocalDate())) {
                availableDates.get(startTime.toLocalDate())
                        .removeIf(time -> startTime.toLocalTime().minusMinutes(multiply).isBefore(time)
                                && endTime.toLocalTime().isAfter(time));
            }
        }
    }

    private void checkForNow(Event event) {
        LocalDate localDate = LocalDate.now();
        if (localDate.isBefore(event.getStartDate()) ||
                localDate.isAfter(event.getEndDate())) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, TIME_RANGE_NOT_AVAILABLE, RANGE_NOT_AVAILABLE);
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

    private LocalDateTime getMaxDate(Integer year, Integer month, Event event, String timeZone) {
        return ZonedDateTime.of(LocalDate.of(year, month, YearMonth.of(year, month).atEndOfMonth().getDayOfMonth()),
                LocalTime.MAX, ZoneId.of(timeZone))
                .withZoneSameInstant(ZoneId.of(event.getTimeZone())).toLocalDateTime();
    }

    private LocalDateTime getMinDate(Integer year, Integer month, Event event, String timeZone) {
        return ZonedDateTime.of(LocalDate.of(year, month, 1), LocalTime.MIN, ZoneId.of(timeZone))
                .withZoneSameInstant(ZoneId.of(event.getTimeZone())).toLocalDateTime();
    }

    private Map<LocalDate, Set<LocalTime>> getAvailableHours(LocalDateTime startDate, LocalDateTime endDate, Event event) {
        Map<LocalDate, Set<LocalTime>> availableDates = new HashMap<>();
        int multiply = DurationType.HOUR.equals(event.getDurationType()) ? 60 : 1;
        int minutes = event.getDuration() * multiply;
        addDailyTimes(startDate, endDate, event, availableDates, minutes);
        deleteUnavailableDates(startDate, endDate, event, availableDates);
        addAdditional(startDate, endDate, event, availableDates, minutes);
        return availableDates;
    }

    private void deleteUnavailableDates(LocalDateTime startDate, LocalDateTime endDate, Event event, Map<LocalDate, Set<LocalTime>> availableDates) {
        List<String> times = event.getScheduler().getUnavailable();
        if (times != null && !times.isEmpty()) {
            List<LocalDate> sorted = times.stream().map(LocalDate::parse)
                    .filter(item -> !item.isAfter(endDate.toLocalDate()) && !item.isBefore(startDate.toLocalDate()))
                    .collect(Collectors.toList());
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
                    .collect(Collectors.toList());
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
            if (selected != null && !selected.isEmpty()) {
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
        LocalDateTime startDate = startTime.atDate(LocalDate.MIN);
        LocalDateTime endDate = endTime.atDate(LocalDate.MIN);
        while (!startDate.plusMinutes(minutes).isAfter(endDate)) {
            availableTimes.add(startDate.toLocalTime());
            startDate = startDate.plusMinutes(minutes);
        }
    }

}
