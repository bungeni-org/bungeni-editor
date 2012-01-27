
package org.bungeni.editor.actions.routers;


import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 * Applys a paragraph style to a block of selected text
 * @author Ashok Hariharan
 */
public class routerApplyStyle extends defaultRouter {
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerApplyStyle.class.getName());
 

    /** Creates a new instance of routerCreateSection */
    public routerApplyStyle() {
        super();
        
    }

    //!+ACTION_RECONF (rm, jan 2012) - removing toolbarAction var, deprecated
    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction, javax.swing.JFrame pFrame,OOComponentHelper ooDocument) {
      String styleName = subAction.action_value();
      boolean bState = ooDocument.setSelectedTextParaStyle(styleName);
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
