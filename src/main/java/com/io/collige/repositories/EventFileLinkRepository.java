/*
 * @author : Oguz Kahraman
 * @since : 8.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.repositories;

import com.io.collige.entitites.EventFileLink;
import com.io.collige.entitites.EventFileLinkId;
import org.springframework.data.repository.CrudRepository;

public interface EventFileLinkRepository extends CrudRepository<EventFileLink, EventFileLinkId> {

    void deleteByIdEventId(Long eventId);

}
