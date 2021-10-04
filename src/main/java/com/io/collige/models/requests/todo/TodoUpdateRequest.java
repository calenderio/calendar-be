package com.io.collige.models.requests.todo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoUpdateRequest {

    private Long id;

    private LocalDateTime updatedTime;

    private String description;

}
