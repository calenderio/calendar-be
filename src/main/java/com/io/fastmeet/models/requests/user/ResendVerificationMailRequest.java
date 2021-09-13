/*
 * @author : Oguz Kahraman
 * @since : 13.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.requests.user;

import com.io.fastmeet.enums.ValidationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ResendVerificationMailRequest {

    @Schema(description = "Email of user", example = "test@test.com", required = true)
    @NotBlank
    @Email(message = "{email.not.valid}")
    private String email;

    @Schema(description = "Validation type", example = "EMAIL", required = true)
    @NotNull
    private ValidationType type;

}
