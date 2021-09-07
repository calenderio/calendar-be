package com.io.fastmeet.services;

import com.io.fastmeet.entitites.Licence;
import com.io.fastmeet.entitites.User;

public interface LicenceService {

    Licence generateFreeTrial(User user);

    Licence findByCompanyId(Long id);

}
