
package org.bungeni.editor.actions.routers;

import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 * This router class launches a dialog in edit and insert mode,
 * It is upto the implementing dialog to decide what to do 
 * @author Ashok
 */
public class routerDialogAction extends defaultRouter {
 private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerDialogAction.class.getName());
  public routerDialogAction(){
      super();
  }
  
    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        
      BungeniValidatorState stateObj = CommonRouterActions.displaySelectorDialog(subAction, pFrame, ooDocument);
        
      return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

      
    @Override
    public BungeniValidatorState route_FullEdit(toolbarAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        
      BungeniValidatorState stateObj = CommonRouterActions.displaySelectorDialog(subAction, pFrame, ooDocument);
        
      return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }
      
}
