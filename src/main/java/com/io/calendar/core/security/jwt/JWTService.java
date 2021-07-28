///*
// * @author : Oguz Kahraman
// * @since : 27.07.2021
// *
// * Copyright - Calendar App Java API
// **/
//package com.io.calendar.core.security.jwt;
//
//import com.io.calendar.core.exception.CalendarAppException;
//import com.io.calendar.core.i18n.Translator;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtParser;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.SignatureException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import javax.crypto.spec.SecretKeySpec;
//import java.security.Key;
//import java.sql.Timestamp;
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.util.TimeZone;
//
//@Service
//public class JWTService {
//
//    @Autowired
//    private JWTUtil util;
//
////    @Autowired
////    private UserService userService;
//
//    private Key signingKey;
//
//    private JwtParser parser;
//
//    /**
//     * Initialize jwt service
//     */
//    @PostConstruct
//    private void initialize() {
//        signingKey = new SecretKeySpec(util.getSecret().getBytes(), SignatureAlgorithm.HS512.getJcaName());
//        parser = Jwts.parserBuilder().setSigningKey(signingKey).build();
//    }
//
//    /**
//     * Creates token for logged in user
//     *
//     * @param userName user Name
//     * @param userNameId user name id
//     * @param id user id
//     * @return user token
//     */
//    public String createToken(String userName, String userNameId, Long id) {
//        return util.getTokenPrefix() + Jwts.builder()
//                .setIssuer("DeityArena API")
//                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
//                .setSubject(userName + "#" + userNameId)
//                .setAudience("DeityArena")
//                .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusMonths(6)))
//                .claim("username", userName.toLowerCase())
//                .claim("userNameId", userNameId)
//                .claim("id", id)
//                .signWith(signingKey)
//                .compact();
//    }
//
//    /**
//     * Parses token and returns user id
//     *
//     * @param token user token
//     * @return user id
//     */
//    private Long getIdFromToken(String token) {
//        Claims claims = getJwtToken(token);
//        return claims.get("id", Double.class).longValue();
//    }
//
//    /**
//     * Parses token and returns user data
//     *
//     * @param token user token
//     * @return user data
//     */
//    public User getUserFromToken(String token) {
//        return userService.findUserById(getIdFromToken(token));
//    }
//
//    /**
//     * Checks if token valid. Checks token iformation on database
//     *
//     * @param token user token
//     * @return valid or not
//     */
//    public boolean chechkIsValid(String token) {
//        User user = userService.findUserById(getIdFromToken(token));
//        Claims claims = getJwtToken(token);
//        return user != null && user.getUsername().equals(claims.get("username")) &&
//                user.getUsernameId().equals(claims.get("userNameId"));
//    }
//
//    /**
//     * Parses token and returns token data
//     *
//     * @param token user token
//     * @return like user data, id, expire data etc.
//     */
//    private Claims getJwtToken(String token) {
//        try {
//            String reToken = token.replace(util.getTokenPrefix(), "");
//            return parser.parseClaimsJws(reToken).getBody();
//        } catch (SignatureException e) {
//            throw new CalendarAppException(HttpStatus.UNAUTHORIZED, Translator.getMessage("error.signature"), "SIGNATURE_ERR");
//        } catch (MalformedJwtException e) {
//            throw new CalendarAppException(HttpStatus.UNAUTHORIZED, "JWT token is invalid", "JWT_TOKEN_INVALID");
//        }
//
//    }
//
//    /**
//     * Chceks if token expired
//     *
//     * @param token user token
//     * @return expired true, not expired false
//     */
//    public boolean isTokenExpired(String token) {
//        Claims claims = getJwtToken(token);
//        LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(claims.getExpiration().getTime()), TimeZone.getDefault().toZoneId());
//        return time.isBefore(LocalDateTime.now());
//    }
//
//}
//
