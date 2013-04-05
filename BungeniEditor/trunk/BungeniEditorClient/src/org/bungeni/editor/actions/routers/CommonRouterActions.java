package org.bungeni.editor.actions.routers;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.document.DocumentSection;
import org.bungeni.editor.document.DocumentSectionsContainer;
import org.bungeni.editor.selectors.BaseMetadataContainerPanel;
import org.bungeni.editor.selectors.DialogSelectorFactory;
import org.bungeni.editor.selectors.IDialogSelector;
import org.bungeni.editor.selectors.IMetadataContainerPanel;
import org.bungeni.editor.selectors.metadata.MetadataEditor;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;
import org.bungeni.ooo.utils.CommonExceptionUtils;

//~--- JDK imports ------------------------------------------------------------

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
import org.bungeni.extutils.BungeniUUID;

/**
 * Utility class that defines the shared APIs used during markup
 * @author Ashok Hariharan
 */
public class CommonRouterActions {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CommonRouterActions.class.getName());

    /** Creates a new instance of CommonRouterActions */
    public CommonRouterActions() {}

        // public static BungeniValidatorState displaySubActionDialog(toolbarSubAction action, toolbarSubAction subAction,
        //       JFrame parentFrame, OOComponentHelper ooDocument, boolean alwaysOnTop) {
        public static BungeniValidatorState displaySubActionDialog(toolbarAction subAction,
           JFrame parentFrame, OOComponentHelper ooDocument, boolean alwaysOnTop)  {

        // get the parentAction from the sub action instance
       // String pAction = subAction.parent_action_name();
       // Class parentActionClass = Class.forName(pAction);

        // !+ACTION_RECONF (rm, jan 2012) create instance of parent action from
        // sub_parent_action using introspection
       // toolbarSubAction parentAction = (toolbarSubAction) parentActionClass.newInstance();
        
        BungeniValidatorState returnState = null;

        try {
            // displaySubActionFrameRunner dsfRunner = new displaySubActionFrameRunner(action, subAction, parentFrame,
            //                                            ooDocument, alwaysOnTop);
            displaySubActionFrameRunner dsfRunner = new displaySubActionFrameRunner(subAction, parentFrame,
                                                        ooDocument, alwaysOnTop);

            javax.swing.SwingUtilities.invokeLater(dsfRunner);
            returnState = new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
        } catch (Exception ex) {
            log.error("displaySelectorDialog : " + ex.getMessage());
            returnState = new BungeniValidatorState(true, new BungeniMsg("EXCEPTION_FAILURE"));
        } finally {
            return returnState;
        }
    }

    /******
     * WARNING _ NOT USED PRESENTLY
     * !+UNUSED_CLASS(ah, jan-2012) - This class is not being used - commented out
     * for further cleanup and review
     * @param action
     * @param subAction
     * @param parentFrame
     * @param ooDocument
     * @return
     */
    /***
    public static BungeniValidatorState displaySubActionModalDialog(toolbarAction action, toolbarSubAction subAction,
            JFrame parentFrame, OOComponentHelper ooDocument) {
        BungeniValidatorState returnState = null;

        try {
            displaySubActionDialogRunner dsfRunner = new displaySubActionDialogRunner(action, subAction, parentFrame,
                                                         ooDocument);

            javax.swing.SwingUtilities.invokeLater(dsfRunner);
            returnState = new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
        } catch (Exception ex) {
            log.error("displaySelectorDialog : " + ex.getMessage());
            returnState = new BungeniValidatorState(true, new BungeniMsg("EXCEPTION_FAILURE"));
        } finally {
            return returnState;
        }
    }
   **/
    
    // public static BungeniValidatorState displaySelectorDialog(toolbarSubAction action, toolbarSubAction subAction,
       //      JFrame parentFrame, OOComponentHelper ooDocument) {
    public static BungeniValidatorState displaySelectorDialog(toolbarAction subAction,
            JFrame parentFrame, OOComponentHelper ooDocument) {        
        BungeniValidatorState returnState = null;

        // get the parentAction from the sub action instance
        //String pAction = subAction.parent_action_name();
        //Class parentActionClass = Class.forName(pAction);

        // !+ACTION_RECONF (rm, jan 2012) create instance of parent action from
        // sub_parent_action using introspection
        //toolbarSubAction action = (toolbarSubAction) parentActionClass.newInstance();

        try {
            // displaySelectorFrameRunner dsfRunner = new displaySelectorFrameRunner(action, subAction, parentFrame,
            //                                            ooDocument);
            displaySelectorFrameRunner dsfRunner = new displaySelectorFrameRunner(subAction, parentFrame,
                                                       ooDocument);

            javax.swing.SwingUtilities.invokeLater(dsfRunner);
            returnState = new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
        } catch (Exception ex) {
            log.error("displaySelectorDialog : " + ex.getMessage());
            returnState = new BungeniValidatorState(true, new BungeniMsg("EXCEPTION_FAILURE"));
        } finally {
            return returnState;
        }
    }

    // public static BungeniValidatorState displayFilteredDialog(toolbarSubAction action, toolbarSubAction subAction,
    //         OOComponentHelper ooDocument) {
    public static BungeniValidatorState displayFilteredDialog(toolbarAction subAction,
            OOComponentHelper ooDocument) {
        BungeniValidatorState returnState = null;

        // get the parentAction from the sub action instance
        //String pAction = subAction.parent_action_name();
        //Class parentActionClass = Class.forName(pAction);

        // !+ACTION_RECONF (rm, jan 2012) create instance of parent action from
        // sub_parent_action using introspection
        //toolbarSubAction action = (toolbarSubAction) parentActionClass.newInstance();

        try {
            log.debug("displayFilteredDialog: subAction name = " + subAction.sub_action_name());

            // toolbarAction parentAction = getParentAction();

            JDialog dlg;

            dlg = new JDialog();
            dlg.setTitle("Enter Settings for Document");
            dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            // initDebaterecord.setPreferredSize(new Dimension(420, 300));
            IDialogSelector panel = DialogSelectorFactory.getDialogClass(subAction.dialog_class());

            // panel.initObject(ooDocument, dlg,  action, subAction);
            panel.initObject(ooDocument, dlg,  subAction);

            // panel.setDialogMode(SelectorDialogModes.TEXT_INSERTION);
            // panel.setBackground(new Color(255, 255, 153));
            // initDebaterecord.setTitle("Selection Mode");
            dlg.getContentPane().add(panel.getPanel());
            dlg.pack();
            dlg.setLocationRelativeTo(null);
            dlg.setVisible(true);
            dlg.setAlwaysOnTop(true);
            returnState = new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
        } catch (Exception ex) {
            log.error("displayFilteredDialog : " + ex.getMessage());
            log.error("displayFilteredDialog: stack trace :  \n"
                      + org.bungeni.ooo.utils.CommonExceptionUtils.getStackTrace(ex));
            returnState = new BungeniValidatorState(true, new BungeniMsg("EXCEPTION_FAILURE"));
        } finally {
            return returnState;
        }
    }

    public static String get_newSectionNameForAction(toolbarAction subAction, OOComponentHelper ooDocument) {
        String newSectionName = "";

        if (subAction.section_numbering_convention().equals("single")) {
            newSectionName = subAction.section_naming_convention();
        } else if (subAction.section_numbering_convention().equals("serial")) {
            String sectionPrefix = subAction.section_naming_convention();

            for (int i = 1; ; i++) {
                if (ooDocument.hasSection(sectionPrefix + i)) {
                    continue;
                } else {
                    newSectionName = sectionPrefix + i;

                    break;
                }
            }
        } else {
            log.error("get_newSectionNameForAction: invalid action naming convention: "
                      + subAction.section_naming_convention());
        }

        return newSectionName;
    }

    public static boolean action_createSystemContainerFromSelection(OOComponentHelper ooDoc,
            String systemContainerName) {
        boolean bResult = false;

        try {
            XTextViewCursor xCursor         = ooDoc.getViewCursor();
            XText           xText           = xCursor.getText();
            XTextContent    xSectionContent = ooDoc.createTextSection(systemContainerName, (short) 1);

            xText.insertTextContent(xCursor, xSectionContent, true);
            bResult = true;
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            bResult = false;
            log.error("in addTextSection : " + ex.getLocalizedMessage(), ex);
        }
        return bResult;
        
    }

    public static boolean action_createSpannedContainer(OOComponentHelper ooDocument, String sectionName,
            String startBookmark, String endBookmark) {
        boolean bState = false;

        try {

            // get source and target bookmark
            Object sBookmark = ooDocument.getBookmarks().getByName(startBookmark);
            Object eBookmark = ooDocument.getBookmarks().getByName(endBookmark);

            // get bookmark ranges
            XTextContent xstartContent = ooQueryInterface.XTextContent(sBookmark);
            XTextContent xendContent   = ooQueryInterface.XTextContent(eBookmark);
            XTextRange   xstartRange   = xstartContent.getAnchor();
            XTextRange   xendRange     = xendContent.getAnchor();

            // get cursor spanning bookmark ranges
            XTextCursor scaleCursor = ooDocument.getTextDocument().getText().createTextCursor();

            scaleCursor.gotoRange(xstartRange, false);
            scaleCursor.gotoRange(xendRange, true);

            // create section over spanned range
            XText        xDocText       = ooDocument.getTextDocument().getText();
            XTextContent sectionContent = ooDocument.createTextSection(sectionName, (short) 1);

            xDocText.insertTextContent(scaleCursor, sectionContent, true);

            // revert to true
            bState = true;
        } catch (Exception ex) {
            log.error("action_createSpannedContainer :" + ex.getMessage());
        }
        return bState;
    }

    /**
     * This API defines the default metadata for all sections created via markup
     * @param pAction
     * @return
     */
    public static HashMap<String, String> get_newSectionMetadata(toolbarAction subAction) {
        HashMap<String, String> metaMap = new HashMap<String, String>();

        metaMap.put("BungeniSectionType", subAction.getSectionType());
        metaMap.put("BungeniSectionID", BungeniUUID.getShortUUID());
        metaMap.putAll(subAction.getMetadatasMap());
        metaMap.put(MetadataEditor.MetaEditableFlag, "false");

        return metaMap;
    }

    public static HashMap<String, String> get_newInlineMetadata(toolbarAction subAction) {
        HashMap<String, String> metaMap = new HashMap<String, String>();

        metaMap.put("BungeniInlineType", subAction.getInlineType());
        metaMap.put("BungeniInlineID", BungeniUUID.getShortUUID());
        metaMap.put(MetadataEditor.MetaEditableFlag, "false");

        return metaMap;
    }




    // !+ ADDED COMMENTS
    /**
     * This method accepts a toolbarObject, the name of the newly created
     * section and the ooDocument instance and uses this to
     * create the new section type and set its name
     * @param pAction - selected action on the sidepane
     * @param newSectionName - name for the new section
     * @param ooDocument - instance of the oooDocument
     */
    public static void setSectionProperties(toolbarAction pAction, String newSectionName,
            OOComponentHelper ooDocument) {
        String                  sectionType  = pAction.getSectionType();
        DocumentSection         secObj       = DocumentSectionsContainer.getDocumentSectionByType(sectionType);
        HashMap<String, Object> sectionProps = secObj.getSectionProperties(ooDocument);
        XTextSection            newSection   = ooDocument.getSection(newSectionName);
        XNamed                  namedSection = ooQueryInterface.XNamed(newSection);
        XPropertySet            xProps       = ooQueryInterface.XPropertySet(newSection);

        for (String propName : sectionProps.keySet()) {
            try {
                log.debug("setSectionProperties : " + propName + " value = " + sectionProps.get(propName).toString());

                Object propVal = sectionProps.get(propName);

                if (propVal.getClass() == java.lang.Integer.class) {
                    xProps.setPropertyValue(propName, (java.lang.Integer) sectionProps.get(propName));
                } else if (propVal.getClass() == java.lang.Long.class) {
                    xProps.setPropertyValue(propName, (java.lang.Long) sectionProps.get(propName));
                } else if (propVal.getClass() == java.lang.String.class) {
                    xProps.setPropertyValue(propName, (java.lang.String) sectionProps.get(propName));
                } else if (propVal.getClass() == com.sun.star.style.GraphicLocation.class) {
                    xProps.setPropertyValue(propName, (com.sun.star.style.GraphicLocation) sectionProps.get(propName));
                } else {
                    xProps.setPropertyValue(propName, (java.lang.String) sectionProps.get(propName));
                }
            } catch (Exception ex) {
                log.error("setSectionProperties :" + propName + " : " + ex.getMessage());
                log.error("setSectionProperties :" + CommonExceptionUtils.getStackTrace(ex));
            }
        }
    }

    static abstract class SwingRunner implements Runnable {
        OOComponentHelper ooDocument;
        JFrame            parentFrame;
        toolbarAction  subAction;

        public SwingRunner() {}

        // public SwingRunner(toolbarSubAction pa, toolbarSubAction sa, JFrame pf, OOComponentHelper ooDoc) {
        public SwingRunner( toolbarAction sa, JFrame pf, OOComponentHelper ooDoc) {
            // action      = pa;
            subAction   = sa;
            parentFrame = pf;
            ooDocument  = ooDoc;
        }

        abstract public void run();
    }


    static class displaySelectorFrameRunner extends SwingRunner {
        // public displaySelectorFrameRunner(toolbarSubAction a, toolbarSubAction sa, JFrame pf, OOComponentHelper ooDoc) {
        public displaySelectorFrameRunner(toolbarAction sa, JFrame pf, OOComponentHelper ooDoc) {
            // super(a, sa, pf, ooDoc);
            super( sa, pf, ooDoc);
        }

        @Override
        public void run() {
            try {
                //!+(ACTION_RECONF, ah-23-01-2012) 
                String mainDialogClass = this.subAction.dialog_class();

                // sString subActionDialogClass = subAction.dialog_class();
                IMetadataContainerPanel containerPanel = null;

                if (mainDialogClass.length() > 0) {
                    containerPanel = BaseMetadataContainerPanel.getContainerPanelObject(mainDialogClass);
                }
                else
                {
                    log.error("Error initializing the containerPanel!" ) ;
                }

                // also calls setupPanels()
                containerPanel.initVariables(ooDocument, parentFrame, subAction,
                                             subAction.getSelectorDialogMode());
                containerPanel.initialize();

                // Main m = new Main();
                // m.initVariables(ooDoc, parentFrm, aAction, aSubAction, dlgMode);
                javax.swing.JFrame f = new javax.swing.JFrame(subAction.toString());

                containerPanel.setContainerFrame(f);
                f.add(containerPanel.getPanelComponent());
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
                f.setAlwaysOnTop(true);

                // f.setVisible(true);
            } catch (Exception ex) {
                log.error("displaySelectorFrameRunner exception :" + ex.getMessage(), ex);
            }
        }
    }


    /***
     * !+UNUSED_CLASS(ah, jan-2012) - This class is not being used - commented out
     * for further cleanup and review
     *
    static class displaySubActionDialogRunner extends SwingRunner {
        public displaySubActionDialogRunner(toolbarAction a, toolbarSubAction sa, JFrame pf, OOComponentHelper ooDoc) {
            super(a, sa, pf, ooDoc);
        }

        @Override
        public void run() {
            try {
                String mainDialogClass = subAction.dialog_class();

                // sString subActionDialogClass = subAction.dialog_class();
                IMetadataContainerPanel containerPanel = null;

                if (mainDialogClass.length() > 0) {
                    containerPanel = BaseMetadataContainerPanel.getContainerPanelObject(mainDialogClass);
                }

                // also calls setupPanels()
                containerPanel.initVariables(ooDocument, parentFrame, action, subAction,
                                             subAction.getSelectorDialogMode());
                containerPanel.initialize();

                JDialog f = new JDialog();

                f.setLocationRelativeTo(parentFrame);
                f.setTitle(action.action_display_text());
                containerPanel.setContainerFrame(f);

                // javax.swing.JFrame f = new javax.swing.JFrame(action.action_display_text());
                // containerPanel.setContainerFrame(f);
                f.setModal(true);
                f.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                f.getContentPane().add(containerPanel.getPanelComponent());
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
                f.setAlwaysOnTop(true);

                // f.setVisible(true);
            } catch (Exception ex) {
                log.error("displaySelectorFrameRunner exception :" + ex.getMessage());
            }
        }
    }
   **/

    static class displaySubActionFrameRunner extends SwingRunner {
        boolean b_alwaysOnTop = false;

        // public displaySubActionFrameRunner(toolbarSubAction a, toolbarSubAction sa, JFrame pf, OOComponentHelper ooDoc,
         //                                  boolean alwaysOnTop) {
         public displaySubActionFrameRunner(toolbarAction sa, JFrame pf, OOComponentHelper ooDoc,
                                           boolean alwaysOnTop) {
            // super(a, sa, pf, ooDoc);
            super(sa, pf, ooDoc);
            b_alwaysOnTop = alwaysOnTop;
        }

        @Override
        public void run() {
            try {
                String mainDialogClass = subAction.dialog_class();

                // sString subActionDialogClass = subAction.dialog_class();
                IRouterSelectorPanel containerPanel = null;

                if (mainDialogClass.length() > 0) {
                    containerPanel = RouterSelectorPanelFactory.getContainerPanelObject(mainDialogClass);
                }

                // also calls setupPanels()
                containerPanel.initVariables(ooDocument, parentFrame, subAction,
                                             subAction.getSelectorDialogMode());
                containerPanel.initialize();

                // Main m = new Main();
                // m.initVariables(ooDoc, parentFrm, aAction, aSubAction, dlgMode);
                javax.swing.JFrame f = new javax.swing.JFrame(subAction.toString());

                containerPanel.setContainerFrame(f);
                f.add(containerPanel.getPanelComponent());
                f.pack();
                f.setLocationRelativeTo(null);
                f.setAlwaysOnTop(b_alwaysOnTop);
                f.setVisible(true);

                // f.setVisible(true);
            } catch (Exception ex) {
                log.error("displaySubActionFrameRunner exception :" + ex.getMessage());
            }
        }
    }

    public static class TypeCreationState {
        public String typeName;
        public String returnState;
        public HashMap<String,String> propsMap; 
                
        public TypeCreationState(String sName, String rState, HashMap<String,String> propsMap) {
            this.typeName = sName;
            this.returnState = rState;
            this.propsMap = propsMap;
        }

    }


    public static TypeCreationState action_createSection(toolbarAction subAction, OOComponentHelper ooDocument){
      HashMap<String,String> returnMap = new HashMap<String,String>();
      String newSectionName = CommonRouterActions.get_newSectionNameForAction(subAction, ooDocument);

      if (newSectionName.length() == 0 ) {
            log.error("New seciton name was empty for action "  + subAction);
            return new TypeCreationState("", "FAILURE_CREATE_SECTION", null);
       } else {
            boolean bAction = CommonRouterActions.action_createSystemContainerFromSelection(ooDocument, newSectionName);
            if (bAction ) {
                //set properties for document section
                CommonRouterActions.setSectionProperties(subAction, newSectionName, ooDocument);
                //set section type metadata
                HashMap<String,String> propMap = CommonRouterActions.get_newSectionMetadata(subAction);
                ooDocument.setSectionMetadataAttributes(
                        newSectionName, 
                        propMap
                        );
                return new TypeCreationState(newSectionName, "SUCCESS", propMap);
            } else {
                log.error("routeAction_TextSelectedInsertAction_CreateSection: error while creating section ");
                return new TypeCreationState("","FAILURE_CREATE_SECTION", null);
            }
         }

    }
       public static boolean action_createRootSection(OOComponentHelper ooDoc, String sectionName) {
        boolean bResult = false;
        try {
            XText docText = ooDoc.getTextDocument().getText();
            XTextCursor docCursor = docText.createTextCursor();
            docCursor.gotoStart(false);
            docCursor.gotoEnd(true);
            XTextContent theContent = ooDoc.createTextSection(sectionName, (short)1);
            docText.insertTextContent(docCursor, theContent, true);
            bResult = true;
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error("in action_createRootSection :" + ex.getMessage());
            log.error("in action_createRootSection :" + CommonExceptionUtils.getStackTrace(ex));
        }
        return bResult;
    }

}
