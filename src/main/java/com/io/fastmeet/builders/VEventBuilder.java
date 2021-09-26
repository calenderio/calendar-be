package com.io.fastmeet.builders;

import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.ParameterList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Encoding;
import net.fortuna.ical4j.model.parameter.FmtType;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.parameter.XParameter;
import net.fortuna.ical4j.model.property.Attach;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.Sequence;
import net.fortuna.ical4j.model.property.TzId;
import net.fortuna.ical4j.model.property.Uid;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;


public class VEventBuilder {
    private final VEvent vEvent;

    public VEventBuilder(Date startDate, Date endDate, String eventTitle, String timeZone) {
        PropertyList<Property> propertyList = new PropertyList<>();
        propertyList.add(new TzId(timeZone));
        DateTime start = new DateTime(startDate, new TimeZone(new VTimeZone(propertyList)));
        DateTime end = new DateTime(endDate, new TimeZone(new VTimeZone(propertyList)));

        vEvent = new VEvent(start, end, eventTitle);

    }

    private void addProperty(Property property) {
        vEvent.getProperties().add(property);
    }

    public VEvent build() {
        return vEvent;
    }


    public VEventBuilder addIcsUid(String icsUid) {
        addProperty(new Uid(icsUid));
        return this;
    }

    public VEventBuilder addTimeZone(String id) {
        TzId tzId = new TzId(id);
        addProperty(tzId);
        return this;
    }

    public VEventBuilder addDescription(String description) {
        addProperty(new Description(description));
        return this;
    }

    public VEventBuilder addLocation(String location) {
        addProperty(new Location(location));
        return this;
    }

    public VEventBuilder setSequence(Integer sequence) {
        addProperty(new Sequence(sequence));
        return this;
    }

    public VEventBuilder addAttachment(byte[] bytes, String fileName, String type) {
        ParameterList parameters = new ParameterList();
        parameters.add(new FmtType(type));
        parameters.add(new XParameter("X-FILENAME", fileName));
        parameters.add(Encoding.BASE64);
        parameters.add(Value.BINARY);
        addProperty(new Attach(parameters, bytes));
        return this;
    }

    public VEventBuilder addOrganizer(String organizer, String organizerName) throws URISyntaxException {
        ParameterList parameters = new ParameterList();
        parameters.add(new Cn(organizerName));
        addProperty(new Organizer(parameters, organizer));
        return this;
    }

    public VEventBuilder addParticipants(List<String> participants) {
        for (String participant : participants) {
            Attendee attendee = new Attendee(URI.create("mailto:" + participant));
            attendee.getParameters().add(Role.REQ_PARTICIPANT);
            vEvent.getProperties().add(attendee);
        }
        return this;
    }

}
