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
import com.io.collige.entitites.EventFileLink;
import com.io.collige.entitites.EventFileLinkId;
import com.io.collige.entitites.FileLink;
import com.io.collige.entitites.Invitation;
import com.io.collige.entitites.Question;
import com.io.collige.entitites.Scheduler;
import com.io.collige.entitites.User;
import com.io.collige.mappers.MailRequestMapper;
import com.io.collige.models.internals.AttachmentModel;
import com.io.collige.models.internals.CreateEventRequest;
import com.io.collige.models.internals.GenericMailRequest;
import com.io.collige.models.internals.MeetInvitationDetailRequest;
import com.io.collige.models.internals.UpdateEventRequest;
import com.io.collige.models.requests.meet.InvitationResendRequest;
import com.io.collige.repositories.EventFileLinkRepository;
import com.io.collige.repositories.EventRepository;
import com.io.collige.repositories.FileLinkRepository;
import com.io.collige.services.EventService;
import com.io.collige.services.InvitationService;
import com.io.collige.services.MailService;
import com.io.collige.services.SchedulerService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EventServiceImpl implements EventService {

    private static final String NOT_VALID_EVENT_ID = "Not valid event id";
    private static final String EVENT_ID = "EVENT_ID";
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

    @Autowired
    private FileLinkRepository fileLinkRepository;

    @Autowired
    private EventFileLinkRepository eventFileLinkRepository;

    @Override
    @Transactional
    public Event createEvent(CreateEventRequest request) {
        User user = jwtService.getLoggedUser();
        List<FileLink> fileLinks = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(request.getFileLinks())) {
            fileLinks.addAll(fileLinkRepository.findByUserIdAndIdIn(user.getId(), new ArrayList<>(request.getFileLinks())));
            if (fileLinks.size() != request.getFileLinks().size()) {
                throw new CalendarAppException(HttpStatus.BAD_REQUEST, "No user file found", "NO_USR_FILE");
            }
        }
        request.getEvent().setUserId(user.getId());
        Event event = createEventDetails(request.getEvent());
        for (FileLink fileLink : fileLinks) {
            EventFileLink eventFileLink = new EventFileLink();
            EventFileLinkId fileLinkId = new EventFileLinkId();
            fileLinkId.setFileId(fileLink.getId());
            fileLinkId.setUserId(user.getId());
            fileLinkId.setEventId(event.getId());
            eventFileLink.setId(fileLinkId);
            eventFileLinkRepository.save(eventFileLink);
        }
        return event;
    }

    @Override
    @Transactional
    public Event updateEvent(UpdateEventRequest request) {
        User user = jwtService.getLoggedUser();
        Event exOne = eventRepository.findByUserIdAndId(user.getId(), request.getEventId())
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, NOT_VALID_EVENT_ID, EVENT_ID));
        Event event = request.getEvent();
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
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, NOT_VALID_EVENT_ID, EVENT_ID));
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
    public Event getEvent(Long eventId) {
        User user = jwtService.getLoggedUser();
        return eventRepository.findByUserIdAndId(user.getId(), eventId)
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, NOT_VALID_EVENT_ID, EVENT_ID));
    }

    @Override
    public void sendEventInvitation(MeetInvitationDetailRequest request) throws IOException {
        User user = jwtService.getLoggedUser();
        GenericMailRequest genericMailRequest = mapper.meetingRequestToMail(request);
        createAttachments(user, genericMailRequest, request.getIdList());
        String id = invitationService.saveInvitation(request);
        genericMailRequest.setInviter(user.getName());
        genericMailRequest.setCode(id);
        genericMailRequest.setCc(null);
        genericMailRequest.setBcc(null);
        genericMailRequest.setHeader(request.getTitle());
        mailService.sendInvitationMail(genericMailRequest);
    }

    @Override
    public void resendInvitation(Long invitationId, InvitationResendRequest request) throws IOException {
        User user = jwtService.getLoggedUser();
        Invitation invitation = invitationService.findInvitationByUserIdAndCheckLimit(invitationId);
        GenericMailRequest genericMailRequest = mapper.invitationToMail(invitation);
        genericMailRequest.setInviter(user.getName());
        createAttachments(user, genericMailRequest, request.getFileIdList());
        genericMailRequest.setCc(null);
        genericMailRequest.setBcc(null);
        genericMailRequest.setHeader(invitation.getTitle());
        mailService.sendInvitationMail(genericMailRequest);
    }

    private void createAttachments(User user, GenericMailRequest genericMailRequest, Set<Long> fileIdList) throws IOException {
        if (CollectionUtils.isNotEmpty(fileIdList)) {
            List<FileLink> fileLinks = fileLinkRepository.findByUserIdAndIdIn(user.getId(), new ArrayList<>(fileIdList));
            if (fileLinks.size() != fileIdList.size()) {
                throw new CalendarAppException(HttpStatus.BAD_REQUEST, "No user file found", "NO_USR_FILE");
            }
            List<AttachmentModel> attachmentModels = new ArrayList<>();
            for (FileLink fileLink : fileLinks) {
                AttachmentModel attachmentModel = new AttachmentModel();
                attachmentModel.setName(fileLink.getName());
                attachmentModel.setSize(fileLink.getSize());
                attachmentModel.setType(fileLink.getType());
                attachmentModel.setData(IOUtils.toByteArray(new URL(fileLink.getLink())));
                attachmentModels.add(attachmentModel);
            }
            genericMailRequest.setAttachments(attachmentModels);
        }
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
