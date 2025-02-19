package com.io.collige.controllers;

import com.io.collige.core.exception.ErrorData;
import com.io.collige.models.requests.todo.TodoCreateRequest;
import com.io.collige.models.responses.todo.TodoDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Min;
import java.util.List;

@Tag(name = "User ToDo Operations", description = "This endpoint performs user todo operations")
public interface TodoController {

    @Operation(summary = "Get User Todos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Getting todo details",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TodoDetails.class)))}),
            @ApiResponse(responseCode = "400", description = "Getting todo error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @GetMapping(value = "/todos")
    ResponseEntity<List<TodoDetails>> getTodos(@Parameter(name = "pageNo", example = "1") @Min(1) @RequestParam(defaultValue = "1") Integer pageNo,
                                               @Parameter(name = "pageSize", example = "1") @Min(5) @RequestParam(defaultValue = "20") Integer pageSize,
                                               @Parameter(name = "sortBy", example = "id") @RequestParam(defaultValue = "id") String sortBy);

    @Operation(summary = "Create Todo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Create Todo",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TodoDetails.class))}),
            @ApiResponse(responseCode = "400", description = "Creating task error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @PostMapping(value = "/todos")
    ResponseEntity<TodoDetails> createTodo(@RequestBody TodoCreateRequest request);

    @Operation(summary = "Delete Todo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted Todo"),
            @ApiResponse(responseCode = "400", description = "Delete todo error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @DeleteMapping(value = "/todos/{todoId}")
    ResponseEntity<Void> deleteTodo(@PathVariable Long todoId);

    @Operation(summary = "Set Done Todo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Set done Todo"),
            @ApiResponse(responseCode = "400", description = "Setting done todo error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @PatchMapping(value = "/todos/{todoId}")
    ResponseEntity<Void> setDoneTodo(@PathVariable Long todoId);

}

