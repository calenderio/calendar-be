/*
 * @author : Oguz Kahraman
 * @since : 10.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.requests.events;

import lombok.Data;

@Data
public class EventUpdateRequest {

    private EventCreateRequest details;
    private Long eventId;

}
