/*
 * @author : Oguz Kahraman
 * @since : 25.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.responses.meeting;

import com.io.collige.models.internals.event.CalendarDuration;
import com.io.collige.models.internals.event.QuestionModel;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class AvailableDateResponse {

    @Schema(implementation = Map.class, example = """
             {
                                      "2021-09-30": [
                                        "11:30:00",
                                        "13:00:00"
                                      ]
                                    }
            """)
    private Map<LocalDate, Set<LocalTime>> availableDates;

    @Schema(description = "Event time zone information", example = "UTC")
    private String timeZone;

    @Schema(description = "Event description", example = "Example Event")
    private String description;

    @Schema(description = "Name of user", example = "Example Surname")
    private String name;

    @Schema(description = "Mail address of user", example = "example@example.com")
    private String email;

    @Schema(description = "Profile picture address", example = "example.png")
    private String picture;

    @Schema(description = "Incitation is scheduled", example = "false")
    private Boolean scheduled;

    @Schema(description = "Is this type calendar needs a file", example = "false")
    private Boolean fileRequired;

    @Schema(description = "Description of file", example = "false")
    private String fileDescription;

    @Schema(description = "Meeting duration", implementation = CalendarDuration.class)
    private CalendarDuration duration;

    @ArraySchema(schema = @Schema(description = "User Questions", implementation = CalendarDuration.class))
    private List<QuestionModel> questions;

}
