package com.medsys.medsysapi.api;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.security.SecUserDetailsService;
import com.medsys.medsysapi.utils.BasicResponse;
import com.medsys.medsysapi.utils.ErrorResponseHandler;
import com.medsys.medsysapi.utils.JsonHandler;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {

    @Autowired
    QueryDispatcher queryDispatcher;

    @Autowired
    SecUserDetailsService secUserDetailsService;

    @PostMapping
    public ResponseEntity login(@RequestBody(required = true) String data) {
        int id = -1;

        JSONObject jsonData = null;
        String username = "";
        String password = "";


        try {
            if(data == null) {
                return ErrorResponseHandler.generateErrorResponse(400, new Exception("Could not find data in request body."));
            }

            jsonData = JsonHandler.readJsonData(data);

            username = (String) jsonData.get("username");
            password = (String) jsonData.get("password");

            if(username == null || password == null) {
                return ErrorResponseHandler.generateErrorResponse(400, new Exception("Could not find username or password in request body."));
            }

            id = queryDispatcher.getIdUsername(username);

            if (queryDispatcher.checkPasswordValid(id, password)) {
                return new BasicResponse(200, "Login successful.", secUserDetailsService.createUser(queryDispatcher.getSecUserDetails(id))).generateResponse();
            }
        } catch (QueryException e) {
            return ErrorResponseHandler.generateErrorResponse(e.getStatus(), e);
        } catch (ParseException e) {
            return ErrorResponseHandler.generateErrorResponse(400, e);
        }

        return ErrorResponseHandler.generateErrorResponse(500, new Exception("Unexpected error at " + this.getClass().getSimpleName() + "::login"));
    }
}
