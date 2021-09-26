/*
 * @author : Oguz Kahraman
 * @since : 24.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.internals;

import com.io.fastmeet.models.requests.calendar.ScheduleMeetingRequest;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScheduleMeetingDetails {

    private ScheduleMeetingRequest request;
    private String invitationId;
    private List<AttachmentModel> models = new ArrayList<>();

}
