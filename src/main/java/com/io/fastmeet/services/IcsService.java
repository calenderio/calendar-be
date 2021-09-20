package com.io.fastmeet.services;

import com.io.fastmeet.entitites.Meeting;
import com.io.fastmeet.models.requests.meet.MeetingRequest;
import net.fortuna.ical4j.model.Calendar;

import java.io.IOException;

public interface IcsService {

    byte[] writeIcsFileToByteArray(MeetingRequest request , String filePath) throws IOException;

    MeetingRequest createMeetingRequest(Calendar calendar, Meeting meeting);
}
