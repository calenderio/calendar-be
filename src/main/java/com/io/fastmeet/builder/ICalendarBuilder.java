package com.io.fastmeet.builder;

import com.io.fastmeet.models.requests.meet.MeetingRequest;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.CalendarException;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VTimeZone;
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
        VEventBuilder vEventBuilder = new VEventBuilder(meeting.getStartDate(), meeting.getEndDate(), meeting.getMeetingTitle(), meeting.getTimeZone());
        try {
            vEventBuilder
                    .addOrganizer(meeting.getOrganizer())
                    .addParticipants(meeting.getParticipants())
                    .addTimeZone(meeting.getTimeZone())
                    .addDescription(meeting.getDescription())
                    .addIcsUid(meeting.getIcsUid())
                    .addSummary(meeting.getMeetingTitle());

        } catch (Exception exc) {
            throw new CalendarException("Invalid Meeting");
        }
        addComponent(vEventBuilder.build());
    }

    public void addMethod(Method method) {
        VTimeZone timeZone = new VTimeZone.Factory().createComponent();

        addProperty(method);
        addComponent(timeZone);
    }

    private void addProperty(Property property) {
        iCalendar.getProperties().add(property);
    }

    private void addComponent(CalendarComponent component) {
        iCalendar.getComponents().add(component);
    }

    private Calendar build() {
        return iCalendar;
    }

}
