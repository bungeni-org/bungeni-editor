
package org.bungeni.trackchanges.ui;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FilenameFilter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.bungeni.odfdocument.docinfo.BungeniDocAuthor;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfChangesMergeHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper.StructuredChangeType;
import org.bungeni.odfdom.utils.BungeniOdfDateHelper;
import org.bungeni.trackchanges.ui.support.TextAreaRenderer;
import org.bungeni.trackchanges.uno.UnoOdfFile;
import org.bungeni.trackchanges.uno.UnoOdfOpenFiles;
import org.bungeni.trackchanges.utils.AppProperties;
import org.bungeni.trackchanges.utils.CommonFunctions;
import org.bungeni.trackchanges.utils.ReviewDocuments;
import org.bungeni.trackchanges.utils.RuntimeProperties;
import org.odftoolkit.odfdom.doc.text.OdfTextChangedRegion;
import org.w3c.dom.Element;

/**
 *
 * @author Ashok Hariharan
 */
public class panelClerkOverview extends panelChangesBase {

 
   
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(panelClerkOverview.class.getName());
     private static Color __CLERK_ACTIVE_ROW_COLOR__ = new Color (51, 255, 255);
    String                  __CLERK_NAME__ = "";
    /** Creates new form panelClerkOverview */
    public panelClerkOverview() {
        super();
        initComponents();
    }

    public panelClerkOverview(JFrame parentFrm, String panelName) {
        super(parentFrm, panelName);
        PANEL_FILTER_REVIEW_STAGE = "ClerkReview";
        PANEL_REVIEW_STAGE = "ClerkConsolidationReview";
        initComponents();
        __CLERK_NAME__ = RuntimeProperties.getDefaultProp("ClerkUser");
        initialize();
        loadFilesFromFolder();

    }

