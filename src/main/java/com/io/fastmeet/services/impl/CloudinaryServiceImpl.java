/*
 * @author : Oguz Kahraman
 * @since : 5.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services.impl;

import com.cloudinary.Cloudinary;
import com.io.fastmeet.services.CloudinaryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
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
    private void initialize() {
        Map<String, String> cloudinaryMap = new HashMap<>();
        cloudinaryMap.put("api_key", apiKey);
        cloudinaryMap.put("api_secret", apiSecret);
        cloudinaryMap.put("cloud_name", cloudName);
        cloudinary = new Cloudinary(cloudinaryMap);
    }

    @Override
    public String uploadPhoto(String photoUrl, String userMail) {
        try {
            Map<String, Object> cloudinaryMap = new HashMap<>();
            cloudinaryMap.put("folder", "fastmeet/profile");
            cloudinaryMap.put("public_id", userMail);
            cloudinaryMap.put("overwrite", true);
            cloudinaryMap.put("use_filename", true);
            if (photoUrl == null) {
                Map response = cloudinary.uploader().upload(blankUrl, cloudinaryMap);
                return response.get("url").toString();
            } else {
                Map response = cloudinary.uploader().upload(photoUrl, cloudinaryMap);
                return response.get("url").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
