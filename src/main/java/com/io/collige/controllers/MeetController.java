/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers;

import com.io.collige.core.exception.ErrorData;
import com.io.collige.models.requests.calendar.ScheduleMeetingRequest;
import com.io.collige.models.requests.meet.MeetInvitationRequest;
import com.io.collige.models.requests.meet.MeetingDateRequest;
import com.io.collige.models.responses.meeting.InvitationResponse;
import com.io.collige.models.responses.meeting.ScheduledMeetingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "User Meeting Operations", description = "This endpoint performs user meeting scheduling operations")
public interface MeetController {

    @Operation(summary = "Send new meeting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sending new meeting request"),
            @ApiResponse(responseCode = "400", description = "Sending meeting error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))}),
            @ApiResponse(responseCode = "403", description = "Not allowed for given parameters", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})
    })
    @PostMapping(path = "/meets/invite", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<Void> sendMeetingInvite(@RequestPart MeetInvitationRequest request,
                                           @RequestPart(name = "file", required = false) List<MultipartFile> files);

    @Operation(summary = "ReSend meeting request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sending new meeting request"),
            @ApiResponse(responseCode = "400", description = "Sending meeting error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))}),
            @ApiResponse(responseCode = "403", description = "Not allowed for given parameters", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})
    })
    @PostMapping(path = "/meets/{meetingId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<Void> resendMeeting(@PathVariable Long meetingId,
                                       @RequestPart(name = "file", required = false) List<MultipartFile> files);

    @Operation(summary = "Get meetings for event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns meeting request",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = InvitationResponse.class)))}),
            @ApiResponse(responseCode = "400", description = "Getting error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})
    })
    @GetMapping(path = "/meets/{eventId}")
    ResponseEntity<List<InvitationResponse>> getMeetings(@PathVariable Long eventId);

    @Operation(summary = "Delete invitation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete invite request"),
            @ApiResponse(responseCode = "400", description = "Deleting error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})
    })
    @DeleteMapping(path = "/meets/{meetingId}")
    ResponseEntity<Void> deleteMapping(@PathVariable Long meetingId);

    @Operation(summary = "Get Available Dates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns meeting request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ScheduledMeetingResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Getting error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})
    })
    @GetMapping(path = "/meets/availability")
    ResponseEntity<ScheduledMeetingResponse> getAvailableDates(MeetingDateRequest request);

    @Operation(summary = "Send new meeting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Scheduled meeting", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ScheduleMeetingRequest.class))}),
            @ApiResponse(responseCode = "400", description = "Schedule meeting error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))}),
            @ApiResponse(responseCode = "403", description = "Not allowed for given parameters", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})
    })
    @PostMapping(path = "/meets/schedule/{invitationId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<Void> scheduleMeeting(@RequestPart ScheduleMeetingRequest request,
                                         @PathVariable String invitationId,
                                         @RequestPart(name = "file", required = false) MultipartFile files);

    @Operation(summary = "Update existing meeting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated meeting", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ScheduleMeetingRequest.class))}),
            @ApiResponse(responseCode = "400", description = "Updating meeting error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))}),
            @ApiResponse(responseCode = "403", description = "Not allowed for given parameters", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})
    })
    @PutMapping(path = "/meets/schedule/{invitationId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<Void> updateMeeting(@RequestPart ScheduleMeetingRequest request,
                                       @PathVariable String invitationId,
                                       @RequestPart(name = "file", required = false) MultipartFile files);

    @Operation(summary = "Delete meeting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted meeting"),
            @ApiResponse(responseCode = "400", description = "Deleting meeting error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))}),
            @ApiResponse(responseCode = "403", description = "Not allowed for given parameters", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})
    })
    @DeleteMapping(path = "/meets/schedule/{invitationId}")
    ResponseEntity<Void> deleteMeeting(@PathVariable String invitationId);
}
