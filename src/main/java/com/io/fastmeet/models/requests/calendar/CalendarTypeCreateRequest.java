/*
 * @author : Oguz Kahraman
 * @since : 8.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.requests.calendar;

import com.io.fastmeet.models.internals.CalendarDateType;
import com.io.fastmeet.models.internals.CalendarDuration;
import com.io.fastmeet.models.internals.Question;
import com.io.fastmeet.models.internals.SchedulerDetails;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class CalendarTypeCreateRequest {

    @Schema(description = "Calendar Name", example = "Example Calendar", required = true)
    @NotBlank
    private String name;
    @Schema(description = "Calendar Description", example = "This is an example calendar", required = true)
    @NotBlank
    private String description;
    @Schema(description = "Calendar timezone", example = "Europe/Istanbul", required = true)
    @NotBlank
    private String timezone;
    @Schema(description = "Is this type calendar needs a file", example = "false")
    private Boolean isFileRequired;
    @Schema(description = "Calendar schedule start date", implementation = CalendarDateType.class, required = true)
    @NotNull
    @Valid
    private CalendarDateType start;
    @Schema(description = "Calendar schedule start date", implementation = CalendarDateType.class, required = true)
    @NotNull
    @Valid
    private CalendarDateType end;
    @Schema(description = "Meeting duration", implementation = CalendarDuration.class, required = true)
    @NotNull
    @Valid
    private CalendarDuration type;
    @ArraySchema(schema = @Schema(description = "Additional questions for meeting request", implementation = Question.class), uniqueItems = true)
    @Valid
    private Set<Question> questions;
    @Schema(description = "Selected date details", implementation = SchedulerDetails.class, required = true)
    @Valid
    @NotNull
    private SchedulerDetails schedule;

}
