package org.bungeni.editor.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
import org.bungeni.editor.panels.toolbar.BungeniToolbarActionElement;
import org.bungeni.editor.panels.toolbar.BungeniToolbarLoader;
import org.bungeni.editor.panels.toolbar.buttonContainerPanel;
import org.bungeni.editor.panels.toolbar.buttonPanel;
import org.bungeni.editor.panels.toolbar.scrollPanel;
import org.bungeni.editor.providers.DocumentSectionAdapterDefaultTreeModel;
import org.bungeni.editor.providers.DocumentSectionFriendlyAdapterDefaultTreeModel;
import org.bungeni.editor.providers.DocumentSectionFriendlyTreeModelProvider;
import org.bungeni.editor.providers.DocumentSectionProvider;
import org.bungeni.editor.providers.DocumentSectionTreeModelProvider;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.editor.toolbar.BungeniToolbarTargetProcessor;
import org.bungeni.editor.toolbar.conditions.BungeniToolbarConditionProcessor;
import org.bungeni.extutils.CommonDocumentUtilFunctions;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.utils.BungeniBNode;
import org.bungeni.utils.BungeniBTree;
import org.bungeni.extutils.CommonTreeFunctions;
import org.bungeni.utils.compare.BungeniTreeRefactorTree;

/**
 * This is the floating panel implementation for the Editor's action bar
 * @author  Ashok Hariharan
 */
public class holderUIPanel2 extends javax.swing.JPanel implements IFloatingPanel {

    private OOComponentHelper ooDocument;
    private JFrame parentFrame;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(holderUIPanel2.class.getName());
    private org.bungeni.editor.toolbar.BungeniToolbarXMLTree toolbarXmlTree;
    private BungeniClientDB instance;
    private Timer timerSectionTree;
    private changeStructureItem[] m_comboChangeStructureItems = {
        //      new changeStructureItem ("VIEW_PARAGRAPHS", "View Paragraphs"),
        new changeStructureItem("VIEW_SECTIONS", "View Sections"),
        new changeStructureItem("VIEW_PRETTY_SECTIONS", "View Structure")
    };
    private changeStructureItem m_selectedChangeStructureItem = null;
    private JTree sectionInternalStructureTree;
    private String m_currentSelectedSectionName;
    private Timer toolbarTimer = null;
    //cache for conditions
    HashMap<String, BungeniToolbarConditionProcessor> conditionMap = new HashMap<String, BungeniToolbarConditionProcessor>();

    /** Creates new form holderUIPanel */
    public holderUIPanel2() {
        initComponents();
        instance = new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
    //the below are called from initUI()
    // aclbarTree();
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
        if (ooDocument != null) {
            currentHandleKey = editorTabbedPanel.componentHandleContainer.generateComponentKey(ooDocument.getDocumentTitle(), ooDocument.getComponent());
            newHandleKey = editorTabbedPanel.componentHandleContainer.generateComponentKey(ooComponent.getDocumentTitle(), ooComponent.getComponent());
            //update only when the incoming handle changes....
            if (!currentHandleKey.equalsIgnoreCase(newHandleKey)) {
                log.debug("holderUIPanel, setOOComponentHandle : updating component handle");
                this.ooDocument = ooComponent;
            }
        //updatePanelonComponentChange();
        } else {
            log.debug("holderUIPanel, setOOComponentHandle : ooDocument was null updating component handle");
            this.ooDocument = ooComponent;
        }
        log.debug("setOOComponenthHandle: starting timer");
    }

    private synchronized OOComponentHelper getOODocument() {
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
        this.initToolbarTabs();
        this.initSectionStructureTree();
        this.initSectionInternalStructureTree();
        this.initButtonListeners();
        this.initMouseListener();
        this.initComboChangeStructure();
        this.initTimers();
        this.initUIAttributes();
    }
    private BungeniToolbarLoader toolbarLoader = null;

    private void initToolbarTabs() {
        toolbarLoader = new BungeniToolbarLoader(new BungeniToolbarCommandListener());
        toolbarLoader.loadToolbar(this.toolbarTabs);
        //toolbar update timer
        toolbarTimer = new javax.swing.Timer(3000, new BungeniToolbarTimerListener());
        toolbarTimer.setInitialDelay(3000);
        toolbarTimer.start();
        //toolbar tab change listener
        toolbarTabs.addChangeListener(new BungeniToolbarTabChangeListener());

    }

