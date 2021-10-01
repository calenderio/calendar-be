/*
 * @author : Oguz Kahraman
 * @since : 7.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.requests.meet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ScheduleMeetRequest {

    @Schema(description = "Id of meet", example = "23123saopdasdu0123", required = true)
    @NotBlank
    private String id;

    @Schema(description = "Meet time", implementation = LocalDateTime.class, required = true)
    @NotNull
    private LocalDateTime meetTime;

    @Schema(description = "Meet Zone", example = "UTC+3", required = true)
    private String timeZone;

}
