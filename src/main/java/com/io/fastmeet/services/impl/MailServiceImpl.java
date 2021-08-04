///*
// * @author : Oguz Kahraman
// * @since : 22.02.2021
// *
// * Copyright - Deity Arena Java API
// **/
//package com.io.fastmeet.services.impl;
//
//import com.io.fastmeet.core.i18n.Translator;
//import com.io.fastmeet.models.dtos.ValidationMailDTO;
//import com.io.fastmeet.services.MailService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.context.Context;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//
//@Service
//@Slf4j
//public class MailServiceImpl implements MailService {
//
//    @Autowired
//    private JavaMailSender emailSender;
//
//    @Autowired
//    private TemplateEngine templateEngine;
//
//    /**
//     * Sends validation mail to user
//     *
//     * @param requestDto validation mail send request
//     */
//    @Override
//    @Async
//    public void sendMailValidation(ValidationMailDTO requestDto) {
//        try {
//            String header = Translator.getMessage("mail.validation.subject", Translator.getLanguage("tr_TR"));
//            genericMessage("tr_TR", "validation", header);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Send mail for all types
//     *
//     * @param language
//     * @param templateName
//     * @throws MessagingException
//     */
//    private void genericMessage(String language, String templateName, String header) throws MessagingException {
//        MimeMessage message = emailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message,
//                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);
//        Context context = new Context(Translator.getLanguage(language));
//        context.setVariable("code", 123);
//        context.setVariable("name", 123);
//        helper.setTo("fixme");
//        helper.setSubject(header);
//        helper.setFrom("info@fixme.com");
//        String html = templateEngine.process("emails/" + templateName, context);
//        helper.setText(html, true);
//        emailSender.send(message);
//    }
//}
