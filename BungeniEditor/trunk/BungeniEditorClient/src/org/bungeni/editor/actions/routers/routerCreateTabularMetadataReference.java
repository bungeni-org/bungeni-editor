
package org.bungeni.editor.actions.routers;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.editor.actions.toolbarActionDeprecated;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Ashok Hariharan
 */
public class routerCreateTabularMetadataReference extends defaultRouter {
 private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerCreateTabularMetadataReference.class.getName());
 

    /** Creates a new instance of routerCreateSection */
    public routerCreateTabularMetadataReference() {
        super();
    }
    
   

    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
       CommonRouterActions.displaySubActionDialog(subAction, pFrame, ooDocument, true);

       return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

}
