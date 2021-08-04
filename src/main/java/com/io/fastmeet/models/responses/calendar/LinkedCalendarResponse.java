/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.responses.calendar;

import com.io.fastmeet.enums.CalendarProviderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LinkedCalendarResponse {

    @Schema(description = "Calendar Provider", example = "MICROSOFT")
    private CalendarProviderType type;

}
