/*
 * @author : Oguz Kahraman
 * @since : 3.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.mappers;

import com.io.fastmeet.entitites.User;
import com.io.fastmeet.models.responses.user.UserResponse;
import com.io.fastmeet.utils.RoleUtil;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse mapToModel(User entity);

    @AfterMapping
    default void mapRole(@MappingTarget UserResponse response, User entity) {
        response.setRoles(RoleUtil.userRole(entity.getLicence().getLicenceType()));
    }

}
