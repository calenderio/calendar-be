/*
 * @author : Oguz Kahraman
 * @since : 10.10.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.mappers;

import com.io.collige.constants.RoleConstants;
import com.io.collige.entitites.Licence;
import com.io.collige.entitites.User;
import com.io.collige.enums.LicenceTypes;
import com.io.collige.models.responses.user.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void mapRole() {
        UserResponse response = new UserResponse();
        Licence licenceTypes = new Licence();
        licenceTypes.setLicenceType(LicenceTypes.INDIVIDUAL);
        User user = new User();
        user.setLicence(licenceTypes);
        mapper.mapRole(response, user);
        assertEquals(2, response.getRoles().size());
        assertTrue(response.getRoles().contains(RoleConstants.INDIVIDUAL));
        assertTrue(response.getRoles().contains(RoleConstants.FREE));
    }
}