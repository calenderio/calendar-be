/*
 * @author : Oguz Kahraman
 * @since : 11.02.2021
 *
 * Copyright - restapi
 **/
package com.io.calendar.controllers.impl;

import com.io.calendar.controllers.UserController;
import com.io.calendar.core.annotations.SkipSecurity;
import com.io.calendar.models.requests.user.AuthRequest;
import com.io.calendar.models.requests.user.UserCreateRequest;
import com.io.calendar.models.responses.user.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserControllerImpl implements UserController {


    @Override
    @SkipSecurity
    public ResponseEntity<UserResponse> createUser(@Valid UserCreateRequest request, String language) {
        return ResponseEntity.ok(new UserResponse());
    }

    @Override
    @SkipSecurity
    public ResponseEntity<UserResponse> loginUser(@Valid AuthRequest request) {
        return ResponseEntity.ok(new UserResponse());
    }


}
