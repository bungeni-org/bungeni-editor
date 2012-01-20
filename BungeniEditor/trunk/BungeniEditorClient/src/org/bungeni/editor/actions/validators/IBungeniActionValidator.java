/*
 * IBungeniActionValidator.java
 *
 * Created on March 3, 2008, 8:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.validators;

import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.ooo.OOComponentHelper;

/**
 *All the validators for an action
 * @author Administrator
 */
// !+ACTION_RECONF (rm, jan 2012) - toolbarAction is deprecated, all functionality has been moved to
// toolbarSubAction
public interface IBungeniActionValidator {
    /**
    public org.bungeni.error.BungeniValidatorState check(toolbarAction action, toolbarSubAction subAction, OOComponentHelper ooDocument);
    public org.bungeni.error.BungeniValidatorState check_DocumentLevelAction(toolbarAction action, toolbarSubAction subAction, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState check_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState check_TextSelectedEdit(toolbarAction action, toolbarSubAction subAction, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState check_FullInsert(toolbarAction action, toolbarSubAction subAction, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState check_FullEdit(toolbarAction action, toolbarSubAction subAction, OOComponentHelper ooDocument) ;
    **/
    
    public org.bungeni.error.BungeniValidatorState check(toolbarSubAction subAction, OOComponentHelper ooDocument);
    public org.bungeni.error.BungeniValidatorState check_DocumentLevelAction(toolbarSubAction subAction, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState check_TextSelectedInsert(toolbarSubAction subAction, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState check_TextSelectedEdit(toolbarSubAction subAction, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState check_FullInsert(toolbarSubAction subAction, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState check_FullEdit(toolbarSubAction subAction, OOComponentHelper ooDocument) ;
}
