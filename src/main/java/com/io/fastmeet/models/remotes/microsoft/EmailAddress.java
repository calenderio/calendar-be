/*
 * @author : Oguz Kahraman
 * @since : 30.07.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.remotes.microsoft;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailAddress {

    private String address;
    private String name;

}
