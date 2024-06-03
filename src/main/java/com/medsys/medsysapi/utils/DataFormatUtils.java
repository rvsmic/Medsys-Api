package com.medsys.medsysapi.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

public class DataFormatUtils {
    private DataFormatUtils() {
        throw new RuntimeException("Utility class");
    }

    public static String[] parseFullName(String fullName) {
        String firstName = "";
        String secondName = "";
        String lastName = "";

        String nameObject[] = fullName.split(" ");
        if(nameObject.length < 2) {
            throw new IllegalArgumentException("Invalid full name: " + fullName);
        }

        firstName = nameObject[0];
        if(nameObject.length > 2) {
            secondName = nameObject[1];
            lastName = Arrays.stream(nameObject).skip(2).collect(Collectors.joining(" "));
        } else {
            secondName = null;
            lastName = nameObject[1];
        }

        return new String[] {firstName, secondName, lastName};
    }

    public static String dateAsFormattedString(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }
}
