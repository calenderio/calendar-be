/*
 * @author : Oguz Kahraman
 * @since : 27.07.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.remotes.microsoft;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CalendarEventItem {

    private String id;
    private String subject;
    private Boolean isReminderOn = true;
    private Boolean isCancelled;
    private Integer reminderMinutesBeforeStart = 10;
    private DateType start;
    private DateType end;
    private CalendarBody body;
    private ResponseStatus responseStatus;
    private List<EmailAddressItem> attendees = new ArrayList<>();
}
