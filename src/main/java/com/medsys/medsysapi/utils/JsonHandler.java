package com.medsys.medsysapi.utils;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.Map;

public abstract class JsonHandler {
    public static JSONObject readJsonData(String data) throws BadRequestException {
        JSONObject jsonData = new JSONObject();
        try {
            jsonData = (JSONObject) JSONValue.parseWithException(data);
        } catch (ParseException e) {
            throw new BadRequestException("Invalid JSON data.");
        }

        return jsonData;
    }

    public static JSONObject writeQueryAsJSONObject(List<Map<String, Object>> data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", data);

        return jsonObject;
    }
}
