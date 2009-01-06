/*
 * routerCreateNumberedHeading.java
 *
 * Created on May 26, 2008, 2:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.routers;

import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextViewCursor;
import java.util.HashMap;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.editor.document.DocumentSection;
import org.bungeni.editor.document.DocumentSectionsContainer;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.editor.numbering.ooo.OOoNumberingHelper;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.utils.BungeniUUID;

/**
 *
 * @author Administrator
 */
public class routerCreateNumberedHeading extends defaultRouter {
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerCreateSection.class.getName());
      
    /** Creates a new instance of routerCreateNumberedHeading */
    public routerCreateNumberedHeading() {
    }
 
    private static final String NUMBERED_SECTION_TYPE = OOoNumberingHelper.NUMBERING_SECTION_TYPE;
    /*
     *This router action marks a heading as a numbered heading and write protects it after applying the associated style
     * apply_numbered_heading:style_name
     */
    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
      String sectionUUID = BungeniUUID.getStringUUID();
      //get the section properties for the numbered container type
      DocumentSection numberedSection = DocumentSectionsContainer.getDocumentSectionByType(NUMBERED_SECTION_TYPE);
      String numberedSectionName = numberedSection.getSectionNamePrefix() + sectionUUID;
      String applyThisStyle = subAction.action_value();
      if (action_markSelectionAsNumbered(ooDocument, numberedSection,numberedSectionName, applyThisStyle, sectionUUID)) {
          
      }
      return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

    private static boolean createSection(OOComponentHelper ooDoc, String sectionName) {
      boolean bRet = false;
        try {
        XTextViewCursor xCursor = ooDoc.getViewCursor();
        XText xText = xCursor.getText();
        XTextContent xSectionContent = ooDoc.createTextSection(sectionName, (short)1);
        xText.insertTextContent(xCursor, xSectionContent , true); 
        bRet = true;
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error("createSection (IllegalArgumentException) " + ex.getMessage());
            bRet = false;
        } finally {
            return bRet;
        }
        
    }
    
    private static void applyStyleToSection(OOComponentHelper ooDoc, String applyStyle) {
        ooDoc.setSelectedTextStyle(applyStyle);
    }
    
    private static void applySectionMetadata(OOComponentHelper ooDoc, DocumentSection aSection, String sectionName, String sectionUUID) {
        HashMap<String,String> sectionMeta = new HashMap<String,String>();
        sectionMeta.put("BungeniSectionType", NUMBERED_SECTION_TYPE);
        sectionMeta.put("BungeniSectionVisibility", aSection.getSectionVisibilty());
        sectionMeta.put("BungeniSectionUUID", sectionUUID);
        ooDoc.setSectionMetadataAttributes(sectionName, sectionMeta);
    }
    
    public static boolean action_markSelectionAsNumbered(OOComponentHelper ooDoc, DocumentSection aSection, String sectionName, String applyStyle, String sectionUUID ){
        boolean bResult = false; 
        try {
        //first insert the section
         if (createSection(ooDoc, sectionName)) {
        //now apply the heading style to the section content
            applyStyleToSection(ooDoc, applyStyle);
        //now apply the section metadata
            applySectionMetadata(ooDoc, aSection, sectionName, sectionUUID);
         //now protect the seciton
            ooDoc.protectSection(sectionName, true);
            bResult = true;
         }
        } catch (Exception ex) {
            bResult = false;
            log.error("in addTextSection : "+ex.getLocalizedMessage(), ex);
        }  finally {
            return bResult;
        }
    }
}
