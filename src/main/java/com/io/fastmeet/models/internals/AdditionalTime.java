/*
 * @author : Oguz Kahraman
 * @since : 10.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.internals;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class AdditionalTime {

    @ArraySchema(schema = @Schema(description = "Scheduler for Sunday", implementation = SchedulerTime.class))
    @Valid
    private List<SchedulerTime> time;
    @Schema(description = "Event time", implementation = LocalDate.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate date;
}

