package com.io.fastmeet.services.impl;

import com.io.fastmeet.builders.ICalendarBuilder;
import com.io.fastmeet.models.requests.meet.MeetingRequest;
import com.io.fastmeet.services.IcsService;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class IcsServiceImpl implements IcsService {

    @Override
    public byte[] writeIcsFileToByteArray(MeetingRequest request) throws IOException {

        ICalendarBuilder iCalendarBuilder = new ICalendarBuilder(request);
        Calendar calendar = iCalendarBuilder.build();

        CalendarOutputter calendarOutputter = new CalendarOutputter();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        calendarOutputter.output(calendar, os);

        return os.toByteArray();
    }


}
