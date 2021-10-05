/*
 * @author : Oguz Kahraman
 * @since : 5.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services;

import com.io.collige.models.internals.AttachmentModel;
import com.io.collige.models.internals.FileDetails;

import java.util.List;

public interface CloudinaryService {
    String uploadPhoto(String photoUrl, String userMail);

    List<FileDetails> uploadMeetingFiles(List<AttachmentModel> attachments, String invitationId);
}
