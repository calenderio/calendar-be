/**
 * @author : Oguz Kahraman
 * @since : 31 Eki 2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.models.remotes.zoom;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ZoomLinkCreateRequest {

    private String topic;
    private int type = 2;
    @SerializedName("start_time")
    private String startTime;
    private int duration;
    private String timezone;
    @SerializedName("default_password")
    private Boolean defaultPassword = true;

}