    private void initialize(){
       initialize_listBoxes();
       initialize_Tables();

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


    private boolean doConsolidateAll(){
        boolean bReturn = false;
        //build a list of all authors in the list
        final List<BungeniDocAuthor> docAuthors = new ArrayList<BungeniDocAuthor>(){{
                for (int i = 0; i < listMembers.getModel().getSize() ; i++) {
                        add((BungeniDocAuthor)listMembers.getModel().getElementAt(i));
                }
        }};

        if (docAuthors.size() == 0 ) {
                JOptionPane.showMessageDialog(parentFrame, java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle").getString("no_dox_for_consolidation"));
        } else {

            getContainerInterface().startProgress();
            btnConsolidateAll.setEnabled(false);
            SwingWorker consolidateAllWorker = new SwingWorker(){
                    @Override
                    protected Object doInBackground() throws Exception {
                        List<BungeniOdfDocumentHelper> consolidatedDocs = new ArrayList<BungeniOdfDocumentHelper>(0);
                        for (int i = 0; i < docAuthors.size(); i++) {
                             BungeniDocAuthor docAuthor = docAuthors.get(i);
                             BungeniOdfDocumentHelper conDoc = consolidateCurrentDocument(i, docAuthor);
                             consolidatedDocs.add(conDoc);
                        }
                        return consolidatedDocs;
                    }

                     @Override
                     protected void done() {
                    try {
                        // get the return envelope
                        List<BungeniOdfDocumentHelper> docs = (List<BungeniOdfDocumentHelper>) this.get();
                        final List<String> savePaths = new ArrayList<String>(0);
                        for (BungeniOdfDocumentHelper doc : docs) {
                            String spathToDocument = doc.getDocumentPath();
                            doc.closeDocument();
                            savePaths.add(spathToDocument);
                        }
                        log.debug("No. of docs returned = " + docs.size());
                        Thread.sleep(2000);
                        //this is required becuase the save done in the background thread may still be writing
                        //the wait in this thread prevents a crash when switching to the other tab and hitting refresh
                        HashMap<String,Object> infomap = new HashMap<String, Object>(){{
                            put("updateListFiles", savePaths);
                        }};
                        getContainerInterface().updatePanel("ConsolidateReview", infomap);
                        getContainerInterface().stopProgress();

                        btnConsolidateAll.setEnabled(true);
                            if (docs.size() > 0) {
                                StringBuffer sbBuffer = new StringBuffer();

                                for (BungeniDocAuthor anAuthor : docAuthors) {
                                    sbBuffer.append(anAuthor.toString() + "\n");
                                }

                                JOptionPane
                                    .showMessageDialog(parentFrame,
                                                       bundleBase
                                                       .getString("consolidation_generated_ok") + sbBuffer
                                                           .toString(), bundleBase
                                                           .getString("consolidation_gen_title"), JOptionPane
                                                           .INFORMATION_MESSAGE);
                            } else {
                             JOptionPane.showMessageDialog(
                                parentFrame, bundleBase.getString(
                                    "consolidation_gen_error_occured"), bundleBase.getString(
                                    "consolidation_gen_title"), JOptionPane.WARNING_MESSAGE);
                            }
                        //viewConsolidatedDocument(envelope);
                    } catch (InterruptedException ex) {
                        log.error("consolidateWorker = " + ex.getMessage());
                    } catch (ExecutionException ex) {
                        log.error("consolidateWorker = " + ex.getMessage());
                    }

                     }
            };
            consolidateAllWorker.execute();
        }

        return bReturn;
    }

    private boolean doConsolidate(){
        boolean bReturn = false;
            final int selIndex = this.listMembers.getSelectedIndex();
            if (-1 == selIndex) {
                JOptionPane.showMessageDialog(parentFrame, java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle").getString("no_doc_selected_for_review"));
                bReturn = false;
                return false;
            } else {
            final BungeniDocAuthor selectedAuthor = (BungeniDocAuthor) this.listMembers.getSelectedValue();

            getContainerInterface().startProgress();
            btnConsolidate.setEnabled(false);
            SwingWorker consolidateWorker = new SwingWorker(){
                    @Override
                    protected Object doInBackground() throws Exception {
                        BungeniOdfDocumentHelper envelope = consolidateCurrentDocument(selIndex, selectedAuthor);
                        return envelope;
                    }

                     @Override
                     protected void done() {
                    try {
                        // get the return envelope
                        BungeniOdfDocumentHelper envelope = (BungeniOdfDocumentHelper) this.get();
                        final String pathtoDoc = envelope.getDocumentPath();
                        // always close the document to finalize the save and close all handles
                        envelope.closeDocument();
                        HashMap<String,Object> infomap = new HashMap<String, Object>(){{
                            put("updateListFile",pathtoDoc);
                        }};
                        getContainerInterface().updatePanel("ConsolidateReview", infomap);
                        getContainerInterface().stopProgress();
                        btnConsolidate.setEnabled(true);
                        if (envelope != null) {
                            JOptionPane.showMessageDialog(parentFrame,
                                                   bundleBase
                                                       .getString("consolidation_generated_for") + selectedAuthor
                                                       .toString(),
                                                       bundleBase
                                                       .getString("consolidation_gen_title"), JOptionPane.INFORMATION_MESSAGE);
                        } else {
                             JOptionPane.showMessageDialog(
                                parentFrame, bundleBase.getString(
                                    "consolidation_gen_error_occured"), bundleBase.getString(
                                    "consolidation_gen_title"), JOptionPane.WARNING_MESSAGE);
                        }
                        //viewConsolidatedDocument(envelope);
                    } catch (InterruptedException ex) {
                        log.error("consolidateWorker = " + ex.getMessage());
                    } catch (ExecutionException ex) {
                        log.error("consolidateWorker = " + ex.getMessage());
                    }

                     }
            };
            consolidateWorker.execute();
         }
    return true;

    }


    private BungeniOdfDocumentHelper  consolidateCurrentDocument(int selIndex, final BungeniDocAuthor selectedAuthor) {
        BungeniOdfDocumentHelper returnDoc = null;
        try {
            // get the selected document index
            BungeniOdfDocumentHelper docHelper = changesInfo.getDocuments().get(selIndex);
            // create a clerk review document of the mp's document
            // this is a copy of the MP's document
            ReviewDocuments rvd = new ReviewDocuments(docHelper, this.PANEL_REVIEW_STAGE); // TODO
            final BungeniOdfDocumentHelper reviewDoc = rvd.getReviewCopy();
            //final BungeniDocAuthor selectedAuthor = (BungeniDocAuthor) this.listMembers.getSelectedValue();
              BungeniOdfChangesMergeHelper chm = reviewDoc.getChangesHelper().getChangesMergeHelper();
              String sourceUser = __CLERK_NAME__;
              String targetUser = selectedAuthor.toString();
              chm.mergeChanges(sourceUser, targetUser);
              reviewDoc.getOdfDocument().save(reviewDoc.getDocumentPath());
              returnDoc = reviewDoc;
        } catch (Exception ex) {
            log.error("consolidateCurrentDocument : " + ex.getMessage(), ex);
        }
        
        return returnDoc;

    }

    private boolean viewConsolidatedDocument (BungeniOdfDocumentHelper reviewD ) {
            // invoke openoffice in a runnable thread
            boolean bState = false;
            final BungeniOdfDocumentHelper reviewDoc = reviewD;
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    try {

                        UnoOdfFile odfFile = UnoOdfOpenFiles.getFile(reviewDoc);

                    } catch (Exception ex) {
                        log.error("doReview:opening document : " + ex.getMessage(), ex);
                    }
                    // open review document in openoffice
                    // open review document in openoffice
                }
            });
           bState = true;
           return bState;
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
        lblDocumentChanges = new javax.swing.JLabel();
        chkFilterByClerk = new javax.swing.JCheckBox();
        btnReview = new javax.swing.JButton();
        btnConsolidate = new javax.swing.JButton();
        btnConsolidateAll = new javax.swing.JButton();

        listMembers.setFont(new java.awt.Font("Lucida Grande", 0, 10));
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

        lblDocumentChanges.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblDocumentChanges.setText(bundle.getString("panelTrackChangesOverview.jLabel1.text")); // NOI18N

        chkFilterByClerk.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        chkFilterByClerk.setText(bundle.getString("panelClerkOverview.chkFilterByClerk.text")); // NOI18N
        chkFilterByClerk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFilterByClerkActionPerformed(evt);
            }
        });

        btnReview.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnReview.setText(bundle.getString("panelClerkOverview.btnReview.text")); // NOI18N

        btnConsolidate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnConsolidate.setText(bundle.getString("panelClerkOverview.btnConsolidate.text")); // NOI18N
        btnConsolidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsolidateActionPerformed(evt);
            }
        });

        btnConsolidateAll.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnConsolidateAll.setText(bundle.getString("panelClerkOverview.btnConsolidateAll.text")); // NOI18N
        btnConsolidateAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsolidateAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollMembers, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                    .addComponent(lblMembers))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnReview)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnConsolidate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConsolidateAll))
                    .addComponent(lblDocumentChanges, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkFilterByClerk)
                    .addComponent(scrollDocChanges, javax.swing.GroupLayout.DEFAULT_SIZE, 673, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMembers)
                    .addComponent(lblDocumentChanges))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(chkFilterByClerk, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollDocChanges, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnReview)
                            .addComponent(btnConsolidate)
                            .addComponent(btnConsolidateAll))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scrollMembers, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                        .addGap(209, 209, 209))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void chkFilterByClerkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFilterByClerkActionPerformed
        displayChangesInfo(listMembers.getSelectedIndex());
    }//GEN-LAST:event_chkFilterByClerkActionPerformed

    private void btnConsolidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsolidateActionPerformed
                doConsolidate();
    }//GEN-LAST:event_btnConsolidateActionPerformed

    private void btnConsolidateAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsolidateAllActionPerformed
      // SwingUtilities.invokeLater(new Runnable() {
     //       public void run() {
                doConsolidateAll();
    //        }
   // });
    }//GEN-LAST:event_btnConsolidateAllActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConsolidate;
    private javax.swing.JButton btnConsolidateAll;
    private javax.swing.JButton btnReview;
    private javax.swing.JCheckBox chkFilterByClerk;
    private javax.swing.JLabel lblDocumentChanges;
    private javax.swing.JLabel lblMembers;
    private javax.swing.JList listMembers;
    private javax.swing.JScrollPane scrollDocChanges;
    private javax.swing.JScrollPane scrollMembers;
    private javax.swing.JTable tblDocChanges;
    // End of variables declaration//GEN-END:variables

    @Override
    public void updatePanel(final HashMap<String, Object> infomap) {
        super.updatePanel(infomap);
        //always call in a non-EDT thread since this is called asynchronously
        //on the tab change
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                loadFilesFromFolder();
                listMembers.setModel(new DocOwnerListModel());
                if (infomap.containsKey("selectedAuthor")) {
                    BungeniDocAuthor selAut  = (BungeniDocAuthor) infomap.get("selectedAuthor");
                    selectAuthorinList(selAut);
                }
            }
         });
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



    private class DocumentChangesTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable aTable, Object aNumberValue, boolean aIsSelected, boolean aHasFocus,int aRow, int aColumn) {
            Component p = super.getTableCellRendererComponent(aTable, aNumberValue, aHasFocus, aHasFocus, aRow, aColumn);
            DocumentChangesTableModel tblModel = ((DocumentChangesTableModel)tblDocChanges.getModel());
            HashMap<String,Object> changesInfo = tblModel.getModelBase().get(aRow);
            String dcCreator = changesInfo.get("dcCreator").toString();
            if (dcCreator.equals(__CLERK_NAME__)) {
                p.setBackground(__CLERK_ACTIVE_ROW_COLOR__);
            } else
                p.setBackground(null);
            return p;
        }
    }

    private class DocumentChangesTableModel extends AbstractTableModel {
        List<HashMap<String,Object>> changeMarks = new ArrayList<HashMap<String,Object>>(0);
        private  String[] column_names = {
            bundleBase.getString("panelTrackChanges.tblDocChanges.action.text"),
            bundleBase.getString("panelTrackChanges.tblDocChanges.date.text"),
            bundleBase.getString("panelTrackChanges.tblDocChanges.text.text")
      //      rBundle.getString("panelTrackChanges.tblDocChanges.position.text"),
      //      rBundle.getString("panelTrackChanges.tblDocChanges.status.text")
        };

        public DocumentChangesTableModel () {
            changeMarks = new ArrayList<HashMap<String,Object>>(0);
        }

        public DocumentChangesTableModel (int iIndex, boolean bFilterbyAuthor) {
            buildModel(iIndex, bFilterbyAuthor);
        }

        public List<HashMap<String,Object>> getModelBase() {
            return changeMarks;
        }
        
        public void updateModel(final int iIndex, final boolean bFilterbyAuthor) {
            getContainerInterface().startProgress();
            SwingWorker modelWorker = new SwingWorker(){

                @Override
                protected Object doInBackground() throws Exception {
                    buildModel(iIndex, bFilterbyAuthor);
                    return Boolean.TRUE;
                }

                @Override
                protected void done(){
                    fireTableDataChanged();
                    getContainerInterface().stopProgress();
                }
            };
            modelWorker.execute();
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
                HashMap<String,Object> changeMark = changeHelper.getChangeInfo(scType);
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
            HashMap<String,Object> changeMark = changeMarks.get(rowIndex);
            
            switch (columnIndex) {
                case 0 :
                    return changeMark.get("changeType");
                case 1 :
                    return BungeniOdfDateHelper.odfDateToPresentationDate(changeMark.get("dcDate").toString());
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
