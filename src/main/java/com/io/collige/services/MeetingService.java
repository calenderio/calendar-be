package com.io.collige.services;

import com.io.collige.models.internals.ScheduleMeetingDetails;

public interface MeetingService {

    void validateAndScheduleMeeting(ScheduleMeetingDetails details);

    void updateMeetingRequest(ScheduleMeetingDetails details);

    void deleteMeetingRequest(ScheduleMeetingDetails details);
}
