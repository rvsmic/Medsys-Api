package com.medsys.medsysapi.model;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.util.Map;

@Data
@AllArgsConstructor
public class Patient {
    public int id;
    public String name;
    public Date date_of_birth;
    public String pesel;
    public String gender;
    public String phone_number;
    public String address;
    @Nullable
    public Date date_of_death;
    public String blood_type;

    public Patient(Map<String, Object> data) {
        if(data.containsKey("id")) {
            this.id = (int) data.get("id");
        }
        if(data.containsKey("name")) {
            this.name = (String) data.get("name");
        }
        if(data.containsKey("date_of_birth")) {
            this.date_of_birth = (Date) data.get("date_of_birth");
        }
        if(data.containsKey("pesel")) {
            this.pesel = (String) data.get("pesel");
        }
        if(data.containsKey("gender")) {
            this.gender = (String) data.get("gender");
        }
        if(data.containsKey("phone_number")) {
            this.phone_number = (String) data.get("phone_number");
        }
        if(data.containsKey("address")) {
            this.address = (String) data.get("address");
        }
        if(data.containsKey("date_of_death")) {
            this.date_of_death = (Date) data.get("date_of_death");
        }
        if(data.containsKey("blood_type")) {
            this.blood_type = (String) data.get("blood_type");
        }
    }
}