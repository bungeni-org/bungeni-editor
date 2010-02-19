/*
 * panelTrackChanges.java
 *
 * Created on March 9, 2009, 10:21 AM
 */

package org.bungeni.trackchanges;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import org.bungeni.odfdocument.docinfo.BungeniChangeDocumentsInfo;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper.StructuredChangeType;
import org.bungeni.trackchanges.ui.support.TextAreaRenderer;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.text.OdfTextChangedRegion;
import org.w3c.dom.Element;

/**
 *
 * @author  Ashok Hariharan
 */
public class panelTrackChanges extends javax.swing.JPanel {

    ResourceBundle rBundle = java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle");

    String __TEST_FOLDER__ = "";
    BungeniChangeDocumentsInfo changesInfo = new BungeniChangeDocumentsInfo();
    JFrame parentFrame;
    /** Creates new form panelTrackChanges */
    public panelTrackChanges(JFrame parentFrame) {
        initComponents();
        this.parentFrame = parentFrame;
        __TEST_FOLDER__ ="/Users/ashok/Devel/TrackChanges/netbeansProject/Files";
        loadFilesFromFolder();
        initialize();
    }

    /**
     * Initialize controls with data
     */
    private void initialize() {

        initialize_listBoxes();
        initialize_Tables();

        listMembers.setModel(new DocOwnerListModel());
        listMembers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel lsm = listMembers.getSelectionModel();
        lsm.addListSelectionListener(new ListSelectionListener(){

            public void valueChanged(ListSelectionEvent lse) {
                ListSelectionModel lsm = (ListSelectionModel)lse.getSource();
                if (lse.getValueIsAdjusting()) {
                    return;
                }

                int firstIndex = lse.getFirstIndex();
                int lastIndex = lse.getLastIndex();

                if (lsm.isSelectionEmpty()) {
                    return;
                } else {
                    // Find out which indexes are selected.
                    int nIndex = lsm.getMinSelectionIndex();
                    displayDocInfo(nIndex);
                    displayChangesInfo(nIndex);
                }
              
            }

        });
    }

    private void initialize_listBoxes() {

        listMembers.setModel(new DocOwnerListModel());
        listMembers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel lsm = listMembers.getSelectionModel();
        lsm.addListSelectionListener(new ListSelectionListener(){

            public void valueChanged(ListSelectionEvent lse) {
                ListSelectionModel lsm = (ListSelectionModel)lse.getSource();
                if (lse.getValueIsAdjusting()) {
                    return;
                }

                int firstIndex = lse.getFirstIndex();
                int lastIndex = lse.getLastIndex();

                if (lsm.isSelectionEmpty()) {
                    return;
                } else {
                    // Find out which indexes are selected.
                    int nIndex = lsm.getMinSelectionIndex();
                    displayDocInfo(nIndex);
                    displayChangesInfo(nIndex);
                }

            }

        });
    }

    private void initialize_Tables() {
        this.tblDocChanges.setModel(new DocumentChangesTableModel());
        TableColumnModel tcmModel = this.tblDocChanges.getColumnModel();
        TextAreaRenderer textAreaRenderer = new TextAreaRenderer();
        tcmModel.getColumn(2).setCellRenderer(textAreaRenderer);
    }


