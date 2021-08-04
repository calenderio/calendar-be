/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.remotes.google;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class GoogleCalendarEventsRequest {
    private ZonedDateTime timeMax;
    private ZonedDateTime timeMin;
    private String timeZone;
    private String userName;
    private String accessToken;
}
