/*
 * @author : Oguz Kahraman
 * @since : 3.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services;

import com.io.fastmeet.entitites.User;
import com.io.fastmeet.models.requests.user.AuthRequest;
import com.io.fastmeet.models.requests.user.UserCreateRequest;
import com.io.fastmeet.models.responses.user.UserResponse;

public interface UserService {
    UserResponse createIndividualUser(UserCreateRequest request);

    UserResponse loginUser(AuthRequest authRequest);

    User findById(Long id);

    void ifUserExist(Long id);
}
