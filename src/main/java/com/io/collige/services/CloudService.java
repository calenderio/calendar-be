/*
 * @author : Oguz Kahraman
 * @since : 5.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services;

import com.io.collige.entitites.User;
import com.io.collige.models.internals.AttachmentModel;
import com.io.collige.models.internals.FileDetails;

import java.util.List;
import java.util.Set;

public interface CloudService {
    String uploadPhoto(String photoUrl, String userMail);

    Set<FileDetails> uploadMeetingFiles(List<AttachmentModel> attachments, String invitationId, User user);

    void deleteInvitationFiles(String invitationId, User user);

    Set<FileDetails> uploadUserFiles(List<AttachmentModel> attachments, User user);

    void deleteFile(String name, User user);
}
