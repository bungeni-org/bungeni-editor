/*
 * documentMetadataPanel.java
 *
 * Created on May 15, 2008, 11:47 AM
 */
package org.bungeni.editor.panels.loadable;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.bungeni.editor.actions.DocumentActionsReader;
import org.bungeni.editor.actions.EditorActionFactory;
import org.bungeni.editor.actions.IEditorActionEvent;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.editor.dialogs.metadatapanel.SectionMetadataLoad;
import org.bungeni.editor.panels.impl.BaseClassForITabbedPanel;
import org.bungeni.editor.providers.DocumentSectionFriendlyAdapterDefaultTreeModel;
import org.bungeni.editor.providers.DocumentSectionFriendlyTreeModelProvider;
import org.bungeni.editor.providers.DocumentSectionProvider;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.editor.selectors.metadata.SectionMetadataEditor;
import org.bungeni.editor.toolbar.target.BungeniToolbarTargetProcessor;
import org.bungeni.extutils.CommonStringFunctions;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.utils.BungeniBNode;
import org.bungeni.extutils.CommonTreeFunctions;
import org.bungeni.utils.compare.BungeniTreeRefactorTree;
import org.jdom.Element;

/**
 *
 * @author  Administrator
 */
public class sectionTreeMetadataPanel extends BaseClassForITabbedPanel {

    private DefaultMutableTreeNode sectionRootNode = null;
    private boolean emptyRootNode = false;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(sectionTreeMetadataPanel.class.getName());
    private static final String ROOT_SECTION = BungeniEditorPropertiesHelper.getDocumentRoot();
    //arraylist capturing tree state.
    private ArrayList<String> m_savedTreeState = new ArrayList<String>();
    //object storing selected section
    private BungeniBNode m_selectedSection;
    private Timer sectionMetadataRefreshTimer;

    /** Creates new form documentMetadataPanel */
    public sectionTreeMetadataPanel() {
        log.debug("constructing sectionTreeMetadataPanel");
        initComponents();
    }

    public sectionTreeMetadataPanel(OOComponentHelper ooDoc, JFrame p) {
        parentFrame = p;
        ooDocument = ooDoc;
        init();
    }

    private void init() {
        initComponents();
        initTreeStructure();
    // initTableDocumentMetadata();
    }

    private sectionTreeMetadataPanel self() {
        return this;
    }

    private void initTableDocumentMetadata() {
        this.treeSectionTreeMetadata.addMouseListener(new treeDocStructureTreeMouseListener());
        DocumentSectionFriendlyAdapterDefaultTreeModel model = DocumentSectionFriendlyTreeModelProvider.create();//_without_subscription();
        this.treeSectionTreeMetadata.setModel(model);
        //CommonTreeFunctions.expandAll(treeSectionStructure);
        updateTableMetadataModel(ROOT_SECTION);
        //-tree-deprecated--CommonTreeFunctions.expandAll(treeSectionStructure, true);
        CommonTreeFunctions.expandAll(treeSectionTreeMetadata);
    }

    private void initTreeStructure() {
        treeSectionTreeMetadata.setExpandsSelectedPaths(true);

        TreeCellRenderer sectionTreeRender = this.treeSectionTreeMetadata.getCellRenderer();
        ImageIcon minusIcon = CommonTreeFunctions.treeMinusIcon();
        ImageIcon plusIcon = CommonTreeFunctions.treePlusIcon();
        /**  sectionTreeRender.setOpenIcon(null); **/
        /**  sectionTreeRender.setClosedIcon(null); **/
        //    UIManager.put("Tree.expandedIcon", minusIcon);
        //    UIManager.put("Tree.collapsedIcon", plusIcon);
        /**  sectionTreeRender.setLeafIcon(null); **/
        //   treeSectionStructure.setCellRenderer(sectionTreeRender);
        // treeSectionTreeMetadata.setCellRenderer(new treeViewPrettySectionsTreeCellRenderer());
        treeSectionTreeMetadata.setShowsRootHandles(true);
        ComponentUI treeUI = treeSectionTreeMetadata.getUI();
        if (treeUI instanceof BasicTreeUI) {
            ((BasicTreeUI) treeUI).setExpandedIcon(minusIcon);
            ((BasicTreeUI) treeUI).setCollapsedIcon(plusIcon);
        }
        this.treeSectionTreeMetadata.addMouseListener(new treeDocStructureTreeMouseListener());
        DocumentSectionFriendlyAdapterDefaultTreeModel model = DocumentSectionFriendlyTreeModelProvider.create();//_without_subscription();
        this.treeSectionTreeMetadata.setModel(model);
        updateTableMetadataModel(ROOT_SECTION);
        CommonTreeFunctions.expandAll(treeSectionTreeMetadata);
    }

