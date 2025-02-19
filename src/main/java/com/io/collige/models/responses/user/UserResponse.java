/*
 * @author : Oguz Kahraman
 * @since : 28.07.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.responses.user;

import com.io.collige.models.responses.calendar.LinkedCalendarResponse;
import com.io.collige.models.responses.license.LicenseResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Data
public class UserResponse {

    @Schema(description = "Email of user", example = "test@test.com")
    private String email;
    @Schema(description = "Name of user", example = "Name Surname")
    private String name;
    @ArraySchema(schema = @Schema(description = "Roles Of User", example = "USER"))
    private Set<String> roles;
    @ArraySchema(schema = @Schema(description = "Calendars Of User", implementation = LinkedCalendarResponse.class))
    private Set<LinkedCalendarResponse> calendars;
    @Schema(description = "JWT token of user", example = "Bearer aksdkasjdjalkdjklajldkjaldjasd")
    private String token;
    @Schema(description = "If user verified", example = "true")
    private Boolean verified;
    @Schema(description = "User picture url", example = "https://example.com/default.jpg")
    private String picture;
    @Schema(description = "Licence details", implementation = LicenseResponse.class)
    private LicenseResponse licence;

}
