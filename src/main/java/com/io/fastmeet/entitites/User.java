/*
 * @author : Oguz Kahraman
 * @since : 3.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.entitites;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "users")
@NoArgsConstructor
@Entity
public class User extends BaseEntity {

    private String name;
    private String email;
    private String password;
    private Boolean isCompany;
    private Long companyId;
    private Long licenceId;

}
