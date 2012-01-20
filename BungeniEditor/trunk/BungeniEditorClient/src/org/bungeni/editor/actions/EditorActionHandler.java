
package org.bungeni.editor.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.apache.log4j.Logger;
import org.bungeni.editor.selectors.SelectSectionEdit;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.extutils.MessageBox;

/**
 *
 *  Note : Some Handlers disable on AH-10-03-11
 * @author Ashok Hariharan
 */
//#############################################
//      !+ACTION_RECONF (rm, jan 20121)       #
//              WARNING!                      #
// THIS CLASS HAS BEEN DEPRECATED, ALL UNIQUE #
// METHODS WITHIN IT HAVE BEEN IMPLEMENTED IN #
// EDITORSELECTIONACTIONHANDLER.JAVA          #
//#############################################


public class EditorActionHandler implements IEditorActionEvent {
     private static org.apache.log4j.Logger log = Logger.getLogger(EditorActionHandler.class.getName());
     private OOComponentHelper ooDocument;
    /** Creates a new instance of EditorActionHandler */
    public EditorActionHandler() {
    }

    public void doCommand(OOComponentHelper ooDocument, toolbarAction action, JFrame parentFrame) {
        //main action handler class 
        //can be implemented by any class that implements IEditorActionEvent]
        this.ooDocument = ooDocument;
        String currentSection = ooDocument.currentSectionName();
        String cmd = action.action_name();
        if (action.action_type().equals("section")) {
            if (action.getSelectorDialogMode() == SelectorDialogModes.TEXT_EDIT) {
                log.debug("XXXXXX -- EDIT MODE DISABLED !!!! XXXXXX");
                //AH-10-03-11
                /***
                //edit mode checks
                //check if an editable section exists..., find all editable sections
                int nCheckSection = 0;
                ArrayList<String> editableSections = new ArrayList<String>();
                editableSections = checkEditableSections(action);
                if (editableSections.size() == 0 ) {
                      MessageBox.OK(null, "An editable section of the type : " + action.action_naming_convention() + " was not found");
                        return;
                }
                Iterator<String> itr  = editableSections.iterator();
                while (itr.hasNext() ) {
                   log.debug("doCommand iterate = "+  itr.next());
                }
                JDialog dlgSelect;
                dlgSelect = SelectSectionEdit.Launch(editableSections);
                SelectSectionEdit objPanel = (SelectSectionEdit)dlgSelect.getContentPane().getComponent(0);
                if (objPanel.isCancelClicked())
                        return;
                String selSection = objPanel.getSelectedSection();
                action.setSelectedActionToActUpon(selSection);
                 *****/
            }
            if (action.getSelectorDialogMode() == SelectorDialogModes.TEXT_INSERTION) {
                //first we check if section can indeed be inserted, make the neccessary checks
                //AH-10-03-11
                /*****
                int nCheckSection = 0;
                nCheckSection = checkSection(action);
                if (nCheckSection < 0) {
                    //failure occured.
                    if (nCheckSection == -1){
                        MessageBox.OK(null, "Only one instance of the section : " + action.action_naming_convention() + " can be added !" +
                                " \n The section already exists");
                        return;
                    }
                   
                    if (nCheckSection == -2) {
                        MessageBox.OK(null, "There is already a section of this type in the document, creating it again \n will replace the contents of the currently available  section");
                    }
                }
                
                action.setSelectedActionToActUpon(currentSection);
                action.setSelectedSectionActionCommand("INSIDE_SECTION");
                *******/
             }
        }
        
        log.debug("doCommand executed : "+ cmd);

    }
    
    // -1 returns error for section already existing
    // -2 returns error for section 
     private int checkSection(toolbarAction action ) {

         /****
          * AH-10-03-11
         //first check if its a markup section or section
         if (action.action_type().equals(toolbarAction.ACTION_TYPE_MARKUP)) {
             return 0;
         } else if (action.action_type().equals(toolbarAction.ACTION_TYPE_SECTION)) {
             //first check if the section is of type single...
             if (action.action_numbering_convention().equals("single")) {
                 //only one instance of this section is allowed.
                 //check if section is editable.. if it isnt then we must allow creation of new section that
                 //deletes the previous instance
                 if (ooDocument.hasSection(action.action_naming_convention())) {
                     if (action.action_edit_dlg_allowed() == 0)
                         return -2 ; //can be recreated
                     else
                        return -1;
                 }
             }
             
         }
          ****/
         return 0;
     }
     
     private ArrayList<String> checkEditableSections(toolbarAction action) {

         ArrayList<String> validSections = new ArrayList<String>();
         //AH-10-03-11 
         /*****
         String sectionType = action.action_section_type().trim();
         String[] sections = new String[ooDocument.getTextSections().getElementNames().length];
       
         String validSection = "";
         sections = ooDocument.getTextSections().getElementNames();
         //look for sections of the same type
         for (int i=0; i < sections.length ; i++ ) {
              HashMap<String,String> thisSectionMetadata = ooDocument.getSectionMetadataAttributes(sections[i]);
              log.debug("checkEditableSections: current section iterate : " + sections[i] + ", for sectionType :" + sectionType);
              if (thisSectionMetadata.size() > 0 ) {
                    String curSectionType = thisSectionMetadata.get("BungeniSectionType").trim();
                    log.debug("checkEditableSections: iterate:" + sections[i] + " type = " + curSectionType);
                    if (curSectionType.equals(sectionType)) {
                        log.debug("checkEditableSections: equation success");
                       if (!sections[i].startsWith("int:"))
                            validSections.add(sections[i]);
                    } else log.debug("checkEditableSections: equation failed : "+ sections[i]);
              }
         }
          *****/
         return validSections;
     }
     
    public void doCommand(OOComponentHelper ooDocument, toolbarSubAction action, JFrame parentFrame) {
    }

    public void doCommand(OOComponentHelper ooDocument, ArrayList<String> action, JFrame parentFrame) {
    }

}
