/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services;

import com.io.collige.models.remotes.google.GoogleCalendarEventResponse;
import com.io.collige.models.remotes.google.GoogleCalendarEventsRequest;
import com.io.collige.models.remotes.google.TokenRefreshResponse;

public interface GoogleService {
    GoogleCalendarEventResponse getCalendarEvents(GoogleCalendarEventsRequest request);

    TokenRefreshResponse getNewAccessToken(String refreshToken);

    void revokeToken(String refreshToken);
}
