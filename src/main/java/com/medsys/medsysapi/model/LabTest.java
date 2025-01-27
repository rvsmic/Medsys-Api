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
public class LabTest {
    public int id;
    public int patient_id;
    public Date test_date;
    public String test_type;
    public String test_result;

    public LabTest(Map<String, Object> data) throws ParseException {
        if(data.containsKey("id")) {
            this.id = (int) data.get("id");
        }
        if(data.containsKey("patient_id")) {
            this.patient_id = (int) data.get("patient_id");
        }
        if(data.containsKey("test_date")) {
            if(data.get("test_date") instanceof String) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                this.test_date = new Date(dateFormat.parse((String) data.get("test_date")).getTime());
            } else {
                this.test_date = (Date) data.get("test_date");
            }
        }
        if(data.containsKey("test_type")) {
            this.test_type = (String) data.get("test_type");
        }
        if(data.containsKey("test_result")) {
            this.test_result = (String) data.get("test_result");
        }
    }
}
