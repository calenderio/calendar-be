/*
 * @author : Oguz Kahraman
 * @since : 10.12.2020
 *
 * Copyright - analytics
 **/
package com.io.fastmeet.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@SuppressWarnings("java:S110")
public class CalendarAppException extends ResponseStatusException {

    public CalendarAppException(HttpStatus status, String message, String error) {
        super(status, message, new Exception(error));
    }

}