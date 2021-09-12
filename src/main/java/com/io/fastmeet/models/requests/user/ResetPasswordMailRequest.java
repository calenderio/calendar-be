/*
 * @author : Oguz Kahraman
 * @since : 12.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.requests.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordMailRequest {


    @Schema(description = "Email of user", example = "test@test.com", required = true)
    @NotBlank
    @Email(message = "{email.not.valid}")
    private String email;

}
