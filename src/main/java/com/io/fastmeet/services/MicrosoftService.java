/*
 * @author : Oguz Kahraman
 * @since : 5.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services;

import com.io.fastmeet.models.remotes.google.TokenRefreshResponse;
import com.io.fastmeet.models.remotes.microsoft.CalendarResponse;
import com.io.fastmeet.models.remotes.microsoft.MicrosoftCalendarEventsRequest;

public interface MicrosoftService {
    CalendarResponse getCalendarEvents(MicrosoftCalendarEventsRequest request);

    TokenRefreshResponse getNewAccessToken(String refreshToken);
}
