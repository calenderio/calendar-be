/*
 * @author : Oguz Kahraman
 * @since : 6.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services;

import com.io.collige.models.internals.file.AttachmentModel;
import com.io.collige.models.responses.calendar.EventTypeResponse;
import com.io.collige.models.responses.files.FileResponse;

import java.util.List;

public interface FileService {
    void uploadFiles(List<AttachmentModel> attachments);

    void deleteFile(Long fileId);

    List<FileResponse> getAllFiles();

    List<FileResponse> getEventFiles(Long eventId);

    List<EventTypeResponse> getFileEvents(Long fileID);
}
