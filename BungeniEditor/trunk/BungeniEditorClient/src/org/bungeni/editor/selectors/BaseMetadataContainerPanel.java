
package org.bungeni.editor.selectors;

import com.l2fprod.common.swing.JTaskPaneGroup;
import java.awt.Cursor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.editor.selectors.metadata.SectionMetadataEditor;
import org.bungeni.ooo.OOComponentHelper;

/**
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
    protected toolbarAction theAction = null;
    protected toolbarSubAction theSubAction = null;
    protected SelectorDialogModes dialogMode;
    protected SectionMetadataEditor sectionMetadataEditor = null;
    protected String editSectionName = "";

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

    private String getMetadataEditorString() {
        if (theSubAction != null) {
            return "toolbarSubAction." + theAction.action_name() + "." + theSubAction.sub_action_name();
        } else {
            return "toolbarAction." + theAction.action_name();
        }
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
    public void initVariables(OOComponentHelper ooDoc, JFrame parentFrm, toolbarAction aAction, toolbarSubAction aSubAction, SelectorDialogModes dlgMode) {
        this.ooDocument = ooDoc;
        this.parentFrame = parentFrm;
        this.theAction = aAction;
        this.theSubAction = aSubAction;
        this.dialogMode = dlgMode;
        setupPanels();
    }

    /**
     * Call initialize() afte the constructor and after calling initvariables
     */
    public void initialize() {
        //setupPanels();
        init();
        if (ooDocument.currentSection() != null) {
            sectionMetadataEditor = new SectionMetadataEditor(getMetadataEditorString());
            if (getDialogMode() == SelectorDialogModes.TEXT_EDIT) {
                editSectionName = ooDocument.currentSectionName();
            }
        }
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
                    boolean bReturnMain = preMainApply();
                   if (!bReturnMain) {
                       //break from the swingworker if preApply failed
                       ApplyState retState = ApplyState.preApplyFailed;
                       return retState;
                     }
                    //now submit the individual panels
                    boolean bValidate = true;
                    for (panelInfo valPanels : getActivePanels()) {
                        //validate the individual panels
                        boolean bState = valPanels.getPanelObject().doValidate();
                        if (!bState) {
                            bValidate = false;
                        }
                    }
                   //if some validations failed, break from the swingworker thread
                    //and return validationFailed
                   if (!bValidate) {
                        log.error("doApplies : some Validations failed");
                        return  ApplyState.validationFailed;
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

    public toolbarAction getTheAction() {
        return theAction;
    }

    public toolbarSubAction getTheSubAction() {
        return theSubAction;
    }

    public SelectorDialogModes getDialogMode() {
        return dialogMode;
    }



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
    protected ArrayList<panelInfo> m_allPanels = new ArrayList<panelInfo>(0);
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

    abstract protected void setupPanels();

    protected ArrayList<panelInfo> getAllPanels() {
        return m_allPanels;
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
        JTaskPaneGroup mainTPgroup = new JTaskPaneGroup();
        mainTPgroup.setTitle(this.theAction.action_display_text() + " ::");
        mainTPgroup.getContentPane().setBackground(BungeniEditorPropertiesHelper.getDialogBackColor());
        //mainTPgroup.setBackground(BungeniEditorPropertiesHelper.getDialogBackColor());
        for (panelInfo panelInf : getActivePanels()) {
            IMetadataPanel panel = panelInf.getPanelObject();
            panel.initVariables(this);
            mainTPgroup.add(panel.getPanelComponent());
        }
        mainTPgroup.setCollapsable(false);
        paneMain.add(mainTPgroup);
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

        paneMain = new com.l2fprod.common.swing.JTaskPane();
        btnApply = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        com.l2fprod.common.swing.PercentLayout percentLayout2 = new com.l2fprod.common.swing.PercentLayout();
        percentLayout2.setGap(14);
        percentLayout2.setOrientation(1);
        paneMain.setLayout(percentLayout2);

        btnApply.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/selectors/Bundle"); // NOI18N
        btnApply.setText(bundle.getString("BaseMetadataContainerPanel.btnApply.text")); // NOI18N

        btnCancel.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnCancel.setText(bundle.getString("BaseMetadataContainerPanel.btnCancel.text")); // NOI18N

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
                .addComponent(paneMain, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnApply)))
        );

        getAccessibleContext().setAccessibleName("Enter Masthead");
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private com.l2fprod.common.swing.JTaskPane paneMain;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
    }
}
