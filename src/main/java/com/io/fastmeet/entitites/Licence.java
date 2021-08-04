package com.io.fastmeet.entitites;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Data
@Table (name ="licences")
public class Licence extends BaseEntity{

    @OneToOne
    private User userId;
    private int duration;
    private char type ;
    private LocalDateTime activationDate;
    private LocalDateTime endDate;
    private String licenseKey;
    private long companyId;
}
