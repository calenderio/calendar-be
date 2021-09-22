package com.io.fastmeet.mappers;


import com.io.fastmeet.entitites.Meeting;
import com.io.fastmeet.models.internals.GenericMailRequest;
import com.io.fastmeet.models.requests.meet.MeetingRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MeetingMapper {

    @Mapping(target = "inviter", source = "organizer")
    @Mapping(target = "emails", source = "participants")
    GenericMailRequest request(MeetingRequest meetingRequest);

    Meeting mapToMeeting(MeetingRequest meetingRequest);

}