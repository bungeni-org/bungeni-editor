/*
 * holderUIPanel.java
 *
 * Created on July 1, 2008, 3:16 PM
 */

package org.bungeni.editor.panels;

import com.sun.star.beans.XPropertySet;
import com.sun.star.text.XTextSection;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.uno.Any;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.editor.actions.EditorActionFactory;
import org.bungeni.editor.actions.IEditorActionEvent;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.editor.dialogs.editorTabbedPanel;
import org.bungeni.editor.panels.impl.IFloatingPanel;
import org.bungeni.editor.providers.DocumentSectionAdapterDefaultTreeModel;
import org.bungeni.editor.providers.DocumentSectionFriendlyAdapterDefaultTreeModel;
import org.bungeni.editor.providers.DocumentSectionFriendlyTreeModelProvider;
import org.bungeni.editor.providers.DocumentSectionProvider;
import org.bungeni.editor.providers.DocumentSectionTreeModelProvider;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.editor.toolbar.BungeniToolbarTargetProcessor;
import org.bungeni.editor.toolbar.BungeniToolbarXMLAdapterNode;
import org.bungeni.editor.toolbar.BungeniToolbarXMLTreeNodeProcessor;
import org.bungeni.editor.toolbar.conditions.BungeniToolbarConditionProcessor;
import org.bungeni.extutils.CommonDocumentUtilFunctions;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.utils.BungeniBNode;
import org.bungeni.utils.BungeniBTree;
import org.bungeni.extutils.CommonTreeFunctions;
import org.bungeni.utils.compare.BungeniTreeRefactorTree;
import org.jdom.Attribute;


/**
 * This is the floating panel implementation for the Editor's action bar
 * @author  Ashok Hariharan
 */
public class holderUIPanel extends javax.swing.JPanel implements IFloatingPanel {
    private OOComponentHelper ooDocument;
    
   
    private JFrame parentFrame;
    
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(holderUIPanel.class.getName());
    
    private org.bungeni.editor.toolbar.BungeniToolbarXMLTree toolbarXmlTree ;    
    private BungeniClientDB instance;
    private Timer timerSectionTree;
    
    private changeStructureItem[] m_comboChangeStructureItems = {
 //      new changeStructureItem ("VIEW_PARAGRAPHS", "View Paragraphs"),
       new changeStructureItem ("VIEW_SECTIONS", "View Sections"),
       new changeStructureItem ("VIEW_PRETTY_SECTIONS", "View Structure")
    };
    
    private changeStructureItem m_selectedChangeStructureItem = null;
    
    private JTree sectionInternalStructureTree;
    private String m_currentSelectedSectionName ;

