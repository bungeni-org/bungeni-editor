
package org.bungeni.editor.selectors;

import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextRangeCompare;
import java.awt.Cursor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import org.bungeni.db.IQueryResultsIterator;
import org.bungeni.db.QueryResults;
import org.bungeni.editor.config.DocumentActionsReader;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.selectors.metadata.SectionMetadataEditor;
import org.bungeni.extutils.BungeniUUID;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * This class is the base class for all interactive dialogs with the Editor.
 * This is a container class that loads sub-panels -- Sub Panels are added to the task pane widget serially.
 * The loading is done in the following manner --
 *
 * First the action_dialog property for an action is queried to determine the action container dialog (the action\
 * container dialog is the container for all sub-panels.
 * Sub Panels are mapped to container dialogs in the selector_dialogs table.
 *
 * @author  Ashok Hariharan
 */
public abstract class BaseMetadataContainerPanel extends javax.swing.JPanel implements IMetadataContainerPanel {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BaseMetadataContainerPanel.class.getName());
    /**
     * openoffice component handle
     */
    protected OOComponentHelper ooDocument;
    /**
     * this is the parent JFrame
     */
    protected JFrame parentFrame;
    /**
     * this is the container JFrame
     */
    protected Window containerFrame;
    protected toolbarAction theSubAction = null;
    protected SelectorDialogModes dialogMode;
    protected SectionMetadataEditor sectionMetadataEditor = null;
    protected String editSectionName = "";
    public String                          mainSectionName     = "";
    public HashMap<String, String>         selectionData         = new HashMap<String, String>();

    protected XTextRange capturedCursorRange = null;

    public class ConditionSet {

        HashMap<String, String> conditionSet = new HashMap<String, String>();

        public ConditionSet() {
        }

        public HashMap<String, String> getConditionSet() {
            return this.conditionSet;
        }

        public boolean conditionalsExist() {
            if (this.conditionSet.size() > 0) {
                return true;
            } else {
                return false;
            }
        }

        public void addConditionSet(String conditionName, String conditionValue) {
            if (!conditionSet.containsKey(conditionName)) {
                this.conditionSet.put(conditionName, conditionValue);
            }
        }

        public void setConditionSetValue(String conditionName, String conditionValue) {
            if (this.conditionSet.containsKey(conditionName)) {
                conditionSet.put(conditionName, conditionValue);
            }
        }

        public String getConditionValue(String condName) {
            if (conditionSet.containsKey(condName)) {
                return conditionSet.get(condName);
            } else {
                return null;
            }
        }
    }
    private ConditionSet conditionSet = null;

    public ConditionSet getConditionSet() {
        return conditionSet;
    }
    /**
     * error messages
     */
    private ArrayList<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>(0);

    /** Creates new form Main */
    /**
     *In the derived class, always call super(), and then execute initVariables to set the requireed parameters
     */
    public BaseMetadataContainerPanel() {
        super();
        initComponents();
        //dont do this -- use the theme color
        //String popupDlgBackColor = BungeniEditorProperties.getEditorProperty("popupDialogBackColor");
        //this.setBackground(java.awt.Color.decode(popupDlgBackColor));

        initListeners();
        conditionSet = new ConditionSet();
    }

    // !+ACTION_RECONF (rm, feb 2012( - since subActions are deprecated, commenting 
    // unreferenced code
    private String getMetadataEditorString() {
        //if (theSubAction != null) {
        //    return "toolbarSubAction." + theSubAction.sub_action_name() + "." + theSubAction.sub_action_name();
        //} else {
            return theSubAction.sub_action_name();
        //}
    }

    protected void setMetadataEditableFlag(boolean bState) {
        if (sectionMetadataEditor != null) {
            sectionMetadataEditor.bMetadataEditable = bState;
        }
    }

    public class ErrorMessage {

        java.awt.Component originatingFrom;
        java.awt.Component focusField;
        String errorMessage;

        ErrorMessage(java.awt.Component originatingFrom, java.awt.Component field, String msg) {
            this.originatingFrom = originatingFrom;
            this.focusField = field;
            this.errorMessage = msg;
        }

        void setFocusOnField() {
            if (focusField != null) {
                focusField.requestFocus();
            }
        }

        @Override
        public String toString() {
            return errorMessage;
        }
    }

    public void clearErrorMessages() {
        errorMessages.clear();
    }

    public String ErrorMessagesAsString() {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (ErrorMessage msg : errorMessages) {
            sb.append(msg.toString());
            if (++count != errorMessages.size()) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public void addErrorMessage(java.awt.Component fromPanel, java.awt.Component comp, String msg) {
        errorMessages.add(new ErrorMessage(fromPanel, comp, msg));
    }

    /**
     * The initvariables function sets the appropriate parameters used by the application. 
     * This function is called immediately after the constructor has been called.
     * @param ooDoc
     * @param parentFrm
     * @param aAction
     * @param aSubAction
     * @param dlgMode
     */
    public void initVariables(OOComponentHelper ooDoc, JFrame parentFrm, toolbarAction aSubAction, SelectorDialogModes dlgMode) {
        this.ooDocument = ooDoc;
        this.parentFrame = parentFrm;
        this.theSubAction = aSubAction;
        this.dialogMode = dlgMode;
        setupPanels();
    }

    /**
     * Call initialize() afte the constructor and after calling initvariables
     */
    public void initialize() {
        //setupPanels();
        //moved here to fix section metadata edit bug
        if (ooDocument.currentSection() != null) {
            sectionMetadataEditor = new SectionMetadataEditor(getMetadataEditorString());
            if (getDialogMode() == SelectorDialogModes.TEXT_EDIT) {
                editSectionName = ooDocument.currentSectionName();
            }
        }
        init();
        captureCursorRange();
    }

    /**
     * Records the selected cursor range on launch of the selector form
     */
    private void captureCursorRange() {
        this.capturedCursorRange = ooDocument.getSelectionRangeIndex(0);
    }

    /**
     * Compares the captured Cursor range with the current selected cursor range,
     * fail if the user has changed the selection range after the launch of th eselector dialog
     * @param currentRange
     * @return
     */
    private boolean compareCursorRange (XTextRange currentRange) {
        boolean bState = false;
        try {
            //first get the range compare object
            XTextRangeCompare objCompare = ooQueryInterface.XTextRangeCompare(ooDocument.getTextDocument().getText());
            //now compare the currentRange with the saved Range.
            if (objCompare.compareRegionStarts(this.capturedCursorRange, currentRange) == 0) {
                if (objCompare.compareRegionEnds(this.capturedCursorRange, currentRange) == 0) {
                    //regions are equal return true;
                    bState =  true;
                } else {
                    bState = false;
                }
            } else {
                bState = false;
            }
            
        } catch (IllegalArgumentException ex) {
            log.error("compareCursorRange", ex);
        }
        return bState;
    }
    /**
     * current section name in edit mode, in all other modes, returns blank
     * @return
     */
    public String getEditSectionName() {
        return editSectionName;
    }

    protected void makeMetaEditable() {
        if (sectionMetadataEditor != null) {
            sectionMetadataEditor.bMetadataEditable = true;
        }
    }

    private void initListeners() {
        //instead of calling doApplies() we use an action listener which splites
        //the apply into a background thread and error return on the swing thread
       btnApply.addActionListener(new applyActionListener());

        btnCancel.addActionListener(new ActionListener() {

            // (rm, feb 2012)- actionListener not attached to
            // any JButton
            public void actionPerformed(ActionEvent arg0) {
                containerFrame.dispose();
            }
        });
    }


    /**
     * Various apply states for the form -- return by the swingworker apply
     */
    enum ApplyState {
        preApplyFailed,
        preApplySuccess,
        validationFailed,
        validationSuccess,
        applySuccess,
        applyFailed,
        postApplySuccess,
        postApplyFailed
    }


    /**
     * Apply swing worker for selector form
     */
    class applyActionListener implements ActionListener {

        // !+ ACTION_RECONF (rm, jan 2012) - added comments here
        // this method applies a given state t
        /**
         * Run the button action in a swingworker thread, so the UI disabling happens immediately
         */
        class buttonActionRunner extends SwingWorker<ApplyState, Void> {

            JButton sourceButton;

            public buttonActionRunner(JButton origButton) {
                this.sourceButton = origButton;
            }

            protected ApplyState doInBackground() throws Exception {
                    //first call preMainApply
                    boolean bReturnMain = false;

                    //now submit the individual panels
                    boolean bValidate = true;
                    for (panelInfo valPanels : getActivePanels()) {
                        //validate the individual panels
                        boolean bState = valPanels.getPanelObject().doValidate();
                        if (!bState) {
                            bValidate = false;
                            log.error("bState indicates that some validations failed");
                        }
                    }
                   //if some validations failed, break from the swingworker thread
                    //and return validationFailed
                   if (!bValidate) {
                        log.error("doApplies : some Validations failed");
                        return  ApplyState.validationFailed;
                    }


                    // !+CHANGING OPERATION SEQUENCE (rm, jan 2012)
                    // moved this code to here to ensure that the highlighting
                    // of the section is only done if user has added the motion
                    // details
                    bReturnMain = preMainApply();
                    if (!bReturnMain) {
                       //break from the swingworker if preApply failed
                       ApplyState retState = ApplyState.preApplyFailed;
                       return retState;
                     }

                   //now apply the individual forms
                   for (panelInfo ppPanels : getActivePanels()) {
                        boolean bState = ppPanels.getPanelObject().doApply();
                        //if the apply failed return applyFailed and break from the worker thread
                        if (!bState) {
                            log.error("doApplies : Submission failed on:" + ppPanels.panelName);
                            ApplyState applyState = ApplyState.applyFailed;
                            return applyState;
                        }
                    }
                  //call post Apply
                  boolean bRetPostApply = postMainApply();
                  if (!bRetPostApply ) {
                     return ApplyState.postApplyFailed;
                   } else {
                      return ApplyState.postApplySuccess;
                  }
            }

            /**
             * Called on the swing EDT - after doInBackground returns
             */
            @Override
            public void done() {
               ApplyState applyState = null;
                try {
                    parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    applyState  = get();
                    switch (applyState) {
                        /**
                         * Show message boxes
                         */
                        case validationFailed :
                             sourceButton.setEnabled(true);
                             displayErrors();
                             break;
                        case applyFailed:
                             sourceButton.setEnabled(true);
                            displayErrors();
                             break;
                        default:
                            sourceButton.setEnabled(false);
                            containerFrame.dispose();
                            break;
                    }
                } catch (InterruptedException ex) {
                    log.error("done(),", ex);
                } catch (ExecutionException ex) {
                    log.error("done(),", ex);
                }
            }
        }

        /**
         * Route the button apply and call the apply swingworker 
         * @param e
         */
        public synchronized void actionPerformed(ActionEvent e) {
            //get the button originating the event
            final JButton sourceButton = (JButton) e.getSource();
            //disable the button immediately
            sourceButton.setEnabled(false);
            //set the wait cursor
            parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //call the swingworker thread for the button event            
            (new buttonActionRunner(sourceButton)).execute();
        }
    }

    /**
     * Overridable from derived class to do processing before the contents of the form are applied
     * @return
     */
    public boolean preMainApply() {
        //check for current range selection,
        //if it has changed since launch, return an error.
        if (!this.compareCursorRange(ooDocument.getSelectionRangeIndex(0))) {
             this.addErrorMessage(this,  this, "The selection of text on the "
                     + "document was moved after the launch of the metadata "
                     + "entry dialog.\n Please close the dialog and re-do the "
                     + "action to be able to proceed");
            return false;
        }

        switch (getDialogMode()) {
            case TEXT_SELECTED_EDIT:
                return preApplySelectedEdit();
            case TEXT_SELECTED_INSERT:
                return preApplySelectedInsert();
            case TEXT_INSERTION:
                return preApplyInsert();
            case TEXT_EDIT:
                return preApplyEdit();
            default:
                return false;
        }
    }

    public boolean preApplySelectedEdit() {
        return true;
    }

    public boolean preApplySelectedInsert() {
        return true;
    }

    public boolean preApplyInsert() {
        return true;
    }

    public boolean preApplyEdit() {
        return true;
    }

    /**
     * Overridable from derived class to do processing after the contents of the form have been applied
     * @return
     */
    public boolean postMainApply() {
        switch (getDialogMode()) {
            case TEXT_SELECTED_EDIT:
                return postApplySelectedEdit();
            case TEXT_SELECTED_INSERT:
                return postApplySelectedInsert();
            case TEXT_INSERTION:
                return postApplyInsert();
            case TEXT_EDIT:
                return postApplyEdit();
            default:
                return false;
        }
    }

    public boolean postApplySelectedEdit() {
        return true;
    }

    public boolean postApplySelectedInsert() {
        return true;
    }

    public boolean postApplyInsert() {
        return true;
    }

    public boolean postApplyEdit() {
        return true;
    }

    private void doApplies() {
        // getActivePanels()
        boolean bReturnMain = preMainApply();
        if (!bReturnMain) {
            return;
        }
        boolean bValidate = true;
        for (panelInfo valPanels : getActivePanels()) {
            boolean bState = valPanels.getPanelObject().doValidate();
            if (!bState) {
                bValidate = false;
            }
        }

        if (!bValidate) {
            log.error("doApplies : some Validations failed");
            displayErrors();
            return;
        }

        for (panelInfo ppPanels : getActivePanels()) {
            boolean bState = ppPanels.getPanelObject().doApply();
            if (!bState) {
                log.error("doApplies : Submission failed on:" + ppPanels.panelName);
                displayErrors();
                return;
            }
        }

        boolean bRetPostApply = postMainApply();

        //disable the apply button
        this.btnApply.setEnabled(false);
    //** do not dispose ... since user may be asked to paste content
    //this.containerFrame.dispose();
    }

    public void displayErrors() {
        org.bungeni.extutils.MessageBox.OK(this, ErrorMessagesAsString());
        clearErrorMessages();
    }

    public OOComponentHelper getOoDocument() {
        return ooDocument;
    }

    public JFrame getParentFrame() {
        return parentFrame;
    }

    /**
    public toolbarSubAction getTheAction() {
        return theAction;
    }
    **/
    
    public toolbarAction getTheSubAction() {
        return theSubAction;
    }

    public SelectorDialogModes getDialogMode() {
        return dialogMode;
    }


    // !+ ADDED COMMENTS TO METHOD
    /**
     * This method determines the panel to display based on the
     * panelClass arg passed. This panel is to be attached to the main
     * JDialog BaseMetadataContainerPanel
     * @param panelClass - panel to be attached to BaseMetadataContainerPanel
     * @return
     */
    public static IMetadataContainerPanel getContainerPanelObject(String panelClass) {
        IMetadataContainerPanel panel = null;
        try {
            Class containerPanel = Class.forName(panelClass);
            panel = (IMetadataContainerPanel) containerPanel.newInstance();
        } catch (InstantiationException ex) {
            log.debug("getContainerPanelObject :" + ex.getMessage());
        } catch (IllegalAccessException ex) {
            log.debug("getContainerPanelObject :" + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            log.debug("getContainerPanelObject :" + ex.getMessage());
        } catch (NullPointerException ex) {
            log.debug("getContainerPanelObject :" + ex.getMessage());
        } finally {
            return panel;
        }
    }

    public void enableAllChildPanels(boolean bState) {
          for (panelInfo ppPanel : getActivePanels()) {
              ppPanel.getPanelObject().enableChildControls(bState);
          }
    }
  
    public void setContainerFrame(Window contFrame) {
        this.containerFrame = contFrame;
    }

    public void setOOComponentHelper(OOComponentHelper ooDoc) {
        this.ooDocument = ooDoc;
    }

    public abstract java.awt.Component getPanelComponent();
  //  protected ArrayList<panelInfo> m_allPanels = new ArrayList<panelInfo>(0);
    /*
    {
    {
    add(new panelInfo("Title","org.bungeni.editor.selectors.debaterecord.masthead.Title"));
    add(new panelInfo("TabledDocuments", "org.bungeni.editor.selectors.debaterecord.masthead.TabledDocuments"));
    }
    };
     */
    protected ArrayList<panelInfo> m_activePanels = new ArrayList<panelInfo>();

    protected class panelField {

        String fieldName;
        panelInfo containerPanel;

        public panelField (String fname, panelInfo cpanel) {
            fieldName = fname;
            containerPanel = cpanel;
        }

        public panelInfo getPanel(){
            return containerPanel;
        }

        public String getName(){
            return fieldName;
        }

    }

    protected HashMap<panelField, Boolean> fieldStates = new HashMap<panelField, Boolean>();

    /*
    {
    {
    add(new panelInfo("Title","org.bungeni.editor.selectors.debaterecord.masthead.Title"));
    add(new panelInfo("TabledDocuments", "org.bungeni.editor.selectors.debaterecord.masthead.TabledDocuments"));
    }
    };*/

    // (rm, feb 2012) - the panels are obtained from an xml file rather than
    // from the db,arg for accquirePanels uses action name
    protected void setupPanels(){
        //load the active panels for the current profile
        String currentActiveProfile = BungeniEditorPropertiesHelper.getActiveProfile();
        String sDocType = BungeniEditorPropertiesHelper.getCurrentDocType();

        // get the parent action
        // String sAction = this.theAction.sub_action_name();
        //String sAction = this.theSubAction.parent_action_name();
         
        String sSubAction = this.theSubAction.sub_action_name();
        // accquirePanels(sDocType, sAction, currentActiveProfile);
        accquirePanels(sDocType, sSubAction, currentActiveProfile);
    }

    class iteratePanels implements IQueryResultsIterator {
        ArrayList<String> selectorDialogClass = new ArrayList<String>(0);

        public boolean iterateRow(QueryResults mQR, Vector<String> rowData) {
            selectorDialogClass.add(mQR.getField(rowData, "SELECTOR_DIALOG"));
            return true;
        }

        public ArrayList<String> getSelectorDialogs(){
            return selectorDialogClass;
        }
    }

    // !+ ADDED COMMENTS TO CODE +++
    /**
     * This method determines the active panels that have to be attached to
     * the main JDialog
     * @param docType
     * @param actionName
     * @param profileName
     */
    private void accquirePanels(String docType, String actionName, String profileName){
        try {
            // get the action
            Element action = DocumentActionsReader.getInstance().getDocumentActionByName(actionName);

            // get the router child element(not attribute)
            String parentDialog = action.getChild("router").getAttributeValue("dialog");
          
            if (null!= parentDialog)
            {
                // get the panels for the dialog
                // from the selector panels
                ArrayList sPanels = DocumentActionsReader.getInstance()
                        .getChildDialogs(parentDialog);
                
                // iterate through the element getting the child panels
                for(int i = 0 ; i <sPanels.size() ; i++){
                    // get the class for the panel
                    Element sPanel = (Element) sPanels.get(i);
                    String dialogClassName = sPanel.getAttributeValue("class") ;
                    this.m_activePanels.add(new panelInfo(BungeniUUID.getStringUUID(),
                            sPanel.getAttributeValue("class")));
                }
            }

        } catch (JDOMException ex) {
            log.error("Error obtaining the panels for dialog : " +ex);
        } catch (IOException ex) {
            log.error("Error obtaining the panels for dialog : " +ex);
        }
        
       
        //
        /**
           BungeniClientDB db        = BungeniClientDB.defaultConnect();
           QueryResults qr = db.ConnectAndQuery(SettingsQueryFactory.Q_FETCH_SELECTOR_DIALOGS(docType,
                                                                        actionName,
                                                                        profileName));
           iteratePanels panelsIterate = new iteratePanels();
           qr.resultsIterator(panelsIterate);
           ArrayList<String> ppanels = panelsIterate.getSelectorDialogs();

           //
           for (String sPanel : ppanels) {
                this.m_activePanels.add(new panelInfo(BungeniUUID.getStringUUID(), sPanel));
            }
         **/
    }

    public void postPanelSetup() {
        return;
    }

    protected ArrayList<panelInfo> getActivePanels() {
        return m_activePanels;
    }

    public void updateAllPanels() {
        for (panelInfo p : getActivePanels()) {
            p.getPanelObject().doUpdateEvent();
        }
    }

    protected void init() {
        //set null borders
        setBorder(null);
        paneMain.setBorder(null);
        paneMain.setBackground(BungeniEditorPropertiesHelper.getDialogBackColor());
        //JXTaskPaneGroup mainTPgroup = new JXTaskPaneGroup();
        //mainTPgroup.setsetTitle(this.theAction.action_display_text() + " ::");
        //mainTPgroup.getgetContentPane().setBackground(BungeniEditorPropertiesHelper.getDialogBackColor());
        //mainTPgroup.setBackground(BungeniEditorPropertiesHelper.getDialogBackColor());
        for (panelInfo panelInf : getActivePanels()) {
            IMetadataPanel panel = panelInf.getPanelObject();
            panel.initVariables(this);
            paneMain.add(panel.getPanelComponent());
        }
        paneMain.setCollapsed(false);
        //paneMain.add(mainTPgroup);
    //   paneMain.add(tpgTD);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnApply = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        paneMain = new org.jdesktop.swingx.JXTaskPane();

        btnApply.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/selectors/Bundle"); // NOI18N
        btnApply.setText(bundle.getString("BaseMetadataContainerPanel.btnApply.text")); // NOI18N
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyActionPerformed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnCancel.setText(bundle.getString("BaseMetadataContainerPanel.btnCancel.text")); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(btnApply, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86))
            .addComponent(paneMain, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(paneMain, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnApply)))
        );

        getAccessibleContext().setAccessibleName("Enter Masthead");
    }// </editor-fold>//GEN-END:initComponents

    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyActionPerformed

    }//GEN-LAST:event_btnApplyActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private org.jdesktop.swingx.JXTaskPane paneMain;
    // End of variables declaration//GEN-END:variables

  
}
