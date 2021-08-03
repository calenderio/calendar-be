/*
 * @author : oguzkahraman
 * @since : 22.05.2021
 *
 * Copyright - Deity Arena Java API
 **/
package com.io.fastmeet.core.i18n;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CalendarLocaleResolver extends AcceptHeaderLocaleResolver {
    List<Locale> locales = Arrays.asList(new Locale("tr"));

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        if (StringUtils.isBlank(request.getHeader("Accept-Language"))) {
            return Locale.getDefault();
        }
        List<Locale.LanguageRange> list = Locale.LanguageRange.parse(request.getHeader("Accept-Language"));
        Locale locale = Locale.lookup(list, locales);
        return locale;
    }
}