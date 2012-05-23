package org.bungeni.editor.actions.routers;

import java.util.HashMap;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 * Marks up the official date
 * @author Ashok Hariharan
 */
public class routerCreateOfficialDateReference extends defaultRouter {
    public routerCreateOfficialDateReference(){
        super();
    }

    // !+ACTION_RECONF (rm, jan 2012) - removed toolbarAction as var, class
    // toolbarAction is deprecated
    @Override
    // public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        HashMap<String,String> fromMap = new HashMap<String,String>(){
            {
                put("DateRef","BungeniOfficialDate");
            }
        };
        ooDocument.setSelectedTextAttributes(fromMap);
        return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }
}
