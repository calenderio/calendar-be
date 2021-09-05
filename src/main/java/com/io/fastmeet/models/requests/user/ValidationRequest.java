/*
 * @author : Oguz Kahraman
 * @since : 10.04.2021
 *
 * Copyright - Deity Arena Java API
 **/
package com.io.fastmeet.models.requests.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class ValidationRequest {

    @NotNull
    @Length(min = 6, max = 6)
    @Schema(description = "Validation code", example = "000000", required = true)
    private String code;

    @Email
    @Schema(description = "Mail Address", example = "test@test.com", required = true)
    private String mail;

}
