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
import com.io.fastmeet.mappers.UserMapper;
import com.io.fastmeet.models.internals.SocialUserCreateRequest;
import com.io.fastmeet.models.requests.user.AuthRequest;
import com.io.fastmeet.models.requests.user.UserCreateRequest;
import com.io.fastmeet.models.responses.user.UserResponse;
import com.io.fastmeet.repositories.LinkedCalendarRepository;
import com.io.fastmeet.repositories.UserRepository;
import com.io.fastmeet.services.UserService;
import com.io.fastmeet.utils.GeneralMessageUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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
    private LinkedCalendarRepository calendarRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserMapper userMapper;

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
        user = userRepository.save(user);
        UserResponse response = userMapper.mapToModel(user);
        response.setToken(jwtService.createToken(user.getEmail(), user.getId()));
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
        addCalendar(request, user);
        userRepository.save(user);
        UserResponse response = userMapper.mapToModel(user);
        response.setToken(jwtService.createToken(user.getEmail(), user.getId()));
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
            userResponse.setToken(jwtService.createToken(authRequest.getUsername(), user.getId()));
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
        userResponse.setToken(jwtService.createToken(email, user.getId()));
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
     * Checks if user exist or not
     *
     * @param user user object
     */
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
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

    private void ifUserExistWithError(String mail) {
        if (userRepository.existsByEmail(mail)) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageUtil.USER_FOUND),
                    GeneralMessageUtil.USR_FOUND);
        }
    }

    private LinkedCalendar getCalendar(String mail) {
        return calendarRepository.findBySocialMail(mail).orElse(new LinkedCalendar());
    }

}
