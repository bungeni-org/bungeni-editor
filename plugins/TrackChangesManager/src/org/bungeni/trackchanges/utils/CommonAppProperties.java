package org.bungeni.trackchanges.utils;

import java.util.HashMap;

/**
 *
 * @author Ashok Hariharan
 */
public class CommonAppProperties {
    private static HashMap<String, Object> appProps = new HashMap<String,Object>();

    public static Object getProperty(String key) {
        if (appProps.containsKey(key)) {
            return appProps.get(key);
        }
        return null;
    }

    public static void setProperty(String key, Object value) {
        appProps.put(key, value);
    }



}
