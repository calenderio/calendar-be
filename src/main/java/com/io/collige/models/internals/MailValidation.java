/*
 * @author : Oguz Kahraman
 * @since : 5.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.internals;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailValidation {

    private String templateName;
    private String header;

}
