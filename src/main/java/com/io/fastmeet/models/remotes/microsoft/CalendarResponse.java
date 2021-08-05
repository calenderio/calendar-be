/*
 * @author : Oguz Kahraman
 * @since : 5.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.remotes.microsoft;

import lombok.Data;

import java.util.List;

@Data
public class CalendarResponse {

    private List<CalendarEventItem> value;

}
