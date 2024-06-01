package com.medsys.medsysapi.utils;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;
import org.springframework.http.converter.HttpMessageNotReadableException;

public class JsonHandler {

    private JsonHandler() {
        throw new RuntimeException("Utility class");
    }

    public static JSONObject readJsonData(String data) throws ParseException {
        try {
            JSONObject jsonData = new JSONObject();
            jsonData = (JSONObject) JSONValue.parseWithException(data);
            return jsonData;
        } catch (ClassCastException e) {
            throw new HttpMessageNotReadableException("Could not read JSON data.", e);
        }
    }
}
