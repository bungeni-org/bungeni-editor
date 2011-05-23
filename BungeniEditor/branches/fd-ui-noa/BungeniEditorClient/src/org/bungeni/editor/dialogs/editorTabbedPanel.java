package org.bungeni.editor.dialogs;

import ag.ion.bion.officelayer.application.OfficeApplicationException;
import ag.ion.bion.officelayer.document.DocumentException;
import ag.ion.noa.NOAException;
import ca.odell.glazedlists.swing.EventComboBoxModel;
import ca.odell.glazedlists.swing.EventListModel;
import com.sun.star.lang.XComponent;
import com.sun.star.uno.XComponentContext;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.editor.panels.impl.ITabbedPanel;
import org.bungeni.editor.panels.factory.TabbedPanelFactory;
import org.bungeni.ooo.BungenioOoHelper;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooDocNotes;
import org.bungeni.extutils.MessageBox;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.editor.actions.EditorActionFactory;
import org.bungeni.editor.actions.IEditorActionEvent;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.editor.metadata.BaseEditorDocMetaModel;
import org.bungeni.editor.metadata.editors.MetadataEditorContainer;
import org.bungeni.editor.noa.BungeniNoaFrame;
import org.bungeni.editor.noa.BungeniNoaFrame.DocumentComposition;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.editor.selectors.metadata.SectionMetadataEditor;
import org.bungeni.editor.toolbar.target.BungeniToolbarTargetProcessor;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.extutils.BungeniFrame;
import org.bungeni.extutils.CommonFileFunctions;
import org.bungeni.extutils.CommonStringFunctions;
import org.bungeni.extutils.FrameLauncher;
import org.bungeni.ooo.ooDocMetadata;

/**
 * This is a single class since there is only 1 tabbed panel allowed in the system
 * @author  Ashok Hariharan
 */
public class editorTabbedPanel extends javax.swing.JPanel {

    private static editorTabbedPanel thisPanel = null;

    /**
     * XComponent object, handle to current openoffice document instance
     */
    private XComponent Component;
    private XComponentContext ComponentContext;
    private BungenioOoHelper ooHelper;
    private OOComponentHelper ooDocument;
    private ooDocNotes m_ooNotes;
    private JFrame parentFrame;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(editorTabbedPanel.class.getName());
    private String ROOT_SECTION = BungeniEditorPropertiesHelper.getDocumentRoot();
    private String currentSelectedSectionName = "";

    private boolean mouseOver_TreeDocStructureTree = false;
    private boolean program_refresh_documents = false;
    //AH-20-05-2011
    //private TreeMap<String, editorTabbedPanel.componentHandleContainer> editorMap;
    private String activeDocument;
    private ArrayList<ITabbedPanel> m_tabbedPanelMap = new ArrayList<ITabbedPanel>();
    public static int coordX,  coordY;

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
    public void init(XComponent impComponent, JFrame pFrame) {
        initMain(impComponent, pFrame);
        initComponents();
        initProviders();
        log.debug("calling initOpenDOcuments");
        initOpenDocuments();

        /***** control moved to other dialog...
        updateListDocuments();
         *****/
        initTabbedPanes();
        initModeLabel();
    }

