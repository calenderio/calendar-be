/*
 * @author : Oguz Kahraman
 * @since : 22.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services;

import com.io.collige.models.internals.event.AvailableDatesDetails;
import com.io.collige.models.requests.calendar.CalendarGetRequest;
import com.io.collige.models.requests.meet.GetAvailableDateRequest;
import com.io.collige.models.responses.calendar.CalendarResponse;

public interface CalendarService {
    AvailableDatesDetails getAvailableDates(GetAvailableDateRequest request);

    CalendarResponse getAllCalendars(CalendarGetRequest request);
}
