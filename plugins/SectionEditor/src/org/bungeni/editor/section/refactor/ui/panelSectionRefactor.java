/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * panelSectionRefactor.java
 *
 * Created on Jan 30, 2009, 10:00:03 AM
 */

package org.bungeni.editor.section.refactor.ui;

import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TreeSelectionEvent;
import org.bungeni.editor.section.refactor.xml.JDomOdfDomBridge;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionListener;
import org.bungeni.editor.section.refactor.changelog.ChangeLog;
import org.bungeni.editor.section.refactor.changelog.ChangeLogTableModel;
import org.bungeni.editor.section.refactor.xml.OdfJDomElement;
import org.bungeni.editor.section.refactor.xml.OdfRefactor;
import org.jdom.Document;
import org.openoffice.odf.doc.OdfDocument;
import org.openoffice.odf.doc.OdfFileDom;
import org.openoffice.odf.doc.element.text.OdfSection;
import org.w3c.dom.Node;

/**
 *
 * @author ashok
 */
public class panelSectionRefactor extends javax.swing.JPanel {
    String pathToFile = "";
    OdfDocument odfDocument = null;
    OdfFileDom contentDom = null;
    OdfJDomTreeModel treeModel = null;
    JDomOdfDomBridge jdofBridge = null;
    OdfRefactor refactorer = null;
    JFrame parentFrame;
     dialogCommitDocChange changePanel = new dialogCommitDocChange();
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(panelSectionRefactor.class.getName());

    /** Creates new form panelSectionRefactor */
    public panelSectionRefactor() {
        initComponents();
    }

    public panelSectionRefactor(String fileName) {
        initComponents();
        this.pathToFile = fileName;
        //create a JDom dom 
        Document jdomDoc = loadDocument();
        //setup ODF refactorer
        refactorer = new OdfRefactor(fileName);
       //setup the jtree ui
        setupTree(jdomDoc);
        setupTables();
    }

