/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.mappers;

import com.io.collige.entitites.Invitation;
import com.io.collige.models.internals.GenericMailRequest;
import com.io.collige.models.internals.MeetInvitationDetailRequest;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collections;

@Mapper(componentModel = "spring")
public interface MailRequestMapper {

    @Mapping(source = "title", target = "header")
    GenericMailRequest meetingRequestToMail(MeetInvitationDetailRequest request);

    @Mapping(source = "ccList", target = "cc")
    @Mapping(source = "bccList", target = "bcc")
    @Mapping(source = "invitationId", target = "code")
    @Mapping(source = "title", target = "header")
    GenericMailRequest invitationToMail(Invitation request);

    @AfterMapping
    default void setEmails(MeetInvitationDetailRequest from, @MappingTarget GenericMailRequest to) {
        to.setEmails(Collections.singleton(from.getUserMail()));
    }

    @AfterMapping
    default void setEmails(Invitation from, @MappingTarget GenericMailRequest to) {
        to.setEmails(Collections.singleton(from.getUserEmail()));
    }
}
