package com.io.collige.entitites;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private Long eventId;
    private Long userId;

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "invitation_id", nullable = false)
    private Invitation invitation;

    @Type(type = "list-array")
    private List<String> participants = new ArrayList<>();

    @Type(type = "list-array")
    private List<String> bcc = new ArrayList<>();

}