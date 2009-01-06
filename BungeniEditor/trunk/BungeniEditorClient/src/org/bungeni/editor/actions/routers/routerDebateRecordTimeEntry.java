/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.routers;

import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public class routerDebateRecordTimeEntry extends defaultRouter {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerDebateRecordTimeEntry.class.getName());
 
  public routerDebateRecordTimeEntry(){
      super();
  }
      
    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        BungeniValidatorState stateObj = CommonRouterActions.displaySelectorDialog(action, subAction, pFrame, ooDocument);
      return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

}
