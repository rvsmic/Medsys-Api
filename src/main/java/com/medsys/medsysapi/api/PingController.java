package com.medsys.medsysapi.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
@CrossOrigin
public class PingController {
    @GetMapping
    public String ping() {
        // get active profile
        String profile = System.getProperty("spring.profiles.active");
        return "Active profile: " + profile;
    }
}