    private void refreshTree() {
        BungeniBNode newRootNode = DocumentSectionProvider.getNewFriendlyTree().getFirstRoot();
        DocumentSectionFriendlyAdapterDefaultTreeModel model = (DocumentSectionFriendlyAdapterDefaultTreeModel) this.treeSectionTreeMetadata.getModel();
        DefaultMutableTreeNode mnode = (DefaultMutableTreeNode) model.getRoot();
        BungeniBNode origNode = (BungeniBNode) mnode.getUserObject();
        BungeniTreeRefactorTree refTree = new BungeniTreeRefactorTree(model, origNode, newRootNode);
        refTree.doMerge();
    }

    /*
    private void refreshTree(){
    //TreePath[] selections = this.treeSectionTreeMetadata.getSelectionPaths();
    captureTreeState();
    //Enumeration expandedNodes = this.treeSectionTreeMetadata.getExpandedDescendants(new TreePath(this.treeSectionTreeMetadata.getModel().getRoot()));
    refreshTreeModel();
    CommonTreeFunctions.expandAll(treeSectionTreeMetadata);
    restoreTreeState();
    }
     */
    private void captureTreeState() {
        TreePath selPath = this.treeSectionTreeMetadata.getSelectionPath();
        if (selPath != null) {
            this.m_savedTreeState.clear();
            this.m_savedTreeState.add(selPath.getLastPathComponent().toString());
            TreePath parent = selPath.getParentPath();
            while (parent != null) {
                m_savedTreeState.add(parent.getLastPathComponent().toString());
                parent = parent.getParentPath();
            }
            Collections.reverse(m_savedTreeState);
        }
    }

    private void restoreTreeState() {
        if (this.m_savedTreeState.size() > 0) {
            String[] treeState = m_savedTreeState.toArray(new String[m_savedTreeState.size()]);
            TreePath foundPath = findByName(this.treeSectionTreeMetadata, treeState);
            if (foundPath != null) {
                this.treeSectionTreeMetadata.setSelectionPath(foundPath);
                this.treeSectionTreeMetadata.scrollPathToVisible(foundPath);
            }
        }
    }

