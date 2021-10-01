/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.enums;

import com.io.collige.constants.GenericConstants;

public enum AppProviderType {


    MICROSOFT("microsoft", "preferred_username"),
    GOOGLE("google", GenericConstants.EMAIL),
    ZOOM("zoom", GenericConstants.EMAIL),
    INTERNAL("internal", GenericConstants.EMAIL),
    APPLE("apple", GenericConstants.EMAIL);

    public final String value;
    public final String preferredUsername;

    AppProviderType(String value, String preferredUsername) {
        this.value = value;
        this.preferredUsername = preferredUsername;
    }

}
