package com.medsys.medsysapi.service.personnel;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.security.SecUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PersonnelService {
    private final QueryDispatcher queryDispatcher;
    private final Logger logger = LoggerFactory.getLogger(PersonnelService.class);

    @Autowired
    public PersonnelService(QueryDispatcher queryDispatcher) {
        this.queryDispatcher = queryDispatcher;
    }

    public List<SecUserDetails> getAllUsers() throws QueryException {
        List<Map<String, Object>> results = queryDispatcher.dispatch("SELECT * FROM personnel", new Object[]{}).getResults();
        List<SecUserDetails> users = new ArrayList<>();
        for (Map<String, Object> result : results) {
            users.add(new SecUserDetails(result));
        }
        return users;
    }

    public SecUserDetails getUser(int id) throws QueryException {
        String sql = "SELECT * FROM personnel WHERE id = ?";
        Object[] params = {id};
        return new SecUserDetails(queryDispatcher.dispatch(sql, params).getFirstResult());
    }

    public List<SecUserDetails> getAllDoctors() throws QueryException {
        List<Map<String, Object>> results = queryDispatcher.dispatch("SELECT * FROM personnel WHERE profession = 'doctor'", new Object[]{}).getResults();
        List<SecUserDetails> doctors = new ArrayList<>();
        for (Map<String, Object> result : results) {
            doctors.add(new SecUserDetails(result));
        }
        return doctors;
    }

    public List<SecUserDetails> getAllPersonnel() throws QueryException {
        List<Map<String, Object>> results = queryDispatcher.dispatch("SELECT * FROM personnel WHERE profession = 'personnel'", new Object[]{}).getResults();
        List<SecUserDetails> personnel = new ArrayList<>();
        for (Map<String, Object> result : results) {
            personnel.add(new SecUserDetails(result));
        }
        return personnel;
    }

    public List<SecUserDetails> getAllAdmins() throws QueryException {
        List<Map<String, Object>> results = queryDispatcher.dispatch("SELECT * FROM personnel WHERE profession = 'admin'", new Object[]{}).getResults();
        List<SecUserDetails> admins = new ArrayList<>();
        for (Map<String, Object> result : results) {
            admins.add(new SecUserDetails(result));
        }
        return admins;
    }
}
