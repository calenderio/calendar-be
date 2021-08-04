/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services;

import com.io.fastmeet.models.remotes.google.GoogleCalendarEventsRequest;
import com.io.fastmeet.models.remotes.google.TokenRefreshResponse;

public interface GoogleService {
    void getCalendarEvents(GoogleCalendarEventsRequest request);

    TokenRefreshResponse getNewAccessToken(String refreshToken);
}
