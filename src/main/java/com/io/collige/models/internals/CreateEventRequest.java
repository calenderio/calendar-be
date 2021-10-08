/*
 * @author : Oguz Kahraman
 * @since : 8.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.internals;

import com.io.collige.entitites.Event;
import lombok.Data;

import java.util.Set;

@Data
public class CreateEventRequest {

    private Event event;
    private Set<Long> fileLinks;

    public CreateEventRequest(Event event, Set<Long> fileLinks) {
        this.event = event;
        this.fileLinks = fileLinks;
    }

}
