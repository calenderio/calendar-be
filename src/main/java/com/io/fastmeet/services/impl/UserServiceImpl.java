/*
 * @author : Oguz Kahraman
 * @since : 3.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services.impl;

import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.core.i18n.Translator;
import com.io.fastmeet.core.security.jwt.JWTService;
import com.io.fastmeet.entitites.LinkedCalendar;
import com.io.fastmeet.entitites.User;
import com.io.fastmeet.entitites.Validation;
import com.io.fastmeet.enums.ValidationType;
import com.io.fastmeet.mappers.UserMapper;
import com.io.fastmeet.models.internals.GenericMailRequest;
import com.io.fastmeet.models.internals.requests.SocialUserCreateRequest;
import com.io.fastmeet.models.requests.user.AuthRequest;
import com.io.fastmeet.models.requests.user.UserCreateRequest;
import com.io.fastmeet.models.responses.user.UserResponse;
import com.io.fastmeet.repositories.LinkedCalendarRepository;
import com.io.fastmeet.repositories.UserRepository;
import com.io.fastmeet.repositories.ValidationRepository;
import com.io.fastmeet.services.CloudinaryService;
import com.io.fastmeet.services.LicenceService;
import com.io.fastmeet.services.MailService;
import com.io.fastmeet.services.UserService;
import com.io.fastmeet.utils.GeneralMessageUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

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

    /**
     * This method creates new individual user
     *
     * @param request user details
     */
    @Override
    public UserResponse createIndividualUser(UserCreateRequest request) {
        ifUserExistWithError(request.getEmail());
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setIsCompany(false);
        user.setPassword(encodePassword(request.getPassword(), request.getEmail()));
        user.setLicence(licenceService.generateFreeTrial(user));
        userRepository.save(user);
        createValidationInfo(user, "tr_TR");
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
    public UserResponse socialSignUp(SocialUserCreateRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setIsCompany(false);
        user.setPassword(encodePassword(request.getPassword(), request.getEmail()));
        user.setVerified(true);
        user.setLicence(licenceService.generateFreeTrial(user));
        user.setPicture(cloudinaryService.uploadPhoto(request.getPictureUrl(), request.getEmail()));
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
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageUtil.USER_NOT_FOUND),
                        GeneralMessageUtil.USR_NOT_FOUND));
        if (encodePassword(authRequest.getPassword(), user.getEmail().toLowerCase()).equals(user.getPassword())) {
            UserResponse userResponse = userMapper.mapToModel(user);
            userResponse.setToken(jwtService.createToken(user));
            return userResponse;
        }
        throw new CalendarAppException(HttpStatus.FORBIDDEN, Translator.getMessage("error.user.password"), "PWD_ERR");
    }

    /**
     * This method creates new individual user
     *
     * @param id id of user
     */
    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageUtil.USER_NOT_FOUND),
                        GeneralMessageUtil.USR_NOT_FOUND));
    }

    /**
     * This method gets user information
     *
     * @param email mail of user
     */
    @Override
    public UserResponse findByMail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageUtil.USER_NOT_FOUND),
                        GeneralMessageUtil.USR_NOT_FOUND));
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
    public void addNewLinkToUser(User user, SocialUserCreateRequest request) {
        List<LinkedCalendar> userLink = user.getCalendars().stream().filter(item -> request.getSocialMediaMail().equals(item.getSocialMail()))
                .collect(Collectors.toList());
        if (!userLink.isEmpty()) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage("error.linked"),
                    GeneralMessageUtil.LINKED);
        }
        addCalendar(request, user);
        userRepository.save(user);
    }

    /**
     * Create new validation information for user
     *
     * @param user
     * @param language validation mail langugage
     */
    @Override
    @Async
    public void createValidationInfo(User user, String language) {
        Validation validation = validationRepository.findByMailAndType(user.getEmail(), ValidationType.EMAIL).orElse(new Validation());
        validation.setUserId(user.getId());
        validation.setCode(RandomStringUtils.randomAlphabetic(50));
        validation.setMail(user.getEmail());
        validation.setDate(LocalDateTime.now());
        validation.setType(ValidationType.EMAIL);
        validationRepository.save(validation);
        mailService.sendMailValidation(new GenericMailRequest(user.getEmail(), user.getName(), validation.getCode(), language));
    }

    /**
     * Get details by user token
     *
     * @param token user jwt
     * @return User detail object
     * @throws CalendarAppException if user not exist
     * @see com.io.fastmeet.models.responses.user.UserResponse for return object details
     */
    @Override
    public UserResponse getUserDetailsFromToken(String token) {
        UserResponse response = userMapper.mapToModel(jwtService.getUserFromToken(token));
        response.setToken(token);
        return response;
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
        MessageDigest md = DigestUtils.getMd5Digest();
        md.update(concat.getBytes(StandardCharsets.UTF_8));
        byte[] bytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     * Add calendar to user
     *
     * @param request social login request
     * @param user    details of user
     */
    private void addCalendar(SocialUserCreateRequest request, User user) {
        LinkedCalendar calendar = getCalendar(request.getSocialMediaMail());
        if (calendar.getId() == null) {
            calendar.setAccessToken(request.getToken());
            calendar.setRefreshToken(request.getRefreshToken());
            calendar.setType(request.getType());
            calendar.setSocialMail(request.getSocialMediaMail());
            calendar.setExpireDate(request.getExpireDate());
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
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageUtil.USER_FOUND),
                    GeneralMessageUtil.USR_FOUND);
        }
    }

    /**
     * Returns user calendar detail by mail
     *
     * @param mail user mail address
     * @return calendar detail
     */
    private LinkedCalendar getCalendar(String mail) {
        return calendarRepository.findBySocialMail(mail).orElse(new LinkedCalendar());
    }

}
