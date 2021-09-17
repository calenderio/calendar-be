/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - event App Java API
 **/
package com.io.fastmeet.controllers;

import com.io.fastmeet.core.exception.ErrorData;
import com.io.fastmeet.models.requests.calendar.EventTypeCreateRequest;
import com.io.fastmeet.models.responses.calendar.EventTypeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "User Event Operations", description = "This endpoint performs user event operations")
public interface EventController {

    @Operation(summary = "Create event Type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create event and returns details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventTypeResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Creating event error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @PostMapping(value = "/users/event")
    ResponseEntity<EventTypeResponse> createEventType(@RequestBody EventTypeCreateRequest request);

    @Operation(summary = "Get event Types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns event types",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EventTypeResponse.class)))}),
            @ApiResponse(responseCode = "400", description = "Getting event types", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @GetMapping(value = "/users/event")
    ResponseEntity<List<EventTypeResponse>> getEventTypes();

}