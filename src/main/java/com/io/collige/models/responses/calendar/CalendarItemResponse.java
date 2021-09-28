package com.io.collige.models.responses.calendar;

import com.io.collige.enums.AppProviderType;
import com.io.collige.enums.CalendarEventStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CalendarItemResponse {

    @Schema(description = "Description of event", example = "Example event item")
    private String description;
    @Schema(description = "Title of event", example = "Example event")
    private String title;
    @Schema(description = "If event accepted", example = "true")
    private boolean accepted;
    @Schema(description = "Event status", example = "TENTATIVE")
    private CalendarEventStatus status;
    @Schema(description = "Event start date", implementation = LocalDateTime.class)
    private LocalDateTime startDate;
    @Schema(description = "Event end date", implementation = LocalDateTime.class)
    private LocalDateTime endDate;
    @Schema(description = "App provider", example = "INTERNAL")
    private AppProviderType type;
    @Schema(description = "User calendar mail address", example = "exampleMail")
    private String email;

}
