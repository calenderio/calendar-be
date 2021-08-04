/*
 * @author : Oguz Kahraman
 * @since : 30.07.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.remotes.microsoft;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Attachment {

    @SerializedName("@odata.type")
    public String oDataType = "microsoft.graph.fileAttachment";
    private String name;
    private String contentBytes;

}
