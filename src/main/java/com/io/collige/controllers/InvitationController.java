/**
 * @author : Oguz Kahraman
 * @since : 10/6/2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.controllers;

import com.io.collige.core.exception.ErrorData;
import com.io.collige.models.requests.meet.InvitationResendRequest;
import com.io.collige.models.requests.meet.MeetInvitationRequest;
import com.io.collige.models.responses.meeting.InvitationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;

@Tag(name = "Invitation Operations", description = "This endpoint performs user invitation and scheduling operations")
public interface InvitationController {

    @Operation(summary = "Send new meeting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sending new meeting request"),
            @ApiResponse(responseCode = "400", description = "Sending meeting error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))}),
            @ApiResponse(responseCode = "403", description = "Not allowed for given parameters", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})
    })
    @PostMapping(path = "/meets/invite")
    ResponseEntity<Void> sendInvitation(@RequestBody MeetInvitationRequest request) throws IOException;

    @Operation(summary = "ReSend meeting request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sending new meeting request"),
            @ApiResponse(responseCode = "400", description = "Sending meeting error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))}),
            @ApiResponse(responseCode = "403", description = "Not allowed for given parameters", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})
    })
    @PostMapping(path = "/meets/{invitationId}")
    ResponseEntity<Void> resendInvitation(@PathVariable Long invitationId, @RequestBody InvitationResendRequest request) throws IOException;

    @Operation(summary = "Get meetings for event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns meeting request",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = InvitationResponse.class)))}),
            @ApiResponse(responseCode = "400", description = "Getting error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})
    })
    @GetMapping(path = "/meets/{invitationId}")
    ResponseEntity<List<InvitationResponse>> getInvitations(@PathVariable Long invitationId);

    @Operation(summary = "Delete invitation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete invite request"),
            @ApiResponse(responseCode = "400", description = "Deleting error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})
    })
    @DeleteMapping(path = "/meets/{invitationId}")
    ResponseEntity<Void> deleteInvitation(@PathVariable Long invitationId);

}
