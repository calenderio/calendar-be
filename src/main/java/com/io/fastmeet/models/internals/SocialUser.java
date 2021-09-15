/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.internals;

import com.io.fastmeet.enums.AppProviderType;
import com.io.fastmeet.models.requests.user.UserCreateRequest;
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
