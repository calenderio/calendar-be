/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.mappers;

import com.io.fastmeet.entitites.Calendar;
import com.io.fastmeet.models.remotes.google.GoogleCalendarEventsRequest;
import com.io.fastmeet.models.remotes.microsoft.MicrosoftCalendarEventsRequest;
import com.io.fastmeet.models.requests.calendar.CalendarEventsRequest;
import com.io.fastmeet.models.requests.calendar.CalendarTypeCreateRequest;
import com.io.fastmeet.models.responses.calendar.CalendarTypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CalendarMapper {

    GoogleCalendarEventsRequest mapToGoogle(CalendarEventsRequest request);

    MicrosoftCalendarEventsRequest mapToMicrosoft(CalendarEventsRequest request);

    @Mapping(source = "isFileRequired", target = "fileRequired")
    @Mapping(source = "type.duration", target = "duration")
    @Mapping(source = "type.durationType", target = "durationType")
    @Mapping(source = "timezone", target = "timeZone")
    Calendar mapRequestToEntity(CalendarTypeCreateRequest request);

    @Mapping(target = "isFileRequired", source = "fileRequired")
    @Mapping(target = "type.duration", source = "duration")
    @Mapping(target = "type.durationType", source = "durationType")
    @Mapping(target = "schedule", ignore = true)
    @Mapping(target = "timezone", source = "timeZone")
    CalendarTypeResponse mapEntityToModel(Calendar request);

}
