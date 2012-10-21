package org.bungeni.editor.panels.loadable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
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
import org.bungeni.editor.actions.EditorActionFactory;
import org.bungeni.editor.actions.IEditorActionEvent;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.panels.impl.BaseClassForITabbedPanel;
import org.bungeni.editor.providers.DocumentSectionAdapterDefaultTreeModel;
import org.bungeni.editor.providers.DocumentSectionFriendlyAdapterDefaultTreeModel;
import org.bungeni.editor.providers.DocumentSectionFriendlyTreeModelProvider;
import org.bungeni.editor.providers.DocumentSectionProvider;
import org.bungeni.editor.providers.DocumentSectionTreeModelProvider;
import org.bungeni.editor.toolbar.conditions.BungeniToolbarConditionProcessor;
import org.bungeni.extutils.CommonDocumentUtilFunctions;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.utils.BungeniBNode;
import org.bungeni.utils.BungeniBTree;
import org.bungeni.extutils.CommonTreeFunctions;
import org.bungeni.utils.CommonBungeniTreeFunctions;
import org.bungeni.utils.compare.BungeniTreeRefactorTree;

/**
 * This is the floating panel implementation for the Editor's action bar
 * AH-23-11-01 This class is a good candidate for a rewrite.
 * Currently the section structure is iterated using the UNO API. This presents some performance and thread
 * contention limitations. Since Openoffice 3 there is a ODFDOM API which allows browsing ODF structures via
 * the file system -- this structure presentation part should be rewritten to use the ODFDOM api.
 * 
 * @author  Ashok Hariharan
 * @rewritecandidate
 */
public class documentStructurePanel extends BaseClassForITabbedPanel {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(documentStructurePanel.class.getName());
    //private BungeniClientDB instance;
    private String m_currentSelectedSectionName;
    private Timer toolbarTimer = null;
    //cache for conditions
    HashMap<String, BungeniToolbarConditionProcessor> conditionMap = new HashMap<String, BungeniToolbarConditionProcessor>();

    /** Creates new form holderUIPanel */
    public documentStructurePanel() {
        initComponents();
       
    }


    private synchronized OOComponentHelper getOODocument() {
        return this.ooDocument;
    }


    public documentStructurePanel(OOComponentHelper ooDocument, JFrame parentFrame) {
        this();
        this.parentFrame = parentFrame;
        this.ooDocument = ooDocument;
        
    }

    private void initDB(){
       // instance = new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
    }

    public IEditorActionEvent getEventClass(toolbarAction subAction) {
        IEditorActionEvent event = EditorActionFactory.getEventClass(subAction);
        return event;
    }

    // !+ACTION_RECONF (rm, jan 2012) - toolbarAction is deprecated
    /**
    public IEditorActionEvent getEventClass(toolbarAction action) {
        IEditorActionEvent event = EditorActionFactory.getEventClass(action);
        return event;
    }
    **/
    
    public void setParentWindowHandle(JFrame c) {
        this.parentFrame = c;
    }


    /**
     *
     */
    @Override
    public void initialize() {
        super.initialize();
        this.initDB();
        this.initSectionStructureTree();
        this.initSectionInternalStructureTree();
        this.initSectionTabsListener();
        this.initMouseListener();
    }

    private Color toolbarTabBgColor = null , toolbarTabSelectedBgColor = null;


