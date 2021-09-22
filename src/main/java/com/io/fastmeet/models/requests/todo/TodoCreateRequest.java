package com.io.fastmeet.models.requests.todo;

import com.io.fastmeet.enums.Priority;

public class TodoCreateRequest {

    private Long userId;

    private String description;

    private Priority priority;
}
