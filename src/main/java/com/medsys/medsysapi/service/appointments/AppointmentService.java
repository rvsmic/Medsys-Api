package com.medsys.medsysapi.service.appointments;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.model.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AppointmentService {
    private final QueryDispatcher queryDispatcher;
    private final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    @Autowired
    public AppointmentService(QueryDispatcher queryDispatcher) {
        this.queryDispatcher = queryDispatcher;
    }

    public List<Map<String, Object>> getAllAppointments() throws QueryException {
        String sql = "SELECT a.id AS \"id\", a.appointment_name AS \"nazwa\", p.name AS \"pacjent\", d.name AS \"lekarz\", a.appointment_time AS \"godzina\", a.appointment_date AS \"data\", a.diagnosis AS \"diagnoza\", a.appointment_status AS \"status\" FROM appointments a JOIN patients p ON a.patient_id = p.id JOIN personnel d ON a.doctor_id = d.id;";
        Object[] params = {};
        List<Map<String, Object>> results = queryDispatcher.dispatch(sql, params).getResults();
        for(Map<String, Object> result : results) {
            Time time = (Time) result.get("godzina");
            String timeString = time.toString();
            String[] timeParts = timeString.split(":");
            String timeFormatted = timeParts[0] + ":" + timeParts[1];
            result.put("godzina", timeFormatted);
        }

        return results;
    }

    public Map<String, Object> getAppointment(int id) throws QueryException, ParseException {
        String sql = "SELECT * FROM appointments WHERE id = ?";
        Object[] params = {id};
        Map<String, Object> results = queryDispatcher.dispatch(sql, params).getFirstResult();
        Time time = (Time) results.get("appointment_time");
        String timeString = time.toString();
        String[] timeParts = timeString.split(":");
        String timeFormatted = timeParts[0] + ":" + timeParts[1];
        results.put("appointment_time", timeFormatted);
        return results;
    }

    public void addAppointment(Appointment appointment) throws QueryException {
        List<Map<String, Object>> appointments = getAllAppointments();
        int appointment_id = 1;
        while(true) {
            boolean found = false;
            for(Map<String, Object> a : appointments) {
                if((int)a.get("id") == appointment_id) {
                    found = true;
                    break;
                }
            }
            if(!found) {
                break;
            }
            appointment_id++;
        }
        String sql = "INSERT INTO appointments (id, patient_id, doctor_id, appointment_date, appointment_time, appointment_status, appointment_name, prescription_id, diagnosis, follow_up_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = {appointment_id, appointment.getPatient_id(), appointment.getDoctor_id(), appointment.getAppointment_date(), appointment.getAppointment_time(), appointment.getAppointment_status(), appointment.getAppointment_name(), appointment.getPrescription_id(), appointment.getDiagnosis(), appointment.getFollow_up_date()};
        queryDispatcher.dispatchUpdate(sql, params);
    }

    public void updateAppointment(int id, Appointment appointment) throws QueryException {
        String sql = "UPDATE appointments SET patient_id = ?, doctor_id = ?, appointment_date = ?, appointment_time = ?, appointment_status = ?, appointment_name = ?, prescription_id = ?, diagnosis = ?, follow_up_date = ? WHERE id = ?";
        Object[] params = {appointment.getPatient_id(), appointment.getDoctor_id(), appointment.getAppointment_date(), appointment.getAppointment_time(), appointment.getAppointment_status(), appointment.getAppointment_name(), appointment.getPrescription_id(), appointment.getDiagnosis(), appointment.getFollow_up_date(), id};
        queryDispatcher.dispatchUpdate(sql, params);
    }

    public void deleteAppointment(int id) throws QueryException {
        String sql = "DELETE FROM appointments WHERE id = ?";
        Object[] params = {id};
        queryDispatcher.dispatchUpdate(sql, params);
    }

    public List<Map<String, Object>> getAllAppointmentsLabeled() throws QueryException {
        String sql = "SELECT id, appointment_name FROM appointments";
        Object[] params = {};
        List<Map<String, Object>> results = queryDispatcher.dispatch(sql, params).getResults();
        List<Map<String, Object>> labeled = new ArrayList<>();
        for (Map<String, Object> result : results) {
            labeled.add(Map.of("value", result.get("id"), "label", result.get("appointment_name")));
        }
        return labeled;
    }
}
