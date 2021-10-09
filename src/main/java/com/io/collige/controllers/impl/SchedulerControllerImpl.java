/*
 * @author : Oguz Kahraman
 * @since : 8.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers.impl;

import com.io.collige.controllers.SchedulerController;
import com.io.collige.entitites.Event;
import com.io.collige.mappers.EventMapper;
import com.io.collige.mappers.InvitationMapper;
import com.io.collige.mappers.SchedulerMapper;
import com.io.collige.models.internals.SchedulerDetailsRequest;
import com.io.collige.models.internals.SchedulerNameUpdateRequest;
import com.io.collige.models.requests.scheduler.SchedulerUpdateRequest;
import com.io.collige.models.responses.events.EventInfoResponse;
import com.io.collige.models.responses.scheduler.SchedulerResponse;
import com.io.collige.services.EventService;
import com.io.collige.services.InvitationService;
import com.io.collige.services.SchedulerService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class SchedulerControllerImpl implements SchedulerController {

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private EventService eventService;

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private SchedulerMapper mapper;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private InvitationMapper invitationMapper;

    @Override
    public ResponseEntity<SchedulerResponse> createScheduler(@Length(max = 50, min = 1) String name) {
        SchedulerResponse schedulerResponse = new SchedulerResponse(mapper.mapEntityListToModelList(schedulerService.createScheduler(name)));
        return ResponseEntity.status(HttpStatus.CREATED).body(schedulerResponse);
    }

    @Override
    public ResponseEntity<SchedulerResponse> getSchedulers() {
        SchedulerResponse schedulerResponse = new SchedulerResponse(mapper.mapEntityListToModelList(schedulerService.getUserSchedulers()));
        return ResponseEntity.ok(schedulerResponse);
    }

    @Override
    public ResponseEntity<Void> changeName(String name, Long schedulerId) {
        schedulerService.updateName(new SchedulerNameUpdateRequest(name, schedulerId));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<SchedulerResponse> updateScheduler(@Valid SchedulerUpdateRequest request, Long schedulerId) {
        SchedulerDetailsRequest schedulerDetailsRequest = new SchedulerDetailsRequest();
        schedulerDetailsRequest.setSchedulerId(schedulerId);
        schedulerDetailsRequest.setScheduler(mapper.mapDetailsToEntity(request.getSchedule(), request.getTimeZone()));
        SchedulerResponse schedulerResponse = new SchedulerResponse(mapper.mapEntityListToModelList(schedulerService.updateScheduler(schedulerDetailsRequest)));
        return ResponseEntity.ok(schedulerResponse);
    }

    @Override
    public ResponseEntity<List<EventInfoResponse>> getSchedulerEvents(Long schedulerId) {
        schedulerService.getUserSchedulerById(schedulerId);
        List<EventInfoResponse> events = eventMapper.mapListToInfoResponseModel(eventService.findEventsByScheduler(schedulerId));
        for (EventInfoResponse response : events) {
            response.setInvitations(invitationMapper.mapEntityListToModelList(invitationService.findInvitations(response.getId())));
        }
        return ResponseEntity.ok(events);
    }

    @Override
    public ResponseEntity<Void> deleteScheduler(Long schedulerId) {
        schedulerService.getUserSchedulerById(schedulerId);
        List<Event> events = eventService.findEventsByScheduler(schedulerId);
        for (Event event : events) {
            invitationService.deleteInvitationByEvent(event.getId());
            eventService.deleteEventFileLinks(event.getId());
        }
        eventService.deleteEventsByScheduler(schedulerId);
        schedulerService.deleteScheduler(1L);
        return ResponseEntity.noContent().build();
    }

}
