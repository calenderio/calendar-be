/*
 * @author : Oguz Kahraman
 * @since : 5.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.utils;

import com.io.collige.constants.RoleConstants;
import com.io.collige.enums.LicenceTypes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleUtil {

    private static final Set<String> FREE = new HashSet<>();
    private static final Set<String> INDIVIDUAL = new HashSet<>();
    private static final Set<String> COMMERCIAL = new HashSet<>();

    static {
        FREE.add(RoleConstants.FREE);
        INDIVIDUAL.addAll(FREE);
        INDIVIDUAL.add(RoleConstants.INDIVIDUAL);
        COMMERCIAL.addAll(INDIVIDUAL);
        COMMERCIAL.add(RoleConstants.COMMERCIAL);
    }

    public static Set<String> userRole(LicenceTypes licenceTypes) {
        if (LicenceTypes.COMMERCIAL.equals(licenceTypes)) {
            return COMMERCIAL;
        } else if (LicenceTypes.FREE_TRIAL.equals(licenceTypes) || LicenceTypes.INDIVIDUAL.equals(licenceTypes)) {
            return INDIVIDUAL;
        }
        return FREE;
    }

}
