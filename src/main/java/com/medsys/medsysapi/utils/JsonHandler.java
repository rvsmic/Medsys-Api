package com.medsys.medsysapi.utils;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;
import org.apache.coyote.BadRequestException;

public class JsonHandler {

    private JsonHandler() {
        throw new RuntimeException("Utility class");
    }

    public static JSONObject readJsonData(String data) throws BadRequestException {
        JSONObject jsonData = new JSONObject();
        try {
            jsonData = (JSONObject) JSONValue.parseWithException(data);
        } catch (ParseException e) {
            throw new BadRequestException("Invalid JSON data.");
        }

        return jsonData;
    }
}
