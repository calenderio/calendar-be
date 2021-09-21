/*
 * @author : Oguz Kahraman
 * @since : 7.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.controllers.impl;

import com.io.fastmeet.controllers.MeetController;
import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.models.internals.AttachmentModel;
import com.io.fastmeet.models.internals.MeetInvitationDetailRequest;
import com.io.fastmeet.models.requests.meet.MeetInvitationRequest;
import com.io.fastmeet.services.EventService;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MeetControllerImpl implements MeetController {

    @Autowired
    private EventService eventService;

    @Value("${spring.allowed.file.extensions}")
    private List<String> allowedFileTypes;

    @Override
    public ResponseEntity<Void> sendMeetingInvite(MeetInvitationRequest request, List<MultipartFile> files) {
        Tika tika = new Tika();
        List<AttachmentModel> modelList = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                try {
                    String type = tika.detect(file.getBytes());
                    if (!allowedFileTypes.contains(type)) {
                        throw new CalendarAppException(HttpStatus.NOT_ACCEPTABLE, "Not allowed file type", "NOT_ALLOWED_FILE");
                    }
                    modelList.add(new AttachmentModel(file.getBytes(), file.getOriginalFilename(), type));
                } catch (Exception e) {
                    throw new CalendarAppException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception", "EXCEPTION");
                }
            }
        }
        eventService.sendEventInvitation(new MeetInvitationDetailRequest(request.getUserMail(), request.getName(), request.getEventId(), modelList,
                request.getCcUsers(), request.getBccUsers()));
        return ResponseEntity.noContent().build();
    }
}
