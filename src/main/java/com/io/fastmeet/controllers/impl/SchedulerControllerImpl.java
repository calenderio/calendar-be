/*
 * @author : Oguz Kahraman
 * @since : 8.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.controllers.impl;

import com.io.fastmeet.controllers.SchedulerController;
import com.io.fastmeet.models.internals.SchedulerNameUpdateRequest;
import com.io.fastmeet.models.requests.scheduler.SchedulerUpdateRequest;
import com.io.fastmeet.models.responses.calendar.SchedulerResponse;
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

    @Override
    public ResponseEntity<SchedulerResponse> createScheduler(@Length(max = 50, min = 1) String name, String token) {
        return ResponseEntity.status(HttpStatus.CREATED).body(schedulerService.createScheduler(name, token));
    }

    @Override
    public ResponseEntity<SchedulerResponse> getSchedulers(String token) {
        return ResponseEntity.ok(schedulerService.getUserSchedulers(token));
    }

    @Override
    public ResponseEntity<Void> changeName(String name, Long schedulerId, String token) {
        schedulerService.updateName(new SchedulerNameUpdateRequest(name, schedulerId, token));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> updateScheduler(@Valid SchedulerUpdateRequest request, Long schedulerId, String token) {
        return null;
    }

}
