/*
 * @author : Oguz Kahraman
 * @since : 8.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.internals;

import com.io.collige.entitites.Event;

import java.util.Set;

public class UpdateEventRequest extends CreateEventRequest {

    private Long eventId;

    public UpdateEventRequest(Event event, Set<Long> fileLinks, Long eventId) {
        super(event, fileLinks);
        this.eventId = eventId;
    }

    public Long getEventId() {
        return eventId;
    }
}
