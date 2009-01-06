/*
 * SelectSection.java
 *
 * Created on October 3, 2007, 12:59 PM
 */

package org.bungeni.editor.selectors;

import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.text.XTextSection;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.editor.BungeniEditorProperties;
import org.bungeni.editor.BungeniEditorPropertiesHelper;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;
import org.bungeni.utils.CommonTreeFunctions;
import org.bungeni.utils.MessageBox;

/**
 *
 * @author  Administrator
 */
public class SelectSection extends selectorTemplatePanel {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SelectSection.class.getName());
    private DefaultMutableTreeNode sectionRootNode = null;
    private String m_selectedSection = "";
    private String m_selectedActionCommand = "";
    private boolean emptyRootNode = false;
    private boolean cancelClicked = false;
    private String[] m_validParentSections;
    private String m_documentRoot = BungeniEditorPropertiesHelper.getDocumentRoot();
    /** Creates new form SelectSection */
    public SelectSection() {
        initComponents();
    }
    
    public SelectSection(OOComponentHelper doc, JDialog parentDlg,  toolbarAction action) {
        super(doc, parentDlg, action);
        initComponents();
        //make the required query to determine parent sections of this node i.e. where it can be inserted.
        getAllowedLocations();
        initSectionList();
        initTree();
    }
    
    private void initTree(){
        treeSectionStructure.setCellRenderer(new treeSectionStructureCellRenderer());
        treeSectionStructure.addTreeSelectionListener(new treeSectionStructureSelectionListener());
        
    }
     private void initSectionList() {
         initSectionsArray();   
         treeSectionStructure.setModel(new DefaultTreeModel(sectionRootNode));
         //-tree-deprecated--CommonTreeFunctions.expandAll(treeSectionStructure, true);
         CommonTreeFunctions.expandAll(treeSectionStructure);
      }
    
     private void getAllowedLocations(){
       try {
       String actionNamingPattern =  theAction.action_naming_convention();
       String actionName = theAction.action_name();
       log.debug("getAllowedLocations : connecting...");
        dbSettings.Connect();
        log.debug("getAllowedLocations : getting Query Results");
        QueryResults qr = dbSettings.QueryResults(SettingsQueryFactory.Q_GET_SECTION_PARENT(theAction.action_name()));
        log.debug("getAllowedLocations : ending connection...");
        dbSettings.EndConnect();
        
        if (qr != null ) {
              log.debug("getAllowedLocations : queryResults were null...");
              if (qr.hasResults()) {
                log.debug("getAllowedLocations: before getting result");  
                m_validParentSections = qr.getSingleColumnResult("ACTION_NAMING_CONVENTION");
                log.debug("getAllowedLocations: after getting results");
              }
        } else {
            //no returned parent action means this a root action
            m_validParentSections = new String[1];
            m_validParentSections[0] = m_documentRoot;
        }
       } catch (Exception ex) {
           log.error("getAllowedSections:" + ex.getMessage());
       }
     }
     
     private boolean isAllowedLocation(String sectionName) {
         log.debug("isAllowedLocation : section = " + sectionName);
         for (int i=0; i < m_validParentSections.length; i++) {
             log.debug ("validParentSections = " + m_validParentSections[i]);
             if (sectionName.startsWith(m_validParentSections[i]) /*section name matches a valid parent*/
                        && 
                     (sectionName.lastIndexOf("-") == -1)) /* and section name doest not contain as "-" */ {
                 return true;
             }
         }
         log.debug("isAllowedLocation: return false");
         return false;
     }
    private void initSectionsArray() {
        try {
            if (!ooDocument.isXComponentValid()) return;
            
            treeSectionStructure.removeAll();
            if (!ooDocument.getTextSections().hasByName(m_documentRoot)) {
                log.debug("no root section found");
                return;
            }
            Object rootSection = ooDocument.getTextSections().getByName(m_documentRoot);
            XTextSection theSection = ooQueryInterface.XTextSection(rootSection);
            if (theSection.getChildSections().length == 0) {
                //root is empty and has no children. 
                //set empty status 
                this.emptyRootNode = true;
            }
            sectionRootNode = new DefaultMutableTreeNode(new String(m_documentRoot));
            
            recurseSections (theSection, sectionRootNode);
            
            //-tree-deprecated--CommonTreeFunctions.expandAll(treeSectionStructure, true);
            CommonTreeFunctions.expandAll(treeSectionStructure);
            
        } catch (NoSuchElementException ex) {
            log.error(ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error(ex.getMessage());
        }
    }
    
    
    private void recurseSections (XTextSection theSection, DefaultMutableTreeNode node ) {
        try {
        //recurse children
        XTextSection[] sections = theSection.getChildSections();
        if (sections != null ) {
            if (sections.length > 0 ) {
                //start from last index and go to first
                for (int nSection = sections.length - 1 ; nSection >=0 ; nSection--) {
                    log.debug ("section name = "+sections[nSection] );
                    //get the name for the section and add it to the root node.
                    XPropertySet childSet = ooQueryInterface.XPropertySet(sections[nSection]);
                    String childSectionName = (String) childSet.getPropertyValue("LinkDisplayName");
                    if (!childSectionName.trim().equals(theAction.action_naming_convention())) {
                     DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(childSectionName);
                     node.add(newNode);
                     recurseSections (sections[nSection], newNode);
                    }
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
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        grpSelectSection = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeSectionStructure = new javax.swing.JTree();
        btnApply = new javax.swing.JButton();
        rbtnAfter = new javax.swing.JRadioButton();
        rbtnInside = new javax.swing.JRadioButton();
        lblMessage = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();

        jScrollPane1.setViewportView(treeSectionStructure);

        btnApply.setText("Apply");
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyActionPerformed(evt);
            }
        });

        grpSelectSection.add(rbtnAfter);
        rbtnAfter.setSelected(true);
        rbtnAfter.setText("After Section");
        rbtnAfter.setActionCommand("AFTER_SECTION");
        rbtnAfter.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rbtnAfter.setMargin(new java.awt.Insets(0, 0, 0, 0));

        grpSelectSection.add(rbtnInside);
        rbtnInside.setText("Inside Section");
        rbtnInside.setActionCommand("INSIDE_SECTION");
        rbtnInside.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rbtnInside.setMargin(new java.awt.Insets(0, 0, 0, 0));

        lblMessage.setText("<html><p>Select a Section from the Tree on the left, \nand then select the position where you would like to \n add the new section.</p></html>");

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 228, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(btnCancel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(btnApply, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(14, 14, 14)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(rbtnAfter, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                            .add(lblMessage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                            .add(rbtnInside, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 115, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(lblMessage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                        .add(14, 14, 14)
                        .add(rbtnAfter)
                        .add(20, 20, 20)
                        .add(rbtnInside)
                        .add(40, 40, 40)
                        .add(btnApply)
                        .add(13, 13, 13)
                        .add(btnCancel))
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 230, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
// TODO add your handling code here:
        cancelClicked = true;
        parent.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyActionPerformed
// TODO add your handling code here:
       TreePath selectionPath =  treeSectionStructure.getSelectionPath();
       if (selectionPath != null ) {
           DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
           m_selectedSection = (String) selectedNode.getUserObject();
           m_selectedActionCommand = grpSelectSection.getSelection().getActionCommand();
           parent.dispose();
           return;
       } else {
           MessageBox.OK(parent, "You must select a section!");
           return;
       }
    }//GEN-LAST:event_btnApplyActionPerformed
   
    public String getSelectedActionCommand() {
        return this.m_selectedActionCommand;
    }
    
    public String getSelectedSection() {
        return this.m_selectedSection;
    }
    
    public boolean isCancelClicked() {
        return cancelClicked;
    }
    
    public static boolean Launchable(OOComponentHelper ooDoc) {
        //launchable only when the root section has children
        if (ooDoc.getTextSections().getElementNames().length > 1 ) {
            if (ooDoc.getTextSections().hasByName(BungeniEditorPropertiesHelper.getDocumentRoot())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    
    public static JDialog Launch(OOComponentHelper ooDoc, toolbarAction action) {
          JDialog selectSectionDlg;
             selectSectionDlg = new JDialog();
             selectSectionDlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
             //initDebaterecord.setPreferredSize(new Dimension(420, 300));
             SelectSection panel = new SelectSection(ooDoc, 
                     selectSectionDlg, action);
             selectSectionDlg.getContentPane().add(panel);
             selectSectionDlg.setTitle("Select Section");
             selectSectionDlg.pack();
             selectSectionDlg.setLocationRelativeTo(null);
             selectSectionDlg.setModal(true);
             selectSectionDlg.setVisible(true);
             //selectSectionDlg.setAlwaysOnTop(true);  
             
             return selectSectionDlg;
    }
    
    public boolean isRootNodeEmpty() {
        return this.emptyRootNode;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private javax.swing.ButtonGroup grpSelectSection;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JRadioButton rbtnAfter;
    private javax.swing.JRadioButton rbtnInside;
    private javax.swing.JTree treeSectionStructure;
    // End of variables declaration//GEN-END:variables
   static boolean rendered = false;
   class treeSectionStructureCellRenderer extends JLabel implements TreeCellRenderer {
       
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
          
            setText(value.toString( ));
            this.setBorder( BorderFactory.createRaisedBevelBorder());
            if (selected) 
                  setOpaque(false);
           else 
                  setOpaque(true);

            if (value instanceof DefaultMutableTreeNode) {
                  DefaultMutableTreeNode uo = (DefaultMutableTreeNode)value;
                  String act = (String) uo.getUserObject();
                  //check if the action name matches with the allowed sections.
                 
                  if (isAllowedLocation(act))  {
                      setBackground(new java.awt.Color(0,200,0));
                      if (!rendered) {
                        tree.setSelectionPath(tree.getPathForRow(row));
                        rendered = true;
                      }
                 }
                  else 
                      setBackground(new java.awt.Color(200, 0, 0));
                  //if (act.action_name())
                }
                return this;    
         
        }
       
   }
   
   static boolean wasSelected = false;
   class treeSectionStructureSelectionListener implements TreeSelectionListener {
        DefaultMutableTreeNode selNode;
        String selectedNodeName;
        String oldSelectedNodeName;
       public void valueChanged(TreeSelectionEvent e) {
            TreePath selectionPath = e.getNewLeadSelectionPath();
            TreePath oldSelectionPath = e.getOldLeadSelectionPath();
            if (selectionPath != null ) {
              selNode = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
              selectedNodeName = (String) selNode.getUserObject();
              if (selectedNodeName.equals(BungeniEditorProperties.getEditorProperty("root:debaterecord"))) {
                  wasSelected = rbtnAfter.isSelected();
                  if (wasSelected)
                      rbtnInside.setSelected(true);
                  rbtnAfter.setEnabled(false);
              } else {
                  rbtnAfter.setEnabled(true);
                  //if (wasSelected )
                  //    rbtnAfter.setSelected(true);
                   if (oldSelectionPath != null) {
                        oldSelectedNodeName = (String) ((DefaultMutableTreeNode)oldSelectionPath.getLastPathComponent()).getUserObject();
                        if (oldSelectedNodeName.equals(BungeniEditorProperties.getEditorProperty("root:debaterecord"))) {
                            //previous selection was a root node
                            if (wasSelected)
                                rbtnAfter.setSelected(true);
                        }
                  }
            }
        }
    }
   }
}
