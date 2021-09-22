/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.internals;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MeetInvitationDetailRequest {

    private String userMail;
    private String name;
    private String title;
    private String description;
    private Long eventId;
    private List<AttachmentModel> attachments;
    private List<String> cc;
    private List<String> bcc;

}
