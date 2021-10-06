/**
 * @author : Oguz Kahraman
 * @since : 10/6/2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.utils;

import com.io.collige.constants.RoleConstants;
import com.io.collige.enums.LicenceTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RoleUtilTest {

    @Test
    void getIndividual() {
        Set<String> roles = RoleUtil.userRole(LicenceTypes.INDIVIDUAL);
        assertEquals(2, roles.size());
        assertTrue(roles.contains(RoleConstants.INDIVIDUAL));
        assertTrue(roles.contains(RoleConstants.FREE));
    }

    @Test
    void getFreeTrial() {
        Set<String> roles = RoleUtil.userRole(LicenceTypes.FREE_TRIAL);
        assertEquals(2, roles.size());
        assertTrue(roles.contains(RoleConstants.INDIVIDUAL));
        assertTrue(roles.contains(RoleConstants.FREE));
    }

    @Test
    void getCommercial() {
        Set<String> roles = RoleUtil.userRole(LicenceTypes.COMMERCIAL);
        assertEquals(3, roles.size());
        assertTrue(roles.contains(RoleConstants.INDIVIDUAL));
        assertTrue(roles.contains(RoleConstants.FREE));
        assertTrue(roles.contains(RoleConstants.COMMERCIAL));
    }

    @Test
    void getFree() {
        Set<String> roles = RoleUtil.userRole(LicenceTypes.FREE);
        assertEquals(1, roles.size());
        assertTrue(roles.contains(RoleConstants.FREE));
    }

}