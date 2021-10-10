/*
 * @author : Oguz Kahraman
 * @since : 13.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.responses.calendar;

import com.io.collige.models.internals.event.AlarmDuration;
import com.io.collige.models.internals.event.CalendarDuration;
import com.io.collige.models.internals.event.QuestionModel;
import com.io.collige.models.internals.event.QuestionModelResponse;
import com.io.collige.models.internals.scheduler.SchedulerObject;
import com.io.collige.models.responses.files.FileResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
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

    @Schema(description = "Description of file", example = "false")
    private String fileDescription;

    @Schema(description = "Event start date", implementation = LocalDate.class)
    private LocalDate startDate;

    @Schema(description = "Event end date", implementation = LocalDate.class)
    private LocalDate endDate;

    @Schema(description = "Meeting duration", implementation = CalendarDuration.class)
    private CalendarDuration type;

    @ArraySchema(schema = @Schema(description = "Meeting alarms", implementation = AlarmDuration.class))
    private List<@Valid AlarmDuration> alarms;

    @Schema(description = "Selected date details", implementation = SchedulerObject.class)
    private SchedulerObject schedule;

    @ArraySchema(schema = @Schema(description = "Additional questionModels for meeting request", implementation = QuestionModel.class), uniqueItems = true)
    private Set<QuestionModelResponse> questions;

    @ArraySchema(schema = @Schema(description = "User File List", implementation = FileResponse.class))
    private List<FileResponse> fileList;

}
