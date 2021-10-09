/*
 * @author : Oguz Kahraman
 * @since : 13.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.repositories;

import com.io.collige.entitites.Event;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends CrudRepository<Event, Long> {

    List<Event> findByUserId(Long userId);

    List<Event> findBySchedulerId(Long schedulerId);

    Optional<Event> findByUserIdAndId(Long userId, Long id);

    void deleteBySchedulerId(Long schedulerId);

}
