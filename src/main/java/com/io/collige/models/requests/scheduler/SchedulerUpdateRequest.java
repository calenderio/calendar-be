/*
 * @author : Oguz Kahraman
 * @since : 8.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.requests.scheduler;

import com.io.collige.models.internals.scheduler.SchedulerDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SchedulerUpdateRequest {

    @Schema(description = "Timezone of scheduler", example = "Europe/Istanbul", required = true)
    @NotBlank
    private String timeZone;

    @Schema(description = "Selected date details", implementation = SchedulerDetails.class, required = true)
    @Valid
    @NotNull
    private SchedulerDetails schedule;

}
