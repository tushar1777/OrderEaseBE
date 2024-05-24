package com.zosh.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/test")
public class ApiTestController {
    @PostMapping("/test")
    public String testApi(){
        return "test";
    }
}
