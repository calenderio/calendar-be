/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.entitites;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.io.fastmeet.enums.CalendarProviderType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "linked_calendars")
@NoArgsConstructor
@Entity
public class LinkedCalendar extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private CalendarProviderType type;
    private String refreshToken;
    private String socialMail;
    @Column(name = "token")
    private String accessToken;
    private LocalDateTime expireDate;
    @JsonBackReference
    @ManyToMany(mappedBy = "calendars", fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();
}
