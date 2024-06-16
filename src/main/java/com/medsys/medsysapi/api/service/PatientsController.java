package com.medsys.medsysapi.api.service;

import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.model.Patient;
import com.medsys.medsysapi.security.SecUser;
import com.medsys.medsysapi.security.SecUserDetailsService;
import com.medsys.medsysapi.security.SecUserRoles;
import com.medsys.medsysapi.service.patients.PatientsService;
import com.medsys.medsysapi.service.stats.ActionType;
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

public class PatientsController {

    @Autowired
    SecUserDetailsService secUserDetailsService;

    @Autowired
    PatientsService patientsService;

    @Autowired
    StatsService statsService;

    @Autowired
    ErrorResponseHandler errorResponseHandler;

    @GetMapping("/service/patients")
    public ResponseEntity patients(@RequestParam(value = "id", required = false) Integer id, @RequestHeader String token) {
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
                return new BasicResponse(200, "OK", patientsService.getAllPatients()).generateResponse();
            } else {
                return new BasicResponse(200, "OK", patientsService.getPatient(id)).generateResponse();
            }
        } catch (QueryException e) {
            return errorResponseHandler.otherExceptionHandler(e);
        }
    }

    @PostMapping("/service/patients")
    public ResponseEntity patients(@RequestBody String body, @RequestHeader String token) {
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
            patientsService.addPatient(new Patient(JsonHandler.readJsonData(body)));
            statsService.recordUserAction(user.getUserDetails().getId(), ActionType.PERSONNEL_ADDED_PATIENT);
            return new BasicResponse(200, "OK").generateResponse();
        } catch (QueryException | ParseException e) {
            return errorResponseHandler.otherExceptionHandler(e);
        }
    }

    @PatchMapping("/service/patients")
    public ResponseEntity patients(@RequestParam int id, @RequestBody String body, @RequestHeader String token) {
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
            patientsService.updatePatient(id, new Patient(JsonHandler.readJsonData(body)));
            return new BasicResponse(200, "OK").generateResponse();
        } catch (QueryException | ParseException e) {
            return errorResponseHandler.otherExceptionHandler(e);
        }
    }

    @DeleteMapping("/service/patients")
    public ResponseEntity patients(@RequestParam int id, @RequestHeader String token) {
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
            patientsService.deletePatient(id);
            return new BasicResponse(200, "OK").generateResponse();
        } catch (QueryException e) {
            return errorResponseHandler.generateErrorResponse(e.getStatus(), e);
        }
    }

    @GetMapping("/service/labeled-patients")
    public ResponseEntity labeledPatients(@RequestHeader String token) {
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
            return new BasicResponse(200, "OK", patientsService.getAllPatientsLabeled()).generateResponse();
        } catch (QueryException e) {
            return errorResponseHandler.generateErrorResponse(e.getStatus(), e);
        }
    }
}
