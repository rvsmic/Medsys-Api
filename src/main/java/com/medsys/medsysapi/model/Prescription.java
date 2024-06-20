package com.medsys.medsysapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

@Data
@AllArgsConstructor
public class Prescription {
    public int id;
    public int patient_id;
    public int doctor_id;
    public Date prescription_date;
    public String prescription_details;

    public Prescription(Map<String, Object> data) throws ParseException {
        if(data.containsKey("id")) {
            this.id = (int) data.get("id");
        }
        if(data.containsKey("patient_id")) {
            this.patient_id = (int) data.get("patient_id");
        }
        if(data.containsKey("doctor_id")) {
            this.doctor_id = (int) data.get("doctor_id");
        }
        if(data.containsKey("prescription_date")) {
            if(data.get("prescription_date") instanceof String) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                this.prescription_date = new Date(dateFormat.parse((String) data.get("prescription_date")).getTime());
            } else {
                this.prescription_date = (Date) data.get("prescription_date");
            }
        }
        if(data.containsKey("prescription_details")) {
            this.prescription_details = (String) data.get("prescription_details");
        }
    }
}
