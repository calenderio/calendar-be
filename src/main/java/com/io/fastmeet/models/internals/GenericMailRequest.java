/*
 * @author : Oguz Kahraman
 * @since : 5.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.internals;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Data
@NoArgsConstructor
public class GenericMailRequest {

    private String email;
    private Locale language;
    private String name;
    private String code;
    private List<AttachmentModel> attachments = new ArrayList<>();
    private byte[] meetingDetails;

    public GenericMailRequest(String email, String name, String code, Locale language) {
        this.email = email;
        this.name = name;
        this.code = code;
        this.language = language;
    }
}