    private void initMain(XComponent impComponent, JFrame parentFrame) {
        log.debug("constructor:editortabbedpanel");

        this.Component = impComponent;
        if (impComponent == null) {
            log.error("constructor:editortabbedpanel impComponent was null");
        }

        this.ComponentContext = BungenioOoHelper.getInstance().getComponentContext();

        /***
         * AH-20-05-2011
         * editorMap = new TreeMap<String, componentHandleContainer>();
         */
        ooDocument = new OOComponentHelper(impComponent, ComponentContext);

        this.parentFrame = parentFrame;
        this.activeDocument = BungeniEditorProperties.getEditorProperty("activeDocumentMode");
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
                  panel.setParentHandles(parentFrame, this);
                  panel.initialize();
                  this.jTabsContainer.add(panel.getPanelTitle(), panel.getObjectHandle());
            } catch (java.lang.Exception ex) {
                log.error("initTabbedPanes : error while initializaing pane :" + panel.getPanelTitle() , ex);
            }
        }
        log.debug("InitTabbedPanes: finished loading");
    }

    private void updateTabbedPanes() {
        for (ITabbedPanel panel : m_tabbedPanelMap) {
            panel.setOOComponentHandle(ooDocument);
            panel.refreshPanel();
        }
    }
    //   private HashMap<String, IEditorPluginAll> loadedPluginsMap = new HashMap<String, IEditorPluginAll>();
    /**
     * Loads the external plugins specified in the EXTERNAL_PLUGINS table.
     */
    /*
    private void initExternalPlugins(){
    ExternalPluginsLoader extLoader = new ExternalPluginsLoader();
    ArrayList<ExternalPlugin> listExtPlugins = extLoader.getExternalPlugins();
    for (ExternalPlugin ep : listExtPlugins ) {
    if (ep.isEnabled) {
    ExternalPluginsFinder epf = new ExternalPluginsFinder(ep.JarFile, ep.Loader);
    //added this check to make sure the plugin load is attempted only if it was found
    if (epf.isPluginFound()) {
    IEditorPluginAll iepAll = epf.getPluginInstance();
    iepAll.setOOComponentHelper(ooDocument);
    iepAll.setInstallDirectory(DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH());
    iepAll.setParentFrame(parentFrame);
    loadedPluginsMap.put(ep.Name, iepAll);
    }
    }
    }
    }*/
  
    /**
     * Used to display section metadata in the tabbed panel
     */
  
  
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
        EventComboBoxModel<DocumentComposition> listdocsModel = 
                new EventComboBoxModel<DocumentComposition>(
                BungeniNoaFrame.getInstance().getOfficeDocuments());
        this.cboListDocuments.setModel(listdocsModel);
    }

    private void initOpenDocuments() {
        log.debug("initOpenDocuments: calling");
        initListDocuments();
        initListDocumentsListener();
        initSelectionInOpenDocuments();
    }

    private void initSelectionInOpenDocuments() {
        //AH-20-05-2011
        //REWORK TODO
        /***
        componentHandleContainer currentDoc = new componentHandleContainer(ooDocument.getDocumentTitle(), ooDocument.getComponent());
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboListDocuments.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            componentHandleContainer foundchc = (componentHandleContainer) model.getElementAt(i);
            if (foundchc.componentKey().equals(currentDoc.componentKey())) {
                model.setSelectedItem(foundchc);
                break;
            }
        }***/
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
     * Depreacted function - as combobox tab switching has been removed.
     */
    private void initSwitchTabs() {
        /*
        int iTabs = jTabsContainer.getTabCount();
        String[] tabs = new String[iTabs];
        for (int i=0; i < iTabs; i++) {
        tabs[i] = jTabsContainer.getTitleAt(i);
        }
        this.cboSwitchTabs.setModel(new DefaultComboBoxModel(tabs));
        cboSwitchTabs.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent arg0) {
        int iSelect = cboSwitchTabs.getSelectedIndex();
        jTabsContainer.setSelectedIndex(iSelect);
        }
        });
         */
    }

    public void setOOoHelper(BungenioOoHelper helper) {
        this.ooHelper = helper;
        initOpenDocuments();
    }

    public void bringEditorWindowToFront() {
        //AH-20-05-2011
        //REWORK TO-DO
        /**
        if (ooDocument.isXComponentValid()) {
            XFrame xDocFrame = ooDocument.getDocumentModel().getCurrentController().getFrame();
            Object docFrameWindow = xDocFrame.getContainerWindow();
            if (docFrameWindow == null) {
                return;
            }

            Object queryInterface = ooQueryInterface.XTopWindow(docFrameWindow);
            if (queryInterface == null) {
                return;
            } else {
                log.debug("Bring selected window to the front");
                ooQueryInterface.XTopWindow(xDocFrame.getContainerWindow()).toFront();
            }
        } **/
    }

    /**
     * Static function invoked by JPanel containing document switcher.
     * @param currentlySelectedDoc currently selected document in switched
     * @param same
     */
    //AH-20-05-2011
    //TODO REWORK
    /**
    public void updateMain(componentHandleContainer currentlySelectedDoc, boolean same) {
        if (same) {
            if (self().program_refresh_documents == true) {
                return;
            } else {
                bringEditorWindowToFront();
            }
        } else {
            if (currentlySelectedDoc == null) {
                log.debug("XComponent is invalid");
            }
            // ooDocument.detachListener();
            log.debug("updateMain : setting new OODocument handle");
            setOODocumentObject(new OOComponentHelper(currentlySelectedDoc.getComponent(), ComponentContext));
            updateProviders();
            //initFields();
            //initializeValues();

            // removed call to collapsiblepane function
            //retrieve the list of dynamic panels from the the dynamicPanelMap and update their component handles
            //updateCollapsiblePanels();
         
            updateTabbedPanes();

            if (self().program_refresh_documents == false) {
                bringEditorWindowToFront();
            }

        }
    } **/

    /*
     *this is invoked on window closing, by the JFrame that contains the panel
     */
    public void cleanup() {
        for (ITabbedPanel panel : m_tabbedPanelMap) {
            panel.cleanup();
        }
    }
    private static int WIDTH_OOo_SCROLLBAR = 25;
    // added for Issue 246, http://code.google.com/p/bungeni-portal/issues/details?id=246

    /*
    @SuppressWarnings("empty-statement")
    private void initFloatingPane() {
        //load the map here
        BungeniFrame floatingFrame = new BungeniFrame();
        IFloatingPanel floatingPanel = FloatingPanelFactory.getPanelClass("toolbarUIPanel");

        floatingPanel.setOOComponentHandle(ooDocument);
        floatingPanel.setParentWindowHandle(floatingFrame);
        floatingPanel.initUI();
        floatingFrame.setTitle(FloatingPanelFactory.panelDescription);
        floatingPanelMap.put("toolbarUIPanel", floatingPanel);
        //panel.setOOoHelper(this.openofficeObject);
        floatingFrame.add(floatingPanel.getObjectHandle());
        //frame.setSize(243, 650);
        floatingFrame.setSize(Integer.parseInt(FloatingPanelFactory.panelWidth), Integer.parseInt(FloatingPanelFactory.panelHeight));
        // floatingFrame.setResizable(false);
        floatingFrame.removeMinMaxClose();
        floatingFrame.setAlwaysOnTop(true);
        floatingFrame.pack();
        floatingFrame.setVisible(true);

        floatingFrame.setResizable(false);

        //position frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = floatingFrame.getSize();
        log.debug("screen size = " + screenSize);
        log.debug("window size = " + windowSize);

        int windowX = screenSize.width - floatingFrame.getWidth() - WIDTH_OOo_SCROLLBAR;
        //int windowY = (screenSize.height - floatingFrame.getHeight())/2;
        int windowY = editorApplicationController.getFrameWindowDimension().y;
        log.debug("initFloatingPane : Window Y =  " + windowY);

        //   floatingFrame.setLocation(windowX, editorTabbedPanel.coordY + 50);  // Don't use "f." inside constructor.
        floatingFrame.setLocation(windowX, parentFrame.getLocation().y);
        floatingFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);


    }

    private void updateFloatingPanels() {
        if (!floatingPanelMap.isEmpty()) {
            Iterator<String> panelNames = floatingPanelMap.keySet().iterator();
            while (panelNames.hasNext()) {

                IFloatingPanel panelObj = floatingPanelMap.get(panelNames.next());
                panelObj.setOOComponentHandle(ooDocument);
                panelObj.updateEvent();
            }
        }
    }
    */
    
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
        btnBringToFront = new javax.swing.JButton();
        btnOpenDocument = new javax.swing.JButton();
        lblCurrentMode = new javax.swing.JLabel();
        btnNewDocument = new javax.swing.JButton();
        btnSaveDocument = new javax.swing.JButton();

        jScrollPane2.setViewportView(jTree1);

        jScrollPane3.setViewportView(jTree2);

        setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jTabsContainer.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabsContainer.setFont(new java.awt.Font("Tahoma", 0, 10));

        cboListDocuments.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        cboListDocuments.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblCurrentlyOpenDocuments.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/dialogs/Bundle"); // NOI18N
        lblCurrentlyOpenDocuments.setText(bundle.getString("editorTabbedPanel.lblCurrentlyOpenDocuments.text")); // NOI18N

        btnBringToFront.setFont(new java.awt.Font("DejaVu Sans", 0, 9));
        btnBringToFront.setText(bundle.getString("editorTabbedPanel.btnBringToFront.text")); // NOI18N
        btnBringToFront.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnBringToFront.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBringToFrontActionPerformed(evt);
            }
        });

        btnOpenDocument.setFont(new java.awt.Font("DejaVu Sans", 0, 9));
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

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(lblCurrentlyOpenDocuments, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 267, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(cboListDocuments, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 235, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, lblCurrentMode, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(btnBringToFront)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnOpenDocument, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnNewDocument, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnSaveDocument, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
            .add(jTabsContainer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 267, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(lblCurrentlyOpenDocuments)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cboListDocuments, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnBringToFront)
                    .add(btnNewDocument)
                    .add(btnSaveDocument)
                    .add(btnOpenDocument))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblCurrentMode)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTabsContainer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void btnBringToFrontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBringToFrontActionPerformed
