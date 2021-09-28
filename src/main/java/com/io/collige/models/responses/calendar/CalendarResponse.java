package com.io.collige.models.responses.calendar;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CalendarResponse {

    @ArraySchema(schema = @Schema(description = "User event items", implementation = CalendarItemResponse.class))
    private List<CalendarItemResponse> items = new ArrayList<>();

}
