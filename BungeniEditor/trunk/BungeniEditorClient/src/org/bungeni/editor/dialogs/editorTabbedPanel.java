/*
 * editorTabbedPanel.java
 *
 * Created on May 28, 2007, 3:55 PM
 */

package org.bungeni.editor.dialogs;

import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.document.XDocumentInfo;
import com.sun.star.document.XDocumentInfoSupplier;
import com.sun.star.document.XEventBroadcaster;
import com.sun.star.frame.XFrame;
import com.sun.star.frame.XModel;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.Type;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.DateTime;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.editor.panels.impl.FloatingPanelFactory;
import org.bungeni.editor.panels.impl.IFloatingPanel;
import org.bungeni.editor.panels.impl.ITabbedPanel;
import org.bungeni.editor.panels.impl.TabbedPanelFactory;
import org.bungeni.ooo.BungenioOoHelper;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooDocNotes;
import org.bungeni.ooo.ooQueryInterface;
import org.bungeni.extutils.MessageBox;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.editor.actions.EditorActionFactory;
import org.bungeni.editor.actions.IEditorActionEvent;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.editor.dialogs.metadatapanel.SectionMetadataLoad;
import org.bungeni.editor.metadata.editors.MetadataEditorContainer;
import org.bungeni.editor.plugin.impl.IEditorPluginAll;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.editor.selectors.metadata.SectionMetadataEditor;
import org.bungeni.editor.toolbar.BungeniToolbarTargetProcessor;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.extutils.BungeniFrame;
import org.bungeni.extutils.BungeniRuntimeProperties;
import org.bungeni.extutils.CommonFileFunctions;
import org.bungeni.extutils.CommonStringFunctions;
import org.bungeni.extutils.FrameLauncher;
import org.bungeni.utils.externalplugin.ExternalPlugin;
import org.bungeni.utils.externalplugin.ExternalPluginsFinder;
import org.bungeni.utils.externalplugin.ExternalPluginsLoader;
/**
 *
 * @author  Administrator
 */
public class editorTabbedPanel extends javax.swing.JPanel {
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
 
    private Timer sectionNameTimer;
    private String currentSelectedSectionName = "";
    private Timer docStructureTimer;
    private Timer componentsTrackingTimer;
    
    private changeStructureItem selectedChangeStructureItem;

    private boolean mouseOver_TreeDocStructureTree = false;
    private boolean program_refresh_documents = false;
    
    private  TreeMap<String, editorTabbedPanel.componentHandleContainer> editorMap;
    
    
    private String activeDocument; 
    
    private HashMap<String, IFloatingPanel> floatingPanelMap = new HashMap<String,IFloatingPanel>();
    private ArrayList<ITabbedPanel> m_tabbedPanelMap = new ArrayList<ITabbedPanel>();
    
    
   
    public static int coordX , coordY;
    /** Creates new form SwingTabbedJPanel */
    public editorTabbedPanel() {
        initComponents();
    }
    
    /**
     * Constructor for main Tabbed panel interface
     */
    public editorTabbedPanel(XComponent impComponent, BungenioOoHelper helperObj, JFrame parentFrame){
       log.debug("constructor:editortabbedpanel");  
       this.Component = impComponent;
       if (impComponent == null) log.debug("constructor:editortabbedpanel impComponent was null");  
       this.ooHelper = helperObj;
       if (helperObj == null) log.debug("constructor:editortabbedpanel helperObj was null");  
       this.ComponentContext = ooHelper.getComponentContext();
       editorMap = new TreeMap<String, componentHandleContainer>();
       ooDocument = new OOComponentHelper(impComponent, ComponentContext);
       this.parentFrame = parentFrame;
       this.activeDocument = BungeniEditorProperties.getEditorProperty("activeDocumentMode");
       init();
   
    }
    
    /* we need three options,
     *one that launches with a blank document
     *the other that allows the user to edit a document
     *the last that launches just the editor panel and attaches it self to existing instances of oOo
     */
    /*this one prompts the user to select a currently open document */
    public editorTabbedPanel(BungenioOoHelper helperObj, JFrame parentFrame){
        
      // this.Component = impComponent;
     //  this.ComponentContext = impComponentContext;
       this.ooHelper = helperObj;
       editorMap = new TreeMap<String, componentHandleContainer>();
       //prompt the user to select a document 
       //ooDocument = new OOComponentHelper(impComponent, impComponentContext);
       this.parentFrame = parentFrame;
       
       init();
    }
   
