/*
 * @author : Oguz Kahraman
 * @since : 22.02.2021
 *
 * Copyright - Deity Arena Java API
 **/
package com.io.calendar.services;


import com.io.calendar.models.dtos.ValidationMailDTO;

public interface MailService {

    void sendMailValidation(ValidationMailDTO requestDto);

}
