/*
 * @author : Oguz Kahraman
 * @since : 8.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.controllers;

import com.io.fastmeet.core.exception.ErrorData;
import com.io.fastmeet.models.requests.scheduler.SchedulerUpdateRequest;
import com.io.fastmeet.models.responses.scheduler.SchedulerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "User Scheduler Operations", description = "This endpoint performs user scheduler operations")
public interface SchedulerController {

    @Operation(summary = "Create Scheduler Type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Create scheduler and returns details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SchedulerResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Creating scheduler error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @PostMapping(value = "/schedulers")
    ResponseEntity<SchedulerResponse> createScheduler(@Parameter(name = "name", example = "Example") @RequestParam String name);

    @Operation(summary = "Gets User Schedulers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Getting scheduler details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SchedulerResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Getting scheduler error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @GetMapping(value = "/schedulers")
    ResponseEntity<SchedulerResponse> getSchedulers();

    @Operation(summary = "Change Scheduler Name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Changed scheduler name"),
            @ApiResponse(responseCode = "400", description = "Changing scheduler name error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @PatchMapping(value = "/schedulers/{schedulerId}")
    ResponseEntity<Void> changeName(@Parameter(name = "name", example = "Example", description = "New name of scheduler") @RequestParam String name,
                                    @Parameter(name = "schedulerId", example = "1", description = "Id of scheduler") @PathVariable Long schedulerId);


    @Operation(summary = "Update Scheduler Type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated scheduler and returns details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SchedulerResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Updating scheduler error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @PutMapping(value = "/schedulers/{schedulerId}")
    ResponseEntity<SchedulerResponse> updateScheduler(@RequestBody SchedulerUpdateRequest request,
                                                      @Parameter(name = "schedulerId", example = "1", description = "Id of scheduler") @PathVariable Long schedulerId);


}
