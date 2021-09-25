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
import com.io.fastmeet.models.internals.AttachmentModel;
import com.io.fastmeet.models.internals.MeetInvitationDetailRequest;
import com.io.fastmeet.repositories.EventRepository;
import com.io.fastmeet.repositories.InvitationRepository;
import com.io.fastmeet.services.InvitationService;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class InvitationServiceImpl implements InvitationService {

    private static final String NOT_ENOUGH_LIMIT_FOR_ATTACHMENT = "Not enough limit for attachment";
    private static final String ATTACHMENT_LIMIT = "ATTACHMENT_LIMIT";
    private static final String CC_LIMIT = "CC_LIMIT";
    private static final String CC_LIMIT_MESSAGE = "Free users can not send cc and bcc";
    private final RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange('0', 'z')
            .filteredBy(CharacterPredicates.DIGITS, CharacterPredicates.LETTERS)
            .build();

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

        limitChecker(request.getAttachments(), user);
        ccBccLimit(request, user);

        String id = UUID.randomUUID().toString().replace("-", "") + pwdGenerator.generate(18);
        Invitation invitation = new Invitation();
        invitation.setInvitationId(id);
        invitation.setUser(user);
        invitation.setEvent(event);
        invitation.setName(WordUtils.capitalize(request.getName()));
        invitation.setUserEmail(request.getUserMail());
        invitation.setCcList(request.getCc());
        invitation.setBccList(request.getBcc());
        invitation.setTitle(request.getTitle());
        invitation.setDescription(request.getDescription());
        invitationRepository.save(invitation);
        return id;
    }

    @Override
    public Invitation findInvitationByUserId(Long id, List<AttachmentModel> attachments) {
        User user = jwtService.getLoggedUser();
        limitChecker(attachments, user);
        return invitationRepository.findByIdAndUserAndScheduledIsFalse(id, user)
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, "Not valid invitation id", "INVITATION_ID"));
    }

    @Override
    public List<Invitation> findInvitations(Long eventId) {
        User user = jwtService.getLoggedUser();
        return invitationRepository.findByUserAndEvent_Id(user, eventId);
    }

    @Override
    public void deleteInvitation(Long meetingId) {
        User user = jwtService.getLoggedUser();
        invitationRepository.deleteInvitation(user.getId(), meetingId);
    }

    private void ccBccLimit(MeetInvitationDetailRequest request, User user) {
        if (request.getBcc() != null && !request.getBcc().isEmpty() &&
                LicenceTypes.FREE.equals(user.getLicence().getLicenceType())) {
            throw new CalendarAppException(HttpStatus.FORBIDDEN, CC_LIMIT_MESSAGE, CC_LIMIT);
        }
        if (request.getCc() != null && !request.getCc().isEmpty() &&
                LicenceTypes.FREE.equals(user.getLicence().getLicenceType())) {
            throw new CalendarAppException(HttpStatus.FORBIDDEN, CC_LIMIT_MESSAGE, CC_LIMIT);
        }
    }

    private void limitChecker(List<AttachmentModel> attachments, User user) {
        if (attachments != null && !attachments.isEmpty()) {
            if (LicenceTypes.FREE.equals(user.getLicence().getLicenceType())) {
                throw new CalendarAppException(HttpStatus.FORBIDDEN, NOT_ENOUGH_LIMIT_FOR_ATTACHMENT, ATTACHMENT_LIMIT);
            }
            if (LicenceTypes.INDIVIDUAL.equals(user.getLicence().getLicenceType()) &&
                    attachments.size() > cacheService.getIntegerCacheValue(CacheConstants.ATTACHMENT_LIMIT_IND)) {
                throw new CalendarAppException(HttpStatus.FORBIDDEN, NOT_ENOUGH_LIMIT_FOR_ATTACHMENT, ATTACHMENT_LIMIT);
            }
            if (LicenceTypes.COMMERCIAL.equals(user.getLicence().getLicenceType()) &&
                    attachments.size() > cacheService.getIntegerCacheValue(CacheConstants.ATTACHMENT_LIMIT_COMMERCIAL)) {
                throw new CalendarAppException(HttpStatus.FORBIDDEN, NOT_ENOUGH_LIMIT_FOR_ATTACHMENT, ATTACHMENT_LIMIT);

            }
        }
    }

}
