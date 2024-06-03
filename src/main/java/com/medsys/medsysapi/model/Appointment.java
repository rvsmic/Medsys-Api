package com.medsys.medsysapi.model;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        if(data.containsKey("id")) {
            this.id = (int) data.get("id");
        }
        if(data.containsKey("patient_id")) {
            this.patient_id = (int) data.get("patient_id");
        }
        if(data.containsKey("doctor_id")) {
            this.doctor_id = (int) data.get("doctor_id");
        }
        if(data.containsKey("appointment_date")) {
            if(data.get("appointment_date") instanceof String) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    this.appointment_date = new Date(dateFormat.parse((String) data.get("appointment_date")).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                this.appointment_date = (Date) data.get("appointment_date");
            }
        }
        if(data.containsKey("appointment_time")) {
            if(data.get("appointment_time") instanceof String) {
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                try {
                    this.appointment_time = new Time(timeFormat.parse((String) data.get("appointment_time")).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                this.appointment_time = (Time) data.get("appointment_time");
            }
        }
        if(data.containsKey("appointment_status")) {
            this.appointment_status = (String) data.get("appointment_status");
        }
        if(data.containsKey("appointment_name")) {
            this.appointment_name = (String) data.get("appointment_name");
        }
        if(data.containsKey("prescription_id")) {
            this.prescription_id = (Integer) data.get("prescription_id");
        }
        if(data.containsKey("diagnosis")) {
            this.diagnosis = (String) data.get("diagnosis");
        }
        if(data.containsKey("follow_up_date")) {
            if(data.get("follow_up_date") instanceof String) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    this.follow_up_date = new Date(dateFormat.parse((String) data.get("follow_up_date")).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                this.follow_up_date = (Date) data.get("follow_up_date");
            }
        }
    }
}
