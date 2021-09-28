/*
 * @author : Oguz Kahraman
 * @since : 13.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.internals;

import com.io.collige.enums.ValidationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResendValidation {

    private String mail;
    private ValidationType type;

}
