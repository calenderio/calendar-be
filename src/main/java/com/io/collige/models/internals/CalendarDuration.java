/*
 * @author : Oguz Kahraman
 * @since : 8.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.internals;

import com.io.collige.enums.DurationType;
import com.io.collige.validators.Duration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Duration
public class CalendarDuration {

    @Schema(description = "Meeting duration", example = "45", required = true)
    @NotNull
    @Min(1)
    private Integer duration;
    @Schema(description = "Duration type", example = "MIN", required = true)
    @NotNull
    private DurationType durationType;

}
