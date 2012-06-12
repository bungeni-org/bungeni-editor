
package org.bungeni.editor.actions.routers;

import java.util.HashMap;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public class routerCreateQuestionByReference extends defaultRouter {
    public routerCreateQuestionByReference(){
        super();
    }

    // !+ACTION_RECONF (rm, jan 2012) - removed toolbarAction as var, class
    // toolbarAction is deprecated
    @Override
    // public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        //AH-15-03-11 see comment below
        /**
         * Style metadata was inherited -- i.e. if i had a nested section with metadata it
         * inherited the non local metadata onto the current section.  This behavior is not
         * the same with the RDF metadata it is not inherited by sub-sections
         */
        HashMap<String,String> fromMap = new HashMap<String,String>(){
            {
                put("BungeniInlineType","from");
            }
        };
        ooDocument.setSelectedTextAttributes(fromMap);
        return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }
}
