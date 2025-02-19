/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.entitites;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.io.collige.enums.AppProviderType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
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
    private AppProviderType type;
    private String refreshToken;
    private String socialMail;
    @Column(name = "token")
    private String accessToken;
    private LocalDateTime expireDate;
    @JsonBackReference
    @ManyToMany(mappedBy = "calendars", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();
}
