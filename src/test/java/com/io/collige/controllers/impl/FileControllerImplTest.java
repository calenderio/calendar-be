/*
 * @author : Oguz Kahraman
 * @since : 6.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers.impl;

import com.io.collige.models.responses.calendar.EventTypeResponse;
import com.io.collige.models.responses.files.FileResponse;
import com.io.collige.services.FileService;
import com.io.collige.utils.AttachmentUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileControllerImplTest {


    @Mock
    private FileService fileService;

    @Mock
    private AttachmentUtil attachmentUtil;

    @InjectMocks
    private FileControllerImpl fileController;

    @Test
    void addNewFiles() {
        ResponseEntity<Void> responseEntity = fileController.addNewFiles(new ArrayList<>());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(attachmentUtil, times(1)).checkAttachments(any());
        verify(fileService, times(1)).uploadFiles(any());
    }

    @Test
    void deleteFile() {
        ResponseEntity<Void> responseEntity = fileController.deleteFile(1L);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(fileService, times(1)).deleteFile(1L);
    }

    @Test
    void getAllFiles() {
        when(fileService.getAllFiles()).thenReturn(new ArrayList<>());
        ResponseEntity<List<FileResponse>> responseEntity = fileController.getAllFiles();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, Objects.requireNonNull(responseEntity.getBody()).size());
    }

    @Test
    void getEvents() {
        when(fileService.getFileEvents(1L)).thenReturn(new ArrayList<>());
        ResponseEntity<List<EventTypeResponse>> responseEntity = fileController.getEvents(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, Objects.requireNonNull(responseEntity.getBody()).size());
    }

}