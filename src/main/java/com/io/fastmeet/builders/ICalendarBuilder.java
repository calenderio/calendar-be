package com.io.fastmeet.builders;

import com.io.fastmeet.models.requests.meet.MeetingRequest;
import com.io.fastmeet.utils.DateUtil;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.CalendarException;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;

public class ICalendarBuilder {


    private String fastMeetProdId = "-//Fastmeet//2.0//TR";

    private Calendar iCalendar;

    public ICalendarBuilder(MeetingRequest meetingRequest) {
        iCalendar = new Calendar();
        addProperty(new ProdId(fastMeetProdId));
        addProperty(Version.VERSION_2_0);
        addProperty(CalScale.GREGORIAN);
        addMethod(Method.REQUEST);
        addEvent(meetingRequest);
    }


    private void addEvent(MeetingRequest meeting) {
        VEventBuilder vEventBuilder = new VEventBuilder(DateUtil.localDateTimeToDate(meeting.getStartDate(), meeting.getTimeZone()),
                DateUtil.localDateTimeToDate(meeting.getEndDate(), meeting.getTimeZone()),
                meeting.getMeetingTitle(), meeting.getTimeZone());
        try {
            vEventBuilder
                    .addOrganizer(meeting.getOrganizer())
                    .addParticipants(meeting.getParticipants())
                    .addDescription(meeting.getDescription())
                    .addIcsUid(meeting.getIcsUid())
                    .addTimeZone(meeting.getTimeZone())
                    .addLocation(meeting.getLocation());

        } catch (Exception exc) {
            throw new CalendarException("Invalid Meeting");
        }
        addComponent(vEventBuilder.build());
    }

    public void addMethod(Method method) {
        addProperty(method);
    }

    private void addProperty(Property property) {
        iCalendar.getProperties().add(property);
    }

    private void addComponent(CalendarComponent component) {
        iCalendar.getComponents().add(component);
    }

    public Calendar build() {
        return iCalendar;
    }

}
