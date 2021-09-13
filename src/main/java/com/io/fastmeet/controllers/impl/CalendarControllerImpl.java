/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.controllers.impl;

import com.io.fastmeet.controllers.CalendarController;
import com.io.fastmeet.entitites.Calendar;
import com.io.fastmeet.mappers.CalendarMapper;
import com.io.fastmeet.mappers.SchedulerMapper;
import com.io.fastmeet.models.requests.calendar.CalendarTypeCreateRequest;
import com.io.fastmeet.models.responses.calendar.CalendarTypeResponse;
import com.io.fastmeet.services.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CalendarControllerImpl implements CalendarController {

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private CalendarMapper calendarMapper;

    @Autowired
    private SchedulerMapper schedulerMapper;

    @Override
    public ResponseEntity<CalendarTypeResponse> createCalendarType(@Valid CalendarTypeCreateRequest request, String token) {
        Calendar calendar = calendarMapper.mapRequestToEntity(request);
        calendar.setScheduler(schedulerMapper.mapDetailsToEntity(request.getSchedule(), request.getTimezone()));
        Calendar calendar1 = calendarService.createCalendarType(calendar, token);
        CalendarTypeResponse response = calendarMapper.mapEntityToModel(calendar1);
        response.setSchedule(schedulerMapper.mapEntityToModel(calendar1.getScheduler()));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<CalendarTypeResponse>> getCalendarTypes(String token) {
        List<Calendar> list = calendarService.getCalendarTypes(token);
        List<CalendarTypeResponse> responseList = new ArrayList<>();
        for (Calendar calendar : list) {
            CalendarTypeResponse response = calendarMapper.mapEntityToModel(calendar);
            response.setSchedule(schedulerMapper.mapEntityToModel(calendar.getScheduler()));
            responseList.add(response);
        }
        return ResponseEntity.ok(responseList);
    }

}