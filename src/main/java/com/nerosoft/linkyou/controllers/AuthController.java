package com.nerosoft.linkyou.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @PostMapping("/token/grant")
    public String grantToken(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    @PostMapping("/token/refresh")
    public String refreshToken(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
}
