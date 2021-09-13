package com.io.fastmeet.models.requests.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserUpdateRequest {

    @Schema(description = "Name of user", example = "example", required = true)
    @NotBlank
    private String name;

    @Schema(description = "User picture url", example = "https://example.com/default.jpg", required = true)
    private String picture;

    @Schema(description = "Email of user", example = "test@test.com", required = true)
    @NotBlank
    @Email(message = "{email.not.valid}")
    private String email;

    @Schema(description = "TimeZone", example = "Europe/Istanbul", required = true)
    @NotBlank
    private String timeZone;

}
