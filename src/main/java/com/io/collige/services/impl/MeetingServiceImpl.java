package com.io.collige.services.impl;


import com.io.collige.core.exception.CalendarAppException;
import com.io.collige.core.i18n.Translator;
import com.io.collige.entitites.Answer;
import com.io.collige.entitites.Event;
import com.io.collige.entitites.Invitation;
import com.io.collige.entitites.Meeting;
import com.io.collige.entitites.Question;
import com.io.collige.enums.DurationType;
import com.io.collige.mappers.MeetingMapper;
import com.io.collige.models.internals.event.AlarmDuration;
import com.io.collige.models.internals.event.AvailableDatesDetails;
import com.io.collige.models.internals.event.QuestionAnswerModel;
import com.io.collige.models.internals.file.AttachmentModel;
import com.io.collige.models.internals.file.DeleteInvitationFileRequest;
import com.io.collige.models.internals.file.UploadMeetingFileRequest;
import com.io.collige.models.internals.mail.GenericMailRequest;
import com.io.collige.models.internals.scheduler.ScheduleMeetingRequest;
import com.io.collige.models.requests.calendar.QuestionData;
import com.io.collige.models.requests.meet.GetAvailableDateRequest;
import com.io.collige.models.requests.meet.MeetingRequest;
import com.io.collige.repositories.InvitationRepository;
import com.io.collige.repositories.MeetingRepository;
import com.io.collige.services.CalendarService;
import com.io.collige.services.CloudService;
import com.io.collige.services.IcsService;
import com.io.collige.services.MailService;
import com.io.collige.services.MeetingLocationService;
import com.io.collige.services.MeetingService;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.model.property.Method;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    private IcsService icsService;

    @Autowired
    private MailService mailService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private MeetingMapper meetingMapper;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private MeetingLocationService meetingLocationService;

    @Override
    public void validateAndScheduleMeeting(ScheduleMeetingRequest details) {
        com.io.collige.models.requests.calendar.ScheduleMeetingRequest request = details.getRequest();
        GetAvailableDateRequest availableDateRequest = new GetAvailableDateRequest();
        availableDateRequest.setInvitationId(details.getInvitationId());
        availableDateRequest.setTimeZone(request.getTimeZone());
        availableDateRequest.setLocalDate(request.getDate());
        AvailableDatesDetails dateDetails = calendarService.getAvailableDates(availableDateRequest);
        Map<LocalDate, Set<LocalTime>> availableDates = dateDetails.getAvailableDates();
        Invitation invitation = dateDetails.getInvitation();
        checkDates(request, availableDates, invitation.getEvent());
        List<QuestionAnswerModel> questionAnswerModels = generateQAModel(request, invitation);

        MeetingRequest meetingRequest = new MeetingRequest();
        meetingRequest.setTitle(invitation.getName() + " - " + invitation.getTitle());
        meetingRequest.setLocation(invitation.getEvent().getLocation().name());
        meetingRequest.setUuid(UUID.randomUUID());
        meetingRequest.setDescription(invitation.getDescription());
        meetingRequest.setOrganizer("info@collige.io");
        meetingRequest.getParticipants().add(invitation.getUser().getEmail());
        if (invitation.getCcList() != null) {
            meetingRequest.getParticipants().addAll(invitation.getCcList());
        }
        meetingRequest.getParticipants().add(invitation.getUserEmail());

        setDates(details, invitation, meetingRequest);
        meetingRequest.setSequence(1);
        generateAttachmentForQuestion(questionAnswerModels, meetingRequest);
        invitation.setScheduled(true);
        Meeting meeting = meetingMapper.mapToMeeting(meetingRequest);
        meeting.setEvent(invitation.getEvent());
        meeting.setFileLinks(cloudService.uploadMeetingFiles(new UploadMeetingFileRequest(details.getInvitationId(),
                meetingRequest.getAttachmentModels(), invitation.getUser().getId())));
        sendInvitationMailAndSaveMeeting(meetingRequest, invitation, meeting);
    }

    @Override
    public void updateMeetingRequest(ScheduleMeetingRequest details) {
        GetAvailableDateRequest availableDateRequest = new GetAvailableDateRequest();
        availableDateRequest.setInvitationId(details.getInvitationId());
        availableDateRequest.setTimeZone(details.getRequest().getTimeZone());
        availableDateRequest.setLocalDate(details.getRequest().getDate());
        AvailableDatesDetails dateDetails = calendarService.getAvailableDates(availableDateRequest);
        Map<LocalDate, Set<LocalTime>> availableDates = dateDetails.getAvailableDates();
        Invitation invitation = dateDetails.getInvitation();
        if (Boolean.FALSE.equals(invitation.getScheduled())) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, "Invitation couldn't scheduled", "NOT_SCHEDULED");
        }
        checkDates(details.getRequest(), availableDates, invitation.getEvent());
        List<QuestionAnswerModel> questionAnswerModels = generateQAModel(details.getRequest(), invitation);
        Meeting meeting = meetingRepository.findByInvitationId(invitation.getId()).orElseThrow(() ->
                new CalendarAppException(HttpStatus.BAD_REQUEST, "Meeting couldn't find", "MEET_NOT_VALID"));
        MeetingRequest meetingRequest = meetingMapper.mapEntityToRequest(meeting);
        meetingRequest.setSequence(meeting.getSequence() + 1);
        setDates(details, invitation, meetingRequest);
        generateAttachmentForQuestion(questionAnswerModels, meetingRequest);
        Meeting newOne = meetingMapper.mapToMeeting(meetingRequest);
        newOne.setId(meeting.getId());
        newOne.setEvent(invitation.getEvent());
        newOne.setFileLinks(cloudService.uploadMeetingFiles(new UploadMeetingFileRequest(details.getInvitationId(),
                meetingRequest.getAttachmentModels(), invitation.getUser().getId())));
        sendInvitationMailAndSaveMeeting(meetingRequest, invitation, newOne);
    }

    @Override
    public void deleteMeetingRequest(ScheduleMeetingRequest details) {
        Invitation invitation = invitationRepository.findByInvitationIdAndScheduledIsTrue(details.getInvitationId()).orElseThrow(() ->
                new CalendarAppException(HttpStatus.BAD_REQUEST, "Invitation couldn't scheduled", "NOT_SCHEDULED"));

        Meeting meeting = meetingRepository.findByInvitationId(invitation.getId()).orElseThrow(() ->
                new CalendarAppException(HttpStatus.BAD_REQUEST, "Meeting couldn't find", "MEET_NOT_VALID"));

        MeetingRequest meetingRequest = meetingMapper.mapEntityToRequest(meeting);
        meetingRequest.setSequence(meeting.getSequence() + 1);
        meetingRequest.setMethod(Method.CANCEL);
        invitation.setScheduled(false);
        if (invitation.getAnswers() != null) {
            invitation.getAnswers().clear();
        }
        meetingRequest.setOrganizerName(invitation.getName());
        meetingRequest.setOrganizerMail(invitation.getUserEmail());
        sendInvitationMailAndSaveMeeting(meetingRequest, invitation, meeting);
        cloudService.deleteInvitationFiles(new DeleteInvitationFileRequest(details.getInvitationId(), invitation.getUser().getId()));
        meetingRepository.deleteMeeting(meeting.getId());
    }

    private List<QuestionAnswerModel> generateQAModel(com.io.collige.models.requests.calendar.ScheduleMeetingRequest request, Invitation invitation) {
        List<QuestionAnswerModel> questionAnswerModels = new ArrayList<>();
        invitation.getAnswers().clear();
        if (invitation.getEvent().getQuestions() != null && !invitation.getEvent().getQuestions().isEmpty()) {
            Map<Long, Question> questionMap = invitation.getEvent().getQuestions().stream().collect(Collectors.toMap(Question::getId, Function.identity()));
            List<Answer> answers = validateAnswers(questionMap, request.getAnswers(), invitation, questionAnswerModels);
            invitation.setAnswers(answers);
        }
        return questionAnswerModels;
    }

    private void checkDates(com.io.collige.models.requests.calendar.ScheduleMeetingRequest request, Map<LocalDate, Set<LocalTime>> availableDates, Event event) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(request.getDate(), request.getTime(), ZoneId.of(request.getTimeZone()))
                .withZoneSameInstant(ZoneId.of(event.getTimeZone()));
        if (!availableDates.containsKey(zonedDateTime.toLocalDate())) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, "Selected date not valid", "NOT_VALID_DATE");
        } else {
            Set<LocalTime> times = availableDates.get(zonedDateTime.toLocalDate());
            if (!times.contains(zonedDateTime.toLocalTime())) {
                throw new CalendarAppException(HttpStatus.BAD_REQUEST, "Selected date not valid", "NOT_VALID_DATE");
            }
        }
    }

    private void setDates(ScheduleMeetingRequest details, Invitation invitation, MeetingRequest meetingRequest) {
        com.io.collige.models.requests.calendar.ScheduleMeetingRequest request = details.getRequest();
        meetingRequest.setOrganizerName(invitation.getName());
        meetingRequest.setOrganizerMail(invitation.getUserEmail());
        meetingRequest.setStartDate(ZonedDateTime.of(request.getDate(), request.getTime(), ZoneId.of(request.getTimeZone()))
                .withZoneSameInstant(ZoneId.of(invitation.getEvent().getTimeZone())).toLocalDateTime());
        int duration = DurationType.HOUR.equals(invitation.getEvent().getDurationType()) ?
                60 * invitation.getEvent().getDuration() : invitation.getEvent().getDuration();
        meetingRequest.setEndDate(ZonedDateTime.of(request.getDate(), request.getTime(), ZoneId.of(request.getTimeZone()))
                .withZoneSameInstant(ZoneId.of(invitation.getEvent().getTimeZone())).toLocalDateTime().plusMinutes(duration));
        meetingRequest.setMethod(Method.REQUEST);
        meetingRequest.setTimeZone(invitation.getEvent().getTimeZone());
        if (CollectionUtils.isNotEmpty(invitation.getEvent().getAlarms())) {
            List<Long> alarms = new ArrayList<>();
            for (AlarmDuration duration1 : invitation.getEvent().getAlarms()) {
                DurationType drType = duration1.getDurationType();
                int alarm = duration1.getDuration();
                long min = DurationType.MIN.equals(drType) ? alarm : alarm * 60L;
                alarms.add(duration1.isBefore() ? min : min * -1);
            }
            meetingRequest.setAlarms(alarms);
        }
        if (invitation.getEvent().isFileRequired()) {
            if (CollectionUtils.isEmpty(details.getModels())) {
                throw new CalendarAppException(HttpStatus.BAD_REQUEST, "File can not be null", "NULL_FILE");
            } else {
                meetingRequest.getAttachmentModels().addAll(details.getModels());
            }
        }
    }

    private void sendInvitationMailAndSaveMeeting(MeetingRequest request, Invitation invitation, Meeting meeting) {
        meeting.setInvitation(invitation);
        if (Method.REQUEST.equals(request.getMethod())) {
            if (request.getSequence() == 1) {
                meetingLocationService.getLocationLink(meeting);
                request.setLocationId(meeting.getLocationId());
                request.setMeetingLink(meeting.getMeetingLink());
            }
        }
        GenericMailRequest toInvitationMail = meetingMapper.request(request);
        try {
            toInvitationMail.setMeetingDetails(icsService.writeIcsFileToByteArray(request));
            if (invitation.getBccList() != null) {
                toInvitationMail.setBcc(new HashSet<>(invitation.getBccList()));
            }
            toInvitationMail.getAttachments().addAll(request.getAttachmentModels());
            toInvitationMail.setLanguage(Translator.getLanguage());
            toInvitationMail.setName(invitation.getName());
            meeting.setDescription(invitation.getDescription());
            meeting.setTitle(invitation.getTitle());
            meeting.setUserId(invitation.getUser().getId());
            meetingRepository.save(meeting);
            mailService.sendScheduledInvitation(toInvitationMail);
        } catch (IOException e) {
            log.error("Invitation send error");
        }
    }

    private List<Answer> validateAnswers(Map<Long, Question> questionMap, List<QuestionData> answers, Invitation invitation, List<QuestionAnswerModel> questionAnswerModels) {
        List<Answer> answerList = new ArrayList<>();
        if (answers != null) {
            for (QuestionData answer : answers) {
                if (!questionMap.containsKey(answer.getId())) {
                    throw new CalendarAppException(HttpStatus.BAD_REQUEST, "Given question id couldn't find", "QUES_NOT_VALID");
                }
                Question question = questionMap.remove(answer.getId());
                switch (question.getType()) {
                    case TEXT -> textQuestionValidator(question, answer);
                    case MULTI -> multiQuestionValidator(question, answer);
                    case NUMERIC -> numericQuestionValidator(question, answer);
                    case BOOL -> Boolean.valueOf(answer.getAnswerList().get(0));
                }
                Answer entity = new Answer();
                entity.setEvent(invitation.getEvent());
                entity.setQuestion(question);
                entity.setText(answer.getAnswerList());
                entity.setInvitation(invitation);
                answerList.add(entity);
                questionAnswerModels.add(new QuestionAnswerModel(question.getText(), entity.getText()));
            }
        }
        if (questionMap.values().stream().anyMatch(Question::getRequired)) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, "Given question id couldn't find", "QUES_NOT_VALID");
        }
        return answerList;
    }

    private void numericQuestionValidator(Question question, QuestionData answer) {
        if (question.getLengthMin() > Integer.parseInt(answer.getAnswerList().get(0))
                || Integer.parseInt(answer.getAnswerList().get(0)) > question.getLengthMax()) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, "Given answer not valid for range", "RANGE_ERR");
        }
    }

    private void textQuestionValidator(Question question, QuestionData answer) {
        if (question.getLengthMin() > answer.getAnswerList().get(0).length()
                || answer.getAnswerList().get(0).length() > question.getLengthMax()) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, "Given answer not valid for range", "RANGE_ERR");
        }
    }

    private void multiQuestionValidator(Question question, QuestionData answer) {
        if (question.getLengthMin() > answer.getAnswerList().size()
                || answer.getAnswerList().size() > question.getLengthMax()) {
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, "Not valid answer size", "ANSWER_RANGE_ERR");
        }
    }

    private void generateAttachmentForQuestion(List<QuestionAnswerModel> questionAnswerModels, MeetingRequest meetingRequest) {
        try {
            if (!questionAnswerModels.isEmpty()) {
                meetingRequest.getAttachmentModels().add(new AttachmentModel(generatePdfBytes(questionAnswerModels), "QA.pdf", "application/pdf", 1L));
            }
        } catch (Exception e) {
            log.error("Attachment add error");
        }
    }

    private byte[] generatePdfBytes(List<QuestionAnswerModel> questionAnswerModels) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
        PdfWriter writer = new PdfWriter(os);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        for (QuestionAnswerModel aq : questionAnswerModels) {
            Paragraph paragraph = new Paragraph(aq.getQuestion()).setFont(bold);
            document.add(paragraph);
            com.itextpdf.layout.element.List list = new com.itextpdf.layout.element.List()
                    .setSymbolIndent(12)
                    .setListSymbol("\u2022")
                    .setFont(font);
            for (String answers : aq.getAnswer()) {
                list.add(new ListItem(answers));
            }
            document.add(list);
        }
        document.close();
        return os.toByteArray();
    }

}
