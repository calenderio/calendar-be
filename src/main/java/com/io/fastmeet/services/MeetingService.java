package com.io.fastmeet.services;

import com.io.fastmeet.models.internals.ScheduleMeetingDetails;

public interface MeetingService {

    void validateAndScheduleMeeting(ScheduleMeetingDetails details);

}