    public void setParentFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }
    
    public void moveAfter(String sectionFrom, String sectionTo) {
        try {
            //
            int nSelect = confirmChange(sectionFrom, sectionTo);
            if (JOptionPane.YES_OPTION == nSelect ) {
                String commitLog = this.changePanel.getCommitLog();
                refactorer.updateCommitLog(commitLog);
                refactorer.moveSectionAfter(sectionFrom, sectionTo);
                refactorer.saveDocument();
                updatePanelUI();
            }
        } catch (Exception ex) {
            log.error("moveAfter : from = " + sectionFrom + " to : " + sectionTo + ex.getMessage());
        }
    }

    public void moveBefore(String sectionFrom, String sectionTo) {
        try {
            int nSelect = confirmChange(sectionFrom, sectionTo);
            if (JOptionPane.YES_OPTION == nSelect ) {
                String commitLog = this.changePanel.getCommitLog();
                refactorer.updateCommitLog(commitLog);
                refactorer.moveSectionBefore(sectionFrom, sectionTo);
                refactorer.saveDocument();
                updatePanelUI();
                //now we have to reload the document model
            }
        } catch (Exception ex) {
            log.error("moveBefore : from = " + sectionFrom + " to : " + sectionTo + ex.getMessage());
        }
    }

    private int confirmChange(String fromSection, String toSection) {
        final JDialog dialog = new JDialog(this.parentFrame,
                                                 "Confirm Change",
                                                 true);
        changePanel.setParentDialog(dialog);  
        dialog.getContentPane().add(this.changePanel) ;
                    dialog.setDefaultCloseOperation(
                        JDialog.DISPOSE_ON_CLOSE);
          dialog.pack();
          dialog.setVisible(true);
          //now the dialog has closed.
         return changePanel.getCommitYesNo();
    }
    
    /**
     * Refreshes the UI panel compoennts - JTree and JTables
     */
    private void updatePanelUI(){
        //update section dom tree
        Document updJdomDoc = loadDocument();
        treeModel =  new OdfJDomTreeModel(new OdfJDomTreeNode((OdfJDomElement)updJdomDoc.getRootElement()));
        this.treeSectionView.setModel(treeModel);
        //update change log table
        tblChangeHistory.setRowSelectionAllowed(false);
        tblChangeHistory.setModel(new ChangeLogTableModel(this.pathToFile));
        tblChangeHistory.setRowSelectionAllowed(true);
    }
    
    
    private void setupTree(Document jdomDocument) {
          treeModel = new OdfJDomTreeModel(new OdfJDomTreeNode((OdfJDomElement)jdomDocument.getRootElement()));
         
          //set the tree model
          this.treeSectionView.setModel(treeModel);
          this.treeSectionView.setCellRenderer(new OdfJDomTreeCellRenderer());
          //enable drag and drop
          OdfJDomTreeNodeTransferHandler handler = new OdfJDomTreeNodeTransferHandler(this);
         // handler.setPanelForm(this);
          this.treeSectionView.setTransferHandler(handler);
          this.treeSectionView.setDragEnabled(true);
          this.treeSectionView.setDropTarget(new OdfJDomTreeDropTarget(handler));
          this.treeSectionView.addTreeSelectionListener(new treeSectionViewMouseListener());
     }

    private void setupTables(){
        //initialize section metadata table with blank model 
        tblSectionMeta.setModel(new OdfSectionMetaTableModel(new ArrayList<Node>(0)));
        //initialize change log table model
        tblChangeHistory.setModel(new ChangeLogTableModel(this.pathToFile));
        tblChangeHistory.setRowSelectionAllowed(true);
        tblChangeHistory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblChangeHistory.getSelectionModel().addListSelectionListener(new tblChangeHistoryRowSelectListener());

    }
    
    class tblChangeHistoryRowSelectListener implements ListSelectionListener{
        public void valueChanged(ListSelectionEvent arg0) {
            int nSelected = tblChangeHistory.getSelectedRow();
            if (nSelected != -1) {
                ChangeLogTableModel model = ((ChangeLogTableModel)tblChangeHistory.getModel());
                ChangeLog clObj = model.getModelObjectAt(nSelected);
                if (clObj != null) {
                    txtChangeLog.setText(clObj.getCommitLog());
                }
            }
        }
    }
    
    class treeSectionViewMouseListener implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent eEvent) {
            Object selectedPath = treeSectionView.getLastSelectedPathComponent();
            if (selectedPath != null ) {
                OdfJDomTreeNode treeNode = (OdfJDomTreeNode) selectedPath; 
                OdfSection matchedSection = treeNode.node.getTextSection();
                ArrayList<Node> nodeList = jdofBridge.getBungeniMetadataAttributes(matchedSection);
                OdfSectionMetaTableModel model = (OdfSectionMetaTableModel) tblSectionMeta.getModel();
                model.resetModel(nodeList);
                //reset table model...
            }
        }
   
    }
    private Document loadDocument() {
        Document jdomDocument = null;
        try {
            //load the odf document
            odfDocument = OdfDocument.loadDocument(pathToFile);
            //convert the odfdom tree to a filtered JDom tree of text:section-s
            jdofBridge = new JDomOdfDomBridge(odfDocument);
            jdofBridge.filterOdfDoc();
            jdomDocument = jdofBridge.getJDomDocument();

        } catch (Exception ex) {
            log.error(ex.getMessage());
        } finally {
            return jdomDocument;
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

        scrollSectionView = new javax.swing.JScrollPane();
        treeSectionView = new javax.swing.JTree();
        btnCancel = new javax.swing.JButton();
        tabSectionRefactor = new javax.swing.JTabbedPane();
        panelSectionMeta = new javax.swing.JPanel();
        scrollSectionMeta = new javax.swing.JScrollPane();
        tblSectionMeta = new javax.swing.JTable();
        panelChangeHistory = new javax.swing.JPanel();
        scrollChangeLog = new javax.swing.JScrollPane();
        txtChangeLog = new javax.swing.JTextArea();
        btnRevertToSelectedVersion = new javax.swing.JButton();
        scrollChangeHistory = new javax.swing.JScrollPane();
        tblChangeHistory = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        scrollSectionView.setViewportView(treeSectionView);

        btnCancel.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnCancel.setText("Cancel");

        tabSectionRefactor.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        tblSectionMeta.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        tblSectionMeta.setModel(new javax.swing.table.DefaultTableModel(
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
        scrollSectionMeta.setViewportView(tblSectionMeta);

        org.jdesktop.layout.GroupLayout panelSectionMetaLayout = new org.jdesktop.layout.GroupLayout(panelSectionMeta);
        panelSectionMeta.setLayout(panelSectionMetaLayout);
        panelSectionMetaLayout.setHorizontalGroup(
            panelSectionMetaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scrollSectionMeta, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
        );
        panelSectionMetaLayout.setVerticalGroup(
            panelSectionMetaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelSectionMetaLayout.createSequentialGroup()
                .add(scrollSectionMeta, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 210, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );

        tabSectionRefactor.addTab("Section Metadata", panelSectionMeta);

        txtChangeLog.setColumns(20);
        txtChangeLog.setEditable(false);
        txtChangeLog.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtChangeLog.setRows(5);
        scrollChangeLog.setViewportView(txtChangeLog);

        btnRevertToSelectedVersion.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnRevertToSelectedVersion.setText("Revert to Selected Version");

        tblChangeHistory.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        tblChangeHistory.setModel(new javax.swing.table.DefaultTableModel(
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
        scrollChangeHistory.setViewportView(tblChangeHistory);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        jLabel1.setText("Change Notes");

        org.jdesktop.layout.GroupLayout panelChangeHistoryLayout = new org.jdesktop.layout.GroupLayout(panelChangeHistory);
        panelChangeHistory.setLayout(panelChangeHistoryLayout);
        panelChangeHistoryLayout.setHorizontalGroup(
            panelChangeHistoryLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelChangeHistoryLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelChangeHistoryLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(scrollChangeLog, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                    .add(panelChangeHistoryLayout.createSequentialGroup()
                        .add(scrollChangeHistory, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                        .addContainerGap())
                    .add(panelChangeHistoryLayout.createSequentialGroup()
                        .add(jLabel1)
                        .addContainerGap(256, Short.MAX_VALUE))
                    .add(panelChangeHistoryLayout.createSequentialGroup()
                        .add(39, 39, 39)
                        .add(btnRevertToSelectedVersion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 231, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        panelChangeHistoryLayout.setVerticalGroup(
            panelChangeHistoryLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelChangeHistoryLayout.createSequentialGroup()
                .addContainerGap()
                .add(scrollChangeHistory, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel1)
                .add(6, 6, 6)
                .add(scrollChangeLog, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 79, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(btnRevertToSelectedVersion)
                .add(28, 28, 28))
        );

        tabSectionRefactor.addTab("Change History", panelChangeHistory);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(scrollSectionView, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(btnCancel)
                        .add(146, 146, 146))
                    .add(tabSectionRefactor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(tabSectionRefactor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 321, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 56, Short.MAX_VALUE)
                .add(btnCancel)
                .addContainerGap())
            .add(scrollSectionView, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnRevertToSelectedVersion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel panelChangeHistory;
    private javax.swing.JPanel panelSectionMeta;
    private javax.swing.JScrollPane scrollChangeHistory;
    private javax.swing.JScrollPane scrollChangeLog;
    private javax.swing.JScrollPane scrollSectionMeta;
    private javax.swing.JScrollPane scrollSectionView;
    private javax.swing.JTabbedPane tabSectionRefactor;
    private javax.swing.JTable tblChangeHistory;
    private javax.swing.JTable tblSectionMeta;
    private javax.swing.JTree treeSectionView;
    private javax.swing.JTextArea txtChangeLog;
    // End of variables declaration//GEN-END:variables


    public static void main(String[] args) {
        JFrame mframe = new JFrame("Restructure Document");
        String fileName = "/home/undesa/Desktop/ken_bill_2009_1_10_eng_main.odt";
        panelSectionRefactor panel = new panelSectionRefactor(fileName);
        panel.setParentFrame(mframe);
        mframe.add(panel);
        mframe.setSize(500, 365);
        mframe.pack();
        mframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mframe.setVisible(true);
    }
}
