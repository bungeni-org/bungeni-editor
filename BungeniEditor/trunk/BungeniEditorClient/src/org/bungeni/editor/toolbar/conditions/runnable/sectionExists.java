/*
 * sectionExists.java
 *
 * Created on January 26, 2008, 10:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.toolbar.conditions.runnable;

import org.bungeni.utils.BungeniEditorProperties;
import org.bungeni.editor.toolbar.conditions.BungeniToolbarCondition;
import org.bungeni.editor.toolbar.conditions.IBungeniToolbarCondition;
import org.bungeni.ooo.OOComponentHelper;

/**
 * 
 * Contextual evaluator that checks if a particular section exists in the document.
 * e.g. sectionsExists:section_name
 * will evaluate to true if a section called section_name exists the document
 * will evaluate to false if a section called section_name does not exist the document
 * @author Administrator
 */
public class sectionExists extends baseRunnableCondition {
    /** Creates a new instance of sectionExists */
    public sectionExists() {
    }

    /*
    public void setOOoComponentHelper(OOComponentHelper ooDocument) {
        this.ooDocument = ooDocument;
    }*/

    boolean check_sectionExists (OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
        sectionNotExists secNotExists = new sectionNotExists();
        if (secNotExists.processCondition(ooDocument, condition)){
            return false;
        } else {
            return true;
        }

        
        
    }
    

    @Override
    public boolean runCondition(OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
        return check_sectionExists(ooDocument, condition);
    }

 


 }
