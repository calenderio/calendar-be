/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.repositories;

import com.io.collige.entitites.LinkedCalendar;
import com.io.collige.enums.AppProviderType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LinkedCalendarRepository extends CrudRepository<LinkedCalendar, Long> {

    Optional<LinkedCalendar> findBySocialMailAndType(String mail, AppProviderType type);

}
