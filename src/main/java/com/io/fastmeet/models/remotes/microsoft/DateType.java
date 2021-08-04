/*
 * @author : Oguz Kahraman
 * @since : 27.07.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.remotes.microsoft;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateType {

    private String dateTime;
    private String timeZone;
}
