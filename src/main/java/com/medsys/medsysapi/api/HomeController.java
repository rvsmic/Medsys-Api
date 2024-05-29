package com.medsys.medsysapi.api;

import com.medsys.medsysapi.security.SecUser;
import com.medsys.medsysapi.security.SecUserDetailsService;
import com.medsys.medsysapi.security.SecUserRoles;
import com.medsys.medsysapi.service.stats.StatsService;
import com.medsys.medsysapi.utils.BasicResponse;
import com.medsys.medsysapi.utils.ErrorResponseHandler;
import com.medsys.medsysapi.utils.JsonHandler;
import net.minidev.json.JSONObject;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/home/*")
@CrossOrigin
public class HomeController {

    @Autowired
    SecUserDetailsService secUserDetailsService;

    @Autowired
    StatsService statsService;

    @GetMapping("/home/stats")
    public ResponseEntity user(@RequestBody String body) {

        JSONObject jsonData = null;
        SecUser user = null;
        String token = "";
        Map<String, Object> responseData = new HashMap<>();

        try {
            jsonData = JsonHandler.readJsonData(body);
            token = (String) jsonData.get("token");
            if (token == null) {
                return ErrorResponseHandler.generateErrorResponse(400, new BadRequestException("Could not find token in request body."));
            }

            user = secUserDetailsService.loadUserByToken(token);

            if (user == null) {
                return ErrorResponseHandler.generateErrorResponse(401, new BadCredentialsException("Invalid or expired token."));
            }
            if (!user.hasAuthority(SecUserRoles.ROLE_USER)) {
                return ErrorResponseHandler.generateErrorResponse(403, new AccessDeniedException("User does not have permission to access this resource."));
            }

            user.getToken().refreshToken();


            List<Map<String, Object>> responseDataDay = statsService.getUserDailyStatisticsLabeled(user.getUserDetails().getId());
            List<Map<String, Object>> responseDataWeek = statsService.getUserWeeklyStatisticsLabeled(user.getUserDetails().getId());
            responseData.put("daily", responseDataDay);
            responseData.put("weekly", responseDataWeek);

            return new BasicResponse(200, "OK", responseData).generateResponse();

        } catch (BadRequestException e) {
            return ErrorResponseHandler.generateErrorResponse(400, e);
        }
    }
}