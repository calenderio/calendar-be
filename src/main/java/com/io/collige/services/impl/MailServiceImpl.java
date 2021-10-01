/*
 * @author : Oguz Kahraman
 * @since : 22.02.2021
 *
 * Copyright - Deity Arena Java API
 **/
package com.io.collige.services.impl;

import com.io.collige.core.i18n.Translator;
import com.io.collige.models.internals.AttachmentModel;
import com.io.collige.models.internals.GenericMailRequest;
import com.io.collige.models.internals.MailValidation;
import com.io.collige.services.MailService;
import com.io.collige.utils.LinkUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    private static final String MAIL_SENDING_ERROR_TO_USER = "Mail sending error to user {} {}";

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LinkUtil linkUtil;

    @Value("${system.from}")
    private String from;

    /**
     * Sends validation mail to user
     *
     * @param requestDto validation mail send request
     */
    @Override
    @Async
    public void sendMailValidation(GenericMailRequest requestDto) {
        try {
            Context context = new Context(Translator.getLanguage());
            String header = Translator.getMessage("mail.validation.subject", requestDto.getLanguage());
            genericMessage(new MailValidation("validation", header), requestDto, context);
        } catch (Exception e) {
            log.info(MAIL_SENDING_ERROR_TO_USER, requestDto.getEmails(), e.getMessage());
        }
    }

    /**
     * Sends password reset mail to user
     *
     * @param requestDto validation mail send request
     */
    @Override
    @Async
    public void sendPasswordResetMail(GenericMailRequest requestDto) {
        try {
            Context context = new Context(Translator.getLanguage());
            String header = Translator.getMessage("mail.password.reset", requestDto.getLanguage());
            genericMessage(new MailValidation("resetPassword", header), requestDto, context);
        } catch (Exception e) {
            log.info(MAIL_SENDING_ERROR_TO_USER, requestDto.getEmails(), e.getMessage());
        }
    }

    /**
     * Sends invitation mail to user
     *
     * @param requestDto invitation mail send request
     */
    @Override
    @Async
    public void sendInvitationMail(GenericMailRequest requestDto) {
        try {
            Context context = new Context(Translator.getLanguage());
            genericMessage(new MailValidation("invitation", requestDto.getHeader()), requestDto, context);
        } catch (Exception e) {
            log.info(MAIL_SENDING_ERROR_TO_USER, requestDto.getEmails(), e.getMessage());
        }
    }

    /**
     * Sends scheduled mail to user
     *
     * @param requestDto invitation mail send request
     */
    @Override
    @Async
    public void sendScheduledInvitation(GenericMailRequest requestDto) {
        try {
            Context context = new Context(Translator.getLanguage());
            genericMessage(new MailValidation("scheduled", requestDto.getHeader()), requestDto, context);
        } catch (Exception e) {
            log.info(MAIL_SENDING_ERROR_TO_USER, requestDto.getEmails(), e.getMessage());
        }
    }

    /**
     * Send mail for all types
     *
     * @param dto request object
     * @throws MessagingException mail sending error
     */
    private void genericMessage(MailValidation dto, GenericMailRequest requestDto, Context context) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        helper.setTo(requestDto.getEmails().toArray(new String[0]));
        context.setVariable("code", requestDto.getCode());
        context.setVariable("name", WordUtils.capitalize(requestDto.getName()));
        context.setVariable("inviter", WordUtils.capitalize(requestDto.getInviter()));
        context.setVariable("description", WordUtils.capitalize(requestDto.getDescription()));
        context.setVariable("mail", requestDto.getEmails().iterator().next());
        context.setVariable("linkUtil", linkUtil);
        helper.setSubject(dto.getHeader());
        helper.setFrom(from, "Collige");
        String html = templateEngine.process("emails/" + dto.getTemplateName(), context);
        Multipart multipart = new MimeMultipart();

        BodyPart htmlBodyPart = new MimeBodyPart();
        htmlBodyPart.setContent(html, "text/html");
        multipart.addBodyPart(htmlBodyPart);

        addCCAndBCC(requestDto, helper);
        if (requestDto.getMeetingDetails() != null) {
            DataSource iCalData = new ByteArrayDataSource(requestDto.getMeetingDetails(), "text/calendar; charset=UTF-8");
            message.setDataHandler(new DataHandler(iCalData));
            message.setHeader("Content-Type", "text/calendar; charset=UTF-8; method=" + requestDto.getMethod());
        } else {
            addAttachments(requestDto, helper);
            helper.setText(html, true);
        }
        emailSender.send(message);
    }

    private void addAttachments(GenericMailRequest requestDto, MimeMessageHelper helper) throws MessagingException {
        if (requestDto.getAttachments() != null && !requestDto.getAttachments().isEmpty()) {
            for (AttachmentModel model : requestDto.getAttachments()) {
                DataSource source = new ByteArrayDataSource(model.getData(), model.getType());
                helper.addAttachment(model.getName(), source);
            }
        }
    }

    private void addCCAndBCC(GenericMailRequest requestDto, MimeMessageHelper helper) throws MessagingException {
        if (requestDto.getBcc() != null && !requestDto.getBcc().isEmpty()) {
            helper.setBcc(requestDto.getBcc().toArray(new String[0]));
        }
        if (requestDto.getCc() != null && !requestDto.getCc().isEmpty()) {
            helper.setCc(requestDto.getCc().toArray(new String[0]));
        }
    }

}
