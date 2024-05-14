package com.medsys.medsysapi.security;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
public class SecUserDetails {

    static Logger logger = LoggerFactory.getLogger(SecUserDetails.class);

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
    private final String password;
    private final String profession;

    public SecUserDetails(Map<String, Object> data) {
        try {
            this.id = (int) data.get("id");
            this.name =(String) data.get("name");
            this.date_of_birth = (Date) data.get("date_of_birth");
            this.pesel = (String) data.get("pesel");
            this.gender = (String) data.get("gender");
            this.phone_number = (String) data.get("phone_number");
            this.address = (String) data.get("address");
            this.speciality = (String) data.get("specialty");
            this.username = (String) data.get("username");
            this.password = (String) data.get("password");
            this.profession = (String) data.get("profession");
        } catch (Throwable e) {
            logger.error("Error creating SecUserDetails: " + e.getMessage() + " caused by " + getClass().getSimpleName());
            logger.debug("Data: " + data);

            throw e;
        }
    }
}
