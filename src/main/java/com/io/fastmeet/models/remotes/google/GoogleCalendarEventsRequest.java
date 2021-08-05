/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.remotes.google;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GoogleCalendarEventsRequest {
    private LocalDateTime timeMax;
    private LocalDateTime timeMin;
    private String timeZone;
    private String userName;
    private String accessToken;
}
