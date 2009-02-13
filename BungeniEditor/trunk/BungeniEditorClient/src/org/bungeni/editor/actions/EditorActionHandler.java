/*
 * EditorActionHandler.java
 *
 * Created on August 20, 2007, 4:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.actions;

import com.sun.star.beans.PropertyValue;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextViewCursor;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.apache.log4j.Logger;
import org.bungeni.editor.selectors.DialogSelectorFactory;
import org.bungeni.editor.selectors.IDialogSelector;
import org.bungeni.editor.selectors.InitPapers;
import org.bungeni.editor.selectors.InitSpeech;
import org.bungeni.editor.selectors.SelectSectionEdit;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.utils.MessageBox;

/**
 *
 * @author Administrator
 */
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
            }
            if (action.getSelectorDialogMode() == SelectorDialogModes.TEXT_INSERTION) {
                //first we check if section can indeed be inserted, make the neccessary checks
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
            }
        }
        
        log.debug("doCommand executed : "+ cmd);
       // if (cmd.equals("makeMastHead"))
        if (cmd.equals ("makePrayerSection")) 
             doMakePrayerSection(action);
        //else if (cmd.equals ("makePrayerSection")) 
        //    doMakeSection(action);
        else if (cmd.equals("makeQASection"))
            doMakeQASection(action);
        else if (cmd.equals("makePaperSection"))
            doMakePaperSection(action);
        else if (cmd.equals("makeNoticeOfMotionSection"))
            doMakeNOMSection(action);
        else if (cmd.equals("makeQuestionBlockSection"))
            doMakeQuestionBlockSection(action);
        else if (cmd.equals("makeSpeechBlockSection"))
            doMakeSpeechBlockSection(action);
        else if (cmd.equals("makeSpeechMarkup"))
            doMakeSpeechMarkup(action);
        else if (cmd.equals("makePrayerMarkup"))
            doMarkup(action);
        else if (cmd.equals("makePaperMarkup"))
            doMarkup(action);
        else if (cmd.equals("makePaperDetailsMarkup"))
            doMarkup(action);
        else if (cmd.equals("makeNoticeOfMotionMarkup"))
            doMarkup(action);
        else if (cmd.equals("makeNoticeMarkup"))
            doMarkup(action);
        else if (cmd.equals("makeQuestionTitleMarkup"))
            doMarkup(action);
        else if (cmd.equals("makeQuestionTextMarkup"))
            doMarkup(action);
        else if (cmd.equals("makeQATitleMarkup"))
            doMarkup(action);
        else
            MessageBox.OK("the command action: "+cmd+" has not been implemented!");   
    }
    
    // -1 returns error for section already existing
    // -2 returns error for section 
     private int checkSection(toolbarAction action ) {
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
         return 0;
     }
     
     private ArrayList<String> checkEditableSections(toolbarAction action) {
         String sectionType = action.action_section_type().trim();
         String[] sections = new String[ooDocument.getTextSections().getElementNames().length];
         ArrayList<String> validSections = new ArrayList<String>();
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
         return validSections;
     }
     
         private void doMakePrayerSection(toolbarAction action) {
             JDialog dlg;
             dlg= new JDialog();
             dlg.setTitle(action.action_display_text());
             dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
             //initDebaterecord.setPreferredSize(new Dimension(420, 300));
             IDialogSelector panel = DialogSelectorFactory.getDialogClass("org.bungeni.editor.selectors.InitDebateRecord");
             panel.initObject(ooDocument, dlg, action, null);
             dlg.getContentPane().add(panel.getPanel());
             dlg.pack();
             dlg.setLocationRelativeTo(null);
             dlg.setVisible(true);
             dlg.setAlwaysOnTop(true);   
            // returnState = new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }
         
         /*
     private void doMakePrayerSection(toolbarAction action) {
      
            //section was added now prompt for dialog information
             JDialog initDebaterecord;
             initDebaterecord = new JDialog();
             initDebaterecord.setTitle("Enter Settings for Document");
             initDebaterecord.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
             //initDebaterecord.setPreferredSize(new Dimension(420, 300));
            
             InitDebateRecord panel = new InitDebateRecord(ooDocument, 
                     initDebaterecord, action);
             //panel.setDialogMode(SelectorDialogModes.TEXT_INSERTION);
             //panel.setBackground(new Color(255, 255, 153));
             //initDebaterecord.setTitle("Selection Mode");
             initDebaterecord.getContentPane().add(panel);
             initDebaterecord.pack();
             initDebaterecord.setLocationRelativeTo(null);
             initDebaterecord.setVisible(true);
             initDebaterecord.setAlwaysOnTop(true);
       
     }
          */
         
          private void doMakeQuestionBlockSection(toolbarAction action) {
             JDialog dlg;
             dlg= new JDialog();
             dlg.setTitle(action.action_display_text());
             dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
             //initDebaterecord.setPreferredSize(new Dimension(420, 300));
             IDialogSelector panel = DialogSelectorFactory.getDialogClass("org.bungeni.editor.selectors.InitQuestionBlock");
             panel.initObject(ooDocument, dlg, action, null);
             dlg.getContentPane().add(panel.getPanel());
             dlg.pack();
             dlg.setLocationRelativeTo(null);
             dlg.setVisible(true);
             dlg.setAlwaysOnTop(true);   
              
          }
          /*
         private void doMakeQuestionBlockSection(toolbarAction action) {
            //section was added now prompt for dialog information
             log.debug("makeQuestionBlockSection: invoke");
             JDialog makeQuestionBlock;
             makeQuestionBlock = new JDialog();
             makeQuestionBlock.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
             //initDebaterecord.setPreferredSize(new Dimension(420, 300));
             InitQuestionBlock panel = new InitQuestionBlock(ooDocument, 
                     makeQuestionBlock, action);;
             makeQuestionBlock.getContentPane().add(panel);
             makeQuestionBlock.setTitle(panel.getWindowTitle());
             makeQuestionBlock.pack();
             makeQuestionBlock.setLocationRelativeTo(null);
             makeQuestionBlock.setVisible(true);
             makeQuestionBlock.setAlwaysOnTop(true);   
     }
     */
     
     private void doMakeSpeechBlockSection(toolbarAction action) {
            //section was added now prompt for dialog information
             JDialog makeSpeechBlock;
             makeSpeechBlock = new JDialog();
             makeSpeechBlock.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
             //initDebaterecord.setPreferredSize(new Dimension(420, 300));
             InitSpeech panel = new InitSpeech(ooDocument,  makeSpeechBlock, action);
             if (panel.okToLaunch() == false) 
                 return;
             /*
              if (ooDocument.isTextSelected()) {
                panel.setDialogMode(SelectorDialogModes.TEXT_SELECTED);
                panel.setBackground(new Color(255, 255, 153));
                makeSpeechBlock.setTitle("Selection Mode");
              } else {
                panel.setDialogMode(SelectorDialogModes.TEXT_INSERTION);
                panel.setBackground(new Color(204, 255, 153));
                makeSpeechBlock.setTitle("Insertion Mode");
              }*/
             makeSpeechBlock.getContentPane().add(panel);
             makeSpeechBlock.setTitle(panel.getWindowTitle());
             makeSpeechBlock.pack();
             makeSpeechBlock.setLocationRelativeTo(null);
             makeSpeechBlock.setVisible(true);
             makeSpeechBlock.setAlwaysOnTop(true);   
     }
     
     
     private int doMakeSection(toolbarAction action){
           //get the section name and numbering type for the command
           String namingConvention, numberingType;
           String newName = "";
           namingConvention = action.action_naming_convention();
           if (namingConvention.equals("")) {
                log.debug("unable to name section, section mame was blank");
                MessageBox.OK("The command:" + action.action_name()+" does not have a naming convention associated with it");
                return -1;
           }
           numberingType = action.action_numbering_convention();
           
           if (numberingType.equals("single")) {
                newName = namingConvention;
           } else if (numberingType.equals("serial")) {
                //do sequential naming thing....or whatever.....
                newName = namingConvention;
           } else if (numberingType.equals("")) {
               MessageBox.OK("The command: "+ action.action_name()+ " does not have a numbering type associated with it");
               return -1;
           }
           if (this.ooDocument.getTextSections().hasByName(newName)){
                   log.debug("in doc command: section  already exists");
                   MessageBox.OK("The section:  prayers already exists");
                   return 0;
            }
          else {
               log.debug("in doCommand : adding text section prayers");
               addTextSection(newName);
               return 1;
          }
    }
    private void addTextSection(String sectionName){
 
        String sectionClass = "com.sun.star.text.TextSection";
        XTextViewCursor xCursor = ooDocument.getViewCursor();
        XText xText = xCursor.getText();
        XTextContent xSectionContent = ooDocument.createTextSection(sectionName, (short)1);
        try {
            xText.insertTextContent(xCursor, xSectionContent , true);        
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error("in addTextSection : "+ex.getLocalizedMessage(), ex);
        }        
        
        
    }
    
      private void doMarkup(toolbarAction action) {
        log.debug("in doMarkup for command: "+action.action_name());
          
           String namingConvention = "", numberingType = "", newName = "";
           namingConvention = action.action_naming_convention();
           numberingType =action.action_numbering_convention();
           log.debug("naming convention = "+ namingConvention);
            if (namingConvention.equals("")) {
                log.debug("unable to name section, section mame was blank");
                MessageBox.OK("The command:" + action.action_name()+" does not have a naming convention associated with it");
                return;
            }
           log.debug("numbering type = " + numberingType);
           if (action.action_type().equals("markup")) {    
              
               PropertyValue[] loadProps = new com.sun.star.beans.PropertyValue[2];
               loadProps[0] = new PropertyValue();
               loadProps[0].Name = new String( "Template");
               loadProps[0].Value = namingConvention;
               loadProps[1] = new PropertyValue();
               loadProps[1].Name = new String( "Family");
               loadProps[1].Value = new Integer(2);
               log.debug("invoking execute dispatch");
               ooDocument.executeDispatch(".uno:StyleApply", loadProps);
           }
    }

    private void doMakeQASection(toolbarAction action) {
             JDialog dlg;
             dlg= new JDialog();
             dlg.setTitle(action.action_display_text());
             dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
             //initDebaterecord.setPreferredSize(new Dimension(420, 300));
             IDialogSelector panel = DialogSelectorFactory.getDialogClass("org.bungeni.editor.selectors.InitQAsection");
             panel.initObject(ooDocument, dlg, action, null);
             //panel.setDialogMode(SelectorDialogModes.TEXT_INSERTION);
             //panel.setBackground(new Color(255, 255, 153));
             //initDebaterecord.setTitle("Selection Mode");
             dlg.getContentPane().add(panel.getPanel());
             dlg.pack();
             dlg.setLocationRelativeTo(null);
             dlg.setVisible(true);
             dlg.setAlwaysOnTop(true);   
            // returnState = new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }
    /*
    private void doMakeQASection(toolbarAction action) {
            JDialog makeQASection;
            makeQASection = new JDialog();
             makeQASection.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
             InitQAsection panel = new InitQAsection(ooDocument,  makeQASection, action);;
             System.out.println("size of panel : "+panel.getSize().height + " ," + panel.getSize().width  );
            
             makeQASection.setSize(348, 314);
             
              if (ooDocument.isTextSelected()) {
              //  panel.setDialogMode(SelectorDialogModes.TEXT_SELECTED);
                //panel.setBackground(new Color(255, 255, 153));
                //makeQASection.setTitle("Selection Mode");
              } else {
                panel.setDialogMode(SelectorDialogModes.TEXT_INSERTION);
                //panel.setBackground(new Color(204, 255, 153));
                //makeQASection.setTitle("Insertion Mode");
              }
             makeQASection.getContentPane().add(panel);
             makeQASection.pack();
             makeQASection.setLocationRelativeTo(null);
             makeQASection.setVisible(true);
             makeQASection.setAlwaysOnTop(true); 
    }
    */
    private void doMakePaperSection(toolbarAction action) {
               //section was added now prompt for dialog information
             JDialog makePaperSection;
             makePaperSection = new JDialog();
             makePaperSection.setTitle(action.action_display_text());
             makePaperSection.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
             InitPapers panel = new InitPapers(ooDocument,  makePaperSection, action);;
             System.out.println("size of panel : "+panel.getSize().height + " ," + panel.getSize().width  );
            
             makePaperSection.setSize(348, 314);
             
              if (ooDocument.isTextSelected()) {
                /*panel.setDialogMode(SelectorDialogModes.TEXT_SELECTED);
                panel.setBackground(new Color(255, 255, 153));
                makePaperSection.setTitle("Selection Mode");*/
              } else {
                panel.setDialogMode(SelectorDialogModes.TEXT_INSERTION);
                panel.setBackground(new Color(204, 255, 153));
                makePaperSection.setTitle(action.action_display_text());
              }
             makePaperSection.getContentPane().add(panel);
             makePaperSection.pack();
             makePaperSection.setLocationRelativeTo(null);
             makePaperSection.setVisible(true);
             makePaperSection.setAlwaysOnTop(true); 
    }

    

    private void doMakeSpeechMarkup(toolbarAction action) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void doMakeNOMSection(toolbarAction action) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


   
    public void doCommand(OOComponentHelper ooDocument, toolbarSubAction action, JFrame parentFrame) {
    }

    public void doCommand(OOComponentHelper ooDocument, ArrayList<String> action, JFrame parentFrame) {
    }

}
