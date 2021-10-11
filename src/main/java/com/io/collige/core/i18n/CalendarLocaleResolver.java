/*
 * @author : oguzkahraman
 * @since : 22.05.2021
 *
 * Copyright - Collige Java API
 **/
package com.io.collige.core.i18n;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CalendarLocaleResolver extends AcceptHeaderLocaleResolver {
    List<Locale> locales = Arrays.asList(new Locale("en"));

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        if (StringUtils.isBlank(request.getHeader("Accept-Language"))) {
            return Locale.getDefault();
        }
        String[] split = request.getHeader("Accept-Language").split("_");
        List<Locale.LanguageRange> list = Locale.LanguageRange.parse(split[0]);
        return Locale.lookup(list, locales) != null ? Locale.lookup(list, locales) : Locale.getDefault();
    }
}
