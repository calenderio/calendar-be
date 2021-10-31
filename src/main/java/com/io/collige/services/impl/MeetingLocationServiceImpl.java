/**
 * @author : Oguz Kahraman
 * @since : 31 Eki 2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.services.impl;

import com.io.collige.core.security.encrypt.TokenEncryptor;
import com.io.collige.entitites.LinkedCalendar;
import com.io.collige.entitites.Meeting;
import com.io.collige.enums.AppProviderType;
import com.io.collige.models.remotes.google.TokenRefreshResponse;
import com.io.collige.models.remotes.zoom.ZoomLinkCreateRequest;
import com.io.collige.models.remotes.zoom.ZoomLinkResponse;
import com.io.collige.repositories.LinkedCalendarRepository;
import com.io.collige.services.MeetingLocationService;
import com.io.collige.services.ZoomService;
import com.io.collige.utils.DurationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
public class MeetingLocationServiceImpl implements MeetingLocationService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Autowired
    private TokenEncryptor tokenEncryptor;

    @Autowired
    private LinkedCalendarRepository linkedCalendarRepository;

    @Autowired
    private ZoomService zoomService;

    @Override
    public void getLocationLink(Meeting meeting) {
        switch (meeting.getEvent().getLocation()) {
            case ZOOM -> {
                LinkedCalendar zoom = meeting.getInvitation().getUser().getCalendars().stream()
                        .filter(item -> AppProviderType.ZOOM.equals(item.getType())).collect(Collectors.toList()).get(0);
                checkAndCreateToken(zoom);
                ZoomLinkCreateRequest createRequest = new ZoomLinkCreateRequest();
                createRequest.setStartTime(meeting.getStartDate().format(FORMATTER));
                createRequest.setDuration(DurationUtil.getDurationAsMinute(meeting.getEvent().getDurationType(), meeting.getEvent().getDuration()));
                createRequest.setTimezone(meeting.getTimeZone());
                createRequest.setTopic(meeting.getTitle());
                ZoomLinkResponse response = zoomService.createZoomLink(createRequest, tokenEncryptor.getDecryptedString(zoom.getAccessToken()));
                meeting.setLocationId(Long.toString(response.getId()));
                meeting.setMeetingLink(response.getJoin_url());
            }
        }
    }

    private void checkAndCreateToken(LinkedCalendar selected) {
        if (!LocalDateTime.now().plusSeconds(10).isBefore(selected.getExpireDate())) {
            LocalDateTime callTime = LocalDateTime.now();
            TokenRefreshResponse response = zoomService.getNewAccessToken(tokenEncryptor.getDecryptedString(selected.getRefreshToken()));
            selected.setExpireDate(callTime.plusSeconds(response.getExpiresIn()));
            selected.setAccessToken(tokenEncryptor.getEncryptedString(response.getAccessToken()));
            linkedCalendarRepository.save(selected);
        }
    }

}
