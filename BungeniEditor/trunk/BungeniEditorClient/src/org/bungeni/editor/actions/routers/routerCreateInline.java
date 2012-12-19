
package org.bungeni.editor.actions.routers;



import java.util.HashMap;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarAction.actionSourceOrigin;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 * Applys a character style to a block of selected text
 * @author Ashok Hariharan
 */
public class routerCreateInline extends defaultRouter {
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerCreateInline.class.getName());
 

    /** Creates a new instance of routerCreateSection */
    public routerCreateInline() {
        super();
        
    }

    @Override
    public BungeniValidatorState route_TextSelectedInsert( toolbarAction subAction, javax.swing.JFrame pFrame,OOComponentHelper ooDocument) {
        // check if the action supports an inline Type markup
        boolean bState = false ; 
        if (subAction.getActionSource().equals(actionSourceOrigin.inlineType)) {
          String inlineTypeName = subAction.getInlineType();
          HashMap<String,String> inlineTypeMap = new HashMap<String,String>();
          inlineTypeMap.put("BungeniInlineType", inlineTypeName);
          ooDocument.setSelectedTextAttributes(inlineTypeMap);
          bState = true;
        }
      if (bState) {
            return new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
        } 
      else {
            return new BungeniValidatorState(true, new BungeniMsg("FAILURE"));
        } 
    }

}
