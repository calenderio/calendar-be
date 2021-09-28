/*
 * @author : Oguz Kahraman
 * @since : 12.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.internals;

import com.io.collige.entitites.Scheduler;
import lombok.Data;

@Data
public class SchedulerDetailsRequest {

    private Scheduler scheduler;
    private Long schedulerId;

}