    /** Creates new form holderUIPanel */
    public holderUIPanel() {
        initComponents();
        instance = new BungeniClientDB (DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
       //the below are called from initUI()
        // initToolbarTree();
       // initSectionStructureTree();
    }

    /**
     * Public APIs from interface
     */

    /**
     * Set openoffice document handle
     * @param ooComponent
     */
    public synchronized void setOOComponentHandle(OOComponentHelper ooComponent) {
            String currentHandleKey = "", newHandleKey = "";
            if (ooDocument != null ) {
                currentHandleKey = editorTabbedPanel.componentHandleContainer.generateComponentKey(ooDocument.getDocumentTitle(), ooDocument.getComponent());
                newHandleKey = editorTabbedPanel.componentHandleContainer.generateComponentKey(ooComponent.getDocumentTitle(), ooComponent.getComponent());
                //update only when the incoming handle changes....
                if (!currentHandleKey.equalsIgnoreCase(newHandleKey)) {
                    log.debug("holderUIPanel, setOOComponentHandle : updating component handle" );
                    this.ooDocument = ooComponent;
                }
                    //updatePanelonComponentChange();
            } else {
                log.debug("holderUIPanel, setOOComponentHandle : ooDocument was null updating component handle" );
                this.ooDocument = ooComponent;
            }
                log.debug("setOOComponenthHandle: starting timer");
    }

    private synchronized OOComponentHelper getOODocument(){
        return this.ooDocument;
    }

    public Component getObjectHandle() {
        return this;
    }

    public IEditorActionEvent getEventClass(toolbarSubAction subAction) {
       IEditorActionEvent event = EditorActionFactory.getEventClass(subAction);
        return event;
    }

    public IEditorActionEvent getEventClass(toolbarAction action) {
       IEditorActionEvent event = EditorActionFactory.getEventClass(action);
        return event;
    }

    public void setParentWindowHandle(JFrame c) {
        this.parentFrame = c;
    }

    public JFrame getParentWindowHandle() {
        return this.parentFrame;
    }

    /**
     *
     */
    public void initUI() {
        this.initToolbarTree();
        this.initSectionStructureTree();
        this.initSectionInternalStructureTree();
        this.initButtonListeners();
        this.initMouseListener();
        this.initComboChangeStructure();
        this.initTimers();
        this.initUIAttributes();
    }


    private void initUIAttributes(){
        //set scroolbar widths
        Dimension dimScrollbarVer = new Dimension(10,0);
        Color bgColor = new Color(0xffffe5);
        this.scrollToolbarTree.getVerticalScrollBar().setPreferredSize(dimScrollbarVer);
        this.scrollTreeView.getVerticalScrollBar().setPreferredSize(dimScrollbarVer);
        this.scrollToolbarTree.getVerticalScrollBar().setBackground(bgColor);
        this.scrollTreeView.getVerticalScrollBar().setBackground(bgColor);


        Dimension dimScrollbarHor = new Dimension(0,10);
        this.scrollToolbarTree.getHorizontalScrollBar().setPreferredSize(dimScrollbarHor);
        this.scrollTreeView.getHorizontalScrollBar().setPreferredSize(dimScrollbarHor);
        this.scrollToolbarTree.getHorizontalScrollBar().setBackground(bgColor);
        this.scrollTreeView.getHorizontalScrollBar().setBackground(bgColor);

    }

    private void initToolbarTree(){
        try{
        toolbarXmlTree = new org.bungeni.editor.toolbar.BungeniToolbarXMLTree(toolbarTree);
        toolbarXmlTree.loadToolbar();
        toolbarTree.addMouseListener(new toolbarTreeMouseListener());
        toolbarTree.setCellRenderer(new toolbarTreeCellRenderer());
         ComponentUI treeui = toolbarTree.getUI();
         if (treeui instanceof BasicTreeUI){
             ((BasicTreeUI)treeui).setExpandedIcon(CommonTreeFunctions.treeMinusIcon());
             ((BasicTreeUI)treeui).setCollapsedIcon(CommonTreeFunctions.treePlusIcon());
         }        
        //-tree-deprecated--CommonTreeFunctions.expandAll(toolbarTree, true);
       // toolbarTree.addTreeWillExpandListener(new toolbarTreeWillExpandListener());
        CommonTreeFunctions.expandAll(toolbarTree);
        //using a timer to repaint the tree causes very poor tab switching performnce in some cases
        //investigate using fireTreeNodesChanged
        javax.swing.Timer toolbarTreePaintTimer = new javax.swing.Timer(3000, new toolbarTreePaintTimerListener(toolbarTree));
        toolbarTreePaintTimer.start();
        //for note above about firenodeschanged, this has been implemented but works for model refresh ==> tree update.
        //but does not reflect in case of document cursor changes ==> reflecting back to the tree, as this requires a full iteration 
        //of the tree again. so for now implemented both as a treeTimer and fireNodeschanged event refresh
        ToolTipManager.sharedInstance().registerComponent(toolbarTree);
        this.addKeyListener(new KeyAdapter(){

                @Override
                public void keyTyped(KeyEvent e) {
                    e.consume();
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    System.out.println("the key " + e.getKeyChar() + " was pressed");
                    if (e.getKeyChar() == '*') {
                        System.out.println("* was pressed");
                        TreePath selectionPath = toolbarTree.getSelectionPath();
                        if (selectionPath != null) {
                                CommonTreeFunctions.expandAll(toolbarTree, selectionPath);
                        }
                    }
                }

        });
        } catch (Exception ex) {
            log.error("initTree: "+ ex.getMessage());
        }
    }

    private void initSectionStructureTree(){
         sectionStructureTree.setExpandsSelectedPaths(true);
         ImageIcon minusIcon = CommonTreeFunctions.treeMinusIcon();
         ImageIcon plusIcon = CommonTreeFunctions.treePlusIcon();
         sectionStructureTree.setCellRenderer(new treeViewPrettySectionsTreeCellRenderer());
         sectionStructureTree.addMouseListener(new MouseAdapter(){
            @Override
                 public void mousePressed(MouseEvent evt) {
                    if (evt.getClickCount() == 2)  {
                        TreePath selPath = sectionStructureTree.getPathForLocation(evt.getX(), evt.getY());
                        if (selPath != null ) {
                            Object node = selPath.getLastPathComponent();
                            DefaultMutableTreeNode dmt = (DefaultMutableTreeNode) node;
                            Object userObject = dmt.getUserObject();
                            if (userObject.getClass().getName().equals(BungeniBNode.class.getName())) {
                                BungeniBNode bNode = (BungeniBNode) userObject;
                                String sectionName = bNode.getName();
                                if (ooDocument.hasSection(sectionName)) {
                                    CommonDocumentUtilFunctions.selectSection(ooDocument, sectionName);
                                }
                            }
                        }
                    }
                 }
         });
         sectionStructureTree.setShowsRootHandles(true);
         ComponentUI treeui = sectionStructureTree.getUI();
         if (treeui instanceof BasicTreeUI){
             ((BasicTreeUI)treeui).setExpandedIcon(minusIcon);
             ((BasicTreeUI)treeui).setCollapsedIcon(plusIcon);
         }
        initSectionStructureTreeModel();     

    }

    private void initSectionStructureTreeModel(){
        try {
        DocumentSectionFriendlyAdapterDefaultTreeModel model = DocumentSectionFriendlyTreeModelProvider.create() ;//_without_subscription();
        //change later
         //  DocumentSectionAdapterDefaultTreeModel model = DocumentSectionTreeModelProvider.create();//_without_subscription();
            this.sectionStructureTree.setModel(model);
        CommonTreeFunctions.expandAll(sectionStructureTree);
        } catch (Exception ex) {
            log.error ("initSectionStructureTreeModel : " + ex.getMessage());
            log.error ("initSectionStructureTreeModel : " + CommonExceptionUtils.getStackTrace(ex));
        }
    }
    
    private void initSectionInternalStructureTree(){
        this.sectionInternalStructureTree = new JTree();
        sectionInternalStructureTree.setExpandsSelectedPaths(true);
         ImageIcon minusIcon = CommonTreeFunctions.treeMinusIcon();
         ImageIcon plusIcon = CommonTreeFunctions.treePlusIcon();
         //sectionInternalStructureTree.setCellRenderer(new treeViewPrettySectionsTreeCellRenderer());
         sectionInternalStructureTree.setShowsRootHandles(true);
         ComponentUI treeui = sectionInternalStructureTree.getUI();
         if (treeui instanceof BasicTreeUI){
             ((BasicTreeUI)treeui).setExpandedIcon(minusIcon);
             ((BasicTreeUI)treeui).setCollapsedIcon(plusIcon);
         }
         initSectionInternalStructureTreeModel();     
    }
    
    private void initSectionInternalStructureTreeModel(){
         try {
        DocumentSectionAdapterDefaultTreeModel model = DocumentSectionTreeModelProvider.create();//_without_subscription();
        this.sectionInternalStructureTree.setModel(model);
        CommonTreeFunctions.expandAll(sectionInternalStructureTree);
        } catch (Exception ex) {
            log.error ("initSectionStructureTreeModel : " + ex.getMessage());
            log.error ("initSectionStructureTreeModel : " + CommonExceptionUtils.getStackTrace(ex));
        }
    }
    
    private void initTimers(){
         Action sectionViewRefreshRunner = new AbstractAction() {
                public void actionPerformed (ActionEvent e) {
                        updateCurrentSectionName();
                        //***** crash warning *****
                        //commented updateSectionTree 13-11-2008 to prevent crash on timed refresh... 
                        //updateSectionTree();
                        //***** crash warning *****
                    };
       };
        timerSectionTree = new Timer(3000, sectionViewRefreshRunner);
        timerSectionTree.setInitialDelay(2000);
        timerSectionTree.start();
    }
    private holderUIPanel self(){
        return this;
    }
    
    private void initButtonListeners() {
        btnViewDefault.setSelected(true);
        //add action listeners to button
        btnViewDefault.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                JRadioButton btn = (JRadioButton) arg0.getSource();
                if (btn.isSelected()){
                    scrollToolbarTree.setVisible(true);
                    scrollTreeView.setVisible(true);
                    self().parentFrame.setVisible(true);
                    self().parentFrame.setExtendedState(JFrame.NORMAL);
                }
            }
        });
        btnHideToolbarTree.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                JRadioButton btn = (JRadioButton) arg0.getSource();
                if (btn.isSelected()){
                    scrollToolbarTree.setVisible(false);
                    scrollTreeView.setVisible(true);
                    //revalidate does 're-layout' of the panel
                    self().revalidate();
                }
            }
        });
        btnHideTreeView.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                JRadioButton btn = (JRadioButton) arg0.getSource();
                if (btn.isSelected()){
                    scrollToolbarTree.setVisible(true);
                    scrollTreeView.setVisible(false);
                    //revalidate does 're-layout' of the panel
                    self().revalidate();
                }
            }
        });
    }
       
    
    private void initMouseListener(){
        //first move focus to frame on mouse over
        parentFrame.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent arg0) {
                log.debug("mouseListener : entered holderUIPanel" );
                parentFrame.requestFocus();
            }
        });
        //after moving focus to frame, shift focus to the toolbar tree
        parentFrame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                toolbarTree.requestFocusInWindow();
            }
        });

    }
 
    private void initComboChangeStructure(){
        changeStructureItem itemDefault = null;
        cboChangeStructure.removeAllItems();
        String defaultHierarchyView = BungeniEditorProperties.getEditorProperty("defaultHierarchyView");
        for (int i=0; i < this.m_comboChangeStructureItems.length; i++) {
            if (m_comboChangeStructureItems[i].getIndex().equalsIgnoreCase(defaultHierarchyView)){
                itemDefault = m_comboChangeStructureItems[i];
            }
            cboChangeStructure.addItem(m_comboChangeStructureItems[i]);    
        }
        
        //add a change listener to the combobox...
        cboChangeStructure.addActionListener (new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
             JComboBox box = (JComboBox) arg0.getSource();
             changeStructureItem theItem = (changeStructureItem) box.getSelectedItem();
             m_selectedChangeStructureItem = theItem;
             updateViewPortForTree();
             updateSectionTree();
            }
        });

        if (itemDefault != null)
            cboChangeStructure.setSelectedItem(itemDefault);
        m_selectedChangeStructureItem = (changeStructureItem)cboChangeStructure.getSelectedItem();        
    }
    
    private void updateCurrentSectionName(){
       String sectionHier = currentSectionName();
       this.txtCurrentSectionName.setText(sectionHier);
    }
    
        public String getSectionHierarchy(XTextSection thisSection) {
                String sectionName = "";
                sectionName = ooQueryInterface.XNamed(thisSection).getName();
                if (thisSection.getParentSection() != null) {
                    sectionName = getSectionHierarchy(thisSection.getParentSection()) + ">" + sectionName;
                } else
                    return sectionName;
                return sectionName;    
            }
       
        
        
        public String currentSectionName() {
            XTextSection loXTextSection;
            XTextViewCursor loXTextCursor;
            XPropertySet loXPropertySet;
            String lstrSectionName = "";
             try
             {
                if (ooDocument.isXComponentValid() ) {
                    loXTextCursor = getOODocument().getViewCursor();
                    loXPropertySet = ooQueryInterface.XPropertySet(loXTextCursor);
                    loXTextSection = (XTextSection)((Any)loXPropertySet.getPropertyValue("TextSection")).getObject();
                    if (loXTextSection != null)
                    {
                        //loXPropertySet = ooQueryInterface.XPropertySet(loXTextSection);
                        //XNamed objSectProps = ooQueryInterface.XNamed(loXTextSection);
                        //lstrSectionName =  objSectProps.getName(); // (String)loXPropertySet.getPropertyValue("LinkDisplayName");
                        m_currentSelectedSectionName  = ooQueryInterface.XNamed(loXTextSection).getName();
                        lstrSectionName = getSectionHierarchy(loXTextSection);
                    } else
                        m_currentSelectedSectionName = "";
                }
              }
              catch (java.lang.Exception poException)
                {
                    log.error("currentSectionName:" + poException.getLocalizedMessage());
                }
              finally {  
                 return lstrSectionName; 
              }
           }
    
    private void updatePanelonComponentChange(){
        //refresh the tree model
        this.timerSectionTree.stop();
       // refreshTreeModel();
        this.timerSectionTree.start();
    }

    private void updateViewPortForTree(){
        SwingUtilities.invokeLater(new Runnable(){

            public void run() {
                if (m_selectedChangeStructureItem.itemIndex.equals("VIEW_SECTIONS")) {
                    scrollTreeView.setViewportView(sectionInternalStructureTree);
                } else if (m_selectedChangeStructureItem.itemIndex.equals("VIEW_PRETTY_SECTIONS")) {
                    scrollTreeView.setViewportView(sectionStructureTree);
                }
            }
            
        });

    }
    
    private void updateSectionTree() {
        try {
        log.debug("updateSectionTree : "+ m_selectedChangeStructureItem);
        if (this.m_selectedChangeStructureItem.itemIndex.equals("VIEW_SECTIONS")) {
            //do this only if the tree is visible 
            if (sectionInternalStructureTree.isShowing()) {
                 log.debug("updateSectionTree : refreshing section_internal_structure tree");
                 BungeniBTree newTree = DocumentSectionProvider.getNewTree();
                 BungeniBNode newRootNode = newTree.getFirstRoot();
                 DocumentSectionAdapterDefaultTreeModel model = (DocumentSectionAdapterDefaultTreeModel) this.sectionInternalStructureTree.getModel();
                 DefaultMutableTreeNode mnode = (DefaultMutableTreeNode) model.getRoot();
                 BungeniBNode origNode = (BungeniBNode) mnode.getUserObject();
                 BungeniTreeRefactorTree refTree = new BungeniTreeRefactorTree (model, origNode, newRootNode);
                 refTree.setMergeDisplayText(false);
                 refTree.doMerge();
            } else {
                 log.debug("updateSectionTree : section internal structure tree is not visible");
            }
                
        } else if (this.m_selectedChangeStructureItem.itemIndex.equals("VIEW_PRETTY_SECTIONS")) {
            if (sectionStructureTree.isShowing()) {
                 log.debug("updateSectionTree : refreshing section friendly structur tree");
            //     NodeDisplayTextSetter nsetter = new NodeDisplayTextSetter(getOODocument());
            //     BungeniBNode.setINodeSetterCallback(nsetter);

                 BungeniBTree newTree = DocumentSectionProvider.getNewFriendlyTree();//FriendlyTree();
                 BungeniBNode newRootNode = newTree.getFirstRoot();

                 DocumentSectionFriendlyAdapterDefaultTreeModel model = (DocumentSectionFriendlyAdapterDefaultTreeModel) this.sectionStructureTree.getModel();
                 //DocumentSectionAdapterDefaultTreeModel model = (DocumentSectionAdapterDefaultTreeModel) this.sectionStructureTree.getModel();
                 DefaultMutableTreeNode mnode = (DefaultMutableTreeNode) model.getRoot();
                 BungeniBNode origNode = (BungeniBNode) mnode.getUserObject();
                 BungeniTreeRefactorTree refTree = new BungeniTreeRefactorTree (model, origNode, newRootNode);
                 refTree.setMergeDisplayText(false);
                 refTree.doMerge();
            } else {
                 log.debug("updateSectionTree : section friendly structure tree is not visible");
            }
        }
        } catch (Exception ex){
            log.error("exception updateSectionTree : " + ex.getMessage());
        }
    }

    /**
     *
     */
    class treeViewPrettySectionsTreeCellRenderer extends DefaultTreeCellRenderer {
        Color bgColor = new java.awt.Color(232, 255, 175);
        Color bgColorSelect = new java.awt.Color(207, 242, 255);
        treeViewPrettySectionsTreeCellRenderer(){
           // setOpaque(true);
        }
        
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
           Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
           setFont (TREE_LABEL_FONT);
           setText(value.toString());
            if (value instanceof DefaultMutableTreeNode) {
                  DefaultMutableTreeNode uo = (DefaultMutableTreeNode)value;
                  Object uoObj = uo.getUserObject();
                  if (uoObj.getClass() == org.bungeni.utils.BungeniBNode.class) {
                      BungeniBNode aNode = (BungeniBNode) uoObj;
                      if (aNode.getName().equals(m_currentSelectedSectionName)) {
                          //setBorder(selBorder);
                          c.setBackground(bgColor);
                      } else if (selected) {
                          c.setBackground(bgColorSelect);
                      }  else {
                          c.setBackground(null);
                      }
                  }
            }
            return c;
        }
        
    }
    
    class toolbarTreeMouseListener implements MouseListener {
      
        public void mouseClicked(MouseEvent evt) {
      
        }

       public void mousePressed(MouseEvent evt) {
            //get treepath for currennt mouse click
            TreePath selPath = toolbarTree.getPathForLocation(evt.getX(), evt.getY());
            if (selPath != null ) {
                Object node = selPath.getLastPathComponent();
                if (node == null ) return;
                if (node.getClass() == org.bungeni.editor.toolbar.BungeniToolbarXMLAdapterNode.class ) {
                    BungeniToolbarXMLAdapterNode toolbarXmlNode = (BungeniToolbarXMLAdapterNode) node;
                    if (toolbarXmlNode.childCount() == 0 && evt.getClickCount() == 2) {
                        processBungeniToolbarXmlAdapterNode(toolbarXmlNode); 
                    }
                }  
            }
       }
       private void processAction(toolbarAction action) {
           log.debug("processAction:" + action.action_name() );

           if (action.isTopLevelAction()) {
               log.info("toolbar: processAction: not processing topLevelAction type");
               return;
           }
          IEditorActionEvent event = getEventClass(action);
          event.doCommand(getOODocument(), action, parentFrame);
       }
       
       private void processSubAction(toolbarSubAction action) {
           log.debug("processSubAction:" + action.sub_action_name() );
                   
              IEditorActionEvent event = getEventClass(action);
              event.doCommand(getOODocument(), action, parentFrame);
       }
       
       private void processBungeniToolbarXmlAdapterNode (BungeniToolbarXMLAdapterNode adapterNode) {
              try {
              BungeniToolbarXMLTreeNodeProcessor nodeProc = new BungeniToolbarXMLTreeNodeProcessor(adapterNode);
              Object nodeUserObject = adapterNode.getUserObject();
                   //check if node is enabled or disabled
                   if (nodeUserObject != null) {
                       if (nodeUserObject.getClass() == treeGeneralEditorNodeState.class) {
                           treeGeneralEditorNodeState thestate = (treeGeneralEditorNodeState)nodeUserObject;
                           //if disabled, dont process, just return
                           if (thestate == treeGeneralEditorNodeState.DISABLED) {
                               log.debug("treeGeneralEditor, mousclick, node state was disabled ");
                             return;  
                           } 
                       }
                   }
                   //blank target, so nothing to process, return
                   if (nodeProc.getTarget() == null ){
                       log.debug("treeGeneralEditor, mousclick, target was null");
                       return;
                   } 
                   //if node is not visible, nothing to process, return
                   if (nodeProc.getVisible() == null ) {
                       return;
                   }
                   //based on the target information we need to create the action objectr
                   
                   String strTarget = nodeProc.getTarget();
                   log.info("processBungeniToolbarXmlAdapterNode  target = " + strTarget);
                   BungeniToolbarTargetProcessor targetObj = new BungeniToolbarTargetProcessor(strTarget);
                   SelectorDialogModes selectedMode = SelectorDialogModes.valueOf(nodeProc.getMode());
                   toolbarAction tbAction = null;
                   toolbarSubAction tbSubAction = null;
                   switch (targetObj.target_type) {
                       case ACTION:
                          tbAction =  processInsertion(targetObj);
                          tbAction.setSelectorDialogMode(selectedMode);
                          processAction (tbAction);
                          break;
                       case SUB_ACTION:
                          tbSubAction =  processSelection(targetObj); 
                          tbSubAction.setSelectorDialogMode(selectedMode);
                          processSubAction(tbSubAction);
                          break;
                   } 
                } catch (Exception ex) {
                    log.error("processBungeniToolbarXmlAdapterNode:"+ ex.getMessage());
                    log.error("processBungeniToolbarXmlAdapterNode:"+ CommonExceptionUtils.getStackTrace(ex));
                }
           
       }
       

       private toolbarSubAction processSelection(BungeniToolbarTargetProcessor targetObj) {
         
            String documentType = BungeniEditorProperties.getEditorProperty("activeDocumentMode");
            instance.Connect();
            String actionQuery = SettingsQueryFactory.Q_FETCH_SUB_ACTIONS(documentType, targetObj.actionName, targetObj.subActionName);
            log.info("processSelection: "+ actionQuery); 
            QueryResults qr = instance.QueryResults(actionQuery);
             instance.EndConnect();
             if (qr == null ) {
                 log.info("processSelection : queryResults :" + actionQuery + " were null, metadata incorrectly setup");
                 return null;
             }
             if (qr.hasResults()) {
                //this should return only a single toolbarSubAction
                 toolbarSubAction subActionObj =  new toolbarSubAction(qr.theResults().elementAt(0), qr.columnNameMap());
                 subActionObj.setActionValue(targetObj.actionValue);
                 return subActionObj;
             } else {
                  log.info("processSelection : queryResults :" + actionQuery + " were null, metadata incorrectly setup");
                 return null;
             }
       }
       private toolbarAction processInsertion(BungeniToolbarTargetProcessor targetAction) {
          // BungeniToolbarTargetProcessor targetObject = new BungeniToolbarTargetProcessor()
            String documentType = BungeniEditorProperties.getEditorProperty("activeDocumentMode");

           
           instance.Connect();
           String actionQuery = SettingsQueryFactory.Q_FETCH_ACTION_BY_NAME(documentType, targetAction.actionName);
           QueryResults qr = instance.QueryResults(SettingsQueryFactory.Q_FETCH_ACTION_BY_NAME(documentType, targetAction.actionName));
             instance.EndConnect();
             if (qr == null ) {
                 log.info("toolbar: processInsertion: the metadata has been setup incorrectly for action :" + targetAction.actionName);
                 return null;
             }
             if (qr.hasResults()) {
                 return new toolbarAction(qr.theResults().elementAt(0), qr.columnNameMap());
             } else {
                 log.info("toolbar: processInsertion: the metadata has been setup incorrectly for action :" + targetAction.actionName);
                 return null;
             }
       }
       
     
       
        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

      
        
    }
    
    class toolbarTreePaintTimerListener implements ActionListener{
        private JTree timedTree;
        
        public toolbarTreePaintTimerListener(JTree timedTree){
            this.timedTree = timedTree;
        }
        
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable(){
                public void run() {
                if (timedTree.isShowing()) {
                    timedTree.repaint();
                }
                }
            });
        }

 
        
    }
        
    /*Static declarations used by toolbar classes */
    static HashMap<String, toolbarIcon> toolbarIconMap = new HashMap<String,toolbarIcon>();
    static int BLOCK_ICON = 0, METADATA_ICON = 1, ACTION_ICON = 2;
    static String[] icons = { "block", "block_action", "metadata", "null"/* "action" */, "subaction"};
       
    class toolbarIcon {
            public String iconName;
            public ImageIcon enabledIcon;
            public ImageIcon disabledIcon;
            public toolbarIcon(String icon, String pathPrefix) {
                    if (icon.equals("null")) {
                     enabledIcon = null;
                     disabledIcon = null;
                    } else {
                   String imgPathEnabled = pathPrefix + icon + "_enabled.png" ;
                   String imgPathDisabled = pathPrefix + icon +"_disabled.png" ;
                   enabledIcon = new ImageIcon(getClass().getResource(imgPathEnabled), "");
                   disabledIcon = new ImageIcon(getClass().getResource(imgPathDisabled), "");
                    }
            }
    
    }

    private static final Font TREE_LABEL_FONT = new Font("Tahoma", Font.PLAIN, 11);

    enum treeGeneralEditorNodeState {ENABLED, DISABLED};
    /**
     * This is the TreeCellRenderer implementation for the Editor Action toolbar
     */
    HashMap<String, BungeniToolbarConditionProcessor> conditionMap = new HashMap<String, BungeniToolbarConditionProcessor>();
    class toolbarTreeCellRenderer extends DefaultTreeCellRenderer /*JLabel implements TreeCellRenderer */ {

         int SECTION_ICON = 0;
         int SECTION_PLUS_ICON = 1;
         int MARKUP_ICON = 2;
         int TOPLEVEL_ICON=3;
         int DISABLED_ICON=4;
         Font labelFont = TREE_LABEL_FONT;
         Color nodeEnabledColor = null;
         Color nodeDisabledColor = null;
         Color nodeNoTargetColor = null;
         public toolbarTreeCellRenderer() {
             if (toolbarIconMap.size() == 0 ) {
                for (int i=0; i < icons.length; i++ ) {
                    toolbarIconMap.put(icons[i], new toolbarIcon(icons[i], "/gui/"));
                }
            }
            this.nodeEnabledColor = Color.decode(BungeniEditorProperties.getEditorProperty("toolbarNodeEnabledColor"));
            this.nodeDisabledColor = Color.decode(BungeniEditorProperties.getEditorProperty("toolbarNodeDisabledColor"));
            this.nodeNoTargetColor = Color.decode(BungeniEditorProperties.getEditorProperty("toolbarNodeNoTargetColor"));
         }

         private int ACTION_OBJECT=0, SELECTION_OBJECT=1;
        
        private toolbarIcon getIcon(String elementName ) {
            if (elementName.equals("root")) {
                return toolbarIconMap.get("block");
            } else if (elementName.equals("actionGroup")) {
                return toolbarIconMap.get("block");
            } else if (elementName.equals("blockAction")) {
                return toolbarIconMap.get("block_action");
            } else if (elementName.equals("action")) {
                return toolbarIconMap.get("null");
            } else if (elementName.equals("subaction")) {
                return toolbarIconMap.get("subaction");
            } else
                return toolbarIconMap.get("block");
        }
        
        
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component c = super.getTreeCellRendererComponent(tree, value, leaf, expanded, leaf, row, hasFocus);
                int objectType = -1;
                toolbarIcon currentIcon;
                Object userObj;
                if (getOODocument() == null) {
                    return this;
                }
                try {
                   //if (selected)  {
                   //   setBackground(Color.YELLOW);
                   //}  else {
                   //   setOpaque(false);
                  // }
                
                    //for selection object the node user object is always a string
                    //if (userObj.getClass() == java.lang.String.class) 
                    //     objectType = this.SELECTION_OBJECT;
                    if (value.getClass() == org.bungeni.editor.toolbar.BungeniToolbarXMLAdapterNode.class) {
                        BungeniToolbarXMLAdapterNode toolbarNode = (BungeniToolbarXMLAdapterNode)value;
                        BungeniToolbarXMLTreeNodeProcessor nodeProc = new BungeniToolbarXMLTreeNodeProcessor(toolbarNode);
                        toolbarIcon theIcon = getIcon(toolbarNode.node.getName());
                        org.jdom.Attribute visibleAttrib = toolbarNode.node.getAttribute("visible");
                        org.jdom.Attribute conditionAttrib = toolbarNode.node.getAttribute("condition");
                        org.jdom.Attribute nameAttrib = toolbarNode.node.getAttribute("name");
                        //org.jdom.Attribute targetAttrib = toolbarNode.node.getAttribute("target");

                        setFont(labelFont);
                        //if visible = false - disable the action
                        //if visible = true  - check if condition is required
                        //if condition = none - enable the action unconditionallhy
                        //if condition = something - check if the condition is valid
                        //enable the aciton only when the condition is valid
                        if (visibleAttrib == null) {
                           nodeEnabled(c, theIcon, nodeProc);
                        } else
                        if (visibleAttrib.getValue().equals("false")) {
                            nodeDisabled(c, theIcon, nodeProc);
                        } else
                        if (visibleAttrib.getValue().equals("true")) {
                            if (conditionAttrib == null ) {
                                //no condition act as if true
                                nodeEnabled(c, theIcon, nodeProc);
                            } else if (conditionAttrib.getValue().equals("none")) {
                                //no condition act as if true
                                nodeEnabled(c, theIcon, nodeProc);
                            } else if (conditionAttrib.getValue().length()> 0) {
                                //other condition always evaluates to whether action should be enabeld or disabled
                                boolean bCondition =  processActionCondition(conditionAttrib);
                               if (bCondition) {
                                   nodeEnabled(c, theIcon, nodeProc);
                               } else {
                                   nodeDisabled(c, theIcon, nodeProc);
                               }
                            }
                        }                    
                    
                    }
                
                } catch (Exception ex) {
                    log.error("cellRender error: " + ex.getMessage());
                    log.error("cellRender stackTrace: "+ org.bungeni.ooo.utils.CommonExceptionUtils.getStackTrace(ex));
                } finally {
                return c;
                }
        }

        void nodeEnabled(Component c, toolbarIcon theIcon, BungeniToolbarXMLTreeNodeProcessor nodeProc) {
            nodeProc.getAdapterNode().setUserObject(treeGeneralEditorNodeState.ENABLED);
            String ttText = nodeProc.getToolTip();
            if (ttText != null) {
               setToolTipText(ttText.replace('\n','-'));
            }
            Attribute targetAttrib = nodeProc.getAdapterNode().node.getAttribute("target");
            if (targetAttrib == null ) {
                c.setForeground(this.nodeNoTargetColor);
            } else if (targetAttrib.getValue().trim().equals("null")) {
                c.setForeground(this.nodeNoTargetColor);
            } else {
                c.setForeground(nodeEnabledColor);
            }
            setText(nodeProc.getTitle());
        }
        
        
        void nodeDisabled(Component c, toolbarIcon theIcon, BungeniToolbarXMLTreeNodeProcessor nodeProc) {
            nodeProc.getAdapterNode().setUserObject(treeGeneralEditorNodeState.DISABLED);
            String ttText = nodeProc.getToolTip();
            if (ttText != null) {
                setToolTipText(ttText.replace('\n','-'));
            }
            c.setForeground(nodeDisabledColor);
            //setIcon(theIcon.disabledIcon);
            setText(nodeProc.getTitle());
                //this.repaint();
            //tree.repaint();
        }
       
         boolean processActionCondition(org.jdom.Attribute conditionAttrib) {
            boolean bAction = true;

            String conditionValue =  conditionAttrib.getValue().trim();
            if (!conditionMap.containsKey(conditionValue)) {
                //already has conditionprocessor object, get cached object and reset...
               //disabled conditionMap.get(conditionValue).setOOComponentHandle(getOODocument());
            //} else {
                BungeniToolbarConditionProcessor condProc = new BungeniToolbarConditionProcessor(conditionValue);
                conditionMap.put(conditionValue, condProc);
            }
             bAction = conditionMap.get(conditionValue).evaluate(getOODocument());

            return bAction;
        }
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
        
       
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupSwitchView = new javax.swing.ButtonGroup();
        scrollToolbarTree = new javax.swing.JScrollPane();
        toolbarTree = new javax.swing.JTree();
        scrollTreeView = new javax.swing.JScrollPane();
        sectionStructureTree = new javax.swing.JTree();
        cboChangeStructure = new javax.swing.JComboBox();
        btnHideToolbarTree = new javax.swing.JRadioButton();
        btnHideTreeView = new javax.swing.JRadioButton();
        btnViewDefault = new javax.swing.JRadioButton();
        txtCurrentSectionName = new javax.swing.JTextField();
        btnRefresh = new javax.swing.JButton();

        scrollToolbarTree.setViewportView(toolbarTree);

        scrollTreeView.setViewportView(sectionStructureTree);

        cboChangeStructure.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        cboChangeStructure.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnGroupSwitchView.add(btnHideToolbarTree);
        btnHideToolbarTree.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/panels/Bundle"); // NOI18N
        btnHideToolbarTree.setText(bundle.getString("holderUIPanel.btnHideToolbarTree.text")); // NOI18N

        btnGroupSwitchView.add(btnHideTreeView);
        btnHideTreeView.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnHideTreeView.setText(bundle.getString("holderUIPanel.btnHideTreeView.text")); // NOI18N

        btnGroupSwitchView.add(btnViewDefault);
        btnViewDefault.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnViewDefault.setText(bundle.getString("holderUIPanel.btnViewDefault.text")); // NOI18N

        txtCurrentSectionName.setEditable(false);

        btnRefresh.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnRefresh.setText(bundle.getString("holderUIPanel.btnRefresh.text")); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cboChangeStructure, 0, 221, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnHideToolbarTree)
                .addContainerGap(97, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnViewDefault)
                .addContainerGap(77, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnHideTreeView)
                .addContainerGap(70, Short.MAX_VALUE))
            .addComponent(scrollToolbarTree, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
            .addComponent(scrollTreeView, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtCurrentSectionName, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(scrollToolbarTree, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollTreeView, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                .addGap(2, 2, 2)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCurrentSectionName, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(cboChangeStructure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHideToolbarTree)
                .addGap(5, 5, 5)
                .addComponent(btnHideTreeView)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnViewDefault))
        );
    }// </editor-fold>//GEN-END:initComponents

