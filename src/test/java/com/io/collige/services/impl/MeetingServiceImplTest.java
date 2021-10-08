/*
 * @author : Oguz Kahraman
 * @since : 26.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.io.collige.core.exception.CalendarAppException;
import com.io.collige.entitites.Event;
import com.io.collige.entitites.Invitation;
import com.io.collige.entitites.Meeting;
import com.io.collige.entitites.Question;
import com.io.collige.entitites.User;
import com.io.collige.enums.EventLocation;
import com.io.collige.enums.QuestionType;
import com.io.collige.mappers.MeetingMapper;
import com.io.collige.models.internals.AttachmentModel;
import com.io.collige.models.internals.AvailableDatesDetails;
import com.io.collige.models.internals.GenericMailRequest;
import com.io.collige.models.internals.ScheduleMeetingDetails;
import com.io.collige.models.requests.calendar.QuestionData;
import com.io.collige.models.requests.calendar.ScheduleMeetingRequest;
import com.io.collige.models.requests.meet.MeetingRequest;
import com.io.collige.repositories.InvitationRepository;
import com.io.collige.repositories.MeetingRepository;
import com.io.collige.services.CalendarService;
import com.io.collige.services.CloudService;
import com.io.collige.services.IcsService;
import com.io.collige.services.MailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeetingServiceImplTest {

    @Mock
    private IcsService icsService;

    @Mock
    private MailService mailService;

    @Mock
    private CalendarService calendarService;

    @Mock
    private MeetingMapper meetingMapper;

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private CloudService cloudService;

    @InjectMocks
    private MeetingServiceImpl meetingService;

    @Test
    void validateAndScheduleMeeting_witherror() {
        ScheduleMeetingDetails meetingDetails = new ScheduleMeetingDetails();
        ScheduleMeetingRequest meetingRequest = new ScheduleMeetingRequest();
        meetingRequest.setDate(LocalDate.now());
        meetingRequest.setTime(LocalTime.now());
        meetingRequest.setTimeZone("UTC");
        meetingDetails.setRequest(meetingRequest);
        meetingDetails.setInvitationId("1");
        meetingDetails.setModels(new ArrayList<>());
        when(calendarService.getAvailableDates(any(), anyString(), anyString())).thenReturn(getDateDetails());
        CalendarAppException exception = assertThrows(CalendarAppException.class, () -> meetingService.validateAndScheduleMeeting(meetingDetails));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("NOT_VALID_DATE", exception.getCause().getMessage());
    }

    @Test
    void validateAndScheduleMeeting() {
        ScheduleMeetingDetails meetingDetails = new ScheduleMeetingDetails();
        ScheduleMeetingRequest meetingRequest = new ScheduleMeetingRequest();
        meetingRequest.setDate(LocalDate.now());
        meetingRequest.setTime(LocalTime.of(12, 12));
        meetingRequest.setTimeZone("UTC");
        meetingDetails.setRequest(meetingRequest);
        meetingDetails.setInvitationId("1");
        meetingDetails.setModels(new ArrayList<>());
        when(calendarService.getAvailableDates(any(), anyString(), anyString())).thenReturn(getDateDetails());
        when(meetingMapper.mapToMeeting(any())).thenReturn(new Meeting());
        when(meetingMapper.request(any())).thenReturn(new GenericMailRequest());
        meetingService.validateAndScheduleMeeting(meetingDetails);
        verify(mailService, times(1)).sendScheduledInvitation(any());
    }

    @Test
    void validateAndScheduleMeeting_fileError() {
        ScheduleMeetingDetails meetingDetails = new ScheduleMeetingDetails();
        ScheduleMeetingRequest meetingRequest = new ScheduleMeetingRequest();
        meetingRequest.setDate(LocalDate.now());
        meetingRequest.setTime(LocalTime.of(12, 12));
        meetingRequest.setTimeZone("UTC");
        meetingDetails.setRequest(meetingRequest);
        meetingDetails.setInvitationId("1");
        meetingDetails.setModels(new ArrayList<>());
        AvailableDatesDetails availableDatesDetails = getDateDetails();
        availableDatesDetails.getInvitation().getEvent().setFileRequired(true);
        when(calendarService.getAvailableDates(any(), anyString(), anyString())).thenReturn(availableDatesDetails);
        CalendarAppException exception = assertThrows(CalendarAppException.class, () -> meetingService.validateAndScheduleMeeting(meetingDetails));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("NULL_FILE", exception.getCause().getMessage());
    }

    @Test
    void validateAndScheduleMeeting_file() {
        AttachmentModel model = new AttachmentModel();
        model.setName("example");
        model.setType("text");
        model.setData("text".getBytes());
        ScheduleMeetingDetails meetingDetails = new ScheduleMeetingDetails();
        ScheduleMeetingRequest meetingRequest = new ScheduleMeetingRequest();
        meetingRequest.setDate(LocalDate.now());
        meetingRequest.setTime(LocalTime.of(12, 12));
        meetingRequest.setTimeZone("UTC");
        meetingDetails.setRequest(meetingRequest);
        meetingDetails.setInvitationId("1");
        meetingDetails.setModels(Collections.singletonList(model));
        meetingRequest.setAnswers(setAnswers());
        AvailableDatesDetails availableDatesDetails = getDateDetails();
        availableDatesDetails.getInvitation().getEvent().setFileRequired(true);
        setQuestions(availableDatesDetails.getInvitation().getEvent());
        when(calendarService.getAvailableDates(any(), anyString(), anyString())).thenReturn(availableDatesDetails);
        when(meetingMapper.mapToMeeting(any())).thenReturn(new Meeting());
        when(meetingMapper.request(any())).thenReturn(new GenericMailRequest());
        meetingService.validateAndScheduleMeeting(meetingDetails);
        verify(mailService, times(1)).sendScheduledInvitation(any());
    }

    @Test
    void updateMeetingRequest() {
        AttachmentModel model = new AttachmentModel();
        model.setName("example");
        model.setType("text");
        model.setData("text".getBytes());
        ScheduleMeetingDetails meetingDetails = new ScheduleMeetingDetails();
        ScheduleMeetingRequest meetingRequest = new ScheduleMeetingRequest();
        meetingRequest.setDate(LocalDate.now());
        meetingRequest.setTime(LocalTime.of(12, 12));
        meetingRequest.setTimeZone("UTC");
        meetingDetails.setRequest(meetingRequest);
        meetingDetails.setInvitationId("1");
        meetingDetails.setModels(Collections.singletonList(model));
        meetingRequest.setAnswers(setAnswers());
        AvailableDatesDetails availableDatesDetails = getDateDetails();
        availableDatesDetails.getInvitation().getEvent().setFileRequired(true);
        availableDatesDetails.getInvitation().setScheduled(true);
        setQuestions(availableDatesDetails.getInvitation().getEvent());
        Meeting meeting = new Meeting();
        meeting.setSequence(1);
        when(calendarService.getAvailableDates(any(), anyString(), anyString())).thenReturn(availableDatesDetails);
        when(meetingMapper.mapToMeeting(any())).thenReturn(new Meeting());
        when(meetingMapper.request(any())).thenReturn(new GenericMailRequest());
        when(meetingRepository.findByInvitationId(1L)).thenReturn(Optional.of(meeting));
        when(meetingMapper.mapEntityToRequest(any())).thenReturn(new MeetingRequest());
        meetingService.updateMeetingRequest(meetingDetails);
        verify(mailService, times(1)).sendScheduledInvitation(any());
        verify(meetingRepository, times(1)).save(any());
    }

    @Test
    void deleteMeetingRequest() {
        ScheduleMeetingDetails meetingDetails = new ScheduleMeetingDetails();
        ScheduleMeetingRequest meetingRequest = new ScheduleMeetingRequest();
        meetingRequest.setDate(LocalDate.now());
        meetingRequest.setTime(LocalTime.of(12, 12));
        meetingRequest.setTimeZone("UTC");
        meetingDetails.setRequest(meetingRequest);
        meetingDetails.setInvitationId("1");
        meetingRequest.setAnswers(setAnswers());
        Invitation invitation = new Invitation();
        invitation.setId(1L);
        invitation.setUser(new User());
        Meeting meeting = new Meeting();
        meeting.setSequence(1);
        when(invitationRepository.findByInvitationIdAndScheduledIsTrue("1")).thenReturn(Optional.of(invitation));
        when(meetingRepository.findByInvitationId(1L)).thenReturn(Optional.of(meeting));
        when(meetingMapper.mapEntityToRequest(any())).thenReturn(new MeetingRequest());
        when(meetingMapper.request(any())).thenReturn(new GenericMailRequest());
        meetingService.deleteMeetingRequest(meetingDetails);
        verify(mailService, times(1)).sendScheduledInvitation(any());
        verify(meetingRepository, times(1)).save(any());
        verify(meetingRepository, times(1)).deleteMeeting(any());
    }

    private Map<LocalDate, Set<LocalTime>> generateDates() {
        Map<LocalDate, Set<LocalTime>> dates = new HashMap<>();
        dates.put(LocalDate.now(), Collections.singleton(LocalTime.of(12, 12)));
        return dates;
    }

    private AvailableDatesDetails getDateDetails() {
        Invitation invitation = new Invitation();
        Event event = new Event();
        User user = new User();
        user.setEmail("example");
        event.setTimeZone("UTC");
        event.setId(1L);
        event.setLocation(EventLocation.ZOOM);
        invitation.setEvent(event);
        invitation.setUser(user);
        invitation.setId(1L);
        invitation.setAnswers(new ArrayList<>());
        AvailableDatesDetails availableDatesDetails = new AvailableDatesDetails();
        availableDatesDetails.setAvailableDates(generateDates());
        availableDatesDetails.setInvitation(invitation);
        return availableDatesDetails;
    }

    private void setQuestions(Event event) {
        List<Question> questionList = new ArrayList<>();
        Question question = new Question();
        question.setId(1L);
        question.setType(QuestionType.BOOL);
        question.setText("example");
        question.setRequired(false);
        questionList.add(question);
        Question question2 = new Question();
        question2.setId(2L);
        question2.setText("example");
        question2.setRequired(false);
        question2.setType(QuestionType.TEXT);
        question2.setLengthMin(1);
        question2.setLengthMax(10);
        questionList.add(question2);
        Question question3 = new Question();
        question3.setId(3L);
        question3.setText("1");
        question3.setRequired(false);
        question3.setType(QuestionType.NUMERIC);
        question3.setLengthMin(1);
        question3.setLengthMax(10);
        questionList.add(question3);
        Question question4 = new Question();
        question4.setId(4L);
        question4.setText("1");
        question4.setRequired(false);
        question4.setType(QuestionType.MULTI);
        question4.setLengthMin(1);
        question4.setLengthMax(10);
        questionList.add(question4);
        event.setQuestions(questionList);
    }

    private List<QuestionData> setAnswers() {
        List<QuestionData> questionList = new ArrayList<>();

        QuestionData data = new QuestionData();
        data.setId(1L);
        data.setAnswerList(Collections.singletonList("abc"));
        questionList.add(data);

        QuestionData data2 = new QuestionData();
        data2.setId(2L);
        data2.setAnswerList(Collections.singletonList("abc"));
        questionList.add(data2);

        QuestionData data3 = new QuestionData();
        data3.setId(3L);
        data3.setAnswerList(Collections.singletonList("1"));
        questionList.add(data3);

        QuestionData data4 = new QuestionData();
        data4.setId(4L);
        data4.setAnswerList(Collections.singletonList("1"));
        questionList.add(data4);

        return questionList;
    }
}