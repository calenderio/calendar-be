package com.io.fastmeet.services.impl;

import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.core.i18n.Translator;
import com.io.fastmeet.entitites.Licence;
import com.io.fastmeet.entitites.User;
import com.io.fastmeet.enums.LicenceTypes;
import com.io.fastmeet.models.requests.license.LicenceGenerateRequest;
import com.io.fastmeet.repositories.LicenceRepository;
import com.io.fastmeet.repositories.UserRepository;
import com.io.fastmeet.services.LicenceService;
import com.io.fastmeet.utils.GeneralMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LicenceServiceImpl implements LicenceService {


    @Autowired
    LicenceRepository licenceRepository;

    @Autowired
    UserRepository userRepository;


    private UUID licenseKey = UUID.randomUUID();

    private Licence licence;

    @Override
    public Licence generator(LicenceGenerateRequest licenceGenerateRequest) {

        licence = new Licence();
        User user = userRepository.findById(licenceGenerateRequest.getUserId().getId())
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageUtil.USER_NOT_FOUND),
                        GeneralMessageUtil.USR_NOT_FOUND));
        if (user.getLicenceId().getId() == null) {
            if (licenceGenerateRequest.getType().equals(LicenceTypes.FREE_TRIAL)) {

                licence.setUserId(licenceGenerateRequest.getUserId());
                licence.setActivationDate(LocalDateTime.now());
                licence.setEndDate(LocalDateTime.now().plusMonths(1));
                licence.setLicenseKey(licenseKey.toString());
                licence.setLicenceType(licenceGenerateRequest.getType());
            }
        }
        return licence;
    }

    @Override
    public Licence findByCompanyId(Long id) {
        return licenceRepository.findByCompanyId(id).orElse(null);
    }

}
