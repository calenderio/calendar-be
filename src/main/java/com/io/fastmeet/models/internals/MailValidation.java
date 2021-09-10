/*
 * @author : Oguz Kahraman
 * @since : 5.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.internals;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailValidation {

    private String templateName;
    private String header;

}
