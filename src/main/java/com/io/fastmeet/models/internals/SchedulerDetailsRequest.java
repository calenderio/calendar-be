/*
 * @author : Oguz Kahraman
 * @since : 12.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.internals;

import com.io.fastmeet.models.requests.scheduler.SchedulerUpdateRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SchedulerDetailsRequest {

    private SchedulerUpdateRequest request;
    private Long schedulerId;
    private String token;
}
