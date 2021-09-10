/*
 * @author : Oguz Kahraman
 * @since : 3.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services;

import com.io.fastmeet.entitites.User;
import com.io.fastmeet.models.internals.SocialUser;
import com.io.fastmeet.models.requests.user.AuthRequest;
import com.io.fastmeet.models.requests.user.UserCreateRequest;
import com.io.fastmeet.models.responses.user.UserResponse;
import org.springframework.scheduling.annotation.Async;

public interface UserService {
    UserResponse createIndividualUser(UserCreateRequest request);

    UserResponse socialSignUp(SocialUser request);

    UserResponse loginUser(AuthRequest authRequest);

    User findById(Long id);

    UserResponse findByMail(String email);

    boolean ifUserExist(String mail);

    void addNewLinkToUser(User user, SocialUser request);

    @Async
    void createValidationInfo(User user, String language);

    UserResponse getUserDetailsFromToken(String token);
}
