/*
 * @author : Oguz Kahraman
 * @since : 5.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.internals;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Data
@NoArgsConstructor
public class GenericMailRequest {

    private Set<String> emails;
    private Set<String> cc;
    private Set<String> bcc;
    private Locale language;
    private String name;
    private String header;
    private String description;
    private String code;
    private String inviter;
    private String location;
    private String method;
    private List<AttachmentModel> attachments = new ArrayList<>();
    private byte[] meetingDetails;

    public GenericMailRequest(Set<String> emails, String name, String code, Locale language) {
        this.emails = emails;
        this.name = name;
        this.code = code;
        this.language = language;
    }
}
