/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.repositories;

import com.io.fastmeet.entitites.LinkedCalendar;
import org.springframework.data.repository.CrudRepository;

public interface LinkedCalendarRepository extends CrudRepository<LinkedCalendar, Long> {
}
