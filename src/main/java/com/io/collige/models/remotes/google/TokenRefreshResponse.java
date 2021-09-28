/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.remotes.google;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class TokenRefreshResponse {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("expires_in")
    private Integer expiresIn;

}
