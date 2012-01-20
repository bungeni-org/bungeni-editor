/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.routers;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public class routerMarkWorkflowAction extends defaultRouter {
 private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerMarkWorkflowAction.class.getName());
 
     /** Creates a new instance of routerMarkWorkflowAction */
    public routerMarkWorkflowAction() {
        super();
    }

    // !+ACTION_RECONF (rm, jan 2012) - removed toolbarAction as var, class
    // toolbarAction is deprecated
    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        
        CommonRouterActions.displaySubActionDialog(subAction, pFrame, ooDocument, true);
        
       return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

}
