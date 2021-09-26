/*
 * @author : Oguz Kahraman
 * @since : 10.12.2020
 *
 * Copyright - analytics
 **/
package com.io.collige.core.exception;

import com.io.collige.core.i18n.Translator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.MarkerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CalendarAppExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String EXCEPTION = "EXCEPTION";
    private static final String UNEXPECTED_ERROR = "Unexpected IS error: {} and user token {}";
    private static final String UNEXPECTED_TRACE = "Unexpected IS trace: {} {}";
    private static final String UNEXPECTED = "UNEXPECTED";
    private static final String NOT_VALID = "NOT_VALID";
    private static final String UNKNOWN_ERR = "UNKNOWN_ERR";
    private static final String AUTHORIZATION = "Authorization";
    private static final String UNKNOWN_EXCEPTION = "unknown.exception";

    @ExceptionHandler
    protected ResponseEntity<Object> handleResponseStatus(ResponseStatusException ex, WebRequest request) {
        String authKey = request.getHeader(AUTHORIZATION);
        log.error(MarkerFactory.getMarker(EXCEPTION), UNEXPECTED_ERROR, ex.getMessage(), authKey);
        log.error(MarkerFactory.getMarker(EXCEPTION), UNEXPECTED_TRACE, ExceptionUtils.getStackTrace(ex), ((ServletWebRequest) request).getRequest().getRequestURI());
        return ResponseEntity.badRequest().body(new ErrorData(LocalDateTime.now().toString(), ex.getStatus().value(),
                ex.getMessage(), ex.getReason(), ((ServletWebRequest) request).getRequest().getRequestURI()));
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDenied(WebRequest request, AccessDeniedException ex) {
        String authKey = request.getHeader(AUTHORIZATION);
        log.error(MarkerFactory.getMarker(EXCEPTION), UNEXPECTED_ERROR, ex.getMessage(), authKey);
        log.error(MarkerFactory.getMarker(EXCEPTION), UNEXPECTED_TRACE, ExceptionUtils.getStackTrace(ex), ((ServletWebRequest) request).getRequest().getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorData(LocalDateTime.now().toString(), HttpStatus.FORBIDDEN.value(),
                Translator.getMessage("error.not.has.permission"),
                "AUTH_ERR", ((ServletWebRequest) request).getRequest().getRequestURI()));
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleASMException(CalendarAppException ex, WebRequest request) {
        String authKey = request.getHeader(AUTHORIZATION);
        log.error(MarkerFactory.getMarker(EXCEPTION), UNEXPECTED_ERROR, ex.getMessage(), authKey);
        log.error(MarkerFactory.getMarker(EXCEPTION), UNEXPECTED_TRACE, ExceptionUtils.getStackTrace(ex), ((ServletWebRequest) request).getRequest().getRequestURI());
        return ResponseEntity.status(ex.getStatus()).body(new ErrorData(LocalDateTime.now().toString(), ex.getStatus().value(),
                ex.getReason(), ex.getCause().getMessage(), ((ServletWebRequest) request).getRequest().getRequestURI()));
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleBadCredential(BadCredentialsException ex, WebRequest request) {
        String authKey = request.getHeader(AUTHORIZATION);
        log.error(MarkerFactory.getMarker(EXCEPTION), UNEXPECTED_ERROR, ex.getMessage(), authKey);
        log.error(MarkerFactory.getMarker(EXCEPTION), UNEXPECTED_TRACE, ExceptionUtils.getStackTrace(ex), ((ServletWebRequest) request).getRequest().getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorData(LocalDateTime.now().toString(), HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(), "BAD_CREDENTIAL", ((ServletWebRequest) request).getRequest().getRequestURI()));
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleBadCredential(HttpClientErrorException ex, WebRequest request) {
        log.error(MarkerFactory.getMarker(EXCEPTION), "Client Exception", ExceptionUtils.getStackTrace(ex));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorData(LocalDateTime.now().toString(), ex.getRawStatusCode(),
                ex.getResponseBodyAsString(), "EXTERNAL_ERROR", ((ServletWebRequest) request).getRequest().getRequestURI()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        String authKey = request.getHeader(AUTHORIZATION);
        log.error(MarkerFactory.getMarker(EXCEPTION), UNEXPECTED_ERROR, ex.getMessage(), authKey);
        log.error(MarkerFactory.getMarker(EXCEPTION), UNEXPECTED_TRACE, ExceptionUtils.getStackTrace(ex), ((ServletWebRequest) request).getRequest().getRequestURI());
        return validationExceptionMessageCreator((ServletWebRequest) request, ex.getBindingResult());
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers,
                                                         HttpStatus status, WebRequest request) {
        String authKey = request.getHeader(AUTHORIZATION);
        log.error(MarkerFactory.getMarker(EXCEPTION), UNEXPECTED_ERROR, ex.getMessage(), authKey);
        log.error(MarkerFactory.getMarker(EXCEPTION), UNEXPECTED_TRACE, ExceptionUtils.getStackTrace(ex), ((ServletWebRequest) request).getRequest().getRequestURI());
        return validationExceptionMessageCreator((ServletWebRequest) request, ex.getBindingResult());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String authKey = request.getHeader(AUTHORIZATION);
        log.error(MarkerFactory.getMarker(EXCEPTION), UNEXPECTED_ERROR, ex.getMessage(), authKey);
        log.error(MarkerFactory.getMarker(EXCEPTION), UNEXPECTED_TRACE, ExceptionUtils.getStackTrace(ex), ((ServletWebRequest) request).getRequest().getRequestURI());
        return ResponseEntity.status(status).body(new ErrorData(LocalDateTime.now().toString(), status.value(),
                ex.getMessage(), NOT_VALID, ((ServletWebRequest) request).getRequest().getRequestURI()));
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handle(Exception ex, WebRequest request) {
        String authKey = request.getHeader(AUTHORIZATION);
        if (ex.getCause() instanceof ConstraintViolationException) {
            log.error(MarkerFactory.getMarker(EXCEPTION), UNEXPECTED_TRACE, ExceptionUtils.getStackTrace(ex), ((ServletWebRequest) request).getRequest().getRequestURI());
            log.error(MarkerFactory.getMarker(EXCEPTION), UNEXPECTED_ERROR, Translator.getMessage("error.already.in"), authKey);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorData(LocalDateTime.now().toString(), HttpStatus.BAD_REQUEST.value(),
                    Translator.getMessage("error.already.in"), "ALREADY_ADDED", ((ServletWebRequest) request).getRequest().getRequestURI()));
        }
        log.error(MarkerFactory.getMarker(EXCEPTION), UNEXPECTED_ERROR, ex.getMessage(), authKey);
        log.error(MarkerFactory.getMarker(UNEXPECTED), UNEXPECTED_TRACE, ExceptionUtils.getStackTrace(ex), ((ServletWebRequest) request).getRequest().getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorData(LocalDateTime.now().toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Translator.getMessage(UNKNOWN_EXCEPTION),
                UNKNOWN_ERR, ((ServletWebRequest) request).getRequest().getRequestURI()));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String authKey = request.getHeader(AUTHORIZATION);
        log.error(MarkerFactory.getMarker(EXCEPTION), UNEXPECTED_ERROR, ex.getMessage(), authKey);
        log.error(MarkerFactory.getMarker(UNEXPECTED), UNEXPECTED_TRACE, ExceptionUtils.getStackTrace(ex), ((ServletWebRequest) request).getRequest().getRequestURI());
        if (!HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            return new ResponseEntity<>(body, headers, status);
        }
        return ResponseEntity.status(status.value()).body(new ErrorData(LocalDateTime.now().toString(), status.value(),
                Translator.getMessage(UNKNOWN_EXCEPTION),
                UNKNOWN_ERR, ((ServletWebRequest) request).getRequest().getRequestURI()));
    }

    private ResponseEntity<Object> validationExceptionMessageCreator(ServletWebRequest request, BindingResult bindingResult) {
        return ResponseEntity.badRequest().body(new ErrorData(LocalDateTime.now().toString(), HttpStatus.BAD_REQUEST.value(),
                Translator.getMessage("error.not.valid.reason",
                        Objects.requireNonNull(Objects.requireNonNull(bindingResult.getFieldError()).getField()),
                        getFieldName(bindingResult))
                        + StringUtils.capitalize(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()), NOT_VALID,
                request.getRequest().getRequestURI()));
    }

    private String getFieldName(BindingResult bindingResult) {
        FieldError fieldError = bindingResult.getFieldError();
        if (fieldError != null && fieldError.getRejectedValue() != null) {
            return Objects.requireNonNull(fieldError.getRejectedValue()).toString();
        }
        return "";
    }

}