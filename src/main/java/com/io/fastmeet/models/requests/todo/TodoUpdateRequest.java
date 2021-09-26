package com.io.fastmeet.models.requests.todo;

import com.io.fastmeet.enums.Priority;
import lombok.Data;

@Data
public class TodoUpdateRequest {

    private Long id;

    private String description;

    private Priority priority;

    private boolean isDone;
}
