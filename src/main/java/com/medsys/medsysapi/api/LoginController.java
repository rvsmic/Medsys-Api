package com.medsys.medsysapi.api;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.security.SecUserDetailsService;
import com.medsys.medsysapi.utils.BasicResponse;
import com.medsys.medsysapi.utils.ErrorResponseHandler;
import com.medsys.medsysapi.utils.JsonHandler;
import net.minidev.json.JSONObject;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    SecUserDetailsService secUserDetailsService;

    @PostMapping
    public ResponseEntity login(@RequestBody(required = true) String data) throws BadRequestException {
        int id = -1;

        String queryResult = "";

        JSONObject jsonData = null;
        String username = "";
        String password = "";


        try {
            jsonData = JsonHandler.readJsonData(data);

            username = (String) jsonData.get("username");
            password = (String) jsonData.get("password");

            id = QueryDispatcher.getIdUsername(jdbcTemplate, username);

            if (QueryDispatcher.checkPasswordValid(jdbcTemplate, id, password)) {
                return new BasicResponse(200, "Login successful.", secUserDetailsService.createUser(QueryDispatcher.getSecUserDetails(jdbcTemplate, id))).generateResponse();
            }
        } catch (QueryException e) {
            return ErrorResponseHandler.generateErrorResponse(e.getStatus(), e);
        }

        return ErrorResponseHandler.generateErrorResponse(500, new Exception("Unexpected error at " + this.getClass().getSimpleName() + "::login"));
    }
}
