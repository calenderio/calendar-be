/*
 * @author : Oguz Kahraman
 * @since : 5.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.core.security.encrypt;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TokenEncryptor {

    @Value("${app.encode.key}")
    private String encodeKey;

    private StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    @PostConstruct
    private void initialize() {
        encryptor.setPassword(encodeKey);
    }

    public synchronized String getEncryptedString(String value) {
        return encryptor.encrypt(value);
    }

    public synchronized String getDecryptedString(String value) {
        return encryptor.decrypt(value);
    }

}
