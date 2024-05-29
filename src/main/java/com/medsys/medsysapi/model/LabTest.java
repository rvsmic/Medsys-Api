package com.medsys.medsysapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.util.Map;

@Data
@AllArgsConstructor
public class LabTest {
    public int id;
    public int patient_id;
    public Date test_date;
    public String test_type;
    public String test_result;

    public LabTest(Map<String, Object> data) {
        this.id = (int) data.get("id");
        this.patient_id = (int) data.get("patient_id");
        this.test_date = (Date) data.get("test_date");
        this.test_type = (String) data.get("test_type");
        this.test_result = (String) data.get("test_result");
    }
}
