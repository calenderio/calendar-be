package com.io.fastmeet.entitites;

import javax.persistence.ElementCollection;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class Meeting {

    @NotBlank
    private UUID icsuid;

    @Email
    private String organizer;

    private String location;

    @NotNull
    private String meetingTitle;

    @NotNull
    private Date startDate;

    private Date endDate;

    @NotNull
    private long duration;

    private String description;

    @NotNull
    private String timeZoneRegion;

    @ElementCollection
    private List<String> participants;


}