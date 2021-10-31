/**
 * @author : Oguz Kahraman
 * @since : 31 Eki 2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.utils;

import com.io.collige.enums.DurationType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DurationUtil {

    public static int getDurationAsMinute(DurationType type, int duration) {
        int multiply = DurationType.HOUR.equals(type) ? 60 : 1;
        return duration * multiply;
    }

}
