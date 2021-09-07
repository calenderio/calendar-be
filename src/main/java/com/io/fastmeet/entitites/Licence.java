package com.io.fastmeet.entitites;

import com.io.fastmeet.enums.LicenceTypes;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Data
@Table(name = "licences")
public class Licence extends BaseEntity {

    @OneToOne
    private User userId;
    private LicenceTypes licenceType ;
    private LocalDateTime activationDate;
    private LocalDateTime endDate;
    private String licenseKey;
    private long companyId;
}
