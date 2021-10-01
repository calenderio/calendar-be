/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.remotes.google;

import lombok.Data;

@Data
public class GoogleCalendarEventItem {

    private String summary;
    private String description;
    private String status;
    private CalendarEventDateType start;
    private CalendarEventDateType end;

}
