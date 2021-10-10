/*
 * @author : Oguz Kahraman
 * @since : 16.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.internals.file;

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
    private Long size;

}
