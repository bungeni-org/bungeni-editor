package org.bungeni.editor.dialogs;

import org.bungeni.utils.BungeniFrame;
import org.bungeni.utils.BungeniDialog;
import org.bungeni.utils.BungeniEditorProperties;
import org.bungeni.utils.BungeniEditorPropertiesHelper;
import ag.ion.bion.officelayer.application.OfficeApplicationException;
import ag.ion.bion.officelayer.document.DocumentException;
import ag.ion.noa.NOAException;
import ca.odell.glazedlists.swing.EventComboBoxModel;
import com.sun.star.lang.XComponent;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.editor.BungeniOOoLayout;
import org.bungeni.editor.config.DocumentActionsReader;
import org.bungeni.editor.panels.impl.ITabbedPanel;
import org.bungeni.editor.panels.factory.TabbedPanelFactory;
import org.bungeni.ooo.BungenioOoHelper;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.editor.actions.EditorActionFactory;
import org.bungeni.editor.actions.IEditorActionEvent;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.dialogs.BungeniDocumentSource.BungeniDocuments;
import org.bungeni.editor.dialogs.BungeniJSoupDocument.Attachment;
import org.bungeni.editor.metadata.BaseEditorDocMetaModel;
import org.bungeni.editor.metadata.editors.MetadataEditorContainer;
import org.bungeni.editor.noa.BungeniNoaFrame;
import org.bungeni.editor.noa.BungeniNoaFrame.DocumentComposition;
import org.bungeni.editor.noa.BungeniNoaTabbedPane;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.editor.selectors.metadata.MetadataEditor;
import org.bungeni.editor.toolbar.target.BungeniToolbarTargetProcessor;
import org.bungeni.extutils.*;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.ooo.ooDocMetadata;
import org.jdom.Element;
import org.jsoup.Jsoup;

/**
 * This is a single class since there is only 1 tabbed panel allowed in the system
 * @author  Ashok Hariharan
 */
public class editorTabbedPanel extends javax.swing.JPanel {

    private static editorTabbedPanel thisPanel = null;

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(editorTabbedPanel.class.getName());
  
    private OOComponentHelper ooDocument;
  
    private String ROOT_SECTION = BungeniEditorPropertiesHelper.getDocumentRoot();
    private String currentSelectedSectionName = "";

    private boolean program_refresh_documents = false;

    private EventComboBoxModel<DocumentComposition> listdocsModel = null;

    private ArrayList<ITabbedPanel> m_tabbedPanelMap = new ArrayList<ITabbedPanel>();
 
    /**
     *
     */
    private editorTabbedPanel() {
        
    }

    /**
     * Does an instance exist ?
     * @return
     */
    public static boolean isInstanceNull(){
        return (null == thisPanel);
    }

    /**
     * Returns a singleton instance of editorTabbedPanel
     * The Panel is not initialized yet - you still need to call init()
     * @return
     */
    public static editorTabbedPanel getInstance(){
        if (null == thisPanel) {
            thisPanel = new editorTabbedPanel();
        }
        return thisPanel;
    }

    /**
     *
     * @param impComponent
     * @param pFrame
     */
    public void init(DocumentComposition dc) {
        initMain(dc.getDocument().getXComponent());
        initComponents();
        CommonUIFunctions.compOrientation(this);
       
        initProviders();
        log.debug("calling initOpenDOcuments");
        initOpenDocuments();
        initTabbedPanes();
        initModeLabel();
        initOOoLayout();
        initExternalListeners();
    }

    private void initMain(XComponent impComponent) {
        log.debug("constructor:editortabbedpanel");
        this.ooDocument = new OOComponentHelper(impComponent,  BungenioOoHelper.getInstance().getComponentContext());
    }


    private void initProviders() {
        org.bungeni.editor.providers.DocumentSectionProvider.initialize(this.ooDocument);
    }

    private void updateProviders() {
        org.bungeni.editor.providers.DocumentSectionProvider.updateOOoHandle(this.ooDocument);
    }

