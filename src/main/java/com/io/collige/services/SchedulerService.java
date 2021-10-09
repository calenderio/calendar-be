/*
 * @author : Oguz Kahraman
 * @since : 11.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services;

import com.io.collige.entitites.Scheduler;
import com.io.collige.models.internals.SchedulerDetailsRequest;
import com.io.collige.models.internals.SchedulerNameUpdateRequest;

import java.util.List;

public interface SchedulerService {
    List<Scheduler> createScheduler(String name);

    List<Scheduler> updateScheduler(SchedulerDetailsRequest request);

    List<Scheduler> getUserSchedulers();

    void updateName(SchedulerNameUpdateRequest request);

    Scheduler getUserSchedulerById(Long id);

    Scheduler saveCalendarTypeScheduler(Scheduler scheduler);

    void deleteEventScheduler(Long schedulerId);

    void deleteScheduler(Long schedulerId);
}
