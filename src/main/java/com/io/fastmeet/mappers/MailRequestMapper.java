/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.mappers;

import com.io.fastmeet.models.internals.GenericMailRequest;
import com.io.fastmeet.models.internals.MeetInvitationDetailRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MailRequestMapper {

    @Mapping(source = "userMail", target = "email")
    GenericMailRequest meetingRequestToMail(MeetInvitationDetailRequest request);

}
