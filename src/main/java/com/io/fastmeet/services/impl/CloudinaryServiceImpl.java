/*
 * @author : Oguz Kahraman
 * @since : 5.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services.impl;

import com.cloudinary.Cloudinary;
import com.io.fastmeet.services.CloudinaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

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
        cloudinaryMap.put("folder", "fastmeet/profile");
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
}
