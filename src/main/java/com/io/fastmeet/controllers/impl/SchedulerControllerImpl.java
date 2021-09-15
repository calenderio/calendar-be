/*
 * @author : Oguz Kahraman
 * @since : 8.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.controllers.impl;

import com.io.fastmeet.controllers.SchedulerController;
import com.io.fastmeet.mappers.SchedulerMapper;
import com.io.fastmeet.models.internals.SchedulerDetailsRequest;
import com.io.fastmeet.models.internals.SchedulerNameUpdateRequest;
import com.io.fastmeet.models.requests.scheduler.SchedulerUpdateRequest;
import com.io.fastmeet.models.responses.scheduler.SchedulerResponse;
import com.io.fastmeet.services.SchedulerService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class SchedulerControllerImpl implements SchedulerController {

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private SchedulerMapper mapper;

    @Override
    public ResponseEntity<SchedulerResponse> createScheduler(@Length(max = 50, min = 1) String name) {
        SchedulerResponse schedulerResponse = new SchedulerResponse(mapper.mapEntityListToModelList(schedulerService.createScheduler(name)));
        return ResponseEntity.status(HttpStatus.CREATED).body(schedulerResponse);
    }

    @Override
    public ResponseEntity<SchedulerResponse> getSchedulers() {
        SchedulerResponse schedulerResponse = new SchedulerResponse(mapper.mapEntityListToModelList(schedulerService.getUserSchedulers()));
        return ResponseEntity.ok(schedulerResponse);
    }

    @Override
    public ResponseEntity<Void> changeName(String name, Long schedulerId) {
        schedulerService.updateName(new SchedulerNameUpdateRequest(name, schedulerId));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<SchedulerResponse> updateScheduler(@Valid SchedulerUpdateRequest request, Long schedulerId) {
        SchedulerDetailsRequest schedulerDetailsRequest = new SchedulerDetailsRequest();
        schedulerDetailsRequest.setSchedulerId(schedulerId);
        schedulerDetailsRequest.setScheduler(mapper.mapDetailsToEntity(request.getSchedule(), request.getTimeZone()));
        SchedulerResponse schedulerResponse = new SchedulerResponse(mapper.mapEntityListToModelList(schedulerService.updateScheduler(schedulerDetailsRequest)));
        return ResponseEntity.ok(schedulerResponse);
    }

}
