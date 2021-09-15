package com.io.fastmeet.models.requests.meet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRequest {

    private String meetingTitle;

    private List<String> participants;

    private String location;

    private  Date startDate;

    private  Date endDate;

    private long Duration;

    private String organizer;

    private String icsUid;

    private String timeZone;

    private String description;

    private String calendarId;



}
