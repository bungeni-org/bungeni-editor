/*
 * IBungeniActionRouter.java
 *
 * Created on March 10, 2008, 12:24 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.routers;

import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Administrator
 */
public interface IBungeniActionRouter {
    
    public  org.bungeni.error.BungeniValidatorState route(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDoc);
    public org.bungeni.error.BungeniValidatorState route_DocumentLevelAction(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState route_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState route_TextSelectedEdit(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState route_FullInsert(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState route_FullEdit(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;

}
