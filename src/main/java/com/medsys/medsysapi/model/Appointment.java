package com.medsys.medsysapi.model;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;
import java.util.Map;

@Data
@AllArgsConstructor
public class Appointment {
    public int id;
    public int patient_id;
    public int doctor_id;
    public Date appointment_date;
    public Time appointment_time;
    public String appointment_status;
    public String appointment_name;
    @Nullable
    public Integer prescription_id;
    public String diagnosis;
    @Nullable
    public Date follow_up_date;

    public Appointment(Map<String, Object> data) {
        this.id = (int) data.get("id");
        this.patient_id = (int) data.get("patient_id");
        this.doctor_id = (int) data.get("doctor_id");
        this.appointment_date = (Date) data.get("appointment_date");
        this.appointment_time = (Time) data.get("appointment_time");
        this.appointment_status = (String) data.get("appointment_status");
        this.appointment_name = (String) data.get("appointment_name");
        this.prescription_id = (Integer) data.get("prescription_id");
        this.diagnosis = (String) data.get("diagnosis");
        this.follow_up_date = (Date) data.get("follow_up_date");
    }
}
