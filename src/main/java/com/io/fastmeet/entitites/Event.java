/*
 * @author : Oguz Kahraman
 * @since : 13.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.entitites;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.io.fastmeet.enums.DurationType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;

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
    private LocalDate startDate;
    private LocalDate endDate;
    private int duration;
    @Enumerated(EnumType.STRING)
    private DurationType durationType;

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "scheduler_id", referencedColumnName = "id")
    private Scheduler scheduler;

    private transient Long preDefinedSchedulerId;

}
