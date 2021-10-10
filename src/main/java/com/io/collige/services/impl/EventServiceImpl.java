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
import com.io.collige.mappers.EventMapper;
import com.io.collige.mappers.MailRequestMapper;
import com.io.collige.mappers.SchedulerMapper;
import com.io.collige.models.internals.event.ResendInvitationRequest;
import com.io.collige.models.internals.file.AttachmentModel;
import com.io.collige.models.internals.mail.GenericMailRequest;
import com.io.collige.models.requests.events.EventCreateRequest;
import com.io.collige.models.requests.events.EventUpdateRequest;
import com.io.collige.models.requests.meet.SendInvitationRequest;
import com.io.collige.models.responses.calendar.EventTypeResponse;
import com.io.collige.repositories.EventFileLinkRepository;
import com.io.collige.repositories.EventRepository;
import com.io.collige.repositories.FileLinkRepository;
import com.io.collige.services.EventService;
import com.io.collige.services.FileService;
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
@Transactional
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
    private EventMapper eventMapper;

    @Autowired
    private SchedulerMapper schedulerMapper;

    @Autowired
    private EventFileLinkRepository eventFileLinkRepository;

    @Autowired
    private FileService fileService;

    @Override
    @Transactional
    public EventTypeResponse createEvent(EventCreateRequest request) {
        User user = jwtService.getLoggedUser();
        List<FileLink> fileLinks = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(request.getFileLinks())) {
            fileLinks.addAll(fileLinkRepository.findByUserIdAndIdIn(user.getId(), new ArrayList<>(request.getFileLinks())));
            if (fileLinks.size() != request.getFileLinks().size()) {
                throw new CalendarAppException(HttpStatus.BAD_REQUEST, "No user file found", "NO_USR_FILE");
            }
        }
        Event event = eventMapper.mapRequestToEntity(request);
        event.setScheduler(schedulerMapper.mapDetailsToEntity(request.getSchedule(), request.getTimezone()));
        event.setUserId(user.getId());
        createEventDetails(event);
        for (FileLink fileLink : fileLinks) {
            EventFileLink eventFileLink = new EventFileLink();
            EventFileLinkId fileLinkId = new EventFileLinkId();
            fileLinkId.setFileId(fileLink.getId());
            fileLinkId.setUserId(user.getId());
            fileLinkId.setEventId(event.getId());
            eventFileLink.setId(fileLinkId);
            eventFileLinkRepository.save(eventFileLink);
        }
        EventTypeResponse response = eventMapper.mapEntityToModel(event);
        response.setSchedule(schedulerMapper.mapEntityToModel(event.getScheduler()));
        return response;
    }

    @Override
    @Transactional
    public EventTypeResponse updateEvent(EventUpdateRequest request) {
        User user = jwtService.getLoggedUser();
        Event exOne = eventRepository.findByUserIdAndId(user.getId(), request.getEventId())
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, NOT_VALID_EVENT_ID, EVENT_ID));
        Event event = eventMapper.mapRequestToEntity(request.getDetails());
        event.setScheduler(schedulerMapper.mapDetailsToEntity(request.getDetails().getSchedule(), request.getDetails().getTimezone()));
        event.setUserId(user.getId());
        event.setId(exOne.getId());
        event.setAnswers(exOne.getAnswers());
        createEventDetails(event);
        EventTypeResponse response = eventMapper.mapEntityToModel(event);
        response.setSchedule(schedulerMapper.mapEntityToModel(event.getScheduler()));
        return response;
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
    public List<EventTypeResponse> getEvents() {
        User user = jwtService.getLoggedUser();
        List<Event> events = eventRepository.findByUserId(user.getId());
        List<EventTypeResponse> responseList = new ArrayList<>();
        for (Event event : events) {
            EventTypeResponse response = eventMapper.mapEntityToModel(event);
            response.setSchedule(schedulerMapper.mapEntityToModel(event.getScheduler()));
            responseList.add(response);
        }
        return responseList;
    }

    @Override
    public EventTypeResponse getEvent(Long eventId) {
        User user = jwtService.getLoggedUser();
        Event event = eventRepository.findByUserIdAndId(user.getId(), eventId)
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, NOT_VALID_EVENT_ID, EVENT_ID));
        EventTypeResponse response = eventMapper.mapEntityToModel(event);
        response.setSchedule(schedulerMapper.mapEntityToModel(event.getScheduler()));
        response.setFileList(fileService.getEventFiles(eventId));
        return response;
    }

    @Override
    public void sendEventInvitation(SendInvitationRequest request) throws IOException {
        User user = jwtService.getLoggedUser();
        GenericMailRequest genericMailRequest = mapper.meetingRequestToMail(request);
        createAttachments(user, genericMailRequest, request.getFileIdList());
        String id = invitationService.saveInvitation(request);
        genericMailRequest.setInviter(user.getName());
        genericMailRequest.setCode(id);
        genericMailRequest.setCc(null);
        genericMailRequest.setBcc(null);
        genericMailRequest.setHeader(request.getTitle());
        mailService.sendInvitationMail(genericMailRequest);
    }

    @Override
    public void resendInvitation(ResendInvitationRequest request) throws IOException {
        User user = jwtService.getLoggedUser();
        Invitation invitation = invitationService.findInvitationByUserIdAndCheckLimit(request.getInvitationId());
        GenericMailRequest genericMailRequest = mapper.invitationToMail(invitation);
        genericMailRequest.setInviter(user.getName());
        createAttachments(user, genericMailRequest, request.getRequest().getFileIdList());
        genericMailRequest.setCc(null);
        genericMailRequest.setBcc(null);
        genericMailRequest.setHeader(invitation.getTitle());
        mailService.sendInvitationMail(genericMailRequest);
    }

    @Override
    public List<Event> findEventsByScheduler(Long schedulerId) {
        return eventRepository.findBySchedulerId(schedulerId);
    }

    @Override
    public void deleteEventsByScheduler(Long schedulerId) {
        eventRepository.deleteBySchedulerId(schedulerId);
    }

    @Override
    public void deleteEventFileLinks(Long eventId) {
        eventFileLinkRepository.deleteByIdEventId(eventId);
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

    private void createEventDetails(Event event) {
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
        eventRepository.save(event);
    }

}
