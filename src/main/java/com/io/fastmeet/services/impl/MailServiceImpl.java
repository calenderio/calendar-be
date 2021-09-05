/*
 * @author : Oguz Kahraman
 * @since : 22.02.2021
 *
 * Copyright - Deity Arena Java API
 **/
package com.io.fastmeet.services.impl;

import com.io.fastmeet.core.i18n.Translator;
import com.io.fastmeet.models.internals.GenericMailRequest;
import com.io.fastmeet.models.internals.ValidationMailDTO;
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

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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
    @Async
    public void sendMailValidation(GenericMailRequest requestDto) {
        try {
            String header = Translator.getMessage("mail.validation.subject", Translator.getLanguage("tr_TR"));
            genericMessage(new ValidationMailDTO("validation", header), requestDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send mail for all types
     *
     * @param dto request object
     * @throws MessagingException
     */
    private void genericMessage(ValidationMailDTO dto, GenericMailRequest requestDto) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);
        Context context = new Context(Translator.getLanguage(requestDto.getLanguage()));
        context.setVariable("code", requestDto.getCode());
        context.setVariable("name", requestDto.getName());
        helper.setTo(requestDto.getEmail());
        helper.setSubject(dto.getHeader());
        helper.setFrom(from);
        String html = templateEngine.process("emails/" + dto.getTemplateName(), context);
        helper.setText(html, true);
        emailSender.send(message);
    }
}
