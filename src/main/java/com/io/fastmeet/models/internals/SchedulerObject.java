/*
 * @author : Oguz Kahraman
 * @since : 11.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.internals;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SchedulerObject {

    @Schema(description = "Id of scheduler", example = "1")
    @NotBlank
    private Long id;
    @Schema(description = "Id of scheduler", example = "1")
    @NotBlank
    private String name;
    @Schema(description = "Timezone of scheduler", example = "Europe/Istanbul")
    @NotBlank
    private String timeZone;
    @Schema(description = "Selected date details", implementation = SchedulerDetails.class)
    @Valid
    @NotNull
    private SchedulerDetails schedule;

}
