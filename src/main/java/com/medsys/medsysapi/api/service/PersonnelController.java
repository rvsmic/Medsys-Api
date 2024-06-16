package com.medsys.medsysapi.api.service;

import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.security.SecUser;
import com.medsys.medsysapi.security.SecUserDetails;
import com.medsys.medsysapi.security.SecUserDetailsService;
import com.medsys.medsysapi.security.SecUserRoles;
import com.medsys.medsysapi.service.stats.ActionType;
import com.medsys.medsysapi.service.stats.StatsService;
import com.medsys.medsysapi.service.user.UserService;
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

public class PersonnelController {
    @Autowired
    SecUserDetailsService secUserDetailsService;

    @Autowired
    UserService userService;

    @Autowired
    StatsService statsService;

    @Autowired
    ErrorResponseHandler errorResponseHandler;

    @GetMapping("/service/doctors")
    public ResponseEntity doctors(@RequestParam(value = "id", required = false) Integer id, @RequestHeader String token) {
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
                return new BasicResponse(200, "OK", userService.getAllDoctors()).generateResponse();
            } else {
                return new BasicResponse(200, "OK", userService.getDoctor(id)).generateResponse();
            }
        } catch (QueryException e) {
            return errorResponseHandler.otherExceptionHandler(e);
        }
    }

    @GetMapping("/service/labeled-doctors")
    public ResponseEntity labeledDoctors(@RequestHeader String token) {
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
            return new BasicResponse(200, "OK", userService.getAllDoctorsLabeled()).generateResponse();
        } catch (QueryException e) {
            return errorResponseHandler.generateErrorResponse(e.getStatus(), e);
        }
    }

    @GetMapping("/service/personnel")
    public ResponseEntity personnel(@RequestParam(value = "id", required = false) Integer id, @RequestHeader String token) {
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
                return new BasicResponse(200, "OK", userService.getAllPersonnel()).generateResponse();
            } else {
                return new BasicResponse(200, "OK", userService.getPersonnel(id)).generateResponse();
            }
        } catch (QueryException e) {
            return errorResponseHandler.otherExceptionHandler(e);
        }
    }

    @GetMapping("/service/labeled-personnel")
    public ResponseEntity labeledPersonnel(@RequestHeader String token) {
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
            return new BasicResponse(200, "OK", userService.getAllPersonnelLabeled()).generateResponse();
        } catch (QueryException e) {
            return errorResponseHandler.generateErrorResponse(e.getStatus(), e);
        }
    }

    @GetMapping("/service/admins")
    public ResponseEntity admins(@RequestParam(value = "id", required = false) Integer id, @RequestHeader String token) {
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
                return new BasicResponse(200, "OK", userService.getAllAdmins()).generateResponse();
            } else {
                return new BasicResponse(200, "OK", userService.getAdmin(id)).generateResponse();
            }
        } catch (QueryException e) {
            return errorResponseHandler.otherExceptionHandler(e);
        }
    }

    @GetMapping("/service/labeled-admins")
    public ResponseEntity labeledAdmins(@RequestHeader String token) {
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
            return new BasicResponse(200, "OK", userService.getAllAdminsLabeled()).generateResponse();
        } catch (QueryException e) {
            return errorResponseHandler.generateErrorResponse(e.getStatus(), e);
        }
    }

    @GetMapping("/service/users")
    public ResponseEntity users(@RequestParam(value = "id", required = false) Integer id, @RequestHeader String token) {
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
                return new BasicResponse(200, "OK", userService.getAllUsers()).generateResponse();
            } else {
                return new BasicResponse(200, "OK", userService.getUser(id)).generateResponse();
            }
        } catch (QueryException e) {
            return errorResponseHandler.otherExceptionHandler(e);
        }
    }

    @GetMapping("/service/labeled-users")
    public ResponseEntity labeledUsers(@RequestHeader String token) {
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
            return new BasicResponse(200, "OK", userService.getAllUsersLabeled()).generateResponse();
        } catch (QueryException e) {
            return errorResponseHandler.generateErrorResponse(e.getStatus(), e);
        }
    }

    @PostMapping("/service/users")
    public ResponseEntity personnel(@RequestBody String body, @RequestHeader String token) {
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
            String password;
            try {
                password = JsonHandler.readJsonData(body).get("password").toString();
            } catch (ParseException e) {
                password = null;
            }
            userService.addUser(new SecUserDetails(JsonHandler.readJsonData(body)), password);
            statsService.recordUserAction(user.getUserDetails().getId(), ActionType.PERSONNEL_ADDED_PERSONNEL);
            return new BasicResponse(200, "OK").generateResponse();
        } catch (QueryException | ParseException e) {
            return errorResponseHandler.otherExceptionHandler(e);
        }
    }

    @PatchMapping("/service/users")
    public ResponseEntity personnel(@RequestParam int id, @RequestBody String body, @RequestHeader String token) {
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
            String password;
            try {
                password = JsonHandler.readJsonData(body).get("password").toString();
            } catch (ParseException e) {
                password = null;
            }
            userService.updateUser(id, new SecUserDetails(JsonHandler.readJsonData(body)), password);
            return new BasicResponse(200, "OK").generateResponse();
        } catch (QueryException | ParseException e) {
            return errorResponseHandler.otherExceptionHandler(e);
        }
    }

    @DeleteMapping("/service/users")
    public ResponseEntity personnel(@RequestParam int id, @RequestHeader String token) {
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
            userService.deleteUser(id);
            return new BasicResponse(200, "OK").generateResponse();
        } catch (QueryException e) {
            return errorResponseHandler.generateErrorResponse(e.getStatus(), e);
        }
    }
}
