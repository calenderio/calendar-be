/*
 * @author : Oguz Kahraman
 * @since : 8.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.requests.events;

import com.io.collige.enums.EventLocation;
import com.io.collige.models.internals.event.AlarmDuration;
import com.io.collige.models.internals.event.CalendarDuration;
import com.io.collige.models.internals.event.QuestionModel;
import com.io.collige.models.internals.scheduler.SchedulerDetails;
import com.io.collige.validators.Event;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Event
public class EventCreateRequest {

    @Schema(description = "Calendar Name", example = "Example Calendar", required = true)
    @NotBlank
    private String name;

    @Schema(description = "Calendar Description", example = "This is an example calendar", required = true)
    @NotBlank
    private String description;

    @Schema(description = "Calendar timezone", example = "Europe/Istanbul", required = true)
    @NotBlank
    private String timezone;

    @Schema(description = "Event Location", example = "MEET")
    private EventLocation location;

    @Schema(description = "Is this type calendar needs a file", example = "false")
    private Boolean isFileRequired;

    @Schema(description = "Description of file", example = "false")
    private String fileDescription;

    @Schema(description = "Event start date", implementation = LocalDate.class, required = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate startDate;

    @Schema(description = "Event end date", implementation = LocalDate.class, required = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate endDate;

    @Schema(description = "Meeting duration", implementation = CalendarDuration.class, required = true)
    @NotNull
    @Valid
    private CalendarDuration type;

    @ArraySchema(schema = @Schema(description = "Meeting alarms", implementation = AlarmDuration.class))
    private Set<@Valid AlarmDuration> alarms;

    @ArraySchema(schema = @Schema(description = "Additional questionModels for meeting request", implementation = QuestionModel.class), uniqueItems = true)
    @Valid
    private List<QuestionModel> questions;

    @Schema(description = "Schedule id for predefined schedulers", example = "1")
    private Long preDefinedSchedulerId;

    @Schema(description = "Selected date details", implementation = SchedulerDetails.class)
    @Valid
    private SchedulerDetails schedule;

    @ArraySchema(schema = @Schema(description = "User uploaded file links", example = "1L"), uniqueItems = true)
    private Set<Long> fileLinks;

}
