package com.medsys.medsysapi.api.service;

import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.model.Prescription;
import com.medsys.medsysapi.security.SecUser;
import com.medsys.medsysapi.security.SecUserDetailsService;
import com.medsys.medsysapi.security.SecUserRoles;
import com.medsys.medsysapi.service.prescriptions.PrescriptionService;
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

public class PrescriptionsController {

    @Autowired
    SecUserDetailsService secUserDetailsService;

    @Autowired
    PrescriptionService prescriptionService;

    @Autowired
    StatsService statsService;

    @Autowired
    ErrorResponseHandler errorResponseHandler;

    @GetMapping("/service/prescriptions")
    public ResponseEntity prescriptions(@RequestParam(value = "id", required = false) Integer id, @RequestHeader String token) {
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
                return new BasicResponse(200, "OK", prescriptionService.getAllPrescriptions()).generateResponse();
            } else {
                return new BasicResponse(200, "OK", prescriptionService.getPrescription(id)).generateResponse();
            }
        } catch (QueryException e) {
            return errorResponseHandler.otherExceptionHandler(e);
        }
    }

    @PostMapping("/service/prescriptions")
    public ResponseEntity prescriptions(@RequestBody String body, @RequestHeader String token) {
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
            prescriptionService.addPrescription(new Prescription(JsonHandler.readJsonData(body)));
            return new BasicResponse(200, "OK").generateResponse();
        } catch (QueryException | ParseException e) {
            return errorResponseHandler.otherExceptionHandler(e);
        }
    }

    @PatchMapping("/service/prescriptions")
    public ResponseEntity prescriptions(@RequestParam int id, @RequestBody String body, @RequestHeader String token) {
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
            prescriptionService.updatePrescription(id, new Prescription(JsonHandler.readJsonData(body)));
            return new BasicResponse(200, "OK").generateResponse();
        } catch (QueryException | ParseException e) {
            return errorResponseHandler.otherExceptionHandler(e);
        }
    }

    @DeleteMapping("/service/prescriptions")
    public ResponseEntity prescriptions(@RequestParam int id, @RequestHeader String token) {
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
            prescriptionService.deletePrescription(id);
            return new BasicResponse(200, "OK").generateResponse();
        } catch (QueryException e) {
            return errorResponseHandler.generateErrorResponse(e.getStatus(), e);
        }
    }

    @GetMapping("/service/labeled-prescriptions")
    public ResponseEntity labeledPrescriptions(@RequestHeader String token) {
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
            return new BasicResponse(200, "OK", prescriptionService.getAllPrescriptionsLabeled()).generateResponse();
        } catch (QueryException e) {
            return errorResponseHandler.generateErrorResponse(e.getStatus(), e);
        }
    }
}
