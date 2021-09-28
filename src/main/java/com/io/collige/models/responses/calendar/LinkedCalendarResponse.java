/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.responses.calendar;

import com.io.collige.enums.AppProviderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LinkedCalendarResponse {

    @Schema(description = "Calendar Provider", example = "MICROSOFT")
    private AppProviderType type;

    @Schema(description = "Calendar E-Mail", example = "test@test.com")
    private String socialMail;

}
