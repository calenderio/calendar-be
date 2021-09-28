package com.io.collige.controllers;

import com.io.collige.core.exception.ErrorData;
import com.io.collige.models.requests.calendar.UserCalendarItemsRequest;
import com.io.collige.models.responses.calendar.CalendarResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "User Calendar Operations", description = "This endpoint performs user calendar operations")
public interface CalendarController {

    @Operation(summary = "Create event Type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns user calendars",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CalendarResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Getting calendars error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @GetMapping(value = "/calendars")
    ResponseEntity<CalendarResponse> getUserCalendars(UserCalendarItemsRequest request);

}
