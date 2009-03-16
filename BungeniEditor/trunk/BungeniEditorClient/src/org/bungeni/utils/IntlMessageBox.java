/*
 * MessageBox.java
 *
 * Created on June 21, 2007, 11:22 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.utils;

import java.awt.Component;
import java.text.MessageFormat;
import javax.swing.JOptionPane;

/**
 * Internationalized Message Box class, fetches error messages from the resource bundle
 * @author undesa
 */
public  class IntlMessageBox extends Object {

    public static void OK(Component parent, String messageStringName, Object[] values){
        String message = CommonResourceBundleHelperFunctions.getErrorMsgString(messageStringName);
        if (values != null) {
            if (values.length > 0 )
                message = MessageFormat.format(message, values);
        }
        JOptionPane.showMessageDialog(parent, message);
    }

    
/***Commented for now ... Support these later *****     
    public static void OK(Component parent, Object[] msgs) {
        JOptionPane.showMessageDialog(parent, msgs);
    }
    public static void OK(String msg){
        JOptionPane.showMessageDialog(null, msg);
    }
    
    public static void OK(Component parent, String msg, String title,  int type){
        
        JOptionPane.showMessageDialog(parent, msg, title, type);
    }
    
    public static int Confirm(Component parent, String msg, String title) {
        int ret = JOptionPane.showConfirmDialog(parent, msg, title, JOptionPane.YES_NO_OPTION );
        return ret;
    }
*/

 
}

