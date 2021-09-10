/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.enums;

public enum AppProviderType {

    MICROSOFT("microsoft", "preferred_username"),
    GOOGLE("google", "email"),
    ZOOM("zoom", "email"),
    APPLE("apple", "email");

    public final String value;
    public final String prefferedUsername;

    AppProviderType(String value, String prefferedUsername) {
        this.value = value;
        this.prefferedUsername = prefferedUsername;
    }

}
