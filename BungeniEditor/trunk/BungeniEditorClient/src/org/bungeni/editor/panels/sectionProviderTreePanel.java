/*
 * documentMetadataPanel.java
 *
 * Created on May 15, 2008, 11:47 AM
 */

package org.bungeni.editor.panels;

import com.sun.star.beans.XPropertySet;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XNamed;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.text.XTextSection;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.editor.dialogs.metadatapanel.SectionMetadataLoad;
import org.bungeni.editor.panels.loadable.sectionTreeMetadataPanel;
import org.bungeni.editor.providers.DocumentSectionTreeModelProvider;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;
import org.bungeni.extutils.CommonTreeFunctions;

/**
 *
 * @author  Administrator
 */
public class sectionProviderTreePanel extends javax.swing.JPanel {
    private OOComponentHelper ooDocument;
    private JFrame parentFrame;
    private DefaultMutableTreeNode sectionRootNode = null;
    private boolean emptyRootNode= false;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(sectionTreeMetadataPanel.class.getName());
    private static final String ROOT_SECTION = BungeniEditorPropertiesHelper.getDocumentRoot();
    private ArrayList<String> m_savedTreeState = new ArrayList<String>();
    
    private Timer sectionMetadataRefreshTimer;
    /** Creates new form documentMetadataPanel */
    public sectionProviderTreePanel() {
        initComponents();
    }
    
   public sectionProviderTreePanel(OOComponentHelper ooDocument, JFrame parentFrame){
         this.parentFrame=parentFrame;
         this.ooDocument=ooDocument;
         init();
     }
    
    private void init() {
        initComponents();
        initTableDocumentMetadata();    
        //initTimer();
    }
    
    private void initTableDocumentMetadata() {
        // initSectionsArray();   
         this.treeSectionTreeMetadata.setModel(DocumentSectionTreeModelProvider.create());
         this.treeSectionTreeMetadata.addMouseListener(new treeDocStructureTreeMouseListener());
         updateTableMetadataModel(ROOT_SECTION);
         //-tree-deprecated--CommonTreeFunctions.expandAll(treeSectionStructure, true);
         CommonTreeFunctions.expandAll(treeSectionTreeMetadata);
    }
    
