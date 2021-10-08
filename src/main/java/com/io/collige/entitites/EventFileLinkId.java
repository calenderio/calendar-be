/*
 * @author : Oguz Kahraman
 * @since : 8.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.entitites;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class EventFileLinkId implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long userId;
    private Long eventId;
    private Long fileId;

}
