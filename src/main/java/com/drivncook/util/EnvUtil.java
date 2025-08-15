package com.drivncook.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvUtil {
    private static final Properties envProps = new Properties();

    static {
        try (InputStream input = EnvUtil.class.getClassLoader().getResourceAsStream(".env")) {
            if (input != null) {
                envProps.load(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return envProps.getProperty(key);
    }
}

