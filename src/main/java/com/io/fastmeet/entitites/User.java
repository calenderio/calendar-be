/*
 * @author : Oguz Kahraman
 * @since : 3.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.entitites;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "users")
@NoArgsConstructor
@Entity
public class User extends BaseEntity {

    private String name;
    private String email;
    private String picture;
    private String password;
    private String timeZone = "UTC";
    private Boolean isCompany;
    private Boolean verified = Boolean.FALSE;
    private Long companyId;

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL)
    private Licence licence;


    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_calendars",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "calendar_id")}
    )
    private Set<LinkedCalendar> calendars = new HashSet<>();

    @OneToMany
    private Set<Todo> todos = new HashSet<>();

}
