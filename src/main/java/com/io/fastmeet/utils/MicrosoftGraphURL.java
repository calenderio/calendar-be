/*
 * @author : Oguz Kahraman
 * @since : 27.07.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MicrosoftGraphURL {

    public static final String CREATE_CALENDAR_URL = "https://graph.microsoft.com/v1.0/me/events";
    public static final String CALENDAR_URL = "https://graph.microsoft.com/v1.0/me/calendarview";
    public static final String CALENDAR_ALL_URL = "https://graph.microsoft.com/v1.0/me/events?$select=subject,body,bodyPreview,organizer,attendees,start,end,location";
    public static final String ADD_ATTACHMENT_URL = "https://graph.microsoft.com/v1.0/me/events/%s/attachments";
    public static final String REFRESH_TOKEN = "https://login.microsoftonline.com/common/oauth2/v2.0/token";

}
