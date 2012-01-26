/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
 * @author undesa
 */
public class routerDebateRecordMarkupLogo extends defaultRouter {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerDebateRecordMarkupLogo.class.getName());
 
  public routerDebateRecordMarkupLogo(){
      super();
  }
      
    @Override
    //public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {

      // BungeniValidatorState stateObj = CommonRouterActions.displaySelectorDialog(action, subAction, pFrame, ooDocument);
        BungeniValidatorState stateObj = CommonRouterActions.displaySelectorDialog(subAction, pFrame, ooDocument);
      return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

}
