/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services;

import com.io.fastmeet.entitites.Calendar;
import com.io.fastmeet.models.requests.calendar.CalendarEventsRequest;

public interface CalendarService {
    //TODO multi calendar support
    void getCalendars(CalendarEventsRequest request, String userToken);

    Calendar createCalendarType(Calendar calendar, String token);
}
