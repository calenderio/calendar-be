package com.io.collige.models.requests.license;

import com.io.collige.entitites.User;
import com.io.collige.enums.LicenceTypes;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LicenceGenerateRequest {

    private User user;
    private LicenceTypes type;

}
