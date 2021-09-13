/*
 * @author : Oguz Kahraman
 * @since : 5.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services.impl;

import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.entitites.Validation;
import com.io.fastmeet.enums.ValidationType;
import com.io.fastmeet.models.internals.ResendValidation;
import com.io.fastmeet.models.requests.user.ValidationRequest;
import com.io.fastmeet.repositories.UserRepository;
import com.io.fastmeet.repositories.ValidationRepository;
import com.io.fastmeet.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Autowired
    private ValidationRepository validationRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * This methods verified user mail
     *
     * @param request verify request detail
     */
    @Override
    public void verifyMail(ValidationRequest request) {
        Validation validation = validationRepository.findByCodeAndMailAndType(request.getCode(), request.getMail(), ValidationType.EMAIL)
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, "Validation not valid", "VAL_ERR"));
        validationRepository.delete(validation);
        userRepository.verifyUserByMail(request.getMail());
    }

    /**
     * This methods verified user mail
     *
     * @param request verify request detail
     */
    @Override
    public void verify(ValidationRequest request) {
        Validation validation = validationRepository.findByCodeAndMailAndType(request.getCode(), request.getMail(), ValidationType.PASSWORD)
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, "Validation not valid", "VAL_ERR"));
        validationRepository.delete(validation);
    }

    /**
     * This methods verified user mail
     *
     * @param request verify request detail
     */
    @Override
    public Validation getValidationDetail(ResendValidation request) {
        return validationRepository.findByMailAndType(request.getMail(), request.getType())
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, "Validation not valid", "VAL_ERR"));
    }

}
