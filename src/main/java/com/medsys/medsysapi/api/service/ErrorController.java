package com.medsys.medsysapi.api.service;

import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.model.Error;
import com.medsys.medsysapi.security.SecUser;
import com.medsys.medsysapi.security.SecUserDetailsService;
import com.medsys.medsysapi.security.SecUserRoles;
import com.medsys.medsysapi.service.errors.ErrorService;
import com.medsys.medsysapi.utils.BasicResponse;
import com.medsys.medsysapi.utils.ErrorResponseHandler;
import com.medsys.medsysapi.utils.JsonHandler;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class ErrorController {
    @Autowired
    SecUserDetailsService secUserDetailsService;

    @Autowired
    ErrorService errorService;

    @Autowired
    ErrorResponseHandler errorResponseHandler;

    @GetMapping("/service/errors")
    public ResponseEntity errors(@RequestParam(value = "id", required = false) Integer id, @RequestHeader String token) {
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

        try {
            if (id == null) {
                return new BasicResponse(200, "OK", errorService.getAllErrors()).generateResponse();
            } else {
                return new BasicResponse(200, "OK", errorService.getError(id)).generateResponse();
            }
        } catch (QueryException e) {
            return errorResponseHandler.otherExceptionHandler(e);
        }
    }

    @PostMapping("service/errors")
    public ResponseEntity errors(@RequestBody String body, @RequestHeader String token) {
        SecUser user = null;

        if (token == null) {
            return errorResponseHandler.generateErrorResponse(400, new Exception("Could not find token in request body."));
        }

        user = secUserDetailsService.loadUserByToken(token);

        if (user == null) {
            return errorResponseHandler.generateErrorResponse(401, new BadCredentialsException("Invalid or expired token."));
        }
        if (!user.hasAuthority(SecUserRoles.ROLE_DOCTOR.toString()) && !user.hasAuthority(SecUserRoles.ROLE_PERSONNEL.toString()) && !user.hasAuthority(SecUserRoles.ROLE_ADMIN.toString())) {
            return errorResponseHandler.generateErrorResponse(403, new AccessDeniedException("User does not have permission to access this resource."));
        }

        user.getToken().refreshToken();

        try {
            errorService.addError(new Error(JsonHandler.readJsonData(body)));
            return new BasicResponse(200, "OK").generateResponse();
        } catch (QueryException | ParseException | java.text.ParseException e) {
            return errorResponseHandler.otherExceptionHandler(e);
        }
    }

    @PatchMapping("/service/errors")
    public ResponseEntity visits(@RequestParam int id, @RequestBody String body, @RequestHeader String token) {
        SecUser user = null;

        if (token == null) {
            return errorResponseHandler.generateErrorResponse(400, new Exception("Could not find token in request body."));
        }

        user = secUserDetailsService.loadUserByToken(token);

        if (user == null) {
            return errorResponseHandler.generateErrorResponse(401, new BadCredentialsException("Invalid or expired token."));
        }
        if (!user.hasAuthority(SecUserRoles.ROLE_DOCTOR.toString()) && !user.hasAuthority(SecUserRoles.ROLE_PERSONNEL.toString()) && !user.hasAuthority(SecUserRoles.ROLE_ADMIN.toString())) {
            return errorResponseHandler.generateErrorResponse(403, new AccessDeniedException("User does not have permission to access this resource."));
        }

        user.getToken().refreshToken();

        try {
            errorService.updateError(id, new Error(JsonHandler.readJsonData(body)));
            return new BasicResponse(200, "OK").generateResponse();
        } catch (QueryException | ParseException | java.text.ParseException e) {
            return errorResponseHandler.otherExceptionHandler(e);
        }
    }

}
