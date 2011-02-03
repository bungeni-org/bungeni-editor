/*
 * IBungeniToolbarCondition.java
 *
 * Created on January 26, 2008, 10:21 PM
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
public interface IBungeniToolbarCondition {
   // public void setOOoComponentHelper(OOComponentHelper ooDocument);
    public boolean processCondition(OOComponentHelper ooDocument, BungeniToolbarCondition condition);
}
