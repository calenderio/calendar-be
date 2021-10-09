/**
 * @author : Oguz Kahraman
 * @since : 10/9/2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.controllers.impl;

import com.io.collige.controllers.SystemController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemControllerImpl implements SystemController {

    @Override
    public ResponseEntity<String> welcomePage() {
        return ResponseEntity.ok("Welcome");
    }

}
