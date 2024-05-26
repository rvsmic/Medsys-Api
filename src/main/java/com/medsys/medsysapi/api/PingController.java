// showcase class for displaying a simple message

package com.medsys.medsysapi.api;

import org.springframework.boot.SpringApplication;
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
        String profile = SpringApplication.run(PingController.class).getEnvironment().getActiveProfiles()[0];
        return "Active profile: " + profile;
    }
}