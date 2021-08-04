/*
 * @author : Oguz Kahraman
 * @since : 30.07.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.remotes.microsoft;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EmailAddressItem {

    @NonNull
    private EmailAddress emailAddress;
    private String type = "required";

}
