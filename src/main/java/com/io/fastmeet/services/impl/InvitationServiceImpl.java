/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services.impl;

import com.io.fastmeet.constants.CacheConstants;
import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.core.security.jwt.JWTService;
import com.io.fastmeet.core.services.CacheService;
import com.io.fastmeet.entitites.Event;
import com.io.fastmeet.entitites.Invitation;
import com.io.fastmeet.entitites.User;
import com.io.fastmeet.enums.LicenceTypes;
import com.io.fastmeet.models.internals.MeetInvitationDetailRequest;
import com.io.fastmeet.repositories.EventRepository;
import com.io.fastmeet.repositories.InvitationRepository;
import com.io.fastmeet.services.InvitationService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InvitationServiceImpl implements InvitationService {

    private static final String NOT_ENOUGH_LIMIT_FOR_ATTACHMENT = "Not enough limit for attachment";
    private static final String ATTACHMENT_LIMIT = "ATTACHMENT_LIMIT";
    @Autowired
    private JWTService jwtService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private InvitationRepository invitationRepository;

    @Override
    public String saveInvitation(MeetInvitationDetailRequest request) {
        User user = jwtService.getLoggedUser();
        Event event = eventRepository.findByUserIdAndId(user.getId(), request.getEventId())
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, "Not valid event id", "EVENT_ID"));
        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            if (LicenceTypes.FREE.equals(user.getLicence().getLicenceType())) {
                throw new CalendarAppException(HttpStatus.FORBIDDEN, NOT_ENOUGH_LIMIT_FOR_ATTACHMENT, ATTACHMENT_LIMIT);
            }
            if (LicenceTypes.INDIVIDUAL.equals(user.getLicence().getLicenceType()) &&
                    request.getAttachments().size() > cacheService.getIntegerCacheValue(CacheConstants.ATTACHMENT_LIMIT_IND)) {
                throw new CalendarAppException(HttpStatus.FORBIDDEN, NOT_ENOUGH_LIMIT_FOR_ATTACHMENT, ATTACHMENT_LIMIT);
            }
            if (LicenceTypes.COMMERCIAL.equals(user.getLicence().getLicenceType()) &&
                    request.getAttachments().size() > cacheService.getIntegerCacheValue(CacheConstants.ATTACHMENT_LIMIT_COMMERCIAL)) {
                throw new CalendarAppException(HttpStatus.FORBIDDEN, NOT_ENOUGH_LIMIT_FOR_ATTACHMENT, ATTACHMENT_LIMIT);

            }
        }
        String id = UUID.randomUUID().toString().replace("-", "") + RandomStringUtils.randomAlphabetic(18);
        Invitation invitation = new Invitation();
        invitation.setInvitationId(id);
        invitation.setUserId(user.getId());
        invitation.setName(WordUtils.capitalize(request.getName()));
        invitation.setUserEmail(request.getUserMail());
        invitation.setEvent(event);
        invitation.setCcList(request.getCc());
        invitation.setBccList(request.getBcc());
        invitationRepository.save(invitation);
        return id;
    }
}
