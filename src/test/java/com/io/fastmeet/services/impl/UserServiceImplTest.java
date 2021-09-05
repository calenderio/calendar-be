/*
 * @author : Oguz Kahraman
 * @since : 6.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services.impl;

import com.io.fastmeet.core.security.jwt.JWTService;
import com.io.fastmeet.mappers.UserMapper;
import com.io.fastmeet.models.requests.user.UserCreateRequest;
import com.io.fastmeet.models.responses.user.UserResponse;
import com.io.fastmeet.repositories.LinkedCalendarRepository;
import com.io.fastmeet.repositories.UserRepository;
import com.io.fastmeet.repositories.ValidationRepository;
import com.io.fastmeet.services.CloudinaryService;
import com.io.fastmeet.services.MailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ValidationRepository validationRepository;

    @Mock
    private LinkedCalendarRepository calendarRepository;

    @Mock
    private JWTService jwtService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private MailService mailService;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createIndividualUser() {
        UserCreateRequest createRequest = new UserCreateRequest();
        createRequest.setEmail("example@example.com");
        createRequest.setPassword("Password");
        UserResponse userResponse = new UserResponse();
        userResponse.setName("Example");
        when(userRepository.existsByEmail("example@example.com")).thenReturn(false);
        when(jwtService.createToken(any())).thenReturn("Bearer token123");
        when(userMapper.mapToModel(any())).thenReturn(userResponse);
        UserResponse userResponse1 = userService.createIndividualUser(createRequest);
        assertEquals(userResponse.getName(), userResponse1.getName());
        assertEquals("Bearer token123", userResponse1.getToken());
    }

    @Test
    void socialSignUp() {
    }

    @Test
    void loginUser() {
    }

    @Test
    void findByMail() {
    }

    @Test
    void ifUserExist() {
    }

    @Test
    void addNewLinkToUser() {
    }

    @Test
    void createValidationInfo() {
    }

    @Test
    void getUserDetailsFromToken() {
    }

    @Test
    void gen() {
    }
}