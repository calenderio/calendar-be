/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.requests.meet;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MeetInvitationRequest {

    @Schema(description = "Mail address of invited user", example = "test@test.com", required = true)
    @NotEmpty
    @Email
    private String userMail;

    @Schema(description = "Name of invited user", example = "Example User", required = true)
    @NotEmpty
    private String name;

    @Schema(description = "Id of event", example = "1", required = true)
    @NotNull
    private Long eventId;

    @ArraySchema(schema = @Schema(description = "Mail request cc users for meeting", example = "test@test.com"))
    private List<@Email String> ccUsers;

    @ArraySchema(schema = @Schema(description = "Mail request bcc users for meeting", example = "test@test.com"))
    private List<@Email String> bccUsers;

}
