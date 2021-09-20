package com.io.fastmeet.services.impl;

import com.io.fastmeet.builders.ICalendarBuilder;
import com.io.fastmeet.entitites.Meeting;
import com.io.fastmeet.models.requests.meet.MeetingRequest;
import com.io.fastmeet.services.IcsService;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import org.apache.http.client.utils.DateUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class IcsServiceImpl implements IcsService {

    private ICalendarBuilder iCalendarBuilder ;

    @Override
    public byte[] writeIcsFileToByteArray(MeetingRequest request, String filePath) throws IOException {

        iCalendarBuilder = new ICalendarBuilder(request);
        Calendar calendar = iCalendarBuilder.build();

        CalendarOutputter calendarOutputter = new CalendarOutputter();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        calendarOutputter.output(calendar , os);

        return os.toByteArray();
    }

    @Override
    public MeetingRequest createMeetingRequest(Calendar calendar, Meeting meeting) {
        MeetingRequest request = new MeetingRequest();
        VEvent eventComponent = calendar.getComponent(net.fortuna.ical4j.model.Component.VEVENT);

        request.setMethod(calendar.getProperty(Property.METHOD));
        request.setOrganizer(eventComponent.getProperty(Property.ORGANIZER).getValue().toLowerCase().replace("mailto:",""));
        request.setDescription(eventComponent.getDescription().toString());
        request.setDuration(eventComponent.getDuration().hashCode());
        request.setIcsUid(eventComponent.getProperty(Property.UID).getValue());
        request.setLocation(eventComponent.getProperty(Property.LOCATION).getValue());
        request.setMeetingTitle(eventComponent.getProperty(Property.SUMMARY).getValue());
        request.setParticipants(eventComponent.getComponent(Property.ATTENDEE));



        return request;
    }
}
