/*
 * @author : Oguz Kahraman
 * @since : 12.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.internals.scheduler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SchedulerNameUpdateRequest {

    private String name;
    private Long schedulerId;
}
