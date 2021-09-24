/*
 * @author : Oguz Kahraman
 * @since : 23.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.requests.calendar;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class ScheduleMeetingRequest {

    @Schema(description = "Selected Date", implementation = LocalDate.class, required = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate date;

    @Schema(description = "Selected Time", implementation = LocalTime.class, required = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull
    private LocalTime time;

    @Schema(description = "User time zone", example = "UTC", required = true)
    @NotBlank
    private String timeZone;

    @ArraySchema(schema = @Schema(description = "Answer list", implementation = QuestionData.class))
    @Valid
    private List<QuestionData> answers;

}
