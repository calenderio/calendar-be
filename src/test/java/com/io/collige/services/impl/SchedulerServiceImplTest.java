/*
 * @author : Oguz Kahraman
 * @since : 27.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.io.collige.core.security.jwt.JWTService;
import com.io.collige.entitites.Scheduler;
import com.io.collige.entitites.User;
import com.io.collige.models.internals.scheduler.SchedulerDetailsRequest;
import com.io.collige.models.internals.scheduler.SchedulerNameUpdateRequest;
import com.io.collige.repositories.SchedulerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceImplTest {

    @Mock
    private SchedulerRepository schedulerRepository;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private SchedulerServiceImpl schedulerService;

    @Test
    void createScheduler() {
        User user = new User();
        user.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(schedulerRepository.findByUserIdAndForCalendarIsFalse(1L)).thenReturn(new ArrayList<>());
        schedulerService.createScheduler("Example");
        verify(schedulerRepository, times(1)).save(any());
        verify(schedulerRepository, times(1)).findByUserIdAndForCalendarIsFalse(1L);
    }

    @Test
    void updateScheduler() {
        SchedulerDetailsRequest request = new SchedulerDetailsRequest();
        Scheduler scheduler = new Scheduler();
        request.setSchedulerId(1L);
        request.setScheduler(scheduler);
        User user = new User();
        user.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(schedulerRepository.findByUserIdAndForCalendarIsFalse(1L)).thenReturn(new ArrayList<>());
        when(schedulerRepository.findByUserIdAndId(1L, 1L)).thenReturn(Optional.of(new Scheduler()));
        schedulerService.updateScheduler(request);
        verify(schedulerRepository, times(1)).save(any());
        verify(schedulerRepository, times(1)).findByUserIdAndForCalendarIsFalse(1L);
    }

    @Test
    void getUserSchedulers() {
        User user = new User();
        user.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(schedulerRepository.findByUserIdAndForCalendarIsFalse(1L)).thenReturn(new ArrayList<>());
        schedulerService.getUserSchedulers();
        verify(schedulerRepository, times(1)).findByUserIdAndForCalendarIsFalse(1L);
    }

    @Test
    void updateName() {
        User user = new User();
        user.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        SchedulerNameUpdateRequest request = new SchedulerNameUpdateRequest("Example", 1L);
        schedulerService.updateName(request);
        verify(schedulerRepository, times(1)).changeSchedulerName("Example", 1L, 1L);
    }

    @Test
    void getUserSchedulerById() {
        User user = new User();
        user.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        when(schedulerRepository.findByUserIdAndId(1L, 1L)).thenReturn(Optional.of(new Scheduler()));
        schedulerService.getUserSchedulerById(1L);
        verify(schedulerRepository, times(1)).findByUserIdAndId(1L, 1L);
    }

    @Test
    void saveCalendarTypeScheduler() {
        Scheduler scheduler = new Scheduler();
        scheduler.setId(1L);
        User user = new User();
        user.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        schedulerService.saveCalendarTypeScheduler(scheduler);
        verify(schedulerRepository, times(1)).save(any());
    }

    @Test
    void deleteEventScheduler() {
        User user = new User();
        user.setId(1L);
        when(jwtService.getLoggedUser()).thenReturn(user);
        schedulerService.deleteEventScheduler(1L);
        verify(schedulerRepository, times(1)).deleteEventScheduler(1L, 1L);
    }

    @Test
    void deleteScheduler() {
        schedulerService.deleteScheduler(1L);
        verify(schedulerRepository, times(1)).deleteById(1L);
    }

}