    private void initSectionTabsListener() {
        this.tabSectionView.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                updateSectionTree();
            }
        });
    }

    public void setSectionChangeInfo(String sectionChange) {
        //update the section structure tree
        SwingUtilities.invokeLater(new Runnable(){

            public void run() {
                SwingUtilities.updateComponentTreeUI(sectionStructureTree);
            }

        });
    }

    public void updateEvent() {
        this.updateSectionTree();
    }

    @Override
    public void refreshPanel() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }


    class BungeniToolbarPreTabChangeListener implements ChangeListener {

        private JTabbedPane forThisTab = null;
        
        public BungeniToolbarPreTabChangeListener(JTabbedPane thisTab) {
                forThisTab = thisTab;
        }

        public void stateChanged(ChangeEvent e) {
            Component selectedComponent = forThisTab.getSelectedComponent();
            selectedComponent.setBackground(toolbarTabBgColor);
            //now set it for the selected sub-tab - if any
        }

    }


    JPopupMenu sectionStructureMenu;
    private static HashMap<String, String> POPUP_ACTIONS = new HashMap<String, String>() {

        {
            put("GO_TO", "Go To");
            put("NL_AFTER", "New Line After");
            put("NL_BEFORE", "New Line Before");
        }
    };


    class PopupMenuActionListener implements ActionListener {

        private JTree whichTree(){
            if (sectionStructureTree.isVisible()) return sectionStructureTree;
            return sectionInternalStructureTree;
        }
        
        public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("GO_TO")) {
                    //call go to section
                    TreePath selPath = whichTree().getSelectionPath();
                    selectSectionFromTreePath(selPath);
                }
                if (e.getActionCommand().equals("NL_AFTER")) {
                    //call go to after
                    //add new line after section
                    TreePath selPath = whichTree().getSelectionPath();
                    String sectionName = getSectionFromTreePath(selPath);
                    CommonDocumentUtilFunctions.addNewLineAfterSection(ooDocument, sectionName);
                }
                if (e.getActionCommand().equals("NL_BEFORE")) {
                    //call go before
                    //add new line before section
                    TreePath selPath = whichTree().getSelectionPath();
                    String sectionName = getSectionFromTreePath(selPath);
                    CommonDocumentUtilFunctions.addNewLineBeforeSection(ooDocument, sectionName);
                }
        }
    }
    private void initPopupMenu() {
        sectionStructureMenu = new JPopupMenu();
        Font menuFont = new java.awt.Font("DejaVu Sans", 0, 10);
        sectionStructureMenu.setFont(menuFont);

        PopupMenuActionListener popupMenuListener = new PopupMenuActionListener();

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
        ImageIcon minusIcon = CommonBungeniTreeFunctions.treeMinusIcon();
        ImageIcon plusIcon = CommonBungeniTreeFunctions.treePlusIcon();
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
        // this.sectionInternalStructureTree = new JTree();
        sectionInternalStructureTree.setExpandsSelectedPaths(true);
        ImageIcon minusIcon = CommonBungeniTreeFunctions.treeMinusIcon();
        ImageIcon plusIcon = CommonBungeniTreeFunctions.treePlusIcon();
        //sectionInternalStructureTree.setCellRenderer(new treeViewPrettySectionsTreeCellRenderer());
        sectionInternalStructureTree.setCellRenderer(new treeInternalStructureSectionsTreeCellRenderer());
        sectionInternalStructureTree.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent evt) {
                //if event is the popup trigger for this os
                if (evt.isPopupTrigger()) {
                    sectionStructureMenu.show((Component) evt.getSource(), evt.getX(), evt.getY());
                }
                //check if double click ...move focus to section
                if (evt.getClickCount() == 2) {
                    TreePath selPath = sectionInternalStructureTree.getPathForLocation(evt.getX(), evt.getY());
                    selectSectionFromTreePath(selPath);
                }
            }
        });

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

  

    private documentStructurePanel self() {
        return this;
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

    private void updateSectionInternalStructureTree() {
        log.debug("updateSectionTree : refreshing section_internal_structure tree");
        BungeniBTree newTree = DocumentSectionProvider.getNewTree();
        BungeniBNode newRootNode = newTree.getFirstRoot();
        DocumentSectionAdapterDefaultTreeModel model = (DocumentSectionAdapterDefaultTreeModel) this.sectionInternalStructureTree.getModel();
        DefaultMutableTreeNode mnode = (DefaultMutableTreeNode) model.getRoot();
        BungeniBNode origNode = (BungeniBNode) mnode.getUserObject();
        BungeniTreeRefactorTree refTree = new BungeniTreeRefactorTree(model, origNode, newRootNode);
        refTree.setMergeDisplayText(false);
        refTree.doMerge();
    }

    private void updateSectionDocStructureTree() {
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

    }

    private void updateSectionTree() {
        try {
            int nIndex = tabSectionView.getSelectedIndex();
            switch (nIndex) {
                case 0:
                    updateSectionDocStructureTree();
                    break;
                case 1:
                    updateSectionInternalStructureTree();
                    break;
                default:
                    return;
            }
        } catch (Exception ex) {
            log.error("exception updateSectionTree : " + ex.getMessage());
        }
    }

    private static final Font TREE_LABEL_FONT = new Font("Tahoma", Font.PLAIN, 11);

    /**
     *
     */

    class treeInternalStructureSectionsTreeCellRenderer extends DefaultTreeCellRenderer {

        Color bgColor = new java.awt.Color(232, 255, 175);
        Color bgColorSelect = new java.awt.Color(207, 242, 255);

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
                     //   c.setBackground(bgColorSelect);
                    } else {
                        c.setBackground(null);
                    }
                }
            }
            return c;
        }

    }

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
                       // c.setBackground(bgColorSelect);
                    } else {
                        c.setBackground(null);
                    }
                }
            }
            return c;
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
        btnRefresh = new javax.swing.JButton();
        btnSectionsExpandAll = new javax.swing.JButton();
        btnSectionsCollapseAll = new javax.swing.JButton();
        tabSectionView = new javax.swing.JTabbedPane();
        scrollTreeView = new javax.swing.JScrollPane();
        sectionStructureTree = new javax.swing.JTree();
        scrollInternalTreeView = new javax.swing.JScrollPane();
        sectionInternalStructureTree = new javax.swing.JTree();

        btnRefresh.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/panels/loadable/Bundle"); // NOI18N
        btnRefresh.setText(bundle.getString("documentStructurePanel.btnRefresh.text")); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnSectionsExpandAll.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnSectionsExpandAll.setText(bundle.getString("documentStructurePanel.btnSectionsExpandAll.text")); // NOI18N
        btnSectionsExpandAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSectionsExpandAllActionPerformed(evt);
            }
        });

        btnSectionsCollapseAll.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnSectionsCollapseAll.setText(bundle.getString("documentStructurePanel.btnSectionsCollapseAll.text")); // NOI18N
        btnSectionsCollapseAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSectionsCollapseAllActionPerformed(evt);
            }
        });

        tabSectionView.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        tabSectionView.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        scrollTreeView.setViewportView(sectionStructureTree);

        tabSectionView.addTab(bundle.getString("documentStructurePanel.scrollTreeView.TabConstraints.tabTitle"), scrollTreeView); // NOI18N

        scrollInternalTreeView.setViewportView(sectionInternalStructureTree);

        tabSectionView.addTab(bundle.getString("documentStructurePanel.scrollInternalTreeView.TabConstraints.tabTitle"), scrollInternalTreeView); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSectionsExpandAll, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSectionsCollapseAll, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(tabSectionView, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(tabSectionView, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSectionsExpandAll, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSectionsCollapseAll, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
// TODO add your handling code here:
    this.updateSectionTree();
}//GEN-LAST:event_btnRefreshActionPerformed

private void btnSectionsExpandAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSectionsExpandAllActionPerformed
    int nSelectedIndex = this.tabSectionView.getSelectedIndex();
    switch (nSelectedIndex) {
        case 0:
            CommonTreeFunctions.expandAll(sectionStructureTree);
            break;
        case 1:
            CommonTreeFunctions.expandAll(sectionInternalStructureTree);
            break;
        default:
            return;
    }
}//GEN-LAST:event_btnSectionsExpandAllActionPerformed

private void btnSectionsCollapseAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSectionsCollapseAllActionPerformed
    // TODO add your handling code here:
    int nSelectedIndex = this.tabSectionView.getSelectedIndex();
    switch (nSelectedIndex) {
        case 0:
            CommonTreeFunctions.collapseAll(sectionStructureTree);
            break;
        case 1:
            CommonTreeFunctions.collapseAll(sectionInternalStructureTree);
            break;
        default:
            return;
    }
}//GEN-LAST:event_btnSectionsCollapseAllActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGroupSwitchView;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSectionsCollapseAll;
    private javax.swing.JButton btnSectionsExpandAll;
    private javax.swing.JScrollPane scrollInternalTreeView;
    private javax.swing.JScrollPane scrollTreeView;
    private javax.swing.JTree sectionInternalStructureTree;
    private javax.swing.JTree sectionStructureTree;
    private javax.swing.JTabbedPane tabSectionView;
    // End of variables declaration//GEN-END:variables

 }
