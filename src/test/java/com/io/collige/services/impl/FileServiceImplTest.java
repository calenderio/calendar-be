/*
 * @author : Oguz Kahraman
 * @since : 6.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.io.collige.constants.CacheConstants;
import com.io.collige.core.exception.CalendarAppException;
import com.io.collige.core.security.jwt.JWTService;
import com.io.collige.core.services.CacheService;
import com.io.collige.entitites.FileLink;
import com.io.collige.entitites.Licence;
import com.io.collige.entitites.User;
import com.io.collige.enums.LicenceTypes;
import com.io.collige.mappers.FileMapper;
import com.io.collige.models.internals.AttachmentModel;
import com.io.collige.models.internals.FileDetails;
import com.io.collige.repositories.FileLinkRepository;
import com.io.collige.services.CloudinaryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private JWTService jwtService;

    @Mock
    private CacheService cacheService;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private FileMapper mapper;

    @Mock
    private FileLinkRepository fileLinkRepository;

    @InjectMocks
    private FileServiceImpl fileService;

    @Test
    void uploadFiles() {
        Licence licence = new Licence();
        licence.setLicenceType(LicenceTypes.INDIVIDUAL);
        User user = new User();
        user.setId(1L);
        user.setLicence(licence);
        when(cacheService.getIntegerCacheValue(CacheConstants.ATTACHMENT_LIMIT_IND)).thenReturn(10);
        when(fileLinkRepository.countByUserId(1L)).thenReturn(1L);
        when(cloudinaryService.uploadUserFiles(any(), any())).thenReturn(Collections.singleton(new FileDetails()));
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(mapper.mapModelToEntity(any(), any())).thenReturn(new FileLink());
        fileService.uploadFiles(new ArrayList<>());
        verify(fileLinkRepository, times(1)).saveAll(any());
        verify(cloudinaryService, times(1)).uploadUserFiles(any(), any());
    }

    @Test
    void uploadFiles_err() {
        Licence licence = new Licence();
        licence.setLicenceType(LicenceTypes.INDIVIDUAL);
        User user = new User();
        user.setId(1L);
        user.setLicence(licence);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(cacheService.getIntegerCacheValue(CacheConstants.ATTACHMENT_LIMIT_IND)).thenReturn(0);
        when(fileLinkRepository.countByUserId(1L)).thenReturn(1L);
        List<AttachmentModel> attachmentModels = new ArrayList<>();
        CalendarAppException exception = assertThrows(CalendarAppException.class, () -> fileService.uploadFiles(attachmentModels));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("ATTACHMENT_LIMIT", exception.getCause().getMessage());
    }

    @Test
    void uploadFiles_err2() {
        Licence licence = new Licence();
        licence.setLicenceType(LicenceTypes.COMMERCIAL);
        User user = new User();
        user.setId(1L);
        user.setLicence(licence);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(cacheService.getIntegerCacheValue(CacheConstants.ATTACHMENT_LIMIT_COMMERCIAL)).thenReturn(0);
        when(fileLinkRepository.countByUserId(1L)).thenReturn(1L);
        List<AttachmentModel> attachmentModels = new ArrayList<>();
        CalendarAppException exception = assertThrows(CalendarAppException.class, () -> fileService.uploadFiles(attachmentModels));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("ATTACHMENT_LIMIT", exception.getCause().getMessage());
    }

    @Test
    void deleteFile() {
        User user = new User();
        user.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(fileLinkRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(new FileLink()));
        fileService.deleteFile(1L);
        verify(fileLinkRepository, times(1)).delete(any());
    }

    @Test
    void deleteFile_err() {
        User user = new User();
        user.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(fileLinkRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());
        CalendarAppException exception = assertThrows(CalendarAppException.class, () -> fileService.deleteFile(1L));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("FILE_NOT_FOUND", exception.getCause().getMessage());
    }
}