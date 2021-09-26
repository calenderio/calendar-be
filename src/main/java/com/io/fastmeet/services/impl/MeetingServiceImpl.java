package com.io.fastmeet.services.impl;


import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.core.i18n.Translator;
import com.io.fastmeet.entitites.Answer;
import com.io.fastmeet.entitites.Event;
import com.io.fastmeet.entitites.Invitation;
import com.io.fastmeet.entitites.Meeting;
import com.io.fastmeet.entitites.Question;
import com.io.fastmeet.enums.DurationType;
import com.io.fastmeet.mappers.MeetingMapper;
import com.io.fastmeet.models.internals.AttachmentModel;
import com.io.fastmeet.models.internals.AvailableDatesDetails;
import com.io.fastmeet.models.internals.GenericMailRequest;
import com.io.fastmeet.models.internals.QuestionAnswerModel;
import com.io.fastmeet.models.internals.ScheduleMeetingDetails;
import com.io.fastmeet.models.requests.calendar.QuestionData;
import com.io.fastmeet.models.requests.calendar.ScheduleMeetingRequest;
import com.io.fastmeet.models.requests.meet.MeetingRequest;
import com.io.fastmeet.repositories.InvitationRepository;
import com.io.fastmeet.repositories.MeetingRepository;
import com.io.fastmeet.services.CalendarService;
import com.io.fastmeet.services.IcsService;
import com.io.fastmeet.services.MailService;
import com.io.fastmeet.services.MeetingService;
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
    private MeetingMapper meetingMapper;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private InvitationRepository invitationRepository;


    @Override
    public void validateAndScheduleMeeting(ScheduleMeetingDetails details) {
        ScheduleMeetingRequest request = details.getRequest();
        AvailableDatesDetails dateDetails = calendarService.getAvailableDates(request.getDate(), details.getInvitationId(), request.getTimeZone());
        Map<LocalDate, Set<LocalTime>> availableDates = dateDetails.getAvailableDates();
        Invitation invitation = dateDetails.getInvitation();
        checkDates(request, availableDates, invitation.getEvent());
        List<QuestionAnswerModel> questionAnswerModels = generateQAModel(request, invitation);

        MeetingRequest meetingRequest = new MeetingRequest();
        if (invitation.getEvent().isFileRequired()) {
            if (details.getModels() == null || details.getModels().isEmpty()) {
                throw new CalendarAppException(HttpStatus.BAD_REQUEST, "File can not be null", "NULL_FILE");
            } else {
                meetingRequest.getAttachmentModels().addAll(details.getModels());
            }
        }
        meetingRequest.setTitle(invitation.getName() + " " + invitation.getTitle());
        meetingRequest.setLocation(invitation.getEvent().getLocation().name());
        meetingRequest.setUuid(UUID.randomUUID());
        meetingRequest.setTimeZone(request.getTimeZone());
        meetingRequest.setDescription(invitation.getDescription());
        meetingRequest.setOrganizer("info@collige.io");
        meetingRequest.getParticipants().add(invitation.getUser().getEmail());
        if (invitation.getCcList() != null) {
            meetingRequest.getParticipants().addAll(invitation.getCcList());
        }
        meetingRequest.getParticipants().add(invitation.getUserEmail());

        setDates(request, invitation, meetingRequest);
        meetingRequest.setSequence(1);
        generateAttachment(questionAnswerModels, meetingRequest);
        invitation.setScheduled(true);
        Meeting meeting = meetingMapper.mapToMeeting(meetingRequest);
        sendInvitationMailAndSaveMeeting(details.getModels(), meetingRequest, invitation, meeting);
    }

    @Override
    public void updateMeetingRequest(ScheduleMeetingDetails details) {
        AvailableDatesDetails dateDetails = calendarService.getAvailableDates(details.getRequest().getDate(), details.getInvitationId(),
                details.getRequest().getTimeZone());
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
        if (invitation.getEvent().isFileRequired()) {
            if (details.getModels() == null || details.getModels().isEmpty()) {
                throw new CalendarAppException(HttpStatus.BAD_REQUEST, "File can not be null", "NULL_FILE");
            } else {
                meetingRequest.getAttachmentModels().addAll(details.getModels());
            }
        }
        meetingRequest.setSequence(meeting.getSequence() + 1);
        setDates(details.getRequest(), invitation, meetingRequest);
        generateAttachment(questionAnswerModels, meetingRequest);
        Meeting newOne = meetingMapper.mapToMeeting(meetingRequest);
        newOne.setId(meeting.getId());
        sendInvitationMailAndSaveMeeting(details.getModels(), meetingRequest, invitation, meeting);
    }

    @Override
    public void deleteMeetingRequest(ScheduleMeetingDetails details) {
        Invitation invitation = invitationRepository.findByInvitationIdAndScheduledIsTrue(details.getInvitationId()).orElseThrow(() ->
                new CalendarAppException(HttpStatus.BAD_REQUEST, "Invitation couldn't scheduled", "NOT_SCHEDULED"));

        Meeting meeting = meetingRepository.findByInvitationId(invitation.getId()).orElseThrow(() ->
                new CalendarAppException(HttpStatus.BAD_REQUEST, "Meeting couldn't find", "MEET_NOT_VALID"));

        MeetingRequest meetingRequest = meetingMapper.mapEntityToRequest(meeting);
        meetingRequest.setSequence(meeting.getSequence() + 1);
        meetingRequest.setMethod(Method.CANCEL);
        Meeting newOne = meetingMapper.mapToMeeting(meetingRequest);
        newOne.setId(meeting.getId());
        invitation.setScheduled(false);
        invitation.getAnswers().clear();
        meetingRequest.setOrganizerName(invitation.getName());
        meetingRequest.setOrganizerMail(invitation.getUserEmail());
        sendInvitationMailAndSaveMeeting(details.getModels(), meetingRequest, invitation, meeting);
        meetingRepository.delete(meeting);
    }

    private List<QuestionAnswerModel> generateQAModel(ScheduleMeetingRequest request, Invitation invitation) {
        List<QuestionAnswerModel> questionAnswerModels = new ArrayList<>();
        if (invitation.getEvent().getQuestions() != null && !invitation.getEvent().getQuestions().isEmpty()) {
            Map<Long, Question> questionMap = invitation.getEvent().getQuestions().stream().collect(Collectors.toMap(Question::getId, Function.identity()));
            List<Answer> answers = validateAnswers(questionMap, request.getAnswers(), invitation, questionAnswerModels);
            invitation.setAnswers(answers);
        } else {
            invitation.getAnswers().clear();
        }
        return questionAnswerModels;
    }

    private void checkDates(ScheduleMeetingRequest request, Map<LocalDate, Set<LocalTime>> availableDates, Event event) {
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

    private void setDates(ScheduleMeetingRequest request, Invitation invitation, MeetingRequest meetingRequest) {
        meetingRequest.setOrganizerName(invitation.getName());
        meetingRequest.setOrganizerMail(invitation.getUserEmail());
        meetingRequest.setStartDate(ZonedDateTime.of(request.getDate(), request.getTime(), ZoneId.of(request.getTimeZone())).toLocalDateTime());
        int duration = DurationType.HOUR.equals(invitation.getEvent().getDurationType()) ?
                60 * invitation.getEvent().getDuration() : invitation.getEvent().getDuration();
        meetingRequest.setEndDate(ZonedDateTime.of(request.getDate(), request.getTime(),
                ZoneId.of(request.getTimeZone())).toLocalDateTime().plusMinutes(duration));
        meetingRequest.setMethod(Method.REQUEST);
    }

    private void sendInvitationMailAndSaveMeeting(List<AttachmentModel> files, MeetingRequest request, Invitation invitation, Meeting meeting) {
        GenericMailRequest toInvitationMail = meetingMapper.request(request);
        try {
            toInvitationMail.setMeetingDetails(icsService.writeIcsFileToByteArray(request));
            if (invitation.getBccList() != null) {
                toInvitationMail.setBcc(new HashSet<>(invitation.getBccList()));
            }
            toInvitationMail.getAttachments().addAll(files);
            toInvitationMail.setLanguage(Translator.getLanguage());
            toInvitationMail.setName(invitation.getName());
            meeting.setInvitation(invitation);
            meeting.setDescription(invitation.getDescription());
            meeting.setTitle(invitation.getTitle());
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

    private void generateAttachment(List<QuestionAnswerModel> questionAnswerModels, MeetingRequest meetingRequest) {
        try {
            if (!questionAnswerModels.isEmpty()) {
                meetingRequest.getAttachmentModels().add(new AttachmentModel(generatePdfBytes(questionAnswerModels), "QA.pdf", "application/pdf"));
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
