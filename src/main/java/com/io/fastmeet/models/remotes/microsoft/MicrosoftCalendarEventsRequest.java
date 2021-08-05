/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.remotes.microsoft;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MicrosoftCalendarEventsRequest {
    private LocalDateTime timeMax;
    private LocalDateTime timeMin;
    private String timeZone;
    private String accessToken;
}
