package com.io.fastmeet.models.requests.meet;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fortuna.ical4j.model.property.Method;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRequest {

    @Schema(description = "Request.Method of Email" , example = "Method.Request", required = true)
    @NotNull
    private Method method;

    @Schema (description = "Title of Meeting" , example = "PoC meeting" , required = true)
    @NotEmpty
    private String meetingTitle;

    @ArraySchema(schema = @Schema(description = "Participants of the Meeting", example = "test@test.com"))
    private List<@Email String> participants;

    @Schema(description = "location of Meeting " , example = "Room 123")
    @NotEmpty
    private String location;

    @Schema(description = "Startdate of a Meeting" , example = "19.01.2020 - 19:30", required = true)
    @NotNull
    private LocalDateTime startDate;

    @Schema(description = "Enddate of a Meeting" , example = "19.01.2020 - 19:30", required = true)
    @NotNull
    private LocalDateTime endDate;

    @Schema(description = "Duration of Meeting" , example = "30 Min")
    @NotNull
    private long duration;

    @Email
    @Schema(description = "Email of organizer" , example = "abc@abc.com", required = true)
    @NotNull
    private String organizer;

    @Schema(description = "uid of meeting")
    @NotNull
    private String icsUid;

    @Schema(description = "Timezone of meeting", example = "UTC +3")
    @NotEmpty
    private String timeZone;

    @Schema(description = "Description of Meeting" , example = "This meeting occures by changes in bussines logic")
    private String description;

}
