/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.routers;

import com.sun.star.container.XNamed;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextViewCursor;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;
import org.bungeni.extutils.CommonDocumentUtilFunctions;

/**
 *
 * @author undesa
 */
public class routerCreateUniqueReference extends defaultRouter {
 private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerCreateUniqueReference.class.getName());
 

    /** Creates a new instance of routerCreateSection */
    public routerCreateUniqueReference() {
        super();
    }
    
    // !+ACTION_RECONF (rm, jan 2012) - removed toolbarAction as var, class
    // toolbarAction is deprecated
    @Override
    // public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
    public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        BungeniValidatorState bRet = new BungeniValidatorState(true, new BungeniMsg("FAILURE"));
        try {
        String uniqueReferenceName = CommonDocumentUtilFunctions.getUniqueReferenceName(subAction.action_value()+ ":", ooDocument);
        routerCreateReference rcf = new routerCreateReference();
        subAction.setActionValue(uniqueReferenceName);
        
        BungeniValidatorState bvs = rcf.route_TextSelectedInsert(subAction, pFrame, ooDocument);
        // BungeniValidatorState bvs = rcf.route_TextSelectedInsert(action, subAction, pFrame, ooDocument);

        bRet = bvs;
        } catch (Exception ex) {
            log.error("routerCreateUniqueReference : route_TextSelectedInsert : " + ex.getMessage());
        } finally {
            return bRet;
        }
        //subAction value has the reference name
       //this.insertRefMark(ooDocument, xCursor, subAction.action_value());
    }

}
