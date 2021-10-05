/*
 * @author : Oguz Kahraman
 * @since : 5.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.io.collige.entitites.User;
import com.io.collige.models.internals.AttachmentModel;
import com.io.collige.models.internals.FileDetails;
import com.io.collige.services.CloudinaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    private static final String INV_LINK = "collige/files/%d/events/%s";

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.blank_url}")
    private String blankUrl;

    private Cloudinary cloudinary = null;

    @PostConstruct
    public void initialize() {
        Map<String, String> cloudinaryMap = new HashMap<>();
        cloudinaryMap.put("api_key", apiKey);
        cloudinaryMap.put("api_secret", apiSecret);
        cloudinaryMap.put("cloud_name", cloudName);
        cloudinary = new Cloudinary(cloudinaryMap);
    }

    /**
     * Uploads photo to cloudinary service by mail
     *
     * @param photoUrl url of photo for social logins
     * @param userMail mail address
     * @return null url
     */
    @Override
    public String uploadPhoto(String photoUrl, String userMail) {
        Map<String, Object> cloudinaryMap = new HashMap<>();
        cloudinaryMap.put("folder", "collige/profile");
        cloudinaryMap.put("public_id", userMail);
        cloudinaryMap.put("overwrite", true);
        cloudinaryMap.put("use_filename", true);
        try {
            if (photoUrl != null) {
                Map<String, Object> response = cloudinary.uploader().upload(photoUrl, cloudinaryMap);
                return response.get("url").toString();
            }
            Map<String, Object> response = cloudinary.uploader().upload(blankUrl, cloudinaryMap);
            return response.get("url").toString();
        } catch (Exception e) {
            log.error("User photo upload error {}", e.getMessage());
            return blankUrl;
        }
    }

    /**
     * Uploads attachments cloudinary service by user id and invitaiton id
     *
     * @param attachments attachment list
     * @return null url list
     */
    @Override
    public Set<FileDetails> uploadMeetingFiles(List<AttachmentModel> attachments, String invitationId, User user) {
        Set<FileDetails> fileDetails = new HashSet<>();
        try {
            cloudinary.api().deleteResourcesByPrefix(String.format(INV_LINK, user.getId(), invitationId), ObjectUtils.emptyMap());
        } catch (Exception e) {
            log.error("User file delete error {}", e.getMessage());
        }
        Map<String, Object> cloudinaryMap = new HashMap<>();
        cloudinaryMap.put("folder", String.format(INV_LINK, user.getId(), invitationId));
        cloudinaryMap.put("overwrite", true);
        cloudinaryMap.put("use_filename", true);
        for (AttachmentModel model : attachments) {
            try {
                cloudinaryMap.put("public_id", model.getName());
                Map<String, Object> response = cloudinary.uploader().upload(model.getData(), cloudinaryMap);
                FileDetails details = new FileDetails();
                details.setFileName(model.getName());
                details.setFileType(model.getType());
                details.setFileLink(response.get("url").toString());
                fileDetails.add(details);
            } catch (Exception e) {
                log.error("User file upload error {} {}", e.getMessage(), model.getName());
            }
        }
        return fileDetails;
    }

    @Override
    public void deleteInvitationFiles(String invitationId, User user) {
        try {
            cloudinary.api().deleteResourcesByPrefix(String.format(INV_LINK, user.getId(), invitationId), ObjectUtils.emptyMap());
            cloudinary.api().deleteFolder(String.format(INV_LINK, user.getId(), invitationId), ObjectUtils.emptyMap());
        } catch (Exception e) {
            log.error("User file delete error {}", e.getMessage());
        }
    }
}
