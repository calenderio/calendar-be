/*
 * @author : Oguz Kahraman
 * @since : 26.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.io.collige.core.i18n.Translator;
import com.io.collige.models.internals.AttachmentModel;
import com.io.collige.models.requests.meet.MeetingRequest;
import net.fortuna.ical4j.model.property.Method;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class IcsServiceImplTest {

    @InjectMocks
    private IcsServiceImpl icsService;

    @Test
    void writeIcsFileToByteArray() throws IOException {
        MockedStatic<Translator> translatorMockedStatic = Mockito.mockStatic(Translator.class);
        translatorMockedStatic.when(() -> Translator.getMessage(any(), any(), any(), any())).thenReturn("Example");
        AttachmentModel model = new AttachmentModel();
        model.setName("example");
        model.setType("text");
        model.setData("text".getBytes());
        MeetingRequest request = new MeetingRequest();
        request.setMethod(Method.REQUEST);
        request.setTitle("");
        request.setLocation("");
        request.setStartDate(LocalDateTime.now());
        request.setEndDate(LocalDateTime.now());
        request.setUuid(UUID.randomUUID());
        request.setTimeZone("");
        request.setDescription("");
        request.setOrganizer("");
        request.setOrganizerName("ex");
        request.setOrganizerMail("");
        request.setSequence(0);
        request.setParticipants(Collections.singletonList("example@example.com"));
        request.setAttachmentModels(Collections.singletonList(model));
        byte[] response = icsService.writeIcsFileToByteArray(request);
        assertNotNull(response);
        translatorMockedStatic.close();
    }
}