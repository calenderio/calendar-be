/*
 * @author : Oguz Kahraman
 * @since : 24.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.internals;

import com.io.fastmeet.entitites.Invitation;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Set;

@Data
public class AvailableDatesDetails {

    private Map<LocalDate, Set<LocalTime>> availableDates;
    private Invitation invitation;

}
