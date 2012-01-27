
package org.bungeni.editor.actions.routers;

import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Administrator
 */
// !+ACTION_RECONF (rm, jan 2012) - ToolbarAction class is deprecated,
// interface methods rewritten to get rid of toolbarAction argument
public interface IBungeniActionRouter {
    // !+ACTION_RECONF (rm, jan 2012) - all these methods do not use action toolbar
    public  org.bungeni.error.BungeniValidatorState route(toolbarAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDoc);
    public org.bungeni.error.BungeniValidatorState route_DocumentLevelAction(toolbarAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState route_TextSelectedEdit(toolbarAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState route_FullInsert(toolbarAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState route_FullEdit(toolbarAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;
    
}
