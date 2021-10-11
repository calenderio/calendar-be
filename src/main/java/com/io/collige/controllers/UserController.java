/*
 * @author : Oguz Kahraman
 * @since : 12.11.2020
 * <p>
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers;


import com.io.collige.core.exception.ErrorData;
import com.io.collige.models.requests.user.AuthRequest;
import com.io.collige.models.requests.user.ChangePasswordRequest;
import com.io.collige.models.requests.user.ResendVerificationMailRequest;
import com.io.collige.models.requests.user.ResetPasswordMailRequest;
import com.io.collige.models.requests.user.ResetPasswordRequest;
import com.io.collige.models.requests.user.UserCreateRequest;
import com.io.collige.models.requests.user.UserUpdateRequest;
import com.io.collige.models.responses.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    ResponseEntity<UserResponse> createUser(@RequestBody UserCreateRequest request);

    @Operation(summary = "Login User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logging",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Login error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @PostMapping(value = "/login")
    ResponseEntity<UserResponse> loginUser(@RequestBody AuthRequest request);

    @Operation(summary = "Returns User Detail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns user details from user token",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Creating user error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @GetMapping(value = "/users")
    ResponseEntity<UserResponse> getDetailsByToken();

    @Operation(summary = "Changes user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User password changed"),
            @ApiResponse(responseCode = "400", description = "Changing user password error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @PutMapping(value = "/password")
    ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request);

    @Operation(summary = "Send reset mail to user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User password reset mail sent"),
            @ApiResponse(responseCode = "400", description = "Sending mail error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @PostMapping(value = "/resetPassword")
    ResponseEntity<Void> resetPasswordRequest(@RequestBody ResetPasswordMailRequest request);

    @Operation(summary = "Reset user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User password changed"),
            @ApiResponse(responseCode = "400", description = "Resetting mail error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @PutMapping(value = "/resetPassword")
    ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request);

    @Operation(summary = "Resend validation mail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resent validation mail"),
            @ApiResponse(responseCode = "400", description = "Sending mail error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @PostMapping(value = "/resendVerification")
    ResponseEntity<Void> resendVerification(@RequestBody ResendVerificationMailRequest request);

    @Operation(summary = "Update User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User infos changed",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Update user error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})})
    @PutMapping(value = "/updateUser")
    ResponseEntity<UserResponse> updateUser(@RequestBody UserUpdateRequest request);
}
