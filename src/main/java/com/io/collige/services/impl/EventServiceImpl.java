/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.io.collige.core.exception.CalendarAppException;
import com.io.collige.core.security.jwt.JWTService;
import com.io.collige.entitites.Event;
import com.io.collige.entitites.Invitation;
import com.io.collige.entitites.Question;
import com.io.collige.entitites.Scheduler;
import com.io.collige.entitites.User;
import com.io.collige.mappers.MailRequestMapper;
import com.io.collige.models.internals.AttachmentModel;
import com.io.collige.models.internals.GenericMailRequest;
import com.io.collige.models.internals.MeetInvitationDetailRequest;
import com.io.collige.repositories.EventRepository;
import com.io.collige.services.EventService;
import com.io.collige.services.InvitationService;
import com.io.collige.services.MailService;
import com.io.collige.services.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private MailRequestMapper mapper;

    @Autowired
    private MailService mailService;

    @Override
    @Transactional
    public Event createEvent(Event event) {
        User user = jwtService.getLoggedUser();
        event.setUserId(user.getId());
        return createEventDetails(event);
    }

    @Override
    @Transactional
    public Event updateEvent(Event event, Long eventId) {
        User user = jwtService.getLoggedUser();
        Event exOne = eventRepository.findByUserIdAndId(user.getId(), eventId)
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, "Not valid event id", "EVENT_ID"));
        event.setUserId(user.getId());
        event.setId(exOne.getId());
        event.setAnswers(exOne.getAnswers());
        return createEventDetails(event);
    }

    @Override
    @Transactional
    public void deleteEvent(Long eventId) {
        User user = jwtService.getLoggedUser();
        Event exOne = eventRepository.findByUserIdAndId(user.getId(), eventId)
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, "Not valid event id", "EVENT_ID"));
        invitationService.deleteInvitationByEvent(eventId);
        schedulerService.deleteEventScheduler(exOne.getScheduler().getId());
        eventRepository.delete(exOne);
    }

    @Override
    public List<Event> getEvents() {
        User user = jwtService.getLoggedUser();
        return eventRepository.findByUserId(user.getId());
    }

    @Override
    public void sendEventInvitation(MeetInvitationDetailRequest request) {
        User user = jwtService.getLoggedUser();
        GenericMailRequest genericMailRequest = mapper.meetingRequestToMail(request);
        String id = invitationService.saveInvitation(request);
        genericMailRequest.setInviter(user.getName());
        genericMailRequest.setCode(id);
        genericMailRequest.setCc(null);
        genericMailRequest.setBcc(null);
        genericMailRequest.setHeader(request.getTitle());
        mailService.sendInvitationMail(genericMailRequest);
    }

    @Override
    public void resendInvitation(Long invitationId, List<AttachmentModel> attachments) {
        User user = jwtService.getLoggedUser();
        Invitation invitation = invitationService.findInvitationByUserIdAndCheckLimit(invitationId, attachments);
        GenericMailRequest genericMailRequest = mapper.invitationToMail(invitation);
        genericMailRequest.setInviter(user.getName());
        genericMailRequest.setAttachments(attachments);
        genericMailRequest.setCc(null);
        genericMailRequest.setBcc(null);
        genericMailRequest.setHeader(invitation.getTitle());
        mailService.sendInvitationMail(genericMailRequest);
    }

    private Event createEventDetails(Event event) {
        if (event.getPreDefinedSchedulerId() != null) {
            event.setScheduler(schedulerService.getUserSchedulerById(event.getPreDefinedSchedulerId()));
        } else {
            Scheduler scheduler = event.getScheduler();
            scheduler.setName(event.getName());
            event.setScheduler(schedulerService.saveCalendarTypeScheduler(scheduler));
        }
        if (event.getQuestions() != null) {
            for (Question question : event.getQuestions()) {
                question.setEvent(event);
            }
        }
        return eventRepository.save(event);
    }

}
