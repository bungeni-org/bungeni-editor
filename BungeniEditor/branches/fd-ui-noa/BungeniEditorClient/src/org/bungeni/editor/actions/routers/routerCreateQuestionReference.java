/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.routers;

import java.util.HashMap;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 * AH-16-03-11 -- Simplified with RDF inline markup instead of using references
 *Marks up the question number
 * @author Ashok Hariharan
 */
public class routerCreateQuestionReference extends defaultRouter {
    public routerCreateQuestionReference(){
        super();
    }
  
    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        HashMap<String,String> fromMap = new HashMap<String,String>(){
            {
                put("InlineType","num");
            }
        };
        ooDocument.setSelectedTextAttributes(fromMap);
        return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }
}
