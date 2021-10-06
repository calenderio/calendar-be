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
import com.io.collige.entitites.User;
import com.io.collige.enums.LicenceTypes;
import com.io.collige.mappers.FileMapper;
import com.io.collige.models.internals.AttachmentModel;
import com.io.collige.models.internals.FileDetails;
import com.io.collige.repositories.FileLinkRepository;
import com.io.collige.services.CloudinaryService;
import com.io.collige.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FileServiceImpl implements FileService {

    private static final String NOT_ENOUGH_LIMIT_FOR_ATTACHMENT = "Not enough limit for attachment";
    private static final String ATTACHMENT_LIMIT = "ATTACHMENT_LIMIT";

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private FileMapper mapper;

    @Autowired
    private FileLinkRepository fileLinkRepository;

    @Override
    public void uploadFiles(List<AttachmentModel> attachments) {
        User user = jwtService.getLoggedUser();
        long size = fileLinkRepository.countByUserId(user.getId());
        limitChecker(size + attachments.size(), user);
        Set<FileDetails> fileDetails = cloudinaryService.uploadUserFiles(attachments, user);
        Set<FileLink> fileLinks = new HashSet<>();
        for (FileDetails file : fileDetails) {
            fileLinks.add(mapper.mapModelToEntity(file, user.getId()));
        }
        fileLinkRepository.saveAll(fileLinks);
    }

    @Override
    public void deleteFile(Long fileId) {
        User user = jwtService.getLoggedUser();
        FileLink fileLink = fileLinkRepository.findByIdAndUserId(fileId, user.getId()).orElseThrow(() ->
                new CalendarAppException(HttpStatus.BAD_REQUEST, "File couldn't find", "FILE_NOT_FOUND"));
        fileLinkRepository.delete(fileLink);
    }

    private void limitChecker(long totalSize, User user) {
        if (LicenceTypes.INDIVIDUAL.equals(user.getLicence().getLicenceType()) &&
                totalSize > cacheService.getIntegerCacheValue(CacheConstants.ATTACHMENT_LIMIT_IND)) {
            throw new CalendarAppException(HttpStatus.FORBIDDEN, NOT_ENOUGH_LIMIT_FOR_ATTACHMENT, ATTACHMENT_LIMIT);
        }
        if (LicenceTypes.COMMERCIAL.equals(user.getLicence().getLicenceType()) &&
                totalSize > cacheService.getIntegerCacheValue(CacheConstants.ATTACHMENT_LIMIT_COMMERCIAL)) {
            throw new CalendarAppException(HttpStatus.FORBIDDEN, NOT_ENOUGH_LIMIT_FOR_ATTACHMENT, ATTACHMENT_LIMIT);
        }
    }

}
