package com.ufo.cart.utils.other;

public class StringUtils {
    public static String toUpper(String inputString) {
        StringBuilder result = new StringBuilder();
        for (char ch : inputString.toCharArray()) {
            if (Character.isLetter(ch)) {
                result.append(Character.toUpperCase(ch));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }
}
