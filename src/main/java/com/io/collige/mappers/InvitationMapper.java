/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.mappers;

import com.io.collige.entitites.Invitation;
import com.io.collige.models.responses.meeting.InvitationResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvitationMapper {

    InvitationResponse mapEntityToModel(Invitation entity);

    List<InvitationResponse> mapEntityListToModelList(List<Invitation> entity);

}
