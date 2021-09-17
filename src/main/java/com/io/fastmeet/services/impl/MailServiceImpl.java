/*
 * @author : Oguz Kahraman
 * @since : 22.02.2021
 *
 * Copyright - Deity Arena Java API
 **/
package com.io.fastmeet.services.impl;

import com.io.fastmeet.core.i18n.Translator;
import com.io.fastmeet.models.internals.AttachmentModel;
import com.io.fastmeet.models.internals.GenericMailRequest;
import com.io.fastmeet.models.internals.MailValidation;
import com.io.fastmeet.services.MailService;
import lombok.extern.slf4j.Slf4j;
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
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${system.from}")
    private String from;

    /**
     * Sends validation mail to user
     *
     * @param requestDto validation mail send request
     */
    @Override
    public void sendMailValidation(GenericMailRequest requestDto) {
        try {
            Context context = new Context(Translator.getLanguage());
            String header = Translator.getMessage("mail.validation.subject", Translator.getLanguage());
            genericMessage(new MailValidation("validation", header), requestDto, context);
        } catch (Exception e) {
            log.info("Mail sending error to user {} {}", requestDto.getEmail(), e.getMessage());
        }
    }

    /**
     * Sends password reset mail to user
     *
     * @param requestDto validation mail send request
     */
    @Override
    public void sendPasswordResetMail(GenericMailRequest requestDto) {
        try {
            Context context = new Context(Translator.getLanguage());
            String header = Translator.getMessage("mail.password.reset", Translator.getLanguage());
            genericMessage(new MailValidation("resetPassword", header), requestDto, context);
        } catch (Exception e) {
            log.info("Mail sending error to user {} {}", requestDto.getEmail(), e.getMessage());
        }
    }

    /**
     * Send mail for all types
     *
     * @param dto request object
     * @throws MessagingException
     */
    @Async
    public void genericMessage(MailValidation dto, GenericMailRequest requestDto, Context context) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);
        context.setVariable("code", requestDto.getCode());
        context.setVariable("name", requestDto.getName());
        helper.setTo(requestDto.getEmail());
        helper.setSubject(dto.getHeader());
        helper.setFrom(from);
        if (requestDto.getMeetingDetails() != null) {
            DataSource iCalData = new ByteArrayDataSource(requestDto.getMeetingDetails(), "text/calendar; charset=UTF-8");
            message.setDataHandler(new DataHandler(iCalData));
            message.setHeader("Content-Type", "text/calendar; charset=UTF-8; method=REQUEST");
        }
        addAttachments(requestDto, message);
        String html = templateEngine.process("emails/" + dto.getTemplateName(), context);
        helper.setText(html, true);
        emailSender.send(message);
    }

    private void addAttachments(GenericMailRequest requestDto, MimeMessage message) throws MessagingException {
        if (requestDto.getAttachments() != null) {
            Multipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            for (AttachmentModel model : requestDto.getAttachments()) {
                DataSource source = new ByteArrayDataSource(model.getData(), model.getType());
                message.setDataHandler(new DataHandler(source));
                message.setFileName(model.getName());
                multipart.addBodyPart(messageBodyPart);
            }
        }
    }

}
