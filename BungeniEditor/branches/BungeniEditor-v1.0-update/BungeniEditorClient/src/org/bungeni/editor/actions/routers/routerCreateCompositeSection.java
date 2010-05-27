/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.routers;

import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.beans.XPropertySetInfo;
import com.sun.star.container.XIndexAccess;
import com.sun.star.container.XNamed;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.text.XParagraphCursor;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextSection;
import com.sun.star.uno.Any;
import java.util.HashMap;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.editor.document.DocumentSection;
import org.bungeni.editor.document.DocumentSectionsContainer;
import org.bungeni.editor.numbering.ooo.OOoNumberingHelper;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;
import org.bungeni.extutils.BungeniUUID;

/**
 *
 * @author undesa
 */
public class routerCreateCompositeSection extends defaultRouter {
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerApplyStyle.class.getName());
   String applyHeadingStyle = "";
   String sectionUUID = "";
   String headingSectionName = "";
   private static final String NUMBERED_SECTION_TYPE = OOoNumberingHelper.NUMBERING_SECTION_TYPE;
    
   routerCreateCompositeSection(){
       super();
   }
   
    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        //get the UUID for the  new numbered heading 
        sectionUUID = BungeniUUID.getStringUUID();
          //get the section properties for the numbered container type
        DocumentSection numberedSection = DocumentSectionsContainer.getDocumentSectionByType(NUMBERED_SECTION_TYPE);
        headingSectionName =  numberedSection.getSectionNamePrefix() + sectionUUID;
        //create composite section here....
        if (ooDocument.isTextSelected()) {
            applyHeadingStyle = subAction.action_value(); 
            selectionProperties foundSelection = getSelectionProperties(ooDocument);
            //now create the main container section for the boundary
//            DocumentSection boundarySection = DocumentSectionsContainer.getDocumentSectionByType(action.action_section_type());
 //           String boundarySectionName = newSectionNameForType(boundarySection);
            if (action_createBoundarySection(ooDocument, action, foundSelection)) {
                log.debug("routerCreateCompositeSection: marking numbered heading");
                if (routerCreateNumberedHeading.action_markSelectionAsNumbered(ooDocument, numberedSection, headingSectionName, applyHeadingStyle, sectionUUID)) {
                    //now create the numbered heading section



               }
            }
            
            
            log.debug("routerCreateCompositeSection : boundarySection : " + foundSelection.boundarySectionName + " , found Section = "+ foundSelection.originSectionName);
            return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
        } 
        else return new BungeniValidatorState(false, new BungeniMsg("FAILURE"));
    }

    
     private String newSectionNameForType(OOComponentHelper ooDocument, DocumentSection newSection) {
         String newSectionName = "";
         
         if (newSection.getSectionNumberingStyle().equals("single")) {
             newSectionName = newSection.getSectionNamePrefix() ;
         } else 
         if (newSection.getSectionNumberingStyle().equals("serial")) {
             newSectionName = newSection.getSectionNamePrefix() ;
             for (int i=1 ; ; i++) {
                if (ooDocument.hasSection(newSectionName + i)) {
                    continue;
                } else {
                    newSectionName = newSectionName + i;
                    break;
                }
             }
         } else {
             log.error("get_newSectionNameForAction: invalid action naming convention: ");
         }
         return newSectionName;
    }
     
    
    private boolean action_createBoundarySection(OOComponentHelper ooDocument, toolbarAction action, selectionProperties foundSelection){
        boolean bState = false;
        try {

        //get properties for new section to create
        //the boundarySection object contains the template for the new section to be created
        DocumentSection newBoundingSection = DocumentSectionsContainer.getDocumentSectionByType(action.action_section_type());
        //get the name for the new section
        String newBoundingSectionName = this.newSectionNameForType(ooDocument, newBoundingSection);
        if (newBoundingSectionName.length() > 0 ) {
                //valid boundary section name ...
                //now create the section
                //create cursor for full range
                //starting point - beginning of selection range - end point : just before next section
                 XTextCursor rangeCursor = ooDocument.getTextDocument().getText().createTextCursor();
                //if the boundary section name is different we use the starting point
                if (foundSelection.originSectionName.equals(foundSelection.boundarySectionName)) {
                    //if the boundary section name is the same as the origin section name then its the
                    //container section, so we have to use the end point
                       rangeCursor.gotoRange(foundSelection.startRange, false);
                       XTextRange boundaryAnchor = foundSelection.boundarySection.getAnchor();
                       rangeCursor.gotoRange(boundaryAnchor.getEnd(), true);
                       rangeCursor.goLeft((short)1, true);
                } else {
                       rangeCursor.gotoRange(foundSelection.startRange, false);
                       XTextRange boundaryAnchor = foundSelection.boundarySection.getAnchor();
                       //select the boundary range
                       rangeCursor.gotoRange(boundaryAnchor.getStart(), true);
                       rangeCursor.goLeft((short) 1, true);
                }
             
                //now rangeCursor has the spanned range for creating a new section
                //ADD the new section
                log.debug("action_createBoundarySection : " + rangeCursor.getString());
               
                XTextContent xSectionContent = ooDocument.createTextSection(newBoundingSectionName, (short) 1);
                ooDocument.getTextDocument().getText().insertTextContent(rangeCursor, xSectionContent, true);
                CommonRouterActions.setSectionProperties(action, newBoundingSectionName, ooDocument);
                HashMap<String, String> metaMap = CommonRouterActions.get_newSectionMetadata(action);
                ooDocument.setSectionMetadataAttributes(newBoundingSectionName, metaMap);
                bState = true;
        }
            } catch (NullPointerException ex) {
                log.error("action_createBoundarySection : " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                log.error("action_createBoundarySection : " + ex.getMessage());
            } finally {
                return bState;
            }
    }
    
    /*
    private void setSectionMetadata(OOComponentHelper ooDocument, DocumentSection secObj, String newSectionName) {
         HashMap<String,String> metaMap = new HashMap<String,String>();
         metaMap.put("BungeniSectionType",secObj.getSectionType());
         ooDocument.setSectionMetadataAttributes(newSectionName, metaMap);
    }*/
    
    /*
    private void setSectionProperties(OOComponentHelper ooDocument, DocumentSection secObj, String newSectionName) {
        String sectionType = secObj.getSectionType();
        //DocumentSection secObj = DocumentSectionsContainer.getDocumentSectionByType(sectionType);
        HashMap<String,Object> sectionProps = secObj.getSectionProperties();
        XTextSection newSection = ooDocument.getSection(newSectionName);
        XNamed namedSection = ooQueryInterface.XNamed(newSection);
        XPropertySet xProps = ooQueryInterface.XPropertySet(newSection);

        for (String propName: sectionProps.keySet()) {
             try {
                log.debug("setSectionProperties : "+ propName + " value = " + sectionProps.get(propName).toString());
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
    */
    class selectionProperties {
        XTextRange startRange = null;
        XTextRange endRange = null;
        XTextSection originSection = null;
        String originSectionName = "";
        XTextSection originSectionParent = null;
        String originSectionParentName = "";
        XTextSection boundarySection = null;
        String boundarySectionName = ""; 
        
    }
    
    
    private selectionProperties getSelectionProperties (OOComponentHelper ooDoc) {
        Object selection = ooDoc.getCurrentSelection();
        selectionProperties currentSelection = new selectionProperties();
        XTextRange xSelectionRange = null;
        XTextRange selStartRange = null, selEndRange = null;
        XServiceInfo xSelInfo = ooQueryInterface.XServiceInfo(selection);
         if ( xSelInfo.supportsService("com.sun.star.text.TextRanges") ){
                XIndexAccess xIndexAccess = ooQueryInterface.XIndexAccess(selection);
                int count = xIndexAccess.getCount();
                try {
                    Object singleSelection;
                    singleSelection = xIndexAccess.getByIndex(0);
                    //get selection ranges
                    xSelectionRange = ooQueryInterface.XTextRange(singleSelection);
                    selStartRange = xSelectionRange.getStart();
                    selEndRange = xSelectionRange.getEnd();
                    currentSelection.startRange = selStartRange;
                    currentSelection.endRange = selEndRange;
                    //get selection section properties
                    currentSelection.originSection = getSectionInSelection(singleSelection);
                    if (currentSelection.originSection != null) {
                        XNamed xOrigName = ooQueryInterface.XNamed(currentSelection.originSection);
                        String sOriginalName = xOrigName.getName();
                        currentSelection.originSectionName = sOriginalName;
                        //get parent properties
                        XTextSection originParentSection = currentSelection.originSection.getParentSection();
                        if (originParentSection != null ) {
                                currentSelection.originSectionParent = originParentSection;
                                XNamed nameOriginParentSection = ooQueryInterface.XNamed(originParentSection);
                                currentSelection.originSectionParentName = nameOriginParentSection.getName();
                        }
                        //get the boundary selection for the found selection
                        getBoundarySelection(ooDoc, currentSelection);
                        processBoundarySelection(ooDoc, currentSelection);
                    }
                    //get the boundary selection
                    
                } catch (IndexOutOfBoundsException ex) {
                   log.error("getSelectionBoundaries : " + ex.getMessage());
                } catch (WrappedTargetException ex) {
                   log.error("getSelectionBoundaries : " + ex.getMessage());                
                }
            }
        return currentSelection;
    }
    
    private void processBoundarySelection(OOComponentHelper ooDoc, selectionProperties props) {
        
    }
    private XTextSection getSectionInSelection(Object selection) {
        XTextSection cursorInSection = null;
        XPropertySet objProps = ooQueryInterface.XPropertySet(selection);
        XPropertySetInfo objPropsInfo = objProps.getPropertySetInfo();
         if (objPropsInfo.hasPropertyByName("TextSection")) {
            try {
                XTextSection xConSection = (XTextSection) ((Any)objProps.getPropertyValue("TextSection")).getObject();
                if (xConSection != null) {
                    cursorInSection = xConSection;
                }
            } catch (UnknownPropertyException ex) {
               log.error("getSectionInSelection :  " + ex.getMessage());
            } catch (WrappedTargetException ex) {
               log.error("getSectionInSelection :  " + ex.getMessage());            }
         }
        return cursorInSection;
    }
    
    private void getBoundarySelection(OOComponentHelper ooDoc, selectionProperties currentSel){

        XTextCursor xCursor = ooDoc.getTextDocument().getText().createTextCursor();
        XParagraphCursor xParaCursor = ooQueryInterface.XParagraphCursor(xCursor);
        xParaCursor.gotoRange(currentSel.startRange, false);
        while (xParaCursor.gotoNextParagraph(false)) {
                XTextSection aSection = getSectionInSelection(xParaCursor);
                if (aSection != null ) {
                    XNamed aSectionName = ooQueryInterface.XNamed(aSection);
                    String strSectionName  = aSectionName.getName();
                    //do this fully
                    if (!currentSel.originSectionName.equals(strSectionName)) {
                        //check if it is the parent section
                       // XTextSection originParentSection = currentSel.originSection.getParentSection();
                       // XNamed nameOriginParentSection = ooQueryInterface.XNamed(originParentSection);
                       // String parentSectionName = nameOriginParentSection.getName();
                        //check if the found section is the parent section
                        if (currentSel.originSectionParentName.equals(strSectionName)) {
                            //boundary section is the origin section
                            currentSel.boundarySection = currentSel.originSection;
                            currentSel.boundarySectionName = currentSel.originSectionName;
                            break;
                        } else {
                            //boundary section is another section
                            currentSel.boundarySection = aSection;
                            currentSel.boundarySectionName = strSectionName;
                            break;
                        }
                    }
                }
        }

    }
}
