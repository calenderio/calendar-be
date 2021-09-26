/*
 * @author : Oguz Kahraman
 * @since : 5.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services;

import com.io.collige.entitites.Validation;
import com.io.collige.models.internals.ResendValidation;
import com.io.collige.models.requests.user.ValidationRequest;

public interface ValidationService {
    void verifyMail(ValidationRequest request);

    void verify(ValidationRequest request);

    Validation getValidationDetail(ResendValidation request);
}