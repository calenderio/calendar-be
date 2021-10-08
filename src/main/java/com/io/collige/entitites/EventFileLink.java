/*
 * @author : Oguz Kahraman
 * @since : 8.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.entitites;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Table(name = "event_file_links")
@NoArgsConstructor
@Entity
public class EventFileLink {

    @EmbeddedId
    private EventFileLinkId id;

}
