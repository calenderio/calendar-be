package com.io.fastmeet.models.requests.license;

import com.io.fastmeet.entitites.User;
import com.io.fastmeet.enums.LicenceTypes;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LicenceGenerateRequest {

    private User userId;
    private LicenceTypes  type;
    private LocalDateTime activationDate;
    private LocalDateTime  endDate;
}
