package com.io.fastmeet.builder;

import com.io.fastmeet.models.requests.meet.MeetingRequest;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.CalendarException;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ICalendarBuilder {


    private static String fastMeetProdId = "-//Fastmeet//2.0//TR";


    private  VEventBuilder vEventBuilder;
    private Calendar iCalendar;

    public ICalendarBuilder() {
        iCalendar = new Calendar();
        addProperty(new ProdId(fastMeetProdId));
        addProperty(Version.VERSION_2_0);
        addProperty(CalScale.GREGORIAN);
    }

    public Calendar createCalendarForSendEmail(MeetingRequest meetingRequest, List<String> participants){
        vEventBuilder = initEventBuilder(meetingRequest , participants);
        createCalendar(Method.REQUEST);
        return iCalendar;
    }


    private VEventBuilder initEventBuilder(MeetingRequest meeting, List<String> receivers) {
        try {
            vEventBuilder = new VEventBuilder(meeting.getStartDate(), meeting.getEndDate(), meeting.getMeetingTitle(), meeting.getTimeZone());

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

        return vEventBuilder;
    }

    public void createCalendar(Method method) {
        VEvent vEvent = vEventBuilder.build();

        VTimeZone timeZone = new VTimeZone.Factory().createComponent();

        addProperty(method);
        addComponent(timeZone);
        addComponent(vEvent);
    }

    private void addProperty(Property property) {
        iCalendar.getProperties().add(property);
    }

    private void addComponent(CalendarComponent component) {
        iCalendar.getComponents().add(component);
    }

}
