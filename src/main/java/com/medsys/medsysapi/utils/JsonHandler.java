package com.medsys.medsysapi.utils;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;

public class JsonHandler {

    private JsonHandler() {
        throw new RuntimeException("Utility class");
    }

    public static JSONObject readJsonData(String data) throws ParseException {
        JSONObject jsonData = new JSONObject();
        jsonData = (JSONObject) JSONValue.parseWithException(data);

        return jsonData;
    }
}
