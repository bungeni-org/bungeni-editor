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
 * AH-16-03-11 Speech By markup
 * @author Ashok Hariharan
 */
public class routerCreateSpeechByReference extends defaultRouter {
    public routerCreateSpeechByReference(){
        super();
    }

    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        //AH-16-03-11 see comment below
        /**
         * Style metadata was inherited -- i.e. if i had a nested section with metadata it
         * inherited the non local metadata onto the current section.  This behavior is not
         * the same with the RDF metadata it is not inherited by sub-sections
         */
        HashMap<String,String> fromMap = new HashMap<String,String>(){
            {
                put("InlineType","from");
            }
        };
        ooDocument.setSelectedTextAttributes(fromMap);
        return new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
    }
}
