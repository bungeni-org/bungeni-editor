
package org.bungeni.editor.actions.routers;

import java.util.HashMap;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public class routerCreateGenericJudgementNumberReference extends defaultRouter {
    public routerCreateGenericJudgementNumberReference(){
        super();
    }
    
    final String _referencePrefix_ = "numRef";
    final String _referenceNameSeparator_ = ":";
    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        int i = 1;
        String newRefNo  = _referencePrefix_ + _referenceNameSeparator_ + i;
        while (ooDocument.getReferenceMarks().hasByName(newRefNo) ) {
            newRefNo = _referencePrefix_ + _referenceNameSeparator_ + i;
            i++;
        }
        subAction.setActionValue(newRefNo);
        //chain the routerCreateReference to this..
        routerCreateReference rcf = new routerCreateReference();
        BungeniValidatorState bvState = rcf.route_TextSelectedInsert(action, subAction, pFrame, ooDocument);
        return bvState;
    }
}
