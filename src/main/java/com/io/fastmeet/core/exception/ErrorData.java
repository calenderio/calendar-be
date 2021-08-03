/*
 * @author : Oguz Kahraman
 * @since : 10.12.2020
 *
 * Copyright - analytics
 **/
package com.io.fastmeet.core.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ErrorData {

    @Schema(description = "Exception Date", implementation = LocalDateTime.class)
    private String timestamp;

    @Schema(description = "Status Code", example = "500", required = true)
    private int status;

    @Schema(description = "Error Explanation", example = "Already added db", required = true)
    private String error;

    @Schema(description = "Error ID", example = "ALREADY_ADDED", required = true)
    private String message;

    @Schema(description = "Error Request Path", example = "/example", required = true)
    private String path;

}