    private void initTabbedPanes() {
        log.debug("InitTabbedPanes: begin");
        m_tabbedPanelMap = TabbedPanelFactory.getPanelsByDocType(BungeniEditorProperties.getEditorProperty("activeDocumentMode"));
        for (ITabbedPanel panel : m_tabbedPanelMap) {
            try {
                  panel.setOOComponentHandle(ooDocument);
                  panel.setParentHandles(parentFrame(), this);
                  panel.initialize();
                  this.jTabsContainer.add(panel.getPanelTitle(), panel.getObjectHandle());
            } catch (java.lang.Exception ex) {
                log.error("initTabbedPanes : error while initializaing pane :" + panel.getPanelTitle() , ex);
            }
        }
        log.debug("InitTabbedPanes: finished loading");
    }


    /***
     * This function is used to install listeners on components external to
     * the editorTabbedPanel. 
     */
    private void initExternalListeners(){
        //Add a change listener on the BungeniNoaTabbedPane
        //we listen for the change event and select an appropriate
        //document int the
        BungeniNoaTabbedPane.getInstance().getTabbedPane().
                addChangeListener(new ChangeListener(){

                    public void stateChanged(ChangeEvent e) {
                        //get the newly selected panel in the tab
                        Component c = BungeniNoaTabbedPane.getInstance().
                                getTabbedPane().getSelectedComponent();
                        JPanel panel = (JPanel) c;
                        DocumentComposition dcSelected = (DocumentComposition) cboListDocuments.getSelectedItem();
                        //if the selected panel is the same as the main panel return
                        if (dcSelected.equalsByNoaPanel(panel)) return;
                        int currentIndex = 0;
                        //otherwise iterate through the listed documents in the cboListDocuments
                        //combobox
                        for (int i = 0; i < listdocsModel.getSize(); i++) {
                            currentIndex = i;
                            DocumentComposition dc =
                                    (DocumentComposition) listdocsModel.getElementAt(i);
                            if (dc.equalsByNoaPanel(panel)) {
                                listdocsModel.setSelectedItem(dc);
                                //this is the document to select
                                break;
                            }
                        }

                    }

        });

    }

    private void initOOoLayout(){
        BungeniOOoLayout.getInstance().applyLayout(ooDocument);
    }

    private void updateTabbedPanes() {
        for (ITabbedPanel panel : m_tabbedPanelMap) {
            panel.setOOComponentHandle(ooDocument);
            panel.refreshPanel();
        }
    }


    protected void setOODocumentObject(OOComponentHelper ooDoc) {
        this.ooDocument = ooDoc;
    }

    public OOComponentHelper getOODocumentObject() {
        return this.ooDocument;
    }


    /*
     * AH-23-05-11 -- document selector combo
     * The document selector combo box uses the EventComboBoxModel.
     * using this model we simply update the underlying list and the model
     * is synced to the list dynamically.
     *
     */
    private void initListDocuments() {
        log.debug("initListDocuments: init");
        listdocsModel = 
                new EventComboBoxModel<DocumentComposition>(
                BungeniNoaFrame.getInstance().getOfficeDocuments());
        this.cboListDocuments.setModel(listdocsModel);
    }

    private void initOpenDocuments() {
        log.debug("initOpenDocuments: calling");
        initListDocuments();
        initSelectionInOpenDocuments();
        //we attach the listener after selecting the document
        initListDocumentsListener();
    }

    /***
     *Initialize the selectio in the list of open documents
     */
    private void initSelectionInOpenDocuments() {
        //AH-20-05-2011
        /**
         * Initially there is only document open so select the first item in the list
         */
         
        listdocsModel.setSelectedItem(listdocsModel.getElementAt(0));
    }

    private void initListDocumentsListener() {
        this.cboListDocuments.addActionListener(new cboListDocumentsActionListener());
    }

    private void initModeLabel() {
        String labelText = this.lblCurrentMode.getText();
        labelText = labelText.replaceAll("%s", BungeniEditorPropertiesHelper.getCurrentDocType());
        this.lblCurrentMode.setText(labelText);
    }

