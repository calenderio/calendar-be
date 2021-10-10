/*
 * @author : Oguz Kahraman
 * @since : 24.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.internals.scheduler;

import com.io.collige.models.internals.file.AttachmentModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScheduleMeetingRequest {

    private com.io.collige.models.requests.calendar.ScheduleMeetingRequest request;
    private String invitationId;
    private List<AttachmentModel> models = new ArrayList<>();

}
