/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GoogleApiURL {

    public static final String REFRESH_TOKEN = "https://oauth2.googleapis.com/token";
    public static final String GET_CALENDAR_EVENTS = "https://www.googleapis.com/calendar/v3/calendars/%s/events";
    public static final String REVOKE_TOKEN = "https://oauth2.googleapis.com/revoke?token=%s";

}