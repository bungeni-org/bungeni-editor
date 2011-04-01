package org.bungeni.extutils;

//~--- JDK imports ------------------------------------------------------------

import java.util.Properties;

/**
 * This class provides a runtime shared map of properties
 * We use it store common parameters across the editor e.g. root paths, path
 * to certain ini files etc.
 * @author Ashok Hariharan
 */
public class BungeniRuntimeProperties {
    private static Properties runtimeProperties = new Properties();

    public static String getProperty(String key) {
        return runtimeProperties.getProperty(key);
    }

    public static void setProperty(String key, String value) {
        runtimeProperties.setProperty(key, value);
    }

    public static boolean propertyExists(String key) {
        return (runtimeProperties.containsKey(key));
    }

    public static void removeProperty(String key) {
        runtimeProperties.remove(key);
    }

    public static void printKeys() {
        System.out.println("printing keys : ");

        for (String s : runtimeProperties.stringPropertyNames()) {
            System.out.println(s);
        }
    }
}
