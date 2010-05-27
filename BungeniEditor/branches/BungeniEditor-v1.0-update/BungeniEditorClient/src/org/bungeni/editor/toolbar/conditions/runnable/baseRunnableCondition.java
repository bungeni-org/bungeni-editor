/*
 * baseRunnableCondition.java
 *
 * Created on May 30, 2008, 5:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.toolbar.conditions.runnable;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.editor.toolbar.conditions.BungeniToolbarCondition;
import org.bungeni.editor.toolbar.conditions.IBungeniToolbarCondition;
import org.bungeni.ooo.OOComponentHelper;

/**
 *Base class that implements the toolbar condition processor interface
 * @author Ashok Hariharsn
 */
public abstract class baseRunnableCondition implements IBungeniToolbarCondition {

      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(baseRunnableCondition.class.getName());


    /** Creates a new instance of baseRunnableCondition */
    public baseRunnableCondition() {
    }

    public abstract boolean runCondition(OOComponentHelper ooDocument, BungeniToolbarCondition condition) throws ConditionFailureException;
    
    public boolean processCondition(OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
            boolean bResult = false;
        try {
            if (condition.hasNegationCondition()) {
                bResult = !runCondition(ooDocument, condition);
            } else {
                bResult = runCondition(ooDocument, condition);
            }
        } catch (Throwable ex) {
            System.out.println("Catching condition Failure Exception");
        }

        return bResult;
    }
    
}
