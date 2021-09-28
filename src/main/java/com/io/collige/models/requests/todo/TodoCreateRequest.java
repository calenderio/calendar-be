package com.io.fastmeet.models.requests.todo;

import com.io.fastmeet.enums.Priority;
import lombok.Data;

@Data
public class TodoCreateRequest {

    private Long userId;

    private String description;

    private Priority priority;

}
