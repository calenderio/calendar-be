/*
 * @author : Oguz Kahraman
 * @since : 12.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.internals;

import com.io.fastmeet.entitites.Scheduler;
import lombok.Data;

@Data
public class SchedulerDetailsRequest {

    private Scheduler scheduler;
    private Long schedulerId;
    private String token;
}
