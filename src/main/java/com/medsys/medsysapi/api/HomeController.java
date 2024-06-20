package com.medsys.medsysapi.api;

import com.medsys.medsysapi.security.SecUser;
import com.medsys.medsysapi.security.SecUserDetailsService;
import com.medsys.medsysapi.security.SecUserRoles;
import com.medsys.medsysapi.service.stats.StatsService;
import com.medsys.medsysapi.utils.BasicResponse;
import com.medsys.medsysapi.utils.ErrorResponseHandler;
import com.medsys.medsysapi.utils.JsonHandler;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
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
    ErrorResponseHandler errorResponseHandler;

    @Autowired
    StatsService statsService;

    @GetMapping("/home/stats")
    public ResponseEntity user(@RequestHeader String token) {

        JSONObject jsonData = null;
        SecUser user = null;
        Map<String, Object> responseData = new HashMap<>();

        if (token == null) {
            return errorResponseHandler.generateErrorResponse(400, new Exception("Could not find token in request body."));
        }

        user = secUserDetailsService.loadUserByToken(token);

        if (user == null) {
            return errorResponseHandler.generateErrorResponse(401, new BadCredentialsException("Invalid or expired token."));
        }
        if (!user.hasAuthority(SecUserRoles.ROLE_USER.toString())) {
            return errorResponseHandler.generateErrorResponse(403, new AccessDeniedException("User does not have permission to access this resource."));
        }

        user.getToken().refreshToken();


        List<Map<String, Object>> responseDataDay = statsService.getUserDailyStatisticsLabeled(user.getUserDetails().getId());
        List<Map<String, Object>> responseDataWeek = statsService.getUserWeeklyStatisticsLabeled(user.getUserDetails().getId());
        responseData.put("daily", responseDataDay);
        responseData.put("weekly", responseDataWeek);

        return new BasicResponse(200, "OK", responseData).generateResponse();
    }
}