package com.io.collige.builders;

import com.io.collige.models.internals.file.AttachmentModel;
import com.io.collige.models.requests.meet.MeetingRequest;
import com.io.collige.utils.DateUtil;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.CalendarException;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Status;
import net.fortuna.ical4j.model.property.Version;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;

public class ICalendarBuilder {


    private static final String FASST_MEET_ID = "-//Collige//2.0//TR";

    private final Calendar iCalendar;

    public ICalendarBuilder(MeetingRequest meetingRequest, String html) {
        iCalendar = new Calendar();
        addProperty(new ProdId(FASST_MEET_ID));
        addProperty(Version.VERSION_2_0);
        addProperty(CalScale.GREGORIAN);
        addMethod(meetingRequest.getMethod());
        addEvent(meetingRequest, html);
    }


    private void addEvent(MeetingRequest meeting, String html) {
        VEventBuilder vEventBuilder = new VEventBuilder(DateUtil.localDateTimeToDate(meeting.getStartDate(), meeting.getTimeZone()),
                DateUtil.localDateTimeToDate(meeting.getEndDate(), meeting.getTimeZone()),
                meeting.getTitle(), meeting.getTimeZone());
        try {
            vEventBuilder
                    .addOrganizer(meeting.getOrganizer(), meeting.getOrganizerName())
                    .addParticipants(new ArrayList<>(meeting.getParticipants()))
                    .addDescription(html)
                    .addIcsUid(meeting.getUuid().toString())
                    .addTimeZone(meeting.getTimeZone())
                    .addLocation(meeting.getLocation())
                    .setSequence(meeting.getSequence());

            if (CollectionUtils.isNotEmpty(meeting.getAlarms())) {
                for (Long alarm : meeting.getAlarms()) {
                    vEventBuilder.addAlarm(meeting.getDescription(), alarm);
                }
            }

            if (!meeting.getAttachmentModels().isEmpty()) {
                for (AttachmentModel model : meeting.getAttachmentModels()) {
                    vEventBuilder.addAttachment(model.getData(), model.getName(), model.getType());
                }
            }

        } catch (Exception exc) {
            throw new CalendarException("Invalid Meeting");
        }
        addComponent(vEventBuilder.build());
    }

    public void addMethod(Method method) {
        if (Method.CANCEL.equals(method)) {
            addProperty(new Status("CANCELED"));
        }
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
