
package org.bungeni.editor.actions.routers;

import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public class routerCreateHeadingUnlock extends defaultRouter {
  
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerCreateHeadingUnlock.class.getName());
 

    /** Creates a new instance of routerCreateSection */
    public routerCreateHeadingUnlock() {
        super();
        
    }
    // !+ACTION_RECONF (rm, jan 2012) - removed toolbarAction as var, class
    // toolbarAction is deprecated
    @Override
    //public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame,OOComponentHelper ooDocument) {
    public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction subAction, javax.swing.JFrame pFrame,OOComponentHelper ooDocument) {
      String styleName = subAction.action_value();
     /* boolean bState = ooDocument.setSelectedTextStyle(styleName);
      if (bState)
          return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
      else    
          return new BungeniValidatorState(true, new BungeniMsg("APPLY_STYLE_FAILURE")); */
        return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

}
