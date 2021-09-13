/*
 * @author : Oguz Kahraman
 * @since : 11.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.responses.scheduler;

import com.io.fastmeet.models.internals.SchedulerObject;
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
