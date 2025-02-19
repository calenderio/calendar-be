/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.requests.meet;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
public class SendInvitationRequest {

    @Schema(description = "Mail address of invited user", example = "test@test.com", required = true)
    @NotBlank
    @Email
    private String userMail;

    @Schema(description = "Name of invited user", example = "Example User", required = true)
    @NotBlank
    private String name;

    @Schema(description = "Title of invitation", example = "Example User", required = true)
    @NotBlank
    private String title;

    @Schema(description = "Description of invitation", example = "Example User", required = true)
    @NotBlank
    private String description;

    @Schema(description = "Id of event", example = "1", required = true)
    @NotNull
    private Long eventId;

    @ArraySchema(schema = @Schema(description = "Mail request cc users for meeting", example = "test@test.com"))
    private List<@Email String> ccUsers;

    @ArraySchema(schema = @Schema(description = "Mail request bcc users for meeting", example = "test@test.com"))
    private List<@Email String> bccUsers;

    @ArraySchema(schema = @Schema(description = "Mail request bcc users for meeting", example = "1"))
    private Set<Long> fileIdList;

}