    /***
     * This event is invoked whenever the Action tabs are switched.
     * The toolbar action states are refreshed for the current tab since the timer delay
     * is 3 seconds.
     */
    class BungeniToolbarTabChangeListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            processActionStatesForSelectedTab();
        }
    }
    
    class BungeniToolbarTimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    processActionStatesForSelectedTab();
                }
            });
        }
    }

    /***** API for conditionally enabling / disabling command buttons *****/

    public Component[] getButtonPanelsForSelectedTab() {
        int nSelectedTab = toolbarTabs.getSelectedIndex();
        if (-1 != nSelectedTab) {
            Component cSelected = toolbarTabs.getSelectedComponent();
            log.debug("selected component class = " + cSelected.getClass().getName());
            scrollPanel panel = (scrollPanel) cSelected;
            buttonContainerPanel buttonsPanel = panel.getViewPortPanel();
            Component[] components = buttonsPanel.getComponents();
            return components;
        }
        return null;
    }

    /***
     * This function is 'synchronized' becuase it is called from within the timer thread
     * and also on the onChange event for the tab
     */
    synchronized void processActionStatesForSelectedTab() {
        Component[] components = getButtonPanelsForSelectedTab();
        for (Component c : components) {
            //process only the child components that are button panels
            if (c.getClass().getName().equals(buttonPanel.class.getName())) {
                buttonPanel buttonPanel = (buttonPanel) c;
                BungeniToolbarActionElement actionElement = buttonPanel.getActionElement();
                boolean bState = processActionCondition(actionElement.getCondition());
                buttonPanel.enableActionButton(bState);
            }
        }
    }

    boolean processActionCondition(String conditionValue) {
        boolean bAction = true;

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


   /***** API for conditionally enabling / disabling command buttons *****/


    class BungeniToolbarCommandListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            //get the button originating the event
            JButton sourceButton = (JButton) e.getSource();
            Container parentContainer = sourceButton.getParent();
            buttonPanel containerPanel = (buttonPanel) parentContainer;
            //get the action element
            BungeniToolbarActionElement actionElement = containerPanel.getActionElement();
            executeToolbarAction(actionElement);
        }

        public void executeToolbarAction(BungeniToolbarActionElement actionElement) {
            BungeniToolbarTargetProcessor targetObj = new BungeniToolbarTargetProcessor(actionElement.getTarget());
            SelectorDialogModes selectedMode = actionElement.getMode();
            toolbarAction tbAction = null;
            toolbarSubAction tbSubAction = null;
            switch (targetObj.target_type) {
                case ACTION:
                    tbAction = processInsertion(targetObj);
                    tbAction.setSelectorDialogMode(selectedMode);
                    processAction(tbAction);
                    break;
                case SUB_ACTION:
                    tbSubAction = processSelection(targetObj);
                    tbSubAction.setSelectorDialogMode(selectedMode);
                    processSubAction(tbSubAction);
                    break;
            }

        }

        private void processSubAction(toolbarSubAction action) {
            log.debug("processSubAction:" + action.sub_action_name());
            IEditorActionEvent event = getEventClass(action);
            event.doCommand(getOODocument(), action, parentFrame);
        }

        private void processAction(toolbarAction action) {
            log.debug("processAction:" + action.action_name());

            if (action.isTopLevelAction()) {
                log.info("toolbar: processAction: not processing topLevelAction type");
                return;
            }
            IEditorActionEvent event = getEventClass(action);
            event.doCommand(getOODocument(), action, parentFrame);
        }

        private toolbarSubAction processSelection(BungeniToolbarTargetProcessor targetObj) {

            String documentType = BungeniEditorProperties.getEditorProperty("activeDocumentMode");
            instance.Connect();
            String actionQuery = SettingsQueryFactory.Q_FETCH_SUB_ACTIONS(documentType, targetObj.actionName, targetObj.subActionName);
            log.info("processSelection: " + actionQuery);
            QueryResults qr = instance.QueryResults(actionQuery);
            instance.EndConnect();
            if (qr == null) {
                log.info("processSelection : queryResults :" + actionQuery + " were null, metadata incorrectly setup");
                return null;
            }
            if (qr.hasResults()) {
                //this should return only a single toolbarSubAction
                toolbarSubAction subActionObj = new toolbarSubAction(qr.theResults().elementAt(0), qr.columnNameMap());
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
            if (qr == null) {
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
    }

    private void initUIAttributes() {
        //set scroolbar widths
        /**** fix this
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
         */
    }
    JPopupMenu sectionStructureMenu;
    private static HashMap<String, String> POPUP_ACTIONS = new HashMap<String, String>() {

        {
            put("GO_TO", "Go To");
            put("NL_AFTER", "New Line After");
            put("NL_BEFORE", "New Line Before");
        }
    };

    private void initPopupMenu() {
        sectionStructureMenu = new JPopupMenu();
        Font menuFont = new java.awt.Font("DejaVu Sans", 0, 10);
        sectionStructureMenu.setFont(menuFont);
        ActionListener popupMenuListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("GO_TO")) {
                    //call go to section
                    TreePath selPath = sectionStructureTree.getSelectionPath();
                    selectSectionFromTreePath(selPath);
                }
                if (e.getActionCommand().equals("NL_AFTER")) {
                    //call go to after
                    //add new line after section
                    TreePath selPath = sectionStructureTree.getSelectionPath();
                    String sectionName = getSectionFromTreePath(selPath);
                    CommonDocumentUtilFunctions.addNewLineAfterSection(ooDocument, sectionName);
                }
                if (e.getActionCommand().equals("NL_BEFORE")) {
                    //call go before
                    //add new line before section
                    TreePath selPath = sectionStructureTree.getSelectionPath();
                    String sectionName = getSectionFromTreePath(selPath);
                    CommonDocumentUtilFunctions.addNewLineBeforeSection(ooDocument, sectionName);

                }
            }
        };

        JMenuItem itemGoto = setupMenuItem("GO_TO", popupMenuListener);
        itemGoto.setFont(menuFont);
        JMenuItem itemnlAfter = setupMenuItem("NL_AFTER", popupMenuListener);
        itemnlAfter.setFont(menuFont);
        JMenuItem itemnlBefore = setupMenuItem("NL_BEFORE", popupMenuListener);
        itemnlBefore.setFont(menuFont);

        sectionStructureMenu.add(itemGoto);
        sectionStructureMenu.add(itemnlAfter);
        sectionStructureMenu.add(itemnlBefore);

    }

    private JMenuItem setupMenuItem(String key, ActionListener listener) {
        String keyValue = POPUP_ACTIONS.get(key);
        JMenuItem item = new JMenuItem(keyValue);
        item.setActionCommand(key);
        item.addActionListener(listener);
        return item;

    }

    private String getSectionFromTreePath(TreePath selPath) {
        String sectionName = "";
        if (selPath != null) {
            try {
                Object node = selPath.getLastPathComponent();
                DefaultMutableTreeNode dmt = (DefaultMutableTreeNode) node;
                Object userObject = dmt.getUserObject();
                if (userObject.getClass().getName().equals(BungeniBNode.class.getName())) {
                    BungeniBNode bNode = (BungeniBNode) userObject;
                    sectionName = bNode.getName();
                }
            } catch (Exception ex) {
                log.error("mousePressed: exception occured : " + ex.getMessage());
            } finally {
                return sectionName;
            }
        }
        return sectionName;
    }

    private void selectSectionFromTreePath(TreePath selPath) {
        String sectionName = getSectionFromTreePath(selPath);
        if (ooDocument.hasSection(sectionName)) {
            CommonDocumentUtilFunctions.selectSection(ooDocument, sectionName);
        }
    }

    private void initSectionStructureTree() {
        initPopupMenu();
        sectionStructureTree.setExpandsSelectedPaths(true);
        ImageIcon minusIcon = CommonTreeFunctions.treeMinusIcon();
        ImageIcon plusIcon = CommonTreeFunctions.treePlusIcon();
        sectionStructureTree.setCellRenderer(new treeViewPrettySectionsTreeCellRenderer());
        sectionStructureTree.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent evt) {
                //if event is the popup trigger for this os
                System.out.println(evt.getButton());
                if (evt.isPopupTrigger()) {
                    sectionStructureMenu.show((Component) evt.getSource(), evt.getX(), evt.getY());
                }
                //check if double click ...move focus to section
                if (evt.getClickCount() == 2) {
                    TreePath selPath = sectionStructureTree.getPathForLocation(evt.getX(), evt.getY());
                    selectSectionFromTreePath(selPath);
                }
            }
        });
        sectionStructureTree.setShowsRootHandles(true);
        ComponentUI treeui = sectionStructureTree.getUI();
        if (treeui instanceof BasicTreeUI) {
            ((BasicTreeUI) treeui).setExpandedIcon(minusIcon);
            ((BasicTreeUI) treeui).setCollapsedIcon(plusIcon);
        }
        initSectionStructureTreeModel();

    }

    private void initSectionStructureTreeModel() {
        try {
            DocumentSectionFriendlyAdapterDefaultTreeModel model = DocumentSectionFriendlyTreeModelProvider.create();//_without_subscription();
            //change later
            //  DocumentSectionAdapterDefaultTreeModel model = DocumentSectionTreeModelProvider.create();//_without_subscription();
            this.sectionStructureTree.setModel(model);
            CommonTreeFunctions.expandAll(sectionStructureTree);
        } catch (Exception ex) {
            log.error("initSectionStructureTreeModel : " + ex.getMessage());
            log.error("initSectionStructureTreeModel : " + CommonExceptionUtils.getStackTrace(ex));
        }
    }

    private void initSectionInternalStructureTree() {
        this.sectionInternalStructureTree = new JTree();
        sectionInternalStructureTree.setExpandsSelectedPaths(true);
        ImageIcon minusIcon = CommonTreeFunctions.treeMinusIcon();
        ImageIcon plusIcon = CommonTreeFunctions.treePlusIcon();
        //sectionInternalStructureTree.setCellRenderer(new treeViewPrettySectionsTreeCellRenderer());
        sectionInternalStructureTree.setShowsRootHandles(true);
        ComponentUI treeui = sectionInternalStructureTree.getUI();
        if (treeui instanceof BasicTreeUI) {
            ((BasicTreeUI) treeui).setExpandedIcon(minusIcon);
            ((BasicTreeUI) treeui).setCollapsedIcon(plusIcon);
        }
        initSectionInternalStructureTreeModel();
    }

    private void initSectionInternalStructureTreeModel() {
        try {
            DocumentSectionAdapterDefaultTreeModel model = DocumentSectionTreeModelProvider.create();//_without_subscription();
            this.sectionInternalStructureTree.setModel(model);
            CommonTreeFunctions.expandAll(sectionInternalStructureTree);
        } catch (Exception ex) {
            log.error("initSectionStructureTreeModel : " + ex.getMessage());
            log.error("initSectionStructureTreeModel : " + CommonExceptionUtils.getStackTrace(ex));
        }
    }

    private void initTimers() {
        /*
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
         * */
    }

    private holderUIPanel2 self() {
        return this;
    }

    private void initButtonListeners() {
        btnViewDefault.setSelected(true);
        //add action listeners to button
        btnViewDefault.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                JRadioButton btn = (JRadioButton) arg0.getSource();
                if (btn.isSelected()) {
                    //////fix this      scrollToolbarTree.setVisible(true);
                    scrollTreeView.setVisible(true);
                    self().parentFrame.setVisible(true);
                    self().parentFrame.setExtendedState(JFrame.NORMAL);
                }
            }
        });
        btnHideToolbarTree.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                JRadioButton btn = (JRadioButton) arg0.getSource();
                if (btn.isSelected()) {
/////fix this                    scrollToolbarTree.setVisible(false);
                    scrollTreeView.setVisible(true);
                    //revalidate does 're-layout' of the panel
                    self().revalidate();
                }
            }
        });
        btnHideTreeView.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                JRadioButton btn = (JRadioButton) arg0.getSource();
                if (btn.isSelected()) {
/////fix this                     scrollToolbarTree.setVisible(true);
                    scrollTreeView.setVisible(false);
                    //revalidate does 're-layout' of the panel
                    self().revalidate();
                }
            }
        });
    }

    private void initMouseListener() {
        //first move focus to frame on mouse over
        parentFrame.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent arg0) {
                log.debug("mouseListener : entered holderUIPanel");
                parentFrame.requestFocus();
            }
        });
        //after moving focus to frame, shift focus to the toolbar tree
        parentFrame.addWindowFocusListener(new WindowAdapter() {

            @Override
            public void windowGainedFocus(WindowEvent e) {
                /////fix this                toolbarTree.requestFocusInWindow();
            }
        });

    }

    private void initComboChangeStructure() {
        changeStructureItem itemDefault = null;
        cboChangeStructure.removeAllItems();
        String defaultHierarchyView = BungeniEditorProperties.getEditorProperty("defaultHierarchyView");
        for (int i = 0; i < this.m_comboChangeStructureItems.length; i++) {
            if (m_comboChangeStructureItems[i].getIndex().equalsIgnoreCase(defaultHierarchyView)) {
                itemDefault = m_comboChangeStructureItems[i];
            }
            cboChangeStructure.addItem(m_comboChangeStructureItems[i]);
        }

        //add a change listener to the combobox...
        cboChangeStructure.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                JComboBox box = (JComboBox) arg0.getSource();
                changeStructureItem theItem = (changeStructureItem) box.getSelectedItem();
                m_selectedChangeStructureItem = theItem;
                updateViewPortForTree();
                updateSectionTree();
            }
        });

        if (itemDefault != null) {
            cboChangeStructure.setSelectedItem(itemDefault);
        }
        m_selectedChangeStructureItem = (changeStructureItem) cboChangeStructure.getSelectedItem();
    }

    private void updatePanelonComponentChange() {
        //refresh the tree model
        this.timerSectionTree.stop();
        // refreshTreeModel();
        this.timerSectionTree.start();
    }

    private void updateViewPortForTree() {
        SwingUtilities.invokeLater(new Runnable() {

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
            log.debug("updateSectionTree : " + m_selectedChangeStructureItem);
            if (this.m_selectedChangeStructureItem.itemIndex.equals("VIEW_SECTIONS")) {
                //do this only if the tree is visible
                if (sectionInternalStructureTree.isShowing()) {
                    log.debug("updateSectionTree : refreshing section_internal_structure tree");
                    BungeniBTree newTree = DocumentSectionProvider.getNewTree();
                    BungeniBNode newRootNode = newTree.getFirstRoot();
                    DocumentSectionAdapterDefaultTreeModel model = (DocumentSectionAdapterDefaultTreeModel) this.sectionInternalStructureTree.getModel();
                    DefaultMutableTreeNode mnode = (DefaultMutableTreeNode) model.getRoot();
                    BungeniBNode origNode = (BungeniBNode) mnode.getUserObject();
                    BungeniTreeRefactorTree refTree = new BungeniTreeRefactorTree(model, origNode, newRootNode);
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
                    BungeniTreeRefactorTree refTree = new BungeniTreeRefactorTree(model, origNode, newRootNode);
                    refTree.setMergeDisplayText(false);
                    refTree.doMerge();
                } else {
                    log.debug("updateSectionTree : section friendly structure tree is not visible");
                }
            }
        } catch (Exception ex) {
            log.error("exception updateSectionTree : " + ex.getMessage());
        }
    }

    /**
     *
     */
    class treeViewPrettySectionsTreeCellRenderer extends DefaultTreeCellRenderer {

        Color bgColor = new java.awt.Color(232, 255, 175);
        Color bgColorSelect = new java.awt.Color(207, 242, 255);

        treeViewPrettySectionsTreeCellRenderer() {
            // setOpaque(true);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            setFont(TREE_LABEL_FONT);
            setText(value.toString());
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode uo = (DefaultMutableTreeNode) value;
                Object uoObj = uo.getUserObject();
                if (uoObj.getClass() == org.bungeni.utils.BungeniBNode.class) {
                    BungeniBNode aNode = (BungeniBNode) uoObj;
                    if (aNode.getName().equals(m_currentSelectedSectionName)) {
                        //setBorder(selBorder);
                        c.setBackground(bgColor);
                    } else if (selected) {
                        c.setBackground(bgColorSelect);
                    } else {
                        c.setBackground(null);
                    }
                }
            }
            return c;
        }
    }
    private static final Font TREE_LABEL_FONT = new Font("Tahoma", Font.PLAIN, 11);

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
        public String toString() {
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
        scrollTreeView = new javax.swing.JScrollPane();
        sectionStructureTree = new javax.swing.JTree();
        cboChangeStructure = new javax.swing.JComboBox();
        btnHideToolbarTree = new javax.swing.JRadioButton();
        btnHideTreeView = new javax.swing.JRadioButton();
        btnViewDefault = new javax.swing.JRadioButton();
        btnRefresh = new javax.swing.JButton();
        btnSectionsExpandAll = new javax.swing.JButton();
        btnSectionsCollapseAll = new javax.swing.JButton();
        toolbarTabs = new javax.swing.JTabbedPane();

        scrollTreeView.setViewportView(sectionStructureTree);

        cboChangeStructure.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        cboChangeStructure.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnGroupSwitchView.add(btnHideToolbarTree);
        btnHideToolbarTree.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/panels/Bundle"); // NOI18N
        btnHideToolbarTree.setText(bundle.getString("holderUIPanel2.btnHideToolbarTree.text")); // NOI18N

        btnGroupSwitchView.add(btnHideTreeView);
        btnHideTreeView.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnHideTreeView.setText(bundle.getString("holderUIPanel2.btnHideTreeView.text")); // NOI18N

        btnGroupSwitchView.add(btnViewDefault);
        btnViewDefault.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnViewDefault.setText(bundle.getString("holderUIPanel2.btnViewDefault.text")); // NOI18N

        btnRefresh.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnRefresh.setText(bundle.getString("holderUIPanel2.btnRefresh.text")); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnSectionsExpandAll.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnSectionsExpandAll.setText(bundle.getString("holderUIPanel2.btnSectionsExpandAll.text")); // NOI18N
        btnSectionsExpandAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSectionsExpandAllActionPerformed(evt);
            }
        });

        btnSectionsCollapseAll.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnSectionsCollapseAll.setText(bundle.getString("holderUIPanel2.btnSectionsCollapseAll.text")); // NOI18N
        btnSectionsCollapseAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSectionsCollapseAllActionPerformed(evt);
            }
        });

        toolbarTabs.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

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
            .addComponent(scrollTreeView, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnSectionsExpandAll, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSectionsCollapseAll, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(toolbarTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(toolbarTabs, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollTreeView, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSectionsExpandAll, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSectionsCollapseAll, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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

private void btnSectionsExpandAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSectionsExpandAllActionPerformed
    // TODO add your handling code here:
    CommonTreeFunctions.expandAll(sectionStructureTree);
}//GEN-LAST:event_btnSectionsExpandAllActionPerformed

private void btnSectionsCollapseAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSectionsCollapseAllActionPerformed
    // TODO add your handling code here:
    CommonTreeFunctions.collapseAll(sectionStructureTree);
}//GEN-LAST:event_btnSectionsCollapseAllActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGroupSwitchView;
    private javax.swing.JRadioButton btnHideToolbarTree;
    private javax.swing.JRadioButton btnHideTreeView;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSectionsCollapseAll;
    private javax.swing.JButton btnSectionsExpandAll;
    private javax.swing.JRadioButton btnViewDefault;
    private javax.swing.JComboBox cboChangeStructure;
    private javax.swing.JScrollPane scrollTreeView;
    private javax.swing.JTree sectionStructureTree;
    private javax.swing.JTabbedPane toolbarTabs;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        javax.swing.JFrame floatingFrame = new javax.swing.JFrame();
        holderUIPanel2 floatingPanel = new holderUIPanel2();
        floatingFrame.add(floatingPanel);
        floatingFrame.setSize(221, 551);
        floatingFrame.setAlwaysOnTop(true);
        floatingFrame.setVisible(true);
        //position frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = floatingFrame.getSize();

        int windowX = screenSize.width - floatingFrame.getWidth() - 2;
        int windowY = (screenSize.height - floatingFrame.getHeight()) / 2;
        floatingFrame.setLocation(windowX, windowY);  // Don't use "f." inside constructor.
        floatingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
