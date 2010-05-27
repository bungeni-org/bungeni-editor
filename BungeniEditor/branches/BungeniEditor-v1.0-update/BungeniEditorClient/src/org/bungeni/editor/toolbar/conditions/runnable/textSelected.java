/*
 * sectionExists.java
 *
 * Created on January 26, 2008, 10:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.toolbar.conditions.runnable;

import org.bungeni.editor.toolbar.conditions.BungeniToolbarCondition;
import org.bungeni.ooo.OOComponentHelper;

/**
 * 
 * Contextual evaluator that checks if text has been selected in the document.
 * (i.e. the cursor has highlight some text)
 * e.g. textSelected: false
 * will evaluate to true if no text was selected in the document
 * will evalaute to false if text selected in the document.
 * @author Administrator
 */
public class textSelected extends baseRunnableCondition {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(textSelected.class.getName());
 
    /** Creates a new instance of sectionExists */
    public textSelected() {
    }

 
     boolean check_textSelected(OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
         String conditionValue = "";
         boolean bSelected = false;
         //synchronized (ooDocument) {
          conditionValue = condition.getConditionValue();
          bSelected = ooDocument.isTextSelected();
         //}
         if (bSelected) {
             if (conditionValue.equalsIgnoreCase("true"))
                 return true;
             else
                 return false;
         } else  {
             if (conditionValue.equalsIgnoreCase("false"))
                 return true;
             else
                 return false;
         }
     }
    

    public boolean runCondition(OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
        return check_textSelected(ooDocument, condition);
    }
 
 }
