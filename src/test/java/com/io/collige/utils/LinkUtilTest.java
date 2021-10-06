/**
 * @author : Oguz Kahraman
 * @since : 10/6/2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class LinkUtilTest {

    @InjectMocks
    private LinkUtil linkUtil;

    @Test
    void url() {
        ReflectionTestUtils.setField(linkUtil, "validationUrl", "example.url");
        String url = linkUtil.getValidationUrl();
        assertEquals("example.url", url);
    }
}