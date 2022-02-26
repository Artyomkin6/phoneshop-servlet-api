package com.es.phoneshop.web.helpers;

import java.util.regex.Pattern;

public class InputValidator {
    private static final String NAME_PATTERN = "[A-Z][A-Za-z]*";
    private static final String PHONE_PATTERN = "\\+\\d+";
    private static final String DATE_PATTERN = "\\d{2}\\.\\d{2}\\.\\d{4}";

    public boolean validateName(String name) {
        return Pattern.matches(NAME_PATTERN, name);
    }

    public boolean validatePhone(String phone) {
        return Pattern.matches(PHONE_PATTERN, phone);
    }

    public boolean validateDate(String date) {
        return Pattern.matches(DATE_PATTERN, date);
    }
}
