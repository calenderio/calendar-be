/*
 * @author : Oguz Kahraman
 * @since : 6.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.mappers;

import com.io.collige.entitites.FileLink;
import com.io.collige.models.internals.file.FileDetails;
import com.io.collige.models.responses.files.FileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileMapper {

    @Mapping(source = "model.fileLink", target = "link")
    @Mapping(source = "model.fileName", target = "name")
    @Mapping(source = "model.fileType", target = "type")
    @Mapping(source = "model.fileSize", target = "size")
    @Mapping(source = "id", target = "userId")
    FileLink mapModelToEntity(FileDetails model, Long id);

    FileResponse mapModelToResponse(FileLink model);

    List<FileResponse> mapModelListToResponse(List<FileLink> model);

}
