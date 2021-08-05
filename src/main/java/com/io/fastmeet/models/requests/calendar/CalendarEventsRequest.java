/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.requests.calendar;

import com.io.fastmeet.validators.EventDate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@EventDate
public class CalendarEventsRequest {

    @Schema(description = "Max Event Time", implementation = LocalDateTime.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime timeMax;
    @Schema(description = "Max Event Time", implementation = LocalDateTime.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime timeMin;
    @Schema(description = "Max Event Time", example = "Europe/Istanbul")
    private String timeZone;
}
