/*
 * @author : Oguz Kahraman
 * @since : 13.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.internals;

import com.io.fastmeet.enums.ValidationType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResendValidation {

    private String mail;
    private ValidationType type;

}
