/*
 * CommonRouterActions.java
 *
 * Created on March 12, 2008, 10:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.routers;

import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNamed;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextSection;
import com.sun.star.text.XTextViewCursor;
import java.util.HashMap;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.editor.document.DocumentSection;
import org.bungeni.editor.document.DocumentSectionsContainer;
import org.bungeni.editor.selectors.BaseMetadataContainerPanel;
import org.bungeni.editor.selectors.DialogSelectorFactory;
import org.bungeni.editor.selectors.IDialogSelector;
import org.bungeni.editor.selectors.IMetadataContainerPanel;
import org.bungeni.editor.selectors.metadata.SectionMetadataEditor;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;
import org.bungeni.ooo.utils.CommonExceptionUtils;

/**
 *
 * @author Administrator
 */
public class CommonRouterActions {
       private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CommonRouterActions.class.getName());
 
    /** Creates a new instance of CommonRouterActions */
    public CommonRouterActions() {
    }
    
   static abstract class SwingRunner implements Runnable {
        toolbarAction action;
        toolbarSubAction subAction;
        OOComponentHelper ooDocument;
        JFrame parentFrame;
        public SwingRunner(){
            
        }
        public SwingRunner(toolbarAction a, toolbarSubAction sa, JFrame pf, OOComponentHelper ooDoc){
            action = a;
            subAction = sa;
            parentFrame = pf;
            ooDocument = ooDoc;
        }
        abstract public void run();
    }
   
    static class displaySubActionDialogRunner extends SwingRunner {
        public displaySubActionDialogRunner(toolbarAction a, toolbarSubAction sa, JFrame pf, OOComponentHelper ooDoc){
            super (a, sa, pf, ooDoc);
        }
        @Override
        public void run() {
                try {
                    String mainDialogClass = subAction.dialog_class();
                    //sString subActionDialogClass = subAction.dialog_class();
                    IMetadataContainerPanel containerPanel = null;
                    if (mainDialogClass.length() > 0 ) {
                        containerPanel = BaseMetadataContainerPanel.getContainerPanelObject(mainDialogClass);
                    }
                    //also calls setupPanels()
                    containerPanel.initVariables(ooDocument, parentFrame, action, subAction, subAction.getSelectorDialogMode());
                    containerPanel.initialize();
                    
                    JDialog f = new JDialog();
                    f.setLocationRelativeTo(parentFrame);
                    f.setTitle(action.action_display_text());
                    containerPanel.setContainerFrame(f);
                    //javax.swing.JFrame f = new javax.swing.JFrame(action.action_display_text());
                    //containerPanel.setContainerFrame(f);
                    f.setModal(true);
                    f.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    f.getContentPane().add(containerPanel.getPanelComponent());
                    f.pack();
                    f.setLocationRelativeTo(null);
                    f.setVisible(true);
                    f.setAlwaysOnTop(true);   
                   // f.setVisible(true);
                } catch (Exception ex){
                    log.error("displaySelectorFrameRunner exception :" + ex.getMessage());
                }
        }
    }
   
    static class displaySubActionFrameRunner extends SwingRunner {
        
        boolean b_alwaysOnTop = false;
        
        public displaySubActionFrameRunner(toolbarAction a, toolbarSubAction sa, JFrame pf, OOComponentHelper ooDoc, boolean alwaysOnTop){
            super (a, sa, pf, ooDoc);
            b_alwaysOnTop = alwaysOnTop;
        }
        @Override
        public void run() {
                try {
                    String mainDialogClass = subAction.dialog_class();
                    //sString subActionDialogClass = subAction.dialog_class();
                    IRouterSelectorPanel containerPanel = null;
                    if (mainDialogClass.length() > 0 ) {
                        containerPanel = RouterSelectorPanelFactory.getContainerPanelObject(mainDialogClass);
                    }
                    //also calls setupPanels()
                    containerPanel.initVariables(ooDocument, parentFrame, action, subAction, subAction.getSelectorDialogMode());
                    containerPanel.initialize();
                    // Main m = new Main();
                  // m.initVariables(ooDoc, parentFrm, aAction, aSubAction, dlgMode);
                    javax.swing.JFrame f = new javax.swing.JFrame(action.action_display_text());
                    containerPanel.setContainerFrame(f);
                    f.add(containerPanel.getPanelComponent());
                    f.pack();
                    f.setLocationRelativeTo(null);
                    f.setAlwaysOnTop(b_alwaysOnTop);   
                    f.setVisible(true);
                   // f.setVisible(true);
                } catch (Exception ex){
                    log.error("displaySubActionFrameRunner exception :" + ex.getMessage());
                }
        }
    }
   
    static class displaySelectorFrameRunner extends SwingRunner {
        public displaySelectorFrameRunner(toolbarAction a, toolbarSubAction sa, JFrame pf, OOComponentHelper ooDoc){
            super (a, sa, pf, ooDoc);
        }
        @Override
        public void run() {
                try {
                    String mainDialogClass = action.action_dialog_class();
                    //sString subActionDialogClass = subAction.dialog_class();
                    IMetadataContainerPanel containerPanel = null;
                    if (mainDialogClass.length() > 0 ) {
                        containerPanel = BaseMetadataContainerPanel.getContainerPanelObject(mainDialogClass);
                    }
                    //also calls setupPanels()
                    containerPanel.initVariables(ooDocument, parentFrame, action, subAction, subAction.getSelectorDialogMode());
                    containerPanel.initialize();
                    // Main m = new Main();
                  // m.initVariables(ooDoc, parentFrm, aAction, aSubAction, dlgMode);
                    javax.swing.JFrame f = new javax.swing.JFrame(action.action_display_text());
                    containerPanel.setContainerFrame(f);
                    f.add(containerPanel.getPanelComponent());
                    f.pack();
                    f.setLocationRelativeTo(null);
                    f.setVisible(true);
                    f.setAlwaysOnTop(true);   
                   // f.setVisible(true);
                } catch (Exception ex){
                    log.error("displaySelectorFrameRunner exception :" + ex.getMessage());
                }
        }
        
    }
    
    
    
    public static BungeniValidatorState displaySubActionDialog(toolbarAction action, toolbarSubAction subAction, JFrame parentFrame, OOComponentHelper ooDocument, boolean alwaysOnTop) {
               BungeniValidatorState returnState = null;
                try {

                    displaySubActionFrameRunner dsfRunner = new displaySubActionFrameRunner( action,  subAction, parentFrame, ooDocument, alwaysOnTop);
                    javax.swing.SwingUtilities.invokeLater(dsfRunner);
                    returnState = new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 

                } catch (Exception ex) {
                   log.error("displaySelectorDialog : " + ex.getMessage());
                   returnState = new BungeniValidatorState(true, new BungeniMsg("EXCEPTION_FAILURE")); 
                } finally {
                    return returnState;
                }
        
    }
    
    public static BungeniValidatorState displaySubActionModalDialog(toolbarAction action, toolbarSubAction subAction, JFrame parentFrame, OOComponentHelper ooDocument) {
               BungeniValidatorState returnState = null;
                try {

                    displaySubActionDialogRunner dsfRunner = new displaySubActionDialogRunner( action,  subAction, parentFrame, ooDocument);
                    javax.swing.SwingUtilities.invokeLater(dsfRunner);
                    returnState = new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 

                } catch (Exception ex) {
                   log.error("displaySelectorDialog : " + ex.getMessage());
                   returnState = new BungeniValidatorState(true, new BungeniMsg("EXCEPTION_FAILURE")); 
                } finally {
                    return returnState;
                }
    }
    
    
    public static BungeniValidatorState displaySelectorDialog(toolbarAction action, toolbarSubAction subAction, JFrame parentFrame, OOComponentHelper ooDocument) {
               BungeniValidatorState returnState = null;
                try {

                    displaySelectorFrameRunner dsfRunner = new displaySelectorFrameRunner( action,  subAction, parentFrame, ooDocument);
                    javax.swing.SwingUtilities.invokeLater(dsfRunner);
                    returnState = new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 

                } catch (Exception ex) {
                   log.error("displaySelectorDialog : " + ex.getMessage());
                   returnState = new BungeniValidatorState(true, new BungeniMsg("EXCEPTION_FAILURE")); 
                } finally {
                    return returnState;
                }
        }
    
   public static BungeniValidatorState displayFilteredDialog(toolbarAction action, toolbarSubAction subAction, OOComponentHelper ooDocument) {
            BungeniValidatorState returnState =  null;
           try {
             log.debug("displayFilteredDialog: subAction name = "+ subAction.sub_action_name());
             // toolbarAction parentAction = getParentAction();
             
             JDialog dlg;
             dlg= new JDialog();
             dlg.setTitle("Enter Settings for Document");
             dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
             //initDebaterecord.setPreferredSize(new Dimension(420, 300));
             IDialogSelector panel = DialogSelectorFactory.getDialogClass(subAction.dialog_class());
             panel.initObject(ooDocument, dlg, action, subAction);
             //panel.setDialogMode(SelectorDialogModes.TEXT_INSERTION);
             //panel.setBackground(new Color(255, 255, 153));
             //initDebaterecord.setTitle("Selection Mode");
             dlg.getContentPane().add(panel.getPanel());
             dlg.pack();
             dlg.setLocationRelativeTo(null);
             dlg.setVisible(true);
             dlg.setAlwaysOnTop(true);   
             returnState = new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
             } catch (Exception ex) {
                 log.error("displayFilteredDialog : " + ex.getMessage());
                 log.error("displayFilteredDialog: stack trace :  \n" + org.bungeni.ooo.utils.CommonExceptionUtils.getStackTrace(ex));
                 returnState = new BungeniValidatorState(true, new BungeniMsg("EXCEPTION_FAILURE")); 
                 
             } finally {
                   return returnState; 
             }
    }

    public static String get_newSectionNameForAction(toolbarAction pAction, OOComponentHelper ooDocument) {
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
         return newSectionName;
    }

 public static boolean action_createSystemContainerFromSelection(OOComponentHelper ooDoc, String systemContainerName){
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

     public static boolean action_createSpannedContainer(OOComponentHelper ooDocument, String sectionName, String startBookmark, String endBookmark) {
        boolean bState = false;
        try {
            
            //get source and target bookmark
            Object sBookmark = ooDocument.getBookmarks().getByName(startBookmark);
            Object eBookmark = ooDocument.getBookmarks().getByName(endBookmark);
            
            //get bookmark ranges
            XTextContent xstartContent = ooQueryInterface.XTextContent(sBookmark);
            XTextContent xendContent = ooQueryInterface.XTextContent(eBookmark);
            XTextRange xstartRange = xstartContent.getAnchor();
            XTextRange xendRange =xendContent.getAnchor();
            
            //get cursor spanning bookmark ranges
            XTextCursor scaleCursor = ooDocument.getTextDocument().getText().createTextCursor();
            scaleCursor.gotoRange(xstartRange, false);
            scaleCursor.gotoRange(xendRange, true);
            
            //create section over spanned range
            XText xDocText = ooDocument.getTextDocument().getText();
            XTextContent sectionContent = ooDocument.createTextSection(sectionName, (short)1);
            xDocText.insertTextContent(scaleCursor, sectionContent, true);
            //revert to true
            bState = true;
        } catch(Exception ex) {
            log.error("action_createSpannedContainer :" + ex.getMessage());
        } finally {
            return bState;
        }
     }
     
     
     public static HashMap<String,String> get_newSectionMetadata(toolbarAction pAction) {
         HashMap<String,String> metaMap = new HashMap<String,String>();
         metaMap.put("BungeniSectionType", pAction.action_section_type());
         metaMap.put(SectionMetadataEditor.MetaEditableFlag, "false");
         return metaMap;
     }

    public static void setSectionProperties(toolbarAction pAction, String newSectionName, OOComponentHelper ooDocument) {
        String sectionType = pAction.action_section_type();
        DocumentSection secObj = DocumentSectionsContainer.getDocumentSectionByType(sectionType);
        HashMap<String,Object> sectionProps = secObj.getSectionProperties(ooDocument);
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
                } else if (propVal.getClass() == com.sun.star.style.GraphicLocation.class) {
                      xProps.setPropertyValue(propName, (com.sun.star.style.GraphicLocation) sectionProps.get(propName));
                } else
                      xProps.setPropertyValue(propName, (java.lang.String) sectionProps.get(propName));
            } catch (Exception ex) {
                log.error("setSectionProperties :"+ propName +" : "  +ex.getMessage());
                log.error("setSectionProperties :"+ CommonExceptionUtils.getStackTrace(ex));
            } 
        }
    }


}
