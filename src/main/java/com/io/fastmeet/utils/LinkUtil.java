/*
 * @author : Oguz Kahraman
 * @since : 26.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LinkUtil {

    @Value("${system.validation.url}")
    private String validationUrl;

    public String getValidationUrl() {
        return validationUrl;
    }

}