      private void initSectionsArray() {
        final OOComponentHelper ooDoc;
        synchronized(ooDocument) {
            ooDoc  = ooDocument;
        }
        try {
            if (!ooDoc.isXComponentValid()) return;
            if (!ooDoc.getTextSections().hasByName(ROOT_SECTION)) {
                log.debug("no root section found");
                return;
            }
            Object rootSection = ooDoc.getTextSections().getByName(ROOT_SECTION);
            XTextSection theSection = ooQueryInterface.XTextSection(rootSection);
            if (theSection.getChildSections().length == 0) {
                //root is empty and has no children. 
                //set empty status 
                this.emptyRootNode = true;
            }
            sectionRootNode = new DefaultMutableTreeNode(new String(ROOT_SECTION));
            recurseSections (theSection, sectionRootNode);
        } catch (NoSuchElementException ex) {
            log.error("recurseSections :" + ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error("recurseSections :" + ex.getMessage());
        }
    }
    
    
    private void recurseSections (XTextSection theSection, DefaultMutableTreeNode node ) {
        //recurse children
        XTextSection[] sections = theSection.getChildSections();
        if (sections != null ) {
            if (sections.length > 0 ) {
                //start from last index and go to first
                for (int nSection = sections.length - 1 ; nSection >=0 ; nSection--) {
                    log.debug ("section name = "+sections[nSection] );
                    //get the name for the section and add it to the root node.
                    XPropertySet childSet = ooQueryInterface.XPropertySet(sections[nSection]);
                    XNamed xNamedSection = ooQueryInterface.XNamed(sections[nSection]);
                    String childSectionName = xNamedSection.getName();
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(childSectionName);
                    node.add(newNode);
                    recurseSections (sections[nSection], newNode);
                }
            } else 
                return;
        } else 
            return;

    }
    
    private synchronized void initTimer(){
          sectionMetadataRefreshTimer= new Timer(4000, new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                //refreshSectionMetadataTreeTable();
                 // captureTreeState();
                  refreshTree();
                 // restoreTreeState();
              }
           });
           sectionMetadataRefreshTimer.start();
    }

   private void refreshTree(){
       //TreePath[] selections = this.treeSectionTreeMetadata.getSelectionPaths();
       captureTreeState();
       //Enumeration expandedNodes = this.treeSectionTreeMetadata.getExpandedDescendants(new TreePath(this.treeSectionTreeMetadata.getModel().getRoot()));
       refreshTreeModel();
       /*
       if (expandedNodes != null  ) {
           log.debug("refreshTree: expandedNodes was NOT null");
           while (expandedNodes.hasMoreElements()) {
               this.treeSectionTreeMetadata.expandPath((TreePath)expandedNodes.nextElement());
           }
       } else
           log.debug("refreshTree: expandedNodes was null");
       */
       CommonTreeFunctions.expandAll(treeSectionTreeMetadata);    
       restoreTreeState();
   }

   private void captureTreeState(){
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
        if (this.m_savedTreeState.size() > 0 ) {
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
        TreeNode root = (TreeNode)tree.getModel().getRoot();
        return findNode(tree, new TreePath(root), names, 0, true);
    }
    private TreePath findNode(JTree tree, TreePath parent, Object[] nodes, int depth, boolean byName) {
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        Object o = node;
    
        // If by name, convert node to a string
        if (byName) {
            o = o.toString();
        }
    
        // If equal, go down the branch
        if (o.equals(nodes[depth])) {
            // If at end, return match
            if (depth == nodes.length-1) {
                return parent;
            }
    
            // Traverse children
            if (node.getChildCount() >= 0) {
                for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                    TreeNode n = (TreeNode)e.nextElement();
                    TreePath path = parent.pathByAddingChild(n);
                    TreePath result = findNode(tree, path, nodes, depth+1, byName);
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
   
   private void refreshTreeModel(){
         initSectionsArray();   
         ((DefaultTreeModel)this.treeSectionTreeMetadata.getModel()).setRoot(sectionRootNode);
         this.treeSectionTreeMetadata.setModel(this.treeSectionTreeMetadata.getModel());
         //this.treeSectionTreeMetadata.setModel(new DefaultTreeModel(sectionRootNode));
   }

   public void updateTableMetadataModel(String sectionName){
          SectionMetadataLoad sectionMetadataTableModel = new SectionMetadataLoad(ooDocument,sectionName);
          this.tblSectionViewMetadata.setModel(sectionMetadataTableModel);
          this.tblSectionViewMetadata.setFont(new Font("Tahoma", Font.PLAIN, 11));   
   }
   
   private String m_selectedSection = "";
   
  class treeDocStructureTreeMouseListener implements MouseListener {
       treeDocStructureTreeMouseListener() {
        }
        
        public void mouseClicked(MouseEvent e) {
        }     
        
         public void mousePressed(MouseEvent evt) {
              int selRow = treeSectionTreeMetadata.getRowForLocation(evt.getX(), evt.getY());
                    TreePath selPath = treeSectionTreeMetadata.getPathForLocation(evt.getX(), evt.getY());
                     if (selRow != -1 ) {
                             DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                             String m_selectedSection = (String)node.getUserObject();
                             updateTableMetadataModel(m_selectedSection);
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
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        lblSectionMetadata = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeSectionTreeMetadata = new javax.swing.JTree();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSectionViewMetadata = new javax.swing.JTable();
        lblSectionTreeMetadataView = new javax.swing.JLabel();

        getAccessibleContext().setAccessibleDescription("Provider Metadata");
        lblSectionMetadata.setText("Section Metadata");

        jScrollPane1.setViewportView(treeSectionTreeMetadata);

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

        lblSectionTreeMetadataView.setText("Click on a section to view its Metadata");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblSectionTreeMetadataView)
                    .add(lblSectionMetadata, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 196, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(lblSectionTreeMetadataView)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblSectionMetadata)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblSectionMetadata;
    private javax.swing.JLabel lblSectionTreeMetadataView;
    private javax.swing.JTable tblSectionViewMetadata;
    private javax.swing.JTree treeSectionTreeMetadata;
    // End of variables declaration//GEN-END:variables
    
}
