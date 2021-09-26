package com.io.collige.mappers;


import com.io.collige.entitites.Meeting;
import com.io.collige.models.internals.AvailableDatesDetails;
import com.io.collige.models.internals.GenericMailRequest;
import com.io.collige.models.requests.meet.MeetingRequest;
import com.io.collige.models.responses.meeting.ScheduledMeetingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MeetingMapper {

    @Mapping(target = "inviter", source = "organizer")
    @Mapping(target = "emails", source = "participants")
    @Mapping(target = "header", source = "title")
    @Mapping(target = "method", source = "method.value")
    GenericMailRequest request(MeetingRequest meetingRequest);

    Meeting mapToMeeting(MeetingRequest meetingRequest);

    MeetingRequest mapEntityToRequest(Meeting meetingRequest);

    @Mapping(source = "invitation.event.timeZone", target = "timeZone")
    @Mapping(source = "invitation.event.description", target = "description")
    @Mapping(source = "invitation.event.duration", target = "duration.duration")
    @Mapping(source = "invitation.event.durationType", target = "duration.durationType")
    @Mapping(source = "invitation.event.fileRequired", target = "fileRequired")
    @Mapping(source = "invitation.event.fileDescription", target = "fileDescription")
    @Mapping(source = "invitation.user.name", target = "name")
    @Mapping(source = "invitation.user.email", target = "email")
    @Mapping(source = "invitation.user.picture", target = "picture")
    @Mapping(source = "invitation.scheduled", target = "scheduled")
    ScheduledMeetingResponse detailsToModel(AvailableDatesDetails details);
}