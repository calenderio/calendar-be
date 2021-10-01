/*
 * @author : Oguz Kahraman
 * @since : 5.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.remotes.microsoft;

import lombok.Data;

import java.util.List;

@Data
public class MicrosoftCalendarResponse {

    private List<CalendarEventItem> value;

}