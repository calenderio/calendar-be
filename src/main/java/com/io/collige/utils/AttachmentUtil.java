/**
 * @author : Oguz Kahraman
 * @since : 10/6/2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.utils;

import com.io.collige.core.exception.CalendarAppException;
import com.io.collige.models.internals.AttachmentModel;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class AttachmentUtil {

    @Value("${spring.allowed.file.extensions}")
    private List<String> allowedFileTypes;

    public List<AttachmentModel> checkAttachments(List<MultipartFile> files) {
        Tika tika = new Tika();
        List<AttachmentModel> modelList = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                String type;
                byte[] fileBytes;
                try {
                    fileBytes = file.getBytes();
                } catch (IOException e) {
                    throw new CalendarAppException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception", "EXCEPTION");
                }
                type = tika.detect(fileBytes);
                if (!allowedFileTypes.contains(type)) {
                    throw new CalendarAppException(HttpStatus.NOT_ACCEPTABLE, "Not allowed file type", "NOT_ALLOWED_FILE");
                }
                modelList.add(new AttachmentModel(fileBytes, file.getOriginalFilename(), type));
            }
        }
        return modelList;
    }

}
