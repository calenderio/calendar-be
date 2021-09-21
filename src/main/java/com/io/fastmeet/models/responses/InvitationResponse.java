/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.responses;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InvitationResponse {

    private Long id;
    private String userEmail;
    private String name;
    private Boolean scheduled;
    private List<String> ccList = new ArrayList<>();
    private List<String> bccList = new ArrayList<>();

}
