/*
 * CommonExceptionUtils.java
 *
 * Created on November 23, 2007, 2:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.ooo.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author Administrator
 */
public class CommonExceptionUtils {
    
    /** Creates a new instance of CommonExceptionUtils */
    public CommonExceptionUtils() {
    }
    
    public static String getStackTrace(Exception exception ) {
        StringWriter stringWriter = new StringWriter();
        String stackTrace = "";
        exception.printStackTrace(new PrintWriter(stringWriter));
        // get he stackTrace as String...
        stackTrace = stringWriter.toString();
        return stackTrace;
    }
}
