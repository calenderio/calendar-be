package com.io.fastmeet.services;

import com.io.fastmeet.models.requests.meet.MeetingRequest;

public interface MeetingService {

    void  sendInvitationMail (MeetingRequest request);

}
