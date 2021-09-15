package com.io.fastmeet.entitites;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "calendar")
public class Calendar extends BaseEntity {

    @OneToOne(mappedBy = "calendar")
    private User userId;

    @NotNull
    private String name;

    @ManyToMany(mappedBy = "calendarSet",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Meeting> meetingSet;
}
