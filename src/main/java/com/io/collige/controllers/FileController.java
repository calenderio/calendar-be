/*
 * @author : Oguz Kahraman
 * @since : 6.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers;

import com.io.collige.core.exception.ErrorData;
import com.io.collige.models.responses.calendar.EventTypeResponse;
import com.io.collige.models.responses.files.FileResponse;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "File Operations", description = "This endpoint performs user file operations")
public interface FileController {

    @Operation(summary = "Upload file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "File added"),
            @ApiResponse(responseCode = "400", description = "Adding file error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))}),
            @ApiResponse(responseCode = "403", description = "Not allowed for given parameters", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})
    })
    @PostMapping(path = "/files", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<Void> addNewFiles(@RequestPart(name = "file") List<MultipartFile> files);

    @Operation(summary = "Delete file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "File deleted"),
            @ApiResponse(responseCode = "400", description = "File delete error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))}),
            @ApiResponse(responseCode = "403", description = "Not allowed for given parameters", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})
    })
    @DeleteMapping(path = "/files/{fileId}")
    ResponseEntity<Void> deleteFile(@PathVariable Long fileId);

    @Operation(summary = "Get User Files")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns user file list",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EventTypeResponse.class)))}),
            @ApiResponse(responseCode = "400", description = "Getting file error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))}),
            @ApiResponse(responseCode = "403", description = "Not allowed for given parameters", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})
    })
    @GetMapping(value = "/files")
    ResponseEntity<List<FileResponse>> getAllFiles();

    @Operation(summary = "Get related events")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns file related events",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EventTypeResponse.class)))}),
            @ApiResponse(responseCode = "400", description = "Getting error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))}),
            @ApiResponse(responseCode = "403", description = "Not allowed for given parameters", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorData.class))})
    })
    @GetMapping(value = "/files/events")
    ResponseEntity<List<EventTypeResponse>> getEvents(Long fileId);

}
