/*
 * @author : Oguz Kahraman
 * @since : 26.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.io.collige.entitites.Licence;
import com.io.collige.repositories.LicenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LicenceServiceImplTest {

    @Mock
    private LicenceRepository licenceRepository;

    @InjectMocks
    private LicenceServiceImpl licenceService;

    @Test
    void generateFreeTrial() {
        Licence licence = new Licence();
        licence.setLicenceKey("123456");
        when(licenceRepository.save(any())).thenReturn(licence);
        Licence response = licenceService.generateFreeTrial();
        assertEquals(response.getLicenceKey(), licence.getLicenceKey());
    }

    @Test
    void findByCompanyId() {
        Licence licence = new Licence();
        licence.setLicenceKey("123456");
        when(licenceRepository.findByCompanyId(1L)).thenReturn(Optional.of(licence));
        Licence response = licenceService.findByCompanyId(1L);
        assertEquals(response.getLicenceKey(), licence.getLicenceKey());
    }
}