/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services;

import com.io.fastmeet.entitites.Event;
import com.io.fastmeet.models.requests.calendar.CalendarEventsRequest;

import java.util.List;

public interface EventService {
    //TODO multi calendar support
    void getCalendars(CalendarEventsRequest request, String userToken);

    Event createCalendarType(Event event);

    List<Event> getCalendarTypes();
}
