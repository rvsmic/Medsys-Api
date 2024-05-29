package com.medsys.medsysapi.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

class DataFormatUtilsTest {

    @Test
    void testParseFullName() {
        String[] result = DataFormatUtils.parseFullName("Jabez Patt");
        Assertions.assertArrayEquals(new String[]{"Jabez", null, "Patt"}, result);

        result = DataFormatUtils.parseFullName("Katharine Mohan Tales");
        Assertions.assertArrayEquals(new String[]{"Katharine", "Mohan", "Tales"}, result);

        result = DataFormatUtils.parseFullName("Charlotte Kilian Patt Jr");
        Assertions.assertArrayEquals(new String[]{"Charlotte", "Kilian", "Patt Jr"}, result);

        result = DataFormatUtils.parseFullName("Sheyla Maxey Rachell Coldiron Danh Goggins");
        Assertions.assertArrayEquals(new String[]{"Sheyla", "Maxey", "Rachell Coldiron Danh Goggins"}, result);
    }

    @Test
    void testDateAsFormattedString() {
        String result = DataFormatUtils.dateAsFormattedString(new GregorianCalendar(2024, Calendar.MAY, 27, 13, 23).getTime(), "dd-MM-yyyy");
        Assertions.assertEquals("27-05-2024", result);

        result = DataFormatUtils.dateAsFormattedString(new GregorianCalendar(2024, Calendar.MAY, 27, 13, 23).getTime(), "dd-MM-yyyy HH:mm");
        Assertions.assertEquals("27-05-2024 13:23", result);

        result = DataFormatUtils.dateAsFormattedString(new GregorianCalendar(2024, Calendar.MAY, 27, 13, 23).getTime(), "yyyy.MM.dd G 'at' HH:mm:ss z+02:00");
        Assertions.assertEquals("2024.05.27 n.e. at 13:23:00 CEST+02:00", result);
    }
}