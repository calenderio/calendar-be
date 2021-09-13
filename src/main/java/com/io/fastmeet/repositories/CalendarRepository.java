/*
 * @author : Oguz Kahraman
 * @since : 13.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.repositories;

import com.io.fastmeet.entitites.Calendar;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CalendarRepository extends CrudRepository<Calendar, Long> {

    List<Calendar> findByUserId(Long userId);

}
