package com.io.calendar.enums;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

public enum ReminderEnum {
    ONE_DAYS("1 DAY", Duration.ofDays(1).toMinutes()),
    TWO_DAYS("2 DAYS", Duration.ofDays(2).toMinutes()),
    THREE_DAYS("3 DAYS", Duration.ofDays(3).toMinutes()),
    ONE_WEEK("1 WEEK", Duration.ofDays(7).toMinutes()),

    ;

    @Getter
    String description;

    @Getter
    long toMinutes;

    ReminderEnum(String description, long toMinutes) {
            this.description = description;
            this.toMinutes = toMinutes;


    }
}

