package com.io.collige.models.requests.calendar;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CalendarGetRequest {

    @Schema(description = "Filter start time", implementation = LocalDateTime.class, required = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull
    private LocalDateTime starDate;
    @Schema(description = "Filter end time", implementation = LocalDateTime.class, required = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull
    private LocalDateTime endDate;
    @Schema(description = "Selected time zone", example = "UTC", required = true)
    @NotNull
    private String timeZone;

}
