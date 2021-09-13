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
import com.io.fastmeet.models.requests.user.ChangePasswordRequest;
import com.io.fastmeet.models.requests.user.ResetPasswordMailRequest;
import com.io.fastmeet.models.requests.user.ResetPasswordRequest;
import com.io.fastmeet.models.requests.user.UserCreateRequest;
import com.io.fastmeet.models.requests.user.UserUpdateRequest;
import com.io.fastmeet.models.responses.user.UserResponse;

public interface UserService {
    UserResponse createIndividualUser(UserCreateRequest request);

    UserResponse socialSignUp(SocialUser request);

    UserResponse loginUser(AuthRequest authRequest);

    User findById(Long id);

    UserResponse findByMail(String email);

    boolean ifUserExist(String mail);

    void addNewLinkToUser(User user, SocialUser request);

    UserResponse getUserDetailsFromToken(String token);

    void resetPasswordRequest(ResetPasswordMailRequest request, String language);

    void resetPassword(ResetPasswordRequest request);

    void updateToken(SocialUser request);

    void changePassword(ChangePasswordRequest request, String token);

    void updateUser(UserUpdateRequest userUpdateRequest , String token);

}
