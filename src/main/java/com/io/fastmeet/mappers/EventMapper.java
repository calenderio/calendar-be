/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.mappers;

import com.io.fastmeet.entitites.Event;
import com.io.fastmeet.models.requests.calendar.EventTypeCreateRequest;
import com.io.fastmeet.models.responses.calendar.EventTypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(source = "isFileRequired", target = "fileRequired")
    @Mapping(source = "type.duration", target = "duration")
    @Mapping(source = "type.durationType", target = "durationType")
    @Mapping(source = "timezone", target = "timeZone")
    Event mapRequestToEntity(EventTypeCreateRequest request);

    @Mapping(target = "isFileRequired", source = "fileRequired")
    @Mapping(target = "type.duration", source = "duration")
    @Mapping(target = "type.durationType", source = "durationType")
    @Mapping(target = "schedule", ignore = true)
    @Mapping(target = "timezone", source = "timeZone")
    EventTypeResponse mapEntityToModel(Event request);

}
