/*
 * @author : Oguz Kahraman
 * @since : 6.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers.impl;

import com.io.collige.constants.RoleConstants;
import com.io.collige.controllers.FileController;
import com.io.collige.models.responses.files.FileResponse;
import com.io.collige.services.FileService;
import com.io.collige.utils.AttachmentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class FileControllerImpl implements FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private AttachmentUtil attachmentUtil;

    @Override
    @Secured({RoleConstants.INDIVIDUAL, RoleConstants.COMMERCIAL})
    public ResponseEntity<Void> addNewFiles(List<MultipartFile> files) {
        fileService.uploadFiles(attachmentUtil.checkAttachments(files));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @Secured({RoleConstants.INDIVIDUAL, RoleConstants.COMMERCIAL})
    public ResponseEntity<Void> deleteFile(Long fileId) {
        fileService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<FileResponse>> getAllFiles() {
        return ResponseEntity.ok(fileService.getAllFiles());
    }

}
