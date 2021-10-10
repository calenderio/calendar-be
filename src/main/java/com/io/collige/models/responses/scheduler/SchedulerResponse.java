/*
 * @author : Oguz Kahraman
 * @since : 11.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.responses.scheduler;

import com.io.collige.models.internals.scheduler.SchedulerObject;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulerResponse {

    @ArraySchema(schema = @Schema(description = "Scheduler items", implementation = SchedulerObject.class))
    private Set<SchedulerObject> schedulers;

}
