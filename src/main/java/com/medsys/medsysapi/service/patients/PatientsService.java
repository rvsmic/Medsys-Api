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

    public Patient getPatient(int id) throws QueryException {
        String sql = "SELECT * FROM patients WHERE id = ?";
        Object[] params = {id};
        return new Patient(queryDispatcher.dispatch(sql, params).getFirstResult());
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

    public List<Map<String, Object>> getAllPatientsLabeled() throws QueryException {
        String sql = "SELECT id, name FROM patients";
        Object[] params = {};
        List<Map<String, Object>> results = queryDispatcher.dispatch(sql, params).getResults();
        List<Map<String, Object>> labeled = new ArrayList<>();
        for(Map<String, Object> result : results) {
            labeled.add(Map.of("value", result.get("id"), "label", result.get("name")));
        }
        return labeled;
    }

    public void addPatient(Patient patient) throws QueryException {
        List<Patient> patients = getAllPatients();
        int patient_id = 1;
        while (true) {
            boolean found = false;
            for (Patient p : patients) {
                if (p.getId() == patient_id) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                break;
            }
            patient_id++;
        }
        String sql = "INSERT INTO patients (id , name, date_of_birth, pesel, gender, phone_number, address, date_of_death, blood_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = {patient.getId(), patient.getName(), patient.getDate_of_birth(), patient.getPesel(), patient.getGender(), patient.getPhone_number(), patient.getAddress(), patient.getDate_of_death(), patient.getBlood_type()};
        queryDispatcher.dispatch(sql, params);
    }

    public void updatePatient(int id, Patient patient) throws QueryException {
        String sql = "UPDATE patients SET name = ?, date_of_birth = ?, pesel = ?, gender = ?, phone_number = ?, address = ?, date_of_death = ?, blood_type = ? WHERE id = ?";
        Object[] params = {patient.getName(), patient.getDate_of_birth(), patient.getPesel(), patient.getGender(), patient.getPhone_number(), patient.getAddress(), patient.getDate_of_death(), patient.getBlood_type(), id};
        queryDispatcher.dispatch(sql, params);
    }

    public void deletePatient(int id) throws QueryException {
        String sql = "DELETE FROM patients WHERE id = ?";
        Object[] params = {id};
        queryDispatcher.dispatch(sql, params);
    }
}
