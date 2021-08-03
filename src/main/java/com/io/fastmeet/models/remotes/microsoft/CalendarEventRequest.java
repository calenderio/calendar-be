/*
 * @author : Oguz Kahraman
 * @since : 27.07.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.remotes.microsoft;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CalendarEventRequest {

    private String subject;
    private Boolean isReminderOn = true;
    private Integer reminderMinutesBeforeStart = 10;
    private DateType start;
    private DateType end;
    private List<EmailAddressItem> attendees = new ArrayList<>();
}
