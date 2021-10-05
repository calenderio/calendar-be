package com.io.collige.models.responses.todo;

import com.io.collige.enums.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoDetails {

    @Schema(description = "Id of todo", example = "example@example.com")
    private Long id;
    @Schema(description = "To do description", example = "example@example.com")
    private String description;
    @Schema(description = "Priority of to do item", example = "LOW")
    private Priority priority;
    @Schema(description = "Create Date", implementation = LocalDateTime.class)
    private LocalDateTime createDate;
    @Schema(description = "Is done", example = "true")
    private boolean done;

}
