/*
 * @author : Oguz Kahraman
 * @since : 11.02.2021
 *
 * Copyright - restapi
 **/
package com.io.fastmeet.models.requests.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class AuthRequest {

    @Schema(description = "Name of user", example = "example", required = true)
    @NotEmpty
    @Length(max = 100)
    private String username;

    @Schema(description = "Password of user", example = "aB123@.123", required = true)
    @NotBlank
    private String password;

}
