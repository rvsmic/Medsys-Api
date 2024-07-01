package com.medsys.medsysapi.api;

import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.security.SecUser;
import com.medsys.medsysapi.security.SecUserDetails;
import com.medsys.medsysapi.security.SecUserDetailsService;
import com.medsys.medsysapi.security.SecUserRoles;
import com.medsys.medsysapi.service.user.UserService;
import com.medsys.medsysapi.utils.BasicResponse;
import com.medsys.medsysapi.utils.ErrorResponseHandler;
import com.medsys.medsysapi.utils.JsonHandler;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/info/*")
@CrossOrigin
public class InfoController {

    @Autowired
    SecUserDetailsService secUserDetailsService;

    @Autowired
    ErrorResponseHandler errorResponseHandler;

    @Autowired
    UserService userService;

    @GetMapping("/info/user")
    public ResponseEntity user(@RequestParam(value = "id", required = false) Integer id, @RequestHeader String token) {

        SecUser user = null;

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
        id = id == null ? user.getUserDetails().getId() : id;

        return userService.userInfo(id);
    }

    @PatchMapping("/info/password")
    public ResponseEntity personnel(@RequestBody String body, @RequestHeader String token) {
        SecUser user = null;

        if (token == null) {
            return errorResponseHandler.generateErrorResponse(400, new Exception("Could not find token in request body."));
        }

        user = secUserDetailsService.loadUserByToken(token);

        if (user == null) {
            return errorResponseHandler.generateErrorResponse(401, new BadCredentialsException("Invalid or expired token."));
        }
        if (!user.hasAuthority(SecUserRoles.ROLE_USER.toString()) && !user.hasAuthority(SecUserRoles.ROLE_DOCTOR.toString()) && !user.hasAuthority(SecUserRoles.ROLE_PERSONNEL.toString()) && !user.hasAuthority(SecUserRoles.ROLE_ADMIN.toString())) {
            return errorResponseHandler.generateErrorResponse(403, new AccessDeniedException("User does not have permission to access this resource."));
        }

        user.getToken().refreshToken();

        int id = user.getUserDetails().getId();

        try {
            Map <String, Object> data = JsonHandler.readJsonData(body);
            if (data.containsKey("new_password")) {
                String password = (String) data.get("new_password");
                userService.updatePassword(id, hashPassword(password));
                return new BasicResponse(200, "OK").generateResponse();
            }
            return errorResponseHandler.generateErrorResponse(406, new Exception("Could not find new password in request body."));
        } catch (QueryException | ParseException e) {
            return errorResponseHandler.otherExceptionHandler(e);
        }
    }

    private String hashPassword(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes()).toUpperCase();
    }
}