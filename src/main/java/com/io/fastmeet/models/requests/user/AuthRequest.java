/*
 * @author : Oguz Kahraman
 * @since : 11.02.2021
 *
 * Copyright - restapi
 **/
package com.io.fastmeet.models.requests.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class AuthRequest {

    @Schema(description = "Name of user", example = "test@test.com", required = true)
    @NotEmpty
    @Email
    private String username;

    @Schema(description = "Password of user", example = "aB123@.123", required = true)
    @NotBlank
    private String password;

}
