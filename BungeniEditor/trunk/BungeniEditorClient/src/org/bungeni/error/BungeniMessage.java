/*
 * BungeniMessage.java
 *
 * Created on December 17, 2007, 12:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.error;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class BungeniMessage {
   // private BungeniErr m_message;
   // private BungeniErr m_next;
    private int n_message;
    private int n_next;
    private ResourceBundle msgBundle;
    private static org.apache.log4j.Logger log = Logger.getLogger(BungeniMessage.class.getName());
 
    public BungeniMessage(){
        msgBundle = ResourceBundle.getBundle("bungenierror", Locale.US);
        n_message = 0;
        n_next = 0;
    }
    /** Creates a new instance of BungeniMessage */
    public BungeniMessage(int next, int message) {
        msgBundle = ResourceBundle.getBundle("bungenierror", Locale.US);
        n_message = message;
        n_next = next;
    }
    
    public int getMessage(){
        return n_message;
    }
    
    public String getMessageString(){
        String outputString = "error[no_error_defined]";
        String outMsg = "";
        try {
        String msgString = "error["+n_message+"]";
        outMsg =  msgBundle.getString(msgString);
        log.debug("out msg = " + outMsg);
        } catch (MissingResourceException ex) {
            outMsg = MessageFormat.format(msgBundle.getString(outputString),n_message);
        } finally {
            return outMsg;
        }
    }
    
    public int getStep(){
        return n_next;
    }
    
}
