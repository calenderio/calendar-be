package com.io.fastmeet.mappers;

import com.io.fastmeet.models.responses.license.LicenseGenerateResponse;
import io.swagger.v3.oas.models.info.License;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LicenseMapper {

    LicenseGenerateResponse mapToModel(License license);

}
