/*
 * @author : Oguz Kahraman
 * @since : 3.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services.impl;

import com.io.fastmeet.constants.GeneralMessageConstants;
import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.core.i18n.Translator;
import com.io.fastmeet.core.security.jwt.JWTService;
import com.io.fastmeet.entitites.LinkedCalendar;
import com.io.fastmeet.entitites.User;
import com.io.fastmeet.entitites.Validation;
import com.io.fastmeet.enums.AppProviderType;
import com.io.fastmeet.enums.ValidationType;
import com.io.fastmeet.mappers.UserMapper;
import com.io.fastmeet.models.internals.GenericMailRequest;
import com.io.fastmeet.models.internals.ResendValidation;
import com.io.fastmeet.models.internals.SocialUser;
import com.io.fastmeet.models.requests.user.AuthRequest;
import com.io.fastmeet.models.requests.user.ChangePasswordRequest;
import com.io.fastmeet.models.requests.user.ResendVerificationMailRequest;
import com.io.fastmeet.models.requests.user.ResetPasswordMailRequest;
import com.io.fastmeet.models.requests.user.ResetPasswordRequest;
import com.io.fastmeet.models.requests.user.UserCreateRequest;
import com.io.fastmeet.models.requests.user.UserUpdateRequest;
import com.io.fastmeet.models.requests.user.ValidationRequest;
import com.io.fastmeet.models.responses.user.UserResponse;
import com.io.fastmeet.repositories.LinkedCalendarRepository;
import com.io.fastmeet.repositories.UserRepository;
import com.io.fastmeet.repositories.ValidationRepository;
import com.io.fastmeet.services.CloudinaryService;
import com.io.fastmeet.services.LicenceService;
import com.io.fastmeet.services.MailService;
import com.io.fastmeet.services.UserService;
import com.io.fastmeet.services.ValidationService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(50, 50).build();


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationRepository validationRepository;

    @Autowired
    private LinkedCalendarRepository calendarRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailService mailService;

    @Autowired
    private LicenceService licenceService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ValidationService validationService;

    /**
     * This method creates new individual user
     *
     * @param request user details
     */
    @Override
    public UserResponse createIndividualUser(UserCreateRequest request) {
        ifUserExistWithError(request.getEmail().toLowerCase());
        User user = new User();
        user.setEmail(request.getEmail().toLowerCase());
        user.setName(WordUtils.capitalize(request.getName()));
        user.setIsCompany(false);
        user.setTimeZone(TimeZone.getTimeZone(request.getTimeZone()) != null ?
                TimeZone.getTimeZone(request.getTimeZone()).getID() : TimeZone.getDefault().getID());
        user.setPassword(encodePassword(request.getPassword(), request.getEmail().toLowerCase()));
        user.setLicence(licenceService.generateFreeTrial());
        userRepository.save(user);
        mailService.sendMailValidation(new GenericMailRequest(Collections.singleton(user.getEmail()), user.getName(),
                createValidationInfo(user, ValidationType.EMAIL), Translator.getLanguage()));
        UserResponse response = userMapper.mapToModel(user);
        response.setToken(jwtService.createToken(user));
        return response;
    }

    /**
     * This method creates new individual user
     *
     * @param request user details
     */
    @Override
    public UserResponse socialSignUp(SocialUser request) {
        User user = new User();
        user.setEmail(request.getEmail().toLowerCase());
        user.setName(WordUtils.capitalize(request.getName()));
        user.setIsCompany(false);
        user.setPassword(encodePassword(request.getPassword(), request.getEmail().toLowerCase()));
        user.setVerified(true);
        user.setLicence(licenceService.generateFreeTrial());
        user.setPicture(cloudinaryService.uploadPhoto(request.getPictureUrl(), request.getEmail().toLowerCase()));
        user.setTimeZone(TimeZone.getDefault().getID());
        addCalendar(request, user);
        userRepository.save(user);
        UserResponse response = userMapper.mapToModel(user);
        response.setToken(jwtService.createToken(user));
        return response;
    }

    /**
     * Performs login functions
     * Checks user
     * Checks password
     *
     * @param authRequest request data
     * @return user data
     */
    @Override
    public UserResponse loginUser(AuthRequest authRequest) {
        User user = userRepository.findByEmail(authRequest.getUsername().toLowerCase())
                .orElseThrow(() -> new CalendarAppException(HttpStatus.FORBIDDEN, Translator.getMessage(GeneralMessageConstants.WRONG_INFO),
                        GeneralMessageConstants.WRONG_INFO_ERR));
        if (encodePassword(authRequest.getPassword(), user.getEmail().toLowerCase()).equals(user.getPassword())) {
            UserResponse userResponse = userMapper.mapToModel(user);
            userResponse.setToken(jwtService.createToken(user));
            return userResponse;
        }
        throw new CalendarAppException(HttpStatus.FORBIDDEN, Translator.getMessage(GeneralMessageConstants.WRONG_INFO),
                GeneralMessageConstants.WRONG_INFO_ERR);
    }

    /**
     * This method creates new individual user
     *
     * @param id id of user
     */
    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageConstants.USER_NOT_FOUND),
                        GeneralMessageConstants.USR_NOT_FOUND));
    }

    /**
     * This method gets user information
     *
     * @param email mail of user
     */
    @Override
    public UserResponse findByMail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageConstants.USER_NOT_FOUND),
                        GeneralMessageConstants.USR_NOT_FOUND));
        UserResponse userResponse = userMapper.mapToModel(user);
        userResponse.setToken(jwtService.createToken(user));
        return userResponse;
    }

    /**
     * Checks if user exist or not
     *
     * @param mail user
     */
    @Override
    public boolean ifUserExist(String mail) {
        return userRepository.existsByEmail(mail);
    }

    /**
     * Checks if user has link if no add to user
     *
     * @param request create object
     * @param user    user object
     */
    @Override
    public void addNewLinkToUser(User user, SocialUser request) {
        List<LinkedCalendar> userLink = user.getCalendars().stream().filter(item -> request.getSocialMediaMail().equals(item.getSocialMail())
                && request.getType().equals(item.getType())).collect(Collectors.toList());
        if (!userLink.isEmpty()) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage("error.linked"),
                    GeneralMessageConstants.LINKED);
        }
        addCalendar(request, user);
        userRepository.save(user);
    }

    /**
     * Create new validation information for user
     *
     * @param user
     * @param type validation type
     */
    @Async
    public String createValidationInfo(User user, ValidationType type) {
        Validation validation = validationRepository.findByMailAndType(user.getEmail(), ValidationType.EMAIL).orElse(new Validation());
        validation.setUserId(user.getId());
        validation.setCode(pwdGenerator.generate(50));
        validation.setMail(user.getEmail());
        validation.setDate(LocalDateTime.now());
        validation.setType(type);
        validationRepository.save(validation);
        return validation.getCode();
    }

    /**
     * Get details by user token
     *
     * @return User detail object
     * @throws CalendarAppException if user not exist
     * @see com.io.fastmeet.models.responses.user.UserResponse for return object details
     */
    @Override
    public UserResponse getUserDetailsFromToken() {
        UserResponse response = userMapper.mapToModel(jwtService.getLoggedUser());
        response.setToken("token");
        return response;
    }

    /**
     * Update user
     *
     * @param userUpdateRequest new details
     * @throws CalendarAppException if user not exist
     */
    @Override
    public UserResponse updateUser(UserUpdateRequest userUpdateRequest) {
        User user = jwtService.getLoggedUser();
        if (!userUpdateRequest.getEmail().equals(user.getEmail())) {
            user.setVerified(false);
            mailService.sendMailValidation(new GenericMailRequest(Collections.singleton(user.getEmail()), user.getName(),
                    createValidationInfo(user, ValidationType.EMAIL), Translator.getLanguage()));
        }
        user.setEmail(userUpdateRequest.getEmail());
        user.setName(userUpdateRequest.getName());
        user.setPicture(userUpdateRequest.getPicture());
        user.setTimeZone(TimeZone.getTimeZone(userUpdateRequest.getTimeZone()).getID());
        return userMapper.mapToModel(userRepository.save(user));
    }

    /**
     * Performs change password for logged in user
     *
     * @param request new and old password
     * @throws CalendarAppException if user not exist or infos wrong
     */
    @Override
    public void changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (!encodePassword(request.getOldPassword(), user.getEmail().toLowerCase()).equals(user.getPassword())) {
            throw new CalendarAppException(HttpStatus.FORBIDDEN, Translator.getMessage(GeneralMessageConstants.WRONG_INFO),
                    GeneralMessageConstants.WRONG_INFO_ERR);
        }
        user.setPassword(encodePassword(request.getNewPassword(), user.getEmail()));
        userRepository.save(user);
    }

    /**
     * Performs change password for logged in user
     *
     * @param request user mail
     * @throws CalendarAppException if user not exist or infos wrong
     */
    @Override
    public void resetPasswordRequest(ResetPasswordMailRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageConstants.USER_NOT_FOUND),
                        GeneralMessageConstants.USR_NOT_FOUND));
        GenericMailRequest mailRequest = new GenericMailRequest();
        mailRequest.setName(user.getName());
        mailRequest.setCode(createValidationInfo(user, ValidationType.PASSWORD));
        mailRequest.setEmails(Collections.singleton(request.getEmail()));
        mailService.sendPasswordResetMail(mailRequest);
    }

    /**
     * Reset password
     *
     * @param request password details
     * @throws CalendarAppException if user not exist or infos wrong
     */
    @Override
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageConstants.USER_NOT_FOUND),
                        GeneralMessageConstants.USR_NOT_FOUND));
        validationService.verify(new ValidationRequest(request.getCode(), request.getEmail()));
        user.setPassword(encodePassword(request.getPassword(), user.getEmail()));
    }


    /**
     * Resend validation
     *
     * @param request mail details
     * @throws CalendarAppException if validation info not exist
     */
    @Override
    public void resendValidationMail(ResendVerificationMailRequest request) {
        Validation validation = validationService.getValidationDetail(new ResendValidation(request.getEmail(), request.getType()));
        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageConstants.USER_NOT_FOUND),
                        GeneralMessageConstants.USR_NOT_FOUND));
        if (ValidationType.EMAIL.equals(request.getType())) {
            mailService.sendMailValidation(new GenericMailRequest(Collections.singleton(request.getEmail()), user.getName(),
                    validation.getCode(), Translator.getLanguage()));
        } else {
            mailService.sendPasswordResetMail(new GenericMailRequest(Collections.singleton(request.getEmail()), user.getName(),
                    validation.getCode(), Translator.getLanguage()));
        }
    }

    /**
     * UpdatesToken
     *
     * @param request social login request
     */
    @Override
    public void updateToken(SocialUser request) {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageConstants.USER_NOT_FOUND),
                        GeneralMessageConstants.USR_NOT_FOUND));
        addCalendar(request, user);
        userRepository.save(user);
    }

    /**
     * Add calendar to user
     *
     * @param request social login request
     * @param user    details of user
     */
    private void addCalendar(SocialUser request, User user) {
        LinkedCalendar calendar = getCalendar(request.getSocialMediaMail(), request.getType());
        if (request.getRefreshToken() != null) {
            calendar.setAccessToken(request.getToken());
            calendar.setRefreshToken(request.getRefreshToken());
            calendar.setExpireDate(request.getExpireDate());
        }
        if (calendar.getId() == null) {
            calendar.setType(request.getType());
            calendar.setSocialMail(request.getSocialMediaMail());
            calendar.setUsers(Collections.singleton(user));
        } else {
            calendar.setUsers(new HashSet<>(calendar.getUsers()));
            calendar.getUsers().add(user);
        }
        user.setCalendars(new HashSet<>(user.getCalendars()));
        user.getCalendars().add(calendar);
    }

    /**
     * Checks if user already added to db by user mail
     *
     * @param mail given mail address
     * @throws CalendarAppException if user not exist
     */
    private void ifUserExistWithError(String mail) {
        if (userRepository.existsByEmail(mail)) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageConstants.USER_FOUND),
                    GeneralMessageConstants.USR_FOUND);
        }
    }

    /**
     * Returns user calendar detail by mail
     *
     * @param mail user mail address
     * @param type app provider type
     * @return calendar detail
     */
    private LinkedCalendar getCalendar(String mail, AppProviderType type) {
        return calendarRepository.findBySocialMailAndType(mail, type).orElse(new LinkedCalendar());
    }

    /**
     * Encode user password for security
     * Combine username, usernameid and security
     *
     * @param password password information
     * @param userMail mail of user
     * @return encoded password
     */
    private synchronized String encodePassword(String password, String userMail) {
        String concat = userMail.toLowerCase();
        MessageDigest md = DigestUtils.getSha256Digest();
        md.update(concat.getBytes(StandardCharsets.UTF_8));
        byte[] bytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

}
