
package org.bungeni.editor.actions.routers;


import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 * Applys a character style to a block of selected text
 * @author Ashok Hariharan
 */
public class routerApplyCharStyle extends defaultRouter {
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerApplyCharStyle.class.getName());
 

    /** Creates a new instance of routerCreateSection */
    public routerApplyCharStyle() {
        super();
        
    }
    
    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame,OOComponentHelper ooDocument) {
      String styleName = subAction.action_value();
      boolean bState = ooDocument.setSelectedTextCharStyle(styleName);
      if (bState)
          return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
      else    
          return new BungeniValidatorState(true, new BungeniMsg("APPLY_STYLE_FAILURE")); 
    }

    
    
    /**** 
     *
     * private APIs for this action 
     *
     ****/

}
