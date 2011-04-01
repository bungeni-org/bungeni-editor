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
public class BungeniMsg {
    private String msg;
    private ResourceBundle msgBundle=null;
    private static org.apache.log4j.Logger log = Logger.getLogger(BungeniMsg.class.getName());
 
    public BungeniMsg(){
        msgBundle = ResourceBundle.getBundle("bungenierror", Locale.US);

    }
    /** Creates a new instance of BungeniMessage */
    public BungeniMsg(String strMsg) {
        msgBundle = ResourceBundle.getBundle("bungenierror", Locale.US);
        msg = strMsg;
    }
    
    public String getMessageString(){
        String outMsg = "";
        try {
        outMsg =  msgBundle.getString(msg);
        log.debug("out msg = " + outMsg);
        } catch (MissingResourceException ex) {
            log.error("getMessageString:" + ex.getMessage() );
        } finally {
            return outMsg;
        }
    }
    
    public String toString(){
        return getMessageString();
    }
 
}
