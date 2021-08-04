/*
 * @author : Oguz Kahraman
 * @since : 12.11.2020
 * <p>
 * Copyright - deityarena
 **/
package com.io.fastmeet.controllers;


import com.io.fastmeet.core.exception.ErrorData;
import com.io.fastmeet.models.requests.user.AuthRequest;
import com.io.fastmeet.models.requests.user.UserCreateRequest;
import com.io.fastmeet.models.responses.user.UserResponse;
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

@Tag(name = "User operations", description = "This endpoint performs user operations")
public interface UserController {

    @Operation(summary = "Create User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Creating new user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Creating user error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @PostMapping(value = "/users")
    ResponseEntity<UserResponse> createUser(@RequestBody UserCreateRequest request,
                                            @RequestHeader(name = "Accept-Language", required = false) String language);

    @Operation(summary = "Login User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logging",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Login error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @PostMapping(value = "/login")
    ResponseEntity<UserResponse> loginUser(@RequestBody AuthRequest request);

}
