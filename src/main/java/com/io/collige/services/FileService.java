/*
 * @author : Oguz Kahraman
 * @since : 6.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services;

import com.io.collige.models.internals.AttachmentModel;

import java.util.List;

public interface FileService {
    void uploadFiles(List<AttachmentModel> attachments);

    void deleteFile(Long fileId);
}
