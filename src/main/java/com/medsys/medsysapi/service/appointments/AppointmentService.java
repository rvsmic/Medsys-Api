package com.medsys.medsysapi.service.appointments;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.model.Appointment;
import com.medsys.medsysapi.utils.JsonHandler;
import com.nimbusds.jose.shaded.gson.JsonObject;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Appointment> getAppointments() throws QueryException {
        String sql = "SELECT * FROM appointments";
        Object[] params = {};
        List<Map<String, Object>> results = queryDispatcher.dispatch(sql, params).getResults();
        List<Appointment> appointments = new ArrayList<>();
        for(Map<String, Object> result : results) {
            appointments.add(new Appointment(result));
        }
        return appointments;
    }

    public Appointment getAppointment(int id) throws QueryException {
        String sql = "SELECT * FROM appointments WHERE id = ?";
        Object[] params = {id};
        return new Appointment(queryDispatcher.dispatch(sql, params).getFirstResult());
    }

    public void addAppointment(Appointment appointment) throws QueryException {
        List<Appointment> appointments = getAppointments();
        int appointment_id = 1;
        while(true) {
            boolean found = false;
            for(Appointment a : appointments) {
                if(a.getId() == appointment_id) {
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
}
