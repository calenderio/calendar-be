package com.io.fastmeet.entitites;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.io.fastmeet.enums.LicenceTypes;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Data
@Table(name = "licences")
public class Licence extends BaseEntity {

    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL)
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LicenceTypes licenceType;
    private LocalDateTime activationDate;
    private LocalDateTime endDate;
    private String licenceKey;
    private Long companyId;
}
