/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.internals;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
