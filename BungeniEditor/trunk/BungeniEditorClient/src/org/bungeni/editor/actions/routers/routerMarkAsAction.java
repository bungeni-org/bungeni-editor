
package org.bungeni.editor.actions.routers;

import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextSection;
import java.util.HashMap;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public class routerMarkAsAction extends defaultRouter {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerMarkAsAction.class.getName());
 
    XTextRange beginRange = null, endRange = null;
    String fromRangeName = "", toRangeName = "", foundSectionType = "";
    /** Creates a new instance of routerCreateSection */
    public routerMarkAsAction() {
        super();
        
    }
    
    String __ACTION_EVENT_SECTION_TYPE__ = "ActionEvent";
    String __ACTION_EVENT_ATTR_NAME__ = "BungeniActionEvent";
    HashMap<String,String> actionEventMetadata = new HashMap<String,String>();

    // !+ACTION_RECONF (rm, jan 2012) - removed toolbarAction as var, class
    // toolbarAction is deprecated
    @Override
    // public BungeniValidatorState route_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame,OOComponentHelper ooDocument) {
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction, javax.swing.JFrame pFrame,OOComponentHelper ooDocument) {
        BungeniValidatorState bvsRet = new BungeniValidatorState(true, new BungeniMsg("FAILURE"));
        try {
        String actionEventName = subAction.action_value();
        String sectionType = __ACTION_EVENT_SECTION_TYPE__;
        routerCreateSection rcs = new routerCreateSection();
        //create the new section
        
        BungeniValidatorState bvs = rcs.route_TextSelectedInsert(subAction, pFrame, ooDocument);
        // BungeniValidatorState bvs = rcs.route_TextSelectedInsert(action, subAction, pFrame, ooDocument);

        if (bvs.state) {
            //rcs.nameOfNewSection;
            XTextSection newSection = ooDocument.getSection(rcs.nameOfNewSection);
            actionEventMetadata.put(__ACTION_EVENT_ATTR_NAME__ , actionEventName);
            ooDocument.setSectionMetadataAttributes(newSection, actionEventMetadata);
            bvsRet = new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
        }
        } catch (Exception ex) {
            bvsRet = new BungeniValidatorState(true, new BungeniMsg("FAILURE"));
            log.error("routerMarkAsAction_textSelectedInsert : " + ex.getMessage());
        } finally {
            return bvsRet;
        }
     /* boolean bState = ooDocument.setSelectedTextStyle(styleName);
      if (bState)
          return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
      else    
          return new BungeniValidatorState(true, new BungeniMsg("APPLY_STYLE_FAILURE")); */

    }

   
   
    
  


    
   
}
