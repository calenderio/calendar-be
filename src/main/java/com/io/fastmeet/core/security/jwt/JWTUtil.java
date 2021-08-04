/*
 * @author : Oguz Kahraman
 * @since : 11.02.2021
 *
 * Copyright - restapi
 **/
package com.io.fastmeet.core.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire.time}")
    private Long expirationTime;

    @Value("${jwt.token.prefix}")
    private String tokenPrefix;

    @Value("${jwt.header.string}")
    private String headerString;

    public String getSecret() {
        return secret;
    }

    public Long getExpirationTime() {
        return expirationTime;
    }

    public String getHeaderString() {
        return headerString;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

}
