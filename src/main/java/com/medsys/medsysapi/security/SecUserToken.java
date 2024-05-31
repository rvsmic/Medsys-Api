package com.medsys.medsysapi.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

public class SecUserToken {
    private String value;
    @Setter
    @Getter
    private Date expirationDate;
    @Getter
    private final int role_id;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public SecUserToken(int role_id, @Nullable Date expirationDate) {
        this.role_id = role_id;
        this.value = generateTokenValue();
        this.expirationDate = expirationDate == null ? new Date(System.currentTimeMillis() + 1000 * 60 * 30) : expirationDate;
    }

    private String generateTokenValue() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public boolean isTokenValid(String value) {
        return !isExpired() && value.equals(this.value);
    }

    @JsonIgnore
    public boolean isExpired() {
        return expirationDate.before(new Date(System.currentTimeMillis()));
    }

    @JsonProperty("token")
    public String getValue() {
        return value;
    }

    public void refreshToken() {
        this.expirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 30);
    }
}