    /**
     * Static function invoked by JPanel containing document switcher.
     * @param currentlySelectedDoc currently selected document in switched
     * @param same
     */
    public void updateMain(DocumentComposition dc, boolean same) {

        setOODocumentObject(new
                OOComponentHelper(dc.getDocument().getXComponent(),
                        BungenioOoHelper.getInstance().getComponentContext()));
        initOOoLayout();
        updateProviders();
        updateTabbedPanes();
    } 

    /*
     *this is invoked on window closing, by the JFrame that contains the panel
     */
    public void cleanup() {
        for (ITabbedPanel panel : m_tabbedPanelMap) {
            panel.cleanup();
        }
    }
    
    private editorTabbedPanel self() {
        return this;
    }

    public Component getComponentHandle() {
        return this;
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGrpBodyMetadataTarget = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTree2 = new javax.swing.JTree();
        jTabsContainer = new javax.swing.JTabbedPane();
        cboListDocuments = new javax.swing.JComboBox();
        lblCurrentlyOpenDocuments = new javax.swing.JLabel();
        btnOpenDocument = new javax.swing.JButton();
        lblCurrentMode = new javax.swing.JLabel();
        btnNewDocument = new javax.swing.JButton();
        btnSaveDocument = new javax.swing.JButton();
        cboOpenFrom = new javax.swing.JComboBox();

        jScrollPane2.setViewportView(jTree1);

        jScrollPane3.setViewportView(jTree2);

        setFont(new java.awt.Font("Tahoma", 0, 10));

        jTabsContainer.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabsContainer.setFont(new java.awt.Font("Tahoma", 0, 10));

        cboListDocuments.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        cboListDocuments.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblCurrentlyOpenDocuments.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/dialogs/Bundle"); // NOI18N
        lblCurrentlyOpenDocuments.setText(bundle.getString("editorTabbedPanel.lblCurrentlyOpenDocuments.text")); // NOI18N

        btnOpenDocument.setFont(new java.awt.Font("DejaVu Sans", 0, 9)); // NOI18N
        btnOpenDocument.setText(bundle.getString("editorTabbedPanel.btnOpenDocument.text")); // NOI18N
        btnOpenDocument.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnOpenDocument.setIconTextGap(2);
        btnOpenDocument.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenDocumentActionPerformed(evt);
            }
        });

        lblCurrentMode.setForeground(java.awt.Color.red);
        lblCurrentMode.setText(bundle.getString("editorTabbedPanel.lblCurrentMode.text")); // NOI18N

        btnNewDocument.setFont(new java.awt.Font("DejaVu Sans", 0, 9));
        btnNewDocument.setText(bundle.getString("editorTabbedPanel.btnNewDocument.text")); // NOI18N
        btnNewDocument.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnNewDocument.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewDocumentActionPerformed(evt);
            }
        });

        btnSaveDocument.setFont(new java.awt.Font("DejaVu Sans", 0, 9));
        btnSaveDocument.setText(bundle.getString("editorTabbedPanel.btnSaveDocument.text")); // NOI18N
        btnSaveDocument.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnSaveDocument.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveDocumentActionPerformed(evt);
            }
        });

        cboOpenFrom.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "File System", "Bungeni", "Plone" }));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(lblCurrentlyOpenDocuments, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 267, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(jTabsContainer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 267, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cboListDocuments, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 235, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblCurrentMode)
                    .add(layout.createSequentialGroup()
                        .add(btnOpenDocument, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(cboOpenFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnNewDocument, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnSaveDocument, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(lblCurrentlyOpenDocuments)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cboListDocuments, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnOpenDocument)
                    .add(btnNewDocument)
                    .add(btnSaveDocument)
                    .add(cboOpenFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblCurrentMode)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTabsContainer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void btnOpenDocumentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenDocumentActionPerformed
// TODO add your handling code here:
    String sSelection = (String) this.cboOpenFrom.getSelectedItem();
    if (sSelection.equals("File System")) {
        loadDocumentFromFileSystemInPanel();
    } else if (sSelection.equals("Bungeni")) {
        loadDocumentFromBungeniInPanel();
    } else if (sSelection.equals("Plone")) {
        loadDocumentFromPloneInPanel();
    }
}//GEN-LAST:event_btnOpenDocumentActionPerformed

private void btnNewDocumentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewDocumentActionPerformed
// TODO add your handling code here:
    newDocumentInPanel();
}//GEN-LAST:event_btnNewDocumentActionPerformed

private void btnSaveDocumentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveDocumentActionPerformed
// TODO add your handling code here:
    saveOpenDocument();
}//GEN-LAST:event_btnSaveDocumentActionPerformed

    public void saveOpenDocument() {
        //if the document is on disk simply save it
        if (ooDocument.isDocumentOnDisk()) {
            ooDocument.saveDocument();
        } else {
            //if the document isnt on disk, we force metadata setting.
            MetadataEditorContainer.launchMetadataEditor(ooDocument, SelectorDialogModes.TEXT_INSERTION);
        }

    }

    private JFrame parentFrame(){
        return BungeniNoaFrame.getInstance();
    }

    public void newDocumentInPanel() {
        String templatePath = BungeniEditorProperties.getEditorProperty(BungeniEditorPropertiesHelper.getCurrentDocType() + "_template");
       //we dont use the OpenDocumentAgent anymore -- as we are using noa, which
       //integrates openoffice window into the native view -- 
       //OOo document needs to be opened in the event dispatch thread, not
       //in the background thread
       //perhaps we run just constructOOOframe in the swingworker ?
       BungeniNoaFrame frame = BungeniNoaFrame.getInstance();
       DocumentComposition dc = null;
        try {
           dc = frame.loadDocumentInPanel(templatePath, true);
           BungeniNoaTabbedPane.getInstance().setActiveTab(dc.getPanel().getPanel());
           this.launchMetadataSetter(dc);
        } catch (OfficeApplicationException e) {
            log.error("Error while opening document from editorTabbedPanel" , e);
        } catch (NOAException e) {
            log.error("Error while opening document from editorTabbedPanel" , e);
        } catch (DocumentException e) {
                log.error("Error while opening document from editorTabbedPanel" , e);
            }
        }

        //!+HACK ( below is used only by loadDocumentFromBungeniInPanel()
        private BungeniAppConnector appConnector = null;
        
        public synchronized void loadDocumentFromBungeniInPanel(){
           // BungeniDialog dlg = new BungeniDialog(
           //         this.parentFrame() ,
           //         "Select a Document",
           //         true
           //         );
           // BungeniDocumentSourceSelectDocument doc =
           //         new BungeniDocumentSourceSelectDocument(dlg);
           // doc.init();
           // dlg.getContentPane().add(doc);
           // dlg.pack();
           // FrameLauncher.CenterFrame(dlg);
           // dlg.setVisible(true);
            String sDocURL = (String)JOptionPane.showInputDialog(
                    this.parentFrame(),
                    "Enter the URL of the document to Import",
                    "Import document from Bungeni",
                    JOptionPane.QUESTION_MESSAGE);

            //If a string was returned, say so.
            if ((sDocURL != null) && (sDocURL.length() > 0)) {
                //open document here
                  //set the wait cursor
               final BungeniDocumentSource.BungeniDocuments selectedDocument = new BungeniDocuments("", sDocURL);
               this.parentFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
              //call the swingworker thread for the button event
               SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    if (appConnector == null) {
                        appConnector = new BungeniAppConnector(
                                "10.0.2.2",
                                "8081",
                                "login",
                                "clerk.p1_01",
                                "member"
                                );
                    }
                    DefaultHttpClient client = appConnector.login();
                    final HttpGet geturl = new HttpGet(selectedDocument.url);
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    String responseBody = "";
                    try {
                        responseBody = client.execute(geturl, responseHandler);
                    } catch (IOException ex) {
                        Logger.getLogger(editorTabbedPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //parse response Body
                      parentFrame().setCursor(Cursor.getDefaultCursor());
                      org.jsoup.nodes.Document doc = Jsoup.parse(responseBody);
                      BungeniJSoupDocument jdoc = new BungeniJSoupDocument(doc);
                      BungeniDialog dlgatts = new BungeniDialog(
                                parentFrame() ,
                                "Import an Attachment",
                                true
                                );
                        BungeniDocumentAttListPanel docAtts =
                                new BungeniDocumentAttListPanel(dlgatts, jdoc);
                        docAtts.init();
                        dlgatts.getContentPane().add(docAtts);
                        dlgatts.pack();
                        FrameLauncher.CenterFrame(dlgatts);
                        dlgatts.setVisible(true);
                        if (docAtts.getSelectedAttachment() != null) {
                            Attachment att = docAtts.getSelectedAttachment();
                            JOptionPane.showMessageDialog(null, att);
                        }


                    //sourceButton.setEnabled(true);
                }
            });
            
            }
    }

    public synchronized void loadDocumentFromPloneInPanel(){
        
    }

    public synchronized void loadDocumentFromFileSystemInPanel() {
        String basePath = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator + "workspace" + File.separator + "files";
        File openFile = CommonFileFunctions.getFileFromChooser(basePath,
                new org.bungeni.utils.fcfilter.ODTFileFilter(),
                JFileChooser.FILES_ONLY,
                parentFrame());
        if (openFile != null) {
            boolean bActive = false;
            int nConfirm = MessageBox.Confirm(parentFrame(),
                    bundle.getString("make_active"),
                    bundle.getString("change_active"));
            if (JOptionPane.YES_OPTION == nConfirm) {
                bActive = true;
            }
            String fullPathToFile = openFile.getAbsolutePath();
            //we dont use the OpenDocumentAgent anymore -- as we are using noa,
            //OOo document needs to be opened in the event dispatch thread, not 
            //in the background thread
            //OpenDocumentAgent openDocAgent = new OpenDocumentAgent(fullPathToFile, bActive, false);
            //openDocAgent.execute();
            BungeniNoaFrame frame = BungeniNoaFrame.getInstance();
            DocumentComposition dc = null;
            try {
               dc = frame.loadDocumentInPanel(fullPathToFile, false);
               if (bActive){
                   int lastTab = BungeniNoaTabbedPane.getInstance().getTabbedPane().getTabCount() - 1;
                   if (lastTab != 0 ) {
                        BungeniNoaTabbedPane.getInstance().getTabbedPane().setSelectedIndex(lastTab);
                   }
               }
            } catch (OfficeApplicationException e) {
                log.error("Error while opening document from editorTabbedPanel" , e);
            } catch (NOAException e) {
                log.error("Error while opening document from editorTabbedPanel" , e);
            } catch (DocumentException e) {
                log.error("Error while opening document from editorTabbedPanel" , e);
            }
        }

    }

    /**
     * Launches the metadata editor of the seciton
     */
    private void launchSectionMetadataEditor() {
        //get the section metadata
        HashMap<String, String> currentSec = ooDocument.getSectionMetadataAttributes(ooDocument.currentSectionName());
        //check if the section has a metadata editor
        if (currentSec.containsKey(MetadataEditor.MetaEditor)) {
            //parse the metadata editor string into a subAction object
            String metaEditorString = currentSec.get(MetadataEditor.MetaEditor);
            BungeniToolbarTargetProcessor btp = new BungeniToolbarTargetProcessor(metaEditorString);
            String documentType = BungeniEditorPropertiesHelper.getCurrentDocType();
            Element subActionElement = null;
            try {
                //create the subAction object out of the settings metadata
                subActionElement = DocumentActionsReader.getInstance().getDocumentActionByName(
                        documentType,
                        btp.getSubActionName()
                        );
       
                if (subActionElement != null) {
                    toolbarAction subActionObj = new toolbarAction(subActionElement);
                    if (!CommonStringFunctions.emptyOrNull(btp.getActionValue())) {
                        subActionObj.setActionValue(btp.getActionValue());
                    }
                    subActionObj.setSelectorDialogMode(SelectorDialogModes.TEXT_EDIT);
                    IEditorActionEvent event = EditorActionFactory.getEventClass(subActionObj);
                    if (event != null) {
                        event.doCommand(ooDocument, subActionObj, parentFrame());
                    } else {
                        log.error("launchSectionMetadataEditor : no IEditorActionEvent object was created for " + subActionObj.sub_action_name());
                    }
                } else {
                    log.error("subActionElement was null ! ");
                }
            } catch (Exception ex) {
               log.error("unable to initialize subAction or failed while setting up toolbarSubAction " , ex);
            } 
        }
    }


    private void launchMetadataSetter(DocumentComposition dc) {
        //check if metadat variable is set
        OOComponentHelper oohc = new OOComponentHelper(dc.getDocument().getXComponent(),
                            BungenioOoHelper.getInstance().getComponentContext());
        ooDocMetadata metaObj = new ooDocMetadata(oohc);
        String sMetaSetProp = metaObj.GetProperty(BaseEditorDocMetaModel.__METADATA_SET_FLAG__);
        //if empty or null ... prompt the metadata
        if (!CommonStringFunctions.emptyOrNull(sMetaSetProp)) {
            //if metadata has been set dont prompt the user for setting the metadata
            if (sMetaSetProp.equals("true")) {
                return;
            }
        }
        String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
        BungeniFrame frm = new BungeniFrame(bundle.getString("frameTitle"));
        frm.initFrame();
        MetadataEditorContainer meta = new MetadataEditorContainer(oohc, frm, SelectorDialogModes.TEXT_INSERTION);
        meta.initialize();
        frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frm.setSize(meta.getPreferredSize());
        frm.add(meta.getPanelComponent());
        frm.setVisible(true);
        frm.setAlwaysOnTop(true);
        FrameLauncher.CenterFrame(frm);
    }
    
    private static boolean structureInitialized = false;

    private static final ResourceBundle bundle = ResourceBundle.getBundle("org/bungeni/editor/dialogs/Bundle");

    public void setProgrammaticRefreshOfDocumentListFlag(boolean bState) {
        this.program_refresh_documents = bState;
    }

 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGrpBodyMetadataTarget;
    private javax.swing.JButton btnNewDocument;
    private javax.swing.JButton btnOpenDocument;
    private javax.swing.JButton btnSaveDocument;
    private javax.swing.JComboBox cboListDocuments;
    private javax.swing.JComboBox cboOpenFrom;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabsContainer;
    private javax.swing.JTree jTree1;
    private javax.swing.JTree jTree2;
    private javax.swing.JLabel lblCurrentMode;
    private javax.swing.JLabel lblCurrentlyOpenDocuments;
    // End of variables declaration//GEN-END:variables

    
    class cboListDocumentsActionListener implements ActionListener {

        //AH-20-05-2011
        /**
        componentHandleContainer oldItem;
        **/
        public void actionPerformed(ActionEvent e) {
            try {
                JComboBox cb = (JComboBox) e.getSource();
                DocumentComposition dc = (DocumentComposition) cb.getSelectedItem();
                updateMain(dc, false);
                //switch tabs here
                JTabbedPane pane = BungeniNoaTabbedPane.getInstance().getTabbedPane();
                //we check if the tab has already been switched -- if it has not
                //been switched, we switched.
                //the tab may have already been switched if the user had manually
                //selected the tab. (see initExternalListeners()for the listener to
                //the tabbed pane

                if (!pane.getSelectedComponent().equals(dc.getPanel().getPanel())) {
                    pane.setSelectedComponent(dc.getPanel().getPanel());
                }
                        
            } catch (RuntimeException ex) {
                log.error("cboListDocuments.actionPerformed = " + ex.getMessage());
                log.error("cboListDocuments.actionPerformed = " + CommonExceptionUtils.getStackTrace(ex));
            }

        }
    }

    /**
    public HashMap<String, IFloatingPanel> getFloatingPanelMap() {
        return this.floatingPanelMap;
    }
     */
}
