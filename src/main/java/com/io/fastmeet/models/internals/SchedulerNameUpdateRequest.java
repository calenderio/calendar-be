/*
 * @author : Oguz Kahraman
 * @since : 12.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.internals;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SchedulerNameUpdateRequest {

    private String name;
    private Long schedulerId;
    private String token;
}
