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

/**
 *
 * @author undesa
 */
public class routerCreateReference extends defaultRouter {
 private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerCreateReference.class.getName());
 

    /** Creates a new instance of routerCreateSection */
    public routerCreateReference() {
        super();
    }
    
   
    private void insertRefMark (OOComponentHelper ooHandle, XTextCursor thisCursor, String referenceName ) {
       Object referenceMark = ooHandle.createInstance("com.sun.star.text.ReferenceMark");
       XNamed xRefMark = ooQueryInterface.XNamed(referenceMark);
       xRefMark.setName(referenceName);
       XTextContent xContent = ooQueryInterface.XTextContent(xRefMark);
       try {
       thisCursor.getText().insertTextContent(thisCursor, xContent, true);
       } catch (Exception ex) {
           log.error("insertReferenceMark :" + ex.getMessage()); 
       }
   }

    // !+ ACTION_RECONF (rm, jan 2012) - removed toolbarAction as var, class
    // toolbarAction is deprecated
    @Override
    // public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
    public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
      XTextViewCursor xCursor = ooDocument.getViewCursor();
      //subAction value has the reference name
      this.insertRefMark(ooDocument, xCursor, subAction.action_value());
     return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

}
