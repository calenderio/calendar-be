/*
 * @author : Oguz Kahraman
 * @since : 10.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.mappers;

import com.io.collige.models.internals.scheduler.SchedulerDetails;
import com.io.collige.models.internals.scheduler.SchedulerObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class SchedulerMapperTest {

    private final SchedulerMapper mapper = Mappers.getMapper(SchedulerMapper.class);

    @Test
    void mapToObject() {
        SchedulerObject object = new SchedulerObject();
        object.setSchedule(new SchedulerDetails());
        mapper.mapToObject(object);
        assertNotNull(object.getSchedule().getFri());
    }

}