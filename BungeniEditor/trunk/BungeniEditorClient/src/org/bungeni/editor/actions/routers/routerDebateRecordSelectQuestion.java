package org.bungeni.editor.actions.routers;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.editor.actions.toolbarActionDeprecated;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Ashok Hariharan
 */
public class routerDebateRecordSelectQuestion extends defaultRouter {
 private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerDebateRecordMarkupLogo.class.getName());
 
  public routerDebateRecordSelectQuestion(){
      super();
  }
  
    @Override
    // public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        
        // BungeniValidatorState stateObj = CommonRouterActions.displaySelectorDialog(action, subAction, pFrame, ooDocument);
        BungeniValidatorState stateObj = CommonRouterActions.displaySelectorDialog(subAction, pFrame, ooDocument);
        
        return new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
    }

    /**
     * Overriden function to router TEXT_EDIT events from the editor client to the the word processor
     * @param action
     * @param subAction
     * @param pFrame
     * @param ooDocument
     * @return
     */
    @Override
    // public BungeniValidatorState route_FullEdit(toolbarSubAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
    public BungeniValidatorState route_FullEdit(toolbarAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        
      // BungeniValidatorState stateObj = CommonRouterActions.displaySelectorDialog(action, subAction, pFrame, ooDocument);
      BungeniValidatorState stateObj = CommonRouterActions.displaySelectorDialog(subAction, pFrame, ooDocument);
        
      return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }
    
      


}
