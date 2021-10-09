/**
 * @author : Oguz Kahraman
 * @since : 10/9/2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface SystemController {

    @GetMapping("/")
    ResponseEntity<String> welcomePage();

}
