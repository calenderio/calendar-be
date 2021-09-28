package com.io.collige.controllers.impl;

import com.io.collige.models.requests.calendar.UserCalendarItemsRequest;
import com.io.collige.models.responses.calendar.CalendarResponse;
import com.io.collige.services.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CalendarControllerImpl {

    @Autowired
    private CalendarService calendarService;

    @GetMapping("getUserCalendars")
    public ResponseEntity<CalendarResponse> getCalendarResponse(@Valid UserCalendarItemsRequest request) {
        return ResponseEntity.ok(calendarService.getAllCalendars(request));
    }

}
