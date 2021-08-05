/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.mappers;

import com.io.fastmeet.models.remotes.google.GoogleCalendarEventsRequest;
import com.io.fastmeet.models.remotes.microsoft.MicrosoftCalendarEventsRequest;
import com.io.fastmeet.models.requests.calendar.CalendarEventsRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CalendarMapper {

    GoogleCalendarEventsRequest mapToGoogle(CalendarEventsRequest request);

    MicrosoftCalendarEventsRequest mapToMicrosoft(CalendarEventsRequest request);

}
