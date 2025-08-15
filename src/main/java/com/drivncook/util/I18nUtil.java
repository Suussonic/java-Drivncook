package com.drivncook.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18nUtil {
    public static ResourceBundle getBundle(String lang) {
        Locale locale = lang.equals("fr") ? Locale.FRENCH : Locale.ENGLISH;
        return ResourceBundle.getBundle("messages", locale);
    }
}
