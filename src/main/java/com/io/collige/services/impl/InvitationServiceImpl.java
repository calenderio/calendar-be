/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.io.collige.core.exception.CalendarAppException;
import com.io.collige.core.security.jwt.JWTService;
import com.io.collige.entitites.Event;
import com.io.collige.entitites.Invitation;
import com.io.collige.entitites.User;
import com.io.collige.enums.LicenceTypes;
import com.io.collige.models.requests.meet.SendInvitationRequest;
import com.io.collige.repositories.EventRepository;
import com.io.collige.repositories.InvitationRepository;
import com.io.collige.services.InvitationService;
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
    private InvitationRepository invitationRepository;

    @Override
    public String saveInvitation(SendInvitationRequest request) {
        User user = jwtService.getLoggedUser();
        Event event = eventRepository.findByUserIdAndId(user.getId(), request.getEventId())
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, "Not valid event id", "EVENT_ID"));

        ccBccLimit(request, user);

        String id = UUID.randomUUID().toString().replace("-", "") + pwdGenerator.generate(18);
        Invitation invitation = new Invitation();
        invitation.setInvitationId(id);
        invitation.setUser(user);
        invitation.setEvent(event);
        invitation.setName(WordUtils.capitalize(request.getName()));
        invitation.setUserEmail(request.getUserMail());
        invitation.setCcList(request.getCcUsers());
        invitation.setBccList(request.getBccUsers());
        invitation.setTitle(request.getTitle());
        invitation.setDescription(request.getDescription());
        invitationRepository.save(invitation);
        return id;
    }

    @Override
    public Invitation findInvitationByUserIdAndCheckLimit(Long id) {
        User user = jwtService.getLoggedUser();
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

    @Override
    public void deleteInvitationByEvent(Long eventId) {
        User user = jwtService.getLoggedUser();
        invitationRepository.deleteInvitationByEvent(user.getId(), eventId);
    }

    private void ccBccLimit(SendInvitationRequest request, User user) {
        if (request.getBccUsers() != null && !request.getBccUsers().isEmpty() &&
                LicenceTypes.FREE.equals(user.getLicence().getLicenceType())) {
            throw new CalendarAppException(HttpStatus.FORBIDDEN, CC_LIMIT_MESSAGE, CC_LIMIT);
        }
        if (request.getCcUsers() != null && !request.getCcUsers().isEmpty() &&
                LicenceTypes.FREE.equals(user.getLicence().getLicenceType())) {
            throw new CalendarAppException(HttpStatus.FORBIDDEN, CC_LIMIT_MESSAGE, CC_LIMIT);
        }
    }

}
