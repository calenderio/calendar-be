/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.models.internals;

import com.io.collige.enums.AppProviderType;
import com.io.collige.models.requests.user.UserCreateRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class SocialUser extends UserCreateRequest {

    private String refreshToken;
    private String token;
    private String pictureUrl;
    private AppProviderType type;
    private LocalDateTime expireDate;
    private String socialMediaMail;
    private String timeZone;

}
