/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services.impl;

import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.core.security.encrypt.TokenEncryptor;
import com.io.fastmeet.entitites.Invitation;
import com.io.fastmeet.entitites.LinkedCalendar;
import com.io.fastmeet.enums.AppProviderType;
import com.io.fastmeet.mappers.EventMapper;
import com.io.fastmeet.models.remotes.google.TokenRefreshResponse;
import com.io.fastmeet.models.remotes.microsoft.MicrosoftCalendarEventsRequest;
import com.io.fastmeet.repositories.InvitationRepository;
import com.io.fastmeet.repositories.LinkedCalendarRepository;
import com.io.fastmeet.services.GoogleService;
import com.io.fastmeet.services.MicrosoftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CalendarServiceImpl {

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
    private EventMapper eventMapper;

    public void getAllCalendars(Integer month, String invitationId, String timeZone) {
        Invitation invitation = invitationRepository.findByInvitationIdAndScheduledIsFalse(invitationId).orElseThrow(() ->
                new CalendarAppException(HttpStatus.BAD_REQUEST, "No event found", "NO_EVENT"));
        Set<LinkedCalendar> linkedCalendarSet = invitation.getUser().getCalendars();
        List<LinkedCalendar> filteredMicrosft = linkedCalendarSet.stream().filter(item -> AppProviderType.MICROSOFT.equals(item.getType()))
                .collect(Collectors.toList());
        if (!filteredMicrosft.isEmpty()) {
            LinkedCalendar selected = filteredMicrosft.get(0);
            checkAndCreateToken(filteredMicrosft, selected, AppProviderType.MICROSOFT);
            ZonedDateTime.of()
            MicrosoftCalendarEventsRequest eventsRequest = eventMapper.mapToMicrosoft(request);
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