    /**
     * Loads change files from folder
     */
    private void loadFilesFromFolder() {
        File fFolder = new File(__TEST_FOLDER__);
        //find files in changes folder
        if (fFolder.isDirectory()) {
           File[] files =  fFolder.listFiles(new FilenameFilter(){
                public boolean accept(File dir, String name) {
                    if (name.startsWith("doc_")){
                        return true;
                    }
                    return false;
                }

            });

            //load files from folder
            for (int i = 0; i < files.length ; i++ ) {
                OdfDocument oDoc = null;
                try {

                    BungeniOdfDocumentHelper docHelper = new BungeniOdfDocumentHelper(files[i]);
                    changesInfo.addDocument(docHelper);

                } catch (Exception ex) {
                    Logger.getLogger(panelTrackChanges.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }


    private void displayDocInfo (int index ) {
        BungeniOdfDocumentHelper docHelper = changesInfo.getDocuments().get(index);
        StringBuffer sbDoc = new StringBuffer();
        File fFile = new File(docHelper.getOdfDocument().getBaseURI());
        sbDoc.append("Name:" + fFile.getName() + "\n");
        sbDoc.append("Created on: " + docHelper.getPropertiesHelper().getMetaCreationDate() + "\n");
        sbDoc.append("No. of times edited : " + docHelper.getPropertiesHelper().getMetaEditingCycles() + "\n");
        sbDoc.append("Editing duration : " + docHelper.getPropertiesHelper().getMetaEditingDuration() + "\n");


        this.txtareaDocInfo.setText(sbDoc.toString());
    }

    private void displayChangesInfo (int index) {
        DocumentChangesTableModel tblModel = (DocumentChangesTableModel) this.tblDocChanges.getModel();
        tblModel.updateModel(index);
    }
    /**
     * List model for document owner
     */
    private class DocOwnerListModel extends AbstractListModel {

         public int getSize() {
          return changesInfo.getSize();
        }

        public Object getElementAt(int arg0) {
            BungeniOdfDocumentHelper docHelper =  changesInfo.getDocuments().get(arg0);
            return docHelper.getPropertiesHelper().getUserDefinedPropertyValue("BungeniDocOwner");
        }

    }

    private class DocumentChangesTableModel extends AbstractTableModel {
        List<HashMap<String,String>> changeMarks = new ArrayList<HashMap<String,String>>(0);
        private  String[] column_names = {
            rBundle.getString("panelTrackChanges.tblDocChanges.action.text"),
            rBundle.getString("panelTrackChanges.tblDocChanges.date.text"),
            rBundle.getString("panelTrackChanges.tblDocChanges.text.text"),
      //      rBundle.getString("panelTrackChanges.tblDocChanges.position.text"),
            rBundle.getString("panelTrackChanges.tblDocChanges.status.text")
        };

        public DocumentChangesTableModel () {
            changeMarks = new ArrayList<HashMap<String,String>>(0);
        }

        public DocumentChangesTableModel (int iIndex) {
            buildModel(iIndex);
        }


        public void updateModel(int iIndex) {
            buildModel(iIndex);
            fireTableDataChanged();
        }

        private void buildModel(int iIndex) {
            changeMarks.clear();
            BungeniOdfDocumentHelper docHelper = changesInfo.getDocuments().get(iIndex);
            BungeniOdfTrackedChangesHelper changeHelper = docHelper.getChangesHelper();
            Element changeContainer = changeHelper.getTrackedChangeContainer();
            ArrayList<OdfTextChangedRegion> changes = changeHelper.getChangedRegions(changeContainer);
            for (OdfTextChangedRegion odfTextChangedRegion : changes) {
                StructuredChangeType scType = changeHelper.getStructuredChangeType(odfTextChangedRegion);
                HashMap<String,String> changeMark = changeHelper.getChangeInfo(scType);
                changeMarks.add(changeMark);
            }
        }

        @Override
        public int getRowCount() {
            if (changeMarks == null) return 0;
            return changeMarks.size();
        }

        @Override
        public int getColumnCount() {
            return column_names.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            HashMap<String,String> changeMark = changeMarks.get(rowIndex);
            System.out.println("Change Mark = " + changeMark);
            switch (columnIndex) {
                case 0 :
                    return changeMark.get("changeType");
                case 1 :
                    return changeMark.get("dcDate");
                case 2 :
                    if (changeMark.containsKey("changeText")) {
                        return changeMark.get("changeText");
                    } else
                        return new String("");
                case 3 :
                    return true;
                default :
                    return new String("");
            }
        }

        @Override
        public Class getColumnClass(int col) {
            if (col == 3)
                return Boolean.class;
            else
              return String.class;
        }


        @Override
        public String getColumnName(int column) {
            return column_names[column];
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

        lblMembers = new javax.swing.JLabel();
        scrollMembers = new javax.swing.JScrollPane();
        listMembers = new javax.swing.JList();
        scrollDocInfo = new javax.swing.JScrollPane();
        txtareaDocInfo = new javax.swing.JTextArea();
        lblDocInfo = new javax.swing.JLabel();
        scrollDocChanges = new javax.swing.JScrollPane();
        tblDocChanges = new javax.swing.JTable();
        btnViewDoc = new javax.swing.JButton();
        btnViewOrigDoc = new javax.swing.JButton();
        btnApplyChanges = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        lblMembers.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle"); // NOI18N
        lblMembers.setText(bundle.getString("panelTrackChanges.lblMembers.text")); // NOI18N

        listMembers.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Tinoula Awopetu", "Mashinski Murigi", "Raul Obwacha", "Felix Kerstengor" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        scrollMembers.setViewportView(listMembers);

        txtareaDocInfo.setColumns(20);
        txtareaDocInfo.setEditable(false);
        txtareaDocInfo.setRows(5);
        txtareaDocInfo.setWrapStyleWord(true);
        scrollDocInfo.setViewportView(txtareaDocInfo);

        lblDocInfo.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblDocInfo.setText(bundle.getString("panelTrackChanges.lblDocInfo.text")); // NOI18N

        tblDocChanges.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Delete", "23/12", "P(3)/L(4)", "This is an exceptional rule", null},
                {"Insert", "23/12", "P(5)/L(6)", ", for eternity.", new Boolean(true)},
                {"Delete", "23/12", "P(8)/W(23)", "The rule of law", new Boolean(true)}
            },
            new String [] {
                "Action", "Date", "Position", "Text", "Accept / Reject"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        scrollDocChanges.setViewportView(tblDocChanges);

        btnViewDoc.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnViewDoc.setText(bundle.getString("panelTrackChanges.btnViewDoc.text")); // NOI18N

        btnViewOrigDoc.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnViewOrigDoc.setText(bundle.getString("panelTrackChanges.btnViewOrigDoc.text")); // NOI18N

        btnApplyChanges.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnApplyChanges.setText(bundle.getString("panelTrackChanges.btnApplyChanges.text")); // NOI18N

        btnCancel.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnCancel.setText(bundle.getString("panelTrackChanges.btnCancel.text")); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        jLabel1.setText(bundle.getString("panelTrackChanges.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblDocInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24))
                            .addComponent(scrollDocInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                            .addComponent(scrollMembers, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                            .addComponent(lblMembers))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(scrollDocChanges, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                                .addGap(6, 6, 6))
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addComponent(btnViewDoc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnViewOrigDoc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnApplyChanges)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMembers)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scrollMembers, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDocInfo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollDocInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                    .addComponent(scrollDocChanges, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnViewDoc)
                    .addComponent(btnViewOrigDoc)
                    .addComponent(btnApplyChanges)
                    .addComponent(btnCancel))
                .addGap(12, 12, 12))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_btnCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApplyChanges;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnViewDoc;
    private javax.swing.JButton btnViewOrigDoc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblDocInfo;
    private javax.swing.JLabel lblMembers;
    private javax.swing.JList listMembers;
    private javax.swing.JScrollPane scrollDocChanges;
    private javax.swing.JScrollPane scrollDocInfo;
    private javax.swing.JScrollPane scrollMembers;
    private javax.swing.JTable tblDocChanges;
    private javax.swing.JTextArea txtareaDocInfo;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args ) {
        
        JFrame frm = new JFrame("Track Changes");
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.getContentPane().add(new panelTrackChanges(frm));
        frm.pack();
        frm.setVisible(true);
    }


}
