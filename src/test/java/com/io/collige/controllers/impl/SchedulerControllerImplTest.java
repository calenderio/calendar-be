/*
 * @author : Oguz Kahraman
 * @since : 27.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.controllers.impl;

import com.io.collige.entitites.Scheduler;
import com.io.collige.mappers.SchedulerMapper;
import com.io.collige.models.internals.SchedulerDetails;
import com.io.collige.models.internals.SchedulerNameUpdateRequest;
import com.io.collige.models.requests.scheduler.SchedulerUpdateRequest;
import com.io.collige.models.responses.scheduler.SchedulerResponse;
import com.io.collige.services.SchedulerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchedulerControllerImplTest {

    @Mock
    private SchedulerService schedulerService;

    @Mock
    private SchedulerMapper mapper;

    @InjectMocks
    private SchedulerControllerImpl schedulerController;

    @Test
    void createScheduler() {
        when(schedulerService.createScheduler("example")).thenReturn(new ArrayList<>());
        when(mapper.mapEntityListToModelList(any())).thenReturn(new HashSet<>());
        ResponseEntity<SchedulerResponse> responseEntity = schedulerController.createScheduler("example");
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void getSchedulers() {
        when(schedulerService.getUserSchedulers()).thenReturn(new ArrayList<>());
        when(mapper.mapEntityListToModelList(any())).thenReturn(new HashSet<>());
        ResponseEntity<SchedulerResponse> responseEntity = schedulerController.getSchedulers();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void changeName() {
        ResponseEntity<Void> responseEntity = schedulerController.changeName("Name", 1L);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(schedulerService, times(1)).updateName(new SchedulerNameUpdateRequest("Name", 1L));
    }

    @Test
    void updateScheduler() {
        SchedulerUpdateRequest request = new SchedulerUpdateRequest();
        request.setTimeZone("UTC");
        request.setSchedule(new SchedulerDetails());
        when(mapper.mapEntityListToModelList(any())).thenReturn(new HashSet<>());
        when(mapper.mapDetailsToEntity(any(), anyString())).thenReturn(new Scheduler());
        ResponseEntity<SchedulerResponse> responseEntity = schedulerController.updateScheduler(request, 1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}