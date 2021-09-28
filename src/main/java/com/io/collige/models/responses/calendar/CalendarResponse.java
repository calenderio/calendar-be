package com.io.collige.models.responses.calendar;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CalendarResponse {

    private List<CalendarItemResponse> items = new ArrayList<>();

}
