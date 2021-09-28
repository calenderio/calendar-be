package com.io.collige.services;

import com.io.collige.entitites.Licence;

public interface LicenceService {

    Licence generateFreeTrial();

    Licence findByCompanyId(Long id);

}
