/*
 * editorTabbedPanel.java
 *
 * Created on May 28, 2007, 3:55 PM
 */

package org.bungeni.editor.dialogs;

import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.beans.XPropertySetInfo;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.container.XNamed;
import com.sun.star.document.XDocumentInfo;
import com.sun.star.document.XDocumentInfoSupplier;
import com.sun.star.document.XEventBroadcaster;
import com.sun.star.frame.XFrame;
import com.sun.star.frame.XModel;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.text.XRelativeTextContentInsert;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextSection;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.uno.Any;
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
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.editor.BungeniEditorPropertiesHelper;
import org.bungeni.editor.macro.ExternalMacro;
import org.bungeni.editor.macro.ExternalMacroFactory;
import org.bungeni.editor.metadata.DocumentMetadataTableModel;
import org.bungeni.editor.panels.impl.FloatingPanelFactory;
import org.bungeni.editor.panels.impl.ICollapsiblePanel;
import org.bungeni.editor.panels.impl.IFloatingPanel;
import org.bungeni.editor.panels.impl.ITabbedPanel;
import org.bungeni.editor.panels.impl.TabbedPanelFactory;
import org.bungeni.ooo.BungenioOoHelper;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooDocNotes;
import org.bungeni.ooo.ooQueryInterface;
import org.bungeni.utils.DocStructureElement;
import org.bungeni.utils.MessageBox;
import org.bungeni.utils.BungeniBTree;
import org.bungeni.utils.BungeniBNode;
import org.bungeni.editor.BungeniEditorProperties;
import org.bungeni.editor.actions.EditorActionFactory;
import org.bungeni.editor.actions.IEditorActionEvent;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.editor.dialogs.debaterecord.DebateRecordMetadata;
import org.bungeni.editor.dialogs.metadatapanel.SectionMetadataLoad;
import org.bungeni.editor.metadata.EditorDocMetadataDialogFactory;
import org.bungeni.editor.metadata.IEditorDocMetadataDialog;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.editor.selectors.metadata.SectionMetadataEditor;
import org.bungeni.editor.toolbar.BungeniToolbarTargetProcessor;
import org.bungeni.ooo.transforms.impl.BungeniTransformationTarget;
import org.bungeni.ooo.transforms.impl.BungeniTransformationTargetFactory;
import org.bungeni.ooo.transforms.impl.IBungeniDocTransform;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.utils.BungeniFrame;
import org.bungeni.utils.CommonFileFunctions;
import org.bungeni.utils.CommonStringFunctions;
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
    private String[] arrDocTypes = { "Acts" , "DebateRecords", "Bills" };
    //vector that houses the list of document headings used by the display tree
    private Vector<DocStructureElement> mvDocumentHeadings = new Vector<DocStructureElement>();
    private Vector<String> mvSections = new Vector<String>();
    private DefaultMutableTreeNode sectionsRootNode;
    private Timer sectionNameTimer;
    private String currentSelectedSectionName = "";
    private Timer docStructureTimer;
    private Timer componentsTrackingTimer;
    
    private Thread tStructure;
    private changeStructureItem selectedChangeStructureItem;
    private JTree treeDocStructureTree;
    private JTree treeSectionStructure;
    private JPopupMenu popupMenuTreeStructure = new JPopupMenu();
    private boolean mouseOver_TreeDocStructureTree = false;
    private boolean program_refresh_documents = false;
    
    private  TreeMap<String, editorTabbedPanel.componentHandleContainer> editorMap;
    
    
    private String activeDocument; 
    private DocumentMetadataTableModel docMetadataTableModel;
    
    private HashMap<String, ICollapsiblePanel> dynamicPanelMap = new HashMap<String,ICollapsiblePanel>();
    private HashMap<String, IFloatingPanel> floatingPanelMap = new HashMap<String,IFloatingPanel>();
    private ArrayList<ITabbedPanel> m_tabbedPanelMap = new ArrayList<ITabbedPanel>();
    
    
    private metadataTabbedPanel metadataTabbedPanel = null;

    private JFrame metadataPanelParentFrame = null;

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
       //initListDocuments();
       initProviders();
       initFields();
       //();
       SwingUtilities.invokeLater(new Runnable(){

            public void run() {
                initFloatingPane();
            }
           
       });
       //initFloatingPane();
       
        //initCollapsiblePane();
       //initNotesPanel();
       //initBodyMetadataPanel();
       initTimers();
       log.debug("calling initOpenDOcuments");
       initOpenDocuments();
      
       /***** control moved to other dialog... 
       updateListDocuments();
        *****/
       //initTableDocMetadata();
       initTabbedPanes();
       initModeLabel();
       initSwitchTabs();
       initSectionMetadataDisplay();
       //metadataChecks();
       
    }
    /*
    private void hideUnusedPanels(){
         this.panelMetadata.setEnabled(false); this.panelMetadata.setVisible(false);
        this.panelBodyMetadata.setEnabled(false); this.panelBodyMetadata.setVisible(false);
        this.panelMarkup.setEnabled(false); this.panelMarkup.setVisible(false);
    }*/
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

        
/*        
        org.bungeni.editor.panels.sectionTreeMetadataPanel sectpanel = new org.bungeni.editor.panels.sectionTreeMetadataPanel (ooDocument, parentFrame);
        this.jTabsContainer.insertTab(sectpanel.getAccessibleContext().getAccessibleDescription(), 
                null,
                (Component) sectpanel,  sectpanel.getAccessibleContext().getAccessibleDescription(), 3 );
        
                */
    }

    
    private void updateTabbedPanes(){
        for (ITabbedPanel panel: m_tabbedPanelMap ) {
            panel.setOOComponentHandle(ooDocument);
            panel.refreshPanel();
        }
    }
    
    SectionMetadataDisplay objMetaDisplay;
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
/*
    private void initTableDocMetadata(){
        
        //document metadata table model is created
        docMetadataTableModel = new DocumentMetadataTableModel(ooDocument);
        //add the check for valid metadata here 
        //if (true) set it to the table, else error 
       
       if(checkTableDocMetadata()){
            docMetadataTableModel.getMetadataSupplier().updateMetadataToDocument("doctype");
            tableDocMetadata.setModel(docMetadataTableModel );
       }
       
         //DocumentMetadataTableModel mModel  = (DocumentMetadataTableModel) tableDocMetadata.getModel();
         DocumentMetadata [] m=docMetadataTableModel.getMetadataSupplier().getDocumentMetadata();
         
         
         //checkDocument Type here
         //check to see if current document has docttype
    
         for(int i=0;i<m.length;i++){
              if ((m[i].getName().equals("doctype") && m[i].getValue().equals("")))
              {
                  log.debug("Setting document type value");
                  m[i].setValue(activeDocument);
                  // docMetadataTableModel.getMetadataSupplier().updateMetadataToDocument("doctype");
             } 
          }
          
         
         
        
        
        
        //table model is set
        //tableDocMetadata.setModel(docMetadataTableModel );
        //various listeners are added 
       //cboListDocuments.addVetoableChangeListener(new cboListDocumentsVetoableChangeListener());
    }    
*/
    protected void setOODocumentObject (OOComponentHelper ooDoc) {
        this.ooDocument = ooDoc;
    }
    
    public OOComponentHelper getOODocumentObject(){
        return this.ooDocument;
    }
    
    /*
    private void refreshTableDocMetadataModel(){
       
        docMetadataTableModel = new DocumentMetadataTableModel(ooDocument);
        tableDocMetadata.setModel(docMetadataTableModel );

        
        
    }
    */
  
    /*
     *
     *at this point the table model for the metadata table has already been set,
     *we are checking the metadata of the table
     */
    /*
    private boolean metadataChecks(){
        try {
      DocumentMetadataTableModel mModel  = (DocumentMetadataTableModel) tableDocMetadata.getModel();
      DocumentMetadata [] m=mModel.getMetadataSupplier().getDocumentMetadata();
     //checkDocument Type here
      //check to see if current document has docttype
    
       for(int i=0;i<m.length;i++){
              if(m[i].getName().equals("doctype") && m[i].getValue().equals("")){
                  log.debug("Setting document type value");
                  m[i].setValue(activeDocument);
                 // mModel.getMetadataSupplier().updateMetadataToDocument("doctype");
             }
          }
        
          mModel.refreshMetaData();
        } catch (Exception ex) {
            log.error ("metadataChecks = " + ex.getMessage());
            log.error("metadataChecks = " + CommonExceptionUtils.getStackTrace(ex));
        } finally {
      
       return true;
        }
    } */
	
    private void initListDocuments(){
        log.debug("initListDocuments: init");
        //String[] listDocuments = getCurrentlyOpenDocuments().keySet().toArray(new String[getCurrentlyOpenDocuments().keySet().size()]);
        ArrayList<componentHandleContainer> listDocuments = this.getDocumentsComboModel();
        this.cboListDocuments.setModel(new DefaultComboBoxModel(listDocuments.toArray()));
    }

    
    private void initOpenDocumentsList(){
             try {
        log.debug("initOpenDocumentsList: getting components");
        XEnumerationAccess enumComponentsAccess = BungenioOoHelper.getDesktop().getComponents();
        XEnumeration enumComponents = enumComponentsAccess.createEnumeration();
        log.debug("initOpenDocumentsList: enumerating components");
        int i=0;
        //cboListDocuments.removeAllItems();
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
                    /*
                     XFrame xframe = xDoc.getCurrentController().getFrame();
                    String strTitle = (String) ooQueryInterface.XPropertySet(xframe).getPropertyValue("Title");
                    int dashIndex = strTitle.lastIndexOf("-");
                    if (dashIndex != -1)
                        strTitle = strTitle.substring(0, dashIndex);
                     */
                    String strTitle = OOComponentHelper.getFrameTitle(xDoc);
                    XComponent xComponent = (XComponent)UnoRuntime.queryInterface(XComponent.class, nextElem);
                    componentHandleContainer compContainer = new componentHandleContainer(strTitle, xComponent);
                    if (!editorMap.containsKey(compContainer.componentKey())) {
                        compContainer.setEventBroadcastListener();
                        editorMap.put(compContainer.componentKey(), compContainer);
                    }
                   // this.cboOpenDocuments.addItem(i+ " - " + strTitle);
                }
            }
        }
        } catch (Exception ex) {
           log.error("InitOpenDocumentsList error :" + ex.getMessage());
           log.error("InitOpenDocumentsList stacktrace : " + CommonExceptionUtils.getStackTrace(ex));
        }
    }
    /*
    private String getFrameTitle(XTextDocument xDoc) {
        String strTitle = "";
        try {
            XFrame xframe = xDoc.getCurrentController().getFrame();
            strTitle = (String) ooQueryInterface.XPropertySet(xframe).getPropertyValue("Title");
            int dashIndex = strTitle.lastIndexOf("-");
            if (dashIndex != -1)
               strTitle = strTitle.substring(0, dashIndex);
 
        } catch (WrappedTargetException ex) {
            log.error(ex.getMessage());
        } catch (UnknownPropertyException ex) {
            log.error(ex.getMessage());
        } finally {
        return strTitle;
        }
    }
    */
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
    
    private void initSwitchTabs(){
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
        
    }
    
    public void setOOoHelper(BungenioOoHelper helper) {
        this.ooHelper = helper;
        //cboListDocuments.addItemListener(new cboListDocumentsItemListener());
        initOpenDocuments();
    }
    
  
    private void initFields(){
        //initTree();
       //rem treeDocStructure.setModel(new DefaultListModel());
       //rem  this.initSectionTree();
       //rem  this.initSectionStructureTree();
       // treeSectionStructure = new JTree();
        //treeSectionStructure.setExpandsSelectedPaths(true);
        //treeDocStructureTree.addMouseListener(new treeDocStructureTreeMouseListener());

        
        
        //initList();
        //initSectionList();
        //clear meatada listbox
        //listboxMetadata.setModel(new DefaultListModel());
        //init combo change structure
        /*
        changeStructureItem[] items = initChangeStructureItems();
        changeStructureItem itemDefault = null;
        String defaultHierarchyView = BungeniEditorProperties.getEditorProperty("defaultHierarchyView");
        for (int i=0; i < items.length; i++) {
            if (items[i].getIndex().equalsIgnoreCase(defaultHierarchyView)){
                itemDefault = items[i];
            }
            comboChangeStructure.addItem(items[i]);    
        }
        comboChangeStructure.addActionListener (new comboChangeStructureListener());
        if (itemDefault != null)
            comboChangeStructure.setSelectedItem(itemDefault);
        selectedChangeStructureItem = (changeStructureItem)comboChangeStructure.getSelectedItem();
        */
        //rem initList();
    }
    /*
    private void initSectionTree(){
        treeDocStructureTree = new JTree();
        treeDocStructureTree.setExpandsSelectedPaths(true);
        treeDocStructureTree.addMouseListener(new treeDocStructureTreeMouseListener());
        NodeMoveTransferHandler transferHandler = new NodeMoveTransferHandler(this);
        treeDocStructureTree.setTransferHandler(transferHandler);
        treeDocStructureTree.setDropTarget(new TreeDropTarget(transferHandler));
        treeDocStructureTree.setDragEnabled(true);
        treeDocStructureTreeCellRenderer render = new treeDocStructureTreeCellRenderer();
        treeDocStructureTree.setCellRenderer(render);
        ComponentUI ui = treeDocStructureTree.getUI();
         if (ui instanceof BasicTreeUI){
             ((BasicTreeUI)ui).setExpandedIcon(CommonTreeFunctions.treeMinusIcon());
             ((BasicTreeUI)ui).setCollapsedIcon(CommonTreeFunctions.treePlusIcon());
         }
    }
    */
    
    public void uncheckEditModeButton() {
        //toggleEditSection.setSelected(false);
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
                    //initNotesPanel();
                    //initBodyMetadataPanel();
                    //check and see if the doctype property exists before you refresh the metadata table
                    ///if(!ooDocument.propertyExists("doctype")){
                    ///   JOptionPane.showMessageDialog(null,"This is not a bungeni document.","Document Type Error",JOptionPane.ERROR_MESSAGE);
                    ///   
                    ///} 
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
    
    private changeStructureItem[] initChangeStructureItems() {
        changeStructureItem itema = new changeStructureItem ("VIEW_PARAGRAPHS", "View Paragraphs");
        changeStructureItem itemb = new changeStructureItem ("VIEW_SECTIONS", "View Sections");
        changeStructureItem itemc = new changeStructureItem ("VIEW_PRETTY_SECTIONS", "View Structure");
        changeStructureItem[] items = new changeStructureItem[3];
        items[0] = itemb;
        items[1] = itema;
        items[2] = itemc;
        return items;
    }
    
    class comboChangeStructureListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JComboBox box = (JComboBox) e.getSource();
            changeStructureItem theItem = (changeStructureItem) box.getSelectedItem();
            String theIndex = theItem.getIndex();
            selectedChangeStructureItem = theItem;
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
            /*XModel xModel = ooQueryInterface.XModel(ooDocument.getComponent());
            XWindow xCompWindow = xModel.getCurrentController().getFrame().getComponentWindow();
            XWindow xContWindow = xModel.getCurrentController().getFrame().getContainerWindow();
            com.sun.star.awt.Rectangle rSize = xCompWindow.getPosSize();
            com.sun.star.awt.Rectangle rContSize = xContWindow.getPosSize();*/
            
           // floatingFrame.setLocation(editorTabbedPanel.coordX, editorTabbedPanel.coordY);
            
          floatingFrame.setLocation(windowX, editorTabbedPanel.coordY + 50);  // Don't use "f." inside constructor.
            floatingFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            
      
    }

    /*
    private void initCollapsiblePane(){
     try {
     
     panelMarkup.removeAll();    
     panelMarkup.setLayout(new FlowLayout());
     
     StackedBox box = new StackedBox(); 
     
     //create scroll pane with stacked box
     log.debug("initializing stackedbox");
     
     JScrollPane scrollPane = new JScrollPane(box);
     scrollPane.setBorder(null);
     //add the scroll pane to the scroll pane
     panelMarkup.add(scrollPane, BorderLayout.CENTER);
   
     ICollapsiblePanel generalEditorPanel = CollapsiblePanelFactory.getPanelClass("generalEditorPanel4");
     generalEditorPanel.setOOComponentHandle(ooDocument);
     generalEditorPanel.setParentWindowHandle(parentFrame);
     dynamicPanelMap.put("generalEditorPanel4", generalEditorPanel);
     
     box.addBox("Editor Tools", generalEditorPanel.getObjectHandle());
     
     
     }
     catch (Exception e){
         log.error("InitCollapsiblePane: exception : "+ e.getMessage());
         log.error("InitCollapsiblePane: stacktrace: " + org.bungeni.ooo.utils.CommonExceptionUtils.getStackTrace(e));
     }
     
    }
    */
    
    private void updateFloatingPanels(){
        if (!floatingPanelMap.isEmpty()){
            Iterator<String> panelNames = floatingPanelMap.keySet().iterator();
                         while (panelNames.hasNext  ()) {
                             
                             IFloatingPanel panelObj = floatingPanelMap.get(panelNames.next());
                             panelObj.setOOComponentHandle(ooDocument);
                         }
        }
    }
    /*
    private void updateCollapsiblePanels(){
                  if (!dynamicPanelMap.isEmpty()) {
                         Iterator<String> panelNames = dynamicPanelMap.keySet().iterator();
                         while (panelNames.hasNext  ()) {
                             
                             ICollapsiblePanel panelObj = dynamicPanelMap.get(panelNames.next());
                             panelObj.setOOComponentHandle(ooDocument);
                         }
                    }
    }
    */
    private editorTabbedPanel self() {
        return this;
    }
    
    
    
    private class selectMetadataModel {
        String query;
        String text;
        public selectMetadataModel(String t, String q) {
            text = t;
            query = q;
        }
        @Override
        public String toString() {
            return text;
        }
    }
    /*
    private void initBodyMetadataPanel(){
        //initilize cboSelectBodyMetadata
        
        cboSelectBodyMetadata.removeAllItems();
        cboSelectBodyMetadata.addItem(new selectMetadataModel("Members of Parliament", GeneralQueryFactory.Q_FETCH_ALL_MPS()));;
        panelEditDocumentMetadata.setVisible(false);
    }
     **/
    
    public Component getComponentHandle(){
        return this;
    }
    /*
    private void initList() {
        if (!ooDocument.isXComponentValid()) return;
        if (selectedChangeStructureItem.getIndex().equals("VIEW_PARAGRAPHS")) {
            log.debug("initList: initParagraphList");
            scrollPane_treeDocStructure.setViewportView(treeDocStructure);
            initParagraphList(); 
        } else if (selectedChangeStructureItem.getIndex().equals("VIEW_SECTIONS")){
            log.debug("initList: initSectionList");
            scrollPane_treeDocStructure.setViewportView(treeDocStructureTree);
            //do not refresh if the mouse is over the tree
            if (mouseOver_TreeDocStructureTree) {
                log.debug("initList: mouseOver treeDocStructure = true");
                return;
            }
           //rem  initSectionList();
        } else if (selectedChangeStructureItem.getIndex().equals("VIEW_PRETTY_SECTIONS")){
             scrollPane_treeDocStructure.setViewportView(treeSectionStructure);
                      //   if (mouseOver_TreeDocStructureTree) {
                      //      log.debug("initList: mouseOver treeDocStructure = true");
                      //      return;
                      //  }
            //commented for section refresh changes / june 2007 / initSectionStructureTreeModel();
            //initSectionList();
             synchronized (ooDocument) {
                 //update the named callback - must be same as seter callback in original node
                NodeDisplayTextSetter nsetter = new NodeDisplayTextSetter(ooDocument);
                BungeniBNode.setINodeSetterCallback(nsetter);
                
                BungeniBTree newTree = DocumentSectionProvider.getNewFriendlyTree();
                log.debug("initList: refreshing tree : " + newTree.toString());
                BungeniBNode newRootNode = newTree.getFirstRoot();
                
                DocumentSectionFriendlyAdapterDefaultTreeModel model = (DocumentSectionFriendlyAdapterDefaultTreeModel) treeSectionStructure.getModel();
                DefaultMutableTreeNode mnode = (DefaultMutableTreeNode) model.getRoot();
                BungeniBNode origNode = (BungeniBNode) mnode.getUserObject();
                BungeniTreeRefactorTree refTree = new BungeniTreeRefactorTree (model, origNode, newRootNode);
                refTree.doMerge();
             }
             //this.initSectionStructureTreeModel();
        }   
   }
     */ 
        
    private void clearTree(){
        treeDocStructureTree.removeAll();
       // treeDocStructureTree.updateUI();

    }
   

    private void initSectionsArray(){
        BungeniBTree treeRoot = new BungeniBTree();
        TreeMap<Integer,String> namesMap = new TreeMap<Integer,String>();
        try {
            if (!ooDocument.isXComponentValid()) return;
         	/*
         	first clear the JTree
         	*/
            clearTree();
            /*
            do a basic check to see if the root section exists
            */
            if (!ooDocument.getTextSections().hasByName(ROOT_SECTION)) {
                log.error("InitSectionsArray: no root section found");
                return;
            }
            /*
            get the root section and it as the root node to the JTree
            */
            Object root = ooDocument.getTextSections().getByName(ROOT_SECTION);
            log.debug("InitSectionsArray: Adding root node");
            treeRoot.addRootNode(new String(ROOT_SECTION));
            /*
            now get the enumeration of the TextSection
            */

            int currentIndex = 0;
            String parentObject = ROOT_SECTION;
            XTextSection theSection = ooQueryInterface.XTextSection(root);
            XTextRange range = theSection.getAnchor();
            XText xText = range.getText();
            XEnumerationAccess enumAccess = (XEnumerationAccess) UnoRuntime.queryInterface(XEnumerationAccess.class, xText);
            //namesMap.put(currentIndex++, parentObject);
            XEnumeration enumeration = enumAccess.createEnumeration();
             log.debug("InitSectionsArray: starting Enumeration ");
            /*
            start the enumeration of sections first
            */ 
             while (enumeration.hasMoreElements()) {
                 Object elem = enumeration.nextElement();
                 XPropertySet objProps = ooQueryInterface.XPropertySet(elem);
                 XPropertySetInfo objPropsInfo = objProps.getPropertySetInfo();
                 /*
                  *enumerate only TextSection objects
                  */
                 if (objPropsInfo.hasPropertyByName("TextSection")) {
                     XTextSection xConSection = (XTextSection) ((Any)objProps.getPropertyValue("TextSection")).getObject();
                     if (xConSection != null ) {
                         /*
                          *Get the section name 
                          */   
                         XNamed objSectProps = ooQueryInterface.XNamed(xConSection);
                         String sectionName = objSectProps.getName();
                         /*
                          *only enumerate non root sections
                          */ 
                         if (!sectionName.equals(ROOT_SECTION)) {
                             log.debug("InitSectionsArray: Found Section :"+ sectionName);
                              /*
                              *check if the node exists in the tree
                              */
                              if (!namesMap.containsValue(sectionName)) {
                              		namesMap.put(currentIndex++, sectionName);
                              }
                         } // if (!sectionName...)     
                     } // if (xConSection !=...)
                 } // if (objPropsInfo.hasProper....)
             } // while (enumeration.hasMore.... )
             
             /*
              *now scan through the enumerated list of sections
              */
             Iterator namesIterator = namesMap.keySet().iterator();
              while (namesIterator.hasNext()) {
                  Integer iOrder = (Integer) namesIterator.next();
                  String sectionName = namesMap.get(iOrder);
                  /*
                   *check if the sectionName exists in our section tree
                   */
                  BungeniBNode theNode = treeRoot.getNodeByName(sectionName);
                  if (theNode == null ) {
                      /*
                       *the node does not exist, build its parent chain
                       */
                       ArrayList<String> parentChain = buildParentChain(sectionName);
                       /*
                        *now iterate through the paren->child hierarchy of sections
                        */
                       Iterator<String> sections = parentChain.iterator();
                       BungeniBNode currentNode = null, previousNode = null;
                       while (sections.hasNext()) {
                           String hierSection = sections.next();
                           currentNode =  treeRoot.getNodeByName(hierSection);
                           if (currentNode == null ) {
                               /* the node doesnt exist in the tree */
                               if (previousNode != null ) {
                                    treeRoot.addNodeToNamedNode(previousNode.getName(), hierSection);
                                    previousNode = treeRoot.getNodeByName(hierSection);
                                    if (previousNode == null ) 
                                        log.error("previousNode was null");
                               } else {
                                   log.error("The root section was not in the BungeniBTree hierarchy, this is an error condition");
                               }
                           } else {
                               /* the node already exists...*/
                               previousNode = currentNode;
                           }
                       }
                  }
                  
                 
              }
               convertBTreetoJTreeNodes(treeRoot);
        } catch (NoSuchElementException ex) {
            log.error(ex.getMessage());
        } catch (UnknownPropertyException ex) {
            log.error(ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error(ex.getMessage());
        }
    }

    private ArrayList<String> buildParentChain(String Sectionname){
          XTextSection currentSection = ooDocument.getSection(Sectionname);
          XTextSection sectionParent=currentSection.getParentSection();
          XNamed parentProps = ooQueryInterface.XNamed(sectionParent);
          String parentSectionname = parentProps.getName();
          String currentSectionname = Sectionname;
          ArrayList<String> nodeHierarchy = new ArrayList<String>();
          //array list goes from child(0) to ancestors (n)
          log.debug("buildParentChain: nodeHierarchy: Adding "+ currentSectionname);
          nodeHierarchy.add(currentSectionname);
          while (1==1) {
              //go up the hierarchy until you reach root.
              //break upon reaching the parent
              if (parentSectionname.equals(ROOT_SECTION)) {
                  nodeHierarchy.add(parentSectionname);
                  log.debug("buildParentChain: nodeHierarchy: Adding "+ parentSectionname + " and breaking.");
                  break;
              }
             nodeHierarchy.add(parentSectionname);
             log.debug("buildParentChain: nodeHierarchy: Adding "+ parentSectionname + ".");
             currentSectionname = parentSectionname;
             sectionParent = sectionParent.getParentSection();
             parentProps = ooQueryInterface.XNamed(sectionParent);
             parentSectionname = parentProps.getName();
          } //end while (1== 1)
          if (nodeHierarchy.size() > 1 )
            Collections.reverse(nodeHierarchy);
          return nodeHierarchy;
    }
    
    private void convertBTreetoJTreeNodes(BungeniBTree theTree){
        //TreeMap<Integer,BungeniBNode> sectionMap = theTree.getTree();
        BungeniBNode rootNode = theTree.getNodeByName(ROOT_SECTION);
        this.sectionsRootNode = null;
        this.sectionsRootNode = new DefaultMutableTreeNode(new String(ROOT_SECTION));
        TreeMap<Integer,BungeniBNode> sectionMap = rootNode.getChildrenByOrder();
        Iterator<Integer> rootIter = sectionMap.keySet().iterator();
           int depth = 0;
           while (rootIter.hasNext()) {
                Integer key = (Integer) rootIter.next();
                BungeniBNode n = sectionMap.get(key);
                DefaultMutableTreeNode n_child = new DefaultMutableTreeNode(n.getName());
                sectionsRootNode.add(n_child);
                //sbOut.append(padding(depth) + n.getName()+ "\n");
                //walkNodeByOrder(n, depth);
                walkBNodeTree(n , n_child);
            }
    }
    
    private void walkBNodeTree(BungeniBNode theNode, DefaultMutableTreeNode pNode){
        if (theNode.hasChildren()) {
           TreeMap<Integer, BungeniBNode> n_children = theNode.getChildrenByOrder();
           Iterator<Integer> nodesByOrder = n_children.keySet().iterator();
           while (nodesByOrder.hasNext()) {
               Integer key = (Integer) nodesByOrder.next();
               BungeniBNode n = n_children.get(key);
               DefaultMutableTreeNode dmt_node = new DefaultMutableTreeNode(n.getName());
               pNode.add(dmt_node);
               walkBNodeTree(n, dmt_node);
           }
        } else
            return;
    }
    
    /****this is the old sections iterator, it uses getTextSections(), and the getChildSections() API, 
     * which does not display sections in the correct order
     * see http://www.openoffice.org/issues/show_bug.cgi?id=82420
     *****/
    
/*    
    private void recurseSections (XTextSection theSection, DefaultMutableTreeNode node ) {
        try {
     
      //  mvSections.add(padding + sectionName);
      //  log.debug("recurse sections, section name:"+padding+sectionName);
        //recurse children
        XTextSection[] sections = theSection.getChildSections();
         
        if (sections != null ) {
            if (sections.length > 0 ) {
                //start from last index and go to first
                for (int nSection = sections.length - 1 ; nSection >=0 ; nSection--) {
                    log.debug ("section name = "+ooQueryInterface.XNamed(sections[nSection]).getName() );
                    //get the name for the section and add it to the root node.
                    XPropertySet childSet = ooQueryInterface.XPropertySet(sections[nSection]);
                    String childSectionName = (String) childSet.getPropertyValue("LinkDisplayName");
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(childSectionName);
                    
                    node.add(newNode);
                    
                    recurseSections (sections[nSection], newNode);
                    
                }
            } else 
                return;
        } else 
            return;
        } catch (UnknownPropertyException ex) {
            log.error(ex.getMessage());
        } catch (WrappedTargetException ex ) {
            log.error(ex.getMessage());
        }
    
    }
  */  
    /*
    private void initSectionList() {
        initSectionsArray();  
        log.debug("setting defaultTreeModel to sectionsRootNode");
        treeDocStructureTree.setModel(new DefaultTreeModel(sectionsRootNode));
        //-tree-deprecated--CommonTreeFunctions.expandAll(treeDocStructureTree, true);
        CommonTreeFunctions.expandAll(treeDocStructureTree);
      }
    */
    
    /*
    private void initSectionStructureTree(){
         this.treeSectionStructure = new JTree();
         treeSectionStructure.setExpandsSelectedPaths(true);
         
        // DefaultTreeCellRenderer sectionTreeRender = (DefaultTreeCellRenderer) this.treeSectionStructure.getCellRenderer();
         ImageIcon minusIcon = CommonTreeFunctions.treeMinusIcon();
         ImageIcon plusIcon = CommonTreeFunctions.treePlusIcon();
       //  sectionTreeRender.setOpenIcon(minusIcon);
       //  sectionTreeRender.setClosedIcon(plusIcon);
     //    UIManager.put("Tree.expandedIcon", minusIcon);
     //    UIManager.put("Tree.collapsedIcon", plusIcon);
      //   sectionTreeRender.setLeafIcon(null);
      //   treeSectionStructure.setCellRenderer(sectionTreeRender);
         treeSectionStructure.setCellRenderer(new treeViewPrettySectionsTreeCellRenderer());
         treeSectionStructure.setShowsRootHandles(true);
         ComponentUI treeui = treeSectionStructure.getUI();
         if (treeui instanceof BasicTreeUI){
             ((BasicTreeUI)treeui).setExpandedIcon(minusIcon);
             ((BasicTreeUI)treeui).setCollapsedIcon(plusIcon);
         }
       initSectionStructureTreeModel();  
    }
    private void initSectionStructureTreeModel(){
        DocumentSectionFriendlyAdapterDefaultTreeModel model = DocumentSectionFriendlyTreeModelProvider.create() ;//_without_subscription();
        //DefaultTreeModel model = new DefaultTreeModel(buildTreeModel());
        this.treeSectionStructure.setModel(model);
        CommonTreeFunctions.expandAll(treeSectionStructure);
    }
    */
        
   
   
public TreeMap<String, editorTabbedPanel.componentHandleContainer> getCurrentlyOpenDocuments(){
    return this.editorMap;
}

     class treePopupMenu {
        TreeMap<String,String> popupMenuMap = new TreeMap<String, String>();
       
        treePopupMenu (String menu_name_to_load_from_settings) {
            //load the menu from settings, probably the db.
            if (menu_name_to_load_from_settings.equals("treeDocStructureTree")) {
                addItem("0_GOTO_SECTION", "Goto Section");
                addItem("1_ADD_PARA_BEFORE_SECTION", "Add Para Before Section");
                addItem("2_ADD_PARA_AFTER_SECTION", "Add Para After Section");
                addItem("3_DELETE_SECTION", "Remove This Section");
            }
        }
        
        public void addItem(String menu_id, String text) {
            popupMenuMap.put(menu_id, text);
        }
        
        public TreeMap<String, String> getMenus() {
            return popupMenuMap;
        }
        
        
    }
    /*
   class treeDocStructureTreeMouseListener implements MouseListener {
       private treePopupMenu theMenu ; 
       treeDocStructureTreeMouseListener() {
            theMenu  = new treePopupMenu("treeDocStructureTree");
        }
        
        public void mouseClicked(MouseEvent e) {
        }     
        
         public void mousePressed(MouseEvent evt) {
             
                if (!toggleEditSection.isSelected()) {
                    int selRow = treeDocStructureTree.getRowForLocation(evt.getX(), evt.getY());
                    TreePath selPath = treeDocStructureTree.getPathForLocation(evt.getX(), evt.getY());
                     if (selRow != -1 ) {
                         if (evt.getClickCount() == 1) {
                             DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                             System.out.println("node = "+ (String) node.getUserObject());   
                             String selectedSection = (String)node.getUserObject();
                             createPopupMenuItems (selectedSection);
                             popupMenuTreeStructure.show(evt.getComponent(), evt.getX(), evt.getY());
                          return;
                         }  

                     }
                }      
        }

        
        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
            log.debug("treeDocStructureTree: mouseEntered!!");
            mouseOver_TreeDocStructureTree = true;
        }

        public void mouseExited(MouseEvent e) {
            log.debug("treeDocStructureTree: mouseExiting!!");
            mouseOver_TreeDocStructureTree = false;
        }
       
               
       private void createPopupMenuItems (String selectedSection){
                popupMenuTreeStructure.removeAll();
                //treePopupMenu menu = new treePopupMenu("treeDocStructureTree");
                //popupMenu.add(new treePopupMenuAction(popup_section_actions[0], baseNodeAction, PopupTypeIdentifier.VIEW_ACTIONS));
                TreeMap<String,String> menus = theMenu.getMenus();
                Iterator<String> keys = menus.keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    popupMenuTreeStructure.add(new treeDocStructureTreePopupAction(key, menus.get(key), selectedSection));
                }
                //popupMenuTreeStructure.add(new treeDocStructureTreePopupAction(org.bungeni.editor.dialogs.editorTabbedPanel.PopupTypeIdentifier.GOTO_SECTION.popup_id(), selectedSection));
               
           }
   }
    */
   /*
    *Drag and Drop handlers for JTree - treeDocStructureTree
    * available under the tree package
    */
    
   
      class treeDocStructureTreePopupAction extends AbstractAction {
           
          treeDocStructureTreePopupAction () {
              
          }
          
          treeDocStructureTreePopupAction (String actionId, String actionText, String sectionName) {
                super(actionText);
                putValue("ACTION_ID", actionId);
                putValue("USER_OBJECT", sectionName);
            }
       
          public void actionPerformed(ActionEvent e) {
              Object value = getValue("USER_OBJECT");
              Object action_id = getValue("ACTION_ID");
              if (value != null ) {
                  processPopupSelection((String)value, (String) action_id);
              }
            }
          
          public void processPopupSelection(String sectionName, String action_id ) {
              //go to selected range
             XTextSection xSelectSection = ooDocument.getSection(sectionName);
          
              if (action_id.equals("0_GOTO_SECTION")) {
                      if (xSelectSection != null  ) {
                          XTextRange sectionRange = xSelectSection.getAnchor();
                          XTextViewCursor xViewCursor = ooDocument.getViewCursor();
                          xViewCursor.gotoRange(sectionRange, false);
                      }
                  
              } else if (action_id.equals("1_ADD_PARA_BEFORE_SECTION")) {
                   XTextContent oPar = ooQueryInterface.XTextContent(ooDocument.createInstance("com.sun.star.text.Paragraph"));
                   XRelativeTextContentInsert xRelativeText = ooQueryInterface.XRelativeTextContentInsert(ooDocument.getTextDocument().getText());
                    try {
                        xRelativeText.insertTextContentBefore(oPar, ooQueryInterface.XTextContent(xSelectSection));
                    } catch (com.sun.star.lang.IllegalArgumentException ex) {
                        log.debug("insertTextContentbefore :" + ex.getMessage());
                    }
                    //move visible cursor to the point where the new para was added
                   ooDocument.getViewCursor().gotoRange(xSelectSection.getAnchor().getStart(), false);
              } else if (action_id.equals("2_ADD_PARA_AFTER_SECTION")) {
                     XTextContent oPar = ooQueryInterface.XTextContent(ooDocument.createInstance("com.sun.star.text.Paragraph"));
                     XRelativeTextContentInsert xRelativeText = ooQueryInterface.XRelativeTextContentInsert(ooDocument.getTextDocument().getText());
                     try {
                            xRelativeText.insertTextContentAfter(oPar, ooQueryInterface.XTextContent(xSelectSection));
                     } catch (com.sun.star.lang.IllegalArgumentException ex) {
                            log.error("insertTextContentbefore :" + ex.getMessage());
                     }
                     //move visible cursor to point where para was added
                    ooDocument.getViewCursor().gotoRange(xSelectSection.getAnchor().getEnd(), false);
              } else if (action_id.equals("3_DELETE_SECTION")) {
                    //first select the range...
                    
                    XTextContent sectionContent = ooQueryInterface.XTextContent(xSelectSection);
                    XTextRange sectionRange = sectionContent.getAnchor();
                    ooDocument.getViewCursor().gotoRange(sectionRange, false);
                    
                    //now prompt with a warning....
                    int nRet = MessageBox.Confirm(self(), "WARNING, The section: "+sectionName+ ", and its contents, \n" +
                            "and any other sections nested inside it will be removed. \n " +
                            "Are you sure you want to proceed ?", "Deletion Warning");
                    if (nRet == JOptionPane.YES_OPTION) {
                        //delete section and contents
                         //aTextRange=section.getAnchor()
                        ExternalMacro RemoveSectionAndContents = ExternalMacroFactory.getMacroDefinition("RemoveSectionAndContents");
                        RemoveSectionAndContents.addParameter(ooDocument.getComponent());
                        RemoveSectionAndContents.addParameter(sectionName);
                        ooDocument.executeMacro(RemoveSectionAndContents.toString(), RemoveSectionAndContents.getParams());
            
                       } else 
                        return;
              }
          }
    }
   
   
    /**
     * Mouse event listener for list box displaying document structure
     */
    class DocStructureListMouseListener implements MouseListener{
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2){
                JList listBox = (JList)e.getSource();
                listBox.getMaxSelectionIndex();
                int nIndex = listBox.locationToIndex(e.getPoint());
                //JOptionPane.showMessageDialog(null, "current selected index is = "+ nIndex);
                
                //get view cursor 
                XTextViewCursor xViewCursor = ooDocument.getViewCursor();
                //get the current object range
                DocStructureElement docElement = (DocStructureElement)mvDocumentHeadings.elementAt(nIndex);
                //move the view cursor to the element's range
                xViewCursor.gotoRange(docElement.getRange(), false);
            }
            if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK ){
                //trap right click
            }
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
        
    }
    class DocStructureListSelectionHandler implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent e) {
        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
        if (lsm.isSelectionEmpty()) {
            return;
        } else {
            // Find out which indexes are selected.
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();
            for (int i = minIndex; i <= maxIndex; i++) {
                if (lsm.isSelectedIndex(i)) {
                    JOptionPane.showMessageDialog(null, "Current Selected Index is = "+ i);
                }
            }
        }
    }
}

    
   private class documentNodeMapKey {
       int level;
       int count;
   }
   
   
   
    private void initializeValues(){
        //get metadata property alues
        String strAuthor = ""; String strDocType = "";
          try {
        if (ooDocument.propertyExists("Bungeni_DocAuthor")){
          
                strAuthor = ooDocument.getPropertyValue("Bungeni_DocAuthor");
           
        }
        if (ooDocument.propertyExists("doctype")){
            strDocType = ooDocument.getPropertyValue("doctype");
        }
        
        //txtDocAuthor.setText(strAuthor);
        //txtDocType.setText(strDocType);
         } catch (UnknownPropertyException ex) {
                ex.printStackTrace();
            }
       
    }
    
    /**
     * Class that handles rendering of List cell elements, in the Document Structure listbox
     */
    /*
public class DocStructureListElementRenderer extends JLabel implements ListCellRenderer {
    private  final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);
    private Color [] COLOR_LEVELS = {
                        new Color(104, 104,104),
                        new Color(124, 124,124),
                        new Color(144, 144,144),
                        new Color(164, 164,164),
                        new Color(184, 184,184),
                        new Color(204, 204,204),
                        new Color(224, 224,224)
    };
    private Border raisedEtched, lineBorder;
    public DocStructureListElementRenderer( ) {
        setOpaque(true);
        setIconTextGap(12);
         raisedEtched = BorderFactory.createRaisedBevelBorder();
         lineBorder = BorderFactory.createLineBorder(Color.GRAY);
        
    }

    public Component getListCellRendererComponent(
        JList list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus)
    {
        DocStructureElement entry = (DocStructureElement)value;
        int nMaxIndex = COLOR_LEVELS.length - 1;
        int nLevel = entry.getLevel();
        if (nLevel > nMaxIndex)
            setBackground(Color.WHITE);
        else
            setBackground(COLOR_LEVELS[nLevel]);
        //setBorder(lineBorder);
        this.setHorizontalAlignment(JLabel.LEFT);
        this.setIconTextGap(0);
        setText(entry.toString());
        setFont(new java.awt.Font("Tahoma", 0, 10));
        setIcon(null);
        //setIcon(entry.getImage());
        if(isSelected) {
            setForeground(Color.white);
            setBorder(raisedEtched);
        } else {
            setForeground(Color.black);
            setBorder(lineBorder);
        }
        return this;
    }
}
*/ 


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
        cboSwitchTabs = new javax.swing.JComboBox();
        lblSwitchTag = new javax.swing.JLabel();
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

        jTabsContainer.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabsContainer.setFont(new java.awt.Font("Tahoma", 0, 10));

        cboListDocuments.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        cboListDocuments.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblCurrentlyOpenDocuments.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblCurrentlyOpenDocuments.setText("Currently Open Documents");

        btnBringToFront.setFont(new java.awt.Font("DejaVu Sans", 0, 9));
        btnBringToFront.setText("To Front");
        btnBringToFront.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnBringToFront.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBringToFrontActionPerformed(evt);
            }
        });

        btnOpenDocument.setFont(new java.awt.Font("DejaVu Sans", 0, 9));
        btnOpenDocument.setText("Open");
        btnOpenDocument.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnOpenDocument.setIconTextGap(2);
        btnOpenDocument.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenDocumentActionPerformed(evt);
            }
        });

        lblCurrentMode.setForeground(java.awt.Color.red);
        lblCurrentMode.setText("CURRENT MODE : %s");

        cboSwitchTabs.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        cboSwitchTabs.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblSwitchTag.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblSwitchTag.setText("Switch Tabs :");

        btnNewDocument.setFont(new java.awt.Font("DejaVu Sans", 0, 9));
        btnNewDocument.setText("New");
        btnNewDocument.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnNewDocument.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewDocumentActionPerformed(evt);
            }
        });

        btnSaveDocument.setFont(new java.awt.Font("DejaVu Sans", 0, 9));
        btnSaveDocument.setText("Save");
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
        lblDisplaySectionName.setText("::");

        lblSecName.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblSecName.setText("Current Section Name :");

        btnEdit.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnEdit.setText("Edit ");
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
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(8, 8, 8)
                        .add(lblSwitchTag, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(cboSwitchTabs, 0, 132, Short.MAX_VALUE)
                        .addContainerGap())
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, lblCurrentMode, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                            .add(btnBringToFront)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(btnOpenDocument, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(btnNewDocument, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(btnSaveDocument, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
            .add(jTabsContainer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .add(lblSecName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 134, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .add(lblDisplaySectionName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 134, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 79, Short.MAX_VALUE)
                .add(btnEdit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
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
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblSwitchTag)
                    .add(cboSwitchTabs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jTabsContainer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 329, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
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
            MessageBox.OK(parentFrame, "Document Export failed " );
}//GEN-LAST:event_btnSaveDocumentActionPerformed

private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
// TODO add your handling code here:
   launchSectionMetadataEditor();
}//GEN-LAST:event_btnEditActionPerformed

   
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
                        if (currentDocType.equals("debaterecord")) {
                            LaunchDebateMetadataSetter(xComp);
                        }
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
   

private void LaunchDebateMetadataSetter(XComponent xComp){
        OOComponentHelper oohc = new OOComponentHelper (xComp, ComponentContext);
        String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
        BungeniFrame frm = new BungeniFrame(docType + " Metadata");
        IEditorDocMetadataDialog metaDlg = EditorDocMetadataDialogFactory.getInstance(BungeniEditorPropertiesHelper.getCurrentDocType());
        metaDlg.initVariables(oohc, frm, SelectorDialogModes.TEXT_EDIT);
        metaDlg.initialize();
       // DebateRecordMetadata meta = new DebateRecordMetadata(oohc, frm, SelectorDialogModes.TEXT_INSERTION);
        frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frm.setSize(new Dimension(410, 424));
        frm.add(metaDlg.getPanelComponent());
        frm.setVisible(true);
        frm.setAlwaysOnTop(true);
}

  
    
    
    private static boolean structureInitialized = false;
    private  void initTimers(){
        
      //  synchronized(this);
        try {
            //structure list & tree structure refresh timer
            /*
            Action DocStructureListRunner = new AbstractAction() {
                public void actionPerformed (ActionEvent e) {
                    if (!structureInitialized) {
                        //initSectionStructureTreeModel();
                        structureInitialized = true;
                    }
                    initList();
                }
            };
            docStructureTimer = new Timer(3000, DocStructureListRunner);
            docStructureTimer.setInitialDelay(2000);
            docStructureTimer.start(); */
            //section name timer
            /*
            sectionNameTimer = new Timer(1000, new CurrentSectionNameUpdater());
            sectionNameTimer.start();
            */
            //component handle tracker timer
      
            Action componentsTrackingRunner = new AbstractAction(){
                public void actionPerformed(ActionEvent e) {
                  componentHandlesTracker();
                  updateListDocuments();
                }
            };
            componentsTrackingTimer = new Timer(5000, componentsTrackingRunner);
            componentsTrackingTimer.start();

            
            //docStructureTimer = new java.util.Timer();
            //docStructureTimer.schedule(task, 0, 3000);
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
     
     private void updateListDocuments(){
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
              ArrayList<Integer> modelIndexesToRemove = new ArrayList<Integer>();
              //remove closed documents from model
              for (int i=0; i < model.getSize(); i++ ){
                  componentHandleContainer compMatch = (componentHandleContainer) model.getElementAt(i);
                  //check if model component handle exists in newly generate component map
                  //if it doesnt we delete the componenth handle from them model
                  if (!getCurrentlyOpenDocuments().containsKey(compMatch.componentKey())){
                      modelIndexesToRemove.add(i);
                  }
              }
              //finally remove all missingindexes
              for (Integer iRemove : modelIndexesToRemove) {
                  model.removeElementAt(iRemove);
              }
              //String selectedItem = (String)cboListDocuments.getSelectedItem();
          
                  /*setProgrammaticRefreshOfDocumentListFlag(true);
                  cboListDocuments.setModel(new DefaultComboBoxModel(componentHandles.toArray()));

                  if (!getCurrentlyOpenDocuments().containsValue(selectedItem))
                       cboListDocuments.setSelectedIndex(0);
                   else
                       cboListDocuments.setSelectedItem(selectedItem);
                  setProgrammaticRefreshOfDocumentListFlag(false);*/
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
    private javax.swing.JComboBox cboSwitchTabs;
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
    private javax.swing.JLabel lblSwitchTag;
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
        componentHandleContainer(String name, XComponent xComponent) {
            log.debug("componentHandleContainer: in constructor()");
            aName = name;
            aComponent = xComponent;
            log.debug("componentHandleContainer: to string = " + aComponent.toString());
            aComponent.addEventListener(compListener);
            componentKey = generateComponentKey();
            //add the event broadcaster to the same listener
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
    
    public static void main(String args[]) {
    JFrame frame = new JFrame("Oval Sample");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

   editorTabbedPanel panel = new editorTabbedPanel();
   frame.add(panel);
   frame.setSize(200,400);
   frame.setVisible(true);
  }   


}
