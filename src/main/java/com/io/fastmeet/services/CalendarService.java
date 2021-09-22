/*
 * @author : Oguz Kahraman
 * @since : 22.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Set;

public interface CalendarService {
    Map<LocalDate, Set<LocalTime>> getAllCalendars(LocalDate localDate, String invitationId);
}
