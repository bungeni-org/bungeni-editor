package org.bungeni.editor.selectors.debaterecord.question;

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

    @Override
    protected void setupPanels() {
       m_allPanels = new ArrayList<panelInfo>(){
                {
                    add(new panelInfo("QuestionSelect", "org.bungeni.editor.selectors.debaterecord.question.QuestionSelect"));
                    add(new panelInfo("QuestionTitle", "org.bungeni.editor.selectors.debaterecord.question.QuestionTitle"));
                    add(new panelInfo("QuestionNameAndURI", "org.bungeni.editor.selectors.debaterecord.question.QuestionNameAndURI"));
                    add(new panelInfo("QuestionAddressedTo","org.bungeni.editor.selectors.debaterecord.question.QuestionAddressedTo"));
                    add(new panelInfo("QuestionText", "org.bungeni.editor.selectors.debaterecord.question.QuestionText"));
                }
        };

       m_activePanels = new ArrayList<panelInfo>(){
            {
                    add(new panelInfo("QuestionSelect", "org.bungeni.editor.selectors.debaterecord.question.QuestionSelect"));
                    add(new panelInfo("QuestionTitle", "org.bungeni.editor.selectors.debaterecord.question.QuestionTitle"));
                    add(new panelInfo("QuestionerNameAndURI", "org.bungeni.editor.selectors.debaterecord.question.QuestionerNameAndURI"));
                    add(new panelInfo("QuestionAddressedTo","org.bungeni.editor.selectors.debaterecord.question.QuestionAddressedTo"));
                    add(new panelInfo("QuestionText", "org.bungeni.editor.selectors.debaterecord.question.QuestionText"));
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
        try {
        for (panelInfo p : m_activePanels) {
            p.getPanelObject().doUpdateEvent();
        }
        } catch (Exception ex) {
            log.error("updateAllPanels : " + ex.getMessage());
        }
    }

    private final short SECTION_COLUMNS = 1;
    private String m_questionSectionName = "";

    @Override
    public boolean preApplySelectedInsert(){
        //
        makeMetaEditable();
        //create the section if it doesnt exist over here...

        String newSection = getActionSectionName();
        if (!ooDocument.hasSection(newSection)) {
            //creat ethe new section
            m_questionSectionName = newSection;
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

           }
        return true;
    }

    @Override
    public boolean postApplySelectedInsert(){
                if (sectionMetadataEditor != null ) {
            if (sectionMetadataEditor.bMetadataEditable) {
                if (m_questionSectionName.length() > 0 ) {
                    if (!sectionMetadataEditor.hasMetadataEditableFlag(ooDocument, m_questionSectionName))
                        sectionMetadataEditor.setMetadataEditableFlag(ooDocument, m_questionSectionName);
                    else
                        sectionMetadataEditor.setMetadataEditableFlag(ooDocument, m_questionSectionName);
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
