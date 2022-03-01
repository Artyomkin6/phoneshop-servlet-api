package com.es.phoneshop.web.helpers;

import java.util.regex.Pattern;

public class QuantityInputHelper implements InputHelper {
    private static final String PATTERN = "-?\\d+";
    @Override
    public boolean CheckInput(String input) {
        return Pattern.matches(PATTERN, input);
    }
}
