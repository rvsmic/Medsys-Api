package com.medsys.medsysapi.security;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.minidev.json.JSONObject;
import net.minidev.json.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
public class SecUserDetails {
    private int id;
    private String name;
    private Date date_of_birth;
    @Nullable
    private String pesel;
    private String gender;
    private String phone_number;
    private String address;
    @Nullable
    private String specialty;
    private String username;
    private String profession;

    public SecUserDetails(Map<String, Object> data) {
        if(data.containsKey("id")) {
            this.id = (int) data.get("id");
        }
        if(data.containsKey("name")) {
            this.name = (String) data.get("name");
        }
        if(data.containsKey("date_of_birth")) {
            if(data.get("date_of_birth") instanceof String) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    this.date_of_birth = new java.sql.Date(dateFormat.parse((String) data.get("date_of_birth")).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                this.date_of_birth = (java.sql.Date) data.get("date_of_birth");
            }
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
        if(data.containsKey("specialty")) {
            this.specialty = (String) data.get("specialty");
        }
        if(data.containsKey("username")) {
            this.username = (String) data.get("username");
        }
        if(data.containsKey("profession")) {
            this.profession = (String) data.get("profession");
        }
    }

    public JSONObject getInfo(){
        JSONObject info = new JSONObject();
        info.put("id", this.id);
        info.put("name", this.name);
        info.put("date_of_birth", this.date_of_birth);
        info.put("pesel", this.pesel);
        info.put("gender", this.gender);
        info.put("phone_number", this.phone_number);
        info.put("address", this.address);
        info.put("specialty", this.specialty);
        info.put("username", this.username);
        info.put("profession", this.profession);
        return info;
    }
}
