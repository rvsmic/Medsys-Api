package com.medsys.medsysapi.api;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.security.SecUser;
import com.medsys.medsysapi.security.SecUserDetailsService;
import com.medsys.medsysapi.security.SecUserRoles;
import com.medsys.medsysapi.service.user.UserService;
import com.medsys.medsysapi.utils.ErrorResponseHandler;
import com.medsys.medsysapi.utils.JsonHandler;
import net.minidev.json.JSONObject;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/info/*")
@CrossOrigin
public class InfoController {

    @Autowired
    SecUserDetailsService secUserDetailsService;

    @Autowired
    UserService userService;

    @GetMapping("/info/user")
    public ResponseEntity user(@RequestParam(value = "id", required = false) Integer id, @RequestBody String body) {

        JSONObject jsonData = null;
        SecUser user = null;
        String token = "";

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
            id = id == null ? user.getUserDetails().getId() : id;

            return userService.userInfo(id);

        } catch (BadRequestException e) {
            return ErrorResponseHandler.generateErrorResponse(400, e);
        }
    }
}