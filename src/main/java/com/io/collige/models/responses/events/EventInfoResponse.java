/**
 * @author : Oguz Kahraman
 * @since : 10/9/2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.models.responses.events;

import com.io.collige.models.responses.meeting.InvitationResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class EventInfoResponse {

    @Schema(description = "Event id", example = "1")
    private Long id;

    @Schema(description = "Event name", example = "Example Event")
    private String name;

    @Schema(description = "Event description", example = "Example Event")
    private String description;

    @ArraySchema(schema = @Schema(description = "Event invitations", implementation = InvitationResponse.class))
    private List<InvitationResponse> invitations;

}
