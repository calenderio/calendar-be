package com.io.collige.controllers.impl;

import com.io.collige.controllers.CalendarController;
import com.io.collige.models.requests.calendar.CalendarGetRequest;
import com.io.collige.models.responses.calendar.CalendarResponse;
import com.io.collige.services.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CalendarControllerImpl implements CalendarController {

    @Autowired
    private CalendarService calendarService;

    @Override
    public ResponseEntity<CalendarResponse> getUserCalendars(@Valid CalendarGetRequest request) {
        return ResponseEntity.ok(calendarService.getAllCalendars(request));
    }

}
