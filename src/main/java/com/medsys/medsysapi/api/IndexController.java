package com.medsys.medsysapi.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "https://apimedsys.azurewebsites.net")
@RestController
public class IndexController {
    @GetMapping({"/", "/index"})
    public String index() {
        return "Medsys API";
    }
}