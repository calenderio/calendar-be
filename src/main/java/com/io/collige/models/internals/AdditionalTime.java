/*
 * @author : Oguz Kahraman
 * @since : 10.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.internals;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
public class AdditionalTime {

    @ArraySchema(schema = @Schema(description = "Scheduler for Sunday", implementation = SchedulerTime.class))
    @Valid
    @Size(max = 5)
    private Set<SchedulerTime> time;

    @Schema(description = "Event time", implementation = LocalDate.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate date;
}

