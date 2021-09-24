/*
 * @author : Oguz Kahraman
 * @since : 8.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.requests.calendar;

import com.io.fastmeet.enums.EventLocation;
import com.io.fastmeet.models.internals.CalendarDuration;
import com.io.fastmeet.models.internals.QuestionModel;
import com.io.fastmeet.models.internals.SchedulerDetails;
import com.io.fastmeet.validators.Event;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@Event
public class EventTypeCreateRequest {

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

    @Schema(description = "Event start date", implementation = LocalDate.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate startDate;

    @Schema(description = "Event end date", implementation = LocalDate.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate endDate;

    @Schema(description = "Meeting duration", implementation = CalendarDuration.class, required = true)
    @NotNull
    @Valid
    private CalendarDuration type;

    @ArraySchema(schema = @Schema(description = "Additional questionModels for meeting request", implementation = QuestionModel.class), uniqueItems = true)
    @Valid
    private List<QuestionModel> questions;

    @Schema(description = "Schedule id for predefined schedulers", example = "1")
    private Long preDefinedSchedulerId;

    @Schema(description = "Selected date details", implementation = SchedulerDetails.class, required = true)
    @Valid
    private SchedulerDetails schedule;

}
