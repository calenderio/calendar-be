/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.mappers;

import com.io.fastmeet.entitites.Invitation;
import com.io.fastmeet.models.responses.InvitationResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvitationMapper {

    InvitationResponse mapEntityToModel(Invitation entity);

    List<InvitationResponse> mapEntityListToModelList(List<Invitation> entity);

}
