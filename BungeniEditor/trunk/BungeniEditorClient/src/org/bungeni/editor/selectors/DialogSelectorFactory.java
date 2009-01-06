/*
 * DialogSelectorFactory.java
 *
 * Created on March 11, 2008, 2:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.selectors;

/**
 *
 * @author Administrator
 */
public class DialogSelectorFactory {
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DialogSelectorFactory.class.getName());
 
    /** Creates a new instance of DialogSelectorFactory */
    public DialogSelectorFactory() {
    }
     
    public static IDialogSelector getDialogClass(String dialogClass) {
             IDialogSelector dialog= null;
       try {
             log.debug("getDialogClass: creating dialog class"+ dialogClass);
             Class classDlg;
             classDlg= Class.forName(dialogClass);
             dialog = (IDialogSelector) classDlg.newInstance();
       } catch (ClassNotFoundException ex) {
           log.error("getDialogClass:"+ ex.getMessage());
        } finally {
             return dialog;
        }
    }
}
