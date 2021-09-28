/*
 * @author : Oguz Kahraman
 * @since : 5.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services;

import com.io.collige.models.remotes.google.TokenRefreshResponse;
import com.io.collige.models.remotes.microsoft.MicrosoftCalendarResponse;
import com.io.collige.models.remotes.microsoft.MicrosoftCalendarEventsRequest;

public interface MicrosoftService {
    MicrosoftCalendarResponse getCalendarEvents(MicrosoftCalendarEventsRequest request);

    TokenRefreshResponse getNewAccessToken(String refreshToken);
}
