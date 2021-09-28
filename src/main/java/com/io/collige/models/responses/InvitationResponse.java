/*
 * @author : Oguz Kahraman
 * @since : 21.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.responses;

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
