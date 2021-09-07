package com.io.fastmeet.services;

import com.io.fastmeet.entitites.Licence;

public interface LicenceService {

    Licence generateFreeTrial();

    Licence findByCompanyId(Long id);

}
