package com.io.fastmeet.services.impl;


import com.io.fastmeet.entitites.Meeting;
import com.io.fastmeet.mappers.MeetingMapper;
import com.io.fastmeet.models.internals.GenericMailRequest;
import com.io.fastmeet.models.requests.meet.MeetingRequest;
import com.io.fastmeet.repositories.MeetingRepository;
import com.io.fastmeet.services.IcsService;
import com.io.fastmeet.services.MailService;
import com.io.fastmeet.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    IcsService icsService;

    @Autowired
    MailService mailService;

    @Autowired
    MeetingMapper meetingMapper;

    @Autowired
    MeetingRepository meetingRepository;


    @Override
    public void sendInvitationMailandSaveMeeting(MeetingRequest request) {
        GenericMailRequest toInvitationMail = meetingMapper.request(request);
        try {
            toInvitationMail.setMeetingDetails(icsService.writeIcsFileToByteArray(request));
        } catch (IOException e) {
            e.printStackTrace();
        }
        meetingRepository.save(meetingRequestToMeeeting(request));
    }

    private Meeting meetingRequestToMeeeting(MeetingRequest request){
        Meeting detachedMeeting = meetingMapper.mapToMeeting(request);
        return detachedMeeting;
    }
}
