package com.io.fastmeet.entitites;

import lombok.Builder;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Table(name = "calendar_meeting")
@Entity
public class Meeting extends BaseEntity {

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


    @ManyToMany(cascade =
            {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "calendar_meeting",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "calendar_id"))
    private Set<Calendar> calendarSet = new HashSet<>();

}