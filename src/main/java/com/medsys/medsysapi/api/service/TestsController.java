package com.medsys.medsysapi.api.service;

import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.model.LabTest;
import com.medsys.medsysapi.security.SecUser;
import com.medsys.medsysapi.security.SecUserDetailsService;
import com.medsys.medsysapi.security.SecUserRoles;
import com.medsys.medsysapi.service.lab_tests.LabTestService;
import com.medsys.medsysapi.service.stats.StatsService;
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

public class TestsController {

    @Autowired
    SecUserDetailsService secUserDetailsService;

    @Autowired
    LabTestService labTestService;

    @Autowired
    StatsService statsService;

    @Autowired
    ErrorResponseHandler errorResponseHandler;

    @GetMapping("/service/tests")
    public ResponseEntity tests(@RequestParam(value = "id", required = false) Integer id, @RequestHeader String token) {
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
                return new BasicResponse(200, "OK", labTestService.getAllLabTests()).generateResponse();
            } else {
                return new BasicResponse(200, "OK", labTestService.getLabTest(id)).generateResponse();
            }
        } catch (QueryException e) {
            return errorResponseHandler.otherExceptionHandler(e);
        }
    }

    @PostMapping("/service/tests")
    public ResponseEntity tests(@RequestBody String body, @RequestHeader String token) {
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
            labTestService.addLabTest(new LabTest(JsonHandler.readJsonData(body)));
            return new BasicResponse(200, "OK").generateResponse();
        } catch (QueryException | ParseException e) {
            return errorResponseHandler.otherExceptionHandler(e);
        }
    }

    @PatchMapping("/service/tests")
    public ResponseEntity tests(@RequestParam int id, @RequestBody String body, @RequestHeader String token) {
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
            labTestService.updateLabTest(id, new LabTest(JsonHandler.readJsonData(body)));
            return new BasicResponse(200, "OK").generateResponse();
        } catch (QueryException | ParseException e) {
            return errorResponseHandler.otherExceptionHandler(e);
        }
    }

    @DeleteMapping("/service/tests")
    public ResponseEntity tests(@RequestParam int id, @RequestHeader String token) {
        SecUser user = null;

        if (token == null) {
            return errorResponseHandler.generateErrorResponse(400, new Exception("Could not find token in request body."));
        }

        user = secUserDetailsService.loadUserByToken(token);

        if (user == null) {
            return errorResponseHandler.generateErrorResponse(401, new BadCredentialsException("Invalid or expired token."));
        }
        if (!user.hasAuthority(SecUserRoles.ROLE_PERSONNEL.toString()) && !user.hasAuthority(SecUserRoles.ROLE_ADMIN.toString())) {
            return errorResponseHandler.generateErrorResponse(403, new AccessDeniedException("User does not have permission to access this resource."));
        }

        user.getToken().refreshToken();

        try {
            labTestService.deleteLabTest(id);
            return new BasicResponse(200, "OK").generateResponse();
        } catch (QueryException e) {
            return errorResponseHandler.generateErrorResponse(e.getStatus(), e);
        }
    }

    @GetMapping("/service/labeled-tests")
    public ResponseEntity labeledTests(@RequestHeader String token) {
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
            return new BasicResponse(200, "OK", labTestService.getAllLabTestsLabeled()).generateResponse();
        } catch (QueryException e) {
            return errorResponseHandler.generateErrorResponse(e.getStatus(), e);
        }
    }
}
