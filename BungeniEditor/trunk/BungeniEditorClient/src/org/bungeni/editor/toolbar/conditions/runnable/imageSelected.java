/*
 * imageSelected.java
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
 * Contextual evaluator that checks the condition if an image has been selecte int he document.
 * e.g. imageSelected:true
 * evaluates to true if an image has been selected in the document
 * evaluates to false if an image has not been selected in the document
 * @author Administrator
 */
public class imageSelected extends baseRunnableCondition {
   /** Creates a new instance of imageSelected */
    public imageSelected() {
    }

    boolean check_imageSelected (OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
        String actionState =  condition.getConditionValue();
        boolean bObjSelected = ooDocument.isTextGraphicObjectSelected();
      return bObjSelected;
    }
    

    @Override
    public boolean runCondition(OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
        return check_imageSelected(ooDocument, condition);
    }
        



 }
