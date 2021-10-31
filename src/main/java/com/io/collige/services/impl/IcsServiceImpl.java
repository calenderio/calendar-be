package com.io.collige.services.impl;

import com.io.collige.builders.ICalendarBuilder;
import com.io.collige.core.i18n.Translator;
import com.io.collige.models.requests.meet.MeetingRequest;
import com.io.collige.services.IcsService;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class IcsServiceImpl implements IcsService {

    @Override
    public byte[] writeIcsFileToByteArray(MeetingRequest request) throws IOException {
        String message = Translator.getMessage("meet.general.message", WordUtils.capitalize(request.getOrganizerName()),
                request.getOrganizerMail().toLowerCase(), request.getDescription(), request.getMeetingLink());

        ICalendarBuilder iCalendarBuilder = new ICalendarBuilder(request, message);
        Calendar calendar = iCalendarBuilder.build();
        CalendarOutputter calendarOutputter = new CalendarOutputter();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        calendarOutputter.output(calendar, os);

        return os.toByteArray();
    }


}
