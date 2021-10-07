/*
 * @author : Oguz Kahraman
 * @since : 6.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.entitites;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "file_links")
@NoArgsConstructor
@Entity
public class FileLink extends BaseEntity {

    private Long userId;
    private String name;
    @Column(name = "file_link")
    private String link;
    @Column(name = "file_type")
    private String type;
    @Column(name = "file_size")
    private Long size;

}
