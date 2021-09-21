/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services.impl;

import com.io.fastmeet.core.security.jwt.JWTService;
import com.io.fastmeet.entitites.Event;
import com.io.fastmeet.entitites.Invitation;
import com.io.fastmeet.entitites.LinkedCalendar;
import com.io.fastmeet.entitites.Question;
import com.io.fastmeet.entitites.Scheduler;
import com.io.fastmeet.entitites.User;
import com.io.fastmeet.mappers.MailRequestMapper;
import com.io.fastmeet.models.internals.AttachmentModel;
import com.io.fastmeet.models.internals.GenericMailRequest;
import com.io.fastmeet.models.internals.MeetInvitationDetailRequest;
import com.io.fastmeet.models.requests.calendar.CalendarEventsRequest;
import com.io.fastmeet.repositories.EventRepository;
import com.io.fastmeet.services.EventService;
import com.io.fastmeet.services.InvitationService;
import com.io.fastmeet.services.MailService;
import com.io.fastmeet.services.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
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

    //TODO multi calendar support
    @Override
    public void getCalendars(CalendarEventsRequest request, String userToken) {
        User user = jwtService.getLoggedUser();
        Set<LinkedCalendar> calendars = user.getCalendars();
    }

    @Override
    public Event createEvent(Event event) {
        User user = jwtService.getLoggedUser();
        event.setUserId(user.getId());
        if (event.getPreDefinedSchedulerId() != null) {
            event.setScheduler(schedulerService.getUserSchedulerById(event.getPreDefinedSchedulerId(), user.getId()));
        } else {
            Scheduler scheduler = event.getScheduler();
            scheduler.setName(event.getName());
            event.setScheduler(schedulerService.saveCalendarTypeScheduler(scheduler, user.getId()));
        }
        for (Question question : event.getQuestions()) {
            question.setEvent(event);
        }
        return eventRepository.save(event);
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
        genericMailRequest.setInviter(user.getName());
        String id = invitationService.saveInvitation(request);
        genericMailRequest.setCode(id);
        mailService.sendInvitationMail(genericMailRequest);
    }

    @Override
    public void resendInvitation(Long invitationId, List<AttachmentModel> attachments) {
        User user = jwtService.getLoggedUser();
        Invitation invitation = invitationService.findInvitationByUserId(invitationId, attachments);
        GenericMailRequest genericMailRequest = mapper.meetingRequestToMail(invitation);
        genericMailRequest.setInviter(user.getName());
        genericMailRequest.setAttachments(attachments);
        mailService.sendInvitationMail(genericMailRequest);
    }

}
