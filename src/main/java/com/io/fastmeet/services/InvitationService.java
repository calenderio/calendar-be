/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services;

import com.io.fastmeet.models.internals.MeetInvitationDetailRequest;

public interface InvitationService {
    String saveInvitation(MeetInvitationDetailRequest request);
}
