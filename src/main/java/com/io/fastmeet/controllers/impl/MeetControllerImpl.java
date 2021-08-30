/*
 * @author : Oguz Kahraman
 * @since : 7.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.controllers.impl;

import com.io.fastmeet.models.requests.meet.ScheduleMeetRequest;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class MeetControllerImpl {

    @Value("${spring.allowed.file.extensions}")
    private List<String> allowedFileTypes;


    @PostMapping(path = "/meet/schedule", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity uploadFile(@RequestPart(required = false) ScheduleMeetRequest request, @RequestPart(name = "file") MultipartFile file) throws IOException {
        Tika tika = new Tika();
        if (!allowedFileTypes.contains(tika.detect(file.getBytes()))) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }

}
