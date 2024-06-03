package com.medsys.medsysapi.api;

import com.medsys.medsysapi.security.SecUserDetailsService;
import com.medsys.medsysapi.utils.BasicResponse;
import com.medsys.medsysapi.utils.JsonHandler;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logout")
@CrossOrigin
public class LogoutController {

    @Autowired
    SecUserDetailsService secUserDetailsService;

    @PostMapping
    public ResponseEntity logout(@RequestBody(required = true) String data) {
        JSONObject jsonData = null;
        String token = "";

        try {
            jsonData = JsonHandler.readJsonData(data);
            token = (String) jsonData.get("token");
            secUserDetailsService.deleteUser(token);
            return new BasicResponse(200, "Logout successful.").generateResponse();
        } catch (ParseException e) {
            return new BasicResponse(400, e.getMessage()).generateResponse();
        }
    }
}
