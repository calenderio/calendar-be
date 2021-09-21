/*
 * @author : Oguz Kahraman
 * @since : 22.02.2021
 *
 * Copyright - Deity Arena Java API
 **/
package com.io.fastmeet.services;


import com.io.fastmeet.models.internals.GenericMailRequest;

public interface MailService {

    void sendMailValidation(GenericMailRequest requestDto);

    void sendPasswordResetMail(GenericMailRequest requestDto);

    void sendInvitationMail(GenericMailRequest requestDto);

}

