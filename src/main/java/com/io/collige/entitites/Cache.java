/*
 * @author : Oguz Kahraman
 * @since : 16.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.entitites;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Table(name = "caches")
@NoArgsConstructor
@Entity
public class Cache {

    @Id
    private String name;
    private String value;
    private LocalDateTime updateDate;

}
