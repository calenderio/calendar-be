/*
 * @author : Oguz Kahraman
 * @since : 27.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.io.collige.entitites.Validation;
import com.io.collige.enums.ValidationType;
import com.io.collige.models.internals.ResendValidation;
import com.io.collige.models.requests.user.ValidationRequest;
import com.io.collige.repositories.UserRepository;
import com.io.collige.repositories.ValidationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationServiceImplTest {

    @Mock
    private ValidationRepository validationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ValidationServiceImpl validationService;

    @Test
    void verifyMail() {
        ValidationRequest request = new ValidationRequest();
        request.setMail("example");
        request.setCode("example");
        when(validationRepository.findByCodeAndMailAndType("example", "example", ValidationType.EMAIL))
                .thenReturn(Optional.of(new Validation()));
        validationService.verifyMail(request);
        verify(validationRepository, times(1)).delete(any());
        verify(userRepository, times(1)).verifyUserByMail("example");
    }

    @Test
    void verify_service() {
        ValidationRequest request = new ValidationRequest();
        request.setMail("example");
        request.setCode("example");
        when(validationRepository.findByCodeAndMailAndType("example", "example", ValidationType.PASSWORD))
                .thenReturn(Optional.of(new Validation()));
        validationService.verify(request);
        verify(validationRepository, times(1)).delete(any());
    }

    @Test
    void getValidationDetail() {
        Validation validation = new Validation();
        validation.setCode("123");
        ResendValidation request = new ResendValidation();
        request.setMail("example");
        request.setType(ValidationType.PASSWORD);
        when(validationRepository.findByMailAndType("example", ValidationType.PASSWORD))
                .thenReturn(Optional.of(validation));
        Validation response = validationService.getValidationDetail(request);
        assertEquals(validation.getCode(), response.getCode());
    }
}