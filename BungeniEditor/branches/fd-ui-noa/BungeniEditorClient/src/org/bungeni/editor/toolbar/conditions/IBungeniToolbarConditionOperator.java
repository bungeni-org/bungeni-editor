/*
 * IBungeniToolbarConditionOperator.java
 *
 * Created on January 26, 2008, 4:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.toolbar.conditions;

import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Administrator
 */
public interface IBungeniToolbarConditionOperator {
   /* public void setOOoComponentHelper(OOComponentHelper ooDocument);*/
    public void setOperatingCondition(BungeniToolbarConditionOperator operator, String[] conditions);
    public boolean result(OOComponentHelper ooDocument);
}
