/*
 * @author : Oguz Kahraman
 * @since : 5.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.core.services;

import com.io.fastmeet.enums.CalendarProviderType;
import com.io.fastmeet.services.GoogleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RevokeTokenService {

    @Autowired
    private GoogleService googleService;

    public void revoke(String refreshToken, CalendarProviderType type) {
        if (CalendarProviderType.GOOGLE.equals(type)) {
            googleService.revokeToken(refreshToken);
        }
    }

}
