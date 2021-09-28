package com.io.collige.models.responses.calendar;

import com.io.collige.enums.AppProviderType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CalendarItemResponse {

    private String description;
    private String title;
    private boolean accepted;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private AppProviderType type;
    private String email;

}
