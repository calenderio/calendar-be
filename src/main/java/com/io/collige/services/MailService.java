/*
 * @author : Oguz Kahraman
 * @since : 22.02.2021
 *
 * Copyright - Deity Arena Java API
 **/
package com.io.collige.services;


import com.io.collige.models.internals.mail.GenericMailRequest;

public interface MailService {

    void sendMailValidation(GenericMailRequest requestDto);

    void sendPasswordResetMail(GenericMailRequest requestDto);

    void sendInvitationMail(GenericMailRequest requestDto);

    void sendScheduledInvitation(GenericMailRequest requestDto);

}

