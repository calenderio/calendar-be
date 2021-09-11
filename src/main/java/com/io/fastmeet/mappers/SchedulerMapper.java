/*
 * @author : Oguz Kahraman
 * @since : 11.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.mappers;

import com.io.fastmeet.entitites.Scheduler;
import com.io.fastmeet.models.internals.SchedulerObject;
import com.io.fastmeet.models.requests.scheduler.SchedulerUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface SchedulerMapper {

    Set<SchedulerObject> mapEntityListToModelList(List<Scheduler> schedulers);

    @Mapping(target = "schedule.mon", source = "mon")
    @Mapping(target = "schedule.tue", source = "tue")
    @Mapping(target = "schedule.wed", source = "wed")
    @Mapping(target = "schedule.thu", source = "thu")
    @Mapping(target = "schedule.fri", source = "fri")
    @Mapping(target = "schedule.sat", source = "sat")
    @Mapping(target = "schedule.sun", source = "sun")
    @Mapping(target = "schedule.additional", source = "additionalTime")
    @Mapping(target = "schedule.unavailable", source = "unavailable")
    SchedulerObject mapEntityToModel(Scheduler schedulers);

    @Mapping()
    Scheduler mapModelToEntity(SchedulerUpdateRequest schedulers);


}
