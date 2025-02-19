/*
 * @author : Oguz Kahraman
 * @since : 8.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.internals.scheduler;

import com.io.collige.models.internals.event.AdditionalTime;
import com.io.collige.validators.SchedulerDetail;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@SchedulerDetail
public class SchedulerDetails {

    @ArraySchema(schema = @Schema(description = "Scheduler for Monday", implementation = SchedulerTime.class))
    @Valid
    @Size(max = 5)
    private Set<SchedulerTime> mon;

    @ArraySchema(schema = @Schema(description = "Scheduler for Tuesday", implementation = SchedulerTime.class))
    @Valid
    @Size(max = 5)
    private Set<SchedulerTime> tue;

    @ArraySchema(schema = @Schema(description = "Scheduler for Wednesday", implementation = SchedulerTime.class))
    @Valid
    @Size(max = 5)
    private Set<SchedulerTime> wed;

    @ArraySchema(schema = @Schema(description = "Scheduler for Thursday", implementation = SchedulerTime.class))
    @Valid
    @Size(max = 5)
    private Set<SchedulerTime> thu;

    @ArraySchema(schema = @Schema(description = "Scheduler for Friday", implementation = SchedulerTime.class))
    @Valid
    @Size(max = 5)
    private Set<SchedulerTime> fri;

    @ArraySchema(schema = @Schema(description = "Scheduler for Saturday", implementation = SchedulerTime.class))
    @Valid
    @Size(max = 5)
    private Set<SchedulerTime> sat;

    @ArraySchema(schema = @Schema(description = "Scheduler for Sunday", implementation = SchedulerTime.class))
    @Valid
    @Size(max = 5)
    private Set<SchedulerTime> sun;

    @ArraySchema(schema = @Schema(description = "Additional scheduler days", implementation = AdditionalTime.class))
    @Valid
    private Set<AdditionalTime> additional;

    @ArraySchema(schema = @Schema(description = "Additional scheduler days", implementation = LocalDate.class))
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Valid
    private Set<LocalDate> unavailable;

}