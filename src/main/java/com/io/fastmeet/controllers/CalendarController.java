/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.controllers;

import com.io.fastmeet.core.exception.ErrorData;
import com.io.fastmeet.models.requests.calendar.CalendarTypeCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "User Calendar Operations", description = "This endpoint performs user calendar operations")
public interface CalendarController {

    @Operation(summary = "Create Calendar Type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create calendar and returns details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CalendarTypeCreateRequest.class))}),
            @ApiResponse(responseCode = "400", description = "Creating calendar error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @PostMapping(value = "/users/calendar")
    ResponseEntity<Void> createCalendarType(@RequestBody CalendarTypeCreateRequest request, @RequestHeader(name = "Authorization", required = false) String token);

}
