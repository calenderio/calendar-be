package com.io.collige.models.responses.license;

import com.io.collige.enums.LicenceTypes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LicenseResponse {

    @Schema(description = "Type of license", example = "Free Trial")
    private LicenceTypes licenceType;
    @Schema(description = "Start date of activation", example = "2021-01-13T17:09:42.411")
    private LocalDateTime activationDate;
    @Schema(description = "End date of activation", example = "2021-01-13T17:09:42.411")
    private LocalDateTime endDate;
    @Schema(description = "Unique key of license for each User", example = "123e4567-e89b-42d3-a456-556642440000")
    private String licenceKey;
    @Schema(description = "License for enterprise", example = "123e4567-e89b-42d3-a456-556642440000")
    private Long companyId;

}
