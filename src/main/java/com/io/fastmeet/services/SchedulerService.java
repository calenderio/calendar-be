/*
 * @author : Oguz Kahraman
 * @since : 11.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services;

import com.io.fastmeet.entitites.Scheduler;
import com.io.fastmeet.models.internals.SchedulerDetailsRequest;
import com.io.fastmeet.models.internals.SchedulerNameUpdateRequest;

import java.util.List;

public interface SchedulerService {
    List<Scheduler> createScheduler(String name);

    List<Scheduler> updateScheduler(SchedulerDetailsRequest request);

    List<Scheduler> getUserSchedulers();

    void updateName(SchedulerNameUpdateRequest request);

    Scheduler getUserSchedulerById(Long id, Long userId);

    Scheduler saveCalendarTypeScheduler(Scheduler scheduler, Long userId);
}
