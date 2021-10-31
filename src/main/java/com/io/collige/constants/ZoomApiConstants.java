/**
 * @author : Oguz Kahraman
 * @since : 31 Eki 2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZoomApiConstants {

    public static final String REFRESH_TOKEN = "https://zoom.us/oauth/token";
    public static final String CREATE_MEETING = "https://api.zoom.us/v2/users/me/meetings";

}
