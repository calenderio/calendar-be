/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.mappers;

import com.io.collige.entitites.Event;
import com.io.collige.models.requests.events.EventCreateRequest;
import com.io.collige.models.responses.calendar.EventTypeResponse;
import com.io.collige.models.responses.events.EventInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(source = "isFileRequired", target = "fileRequired")
    @Mapping(source = "type.duration", target = "duration")
    @Mapping(source = "type.durationType", target = "durationType")
    @Mapping(source = "timezone", target = "timeZone")
    Event mapRequestToEntity(EventCreateRequest request);

    @Mapping(target = "isFileRequired", source = "fileRequired")
    @Mapping(target = "type.duration", source = "duration")
    @Mapping(target = "type.durationType", source = "durationType")
    @Mapping(target = "schedule", ignore = true)
    @Mapping(target = "timezone", source = "timeZone")
    EventTypeResponse mapEntityToModel(Event request);

    List<EventTypeResponse> mapListToModel(List<Event> request);

    List<EventInfoResponse> mapListToInfoResponseModel(List<Event> request);

}
