/*
 * @author : Oguz Kahraman
 * @since : 22.02.2021
 *
 * Copyright - Deity Arena Java API
 **/
package com.io.fastmeet.core.i18n;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class Translator {

    private static ResourceBundleMessageSource messageSource;

    @Autowired
    private Translator(ResourceBundleMessageSource messageSource) {
        Translator.messageSource = messageSource;
    }

    public static String getMessage(String message) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(message, null, locale);
    }

    public static String getMessage(String message, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(message, args, locale);
    }

    public static String getMessage(String message, Locale locale) {
        return messageSource.getMessage(message, null, locale);
    }

    public static Locale getLanguage(String language) {
        if (StringUtils.isBlank(language)) {
            return LocaleContextHolder.getLocale();
        }
        return Locale.forLanguageTag(language);
    }
}