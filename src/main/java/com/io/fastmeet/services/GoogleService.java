/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services;

import com.io.fastmeet.models.remotes.google.GoogleCalendarEventResponse;
import com.io.fastmeet.models.remotes.google.GoogleCalendarEventsRequest;
import com.io.fastmeet.models.remotes.google.TokenRefreshResponse;

public interface GoogleService {
    GoogleCalendarEventResponse getCalendarEvents(GoogleCalendarEventsRequest request);

    TokenRefreshResponse getNewAccessToken(String refreshToken);

    void revokeToken(String refreshToken);
}
