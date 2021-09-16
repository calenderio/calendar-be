/*
 * @author : Oguz Kahraman
 * @since : 27.07.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.core.security.jwt;

import com.io.fastmeet.constants.GeneralMessageConstants;
import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.core.i18n.Translator;
import com.io.fastmeet.entitites.User;
import com.io.fastmeet.repositories.UserRepository;
import com.io.fastmeet.utils.RoleUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class JWTService {

    @Autowired
    private JWTUtil util;

    @Autowired
    private UserRepository userRepository;

    private Key signingKey;

    private JwtParser parser;

    /**
     * Initialize jwt service
     */
    @PostConstruct
    private void initialize() {
        signingKey = new SecretKeySpec(util.getSecret().getBytes(), SignatureAlgorithm.HS512.getJcaName());
        parser = Jwts.parserBuilder().setSigningKey(signingKey).build();
    }

    /**
     * Creates token for logged in user
     *
     * @param user user details
     * @return user token
     */
    public String createToken(User user) {
        return util.getTokenPrefix() + Jwts.builder()
                .setIssuer("FastMeet API")
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setSubject(user.getEmail())
                .setAudience("FastMeet")
                .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusHours(1)))
                .claim("username", user.getEmail().toLowerCase())
                .claim("id", user.getId())
                .claim("roles", RoleUtil.userRole(user.getLicence().getLicenceType()))
                .signWith(signingKey)
                .compact();
    }

    /**
     * Parses token and returns user id
     *
     * @param token user token
     * @return user id
     */
    private Long getIdFromToken(String token) {
        Claims claims = getJwtToken(token);
        return claims.get("id", Double.class).longValue();
    }

    /**
     * Parses token and returns user data
     *
     * @return user data
     */
    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    /**
     * Parses token and returns user data
     *
     * @param token user token
     * @return user data
     */
    public User getUserFromToken(String token) {
        return findById(getIdFromToken(token));
    }

    /**
     * Checks if token valid. Checks token iformation on database
     *
     * @param token user token
     * @return valid or not
     */
    public boolean checkIsValid(String token) {
        User user = findById(getIdFromToken(token));
        Claims claims = getJwtToken(token);
        return user != null && user.getEmail().equals(claims.get("username"));
    }

    /**
     * Parses token and returns token data
     *
     * @param token user token
     * @return like user data, id, expire data etc.
     */
    private Claims getJwtToken(String token) {
        try {
            String reToken = token.replace(util.getTokenPrefix(), "");
            return parser.parseClaimsJws(reToken).getBody();
        } catch (SignatureException e) {
            throw new CalendarAppException(HttpStatus.UNAUTHORIZED, Translator.getMessage("error.signature"), "SIGNATURE_ERR");
        } catch (MalformedJwtException | ExpiredJwtException e) {
            throw new CalendarAppException(HttpStatus.UNAUTHORIZED, "JWT token is invalid", "JWT_TOKEN_INVALID");
        }

    }

    /**
     * Chceks if token expired
     *
     * @param token user token
     * @return expired true, not expired false
     */
    public boolean isTokenExpired(String token) {
        Claims claims = getJwtToken(token);
        LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(claims.getExpiration().getTime()), TimeZone.getDefault().toZoneId());
        return time.isBefore(LocalDateTime.now());
    }

    /**
     * Generates user authorities
     *
     * @param userDetails details
     * @return spring object
     */
    public UsernamePasswordAuthenticationToken getAuthenticationToken(final User userDetails) {

        Set<String> roles = RoleUtil.userRole(userDetails.getLicence().getLicenceType());

        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(roles.toArray(new String[0]))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    /**
     * This method creates new individual user
     *
     * @param id id of user
     */
    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageConstants.USER_NOT_FOUND),
                        GeneralMessageConstants.USR_NOT_FOUND));
    }

}

