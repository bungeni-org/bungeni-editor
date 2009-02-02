/*
 * CommonRouterActions.java
 *
 * Created on March 12, 2008, 10:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.routers;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.editor.selectors.BaseMetadataContainerPanel;
import org.bungeni.editor.selectors.DialogSelectorFactory;
import org.bungeni.editor.selectors.IDialogSelector;
import org.bungeni.editor.selectors.IMetadataContainerPanel;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

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
    
    static class displaySubActionFrameRunner extends SwingRunner {
        public displaySubActionFrameRunner(toolbarAction a, toolbarSubAction sa, JFrame pf, OOComponentHelper ooDoc){
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
    
    public static BungeniValidatorState displaySubActionDialog(toolbarAction action, toolbarSubAction subAction, JFrame parentFrame, OOComponentHelper ooDocument) {
               BungeniValidatorState returnState = null;
                try {

                    displaySubActionFrameRunner dsfRunner = new displaySubActionFrameRunner( action,  subAction, parentFrame, ooDocument);
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
}
