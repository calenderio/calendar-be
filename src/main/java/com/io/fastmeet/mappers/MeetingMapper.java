package com.io.fastmeet.mappers;


import com.io.fastmeet.models.internals.GenericMailRequest;
import com.io.fastmeet.models.requests.meet.MeetingRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MeetingMapper {


    @Mapping(target = "organizer", source = "inviter")
    @Mapping(target = "participants", source = "email")
    GenericMailRequest request(MeetingRequest meetingRequest);
}