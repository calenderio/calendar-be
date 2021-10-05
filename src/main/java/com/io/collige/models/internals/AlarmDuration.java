/*
 * @author : Oguz Kahraman
 * @since : 8.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.internals;

import com.io.collige.validators.Duration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Duration
public class AlarmDuration extends CalendarDuration {

    @Schema(description = "Is  before", example = "true", required = true)
    @NotNull
    private boolean before;

}
