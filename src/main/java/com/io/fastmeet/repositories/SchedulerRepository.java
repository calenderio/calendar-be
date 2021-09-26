/*
 * @author : Oguz Kahraman
 * @since : 11.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.repositories;

import com.io.fastmeet.entitites.Scheduler;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SchedulerRepository extends CrudRepository<Scheduler, Long> {

    Optional<List<Scheduler>> findByUserIdAndForCalendarIsFalse(Long userId);

    Optional<Scheduler> findByUserIdAndId(Long userId, Long id);

    @Query("update Scheduler s set s.name = :name where s.id = :id and s.userId = :userId")
    @Modifying
    int changeSchedulerName(String name, Long id, Long userId);

    @Query("delete from Scheduler s where s.id = :id and s.userId = :userId and s.forCalendar = true")
    @Modifying
    int deleteEventScheduler(Long id, Long userId);

}
