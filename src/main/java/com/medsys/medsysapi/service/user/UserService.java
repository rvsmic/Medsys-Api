package com.medsys.medsysapi.service.user;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.security.SecUserDetails;
import com.medsys.medsysapi.utils.BasicResponse;
import com.medsys.medsysapi.utils.DataFormatUtils;
import com.medsys.medsysapi.utils.ErrorResponseHandler;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final QueryDispatcher queryDispatcher;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    ErrorResponseHandler errorResponseHandler;

    @Autowired
    public UserService(QueryDispatcher queryDispatcher) {
        this.queryDispatcher = queryDispatcher;
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
            responseData.put("specialty", userDetails.getSpecialty());

            return new BasicResponse(200, "OK", responseData).generateResponse();

        } catch (QueryException e) {
            return errorResponseHandler.generateErrorResponse(e.getStatus(), e);
        }
    }

    public SecUserDetails getUser(int id) throws QueryException {
        String sql = "SELECT * FROM personnel WHERE id = ?";
        Object[] params = {id};
        return new SecUserDetails(queryDispatcher.dispatch(sql, params).getFirstResult());
    }


    public List<SecUserDetails> getAllPersonnel() throws QueryException {
        List<Map<String, Object>> results = queryDispatcher.dispatch("SELECT * FROM personnel ", new Object[]{}).getResults();
        List<SecUserDetails> personnel = new ArrayList<>();
        for (Map<String, Object> result : results) {
            personnel.add(new SecUserDetails(result));
        }
        return personnel;
    }


    public SecUserDetails getPersonnel(int id) throws QueryException {
        SecUserDetails personnel = getUser(id);
        return personnel;
    }


    public List<Map<String, Object>> getAllUsersLabeled() throws QueryException {
        String sql = "SELECT id, name FROM personnel";
        Object[] params = {};
        List<Map<String, Object>> results = queryDispatcher.dispatch(sql, params).getResults();
        List<Map<String, Object>> labeled = new ArrayList<>();
        for (Map<String, Object> result : results) {
            labeled.add(Map.of("value", result.get("id"), "label", result.get("name")));
        }
        return labeled;
    }

    public List<Map<String, Object>> getAllDoctorsLabeled() throws QueryException {
        String sql = "SELECT id, name FROM personnel WHERE profession = 'Lekarz'";
        Object[] params = {};
        List<Map<String, Object>> results = queryDispatcher.dispatch(sql, params).getResults();
        List<Map<String, Object>> labeled = new ArrayList<>();
        for (Map<String, Object> result : results) {
            labeled.add(Map.of("value", result.get("id"), "label", result.get("name")));
        }
        return labeled;
    }

    public List<Map<String, Object>> getAllPersonnelLabeled() throws QueryException {
        String sql = "SELECT id, name FROM personnel WHERE profession = 'Personel'";
        Object[] params = {};
        List<Map<String, Object>> results = queryDispatcher.dispatch(sql, params).getResults();
        List<Map<String, Object>> labeled = new ArrayList<>();
        for (Map<String, Object> result : results) {
            labeled.add(Map.of("value", result.get("id"), "label", result.get("name")));
        }
        return labeled;
    }

    public List<Map<String, Object>> getAllAdminsLabeled() throws QueryException {
        String sql = "SELECT id, name FROM personnel WHERE profession = 'Admin'";
        Object[] params = {};
        List<Map<String, Object>> results = queryDispatcher.dispatch(sql, params).getResults();
        List<Map<String, Object>> labeled = new ArrayList<>();
        for (Map<String, Object> result : results) {
            labeled.add(Map.of("value", result.get("id"), "label", result.get("name")));
        }
        return labeled;
    }

    public void addUser(SecUserDetails userDetails, String password) throws QueryException {
        if(password == null) {
            throw new QueryException(400, "Password cannot be null.");
        }
        List<SecUserDetails> users = getAllPersonnel();
        int user_id = 1;
        while(true) {
            boolean found = false;
            for(SecUserDetails a : users) {
                if(a.getId() == user_id) {
                    found = true;
                    break;
                }
            }
            if(!found) {
                break;
            }
            user_id++;
        }
        String sql = "INSERT INTO personnel (id, name, date_of_birth, pesel, gender, phone_number, address, specialty, username, password, profession) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = {user_id, userDetails.getName(), userDetails.getDate_of_birth(), userDetails.getPesel(), userDetails.getGender(), userDetails.getPhone_number(), userDetails.getAddress(), userDetails.getSpecialty(), userDetails.getUsername(), password, userDetails.getProfession()};
        queryDispatcher.dispatchUpdate(sql, params);
    }

    public void updateUser(int id, SecUserDetails userDetails, @Nullable String password) throws QueryException {
        if (password == null) {
            String sql = "UPDATE personnel SET name = ?, date_of_birth = ?, pesel = ?, gender = ?, phone_number = ?, address = ?, specialty = ?, username = ?, profession = ? WHERE id = ?";
            Object[] params = {userDetails.getName(), userDetails.getDate_of_birth(), userDetails.getPesel(), userDetails.getGender(), userDetails.getPhone_number(), userDetails.getAddress(), userDetails.getSpecialty(), userDetails.getUsername(), userDetails.getProfession(), id};
            queryDispatcher.dispatchUpdate(sql, params);
        } else {
            String sql = "UPDATE personnel SET name = ?, date_of_birth = ?, pesel = ?, gender = ?, phone_number = ?, address = ?, specialty = ?, username = ?, password = ?, profession = ? WHERE id = ?";
            Object[] params = {userDetails.getName(), userDetails.getDate_of_birth(), userDetails.getPesel(), userDetails.getGender(), userDetails.getPhone_number(), userDetails.getAddress(), userDetails.getSpecialty(), userDetails.getUsername(), password, userDetails.getProfession(), id};
            queryDispatcher.dispatchUpdate(sql, params);
        }
    }

    public void deleteUser(int id) throws QueryException {
        String sql = "DELETE FROM personnel WHERE id = ?";
        Object[] params = {id};
        queryDispatcher.dispatchUpdate(sql, params);
    }
}
