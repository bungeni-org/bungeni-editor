/*
 * BungeniLoggingUtils.java
 *
 * Created on October 2, 2007, 10:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Administrator
 */
public class BungeniLoggingUtils {
    
    /** Creates a new instance of BungeniLoggingUtils */
    public BungeniLoggingUtils() {
    }
    
    public static String dumpMap(HashMap map ) {
        StringBuffer strDump=new StringBuffer();
        Set keySet = map.keySet();
        Iterator iKeys = keySet.iterator();
        while (iKeys.hasNext()) {
            String key = (String) iKeys.next();
            String value = (String) map.get(key);
            strDump.append(key+" : "+value + "\n");
        }
        return strDump.toString();
    }
}
