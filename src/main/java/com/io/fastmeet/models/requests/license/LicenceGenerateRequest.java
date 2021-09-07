package com.io.fastmeet.models.requests.license;

import com.io.fastmeet.entitites.User;
import com.io.fastmeet.enums.LicenceTypes;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LicenceGenerateRequest {

    private User user;
    private LicenceTypes type;

}
