/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.responses.meeting;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InvitationResponse {

    @Schema(description = "Id of invitation", example = "1")
    private Long id;
    @Schema(description = "Email of user", example = "example@example.com")
    private String userEmail;
    @Schema(description = "Name of user", example = "Example")
    private String name;
    @Schema(description = "Is scheduled", example = "true")
    private Boolean scheduled;
    @ArraySchema(schema = @Schema(description = "Email of cc users", example = "example@example.com"))
    private List<String> ccList = new ArrayList<>();
    @ArraySchema(schema = @Schema(description = "Email of bcc users", example = "example@example.com"))
    private List<String> bccList = new ArrayList<>();

}
