/*
 * @author : Oguz Kahraman
 * @since : 27.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers.impl;

import com.io.collige.models.requests.user.AuthRequest;
import com.io.collige.models.requests.user.ChangePasswordRequest;
import com.io.collige.models.requests.user.ResendVerificationMailRequest;
import com.io.collige.models.requests.user.ResetPasswordMailRequest;
import com.io.collige.models.requests.user.ResetPasswordRequest;
import com.io.collige.models.requests.user.UserCreateRequest;
import com.io.collige.models.requests.user.UserUpdateRequest;
import com.io.collige.models.responses.user.UserResponse;
import com.io.collige.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerImplTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserControllerImpl userController;

    @Test
    void createUser() {
        UserResponse userResponse = new UserResponse();
        userResponse.setName("Example");
        when(userService.createIndividualUser(any())).thenReturn(userResponse);
        ResponseEntity<UserResponse> responseEntity = userController.createUser(new UserCreateRequest());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userResponse.getName(), Objects.requireNonNull(responseEntity.getBody()).getName());
    }

    @Test
    void loginUser() {
        UserResponse userResponse = new UserResponse();
        userResponse.setName("Example");
        when(userService.loginUser(any())).thenReturn(userResponse);
        ResponseEntity<UserResponse> responseEntity = userController.loginUser(new AuthRequest());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userResponse.getName(), Objects.requireNonNull(responseEntity.getBody()).getName());
    }

    @Test
    void getDetailsByToken() {
        UserResponse userResponse = new UserResponse();
        userResponse.setName("Example");
        when(userService.getUserDetailsFromToken()).thenReturn(userResponse);
        ResponseEntity<UserResponse> responseEntity = userController.getDetailsByToken();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userResponse.getName(), Objects.requireNonNull(responseEntity.getBody()).getName());
    }

    @Test
    void changePassword() {
        ResponseEntity<Void> responseEntity = userController.changePassword(new ChangePasswordRequest());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(userService, times(1)).changePassword(any());
    }

    @Test
    void resetPasswordRequest() {
        ResponseEntity<Void> responseEntity = userController.resetPasswordRequest(new ResetPasswordMailRequest());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(userService, times(1)).resetPasswordRequest(any());
    }

    @Test
    void resetPassword() {
        ResponseEntity<Void> responseEntity = userController.resetPassword(new ResetPasswordRequest());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(userService, times(1)).resetPassword(any());
    }

    @Test
    void resendVerification() {
        ResponseEntity<Void> responseEntity = userController.resendVerification(new ResendVerificationMailRequest());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(userService, times(1)).resendValidationMail(any());
    }

    @Test
    void updateUser() {
        UserResponse userResponse = new UserResponse();
        userResponse.setName("Example");
        when(userService.updateUser(any())).thenReturn(userResponse);
        ResponseEntity<UserResponse> responseEntity = userController.updateUser(new UserUpdateRequest());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userResponse.getName(), Objects.requireNonNull(responseEntity.getBody()).getName());
    }
}