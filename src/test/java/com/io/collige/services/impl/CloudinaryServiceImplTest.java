/*
 * @author : Oguz Kahraman
 * @since : 26.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.io.collige.entitites.User;
import com.io.collige.models.internals.AttachmentModel;
import com.io.collige.models.internals.FileDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CloudinaryServiceImplTest {

    @Mock
    private Cloudinary cloudinary;

    @InjectMocks
    private CloudinaryServiceImpl cloudinaryService;

    @Test
    void uploadPhoto() throws IOException {
        defaultSettings();
        String url = cloudinaryService.uploadPhoto("", "");
        assertEquals("exampleUrl", url);
    }

    @Test
    void uploadPhoto_error() {
        ReflectionTestUtils.setField(cloudinaryService, "apiKey", "value");
        ReflectionTestUtils.setField(cloudinaryService, "apiSecret", "value");
        ReflectionTestUtils.setField(cloudinaryService, "cloudName", "value");
        ReflectionTestUtils.setField(cloudinaryService, "blankUrl", "value");
        cloudinaryService.initialize();
        ReflectionTestUtils.setField(cloudinaryService, "cloudinary", cloudinary);
        String url = cloudinaryService.uploadPhoto("", "");
        assertEquals("value", url);
    }

    @Test
    void uploadPhoto_null() throws IOException {
        defaultSettings();
        String url = cloudinaryService.uploadPhoto(null, "");
        assertEquals("exampleUrl", url);
    }

    @Test
    void uploadMeetingFiles() throws IOException {
        defaultSettings();
        Set<FileDetails> fileDetails = cloudinaryService.uploadMeetingFiles(Collections.singletonList(new AttachmentModel()), "1", new User());
        assertEquals(1, fileDetails.size());

    }

    @Test
    void uploadUserFiles() throws IOException {
        defaultSettings();
        Set<FileDetails> fileDetails = cloudinaryService.uploadUserFiles(Collections.singletonList(new AttachmentModel()), new User());
        assertEquals(1, fileDetails.size());
    }

    @Test
    void deleteFile() throws IOException {
        Uploader uploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(uploader);
        cloudinaryService.deleteFile("example", new User());
        verify(uploader, times(1)).destroy(any(), anyMap());
    }

    private void defaultSettings() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("url", "exampleUrl");
        ReflectionTestUtils.setField(cloudinaryService, "apiKey", "value");
        ReflectionTestUtils.setField(cloudinaryService, "apiSecret", "value");
        ReflectionTestUtils.setField(cloudinaryService, "cloudName", "value");
        ReflectionTestUtils.setField(cloudinaryService, "blankUrl", "value");
        cloudinaryService.initialize();
        ReflectionTestUtils.setField(cloudinaryService, "cloudinary", cloudinary);
        Uploader uploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(), anyMap())).thenReturn(map);
    }
}