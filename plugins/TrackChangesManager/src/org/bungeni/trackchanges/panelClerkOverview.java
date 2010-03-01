
package org.bungeni.trackchanges;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FilenameFilter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.bungeni.odfdocument.docinfo.BungeniDocAuthor;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper.StructuredChangeType;
import org.bungeni.trackchanges.ui.support.TextAreaRenderer;
import org.bungeni.trackchanges.utils.AppProperties;
import org.bungeni.trackchanges.utils.CommonFunctions;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.text.OdfTextChangedRegion;
import org.w3c.dom.Element;

/**
 *
 * @author Ashok Hariharan
 */
public class panelClerkOverview extends panelChangesBase {

 
   
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(panelClerkOverview.class.getName());



    /** Creates new form panelClerkOverview */
    public panelClerkOverview() {
        super();
        initComponents();
    }

    public panelClerkOverview(JFrame parentFrm) {
        super(parentFrm);
        initComponents();
        initialize();
        loadFilesFromFolder();

    }

    private void initialize(){
       initialize_listBoxes();
       initialize_Tables();

    }


    private void loadFilesFromFolder(){
          String currentBillFolder = CommonFunctions.getWorkspaceForBill((String)AppProperties.getProperty("CurrentBillID"));
          if (currentBillFolder.length() > 0 ) {
            File fFolder = new File(currentBillFolder);
            //find files in changes folder
            if (fFolder.isDirectory()) {
               File[] files =  fFolder.listFiles(new FilenameFilter(){
               Pattern pat = Pattern.compile("clerk_u[0-9][0-9][0-9][0-9]([a-z0-9_-]*?).odt");
                    public boolean accept(File dir, String name) {
                        if (pat.matcher(name).matches()) {
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
                        System.out.println("no. of change documents = " + changesInfo.getSize());
                    } catch (Exception ex) {
                        log.error("loadingFilesFromFolder : " + ex.getMessage(), ex);
                    }

                }
            }
        }
    }

    private void initialize_listBoxes(){
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
                    //do struff here
                    displayChangesInfo(nIndex);
                }

            }

        });
        
    }

    private void initialize_Tables(){
        this.tblDocChanges.setModel(new DocumentChangesTableModel());
        TableColumnModel tcmModel = this.tblDocChanges.getColumnModel();
        this.tblDocChanges.setDefaultRenderer(String.class, new DocumentChangesTableCellRenderer());
        TextAreaRenderer textAreaRenderer = new TextAreaRenderer();
        tcmModel.getColumn(2).setCellRenderer(textAreaRenderer);

    }

    private void displayChangesInfo (int index) {
        DocumentChangesTableModel tblModel = (DocumentChangesTableModel) this.tblDocChanges.getModel();
        tblModel.updateModel(index, filterByClerk());
    }

    private boolean filterByClerk(){
          return this.chkFilterByClerk.isSelected();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollMembers = new javax.swing.JScrollPane();
        listMembers = new javax.swing.JList();
        lblMembers = new javax.swing.JLabel();
        scrollDocChanges = new javax.swing.JScrollPane();
        tblDocChanges = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        chkFilterByClerk = new javax.swing.JCheckBox();

        listMembers.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        listMembers.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Tinoula Awopetu", "Mashinski Murigi", "Raul Obwacha", "Felix Kerstengor" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        scrollMembers.setViewportView(listMembers);

        lblMembers.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle"); // NOI18N
        lblMembers.setText(bundle.getString("panelTrackChangesOverview.lblMembers.text")); // NOI18N

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

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jLabel1.setText(bundle.getString("panelTrackChangesOverview.jLabel1.text")); // NOI18N

        chkFilterByClerk.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        chkFilterByClerk.setText("Don't Show my Changes");
        chkFilterByClerk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFilterByClerkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollMembers, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                    .addComponent(lblMembers))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkFilterByClerk)
                    .addComponent(scrollDocChanges, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE))
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(chkFilterByClerk, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollDocChanges, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                        .addGap(36, 36, 36))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scrollMembers, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                        .addGap(206, 206, 206))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void chkFilterByClerkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFilterByClerkActionPerformed
        // TODO add your handling code here:
        displayChangesInfo(listMembers.getSelectedIndex());
    }//GEN-LAST:event_chkFilterByClerkActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkFilterByClerk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblMembers;
    private javax.swing.JList listMembers;
    private javax.swing.JScrollPane scrollDocChanges;
    private javax.swing.JScrollPane scrollMembers;
    private javax.swing.JTable tblDocChanges;
    // End of variables declaration//GEN-END:variables

    @Override
    public void updatePanel(HashMap<String, Object> infomap) {
        super.updatePanel(infomap);
        loadFilesFromFolder();
        this.listMembers.setModel(new DocOwnerListModel());
        if (infomap.containsKey("selectedAuthor")) {
            BungeniDocAuthor selAut  = (BungeniDocAuthor) infomap.get("selectedAuthor");
            selectAuthorinList(selAut);
        }
    }

    private void selectAuthorinList(BungeniDocAuthor selAut) {
        DocOwnerListModel model = ((DocOwnerListModel)listMembers.getModel());
        int nSize = model.getSize();
        for (int i = 0; i < nSize ; i++) {
            BungeniDocAuthor autAtIndex = model.getAuthorAtIndex(i);
            if (autAtIndex.equals(selAut)) {
                listMembers.setSelectedIndex(i);
                return;
            }
        }
    }


    private static String __CLERK_NAME__ = "Ashok Hariharan";

    private class DocumentChangesTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable aTable, Object aNumberValue, boolean aIsSelected, boolean aHasFocus,int aRow, int aColumn) {
            Component p = super.getTableCellRendererComponent(aTable, aNumberValue, aHasFocus, aHasFocus, aRow, aColumn);
            DocumentChangesTableModel tblModel = ((DocumentChangesTableModel)tblDocChanges.getModel());
            HashMap<String,String> changesInfo = tblModel.getModelBase().get(aRow);
            String dcCreator = changesInfo.get("dcCreator");
            if (dcCreator.equals(__CLERK_NAME__)) {
                p.setBackground(Color.magenta);
            } else
                p.setBackground(null);
            return p;
        }
    }

    private class DocumentChangesTableModel extends AbstractTableModel {
        List<HashMap<String,String>> changeMarks = new ArrayList<HashMap<String,String>>(0);
        private  String[] column_names = {
            bundleBase.getString("panelTrackChanges.tblDocChanges.action.text"),
            bundleBase.getString("panelTrackChanges.tblDocChanges.date.text"),
            bundleBase.getString("panelTrackChanges.tblDocChanges.text.text")
      //      rBundle.getString("panelTrackChanges.tblDocChanges.position.text"),
      //      rBundle.getString("panelTrackChanges.tblDocChanges.status.text")
        };

        public DocumentChangesTableModel () {
            changeMarks = new ArrayList<HashMap<String,String>>(0);
        }

        public DocumentChangesTableModel (int iIndex, boolean bFilterbyAuthor) {
            buildModel(iIndex, bFilterbyAuthor);
        }

        public List<HashMap<String,String>> getModelBase() {
            return changeMarks;
        }
        
        public void updateModel(int iIndex, boolean bFilterbyAuthor) {
            buildModel(iIndex, bFilterbyAuthor);
            fireTableDataChanged();
        }

        private void buildModel(int iIndex , boolean bFilterByAuthor) {
            changeMarks.clear();
            BungeniOdfDocumentHelper docHelper = changesInfo.getDocuments().get(iIndex);
            String docAuthor = docHelper.getPropertiesHelper().getUserDefinedPropertyValue("BungeniDocAuthor");
            BungeniOdfTrackedChangesHelper changeHelper = docHelper.getChangesHelper();
            Element changeContainer = changeHelper.getTrackedChangeContainer();
            ArrayList<OdfTextChangedRegion> changes = new ArrayList<OdfTextChangedRegion>(0);
            if (!bFilterByAuthor)
                changes = changeHelper.getChangedRegions(changeContainer);
            else
                changes = changeHelper.getChangedRegionsByCreator(changeContainer, docAuthor);
            for (OdfTextChangedRegion odfTextChangedRegion : changes) {
                StructuredChangeType scType = changeHelper.getStructuredChangeType(odfTextChangedRegion);
                HashMap<String,String> changeMark = changeHelper.getChangeInfo(scType);
                changeMarks.add(changeMark);
            }
            updateMsgNoOfChanges(changeMarks.size());
        }

        private void updateMsgNoOfChanges(int nSize) {
            Object[] values = { new Integer(nSize) };
            MessageFormat format = new MessageFormat(bundleBase.getString("panelTrackChangesOverview.lblNoOfChanges.text"));
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


}
