package com.io.collige.models.requests.todo;

import com.io.collige.enums.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TodoCreateRequest {

    @Schema(description = "Todo description", example = "Example ToDo")
    private String description;

    @Schema(description = "Todo priority level", example = "CRITICAL")
    private Priority priority;

}
