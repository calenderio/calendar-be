/*
 * @author : Oguz Kahraman
 * @since : 5.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.utils;

import com.io.fastmeet.enums.LicenceTypes;
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
        FREE.add("ROLE_FREE");
        INDIVIDUAL.addAll(FREE);
        INDIVIDUAL.add("ROLE_INDIVIDUAL");
        COMMERCIAL.addAll(INDIVIDUAL);
        COMMERCIAL.add("ROLE_COMMERCIAL");
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
