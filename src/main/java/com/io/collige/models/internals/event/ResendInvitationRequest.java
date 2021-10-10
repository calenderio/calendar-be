/*
 * @author : Oguz Kahraman
 * @since : 10.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.internals.event;

import com.io.collige.models.requests.meet.InvitationResendRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResendInvitationRequest {

    private Long invitationId;
    private InvitationResendRequest request;

}
