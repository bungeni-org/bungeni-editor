/*
 * routerCreateSection.java
 *
 * Created on March 11, 2008, 12:54 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.routers;

import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNamed;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextSection;
import com.sun.star.text.XTextViewCursor;
import java.util.HashMap;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.editor.document.DocumentSection;
import org.bungeni.editor.document.DocumentSectionsContainer;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;
import org.bungeni.ooo.utils.CommonExceptionUtils;

/**
 *
 * @author Administrator
 */
public class routerCreateObservation extends defaultRouter {
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerCreateObservation.class.getName());
 
    public String nameOfNewSection = "";
    
    /** Creates a new instance of routerCreateSection */
    public routerCreateObservation() {
        super();
        
    }
    
    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
     String newSectionName = "";
     routerApplyStyle ras = new routerApplyStyle();
     subAction.setActionValue("observation");
     ras.route_TextSelectedInsert(action, subAction, pFrame, ooDocument);
     
     newSectionName = get_newSectionNameForAction(action, ooDocument);
         if (newSectionName.length() == 0 ) {
             
         } else {
            boolean bAction = action_createSystemContainerFromSelection(ooDocument, newSectionName);
            if (bAction ) {
                //set section type metadata
                setSectionProperties(action, newSectionName, ooDocument);
                ooDocument.setSectionMetadataAttributes(newSectionName, get_newSectionMetadata(action));
            } else {
                log.error("routeAction_TextSelectedInsertAction_CreateSection: error while creating section ");
                return new BungeniValidatorState(true, new BungeniMsg("FAILURE_CREATE_SECTION"));
            }
         }      
      return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

    
    
    /**** 
     *
     * private APIs for this action 
     *
     ****/
 private String get_newSectionNameForAction(toolbarAction pAction, OOComponentHelper ooDocument) {
         String newSectionName = "";
         if (pAction.action_numbering_convention().equals("single")) {
             newSectionName = pAction.action_naming_convention();
         } else if (pAction.action_numbering_convention().equals("serial")) {
             String sectionPrefix = pAction.action_naming_convention();
             for (int i=1 ; ; i++) {
                if (ooDocument.hasSection(sectionPrefix+i)) {
                    continue;
                } else {
                    newSectionName = sectionPrefix+i;
                    break;
                }
             }
           
         } else {
             log.error("get_newSectionNameForAction: invalid action naming convention: "+ pAction.action_naming_convention());
         }
         this.nameOfNewSection = newSectionName;
         return newSectionName;
    }

 private boolean action_createSystemContainerFromSelection(OOComponentHelper ooDoc, String systemContainerName){
        boolean bResult = false; 
        try {
        XTextViewCursor xCursor = ooDoc.getViewCursor();
        XText xText = xCursor.getText();
        XTextContent xSectionContent = ooDoc.createTextSection(systemContainerName, (short)1);
        xText.insertTextContent(xCursor, xSectionContent , true); 
        bResult = true;
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            bResult = false;
            log.error("in addTextSection : "+ex.getLocalizedMessage(), ex);
        }  finally {
            return bResult;
        }
    }

     private HashMap<String,String> get_newSectionMetadata(toolbarAction pAction) {
         HashMap<String,String> metaMap = new HashMap<String,String>();
         metaMap.put("BungeniSectionType", pAction.action_section_type());
         return metaMap;
     }

    private void setSectionProperties(toolbarAction pAction, String newSectionName, OOComponentHelper ooDocument) {
        String sectionType = pAction.action_section_type();
        DocumentSection secObj = DocumentSectionsContainer.getDocumentSectionByType(sectionType);
        HashMap<String,Object> sectionProps = secObj.getSectionProperties();
        XTextSection newSection = ooDocument.getSection(newSectionName);
        XNamed namedSection = ooQueryInterface.XNamed(newSection);
       
        XPropertySet xProps = ooQueryInterface.XPropertySet(newSection);
       /*
        xProps.setPropertyValue("BackColor", new Integer(16711680));
                            xProps.setPropertyValue("SectionLeftMargin", new Integer(762));
    */
        for (String propName: sectionProps.keySet()) {
             try {
                //Long margin = new Long(762);
               
                //Integer i = new Integer(0);
                
                log.debug("setSectionProperties : "+ propName + " value = " + sectionProps.get(propName).toString());
                
                //xProps.setPropertyValue(propName, sectionProps.get(propName));
                Object propVal = sectionProps.get(propName);
                if (propVal.getClass() == java.lang.Integer.class) {
                      xProps.setPropertyValue(propName, (java.lang.Integer) sectionProps.get(propName));
                } else if (propVal.getClass() == java.lang.Long.class) {
                      xProps.setPropertyValue(propName, (java.lang.Long) sectionProps.get(propName));               
                } else if (propVal.getClass() == java.lang.String.class) {
                      xProps.setPropertyValue(propName, (java.lang.String) sectionProps.get(propName));
                } else
                      xProps.setPropertyValue(propName, (java.lang.String) sectionProps.get(propName));
            } catch (Exception ex) {
                log.error("setSectionProperties :"+ propName +" : "  +ex.getMessage());
                log.error("setSectionProperties :"+ CommonExceptionUtils.getStackTrace(ex));
            } 
        }
    }
}
