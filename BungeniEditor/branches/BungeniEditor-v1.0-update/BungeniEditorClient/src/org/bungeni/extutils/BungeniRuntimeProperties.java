/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.extutils;

import java.util.Properties;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniRuntimeProperties  {
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

       public static void printKeys(){
           System.out.println("printing keys : ");
           for (String s : runtimeProperties.stringPropertyNames()) {
               System.out.println(s);
           }
       }

}
