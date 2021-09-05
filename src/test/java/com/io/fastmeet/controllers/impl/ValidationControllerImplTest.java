/*
 * @author : Oguz Kahraman
 * @since : 6.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.controllers.impl;

import com.io.fastmeet.models.requests.user.ValidationRequest;
import com.io.fastmeet.services.ValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class ValidationControllerImplTest {

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private ValidationControllerImpl validationController;

    @Test
    void verifyMail() {
        doNothing().when(validationService).verifyMail(any());
        ResponseEntity<Void> responseEntity = validationController.verifyMail(new ValidationRequest());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }
}