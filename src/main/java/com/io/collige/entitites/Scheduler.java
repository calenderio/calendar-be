/*
 * @author : Oguz Kahraman
 * @since : 11.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.entitites;

import com.io.collige.models.internals.event.AdditionalTime;
import com.io.collige.models.internals.scheduler.SchedulerTime;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "schedulers")
@Data
@NoArgsConstructor
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
@TypeDef(name = "json", typeClass = JsonType.class)
@SuppressWarnings("java:S1948")
public class Scheduler extends BaseEntity {

    private Long userId;
    private String name;
    private String timeZone;
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private Set<SchedulerTime> mon = new HashSet<>();
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private Set<SchedulerTime> tue = new HashSet<>();
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private Set<SchedulerTime> wed = new HashSet<>();
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private Set<SchedulerTime> thu = new HashSet<>();
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private Set<SchedulerTime> fri = new HashSet<>();
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private Set<SchedulerTime> sat = new HashSet<>();
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private Set<SchedulerTime> sun = new HashSet<>();
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private Set<AdditionalTime> additionalTime = new HashSet<>();
    @Type(type = "list-array")
    private List<String> unavailable = new ArrayList<>();
    private boolean forCalendar = Boolean.FALSE;


}
