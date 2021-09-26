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
import com.io.fastmeet.entitites.LinkedCalendar;
import com.io.fastmeet.entitites.User;
import com.io.fastmeet.entitites.Validation;
import com.io.fastmeet.enums.AppProviderType;
import com.io.fastmeet.enums.ValidationType;
import com.io.fastmeet.mappers.UserMapper;
import com.io.fastmeet.models.internals.SocialUser;
import com.io.fastmeet.models.requests.user.AuthRequest;
import com.io.fastmeet.models.requests.user.ChangePasswordRequest;
import com.io.fastmeet.models.requests.user.ResendVerificationMailRequest;
import com.io.fastmeet.models.requests.user.ResetPasswordMailRequest;
import com.io.fastmeet.models.requests.user.ResetPasswordRequest;
import com.io.fastmeet.models.requests.user.UserCreateRequest;
import com.io.fastmeet.models.requests.user.UserUpdateRequest;
import com.io.fastmeet.models.responses.license.LicenseResponse;
import com.io.fastmeet.models.responses.user.UserResponse;
import com.io.fastmeet.repositories.LinkedCalendarRepository;
import com.io.fastmeet.repositories.UserRepository;
import com.io.fastmeet.repositories.ValidationRepository;
import com.io.fastmeet.services.CloudinaryService;
import com.io.fastmeet.services.LicenceService;
import com.io.fastmeet.services.MailService;
import com.io.fastmeet.services.ValidationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

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
    private ValidationRepository validationRepository;

    @Mock
    private LicenceService licenceService;

    @Mock
    private ValidationService validationService;

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
    void createIndividualUser_Error() {
        UserCreateRequest createRequest = new UserCreateRequest();
        createRequest.setEmail("example@example.com");
        when(userRepository.existsByEmail("example@example.com")).thenReturn(true);
        CalendarAppException exception = assertThrows(CalendarAppException.class, () -> userService.createIndividualUser(createRequest));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(GeneralMessageConstants.USR_FOUND, exception.getCause().getMessage());
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
        UserResponse user = new UserResponse();
        user.setName("Example");
        User userModel = new User();
        when(userRepository.findByEmail("example")).thenReturn(Optional.of(userModel));
        when(userMapper.mapToModel(userModel)).thenReturn(user);
        UserResponse user1 = userService.findByMail("example");
        assertEquals("Example", user1.getName());
    }

    @Test
    void findById() {
        User user = new User();
        user.setName("Example");
        when(userRepository.existsByEmail("example")).thenReturn(true);
        boolean bool = userService.ifUserExist("example");
        assertTrue(bool);
    }

    @Test
    void ifUserExist() {
        User user = new User();
        user.setName("Example");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User user1 = userService.findById(1L);
        assertEquals("Example", user1.getName());
    }

    @Test
    void addNewLinkToUser() {
        User user = new User();
        user.setCalendars(Collections.singleton(new LinkedCalendar()));
        SocialUser socialUser = new SocialUser();
        socialUser.setSocialMediaMail("example");
        socialUser.setType(AppProviderType.MICROSOFT);
        userService.addNewLinkToUser(user, socialUser);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void addNewLinkToUser_notEmpty() {
        User user = new User();
        LinkedCalendar calendar = new LinkedCalendar();
        calendar.setSocialMail("example");
        calendar.setType(AppProviderType.MICROSOFT);
        user.setCalendars(Collections.singleton(calendar));
        SocialUser socialUser = new SocialUser();
        socialUser.setSocialMediaMail("example");
        socialUser.setType(AppProviderType.MICROSOFT);
        CalendarAppException exception = assertThrows(CalendarAppException.class, () -> userService.addNewLinkToUser(user, socialUser));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(GeneralMessageConstants.LINKED, exception.getCause().getMessage());
    }

    @Test
    void getUserDetailsFromToken() {
        User user = new User();
        UserResponse userResponse = new UserResponse();
        userResponse.setName("Example");
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(jwtService.createToken(user)).thenReturn("123456");
        when(userMapper.mapToModel(user)).thenReturn(userResponse);
        UserResponse response = userService.getUserDetailsFromToken();
        assertNotNull(response.getToken());
        assertEquals("123456", response.getToken());
    }

    @Test
    void updateUser() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setEmail("example");
        request.setTimeZone("GMT");
        User user = new User();
        user.setEmail("example2");
        when(jwtService.getLoggedUser()).thenReturn(user);
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail("Example");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.mapToModel(user)).thenReturn(userResponse);
        UserResponse response = userService.updateUser(request);
        assertEquals("Example", response.getEmail());
    }

    @Test
    void changePassword() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("123456");
        request.setNewPassword("1234567");
        User user = new User();
        user.setPassword("4b084be9ac9e2d6fa8651df17c6df24b324be0d01e75f07a2f56e4f2645a658d");
        user.setEmail("example");
        when(userRepository.save(user)).thenReturn(user);
        when(jwtService.getLoggedUser()).thenReturn(user);
        userService.changePassword(request);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void changePassword_error() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("123456");
        request.setNewPassword("1234567");
        User user = new User();
        user.setPassword("4b084be9a1df17c6df24b324be0d01e75f07a2f56e4f2645a658d");
        user.setEmail("example");
        when(jwtService.getLoggedUser()).thenReturn(user);
        CalendarAppException exception = assertThrows(CalendarAppException.class, () -> userService.changePassword(request));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals(GeneralMessageConstants.WRONG_INFO_ERR, exception.getCause().getMessage());
    }

    @Test
    void resetPasswordRequest() {
        User user = new User();
        user.setPassword("4b084be9a1df17c6df24b324be0d01e75f07a2f56e4f2645a658d");
        user.setEmail("example");
        when(userRepository.findByEmail("example")).thenReturn(Optional.of(user));
        ResetPasswordMailRequest request = new ResetPasswordMailRequest();
        request.setEmail("example");
        userService.resetPasswordRequest(request);
        verify(mailService, times(1)).sendPasswordResetMail(any());
    }

    @Test
    void resetPassword() {
        User user = new User();
        user.setPassword("4b084be9a1df17c6df24b324be0d01e75f07a2f56e4f2645a658d");
        user.setEmail("example");
        when(userRepository.findByEmail("example")).thenReturn(Optional.of(user));
        doNothing().when(validationService).verify(any());
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setEmail("example");
        request.setPassword("123");
        request.setCode("123");
        userService.resetPassword(request);
        verify(validationService, times(1)).verify(any());
    }

    @Test
    void resendValidationMail() {
        ResendVerificationMailRequest request = new ResendVerificationMailRequest();
        request.setEmail("Example");
        request.setType(ValidationType.EMAIL);
        User user = new User();
        Validation validation = new Validation();
        validation.setMail("example");
        validation.setType(ValidationType.EMAIL);
        when(userRepository.findByEmail("example")).thenReturn(Optional.of(user));
        when(validationService.getValidationDetail(any())).thenReturn(validation);
        userService.resendValidationMail(request);
        verify(mailService, times(1)).sendMailValidation(any());
    }

    @Test
    void resendValidationMail_Password() {
        ResendVerificationMailRequest request = new ResendVerificationMailRequest();
        request.setEmail("Example");
        request.setType(ValidationType.PASSWORD);
        User user = new User();
        Validation validation = new Validation();
        validation.setMail("example");
        validation.setType(ValidationType.PASSWORD);
        when(userRepository.findByEmail("example")).thenReturn(Optional.of(user));
        when(validationService.getValidationDetail(any())).thenReturn(validation);
        userService.resendValidationMail(request);
        verify(mailService, times(1)).sendPasswordResetMail(any());
    }

    @Test
    void updateToken() {
        SocialUser socialUser = new SocialUser();
        socialUser.setSocialMediaMail("example");
        when(userRepository.findByEmail("example")).thenReturn(Optional.of(new User()));
        userService.updateToken(socialUser);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void updateTokenAll() {
        SocialUser socialUser = new SocialUser();
        LinkedCalendar linkedCalendar = new LinkedCalendar();
        linkedCalendar.setId(1L);
        socialUser.setSocialMediaMail("example");
        socialUser.setRefreshToken("123");
        when(userRepository.findByEmail("example")).thenReturn(Optional.of(new User()));
        when(calendarRepository.findBySocialMailAndType(any(), any())).thenReturn(Optional.of(linkedCalendar));
        userService.updateToken(socialUser);
        verify(userRepository, times(1)).save(any());
    }

}