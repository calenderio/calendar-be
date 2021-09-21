/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.mappers;

import com.io.fastmeet.models.internals.GenericMailRequest;
import com.io.fastmeet.models.internals.MeetInvitationDetailRequest;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Collections;

@Mapper(componentModel = "spring")
public interface MailRequestMapper {

    GenericMailRequest meetingRequestToMail(MeetInvitationDetailRequest request);

    @AfterMapping
    default void setEmails(MeetInvitationDetailRequest from, @MappingTarget GenericMailRequest to) {
        to.setEmails(Collections.singletonList(from.getUserMail()));
    }
}
