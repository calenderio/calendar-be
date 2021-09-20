package com.io.fastmeet.models.requests.meet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fortuna.ical4j.model.property.Method;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRequest {

    private Method method;

    private String meetingTitle;

    private List<String> participants;

    private String location;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private long duration;

    private String organizer;

    private String icsUid;

    private String timeZone;

    private String description;

    private String calendarId;


}
