/*
 * @author : Oguz Kahraman
 * @since : 13.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.responses.calendar;

import com.io.fastmeet.models.internals.CalendarDuration;
import com.io.fastmeet.models.internals.Question;
import com.io.fastmeet.models.internals.SchedulerObject;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class EventTypeResponse {

    @Schema(description = "Id of calendar", example = "1")
    private Long id;

    @Schema(description = "Calendar Name", example = "Example Calendar")
    private String name;

    @Schema(description = "Calendar Description", example = "This is an example calendar")
    private String description;

    @Schema(description = "Calendar timezone", example = "Europe/Istanbul")
    private String timezone;

    @Schema(description = "Is this type calendar needs a file", example = "false")
    private Boolean isFileRequired;

    @Schema(description = "Event start date", implementation = LocalDate.class)
    private LocalDate startDate;

    @Schema(description = "Event end date", implementation = LocalDate.class)
    private LocalDate endDate;

    @Schema(description = "Meeting duration", implementation = CalendarDuration.class)
    private CalendarDuration type;

    @Schema(description = "Selected date details", implementation = SchedulerObject.class)
    private SchedulerObject schedule;

    @ArraySchema(schema = @Schema(description = "Additional questions for meeting request", implementation = Question.class), uniqueItems = true)
    private Set<Question> questions;

    @Schema(description = "Is mail required for meeting", example = "true")
    private Boolean mailRequired;

    @Schema(description = "Is name required for meeting", example = "true")
    private Boolean nameRequired;

}
