/*
 * ooUserDefinedAttributes.java
 *
 * Created on August 15, 2007, 12:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.ooo;

import java.util.HashMap;

/**
 *
 * @author Administrator
 */
public class ooUserDefinedAttributes {
    private static String NS_PREFIX = "akoma:";
    private HashMap<String,String> udfAttribs = new HashMap<String,String>();
    /** Creates a new instance of ooUserDefinedAttributes */
    public ooUserDefinedAttributes() {
    }
 
    public static void setNSPrefix (String nsPrefix) {
        NS_PREFIX = nsPrefix;
    }
    
    public static HashMap<String,String> make(String[] list){
        String nsPrefix = NS_PREFIX;
        HashMap<String,String> xmlAttribs = new HashMap<String,String>();
       //name value pairs separated by : 
        for (int i = 0 ; i < list.length ; i++) {
            String[] nameValuePair = list[i].split(":");
            String key = nsPrefix + nameValuePair[0].toLowerCase().trim();
            String value = nameValuePair[1].trim();
            
            xmlAttribs.put(key, value);
        }
        return xmlAttribs;
    }    
    
    public void addAttribute(String key, String value) {
        
    }
    
   
}
