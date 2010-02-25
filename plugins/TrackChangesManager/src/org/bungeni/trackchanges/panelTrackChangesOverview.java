/*
 * panelTrackChanges.java
 *
 * Created on March 9, 2009, 10:21 AM
 */

package org.bungeni.trackchanges;

import com.sun.star.lang.XComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import org.bungeni.odfdocument.docinfo.BungeniChangeDocumentsInfo;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper.StructuredChangeType;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.trackchanges.registrydata.BungeniBill;
import org.bungeni.trackchanges.ui.support.TextAreaRenderer;
import org.bungeni.trackchanges.uno.UnoOdfFile;
import org.bungeni.trackchanges.uno.UnoOdfOpenFiles;
import org.bungeni.trackchanges.utils.CommonFunctions;
import org.bungeni.trackchanges.utils.ReviewDocuments;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.text.OdfTextChangedRegion;
import org.w3c.dom.Element;

/**
 *
 * @author  Ashok Hariharan
 */
public class panelTrackChangesOverview extends javax.swing.JPanel {

    ResourceBundle rBundle = java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle");
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(panelTrackChangesOverview.class.getName());

    String __TEST_FOLDER__ = "";
    String __CURRENT_BILL_FOLDER__ = "";
    BungeniChangeDocumentsInfo changesInfo = new BungeniChangeDocumentsInfo();
    ArrayList<BungeniBill> bungeniBills = new ArrayList<BungeniBill>() {
        {
           add( new BungeniBill("/ke/bills/en/finance-bill/01","Finance Bill","863524","2009-01-02"));
           add( new BungeniBill("/ke/bills/en/education-bill/01","Education Bill","848524","2009-01-12"));
        }
    };
    JFrame parentFrame;
    /** Creates new form panelTrackChanges */
    public panelTrackChangesOverview(JFrame parentFrame) {
        initComponents();
        this.parentFrame = parentFrame;
        __TEST_FOLDER__ ="test_files";
        initialize();
        loadFilesFromFolder();

    }

    /**
     * Initialize controls with data
     */
    private void initialize() {
        initialize_comboBoxes();
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

    private void initialize_comboBoxes() {
        this.cboBills.setModel(new BillsComboBoxModel
                (bungeniBills.toArray(new BungeniBill[bungeniBills.size()])));
        this.cboBills.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
               BungeniBill bBill = (BungeniBill) cboBills.getSelectedItem();
               String billId = bBill.getID();
               String filesDir =  System.getProperty("user.dir") + File.separator + "review_workspace" + File.separator +  "bill-"+billId;
               __CURRENT_BILL_FOLDER__ = filesDir;
               System.out.println("CUrrent folder = " + __CURRENT_BILL_FOLDER__);
               System.out.println("USer dir = " + System.getProperty("user.dir"));
               loadFilesFromFolder();
            }

        });
        this.cboBills.setSelectedItem(bungeniBills.get(0));
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
        if (__CURRENT_BILL_FOLDER__.length() > 0 ) {
            File fFolder = new File(__CURRENT_BILL_FOLDER__);
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
                changesInfo.clear();
                for (int i = 0; i < files.length ; i++ ) {
                    OdfDocument oDoc = null;
                    try {

                        BungeniOdfDocumentHelper docHelper = new BungeniOdfDocumentHelper(files[i]);
                        changesInfo.addDocument(docHelper);

                    } catch (Exception ex) {
                        log.error(ex);
                    }

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

    private BungeniOdfDocumentHelper getSelectedDocument() {
        Object selectedValue = this.listMembers.getSelectedValue();
        if (selectedValue != null ) {
            return (BungeniOdfDocumentHelper) selectedValue;
        }
        return null;
    }

    private void doReview() {
        try {
            //get the selected document
            final int selIndex = this.listMembers.getSelectedIndex();
            if (-1 == selIndex) {
                JOptionPane.showMessageDialog(parentFrame, "No document was selected. Please select a document for review");
                return;
            }

            SwingUtilities.invokeLater(new Runnable(){

                public void run() {
                    try {
                        BungeniOdfDocumentHelper docHelper = changesInfo.getDocuments().get(selIndex);
                        //create a clerk review document of the mp's document
                        //this is a copy of the MP's document
                        ReviewDocuments rvd = new ReviewDocuments(docHelper);
                        BungeniOdfDocumentHelper reviewDoc = rvd.getReviewCopy();
                        UnoOdfFile odfFile = UnoOdfOpenFiles.getFile(reviewDoc);
                        //open review document in openoffice
                    } catch (Exception ex) {
                        log.error("doReview:opening document : " + ex.getMessage(), ex);
                    }
                    //open review document in openoffice
                }

            });
            
        } catch (Exception ex) {
           log.error(ex);
        }

    }


    private class BillsComboBoxModel extends DefaultComboBoxModel {
        public BillsComboBoxModel(BungeniBill[] bills) {
            super(bills);
        }
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
            rBundle.getString("panelTrackChanges.tblDocChanges.text.text")
      //      rBundle.getString("panelTrackChanges.tblDocChanges.position.text"),
      //      rBundle.getString("panelTrackChanges.tblDocChanges.status.text")
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
               // case 3 :
               //     return true;
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
        jLabel1 = new javax.swing.JLabel();
        btnReview = new javax.swing.JButton();
        cboBills = new javax.swing.JComboBox();
        lblSelectBill = new javax.swing.JLabel();

        lblMembers.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle"); // NOI18N
        lblMembers.setText(bundle.getString("panelTrackChangesOverview.lblMembers.text")); // NOI18N

        listMembers.setFont(new java.awt.Font("Lucida Grande", 0, 10));
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
        lblDocInfo.setText(bundle.getString("panelTrackChangesOverview.lblDocInfo.text")); // NOI18N

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

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        jLabel1.setText(bundle.getString("panelTrackChangesOverview.jLabel1.text")); // NOI18N

        btnReview.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        btnReview.setText(bundle.getString("panelTrackChangesOverview.btnReview.text")); // NOI18N
        btnReview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReviewActionPerformed(evt);
            }
        });

        cboBills.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        cboBills.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Finance Bill", "Auto Bill" }));

        lblSelectBill.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblSelectBill.setText(bundle.getString("panelTrackChangesOverview.lblSelectBill.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollDocInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(scrollMembers, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(cboBills, 0, 220, Short.MAX_VALUE)
                    .addComponent(lblMembers)
                    .addComponent(lblSelectBill, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDocInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollDocChanges, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReview))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblSelectBill))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(scrollDocChanges, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReview, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(cboBills, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblMembers)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollMembers, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblDocInfo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollDocInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnReviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReviewActionPerformed
        try {
            btnReview.setEnabled(false);
            doReview();
            btnReview.setEnabled(true);

        } catch (Exception ex) {
            log.error(ex);
        }


    }//GEN-LAST:event_btnReviewActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReview;
    private javax.swing.JComboBox cboBills;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblDocInfo;
    private javax.swing.JLabel lblMembers;
    private javax.swing.JLabel lblSelectBill;
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
        frm.getContentPane().add(new panelTrackChangesOverview(frm));
        frm.pack();
        frm.setVisible(true);
    }


}
