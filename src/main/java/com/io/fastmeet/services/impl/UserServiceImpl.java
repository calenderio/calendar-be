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
import com.io.fastmeet.entitites.User;
import com.io.fastmeet.mappers.UserMapper;
import com.io.fastmeet.models.requests.user.AuthRequest;
import com.io.fastmeet.models.requests.user.UserCreateRequest;
import com.io.fastmeet.models.responses.user.UserResponse;
import com.io.fastmeet.repositories.UserRepository;
import com.io.fastmeet.services.UserService;
import com.io.fastmeet.utils.GeneralMessageUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
     * Checks if user exist or not
     *
     * @param id user id
     */
    @Override
    public void ifUserExist(Long id) {
        if (!userRepository.existsById(id)) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageUtil.USER_NOT_FOUND),
                    GeneralMessageUtil.USR_NOT_FOUND);
        }
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

}
