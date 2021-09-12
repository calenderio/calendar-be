/*
 * @author : Oguz Kahraman
 * @since : 11.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services;

import com.io.fastmeet.models.internals.SchedulerDetailsRequest;
import com.io.fastmeet.models.internals.SchedulerNameUpdateRequest;
import com.io.fastmeet.models.responses.calendar.SchedulerResponse;

public interface SchedulerService {
    SchedulerResponse createScheduler(String name, String token);

    SchedulerResponse updateScheduler(SchedulerDetailsRequest request);

    SchedulerResponse getUserSchedulers(String token);

    void updateName(SchedulerNameUpdateRequest request);
}
