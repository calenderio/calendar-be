package com.io.collige.models.requests.todo;

import com.io.collige.enums.Priority;
import lombok.Data;

@Data
public class TodoCreateRequest {

    private Long userId;

    private String description;

    private Priority priority;

}
