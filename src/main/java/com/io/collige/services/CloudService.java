/*
 * @author : Oguz Kahraman
 * @since : 5.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services;

import com.io.collige.entitites.User;
import com.io.collige.models.internals.file.DeleteInvitationFileRequest;
import com.io.collige.models.internals.file.FileDetails;
import com.io.collige.models.internals.file.UploadMeetingFileRequest;
import com.io.collige.models.internals.file.UploadUserFileRequest;

import java.util.Set;

public interface CloudService {
    String uploadPhoto(String photoUrl, String userMail);

    Set<FileDetails> uploadMeetingFiles(UploadMeetingFileRequest request);

    void deleteInvitationFiles(DeleteInvitationFileRequest request);

    Set<FileDetails> uploadUserFiles(UploadUserFileRequest request);

    void deleteFile(String name, User user);
}
