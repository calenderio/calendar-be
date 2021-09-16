/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.controllers.impl;

import com.io.fastmeet.controllers.EventController;
import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.entitites.Event;
import com.io.fastmeet.mappers.EventMapper;
import com.io.fastmeet.mappers.SchedulerMapper;
import com.io.fastmeet.models.requests.calendar.EventTypeCreateRequest;
import com.io.fastmeet.models.responses.calendar.EventTypeResponse;
import com.io.fastmeet.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class EventControllerImpl implements EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private SchedulerMapper schedulerMapper;

    @Override
    public ResponseEntity<EventTypeResponse> createEventType(@Valid EventTypeCreateRequest request) {
        if (request.getPreDefinedSchedulerId() == null && request.getSchedule() == null) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, "Schedule details null", "NLL_SCH");
        }
        Event event = eventMapper.mapRequestToEntity(request);
        event.setScheduler(schedulerMapper.mapDetailsToEntity(request.getSchedule(), request.getTimezone()));
        Event event1 = eventService.createCalendarType(event);
        EventTypeResponse response = eventMapper.mapEntityToModel(event1);
        response.setSchedule(schedulerMapper.mapEntityToModel(event1.getScheduler()));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<EventTypeResponse>> getEventTypes() {
        List<Event> list = eventService.getCalendarTypes();
        List<EventTypeResponse> responseList = new ArrayList<>();
        for (Event event : list) {
            EventTypeResponse response = eventMapper.mapEntityToModel(event);
            response.setSchedule(schedulerMapper.mapEntityToModel(event.getScheduler()));
            responseList.add(response);
        }
        return ResponseEntity.ok(responseList);
    }

}