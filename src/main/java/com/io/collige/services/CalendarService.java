/*
 * @author : Oguz Kahraman
 * @since : 22.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services;

import com.io.collige.models.internals.AvailableDatesDetails;

import java.time.LocalDate;

public interface CalendarService {
    AvailableDatesDetails getAvailableDates(LocalDate localDate, String invitationId, String timeZone);
}
