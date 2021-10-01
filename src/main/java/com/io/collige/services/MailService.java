/*
 * @author : Oguz Kahraman
 * @since : 22.02.2021
 *
 * Copyright - Deity Arena Java API
 **/
package com.io.collige.services;


import com.io.collige.models.internals.GenericMailRequest;
import org.springframework.scheduling.annotation.Async;

public interface MailService {

    void sendMailValidation(GenericMailRequest requestDto);

    void sendPasswordResetMail(GenericMailRequest requestDto);

    void sendInvitationMail(GenericMailRequest requestDto);

    @Async
    void sendScheduledInvitation(GenericMailRequest requestDto);
}

