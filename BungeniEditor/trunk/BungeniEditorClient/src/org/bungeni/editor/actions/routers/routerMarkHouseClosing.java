package org.bungeni.editor.actions.routers;

import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextSection;
import java.util.HashMap;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.extutils.CommonDocumentUtilFunctions;

/**
 *
 * @author undesa
 */
public class routerMarkHouseClosing extends defaultRouter {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerMarkHouseClosing.class.getName());
 
    XTextRange beginRange = null, endRange = null;
    String fromRangeName = "", toRangeName = "", foundSectionType = "";
    /** Creates a new instance of routerCreateSection */
    public routerMarkHouseClosing() {
        super();
        
    }
    
    String __ACTION_EVENT_SECTION_TYPE__ = "ActionEvent";
    String __ACTION_EVENT_ATTR_NAME__ = "BungeniActionEvent";
    String __REF_HOUSE_CLOSING_TIME__ = "BungeniHouseClosingTime";
    HashMap<String,String> actionEventMetadata = new HashMap<String,String>();

    // !+ACTION_RECONF (rm, jan 2012) - removed toolbarAction as var, class
    // toolbarAction is deprecated
    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction, javax.swing.JFrame pFrame,OOComponentHelper ooDocument) {
    // public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction subAction, javax.swing.JFrame pFrame,OOComponentHelper ooDocument) {
        BungeniValidatorState bvsRet = new BungeniValidatorState(true, new BungeniMsg("FAILURE"));
        try {
            //get current section 
            XTextSection xSection = ooDocument.currentSection();
            HashMap<String,String> metaMap = new HashMap<String,String>();
            if (xSection != null) {
                metaMap = ooDocument.getSectionMetadataAttributes(xSection);
                if (metaMap.containsKey("BungeniHouseRisingTime")){
                    //create reference directl
                    String sRefname = CommonDocumentUtilFunctions.getUniqueReferenceName(__REF_HOUSE_CLOSING_TIME__+":", ooDocument);
                    routerCreateReference rcf = new routerCreateReference();
                    subAction.setActionValue(sRefname);
                    // BungeniValidatorState bvs = rcf.route_TextSelectedInsert(action, subAction, pFrame, ooDocument);
                    BungeniValidatorState bvs = rcf.route_TextSelectedInsert(subAction, pFrame, ooDocument);
                    bvsRet = bvs;
                    
                } else {
                    //popup dialog to select house rising time and set section metadata... 
                    BungeniValidatorState stateObj = CommonRouterActions.displaySelectorDialog(subAction, pFrame, ooDocument);
                   // return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
                    bvsRet = stateObj;
                }
            } else {
                bvsRet = new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
            }
        } catch (Exception ex) {
            //bvsRet = new BungeniValidatorState(true, new BungeniMsg("FAILURE"));
            log.error("routerMarkHouseClosing_textSelectedInsert : " + ex.getMessage());
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
