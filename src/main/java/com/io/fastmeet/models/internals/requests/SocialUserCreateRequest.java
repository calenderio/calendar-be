/*
 * @author : Oguz Kahraman
 * @since : 4.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.models.internals.requests;

import com.io.fastmeet.enums.CalendarProviderType;
import com.io.fastmeet.models.requests.user.UserCreateRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SocialUserCreateRequest extends UserCreateRequest {

    private String token;
    private String refreshToken;
    private CalendarProviderType type;

}