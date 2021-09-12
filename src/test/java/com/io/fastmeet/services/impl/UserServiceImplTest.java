/*
 * @author : Oguz Kahraman
 * @since : 6.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services.impl;

import com.io.fastmeet.constants.GeneralMessageConstants;
import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.core.i18n.Translator;
import com.io.fastmeet.core.security.jwt.JWTService;
import com.io.fastmeet.entitites.Licence;
import com.io.fastmeet.entitites.User;
import com.io.fastmeet.mappers.UserMapper;
import com.io.fastmeet.models.internals.SocialUser;
import com.io.fastmeet.models.requests.user.AuthRequest;
import com.io.fastmeet.models.requests.user.UserCreateRequest;
import com.io.fastmeet.models.responses.license.LicenseResponse;
import com.io.fastmeet.models.responses.user.UserResponse;
import com.io.fastmeet.repositories.LinkedCalendarRepository;
import com.io.fastmeet.repositories.UserRepository;
import com.io.fastmeet.repositories.ValidationRepository;
import com.io.fastmeet.services.CloudinaryService;
import com.io.fastmeet.services.LicenceService;
import com.io.fastmeet.services.MailService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Mock
    private LicenceService licenceService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeAll
    public static void init() {
        MockedStatic<Translator> translatorMockedStatic = Mockito.mockStatic(Translator.class);
        translatorMockedStatic.when(() -> Translator.getMessage(any())).thenReturn("Error");
    }


    @Test
    void createIndividualUser() {
        UserCreateRequest createRequest = new UserCreateRequest();
        createRequest.setEmail("example@example.com");
        createRequest.setPassword("Password");
        createRequest.setTimeZone("UTC");
        UserResponse userResponse = new UserResponse();
        userResponse.setName("Example");
        userResponse.setVerified(false);
        when(userRepository.existsByEmail("example@example.com")).thenReturn(false);
        when(jwtService.createToken(any())).thenReturn("Bearer token123");
        when(userMapper.mapToModel(any())).thenReturn(userResponse);
        UserResponse userResponse1 = userService.createIndividualUser(createRequest);
        assertEquals(userResponse.getName(), userResponse1.getName());
        assertEquals("Bearer token123", userResponse1.getToken());
        assertFalse(userResponse1.getVerified());
    }

    @Test
    void socialSignUp() {
        SocialUser createRequest = new SocialUser();
        createRequest.setEmail("example@example.com");
        createRequest.setPassword("Password");
        UserResponse userResponse = new UserResponse();
        userResponse.setName("Example");
        userResponse.setLicence(new LicenseResponse());
        when(jwtService.createToken(any())).thenReturn("Bearer token123");
        when(userMapper.mapToModel(any())).thenReturn(userResponse);
        when(licenceService.generateFreeTrial()).thenReturn(new Licence());
        UserResponse userResponse1 = userService.socialSignUp(createRequest);
        assertEquals(userResponse.getName(), userResponse1.getName());
        assertEquals("Bearer token123", userResponse1.getToken());
        assertNotNull(userResponse1.getLicence());
    }

    @Test
    void loginUser() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setPassword("123456");
        authRequest.setUsername("example");
        User user = new User();
        user.setPassword("4b084be9ac9e2d6fa8651df17c6df24b324be0d01e75f07a2f56e4f2645a658d");
        user.setEmail("example");
        when(userRepository.findByEmail("example")).thenReturn(Optional.of(user));
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail("example");
        when(jwtService.createToken(any())).thenReturn("Bearer token123");
        when(userMapper.mapToModel(any())).thenReturn(userResponse);
        UserResponse userResponse1 = userService.loginUser(authRequest);
        assertEquals(userResponse.getEmail(), userResponse1.getEmail());
        assertEquals("Bearer token123", userResponse1.getToken());
    }

    @Test
    void loginUserException() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("example");
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        CalendarAppException exception = assertThrows(CalendarAppException.class, () -> userService.loginUser(authRequest));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("Error", exception.getReason());
        assertEquals(GeneralMessageConstants.WRONG_INFO_ERR, exception.getCause().getMessage());
    }

    @Test
    void loginUserPasswordException() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setPassword("123456");
        authRequest.setUsername("example");
        User user = new User();
        user.setPassword("wqeqwe");
        user.setEmail("wqeqwe");
        when(userRepository.findByEmail("example")).thenReturn(Optional.of(user));
        CalendarAppException exception = assertThrows(CalendarAppException.class, () -> userService.loginUser(authRequest));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("Error", exception.getReason());
        assertEquals(GeneralMessageConstants.WRONG_INFO_ERR, exception.getCause().getMessage());
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