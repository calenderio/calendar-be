/*
 * @author : Oguz Kahraman
 * @since : 6.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.mappers;

import com.io.collige.entitites.FileLink;
import com.io.collige.models.internals.FileDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMapper {

    @Mapping(source = "model.fileLink", target = "link")
    @Mapping(source = "model.fileName", target = "name")
    @Mapping(source = "model.fileType", target = "type")
    @Mapping(source = "id", target = "userId")
    FileLink mapModelToEntity(FileDetails model, Long id);

}
