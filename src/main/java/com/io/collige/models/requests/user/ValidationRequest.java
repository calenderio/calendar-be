/*
 * @author : Oguz Kahraman
 * @since : 10.04.2021
 *
 * Copyright - Deity Arena Java API
 **/
package com.io.collige.models.requests.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationRequest {

    @NotNull
    @Length(min = 50, max = 50)
    @Schema(description = "Validation code", example = "000000", required = true)
    private String code;

    @Email
    @Schema(description = "Mail Address", example = "test@test.com", required = true)
    private String mail;

}
