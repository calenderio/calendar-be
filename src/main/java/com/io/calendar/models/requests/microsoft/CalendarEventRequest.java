/*
 * @author : Oguz Kahraman
 * @since : 27.07.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.calendar.models.requests.microsoft;

import lombok.Data;

@Data
public class CalendarEventRequest {

    private String subject;
    private DateType start;
    private DateType end;
}
