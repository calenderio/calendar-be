/*
 * @author : Oguz Kahraman
 * @since : 10.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SystemControllerImplTest {

    @InjectMocks
    private SystemControllerImpl systemController;

    @Test
    void welcomePage() {
        ResponseEntity<String> response = systemController.welcomePage();
        assertEquals("Welcome", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}