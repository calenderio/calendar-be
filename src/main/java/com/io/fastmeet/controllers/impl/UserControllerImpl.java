/*
 * @author : Oguz Kahraman
 * @since : 11.02.2021
 *
 * Copyright - restapi
 **/
package com.io.fastmeet.controllers.impl;

import com.io.fastmeet.controllers.UserController;
import com.io.fastmeet.core.annotations.SkipSecurity;
import com.io.fastmeet.models.requests.user.AuthRequest;
import com.io.fastmeet.models.requests.user.ChangePasswordRequest;
import com.io.fastmeet.models.requests.user.ResetPasswordMailRequest;
import com.io.fastmeet.models.requests.user.ResetPasswordRequest;
import com.io.fastmeet.models.requests.user.UserCreateRequest;
import com.io.fastmeet.models.responses.user.UserResponse;
import com.io.fastmeet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserControllerImpl implements UserController {

    @Autowired
    private UserService userService;

    @Override
    @SkipSecurity
    public ResponseEntity<UserResponse> createUser(@Valid UserCreateRequest request, String language) {
        return ResponseEntity.ok(userService.createIndividualUser(request));
    }

    @Override
    @SkipSecurity
    public ResponseEntity<UserResponse> loginUser(@Valid AuthRequest request) {
        return ResponseEntity.ok(userService.loginUser(request));
    }

    @Override
    public ResponseEntity<UserResponse> getDetailsByToken(String token) {
        return ResponseEntity.ok(userService.getUserDetailsFromToken(token));
    }

    @Override
    public ResponseEntity<Void> changePassword(@Valid ChangePasswordRequest request, String token) {
        userService.changePassword(request, token);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> resetPasswordRequest(@Valid ResetPasswordMailRequest request, String language) {
        userService.resetPasswordRequest(request, language);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> resetPassword(@Valid ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/userping", method = RequestMethod.GET)
    public String userPing() {
        return "Any User Can Read This";
    }

    @PreAuthorize("hasRole('NOONE')")
    @RequestMapping(value = "/noone", method = RequestMethod.GET)
    public String usernoone() {
        return "Not accessible";
    }


}
