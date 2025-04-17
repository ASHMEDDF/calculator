package com.raven.calculator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test/public")
    public String publicEndpoint() {
        return "This is public";
    }
}
