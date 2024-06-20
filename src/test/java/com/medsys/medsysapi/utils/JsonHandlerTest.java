package com.medsys.medsysapi.utils;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonHandlerTest {

    @Test
    void testReadJsonData() {
        JSONObject result = null;
        try {

            String jsonData = "{\n" +
                    "    \"n\": \"1\",\n" +
                    "    \"id\": \"f7a9c57f-f20b-4ed9-9a32-4438dbedadc2\",\n" +
                    "    \"name\": \"Santonio Stern\", \n" +
                    "    \"address\": \"Joyce Road 3246, Coeburn, Maldives, 354992\",\n" +
                    "    \"birthDate\": \"17.03.1987\",\n" +
                    "    \"email\": \"tim_aybarbo2@stopped.zeq\"\n" +
                    "}";

            result = JsonHandler.readJsonData(jsonData);
            Assertions.assertEquals("Joyce Road 3246, Coeburn, Maldives, 354992", result.get("address"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}