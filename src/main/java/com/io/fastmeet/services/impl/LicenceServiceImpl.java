package com.io.fastmeet.services.impl;

import com.io.fastmeet.entitites.Licence;
import com.io.fastmeet.enums.LicenceTypes;
import com.io.fastmeet.repositories.LicenceRepository;
import com.io.fastmeet.services.LicenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LicenceServiceImpl implements LicenceService {

    @Autowired
    private LicenceRepository licenceRepository;

    @Override
    public Licence generateFreeTrial() {
        UUID licenseKey = UUID.randomUUID();
        Licence licence = new Licence();
        licence.setActivationDate(LocalDateTime.now());
        licence.setEndDate(LocalDateTime.now().plusMonths(1));
        licence.setLicenceKey(licenseKey.toString());
        licence.setLicenceType(LicenceTypes.FREE_TRIAL);
        return licenceRepository.save(licence);
    }

    @Override
    public Licence findByCompanyId(Long id) {
        return licenceRepository.findByCompanyId(id).orElse(null);
    }

}
