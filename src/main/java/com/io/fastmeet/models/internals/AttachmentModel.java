/*
 * @author : Oguz Kahraman
 * @since : 16.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.internals;

import lombok.Data;

@Data
public class AttachmentModel {

    private byte[] data;
    private String name;
    private String type;

}
