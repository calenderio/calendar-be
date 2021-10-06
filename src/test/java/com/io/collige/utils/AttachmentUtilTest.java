/**
 * @author : Oguz Kahraman
 * @since : 10/6/2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.utils;

import com.io.collige.core.exception.CalendarAppException;
import com.io.collige.models.internals.AttachmentModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttachmentUtilTest {

    @InjectMocks
    private AttachmentUtil attachmentUtil;

    @Test
    void checkAttachments() {
        addTypes();
        MockMultipartFile file = new MockMultipartFile("data", "filename.txt",
                "application/octet-stream", "some xml".getBytes());
        List<AttachmentModel> attachmentModels = attachmentUtil.checkAttachments(Collections.singletonList(file));
        assertEquals(1, attachmentModels.size());
        assertEquals("text/plain", attachmentModels.get(0).getType());
        assertEquals("filename.txt", attachmentModels.get(0).getName());
    }

    @Test
    void checkAttachments_err() {
        addTypesError();
        MockMultipartFile file = new MockMultipartFile("data", "filename.txt",
                "application/octet-stream", "some xml".getBytes());
        CalendarAppException exception = assertThrows(CalendarAppException.class, () ->
                attachmentUtil.checkAttachments(Collections.singletonList(file)));
        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatus());
    }

    @Test
    void checkAttachments_err2() throws IOException {
        addTypesError();
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenThrow(new IOException());
        CalendarAppException exception = assertThrows(CalendarAppException.class, () ->
                attachmentUtil.checkAttachments(Collections.singletonList(file)));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
    }

    private void addTypes() {
        String values = "application/vnd.openxmlformats-officedocument.wordprocessingml.document," +
                "application/msword,application/pdf,application/x-tika-ooxml,application/x-tika-msoffice," +
                "application/octet-stream,text/plain";
        ReflectionTestUtils.setField(attachmentUtil, "allowedFileTypes", Arrays.asList(values.split(",")));
    }

    private void addTypesError() {
        String values = "application/vnd.openxmlformats-officedocument.wordprocessingml.document," +
                "application/msword,application/pdf,application/x-tika-ooxml,application/x-tika-msoffice," +
                "application/octet-stream";
        ReflectionTestUtils.setField(attachmentUtil, "allowedFileTypes", Arrays.asList(values.split(",")));
    }
}