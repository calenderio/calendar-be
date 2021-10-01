/*
 * @author : Oguz Kahraman
 * @since : 11.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.services.impl;

import com.io.collige.constants.GeneralMessageConstants;
import com.io.collige.core.exception.CalendarAppException;
import com.io.collige.core.i18n.Translator;
import com.io.collige.core.security.jwt.JWTService;
import com.io.collige.entitites.Scheduler;
import com.io.collige.entitites.User;
import com.io.collige.models.internals.SchedulerDetailsRequest;
import com.io.collige.models.internals.SchedulerNameUpdateRequest;
import com.io.collige.models.internals.SchedulerTime;
import com.io.collige.repositories.SchedulerRepository;
import com.io.collige.services.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class SchedulerServiceImpl implements SchedulerService {

    @Autowired
    private SchedulerRepository schedulerRepository;

    @Autowired
    private JWTService jwtService;

    @Override
    public List<Scheduler> createScheduler(String name) {
        User user = jwtService.getLoggedUser();
        Scheduler scheduler = new Scheduler();
        scheduler.setTimeZone(user.getTimeZone());
        scheduler.setMon(defaultSchedulerTimeSet());
        scheduler.setThu(defaultSchedulerTimeSet());
        scheduler.setWed(defaultSchedulerTimeSet());
        scheduler.setTue(defaultSchedulerTimeSet());
        scheduler.setFri(defaultSchedulerTimeSet());
        scheduler.setName(name);
        scheduler.setUserId(user.getId());
        schedulerRepository.save(scheduler);
        return schedulerRepository.findByUserIdAndForCalendarIsFalse(user.getId());
    }

    @Override
    public List<Scheduler> updateScheduler(SchedulerDetailsRequest request) {
        User user = jwtService.getLoggedUser();
        Scheduler scheduleObject = schedulerRepository.findByUserIdAndId(user.getId(), request.getSchedulerId()).orElseThrow(() ->
                new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageConstants.SCH_NOT_FOUND),
                        GeneralMessageConstants.USR_NOT_FOUND));
        request.getScheduler().setId(scheduleObject.getId());
        request.getScheduler().setName(scheduleObject.getName());
        request.getScheduler().setUserId(user.getId());
        schedulerRepository.save(request.getScheduler());
        return schedulerRepository.findByUserIdAndForCalendarIsFalse(user.getId());
    }

    @Override
    public List<Scheduler> getUserSchedulers() {
        User user = jwtService.getLoggedUser();
        return schedulerRepository.findByUserIdAndForCalendarIsFalse(user.getId());
    }

    @Override
    public void updateName(SchedulerNameUpdateRequest request) {
        User user = jwtService.getLoggedUser();
        schedulerRepository.changeSchedulerName(request.getName(), request.getSchedulerId(), user.getId());
    }

    @Override
    public Scheduler getUserSchedulerById(Long id) {
        User user = jwtService.getLoggedUser();
        return schedulerRepository.findByUserIdAndId(user.getId(), id).orElseThrow(() ->
                new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageConstants.SCH_NOT_FOUND),
                        GeneralMessageConstants.USR_NOT_FOUND));
    }

    @Override
    public Scheduler saveCalendarTypeScheduler(Scheduler scheduler) {
        User user = jwtService.getLoggedUser();
        scheduler.setForCalendar(true);
        scheduler.setUserId(user.getId());
        return schedulerRepository.save(scheduler);
    }

    @Override
    public void deleteEventScheduler(Long schedulerId) {
        User user = jwtService.getLoggedUser();
        schedulerRepository.deleteEventScheduler(schedulerId, user.getId());
    }

    private Set<SchedulerTime> defaultSchedulerTimeSet() {
        Set<SchedulerTime> times = new HashSet<>();
        times.add(new SchedulerTime("09:00", "12:00"));
        times.add(new SchedulerTime("13:00", "18:00"));
        return times;
    }

}
