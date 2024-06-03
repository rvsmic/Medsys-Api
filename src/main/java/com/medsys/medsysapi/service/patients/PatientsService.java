package com.medsys.medsysapi.service.patients;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PatientsService {
    private final QueryDispatcher queryDispatcher;
    private final Logger logger = LoggerFactory.getLogger(PatientsService.class);

    @Autowired
    public PatientsService(QueryDispatcher queryDispatcher) {
        this.queryDispatcher = queryDispatcher;
    }

    public List<Patient> getAllPatients() throws QueryException {
        String sql = "SELECT * FROM patients";
        Object[] params = {};
        List<Map<String, Object>> results = queryDispatcher.dispatch(sql, params).getResults();
        List<Patient> patients = new ArrayList<>();
        for(Map<String, Object> result : results) {
            patients.add(new Patient(result));
        }
        return patients;
    }

    public Patient getPatient(int id) throws QueryException {
        String sql = "SELECT * FROM patients WHERE id = ?";
        Object[] params = {id};
        return new Patient(queryDispatcher.dispatch(sql, params).getFirstResult());
    }
}
