/*
 * andOperator.java
 *
 * Created on January 26, 2008, 8:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.toolbar.conditions.operators;

import org.bungeni.editor.toolbar.conditions.BungeniToolbarCondition;
import org.bungeni.editor.toolbar.conditions.BungeniToolbarConditionOperator;
import org.bungeni.editor.toolbar.conditions.IBungeniToolbarCondition;
import org.bungeni.editor.toolbar.conditions.IBungeniToolbarConditionOperator;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Administrator
 */
public class orOperator extends baseOperator implements IBungeniToolbarConditionOperator {
    private String[] conditionValues;
    //private OOComponentHelper ooDocument;
    private BungeniToolbarConditionOperator conditionOperator;
    /** Creates a new instance of andOperator */
    public orOperator() {
    }
/*
    public void setOOoComponentHelper(OOComponentHelper ooDocument) {
        this.ooDocument = ooDocument;
    }*/
    public void setOperatingCondition(BungeniToolbarConditionOperator operator, String[] conditions) {
        conditionValues = conditions;
        conditionOperator = operator;
    }
     
    public boolean result(OOComponentHelper ooDocument) {
        //if any of the conditions fails return false
        for (int i=0; i < conditionValues.length; i++) {
            BungeniToolbarCondition toolbarCond =    new BungeniToolbarCondition(conditionValues[i]);
            IBungeniToolbarCondition iCondition = getConditionObject(toolbarCond.getConditionClass());
            //iCondition.setOOoComponentHelper(ooDocument);
            if (iCondition.processCondition(ooDocument, toolbarCond) == true)
                return true;
        }
        return false;
    }
    
}
