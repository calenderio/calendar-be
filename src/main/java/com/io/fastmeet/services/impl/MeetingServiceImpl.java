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
    private IcsService icsService;

    @Autowired
    private MailService mailService;

    @Autowired
    private MeetingMapper meetingMapper;

    @Autowired
    private MeetingRepository meetingRepository;


    @Override
    public void sendInvitationMailAndSaveMeeting(MeetingRequest request) {
        GenericMailRequest toInvitationMail = meetingMapper.request(request);
        try {
            toInvitationMail.setMeetingDetails(icsService.writeIcsFileToByteArray(request));
        } catch (IOException e) {
            e.printStackTrace();
        }
        meetingRepository.save(meetingRequestToMeeting(request));
    }

    private Meeting meetingRequestToMeeting(MeetingRequest request) {
        return meetingMapper.mapToMeeting(request);
    }
}
