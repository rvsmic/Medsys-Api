package com.medsys.medsysapi.api;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@CrossOrigin
public class IndexController {
    @GetMapping
    public String index() {
        return "Medsys API";
    }
}