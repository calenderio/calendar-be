/*
 * @author : Oguz Kahraman
 * @since : 5.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleUtil {

    public static String userRole(Boolean isCompany) {
        return isCompany ? "ROLE_COMMERCIAL" : "ROLE_INDIVIDUAL";
    }

}