private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
// TODO add your handling code here:
    this.updateSectionTree();
}//GEN-LAST:event_btnRefreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGroupSwitchView;
    private javax.swing.JRadioButton btnHideToolbarTree;
    private javax.swing.JRadioButton btnHideTreeView;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JRadioButton btnViewDefault;
    private javax.swing.JComboBox cboChangeStructure;
    private javax.swing.JScrollPane scrollToolbarTree;
    private javax.swing.JScrollPane scrollTreeView;
    private javax.swing.JTree sectionStructureTree;
    private javax.swing.JTree toolbarTree;
    private javax.swing.JTextField txtCurrentSectionName;
    // End of variables declaration//GEN-END:variables

    public static void main (String[] args){
             javax.swing.JFrame floatingFrame = new javax.swing.JFrame();
             holderUIPanel floatingPanel = new holderUIPanel();
             floatingFrame.add(floatingPanel);
             floatingFrame.setSize(221, 551);
             floatingFrame.setAlwaysOnTop(true);
             floatingFrame.setVisible(true);
            //position frame
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension windowSize = floatingFrame.getSize();
            
            int windowX = screenSize.width - floatingFrame.getWidth() - 2;
            int windowY = (screenSize.height - floatingFrame.getHeight())/2;
            floatingFrame.setLocation(windowX, windowY);  // Don't use "f." inside constructor.
            floatingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

}
