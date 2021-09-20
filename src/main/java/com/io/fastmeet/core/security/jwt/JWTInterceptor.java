/*
 * @author : oguzkahraman
 * @since : 27.07.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.core.security.jwt;

import com.io.fastmeet.core.annotations.SkipSecurity;
import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.entitites.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JWTInterceptor implements HandlerInterceptor {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private JWTUtil util;

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            if (hm.getMethod().getDeclaredAnnotation(SkipSecurity.class) != null ||
                    hm.getBeanType().getDeclaredAnnotation(SkipSecurity.class) != null) {
                return true;
            }
        }
        String token = request.getHeader(util.getHeaderString());
        if (token == null || !token.contains(util.getTokenPrefix()) || !jwtService.checkIsValid(token)) {
            throw new CalendarAppException(HttpStatus.UNAUTHORIZED, "JWT token is invalid", "JWT_TOKEN_INVALID");
        }
        try {
            if (jwtService.isTokenExpired(token)) {
                throw new CalendarAppException(HttpStatus.UNAUTHORIZED, "JWT token is expired", "JWT_TOKEN_EXPIRED");
            }
        } catch (Exception e) {
            throw new CalendarAppException(HttpStatus.UNAUTHORIZED, "JWT token is invalid", "JWT_TOKEN_INVALID");
        }
        User user = jwtService.getUserFromToken(token);
//        if (!Boolean.TRUE.equals(user.getVerified())) {
//            throw new CalendarAppException(HttpStatus.FORBIDDEN, "Pls validate mail address", "NOT_VALIDATED");
//        }
        UsernamePasswordAuthenticationToken authentication = jwtService.getAuthenticationToken(user);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return true;
    }


}
