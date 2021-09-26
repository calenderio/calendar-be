/*
 * @author : Oguz Kahraman
 * @since : 11.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.mappers;

import com.io.collige.entitites.Scheduler;
import com.io.collige.models.internals.SchedulerDetails;
import com.io.collige.models.internals.SchedulerObject;
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

    @Mapping(target = "additionalTime", source = "model.additional")
    @Mapping(target = "unavailable", source = "model.unavailable")
    Scheduler mapDetailsToEntity(SchedulerDetails model, String timeZone);

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
