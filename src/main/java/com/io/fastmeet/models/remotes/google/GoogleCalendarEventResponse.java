/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.remotes.google;

import lombok.Data;

import java.util.List;

@Data
public class GoogleCalendarEventResponse {

    private String timeZone;
    private List<GoogleCalendarEventItem> items;

}
