/*
 * @author : Oguz Kahraman
 * @since : 16.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    public static Date localDateTimeToDate(LocalDateTime localDateTime, String timeZone) {
        if (StringUtils.isBlank(timeZone)) {
            return Date.from(localDateTime.atZone(ZoneId.of("UTC")).toInstant());
        }
        return Date.from(localDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of(timeZone)).toInstant());
    }

}
