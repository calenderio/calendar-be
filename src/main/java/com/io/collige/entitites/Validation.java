/*
 * @author : Oguz Kahraman
 * @since : 28.02.2021
 *
 * Copyright - Collige Java API
 **/
package com.io.collige.entitites;

import com.io.collige.enums.ValidationType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "validations")
@Data
public class Validation {

    @Id
    @SequenceGenerator(name = "validations_id_seq", sequenceName = "validations_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "validations_id_seq")
    private Long id;
    private Long userId;
    private String mail;
    private String code;
    @Enumerated(EnumType.STRING)
    private ValidationType type;
    private LocalDateTime date = LocalDateTime.now();

}