    // Finds the path in tree as specified by the array of names. The names array is a
    // sequence of names where names[0] is the root and names[i] is a child of names[i-1].
    // Comparison is done using String.equals(). Returns null if not found.
    public TreePath findByName(JTree tree, String[] names) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        return findNode(tree, new TreePath(root), names, 0, true);
    }

    private TreePath findNode(JTree tree, TreePath parent, Object[] nodes, int depth, boolean byName) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        Object o = node;

        // If by name, convert node to a string
        if (byName) {
            o = o.toString();
        }

        // If equal, go down the branch
        if (o.equals(nodes[depth])) {
            // If at end, return match
            if (depth == nodes.length - 1) {
                return parent;
            }

            // Traverse children
            if (node.getChildCount() >= 0) {
                for (Enumeration e = node.children(); e.hasMoreElements();) {
                    TreeNode n = (TreeNode) e.nextElement();
                    TreePath path = parent.pathByAddingChild(n);
                    TreePath result = findNode(tree, path, nodes, depth + 1, byName);
                    // Found a match
                    if (result != null) {
                        return result;
                    }
                }
            }
        }

        // No match at this branch
        return null;
    }

    public void updateTableMetadataModel(String sectionName) {
        SectionMetadataLoad sectionMetadataTableModel = new SectionMetadataLoad(ooDocument, sectionName);
        this.tblSectionViewMetadata.setModel(sectionMetadataTableModel);
        this.tblSectionViewMetadata.setFont(new Font("Tahoma", Font.PLAIN, 11));
    }

    class treeDocStructureTreeMouseListener implements MouseListener {

        treeDocStructureTreeMouseListener() {
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent evt) {
            int selRow = treeSectionTreeMetadata.getRowForLocation(evt.getX(), evt.getY());
            TreePath selPath = treeSectionTreeMetadata.getPathForLocation(evt.getX(), evt.getY());
            if (selRow != -1) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                m_selectedSection = (BungeniBNode) node.getUserObject();
                updateTableMetadataModel(m_selectedSection.getName());

                // (rm, feb 2012) -check if the selected node is
                // an editable section
                // SectionMetadataEditor accepts
                enableSectionMetadataEditButton(m_selectedSection.getName());
                return;
            }
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblSectionMetadata = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeSectionTreeMetadata = new javax.swing.JTree();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSectionViewMetadata = new javax.swing.JTable();
        lblSectionTreeMetadataView = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        btnMetadataEdit = new javax.swing.JButton();

        setFont(new java.awt.Font("DejaVu Sans", 0, 11));

        lblSectionMetadata.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/panels/loadable/Bundle"); // NOI18N
        lblSectionMetadata.setText(bundle.getString("sectionTreeMetadataPanel.lblSectionMetadata.text")); // NOI18N

        treeSectionTreeMetadata.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        jScrollPane1.setViewportView(treeSectionTreeMetadata);

        tblSectionViewMetadata.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        tblSectionViewMetadata.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tblSectionViewMetadata);

        lblSectionTreeMetadataView.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblSectionTreeMetadataView.setText(bundle.getString("sectionTreeMetadataPanel.lblSectionTreeMetadataView.text")); // NOI18N

        btnRefresh.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnRefresh.setText(bundle.getString("sectionTreeMetadataPanel.btnRefresh.text")); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnMetadataEdit.setText(bundle.getString("sectionTreeMetadataPanel.btnMetadataEdit.text")); // NOI18N
        btnMetadataEdit.setEnabled(false);
        btnMetadataEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMetadataEditActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .add(lblSectionMetadata, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 196, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(lblSectionTreeMetadataView)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 7, Short.MAX_VALUE))
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE))
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .add(btnMetadataEdit)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnRefresh)
                .add(64, 64, 64))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(lblSectionTreeMetadataView)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 278, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnRefresh, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnMetadataEdit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(lblSectionMetadata)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleDescription("Section Metadata");
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
// TODO add your handling code here:
        refreshTree();
    }//GEN-LAST:event_btnRefreshActionPerformed


    private void launchSectionMetadataEditor(String currentSectionName ) {
        //get the section metadata
        HashMap<String, String> currentSec = ooDocument.getSectionMetadataAttributes(currentSectionName);
        //check if the section has a metadata editor
        if (currentSec.containsKey(SectionMetadataEditor.MetaEditor)) {
            //parse the metadata editor string into a subAction object
            String metaEditorString = currentSec.get(SectionMetadataEditor.MetaEditor);
            BungeniToolbarTargetProcessor btp = new BungeniToolbarTargetProcessor(metaEditorString);
            String documentType = BungeniEditorPropertiesHelper.getCurrentDocType();
            Element subActionElement = null;
            try {
                //create the subAction object out of the settings metadata
                // the actionName is indicated as
                // << toolbarAction.actionName >> (rm, feb 2012) - this is now deprecated! ActionNames are specified as
                // <<actionName>>
                // split the string to obtain the action name                       
                subActionElement = DocumentActionsReader.getInstance().getDocumentActionByName(btp.getSubActionName());

                if (subActionElement != null) {
                    toolbarAction subActionObj = new toolbarAction(subActionElement);
                    if (!CommonStringFunctions.emptyOrNull(btp.getActionValue())) {
                        subActionObj.setActionValue(btp.getActionValue());
                    }
                    subActionObj.setSelectorDialogMode(SelectorDialogModes.TEXT_EDIT);
                    IEditorActionEvent event = EditorActionFactory.getEventClass(subActionObj);
                    if (event != null) {
                        event.doCommand(ooDocument, subActionObj, parentFrame);
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

    /**
     * (rm,feb 2012) - The jEdit button's actionListener launches a dialog that allows
     * a user to edit the section metadata, but only if the section is
     * editable
     * @param evt
     */
    private void btnMetadataEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMetadataEditActionPerformed
        // get the action , parent frame and the document
        Object[][] sectionMetadata;
        HashMap<String, String>  sectionMetadataMap = new HashMap<String, String>();

        String selectedSectionName = m_selectedSection.getName() ;

        launchSectionMetadataEditor(selectedSectionName);
        
        /**
        sectionMetadataMap = ooDocument.getSectionMetadataAttributes(selectedSectionName);
        String sectionAction = null ;
        
        // get the action
       // String sectionAction = null ;
        if (sectionMetadataMap.size() > 0) {
                sectionMetadata = new Object[sectionMetadataMap.size()][];

                // iterate through the section meta tags to determine
                // if metadata is editable
                Iterator metaIterator = sectionMetadataMap.keySet().iterator();

                while (metaIterator.hasNext()) {
                    for (int i = 0; i < sectionMetadataMap.size(); i++) {
                        String metaName = (String) metaIterator.next();

                        if (metaName.equals("hiddenBungeniMetaEditor"))
                        {
                            sectionAction = sectionMetadataMap.get(metaName);                            
                        }
                    }
            }

            // get the action name
            if( null != sectionAction )
            {
                String [] actionTokens = sectionAction.split("\\.") ;
                String action = actionTokens[1];
                try {
                    //  get the Router object from the action tag in the relevant xml file
                    Element sectionActionElement = DocumentActionsReader.getInstance()
                            .getDocumentActionByName(action);

                    // get the parent frame for the action class
                    String parentFrameElement =  DocumentActionsReader.getInstance()
                            .getRouter(action).getAttribute("dialog").getValue() ;

                    // using introspection to create the frame class
                    // Class actionPanelClass = Class.forName(parentFrameElement);
                    // BaseMetadataContainerPanel actionPanel =
                    //        (BaseMetadataContainerPanel) actionPanelClass.newInstance();
                    
                    // display the editor dialog
                    toolbarAction actionClass =  new toolbarAction(sectionActionElement) ;

                    // set the dialog mode
                    actionClass.setSelectorDialogMode(SelectorDialogModes.TEXT_EDIT);

                    // launch the dialog
                    CommonRouterActions.displaySelectorDialog(actionClass,
                            parentFrame, ooDocument ) ;

                } catch (Exception ex) {
                    log.error(ex) ;
                }

            }
         
        }

        
        // display the dialog to allow the editing of the settings for
        // the selected section on the tree node
     **/
    }//GEN-LAST:event_btnMetadataEditActionPerformed

    /** 
     * (rm, feb 2012) - This method determines whether the editSectionMetadata
     * should be enabled. The 'Edit' Button is enabled where
     * a section has been selected
     *
     * The 'Edit' Button is activated when an item in the
     * section tree is selected or a selection made in the oOo doc
     */
    private boolean enableSectionMetadataEditButton(String sectionName)
    {
        // disable the 'Edit' JButton
        btnMetadataEdit.setEnabled(false);
        
        // will store all the section meta data
        HashMap<String, String>  sectionMetadataMap = new HashMap<String, String>();
        Object[][] sectionMetadata;
        
        // get the key=>value to the hashmap
        String[] column_names = { "Metadata", "Value" };

        // get the meta atts to map
        sectionMetadataMap = ooDocument.getSectionMetadataAttributes(sectionName);

        if (sectionMetadataMap.size() > 0) {
                sectionMetadata = new Object[sectionMetadataMap.size()][column_names.length];
                
                // iterate through the section meta tags to determine
                // if metadata is editable
                Iterator metaIterator = sectionMetadataMap.keySet().iterator();

                while (metaIterator.hasNext()) {
                    for (int i = 0; i < sectionMetadataMap.size(); i++) {
                        String metaName = (String) metaIterator.next();

                        // rm, feb 2012 - "hiddenBungeniMetaEditable" replaced with static ref
                        if (metaName.equals(SectionMetadataEditor.MetaEditableFlag))
                        {
                            String sectionAtt = sectionMetadataMap.get(metaName) ;

                            // check to see if the attribute has been set to
                            // true or not
                            if(sectionAtt.equals("true"))
                            {
                                btnMetadataEdit.setEnabled(true);
                            }
                        }                        
                    }
                }
            }
                
        return false ;
    }

    @Override
    public void initialize() {
        super.initialize();
        //initTableDocumentMetadata();    
        initTreeStructure();
    }

    public void refreshPanel() {
        //nothing to do here... the timer refreshes itself....
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMetadataEdit;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblSectionMetadata;
    private javax.swing.JLabel lblSectionTreeMetadataView;
    private javax.swing.JTable tblSectionViewMetadata;
    private javax.swing.JTree treeSectionTreeMetadata;
    // End of variables declaration//GEN-END:variables
}
