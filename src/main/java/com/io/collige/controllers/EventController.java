/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - event App Java API
 **/
package com.io.collige.controllers;

import com.io.collige.core.exception.ErrorData;
import com.io.collige.models.requests.events.EventCreateRequest;
import com.io.collige.models.responses.calendar.EventTypeResponse;
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
import org.springframework.web.bind.annotation.PutMapping;
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
    @PostMapping(value = "/events")
    ResponseEntity<EventTypeResponse> createEventType(@RequestBody EventCreateRequest request);

    @Operation(summary = "Update Event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update event and returns details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventTypeResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Updating event error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @PutMapping(value = "/events/{eventId}")
    ResponseEntity<EventTypeResponse> updateEvent(@RequestBody EventCreateRequest request, @PathVariable Long eventId);

    @Operation(summary = "Delete Event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted event"),
            @ApiResponse(responseCode = "400", description = "Deleting event error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @DeleteMapping(value = "/events/{eventId}")
    ResponseEntity<Void> deleteEvent(@PathVariable Long eventId);

    @Operation(summary = "Get event Types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns event types",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EventTypeResponse.class)))}),
            @ApiResponse(responseCode = "400", description = "Getting event types", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @GetMapping(value = "/events")
    ResponseEntity<List<EventTypeResponse>> getEventTypes();

    @Operation(summary = "Get event details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns event details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventTypeResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Getting event detail error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @GetMapping(value = "/events/{eventId}")
    ResponseEntity<EventTypeResponse> getEventDetail(@PathVariable Long eventId);

}
