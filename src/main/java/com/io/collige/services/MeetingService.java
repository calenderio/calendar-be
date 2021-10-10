package com.io.collige.services;

import com.io.collige.models.internals.scheduler.ScheduleMeetingRequest;

public interface MeetingService {

    void validateAndScheduleMeeting(ScheduleMeetingRequest details);

    void updateMeetingRequest(ScheduleMeetingRequest details);

    void deleteMeetingRequest(ScheduleMeetingRequest details);
}
