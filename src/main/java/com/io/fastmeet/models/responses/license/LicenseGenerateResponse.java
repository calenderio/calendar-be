package com.io.fastmeet.models.responses.license;

import com.io.fastmeet.entitites.User;
import com.io.fastmeet.enums.LicenceTypes;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class LicenseGenerateResponse {

    @Schema(description = "User of license", example = "User")
    private User user;
    @Schema (description = "Type of license" , example = "Free Trial")
    private LicenceTypes licenceTypes;
    @Schema (description = "Startdate of activation" , example = "2021-01-13T17:09:42.411")
    private LocalDateTime activationDate;
    @Schema (description = "Enddate of activation" , example = "2021-01-13T17:09:42.411")
    private LocalDateTime endDate;
    @Schema (description = "Unique key of license for each User" , example = "123e4567-e89b-42d3-a456-556642440000")
    private String licenseKey;
    @Schema (description = "License for enterprise" , example = "123e4567-e89b-42d3-a456-556642440000")
    private long companyId;



}
