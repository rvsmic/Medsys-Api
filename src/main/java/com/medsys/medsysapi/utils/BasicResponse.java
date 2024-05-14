package com.medsys.medsysapi.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BasicResponse<T> {
    static Logger logger = LoggerFactory.getLogger(BasicResponse.class);

    private final int status;
    private final String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private final Date timeStamp;
    private final T data;

    public BasicResponse() {
        this.timeStamp = new Date(System.currentTimeMillis());
        this.status = 200;
        this.message = "OK";
        this.data = null;
    }

    public BasicResponse(int status, String message) {
        this.timeStamp = new Date(System.currentTimeMillis());
        this.status = status;
        this.message = message;
        this.data = null;
    }

    public BasicResponse(int status, String message, T payload) {
        this.timeStamp = new Date(System.currentTimeMillis());
        this.status = status;
        this.message = message;
        this.data = payload;
    }

    private String getLogMessage() {
        return "Response: " + this.status + " " + this.message;
    }

    public ResponseEntity<Object> generateResponse() {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("timestamp", this.timeStamp);
        responseData.put("status", this.status);
        responseData.put("message", this.message);
        if(this.data != null)
            responseData.put("data", this.data);

        logger.info(getLogMessage());
        return ResponseEntity.status(this.status).body(responseData);
    }
}
