package com.medsys.medsysapi.service.prescriptions;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.model.Prescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PrescriptionService {
    private final QueryDispatcher queryDispatcher;
    private final Logger logger = LoggerFactory.getLogger(PrescriptionService.class);

    @Autowired
    public PrescriptionService(QueryDispatcher queryDispatcher) {
        this.queryDispatcher = queryDispatcher;
    }

    public Prescription getPrescription(int id) throws QueryException {
        String sql = "SELECT * FROM prescriptions WHERE id = ?";
        Object[] params = {id};
        return new Prescription(queryDispatcher.dispatch(sql, params).getFirstResult());
    }

    public List<Prescription> getAllPrescriptions() throws QueryException {
        String sql = "SELECT * FROM prescriptions";
        Object[] params = {};
        List<Map<String, Object>> results = queryDispatcher.dispatch(sql, params).getResults();
        List<Prescription> prescriptions = new ArrayList<>();
        for(Map<String, Object> result : results) {
            prescriptions.add(new Prescription(result));
        }
        return prescriptions;
    }

    public List<Map<String, Object>> getAllPrescriptionsLabeled() throws QueryException {
        String sql = "SELECT id, prescription_date, prescription_details FROM prescriptions";
        Object[] params = {};
        List<Map<String, Object>> results = queryDispatcher.dispatch(sql, params).getResults();
        List<Map<String, Object>> labeled = new ArrayList<>();
        for(Map<String, Object> result : results) {
            Date date = (Date) result.get("prescription_date");
            String dateString = "";
            if(date.getMonth() < 10) {
                dateString += "0";
            }
            dateString += date.getMonth() + "/";
            if(date.getDay() < 10) {
                dateString += "0";
            }
            dateString += date.getDay() + "/" + (date.getYear() + 1900);
            String label = "rec. " + result.get("id") + "-" + dateString;
            labeled.add(Map.of("value", result.get("id"), "label", label));
        }
        return labeled;
    }

    public void addPrescription(Prescription prescription) throws QueryException {
        List<Prescription> prescriptions = getAllPrescriptions();
        int prescription_id = 1;
        while(true) {
            boolean found = false;
            for(Prescription p : prescriptions) {
                if(p.getId() == prescription_id) {
                    found = true;
                    break;
                }
            }
            if(!found) {
                break;
            }
            prescription_id++;
        }
        String sql = "INSERT INTO prescriptions (id, patient_id, doctor_id, prescription_date, prescription_details) VALUES (?, ?, ?, ?, ?)";
        Object[] params = {prescription_id, prescription.getPatient_id(), prescription.getDoctor_id(), prescription.getPrescription_date(), prescription.getPrescription_details()};
        queryDispatcher.dispatch(sql, params);
    }

    public void updatePrescription(int id, Prescription prescription) throws QueryException {
        String sql = "UPDATE prescriptions SET patient_id = ?, doctor_id = ?, prescription_date = ?, prescription_details = ? WHERE id = ?";
        Object[] params = {prescription.getPatient_id(), prescription.getDoctor_id(), prescription.getPrescription_date(), prescription.getPrescription_details(), id};
        queryDispatcher.dispatch(sql, params);
    }

    public void deletePrescription(int id) throws QueryException {
        String sql = "DELETE FROM prescriptions WHERE id = ?";
        Object[] params = {id};
        queryDispatcher.dispatch(sql, params);
    }
}
