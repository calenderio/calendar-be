package com.io.fastmeet.models.requests.user;

import com.io.fastmeet.validators.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserUpdateRequest {

    @Schema
    @NotBlank
    private long id;

    @Schema(description = "Name of user", example = "example", required = true)
    @NotBlank
    private String name;

    @Schema(description = "User picture url", example = "https://example.com/default.jpg")
    private String picture;

    @Schema(description = "Email of user", example = "test@test.com")
    @NotBlank
    @Email(message = "{email.not.valid}")
    private String email;
}
