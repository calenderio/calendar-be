/*
 * @author : Oguz Kahraman
 * @since : 26.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.io.collige.core.i18n.Translator;
import com.io.collige.models.internals.file.AttachmentModel;
import com.io.collige.models.internals.mail.GenericMailRequest;
import com.io.collige.utils.LinkUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;

import javax.mail.internet.MimeMessage;
import java.util.Collections;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MailServiceImplTest {

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private LinkUtil linkUtil;

    @InjectMocks
    private MailServiceImpl mailService;

    private MockedStatic<Translator> translatorMockedStatic;

    @BeforeAll
    void init() {
        translatorMockedStatic = Mockito.mockStatic(Translator.class);
        translatorMockedStatic.when(() -> Translator.getMessage(any(), any(Locale.class))).thenReturn("Example");
    }

    @AfterAll
    void end() {
        translatorMockedStatic.close();
    }

    @Test
    void sendMailValidation() {
        GenericMailRequest request = mockTestData();
        mailService.sendMailValidation(request);
        verify(emailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void sendPasswordResetMail() {
        GenericMailRequest request = mockTestData();
        mailService.sendPasswordResetMail(request);
        verify(emailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void sendInvitationMail() {
        GenericMailRequest request = mockTestData();
        mailService.sendInvitationMail(request);
        verify(emailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void sendScheduledInvitation() {
        GenericMailRequest request = mockTestData();
        request.setMeetingDetails("example user".getBytes());
        mailService.sendScheduledInvitation(request);
        verify(emailSender, times(1)).send(any(MimeMessage.class));
    }

    private GenericMailRequest mockTestData() {
        ReflectionTestUtils.setField(mailService, "from", "example@example.com");
        when(emailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        when(templateEngine.process(anyString(), any(IContext.class))).thenReturn("example html");
        doNothing().when(emailSender).send(any(MimeMessage.class));
        AttachmentModel model = new AttachmentModel();
        model.setName("example");
        model.setType("text");
        model.setData("text".getBytes());
        GenericMailRequest request = new GenericMailRequest();
        request.setEmails(Collections.singleton("example@example.com"));
        request.setCc(Collections.singleton("example@example.com"));
        request.setBcc(Collections.singleton("example@example.com"));
        request.setLanguage(Locale.ENGLISH);
        request.setName("");
        request.setHeader("");
        request.setDescription("");
        request.setCode("");
        request.setInviter("");
        request.setLocation("");
        request.setMethod("");
        request.setAttachments(Collections.singletonList(model));
        return request;
    }

}