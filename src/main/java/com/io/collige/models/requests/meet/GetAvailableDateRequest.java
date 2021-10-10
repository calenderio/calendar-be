/*
 * @author : Oguz Kahraman
 * @since : 22.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.requests.meet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class GetAvailableDateRequest {

    @Schema(description = "Max Event Time", implementation = LocalDate.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate localDate;
    @Schema(description = "Max Event Time", example = "12345678901234567890123451234567890123456789012345")
    @NotNull
    @Length(min = 50, max = 50)
    private String invitationId;
    @Schema(description = "TimeZone", example = "UTC")
    @NotNull
    private String timeZone;

}
