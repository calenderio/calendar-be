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
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.HashSet;
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

    @Mapping(target = "mon", source = "model.schedule.mon")
    @Mapping(target = "tue", source = "model.schedule.tue")
    @Mapping(target = "wed", source = "model.schedule.wed")
    @Mapping(target = "thu", source = "model.schedule.thu")
    @Mapping(target = "fri", source = "model.schedule.fri")
    @Mapping(target = "sat", source = "model.schedule.sat")
    @Mapping(target = "sun", source = "model.schedule.sun")
    @Mapping(target = "additionalTime", source = "model.schedule.additional")
    @Mapping(target = "unavailable", source = "model.schedule.unavailable")
    @Mapping(target = "timeZone", source = "model.timeZone")
    @Mapping(target = "name", source = "entity.name")
    @Mapping(target = "id", source = "entity.id")
    Scheduler mapUpdateModelsToEntity(SchedulerUpdateRequest model, Scheduler entity);

    @AfterMapping
    default void mapToObject(@MappingTarget SchedulerObject object) {
        if (object.getSchedule().getMon() == null) {
            object.getSchedule().setMon(new HashSet<>());
        }
        if (object.getSchedule().getTue() == null) {
            object.getSchedule().setTue(new HashSet<>());
        }
        if (object.getSchedule().getWed() == null) {
            object.getSchedule().setWed(new HashSet<>());
        }
        if (object.getSchedule().getThu() == null) {
            object.getSchedule().setThu(new HashSet<>());
        }
        if (object.getSchedule().getFri() == null) {
            object.getSchedule().setFri(new HashSet<>());
        }
        if (object.getSchedule().getSat() == null) {
            object.getSchedule().setSat(new HashSet<>());
        }
        if (object.getSchedule().getSun() == null) {
            object.getSchedule().setSun(new HashSet<>());
        }
        if (object.getSchedule().getAdditional() == null) {
            object.getSchedule().setAdditional(new HashSet<>());
        }
        if (object.getSchedule().getUnavailable() == null) {
            object.getSchedule().setUnavailable(new HashSet<>());
        }

    }


}
