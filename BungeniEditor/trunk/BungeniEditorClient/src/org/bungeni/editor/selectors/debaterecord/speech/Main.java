package org.bungeni.editor.selectors.debaterecord.speech;

import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextSection;
import com.sun.star.text.XTextViewCursor;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import org.bungeni.editor.actions.routers.CommonRouterActions;
import org.bungeni.editor.selectors.BaseMetadataContainerPanel;

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
    
      public static void main(String[] args){
        Main m = new Main();
        JFrame f = new JFrame("MastHead title");
        f.add(m);
        f.pack();
        f.setVisible(true);
    }
      
    @Override
    protected void setupPanels() {
       m_allPanels = new ArrayList<panelInfo>(){
                {
                    add(new panelInfo("PersonSelector","org.bungeni.editor.selectors.debaterecord.speech.PersonSelector"));
                    add(new panelInfo("PersonURI", "org.bungeni.editor.selectors.debaterecord.speech.PersonURI"));
                    add(new panelInfo("SpeechBy", "org.bungeni.editor.selectors.debaterecord.speech.SpeechBy"));
                }
        };
    
       m_activePanels = new ArrayList<panelInfo>(){
            {
                    add(new panelInfo("PersonSelector","org.bungeni.editor.selectors.debaterecord.speech.PersonSelector"));
                    add(new panelInfo("PersonURI", "org.bungeni.editor.selectors.debaterecord.speech.PersonURI"));
                    add(new panelInfo("SpeechBy", "org.bungeni.editor.selectors.debaterecord.speech.SpeechBy"));
            }
         };
    }

    @Override
    public Component getPanelComponent() {
       return this;
    }
    
     
    
     private final short SECTION_COLUMNS = 1;
    @Override
    public boolean preApplySelectedInsert(){
        //create the section if it doesnt exist over here...
        makeMetaEditable();
        
        String newSection = getActionSectionName();
        if (!ooDocument.hasSection(newSection)) {
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
            CommonRouterActions.setSectionProperties(theAction, newSection, ooDocument);
             ooDocument.setSectionMetadataAttributes(xSection, CommonRouterActions.get_newSectionMetadata(theAction));

            /*
             *
            XNamed namedSection = ooQueryInterface.XNamed(xSection);
            XPropertySet xProps = ooQueryInterface.XPropertySet(xSection);
            //container section was created here ...
             String sectionType = theAction.action_section_type();
             DocumentSection secObj = DocumentSectionsContainer.getDocumentSectionByType(sectionType);
             HashMap<String,Object> sectionProps = secObj.getSectionProperties(ooDocument);
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
            ooDocument.setSectionMetadataAttributes(xSection, metaMap); */
            // ooDocument.c
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

    
     @Override
    public boolean postApplySelectedInsert(){
                if (sectionMetadataEditor != null ) {
            if (sectionMetadataEditor.bMetadataEditable) {
                if (mainSectionName.length() > 0 ) {
                    if (!sectionMetadataEditor.hasMetadataEditableFlag(ooDocument, mainSectionName))
                        sectionMetadataEditor.setMetadataEditableFlag(ooDocument, mainSectionName);
                    else
                        sectionMetadataEditor.setMetadataEditableFlag(ooDocument, mainSectionName);
                }
            }
        }
        return true;
    }
}