    private void init() {
       initComponents();
       initProviders();
       SwingUtilities.invokeLater(new Runnable(){

            public void run() {
                initFloatingPane();
            }
           
       });

       initTimers();
       log.debug("calling initOpenDOcuments");
       initOpenDocuments();
      
       /***** control moved to other dialog... 
       updateListDocuments();
        *****/
       initTabbedPanes();
       initModeLabel();
       initSectionMetadataDisplay();
    }

    private void initProviders(){
        org.bungeni.editor.providers.DocumentSectionProvider.initialize(this.ooDocument);
    }
    
    private void updateProviders() {
        org.bungeni.editor.providers.DocumentSectionProvider.updateOOoHandle(this.ooDocument);
    }
    private void initTabbedPanes() {
        log.debug("InitTabbedPanes: begin");
        m_tabbedPanelMap = TabbedPanelFactory.getPanelsByDocType(BungeniEditorProperties.getEditorProperty("activeDocumentMode"));
        for (ITabbedPanel panel: m_tabbedPanelMap ) {
            panel.setOOComponentHandle(ooDocument);
            panel.setParentHandles(parentFrame, this);
            panel.initialize();
            this.jTabsContainer.add(panel.getPanelTitle(), panel.getObjectHandle());
        }
        log.debug("InitTabbedPanes: finished loading");
    }

    
    private void updateTabbedPanes(){
        for (ITabbedPanel panel: m_tabbedPanelMap ) {
            panel.setOOComponentHandle(ooDocument);
            panel.refreshPanel();
        }
    }
    
    private HashMap<String, IEditorPluginAll> loadedPluginsMap = new HashMap<String, IEditorPluginAll>();
    
