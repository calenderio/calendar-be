/*
 * @author : Oguz Kahraman
 * @since : 13.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.entitites;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.io.collige.enums.DurationType;
import com.io.collige.enums.EventLocation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "events")
@NoArgsConstructor
@Entity
public class Event extends BaseEntity {

    private Long userId;
    private String name;
    private String description;
    private String timeZone;
    private boolean fileRequired;
    private String fileDescription;
    private LocalDate startDate;
    private LocalDate endDate;
    private int duration;
    @Enumerated(EnumType.STRING)
    private DurationType durationType;
    @Enumerated(EnumType.STRING)
    private EventLocation location;

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "scheduler_id", referencedColumnName = "id")
    private Scheduler scheduler;

    @JsonBackReference
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Question> questions;

    @JsonBackReference
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Answer> answers;

    private transient Long preDefinedSchedulerId;

}
