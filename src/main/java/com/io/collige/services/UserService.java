/*
 * @author : Oguz Kahraman
 * @since : 3.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services;

import com.io.collige.core.models.SocialUser;
import com.io.collige.entitites.User;
import com.io.collige.models.requests.user.AuthRequest;
import com.io.collige.models.requests.user.ChangePasswordRequest;
import com.io.collige.models.requests.user.ResendVerificationMailRequest;
import com.io.collige.models.requests.user.ResetPasswordMailRequest;
import com.io.collige.models.requests.user.ResetPasswordRequest;
import com.io.collige.models.requests.user.UserCreateRequest;
import com.io.collige.models.requests.user.UserUpdateRequest;
import com.io.collige.models.responses.user.UserResponse;

public interface UserService {
    UserResponse createIndividualUser(UserCreateRequest request);

    UserResponse socialSignUp(SocialUser request);

    UserResponse loginUser(AuthRequest authRequest);

    User findById(Long id);

    UserResponse findByMail(String email);

    boolean ifUserExist(String mail);

    void addNewLinkToUser(User user, SocialUser request);

    UserResponse getUserDetailsFromToken();

    void resetPasswordRequest(ResetPasswordMailRequest request);

    void resetPassword(ResetPasswordRequest request);

    void updateToken(SocialUser request);

    void changePassword(ChangePasswordRequest request);

    void resendValidationMail(ResendVerificationMailRequest request);

    UserResponse updateUser(UserUpdateRequest userUpdateRequest);

}
