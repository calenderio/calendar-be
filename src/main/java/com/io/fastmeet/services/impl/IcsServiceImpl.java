package com.io.fastmeet.services.impl;

import com.io.fastmeet.builders.ICalendarBuilder;
import com.io.fastmeet.models.requests.meet.MeetingRequest;
import com.io.fastmeet.services.IcsService;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class IcsServiceImpl implements IcsService {

    private ICalendarBuilder iCalendarBuilder ;

    @Override
    public byte[] writeIcsFileToByteArray(MeetingRequest request, String filePath) throws IOException {

        iCalendarBuilder = new ICalendarBuilder(request);
        Calendar calendar = iCalendarBuilder.build();

        CalendarOutputter outputter = new CalendarOutputter();
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        return os.toByteArray();
    }
}
