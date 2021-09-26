package com.io.fastmeet.models.requests.meet;

import com.io.fastmeet.models.internals.AttachmentModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fortuna.ical4j.model.property.Method;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRequest {

    private Method method;
    private String title;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UUID uuid;
    private String timeZone;
    private String description;
    private String organizer;
    private String organizerName;
    private String organizerMail;
    private Integer sequence;
    private List<String> participants = new ArrayList<>();
    private List<AttachmentModel> attachmentModels = new ArrayList<>();
    private List<String> bcc = new ArrayList<>();

}
