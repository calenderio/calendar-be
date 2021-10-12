package com.io.collige.controllers;

import com.io.collige.core.exception.ErrorData;
import com.io.collige.entitites.Todo;
import com.io.collige.models.requests.todo.TodoCreateRequest;
import com.io.collige.models.requests.todo.TodoUpdateRequest;
import com.io.collige.models.responses.scheduler.SchedulerResponse;
import com.io.collige.models.responses.todo.TodoCreateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TodoController {




    @Operation(summary = "Get User Todos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Getting todo details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SchedulerResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Getting todo error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @GetMapping(value = "/todos")
    ResponseEntity<List<Todo>> getTodos(@Parameter(name = "page no" , example = "1")@RequestParam(defaultValue = "0") Integer pageNo,
                                        @Parameter (name = "page size" ,example = "1") @RequestParam(defaultValue = "20") Integer pageSize,
                                        @Parameter(name = "String sort by" , example = "id")@RequestParam(defaultValue = "id")String sortBy);

    @Operation(summary = "Create Todo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Create Todo",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SchedulerResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Creating task error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @PostMapping(value = "/todos")
    ResponseEntity<TodoCreateResponse> createTodo(@Parameter(name = "request", example = "Example") @RequestParam TodoCreateRequest request);

    @Operation(summary = "Delete Todo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete Todo",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SchedulerResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Delete todo error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @DeleteMapping(value = "/todos/{todoId}")
    ResponseEntity<Void> deleteTodo (@Parameter(name = "Todo Id ", example = "1") @PathVariable Long todoId);

    @Operation(summary = "Set Done Todo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Set done Todo",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SchedulerResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Setting done todo error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @DeleteMapping(value = "/todos/{todoId}")
    ResponseEntity<Void> setDoneTodo (@Parameter(name = "Todo Id ", example = "1") @RequestParam Long todoId);
}

