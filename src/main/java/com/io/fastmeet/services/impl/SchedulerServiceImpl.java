/*
 * @author : Oguz Kahraman
 * @since : 11.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services.impl;

import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.core.i18n.Translator;
import com.io.fastmeet.core.security.jwt.JWTService;
import com.io.fastmeet.entitites.Scheduler;
import com.io.fastmeet.entitites.User;
import com.io.fastmeet.mappers.SchedulerMapper;
import com.io.fastmeet.models.internals.SchedulerDetailsRequest;
import com.io.fastmeet.models.internals.SchedulerNameUpdateRequest;
import com.io.fastmeet.models.internals.SchedulerTime;
import com.io.fastmeet.models.responses.calendar.SchedulerResponse;
import com.io.fastmeet.repositories.SchedulerRepository;
import com.io.fastmeet.services.SchedulerService;
import com.io.fastmeet.utils.GeneralMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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

    @Autowired
    private SchedulerMapper schedulerMapper;

    @Override
    public SchedulerResponse createScheduler(String name, String token) {
        User user = jwtService.getUserFromToken(token);
        Scheduler scheduler = new Scheduler();
        scheduler.setTimeZone(user.getTimeZone());
        scheduler.setMon(defaultSchedulerTimeSet());
        scheduler.setThu(defaultSchedulerTimeSet());
        scheduler.setWed(defaultSchedulerTimeSet());
        scheduler.setTue(defaultSchedulerTimeSet());
        scheduler.setFri(defaultSchedulerTimeSet());
        scheduler.setName(name);
        scheduler.setUserId(user.getId());
        scheduler.setUnavailable(Collections.singletonList(LocalDate.now().toString()));
        schedulerRepository.save(scheduler);
        List<Scheduler> list = schedulerRepository.findByUserId(user.getId()).orElse(new ArrayList<>());
        return new SchedulerResponse(schedulerMapper.mapEntityListToModelList(list));
    }

    @Override
    public SchedulerResponse updateScheduler(SchedulerDetailsRequest request) {
        User user = jwtService.getUserFromToken(request.getToken());
        Scheduler scheduleObject = schedulerRepository.findByUserIdAndId(user.getId(), request.getSchedulerId()).orElseThrow(() ->
                new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageUtil.SCH_NOT_FOUND),
                        GeneralMessageUtil.USR_NOT_FOUND));
        schedulerRepository.save(schedulerMapper.mapUpdateModelsToEntity(request.getRequest(), scheduleObject));
        List<Scheduler> list = schedulerRepository.findByUserId(user.getId()).orElse(new ArrayList<>());
        return new SchedulerResponse(schedulerMapper.mapEntityListToModelList(list));
    }

    @Override
    public SchedulerResponse getUserSchedulers(String token) {
        User user = jwtService.getUserFromToken(token);
        List<Scheduler> list = schedulerRepository.findByUserId(user.getId()).orElse(new ArrayList<>());
        return new SchedulerResponse(schedulerMapper.mapEntityListToModelList(list));
    }

    @Override
    public void updateName(SchedulerNameUpdateRequest request) {
        User user = jwtService.getUserFromToken(request.getToken());
        schedulerRepository.changeSchedulerName(request.getName(), request.getSchedulerId(), user.getId());
    }

    private Set<SchedulerTime> defaultSchedulerTimeSet() {
        Set<SchedulerTime> times = new HashSet<>();
        times.add(new SchedulerTime("09:00", "12:00"));
        times.add(new SchedulerTime("13:00", "18:00"));
        return times;
    }

}
