/*
 * @author : Oguz Kahraman
 * @since : 22.02.2021
 *
 * Copyright - Deity Arena Java API
 **/
package com.io.fastmeet.services;


import com.io.fastmeet.models.internals.GenericMailRequest;
import org.springframework.scheduling.annotation.Async;

public interface MailService {

    void sendMailValidation(GenericMailRequest requestDto);

    void sendPasswordResetMail(GenericMailRequest requestDto);

    @Async
    void sendInvitationMail(GenericMailRequest requestDto);
}

