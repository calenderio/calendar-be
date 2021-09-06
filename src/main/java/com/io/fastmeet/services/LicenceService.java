package com.io.fastmeet.services;

import com.io.fastmeet.entitites.Licence;
import com.io.fastmeet.models.requests.license.LicenceGenerateRequest;

public interface LicenceService {

    Licence generator(LicenceGenerateRequest licenceGenerateRequest);

    Licence findByCompanyId(Long id);

}
