package com.medsys.medsysapi.service.user;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.security.SecUserDetails;
import com.medsys.medsysapi.utils.BasicResponse;
import com.medsys.medsysapi.utils.DataFormatUtils;
import com.medsys.medsysapi.utils.ErrorResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    QueryDispatcher queryDispatcher;

    @Autowired
    public UserService() {
    }

    public ResponseEntity userInfo(int id) {
        try {
            SecUserDetails userDetails = queryDispatcher.getSecUserDetails(id);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("system_id", userDetails.getId());

            String firstName = "";
            String secondName = "";
            String lastName = "";

            String[] nameParts = DataFormatUtils.parseFullName(userDetails.getName());

            firstName = nameParts[0];
            secondName = nameParts[1];
            lastName = nameParts[2];

            responseData.put("first_name", firstName);
            responseData.put("second_name", secondName);
            responseData.put("last_name", lastName);
            responseData.put("full_name", userDetails.getName());
            responseData.put("login", userDetails.getUsername());
            responseData.put("dob", DataFormatUtils.dateAsFormattedString(userDetails.getDate_of_birth(), "dd.MM.yyyy"));
            responseData.put("profession", userDetails.getProfession());
            responseData.put("speciality", userDetails.getSpeciality());

            return new BasicResponse(200, "OK", responseData).generateResponse();

        } catch (QueryException e) {
            return ErrorResponseHandler.generateErrorResponse(e.getStatus(), e);
        }
    }
}
