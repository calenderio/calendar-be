/*
 * @author : Oguz Kahraman
 * @since : 5.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.controllers.impl;

import com.io.fastmeet.controllers.ValidationController;
import com.io.fastmeet.models.requests.user.ValidationRequest;
import com.io.fastmeet.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ValidationControllerImpl implements ValidationController {

    @Autowired
    private ValidationService validationService;

    @Override
    public ResponseEntity<Void> verifyMail(@Valid ValidationRequest request) {
        validationService.verifyMail(request);
        return ResponseEntity.noContent().build();
    }

}