// TODO add your handling code here:
    this.bringEditorWindowToFront();
}//GEN-LAST:event_btnBringToFrontActionPerformed

private void btnOpenDocumentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenDocumentActionPerformed
// TODO add your handling code here:
    loadDocumentInPanel();
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

    public void newDocumentInPanel() {
        String templatePath = BungeniEditorProperties.getEditorProperty(BungeniEditorPropertiesHelper.getCurrentDocType() + "_template");
        boolean bActive = false;
        int nConfirm = MessageBox.Confirm(parentFrame, bundle.getString("make_new_active"), bundle.getString("change_active"));
        if (JOptionPane.YES_OPTION == nConfirm) {
            bActive = true;
        }
       //we dont use the OpenDocumentAgent anymore -- as we are using noa,
       //OOo document needs to be opened in the event dispatch thread, not
       //in the background thread
       BungeniNoaFrame frame = BungeniNoaFrame.getInstance();
       DocumentComposition dc = null;
        try {
           dc = frame.loadDocumentInPanel(templatePath, true);
        } catch (OfficeApplicationException e) {
            log.error("Error while opening document from editorTabbedPanel" , e);
        } catch (NOAException e) {
            log.error("Error while opening document from editorTabbedPanel" , e);
        } catch (DocumentException e) {
            log.error("Error while opening document from editorTabbedPanel" , e);
        }
    }

    public synchronized void loadDocumentInPanel() {
        String basePath = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator + "workspace" + File.separator + "files";
        File openFile = CommonFileFunctions.getFileFromChooser(basePath, new org.bungeni.utils.fcfilter.ODTFileFilter(), JFileChooser.FILES_ONLY, parentFrame);
        if (openFile != null) {
            boolean bActive = false;
            int nConfirm = MessageBox.Confirm(parentFrame, bundle.getString("make_active"), bundle.getString("change_active"));
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
        if (currentSec.containsKey(SectionMetadataEditor.MetaEditor)) {
            //parse the metadata editor string into a subAction object
            String metaEditorString = currentSec.get(SectionMetadataEditor.MetaEditor);
            BungeniToolbarTargetProcessor btp = new BungeniToolbarTargetProcessor(metaEditorString);
            String documentType = BungeniEditorPropertiesHelper.getCurrentDocType();
            //create the subAction object out of the settings metadata
            BungeniClientDB instance = new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
            instance.Connect();
            String actionQuery = SettingsQueryFactory.Q_FETCH_SUB_ACTIONS(documentType, btp.actionName, btp.subActionName);
            //   log.info("processSelection: "+ actionQuery);
            QueryResults qr = instance.QueryResults(actionQuery);
            instance.EndConnect();
            if (qr == null) {
                log.info("launchSectionMetadataEditor : queryResults :" + actionQuery + " were null, metadata incorrectly setup");
                return;
            }
            if (qr.hasResults()) {
                //this should return only a single toolbarSubAction
                //create the subAction object here
                toolbarSubAction subActionObj = new toolbarSubAction(qr.theResults().elementAt(0), qr.columnNameMap());
                if (!CommonStringFunctions.emptyOrNull(btp.actionValue)) {
                    subActionObj.setActionValue(btp.actionValue);
                }
                subActionObj.setSelectorDialogMode(SelectorDialogModes.TEXT_EDIT);
                IEditorActionEvent event = EditorActionFactory.getEventClass(subActionObj);
                if (event != null) {
                    event.doCommand(ooDocument, subActionObj, parentFrame);
                } else {
                    log.info("launchSectionMetadataEditor : no IEditorActionEvent object was created for " + subActionObj.sub_action_name());
                }
            }

        }
    }

    /**
     * Opens an OOo document in the swingworker thread.
     * The agent opens the document and positions
     * an option to make the document the currently edited document (active document)
     * The functionality to make the OOo document the active document is executed in the EDT,
     * the event dispatch thread.
     */
    @Deprecated
    class OpenDocumentAgent extends SwingWorker<DocumentComposition, Void> {

        String documentToOpen = "";
        boolean makeActiveDocument = false;
        boolean isTemplate = false;

        public OpenDocumentAgent(String documentPath, boolean makeActive, boolean fromTemplate) {
            documentToOpen = documentPath;
            makeActiveDocument = makeActive;
            isTemplate = fromTemplate;
        }

        @Override
        protected DocumentComposition doInBackground() {
            XComponent xComp = null;
            BungeniNoaFrame frame = BungeniNoaFrame.getInstance();
            DocumentComposition dc = null;
            try {
               dc = frame.loadDocumentInPanel(documentToOpen, isTemplate);
            } catch (OfficeApplicationException e) {
                log.error("Error while opening document from editorTabbedPanel" , e);
            } catch (NOAException e) {
                log.error("Error while opening document from editorTabbedPanel" , e);
            } catch (DocumentException e) {
                log.error("Error while opening document from editorTabbedPanel" , e);
            }
         
            return dc;
        }

        @Override
        protected void done(){
             cboListDocuments.setEnabled(false);
            try {
                DocumentComposition dc = get();
                if (dc != null) {
                    /**
                    XTextDocument doc = ooQueryInterface.XTextDocument(xComp);
                    String strTitle = OOComponentHelper.getFrameTitle(doc);
                    componentHandleContainer chc = new componentHandleContainer(strTitle, xComp);
                    synchronized (editorMap) {
                        if (!editorMap.containsKey(chc.componentKey())) {
                            chc.setEventBroadcastListener();
                            editorMap.put(chc.componentKey(), chc);
                        }
                        DefaultComboBoxModel model = (DefaultComboBoxModel) cboListDocuments.getModel();
                        if (!existsInComboModel(chc.componentKey())) {
                            model.addElement(editorMap.get(chc.componentKey()));
                        }
                        if (makeActiveDocument) {
                            model.setSelectedItem(chc);
                        } else {
                            bringEditorWindowToFront();
                        }
                    } else {
                        log.error("DocumentCompp")
                    }
                    String currentDocType = BungeniEditorPropertiesHelper.getCurrentDocType();
                    launchMetadataSetter(xComp);
                     */
                }
            } catch (InterruptedException ex) {
                log.error("error while getting DocumentComposition object in done()", ex);
            } catch (ExecutionException ex) {
                log.error("error while getting DocumentComposition object in done()", ex);
            }

             cboListDocuments.setEnabled(true);
       }
      
    }

    private void launchMetadataSetter(XComponent xComp) {
        OOComponentHelper oohc = new OOComponentHelper(xComp, ComponentContext);
        //check if metadat variable is set
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
        BungeniFrame frm = new BungeniFrame(docType + " Metadata");
        MetadataEditorContainer meta = new MetadataEditorContainer(oohc, frm, SelectorDialogModes.TEXT_INSERTION);
        meta.initialize();
        frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frm.setSize(meta.getFrameSize());
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
    private javax.swing.JButton btnBringToFront;
    private javax.swing.ButtonGroup btnGrpBodyMetadataTarget;
    private javax.swing.JButton btnNewDocument;
    private javax.swing.JButton btnOpenDocument;
    private javax.swing.JButton btnSaveDocument;
    private javax.swing.JComboBox cboListDocuments;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabsContainer;
    private javax.swing.JTree jTree1;
    private javax.swing.JTree jTree2;
    private javax.swing.JLabel lblCurrentMode;
    private javax.swing.JLabel lblCurrentlyOpenDocuments;
    // End of variables declaration//GEN-END:variables

    /*
     *This is the class contained in the map of all open documents
     *Adds an eventListener()
     */
    /****
     * AH-20-05-2011 - removed !!!!
     */
    /****
    public static class componentHandleContainer {

        private String aName;
        private XComponent aComponent;
        private boolean componentDisposed = false;
        private xComponentListener compListener = new xComponentListener();
        private String componentKey = "";

        public componentHandleContainer(String name, XComponent xComponent) {
            log.debug("componentHandleContainer: in constructor()");
            aName = name;
            aComponent = xComponent;
            log.debug("componentHandleContainer: to string = " + aComponent.toString());
            aComponent.addEventListener(compListener);
            componentKey = generateComponentKey();
        //add the event broadcaster to the same listener
        }

        public String getDocURL() {
            XStorable xStore = ooQueryInterface.XStorable(aComponent);
            if (xStore.hasLocation()) {
                return xStore.getLocation();
            } else {
                return null;
            }
        }

        public void setEventBroadcastListener() {
            XEventBroadcaster xEventBroadcaster = (com.sun.star.document.XEventBroadcaster) UnoRuntime.queryInterface(com.sun.star.document.XEventBroadcaster.class, aComponent);
            xEventBroadcaster.addEventListener(compListener);
        }

        public XComponent getComponent() {
            return aComponent;
        }

        @Override
        public String toString() {
            return getName();
        }

        public String componentKey() {
            return componentKey;
        }

        private String generateComponentKey() {
            String ckey = generateComponentKey(getName(), this.aComponent);
            return ckey;
        }

        public static String generateComponentKey(String iName, XComponent component) {
            String compKey = "";
            try {
                XTextDocument xTextDoc = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, component);
                XDocumentInfoSupplier xdisInfoProvider = (XDocumentInfoSupplier) UnoRuntime.queryInterface(XDocumentInfoSupplier.class, xTextDoc);
                XDocumentInfo xDocInfo = xdisInfoProvider.getDocumentInfo();
                XPropertySet xDocProperties = ooQueryInterface.XPropertySet(xDocInfo);
                DateTime docCreationDate = (DateTime) AnyConverter.toObject(new Type(DateTime.class), xDocProperties.getPropertyValue("CreationDate"));
                Object objTemplateDate = xDocProperties.getPropertyValue("TemplateDate");
                Type foundType = AnyConverter.getType(objTemplateDate);
                log.debug("generateComponentkey template foundtype = " + foundType.getTypeName());
                Type reqdType = new Type(DateTime.class);
                if (foundType.getTypeName().equals(reqdType.getTypeName())) {
                    DateTime docTemplateDate = (DateTime) AnyConverter.toObject(new Type(DateTime.class), xDocProperties.getPropertyValue("TemplateDate"));
                    compKey = iName + UnoDateTimeToStr(docCreationDate) + UnoDateTimeToStr(docTemplateDate);
                } else {
                    compKey = iName + UnoDateTimeToStr(docCreationDate);
                }
            } catch (Exception ex) {
                log.error("generateComponentKey : " + ex.getMessage());
                log.error("generateComponentKey : " + CommonExceptionUtils.getStackTrace(ex));
            } finally {
                
            }
            return compKey;
        }

        private static String UnoDateTimeToStr(DateTime dt) {
            String returnDate = "";
            if (dt != null) {
                returnDate = Short.toString(dt.Year);
                returnDate += Short.toString(dt.Month);
                returnDate += Short.toString(dt.Day);
                returnDate += Short.toString(dt.Hours);
                returnDate += Short.toString(dt.Minutes);
                returnDate += Short.toString(dt.Seconds);
                returnDate += Short.toString(dt.HundredthSeconds);
            }
            return returnDate;
        }

        public String getName() {
            return aName;
        }

        public boolean isComponentDisposed() {
            return componentDisposed;
        }

        public void removeListener() {
            aComponent.removeEventListener(compListener);
        }

        class xComponentListener implements com.sun.star.document.XEventListener {

            public void disposing(com.sun.star.lang.EventObject eventObject) {
                //document window is closing
                log.debug("xComponentListner : the document window is closing" + getName());
                componentDisposed = true;
            }

            public void notifyEvent(com.sun.star.document.EventObject eventObject) {
     
            }
        }
    }
    ***/
    
    class cboListDocumentsActionListener implements ActionListener {

        //AH-20-05-2011
        /**
        componentHandleContainer oldItem;
        **/
        public void actionPerformed(ActionEvent e) {
            try {
                JComboBox cb = (JComboBox) e.getSource();
                /***
                componentHandleContainer newItem = (componentHandleContainer) cb.getSelectedItem();
                boolean same = false;
                if (oldItem != null) {
                    same = newItem.componentKey().equals(oldItem.componentKey());
                }
                oldItem = newItem;
                if ("comboBoxChanged".equals(e.getActionCommand())) {
                    updateMain((componentHandleContainer) newItem, same);
                } ***/
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
