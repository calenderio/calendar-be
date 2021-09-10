/*
 * @author : Oguz Kahraman
 * @since : 11.02.2021
 *
 * Copyright - restapi
 **/
package com.io.fastmeet.models.requests.user;

import com.io.fastmeet.validators.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserCreateRequest {

    @Schema(description = "Name of user", example = "example", required = true)
    @NotBlank
    private String name;

    @Schema(description = "Name of user", example = "example", required = true)
    @NotBlank
    private String timeZone;

    @Schema(description = "Password of user", example = "aB123@.123", required = true)
    @NotBlank
    @Length(min = 8)
    @Password
    private String password;

    @Schema(description = "Email of user", example = "test@test.com")
    @NotBlank
    @Email(message = "{email.not.valid}")
    private String email;


}
