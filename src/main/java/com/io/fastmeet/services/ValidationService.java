/*
 * @author : Oguz Kahraman
 * @since : 5.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services;

import com.io.fastmeet.models.requests.user.ValidationRequest;

public interface ValidationService {
    void verifyMail(ValidationRequest request);
}
