package org.bungeni.editor.actions.routers;

import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 * Selects a question type for a questions Section
 * @author Ashok Hariharan
 */
public class routerDebateRecordSelectQuestionsType extends defaultRouter {
 private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerDebateRecordSelectQuestionsType.class.getName());
 
  public routerDebateRecordSelectQuestionsType(){
      super();
  }
  
    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
       BungeniValidatorState stateObj = CommonRouterActions.displaySelectorDialog(action, subAction, pFrame, ooDocument);
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
    public BungeniValidatorState route_FullEdit(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
       BungeniValidatorState stateObj = CommonRouterActions.displaySelectorDialog(action, subAction, pFrame, ooDocument);
      return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }
    
      


}
