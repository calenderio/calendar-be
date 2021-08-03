/*
 * @author : Oguz Kahraman
 * @since : 28.07.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.responses.user;

import lombok.Data;

@Data
public class UserResponse {

    private String email;
    private String name;
    private String token;


}
