package com.io.fastmeet.models.internals;

import com.io.fastmeet.enums.LicenceTypes;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LicenceGenerateRequest {

    private long userId;
    private LicenceTypes  type;
    private LocalDateTime activationDate;
    private LocalDateTime  endDate;
}