    /**
     * Loads the external plugins specified in the EXTERNAL_PLUGINS table.
     */
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
    }
    
    SectionMetadataDisplay objMetaDisplay;
    /**
     * Used to display section metadata in the tabbed panel
     */
    private void initSectionMetadataDisplay(){
           objMetaDisplay = new SectionMetadataDisplay();
    }
    
    class SectionMetadataDisplay{
        private Timer refreshTimer;
        private Action sectionViewRefreshRunner;
    
        public SectionMetadataDisplay(){
            tblSectionmeta.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 11));
            tblSectionmeta.setFont(new Font("Tahoma", Font.PLAIN, 11)); 
            initTimers();
        }
        
      private void initTimers(){
        sectionViewRefreshRunner = new viewRefreshAction();
        refreshTimer = new Timer(3000, sectionViewRefreshRunner);
        refreshTimer.setInitialDelay(2000);
        refreshTimer.start();
    }  
       public void updateSectionMetadataView(String sectionName) {
          lblDisplaySectionName.setText(sectionName);
          SectionMetadataLoad sectionMetadataTableModel = new SectionMetadataLoad(ooDocument,sectionName);
          tblSectionmeta.setModel(sectionMetadataTableModel);
    }
       
       public void updateSectionMetadataEditButton(String newSectionName) {
           HashMap<String,String> sectionMeta = ooDocument.getSectionMetadataAttributes(newSectionName);
           if (sectionMeta.containsKey(SectionMetadataEditor.MetaEditor)) {
               btnEdit.setEnabled(true);
           } else
               btnEdit.setEnabled(false);
       }
          
        private class viewRefreshAction extends AbstractAction {
        public String oldSectionName ; 
        public String newSectionName;
        public void actionPerformed(ActionEvent arg0) {
            if (ooDocument != null ) {
                String sSect = ooDocument.currentSectionName();
                if (sSect != null) {
                    newSectionName = sSect;
                    if (newSectionName.equals(oldSectionName)) {
                        //dont do anything
                    } else {
                        updateSectionMetadataView(newSectionName);
                        updateSectionMetadataEditButton(newSectionName);
                        oldSectionName = newSectionName;
                    }
                }
            }
        }
    
        }
    }

    protected void setOODocumentObject (OOComponentHelper ooDoc) {
        this.ooDocument = ooDoc;
    }
    
    public OOComponentHelper getOODocumentObject(){
        return this.ooDocument;
    }

  
    /*
     *
     *at this point the table model for the metadata table has already been set,
     *we are checking the metadata of the table
     */
  
	
    private void initListDocuments(){
        log.debug("initListDocuments: init");
        //String[] listDocuments = getCurrentlyOpenDocuments().keySet().toArray(new String[getCurrentlyOpenDocuments().keySet().size()]);
        ArrayList<componentHandleContainer> listDocuments = this.getDocumentsComboModel();
        this.cboListDocuments.setModel(new DefaultComboBoxModel(listDocuments.toArray()));
    }

    
    private void initOpenDocumentsList() {
         try {
            log.debug("initOpenDocumentsList: getting components");
            XEnumerationAccess enumComponentsAccess = BungenioOoHelper.getDesktop().getComponents();
            XEnumeration enumComponents = enumComponentsAccess.createEnumeration();
            log.debug("initOpenDocumentsList: enumerating components");
            int i=0;
            editorMap.clear(); //reset the map before adding things to it.
            while (enumComponents.hasMoreElements()) {
                Object nextElem = enumComponents.nextElement();
                log.debug("initOpenDocumentsList: getting model interface");
                XModel docModel = ooQueryInterface.XModel(nextElem);

                if (docModel != null ) { //supports XModel interface
                    log.debug("initOpenDocumentsList: docModel != null");
                    XServiceInfo serviceInfo = ooQueryInterface.XServiceInfo(nextElem);
                    if (serviceInfo.supportsService("com.sun.star.text.TextDocument")) {
                        log.debug("initOpenDocumentsList: supports TextDocument "+ (++i));
                        XTextDocument xDoc = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, nextElem);
                        String strTitle = OOComponentHelper.getFrameTitle(xDoc);
                        XComponent xComponent = (XComponent)UnoRuntime.queryInterface(XComponent.class, nextElem);
                        componentHandleContainer compContainer = new componentHandleContainer(strTitle, xComponent);
                        if (!editorMap.containsKey(compContainer.componentKey())) {
                            compContainer.setEventBroadcastListener();
                            editorMap.put(compContainer.componentKey(), compContainer);
                        }
                    }
                }
            }
        } catch (Exception ex) {
           log.error("InitOpenDocumentsList error :" + ex.getMessage());
           log.error("InitOpenDocumentsList stacktrace : " + CommonExceptionUtils.getStackTrace(ex));
        }
    }

    
    private void initOpenDocuments(){
        log.debug("initOpenDocuments: calling");
        //commented here for listener synchronization issues, as the combox action
        //listener depends on the tree data model being set.
       // cboListDocuments.addActionListener(new cboListDocumentsActionListener());
        initOpenDocumentsList();
        initListDocuments();
        initListDocumentsListener();
        initSelectionInOpenDocuments();
    }
    
    private void initSelectionInOpenDocuments(){
        componentHandleContainer currentDoc = new componentHandleContainer (ooDocument.getDocumentTitle(), ooDocument.getComponent());
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboListDocuments.getModel();
        for (int i=0 ; i < model.getSize(); i++ ){
            componentHandleContainer foundchc = (componentHandleContainer) model.getElementAt(i);
            if (foundchc.componentKey().equals(currentDoc.componentKey())) {
                model.setSelectedItem(foundchc);
                break;
            }
        }
    }
    
    private void initListDocumentsListener(){
        this.cboListDocuments.addActionListener(new cboListDocumentsActionListener());
    }
    
    private void initModeLabel(){
        String labelText =  this.lblCurrentMode.getText();
        labelText = labelText.replaceAll("%s", BungeniEditorPropertiesHelper.getCurrentDocType());
        this.lblCurrentMode.setText(labelText);
    }

    /**
     * Depreacted function - as combobox tab switching has been removed.
     */
    private void initSwitchTabs(){
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
    
  

    

    
    public void bringEditorWindowToFront(){
   	if (ooDocument.isXComponentValid()) {
        XFrame xDocFrame = ooDocument.getDocumentModel().getCurrentController().getFrame();
        Object docFrameWindow = xDocFrame.getContainerWindow();
        if (docFrameWindow == null) return;
        
        Object queryInterface=ooQueryInterface.XTopWindow(docFrameWindow);
        if (queryInterface==null){
            return;
        }else{
            log.debug("Bring selected window to the front");
            ooQueryInterface.XTopWindow(xDocFrame.getContainerWindow()).toFront();
        }
      }
   } 
       /**
     * Static function invoked by JPanel containing document switcher.
     * @param currentlySelectedDoc currently selected document in switched
     * @param same
     */
    public void updateMain(componentHandleContainer currentlySelectedDoc, boolean same) {
                  if (same) {
                    if (self().program_refresh_documents == true)
                        return;
                    else
                         bringEditorWindowToFront();
                } else {
                    if (currentlySelectedDoc == null ) {
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
                    updateFloatingPanels();
                    updateTabbedPanes();
               
                    /**** commented *** refreshTableDocMetadataModel();****/
                    if (self().program_refresh_documents == false)
                        bringEditorWindowToFront();
                    
                }
    }
    
    /*
     *this is invoked on window closing, by the JFrame that contains the panel
     */
    public void cleanup() {
        //shutdown timers
            docStructureTimer.stop();   
            sectionNameTimer.stop();
            componentsTrackingTimer.stop();
        //cleanup component listners
            Iterator keyIterator = editorMap.keySet().iterator();
            while (keyIterator.hasNext()) {
                String key = (String) keyIterator.next();
                componentHandleContainer compHandle = editorMap.get(key);
                compHandle.removeListener();
            }
    }
    

    private static int  WIDTH_OOo_SCROLLBAR = 25;
    // added for Issue 246, http://code.google.com/p/bungeni-portal/issues/details?id=246
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
            floatingFrame.setVisible(true);
            //position frame
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension windowSize = floatingFrame.getSize();
            log.debug("screen size = "+ screenSize);
            log.debug("window size = "+ windowSize);
            
            int windowX = screenSize.width - floatingFrame.getWidth() - WIDTH_OOo_SCROLLBAR;
            //int windowY = (screenSize.height - floatingFrame.getHeight())/2;
            int windowY = editorApplicationController.getFrameWindowDimension().y;
            log.debug("initFloatingPane : Window Y =  "+ windowY);
          
          floatingFrame.setLocation(windowX, editorTabbedPanel.coordY + 50);  // Don't use "f." inside constructor.
            floatingFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            
      
    }

   
    
    private void updateFloatingPanels(){
        if (!floatingPanelMap.isEmpty()){
            Iterator<String> panelNames = floatingPanelMap.keySet().iterator();
                         while (panelNames.hasNext  ()) {
                             
                             IFloatingPanel panelObj = floatingPanelMap.get(panelNames.next());
                             panelObj.setOOComponentHandle(ooDocument);
                         }
        }
    }
 
    private editorTabbedPanel self() {
        return this;
    }

    public Component getComponentHandle(){
        return this;
    }
     
    public TreeMap<String, editorTabbedPanel.componentHandleContainer> getCurrentlyOpenDocuments(){
        return this.editorMap;
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSectionmeta = new javax.swing.JTable();
        lblDisplaySectionName = new javax.swing.JLabel();
        lblSecName = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();

        jScrollPane2.setViewportView(jTree1);

        jScrollPane3.setViewportView(jTree2);

        setFont(new java.awt.Font("Tahoma", 0, 10));

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

        tblSectionmeta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblSectionmeta);

        lblDisplaySectionName.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblDisplaySectionName.setText(bundle.getString("editorTabbedPanel.lblDisplaySectionName.text")); // NOI18N

        lblSecName.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblSecName.setText(bundle.getString("editorTabbedPanel.lblSecName.text")); // NOI18N

        btnEdit.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnEdit.setText(bundle.getString("editorTabbedPanel.btnEdit.text")); // NOI18N
        btnEdit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
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
                        .add(btnSaveDocument, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(32, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .add(lblSecName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 134, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .add(lblDisplaySectionName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 134, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 79, Short.MAX_VALUE)
                .add(btnEdit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
            .add(jTabsContainer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
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
                .add(jTabsContainer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 355, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(lblSecName)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblDisplaySectionName)
                    .add(btnEdit))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 157, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
    /*
        BungeniTransformationTarget transform= new BungeniTransformationTarget ("ODT", "ODT (OpenDocument Format)", "odt" , "org.bungeni.ooo.transforms.loadable.ODTSaveTransform");
        //BungeniTransformationTarget transform = (BungeniTransformationTarget) this.cboTransformFrom.getSelectedItem();
        IBungeniDocTransform iTransform = BungeniTransformationTargetFactory.getTransformClass(transform);
        
        String exportPath = BungeniEditorProperties.getEditorProperty("defaultExportPath");
        exportPath = exportPath.replace('/', File.separatorChar);
        exportPath = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator + exportPath;
        exportPath = exportPath + File.separatorChar + OOComponentHelper.getFrameTitle(ooDocument.getTextDocument()).trim()+"."+transform.targetExt;
        File fileExp = new File(exportPath);
        String exportPathURL = "";
            exportPathURL = fileExp.toURI().toString();
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("StoreToUrl", exportPathURL);
        
        iTransform.setParams(params);
        boolean bState= iTransform.transform(ooDocument);
        if (bState ) {
            MessageBox.OK(parentFrame, "Document was successfully Exported ");
        } else
            MessageBox.OK(parentFrame, "Document Export failed " );*/

}//GEN-LAST:event_btnSaveDocumentActionPerformed

private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
// TODO add your handling code here:
   launchSectionMetadataEditor();
}//GEN-LAST:event_btnEditActionPerformed


public void saveOpenDocument() {
    //if the document is on disk simply save it
    if (ooDocument.isDocumentOnDisk()) {
        ooDocument.saveDocument();
    } else {
      //if the document isnt on disk, we force metadata setting.
        MetadataEditorContainer.launchMetadataEditor(ooDocument, SelectorDialogModes.TEXT_INSERTION);
    }

}


public void newDocumentInPanel(){
    String templatePath = BungeniEditorProperties.getEditorProperty(BungeniEditorPropertiesHelper.getCurrentDocType()+"_template");
    Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    boolean bActive = false;
    int nConfirm = MessageBox.Confirm(parentFrame, "Make this document the active document ?", "Change active document");
    if (JOptionPane.YES_OPTION == nConfirm) {
            bActive=true;
    }
    OpenDocumentAgent openDocAgent = new OpenDocumentAgent(templatePath, screenSize, bActive, true);
    openDocAgent.execute();
}
public synchronized void loadDocumentInPanel(){
    String basePath = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH()+File.separator+"workspace"+File.separator+"files";
    File openFile = CommonFileFunctions.getFileFromChooser(basePath, new org.bungeni.utils.fcfilter.ODTFileFilter(), JFileChooser.FILES_ONLY, parentFrame);
    if (openFile != null) {
        boolean bActive = false;
        int nConfirm = MessageBox.Confirm(parentFrame, "Make this document the active document ?", "Change active document");
        if (JOptionPane.YES_OPTION == nConfirm) {
            bActive=true;
        }
        String fullPathToFile = openFile.getAbsolutePath();
        //we calculate the screen dimension in advance and pass it to the open document agent
        //as the awt toolkit executes in the EDT while the swingworker thread is an independent thread,
        //and thus it is unsafe to make awt call from the swing worker thread.
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        OpenDocumentAgent openDocAgent = new OpenDocumentAgent(fullPathToFile, screenSize, bActive, false);
        openDocAgent.execute();
    }
    
}

   /**
    * Launches the metadata editor of the seciton
    */
   private void launchSectionMetadataEditor(){
        //get the section metadata
       HashMap<String, String> currentSec = ooDocument.getSectionMetadataAttributes(ooDocument.currentSectionName());
       //check if the section has a metadata editor
       if (currentSec.containsKey(SectionMetadataEditor.MetaEditor)) {
            //parse the metadata editor string into a subAction object
           String metaEditorString = currentSec.get(SectionMetadataEditor.MetaEditor);
           BungeniToolbarTargetProcessor btp = new BungeniToolbarTargetProcessor(metaEditorString);
           String documentType = BungeniEditorPropertiesHelper.getCurrentDocType();
           //create the subAction object out of the settings metadata
           BungeniClientDB instance = new BungeniClientDB (DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
           instance.Connect();
            String actionQuery = SettingsQueryFactory.Q_FETCH_SUB_ACTIONS(documentType, btp.actionName, btp.subActionName);
             //   log.info("processSelection: "+ actionQuery); 
                QueryResults qr = instance.QueryResults(actionQuery);
                 instance.EndConnect();
                 if (qr == null ) {
                     log.info("launchSectionMetadataEditor : queryResults :" + actionQuery + " were null, metadata incorrectly setup");
                     return;
                 }
                 if (qr.hasResults()) {
                    //this should return only a single toolbarSubAction
                    //create the subAction object here 
                     toolbarSubAction subActionObj =  new toolbarSubAction(qr.theResults().elementAt(0), qr.columnNameMap());
                     if (!CommonStringFunctions.emptyOrNull(btp.actionValue)) 
                         subActionObj.setActionValue(btp.actionValue);
                     subActionObj.setSelectorDialogMode(SelectorDialogModes.TEXT_EDIT);
                     IEditorActionEvent event = EditorActionFactory.getEventClass(subActionObj);
                     if (event != null ) {
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
class OpenDocumentAgent extends SwingWorker <XComponent, Void> {
        String documentToOpen = "";
        boolean makeActiveDocument = false;
        Dimension screenDimension = null;
        boolean isTemplate= false;
        public OpenDocumentAgent (String documentPath, Dimension screenSize, boolean makeActive, boolean fromTemplate) {
            documentToOpen = documentPath;
            makeActiveDocument = makeActive ;
            screenDimension = screenSize;
            isTemplate = fromTemplate;
        }
    
        @Override
        protected XComponent doInBackground() {
            XComponent xComp = null;
            if (isTemplate)
                xComp = OOComponentHelper.newDocument(documentToOpen);
            else
                xComp = OOComponentHelper.openExistingDocument(documentToOpen);
            if (xComp != null ) {
                OOComponentHelper.positionOOoWindow(xComp, screenDimension);
            }
            return xComp;
        }
    
        @Override
        protected void done(){
            try {
                cboListDocuments.setEnabled(false);
                XComponent xComp = get();
                    if (xComp != null) {
                        XTextDocument doc = ooQueryInterface.XTextDocument(xComp);
                        String strTitle = OOComponentHelper.getFrameTitle(doc);
                        componentHandleContainer chc = new componentHandleContainer(strTitle, xComp);
                        synchronized(editorMap) {
                            if (!editorMap.containsKey(chc.componentKey())){
                                chc.setEventBroadcastListener();
                                editorMap.put(chc.componentKey(), chc);
                            }
                            DefaultComboBoxModel model = (DefaultComboBoxModel) cboListDocuments.getModel();
                            if (!existsInComboModel(chc.componentKey())){
                                model.addElement(editorMap.get(chc.componentKey()));
                            }
                            if (makeActiveDocument)
                                model.setSelectedItem(chc);
                            else 
                                bringEditorWindowToFront();
                        }
                            String currentDocType = BungeniEditorPropertiesHelper.getCurrentDocType();
                            launchMetadataSetter(xComp);
                }
            } catch (InterruptedException ex) {
                log.error("openDocumentAgent : done: " + ex.getMessage());
            } catch (ExecutionException ex) {
                log.error("openDocumentAgent : done: " + ex.getMessage());            
            } finally {
                cboListDocuments.setEnabled(true);
                return;
            }
        }
}
   

private void launchMetadataSetter(XComponent xComp){
        OOComponentHelper oohc = new OOComponentHelper (xComp, ComponentContext);
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
    private  void initTimers(){
        try {
            //component handle tracker timer
      
            Action componentsTrackingRunner = new AbstractAction(){
                public void actionPerformed(ActionEvent e) {
                  componentHandlesTracker();
                    try {
                        updateListDocuments();
                    } catch (MalformedURLException ex) {
                       log.error("updateListDocuments  : " + ex.getMessage());
                    } catch (URISyntaxException ex) {
                       log.error("updateListDocuments  : " + ex.getMessage());
                    }
                }
            };
            componentsTrackingTimer = new Timer(2500, componentsTrackingRunner);
            componentsTrackingTimer.start();

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    
     private ArrayList<componentHandleContainer> getDocumentsComboModel(){

         ArrayList<componentHandleContainer> listDocuments = new ArrayList<componentHandleContainer>();
         for (String docKey : getCurrentlyOpenDocuments().keySet()){
             componentHandleContainer compHandle = getCurrentlyOpenDocuments().get(docKey);
             listDocuments.add(compHandle);
         }
         
         return listDocuments;
     }
     
     private boolean existsInComboModel(String compKey) {
         boolean bFound = false;
          DefaultComboBoxModel model = (DefaultComboBoxModel) cboListDocuments.getModel();
          for (int i=0; i < model.getSize(); i++ ) {
              componentHandleContainer foundCHC = (componentHandleContainer) model.getElementAt(i);
              if (foundCHC.componentKey().equals(compKey)){
                  bFound = true;
              }
          }
          return bFound;
     }
     
     private void updateListDocuments() throws MalformedURLException, URISyntaxException{
         //new refreshed list of component handles 
         synchronized(editorMap) {
             ArrayList<componentHandleContainer> componentHandles = getDocumentsComboModel();
             //capture currentlySelected item
             componentHandleContainer selectedItem = (componentHandleContainer) cboListDocuments.getModel().getSelectedItem();
             //add newly opened documents to model
              DefaultComboBoxModel model = (DefaultComboBoxModel) cboListDocuments.getModel();
              for (String compKey : getCurrentlyOpenDocuments().keySet()) {
                  if (!existsInComboModel(compKey)) {
                      model.addElement(getCurrentlyOpenDocuments().get(compKey));
                  }
              }
              //build an array of disposed component handles i.e. documents which have been closed.
              ArrayList<componentHandleContainer> handleContainer = new ArrayList<componentHandleContainer>(0);
              //remove closed documents from model
              for (int i=0; i < model.getSize(); i++ ){
                  componentHandleContainer compMatch = (componentHandleContainer) model.getElementAt(i);
                    try {
                      //test if disposed - if disposed this raises an exception
                      String compMatchURL = compMatch.getDocURL();
                      //check if model component handle exists in newly generate component map
                      //if it doesnt we delete the componenth handle from them model
                      if (!getCurrentlyOpenDocuments().containsKey(compMatch.componentKey())){
                          handleContainer.add(compMatch);
                     }
                  } catch (com.sun.star.lang.DisposedException ex) {
                      //if disposed exception was raised.. the document has been disposed
                      //so add it for deletion
                      handleContainer.add(compMatch);
                  }
              }

              //remove all the handles marked for deletion from the combo box model.
             for (componentHandleContainer chcHandle : handleContainer) {
                 model.removeElement(chcHandle);
             }
              //set selected item
              //first check if there is a saved document
             if (BungeniRuntimeProperties.propertyExists("SAVED_FILE")) {
                    //set the URI fo the saved document
                    String savedDocumentURL = BungeniRuntimeProperties.getProperty("SAVED_FILE");
                    URL urlDoc = new URL(savedDocumentURL);
                    String savedDocumentURI = urlDoc.toString();
                    //see if it matches any of the currently open documents
                    DefaultComboBoxModel freshModel = (DefaultComboBoxModel) cboListDocuments.getModel();
                    for (int i = 0; i < freshModel.getSize(); i++) {
                            componentHandleContainer chc = (componentHandleContainer)freshModel.getElementAt(i);
                            String chcDocUrl = chc.getDocURL();
                            if (chcDocUrl != null ) {
                                //check if the saved document url equals the url of one of the currently open documents
                                //if it does set the combo index to that document.
                                URL docUrl = new URL(chcDocUrl);
                                if (docUrl.toString().equals(savedDocumentURI)){
                                    //since this code runs in a timer thread .. it executes continuously
                                    //but since we are removing "saved_file" from the static property map,
                                    //this should never get executed

                                    //temporarily disable combo action listeners
                                  //  ActionListener[] actionListeners = cboListDocuments.getActionListeners();
                                  //  for (ActionListener aListener : actionListeners) {
                                  //    cboListDocuments.removeActionListener(aListener);
                                  //  }
                                    //set the combo index
                                    cboListDocuments.setSelectedIndex(i);

                                    //restore action listeners
                                    //for (ActionListener addListener : actionListeners) {
                                    //    cboListDocuments.addActionListener(addListener);
                                   // }
                                    //remove the saved_file property
                                    BungeniRuntimeProperties.removeProperty("SAVED_FILE");
                                    break;
                                }
                            }
                    }
                }

             
         }
    }
    
    public void setProgrammaticRefreshOfDocumentListFlag (boolean bState) {
        this.program_refresh_documents = bState;
    }
    
    private  void componentHandlesTracker() {
                    log.debug("componentHandlesTracker: begin ");
                    //array list caches keys to be removed
                    ArrayList<String> keysToRemove = new ArrayList<String>();
                    //find the components that have been disposed
                    //and capture them in an array
                    log.debug("componentHandlesTracker: finding disposed documents ");

                      log.debug("componentHandlesTracker: capturing selected item ");

                    //now remove the disposed components from the map
                    
                      log.debug("componentHandlesTracker: removing disposed components ");

                   
                   //some documents may have been opened in the meanwhile... we look for them and add them
                      log.debug("componentHandlesTracker: refreshing document open keyset map ");
                  
                    initOpenDocumentsList();
                   
                   //now update the combo box... 

                   //this.program_refresh_documents = false;
    }    
    class changeStructureItem {
        String itemText;
        String itemIndex;
        changeStructureItem(String itemIndex, String itemText) {
            this.itemText = itemText;
            this.itemIndex = itemIndex;
        }
        
        public String getIndex() {
            return itemIndex;
        }
        @Override
        public String toString(){
            return itemText;
        }
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBringToFront;
    private javax.swing.JButton btnEdit;
    private javax.swing.ButtonGroup btnGrpBodyMetadataTarget;
    private javax.swing.JButton btnNewDocument;
    private javax.swing.JButton btnOpenDocument;
    private javax.swing.JButton btnSaveDocument;
    private javax.swing.JComboBox cboListDocuments;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabsContainer;
    private javax.swing.JTree jTree1;
    private javax.swing.JTree jTree2;
    private javax.swing.JLabel lblCurrentMode;
    private javax.swing.JLabel lblCurrentlyOpenDocuments;
    private javax.swing.JLabel lblDisplaySectionName;
    private javax.swing.JLabel lblSecName;
    private javax.swing.JTable tblSectionmeta;
    // End of variables declaration//GEN-END:variables

    /*
     *This is the class contained in the map of all open documents
     *Adds an eventListener()
     */
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

        public String getDocURL(){
               XStorable xStore = ooQueryInterface.XStorable(aComponent);
                 if (xStore.hasLocation()) {
                         return xStore.getLocation();
                    } else {
                        return null;
                      }
        }

        public void setEventBroadcastListener(){
            XEventBroadcaster xEventBroadcaster = (com.sun.star.document.XEventBroadcaster) UnoRuntime.queryInterface (com.sun.star.document.XEventBroadcaster.class, aComponent);
            xEventBroadcaster.addEventListener (compListener); 
        }
        
        public XComponent getComponent(){
            return aComponent;
        }
        
        @Override
        public String toString(){
            return getName();
        }
        
        public String componentKey(){
            return componentKey;
        }
        
        private String generateComponentKey() {
            String ckey = generateComponentKey(getName(), this.aComponent);
            return ckey;
        }
        
        public static String generateComponentKey(String iName, XComponent component){
            String compKey = "";
            try {
               XTextDocument xTextDoc = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, component);
               XDocumentInfoSupplier xdisInfoProvider =  (XDocumentInfoSupplier) UnoRuntime.queryInterface(XDocumentInfoSupplier.class, xTextDoc );
               XDocumentInfo xDocInfo = xdisInfoProvider.getDocumentInfo();
               XPropertySet xDocProperties = ooQueryInterface.XPropertySet(xDocInfo);
               DateTime docCreationDate = (DateTime) AnyConverter.toObject(new Type(DateTime.class), xDocProperties.getPropertyValue("CreationDate"));
               Object objTemplateDate = xDocProperties.getPropertyValue("TemplateDate");
               Type foundType = AnyConverter.getType(objTemplateDate);
               log.debug("generateComponentkey template foundtype = " + foundType.getTypeName());
               Type reqdType = new Type(DateTime.class);
               if (foundType.getTypeName().equals(reqdType.getTypeName())) {
                    DateTime docTemplateDate = (DateTime) AnyConverter.toObject(new Type(DateTime.class), xDocProperties.getPropertyValue("TemplateDate"));
                    compKey = iName+UnoDateTimeToStr(docCreationDate)+UnoDateTimeToStr(docTemplateDate);
               } else {
                    compKey = iName+UnoDateTimeToStr(docCreationDate);
               }   
            } catch (Exception ex) {
                log.error("generateComponentKey : " + ex.getMessage());
                log.error("generateComponentKey : " + CommonExceptionUtils.getStackTrace(ex));
            } finally {
                return compKey;
            }
        }
        
        private static String UnoDateTimeToStr(DateTime dt){
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
        
        
        public String getName(){
            return aName;
        }
        
        public boolean isComponentDisposed() {
            return componentDisposed;
        }
        
        public void removeListener(){
            aComponent.removeEventListener(compListener);
        }
    
        class xComponentListener implements com.sun.star.document.XEventListener {
                 public void disposing(com.sun.star.lang.EventObject eventObject) {
                    //document window is closing
                     log.debug("xComponentListner : the document window is closing" + getName());
                     componentDisposed = true;
                }
                
                public void notifyEvent(com.sun.star.document.EventObject eventObject) {
                    /*
                    if (eventObject.EventName.equals("OnFocus")) {
                        log.error("xComponentListner : the document window OnFocus()" + getName());
                        //getName() for this document compare it with the current documetn in the editorTabbedPanel lis
                        //if it isnt equal notify the user with a message box that the 
                        Object selected = cboListDocuments.getSelectedItem();
                        String selectedDocument = "";
                        if (selected != null) {
                            selectedDocument = (String) selected;
                            if (selectedDocument.trim().equals(getName().trim())) {
                              /// commented below to prevent swing thread-sync bug 
                               // parentFrame.setAlwaysOnTop(true);
                              //  parentFrame.setAlwaysOnTop(false);
                              //   parentFrame.toFront();
                              //  parentFrame.setAlwaysOnTop(true);
                               
                            } else {
                             ///// commented below to prevent thread synchronization bug 
                                //parentFrame.setAlwaysOnTop(true);
                               // parentFrame.setAlwaysOnTop(false);
                               // parentFrame.toFront();
                               // parentFrame.setAlwaysOnTop(true);
                               
                                //MessageBox.OK(self(), "The current window is not the one being edited using the Bungeni Editor, please select this document :" +  getName() + " from the Editor Selector to be able to edit it!");
                            }
                        } else {
                            log.error("xComponentListner :  selected document object is null"  );
                        }
                    }*/
                }
        
            }        
    }

      class cboListDocumentsActionListener implements ActionListener {
        componentHandleContainer oldItem;
        public void actionPerformed(ActionEvent e) {
          try {
            JComboBox cb = (JComboBox) e.getSource();
            componentHandleContainer newItem = (componentHandleContainer)cb.getSelectedItem();
            boolean same= false;
            if (oldItem != null )
                same = newItem.componentKey().equals(oldItem.componentKey());
            oldItem = newItem;
            if ("comboBoxChanged".equals(e.getActionCommand())) {
                updateMain((componentHandleContainer)newItem, same);
            }
          } catch (Exception ex) {
              log.error ("cboListDocuments.actionPerformed = " + ex.getMessage()) ;
              log.error("cboListDocuments.actionPerformed = " + CommonExceptionUtils.getStackTrace(ex));
          }
            
        }
        
    }
    

    public HashMap<String, IFloatingPanel> getFloatingPanelMap() {
        return this.floatingPanelMap;
    }
    


}
