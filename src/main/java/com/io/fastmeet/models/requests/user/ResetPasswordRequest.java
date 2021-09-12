/*
 * @author : Oguz Kahraman
 * @since : 12.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.requests.user;

import com.io.fastmeet.validators.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ResetPasswordRequest {

    @Schema(description = "Email of user", example = "test@test.com", required = true)
    @NotBlank
    @Email(message = "{email.not.valid}")
    private String email;

    @Schema(description = "Password of user", example = "aB123@.123", required = true)
    @NotBlank
    @Length(min = 8)
    @Password
    private String password;

    @NotNull
    @Length(min = 50, max = 50)
    @Schema(description = "Validation code", example = "00000000000000", required = true)
    private String code;

}
