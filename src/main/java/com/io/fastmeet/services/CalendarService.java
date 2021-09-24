/*
 * @author : Oguz Kahraman
 * @since : 22.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services;

import com.io.fastmeet.models.internals.AvailableDatesDetails;

import java.time.LocalDate;

public interface CalendarService {
    AvailableDatesDetails getAvailableDates(LocalDate localDate, String invitationId, String timeZone);
}
