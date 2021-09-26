package com.io.fastmeet.models.responses;

import com.io.fastmeet.enums.Priority;
import lombok.Data;
import net.fortuna.ical4j.model.DateTime;

import java.time.LocalDateTime;

@Data
public class TodoCreateResponse {

    private Long userId;

    private String description;

    private Priority priority;

    private LocalDateTime createdDate;

    private boolean isDone;
}
