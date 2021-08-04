/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.remotes.google;

import lombok.Data;

@Data
public class GoogleCalendarEventItem {

    private String summary;
    private String description;
    private String confirmed;
    private CalendarEventDateType start;
    private CalendarEventDateType end;

}
