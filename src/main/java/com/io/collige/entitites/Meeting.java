package com.io.collige.entitites;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.io.collige.models.internals.FileDetails;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "meetings")
@NoArgsConstructor
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
@Entity
public class Meeting extends BaseEntity {

    private UUID uuid;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;
    private String title;
    private String timeZone;
    private String organizer;
    private String location;
    private Integer sequence;
    private Long userId;

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "invitation_id", nullable = false)
    private Invitation invitation;

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Type(type = "list-array")
    private List<String> participants = new ArrayList<>();

    @Type(type = "list-array")
    private List<String> bcc = new ArrayList<>();

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private Set<FileDetails> fileLinks = new HashSet<>();

}