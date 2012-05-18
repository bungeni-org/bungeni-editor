package org.bungeni.editor.actions.routers;

import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Administrator
 */
public class routerSelectBill extends defaultRouter {
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerSelectBill.class.getName());
 

    /** Creates a new instance of routerCreateSection */
    public routerSelectBill() {
        super();
        
    }

    // !+ACTION_RECONF (rm, jan 2012) - removed toolbarAction as var, class
    // toolbarAction is deprecated    
    // public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame,OOComponentHelper ooDocument) {
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction, javax.swing.JFrame pFrame,OOComponentHelper ooDocument) {
        
      // BungeniValidatorState stateObj = CommonRouterActions.displaySelectorDialog(action, subAction, pFrame, ooDocument);
      BungeniValidatorState stateObj = CommonRouterActions.displaySelectorDialog(subAction, pFrame, ooDocument);
        
      return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

    
    
    /**** 
     *
     * private APIs for this action 
     *
     ****/

   
}
