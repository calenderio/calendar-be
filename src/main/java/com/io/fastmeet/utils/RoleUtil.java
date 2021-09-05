/*
 * @author : Oguz Kahraman
 * @since : 5.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.utils;

public class RoleUtil {

    public static String userRole(Boolean isCompany) {
        return isCompany ? "ROLE_COMMERCIAL" : "ROLE_INDIVIDUAL";
    }

}
