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
 * Contextual evaluator that checks if a field exists in a document.
 * e.g. fieldExists:field_name
 * will evaluate to true if the field_name exists in the document
 * will evaluate to false if the field_name does not exist in the document
 * @author Administrator
 */
public class fieldNotExists extends baseRunnableCondition {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(fieldExists.class.getName());
    /** Creates a new instance of sectionExists */
    public fieldNotExists() {
    }


    boolean check_fieldNotExists (OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
        fieldExists fldExists = new fieldExists();
        ////fldExists.setOOoComponentHelper(ooDocument);
        if (fldExists.processCondition(ooDocument, condition)) {
            log.debug("fieldNotExists = false");
            return false;
        } else {
            log.debug("fieldNotExists = true");
            return true;
        }
    }
    

    @Override
    public boolean runCondition(OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
         return check_fieldNotExists(ooDocument, condition);
    }
        




 }
