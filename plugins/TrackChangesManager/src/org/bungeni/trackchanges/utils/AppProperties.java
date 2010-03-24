package org.bungeni.trackchanges.utils;

import java.util.HashMap;

/**
 * Class for setting runtime properties in memory
 * @author Ashok Hariharan
 */
public class AppProperties {
    private static HashMap<String, Object> appProps = new HashMap<String,Object>();
    /*
     * Currently used AppProperties :
     *
     * CurrentBillID
     *
     */
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
