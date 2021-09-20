/*
 * @author : Oguz Kahraman
 * @since : 16.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.internals;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentModel {

    private byte[] data;
    private String name;
    private String type;

}
