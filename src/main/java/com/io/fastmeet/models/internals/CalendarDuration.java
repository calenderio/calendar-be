/*
 * @author : Oguz Kahraman
 * @since : 8.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.internals;

import com.io.fastmeet.enums.DurationType;
import com.io.fastmeet.validators.Duration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Duration
public class CalendarDuration {

    @Schema(description = "Meeting duration", example = "24", required = true)
    @NotNull
    private Integer duration;
    @Schema(description = "Duration type", example = "HOUR", required = true)
    @NotNull
    private DurationType durationType;

}
