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
// !+ACTION_RECONF (rm, jan 2012) - ToolbarAction class is deprecated,
// interface methods rewritten to get rid of toolbarAction argument
public interface IBungeniActionRouter {
    // !+ACTION_RECONF (rm, jan 2012) - all these methods do not use action toolbar
    public  org.bungeni.error.BungeniValidatorState route(toolbarSubAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDoc);
    public org.bungeni.error.BungeniValidatorState route_DocumentLevelAction(toolbarSubAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState route_TextSelectedInsert(toolbarSubAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState route_TextSelectedEdit(toolbarSubAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState route_FullInsert(toolbarSubAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState route_FullEdit(toolbarSubAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;
    
}
