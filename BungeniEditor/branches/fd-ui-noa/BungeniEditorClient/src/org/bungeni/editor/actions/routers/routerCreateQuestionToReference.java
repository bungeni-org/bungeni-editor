/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.routers;

import java.util.HashMap;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.extutils.CommonStringFunctions;

/**
 * AH-16-03-11 -- THIS ACTION IS DEPRECATED
 * @author Ashok Hariharan
 */
@Deprecated
public class routerCreateQuestionToReference extends defaultRouter {
    public routerCreateQuestionToReference(){
        super();
    }
    
    final String _referencePrefix_ = "BungeniQuestionTo";
    final String _referenceNameSeparator_ = ":";
    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
           
        String currentSection =  ooDocument.currentSectionName();
        HashMap<String,String> sectionMeta = ooDocument.getSectionMetadataAttributes(currentSection);
        String strQuestionNo = sectionMeta.get(_referencePrefix_);
        strQuestionNo = CommonStringFunctions.makeReferenceFriendlyString(strQuestionNo);
        String newRefNo  = _referencePrefix_ + _referenceNameSeparator_ + strQuestionNo;
        int i = 1;
        while (ooDocument.getReferenceMarks().hasByName(newRefNo) ) {
            newRefNo = _referencePrefix_ + _referenceNameSeparator_ + strQuestionNo + "_"+i;
            i++;
        }
        subAction.setActionValue(newRefNo);
        //chain the routerCreateReference to this.. 
        routerCreateReference rcf = new routerCreateReference();
        BungeniValidatorState bvState = rcf.route_TextSelectedInsert(action, subAction, pFrame, ooDocument);
        return bvState; 
    }
}
