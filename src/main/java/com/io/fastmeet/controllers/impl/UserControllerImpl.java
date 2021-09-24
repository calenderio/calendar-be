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
import com.io.fastmeet.models.requests.user.ResendVerificationMailRequest;
import com.io.fastmeet.models.requests.user.ResetPasswordMailRequest;
import com.io.fastmeet.models.requests.user.ResetPasswordRequest;
import com.io.fastmeet.models.requests.user.UserCreateRequest;
import com.io.fastmeet.models.requests.user.UserUpdateRequest;
import com.io.fastmeet.models.responses.user.UserResponse;
import com.io.fastmeet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserControllerImpl implements UserController {

    @Autowired
    private UserService userService;

    @Override
    @SkipSecurity
    public ResponseEntity<UserResponse> createUser(@Valid UserCreateRequest request) {
        return ResponseEntity.ok(userService.createIndividualUser(request));
    }

    @Override
    @SkipSecurity
    public ResponseEntity<UserResponse> loginUser(@Valid AuthRequest request) {
        return ResponseEntity.ok(userService.loginUser(request));
    }

    @Override
    public ResponseEntity<UserResponse> getDetailsByToken() {
        return ResponseEntity.ok(userService.getUserDetailsFromToken());
    }

    @Override
    public ResponseEntity<Void> changePassword(@Valid ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> resetPasswordRequest(@Valid ResetPasswordMailRequest request) {
        userService.resetPasswordRequest(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> resetPassword(@Valid ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> resendVerification(ResendVerificationMailRequest request) {
        userService.resendValidationMail(request);
        return ResponseEntity.noContent().build();

    }

    @Override
    public ResponseEntity<UserResponse> updateUser(UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(request));
    }

}
