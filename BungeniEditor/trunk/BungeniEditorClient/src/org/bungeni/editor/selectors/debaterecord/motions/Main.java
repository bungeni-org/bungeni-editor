/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.selectors.debaterecord.motions;

import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNamed;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextSection;
import com.sun.star.text.XTextViewCursor;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import org.bungeni.editor.document.DocumentSection;
import org.bungeni.editor.document.DocumentSectionsContainer;
import org.bungeni.editor.selectors.BaseMetadataContainerPanel;
import org.bungeni.ooo.ooQueryInterface;
import org.bungeni.ooo.utils.CommonExceptionUtils;

/**
 *
 * @author undesa
 */
public class Main extends BaseMetadataContainerPanel {
    public HashMap<String, String> selectionData = new HashMap<String,String>();
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Main.class.getName());
    public String mainSectionName = "";
    public Main(){
        super();
    }
    
    @Override
    protected void setupPanels() {
       m_allPanels = new ArrayList<panelInfo>(){
                {
                    add(new panelInfo("MotionSelect", "org.bungeni.editor.selectors.debaterecord.motions.MotionSelect"));
                    add(new panelInfo("MotionTitle", "org.bungeni.editor.selectors.debaterecord.motions.MotionTitle"));
                    add(new panelInfo("MotionNameAndURI", "org.bungeni.editor.selectors.debaterecord.motions.MotionNameAndURI"));
                    add(new panelInfo("MotionText", "org.bungeni.editor.selectors.debaterecord.motions.MotionText"));
                }
        };
    
       m_activePanels = new ArrayList<panelInfo>(){
            {
                    add(new panelInfo("MotionSelect", "org.bungeni.editor.selectors.debaterecord.motions.MotionSelect"));
                    add(new panelInfo("MotionTitle", "org.bungeni.editor.selectors.debaterecord.motions.MotionTitle"));
                    add(new panelInfo("MotionNameAndURI", "org.bungeni.editor.selectors.debaterecord.motions.MotionNameAndURI"));
                    add(new panelInfo("MotionText", "org.bungeni.editor.selectors.debaterecord.motions.MotionText"));
            }
         };
    }

     public static void main(String[] args){
        Main m = new Main();
        m.initialize();
        JFrame f = new JFrame("MastHead title");
        f.add(m);
        f.pack();
        f.setVisible(true);
    }

    @Override
    public Component getPanelComponent() {
        return this;
    }
    
    @Override
    public void updateAllPanels(){
        for (panelInfo p : m_activePanels) {
            p.getPanelObject().doUpdateEvent();
        }
    }
    
    private final short SECTION_COLUMNS = 1;
    private String m_motionSectionName = "";
    @Override
    public boolean preApplySelectedInsert(){
        makeMetaEditable();
        
        //create the section if it doesnt exist over here...
        String newSection = getActionSectionName();
        if (!ooDocument.hasSection(newSection)) {
            m_motionSectionName = newSection;
            //creat ethe new section
            XTextViewCursor xCursor = ooDocument.getViewCursor();
            XText xText = xCursor.getText();
            XTextContent xSectionContent = ooDocument.createTextSection(newSection, SECTION_COLUMNS);
            try {
            xText.insertTextContent(xCursor, xSectionContent , true); 
            } catch (com.sun.star.lang.IllegalArgumentException ex) {
                log.error("IllegalArgumentException, preMainApply : " + ex.getMessage());
            }
            mainSectionName = newSection;
            //get section poperties
            XTextSection xSection = ooDocument.getSection(newSection);
            XNamed namedSection = ooQueryInterface.XNamed(xSection);
            XPropertySet xProps = ooQueryInterface.XPropertySet(xSection);
            //container section was created here ...
             String sectionType = theAction.action_section_type();
             DocumentSection secObj = DocumentSectionsContainer.getDocumentSectionByType(sectionType);
             HashMap<String,Object> sectionProps = secObj.getSectionProperties();
             //update properties...
             for (String propName: sectionProps.keySet()) {
             try {
                    //log.debug("setSectionProperties : "+ propName + " value = " + sectionProps.get(propName).toString());
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
            HashMap<String,String> metaMap = new HashMap<String,String>();
            metaMap.put("BungeniSectionType", theAction.action_section_type());
            ooDocument.setSectionMetadataAttributes(xSection, metaMap);
            // ooDocument.c
        }
        return true;
    }
    
        @Override
    public boolean postApplySelectedInsert(){
                if (sectionMetadataEditor != null ) {
            if (sectionMetadataEditor.bMetadataEditable) {
                if (m_motionSectionName.length() > 0 ) {
                    if (!sectionMetadataEditor.hasMetadataEditableFlag(ooDocument, m_motionSectionName))
                        sectionMetadataEditor.setMetadataEditableFlag(ooDocument, m_motionSectionName);
                    else
                        sectionMetadataEditor.setMetadataEditableFlag(ooDocument, m_motionSectionName);
                }
            }
        }
        return true;
    }
    

    @SuppressWarnings("empty-statement")
        public String getActionSectionName() {
        //get the action naming convention
        String numberingConvention = theAction.action_numbering_convention();
        if (numberingConvention.equals("none") || numberingConvention.equals("single")) {
            return theAction.action_naming_convention();
        } else if (numberingConvention.equals("serial")) {
            //get highest section name possible
            int iStart = 1;
            for (; ooDocument.hasSection(theAction.action_naming_convention()+iStart); iStart++ );
            return theAction.action_naming_convention()+iStart; 
        } else
            return theAction.action_naming_convention();
    }
}
