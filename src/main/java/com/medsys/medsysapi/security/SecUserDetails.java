package com.medsys.medsysapi.security;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
public final class SecUserDetails {

    private final Logger logger = LoggerFactory.getLogger(SecUserDetails.class);

    private final int id;
    private final String name;
    private final Date date_of_birth;
    @Nullable
    private final String pesel;
    private final String gender;
    private final String phone_number;
    private final String address;
    @Nullable
    private final String speciality;
    private final String username;
    private final String profession;

    public SecUserDetails(Map<String, Object> data) {
        this.id = (int) data.get("id");
        this.name =(String) data.get("name");
        this.date_of_birth = (Date) data.get("date_of_birth");
        this.pesel = (String) data.get("pesel");
        this.gender = (String) data.get("gender");
        this.phone_number = (String) data.get("phone_number");
        this.address = (String) data.get("address");
        this.speciality = (String) data.get("specialty");
        this.username = (String) data.get("username");
        this.profession = (String) data.get("profession");
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
        info.put("speciality", this.speciality);
        info.put("username", this.username);
        info.put("profession", this.profession);
        return info;
    }
}
