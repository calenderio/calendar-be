/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.controllers.impl;

import com.io.fastmeet.controllers.CalendarController;
import com.io.fastmeet.models.requests.calendar.CalendarEventsRequest;
import com.io.fastmeet.services.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CalendarControllerImpl implements CalendarController {

    @Autowired
    private CalendarService calendarService;

    @Override
    public ResponseEntity<Void> getCalendars(@Valid CalendarEventsRequest request, String token) {
        calendarService.getCalendars(request, token);
        return ResponseEntity.noContent().build();
    }
}
