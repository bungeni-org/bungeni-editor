package org.bungeni.trackchanges;


//~--- non-JDK imports --------------------------------------------------------

import java.util.Vector;
import org.bungeni.odfdocument.docinfo.BungeniDocAuthor;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper.StructuredChangeType;
import org.bungeni.trackchanges.registrydata.BungeniBill;
import org.bungeni.trackchanges.rss.BungeniBillDataProvider;
import org.bungeni.trackchanges.ui.support.TextAreaRenderer;
import org.bungeni.trackchanges.utils.AppProperties;
import org.bungeni.trackchanges.utils.CommonFunctions;
import org.bungeni.trackchanges.utils.ReviewDocuments;
import org.bungeni.trackchanges.utils.RuntimeProperties;

import org.odftoolkit.odfdom.doc.text.OdfTextChangedRegion;

import org.w3c.dom.Element;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.awt.Component;

import java.io.File;
import java.io.FilenameFilter;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import javax.swing.DefaultCellEditor;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.bungeni.db.AbstractQueryResultsIterator;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.QueryResults;
import org.bungeni.odfdom.utils.BungeniOdfDateHelper;
import org.bungeni.trackchanges.process.schema.ProcessAmend;
import org.bungeni.trackchanges.process.schema.ProcessAmends;
import org.bungeni.trackchanges.process.schema.ProcessDocument;
import org.bungeni.trackchanges.queries.ProcessQueries;
import org.bungeni.trackchanges.utils.GenericListSelectionListener;

/**
 *
 * @author Ashok Hariharan
 */
public class panelApproveRejectChanges extends panelChangesBase {

    // InfiniteProgressPanel m_glassPane =  null;
    private static String                  __CLERK_NAME__ = "";
    private static org.apache.log4j.Logger log            =
        org.apache.log4j.Logger.getLogger(panelApproveRejectChanges.class.getName());

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnComplete;
    private javax.swing.JButton btnEndProcess;
    private javax.swing.JButton btnRestartProcess;
    private javax.swing.JButton btnStartProcess;
    private javax.swing.JLabel lblDocumentChanges;
    private javax.swing.JLabel lblMembers;
    private javax.swing.JList listMembers;
    private javax.swing.JScrollPane scrollDocChanges;
    private javax.swing.JScrollPane scrollMembers;
    private javax.swing.JTable tblDocChanges;
    // End of variables declaration//GEN-END:variables

    /** Creates new form panelClerkOverview */
    public panelApproveRejectChanges() {
        super();
        initComponents();
    }

    public panelApproveRejectChanges(JFrame parentFrm, String pName) {
        super(parentFrm, pName);
        PANEL_FILTER_REVIEW_STAGE = "ClerkApproveRejectView";
        PANEL_REVIEW_STAGE        = "";
        initComponents();
        __CLERK_NAME__ = RuntimeProperties.getDefaultProp("ClerkUser");
        loadFilesFromFolder();
        initialize();
    }

    private void initialize() {
        initialize_listBoxes();
        initialize_Tables();
    }

    private void loadFilesFromFolder() {
        String currentBillFolder =
            CommonFunctions.getWorkspaceForBill(CommonFunctions.getCurrentBillID());

        if (currentBillFolder.length() > 0) {
            File fFolder = new File(currentBillFolder);

            // find files in changes folder
            if (fFolder.isDirectory()) {
                File[] files = fFolder.listFiles(new FilenameFilter() {
                    Pattern pat = Pattern.compile(
                                      ReviewDocuments.getReviewStage(
                                          PANEL_FILTER_REVIEW_STAGE).getDocumentFilterPattern());    // ("clerk_u[0-9][0-9][0-9][0-9]([a-z0-9_-]*?).odt");
                    public boolean accept(File dir, String name) {
                        if (pat.matcher(name).matches()) {
                            return true;
                        }

                        return false;
                    }
                });

                System.out.println("cons files found :" + files.length);

                for (File file : files) {
                    System.out.println("cons file : " + file.getName());
                }

                changesInfo.reload(files);
            }
        }

    }

    private void initialize_listBoxes() {

        /**
         *
         * Members list box
         *
         *
         */
        DocApproveRejectListModel consModel = new DocApproveRejectListModel();

        consModel.resetModel();
        listMembers.setModel(consModel);
        listMembers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ListSelectionModel lsm = listMembers.getSelectionModel();

        lsm.addListSelectionListener(new GenericListSelectionListener() {
            @Override
            public void onSelectIndex(int nIndex) {
                //displayChangesInfo(nIndex);
                
            }
        });
    }

    private void initialize_Tables() {
        this.tblDocChanges.setModel(new DocumentApproveRejectChangesTableModel());

        TableColumnModel tcmModel = this.tblDocChanges.getColumnModel();

        this.tblDocChanges.setDefaultRenderer(String.class, new DocumentApproveRejectChangesTableCellRenderer());

        TextAreaRenderer textAreaRenderer = new TextAreaRenderer();

        tcmModel.getColumn(2).setCellRenderer(textAreaRenderer);

        TableColumn tblColumnStatus = this.tblDocChanges.getColumnModel().getColumn(3);
        final JCheckBox fjCheckBox = new JCheckBox();
        ApproveRejectStatusEditor aprEditor = new ApproveRejectStatusEditor(fjCheckBox);
        tblColumnStatus.setCellEditor(aprEditor);
       


    }

class ApproveRejectStatusEditor extends DefaultCellEditor {

  public ApproveRejectStatusEditor(final JCheckBox tf) {
   super(tf);
   tf.setBorder(null);
   delegate = new EditorDelegate() {

    @Override
    public void setValue(Object param) {
     Boolean _value = (Boolean)param;
     if (_value == null) {
         tf.setSelected(Boolean.FALSE);
     } else {
          Boolean _d = _value.booleanValue();
          tf.setSelected(_d);
     }
    }

   @Override
    public Object getCellEditorValue() {
      Boolean _field = tf.isSelected();
      return _field;
    }
   };
  }
}

    private void displayChangesInfo(int index) {
        DocumentApproveRejectChangesTableModel tblModel = (DocumentApproveRejectChangesTableModel) this.tblDocChanges.getModel();

        tblModel.updateModel(index);
    }


    private String getBillName() {
        String            aBillId      = (String) AppProperties.getProperty("CurrentBillID");
        String            billTitle    = "";
        List<BungeniBill> bungeniBills = BungeniBillDataProvider.getData();

        for (BungeniBill bungeniBill : bungeniBills) {
            if (bungeniBill.getID().equals(aBillId)) {
                billTitle = bungeniBill.getTitle();
            }
        }

        if (billTitle.length() == 0) {
            return "Unknown Bill Title";
        }

        return billTitle;
    }



    /**
     * Changes in a single document
     */
    class DocumentChange {

        String docName;
        String docAuthor;

        List<HashMap<String, Object>> changeMarks = new ArrayList<HashMap<String, Object>>(0);

        public DocumentChange(){

        }
        
        public void addChangeMark(HashMap<String,Object> cmark) {
            changeMarks.add(cmark);
        }

        public void setChangeMarks(List<HashMap<String, Object>> cmarks) {
            changeMarks = cmarks;
        }

        public List<HashMap<String, Object>>  getChangeMarks(){
            return changeMarks;
        }

        public void setDocName(String dName) {
            this.docName = dName;
        }

        public void setDocAuthor (String dAuthor) {
            this.docAuthor = dAuthor;
        }

        public String getDocName(){
            return this.docName;
        }

        public String getDocAuthor(){
            return this.docAuthor;
        }
                
    }

    class DocumentChanges {
        List<DocumentChange> documentChanges = new ArrayList<DocumentChange>(0);

        public DocumentChanges(){

        }

        public void addChange(DocumentChange ch) {
            this.documentChanges.add(ch);
        }

        public List<DocumentChange> getChanges(){
            return documentChanges;
        }
    }


    /**
     *  String processId,
            String docName,
            String docAuthor,
            String changeId,
            String changeType ,
            String changeDate,
            String changeText,
            String changeStatus
     * @param docChange
     * @return
     */
     private List<String> getDocChangeQueries(String processId, DocumentChange docChange) {
         List<String> runQueries = new ArrayList<String>(0);
         List<HashMap<String,Object>> changeList = docChange.getChangeMarks();
         for (HashMap<String, Object> aChange : changeList) {
             Date aDate = BungeniOdfDateHelper.odfDateToJavaDate(aChange.get("dcDate").toString());
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             String sDcDate = sdf.format(aDate);
             String aQuery = ProcessQueries.INSERT_PROCESS_AMENDS(
                     processId, 
                     docChange.getDocName(), 
                     docChange.getDocAuthor(), 
                     aChange.get("changeId").toString(), 
                     aChange.get("changeType").toString(),
                     sDcDate,
                     aChange.get("changeText").toString(),
                     "false");
             runQueries.add(aQuery);
         }
         return runQueries;
     }

      private DocumentChange getDocChange(BungeniOdfDocumentHelper docHelper, boolean bFilterByAuthor, String docName, String docAuthor) {
            DocumentChange change = new DocumentChange();
            System.out.println("building model for " + docAuthor);
            BungeniOdfTrackedChangesHelper  changeHelper    = docHelper.getChangesHelper();
            Element                         changeContainer = changeHelper.getTrackedChangeContainer();
            ArrayList<OdfTextChangedRegion> changes         = new ArrayList<OdfTextChangedRegion>(0);
            change.setDocName(docName);
            change.setDocAuthor(docAuthor);
            if (!bFilterByAuthor) {
                changes = changeHelper.getChangedRegions(changeContainer);
            } else {
                changes = changeHelper.getChangedRegionsByCreator(changeContainer, docAuthor);
            }

            for (OdfTextChangedRegion odfTextChangedRegion : changes) {
                StructuredChangeType    scType     = changeHelper.getStructuredChangeType(odfTextChangedRegion);
                HashMap<String, Object> changeMark = changeHelper.getChangeInfo(scType);
                change.addChangeMark(changeMark);
            }
            return change;

        }

  
    private void doProcessDocuments(){
       getContainerInterface().startProgress();
       this.btnStartProcess.setEnabled(false);

        SwingWorker procDocsWorker = new SwingWorker(){
            @Override
            protected Object doInBackground() throws Exception {
                //first build a list of documents
                ArrayList<String> listQueries = new ArrayList<String>(0);
               //get process query
               String processId = UUID.randomUUID().toString();
               String billId = (String) AppProperties.getProperty("CurrentBillID");
               String insQuery = ProcessQueries.INSERT_PROCESS(processId, billId);
               listQueries.add(insQuery);
               loadFilesFromFolder();
               for (BungeniOdfDocumentHelper docHelper  : changesInfo.getDocuments()) {
                    //get process docs queries
                    String documentPath  = docHelper.getDocumentPath();
                    String filename = documentPath.substring(documentPath.lastIndexOf(File.separator) + 1);
                    String docAuthor = docHelper.getPropertiesHelper().getUserDefinedPropertyValue("BungeniDocAuthor");
                    String insQuery2 = ProcessQueries.INSERT_PROCESS_DOCS(processId, filename, documentPath, docAuthor);
                    listQueries.add(insQuery2);
                    {
                        //get process amend queries
                        DocumentChange documentChange = getDocChange(docHelper, true, filename, docAuthor);
                        listQueries.addAll(getDocChangeQueries(processId, documentChange));
                    }
                }
                BungeniClientDB db = BungeniClientDB.defaultConnect();
                db.Connect();
                db.Update(listQueries, true);
                db.EndConnect();
                return Boolean.TRUE;
            }
            
            @Override
            protected void done(){
                try {
                    Boolean bState = (Boolean) get();
                    btnStartProcess.setEnabled(true);
                    getContainerInterface().stopProgress();
                } catch (InterruptedException ex) {
                    log.error(ex.getMessage());
                } catch (ExecutionException ex) {
                    log.error(ex.getMessage());
                }
            }
        };
        procDocsWorker.execute();
    }

    /**
     * This method is called from within the constructor to
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
        btnStartProcess = new javax.swing.JButton();
        btnRestartProcess = new javax.swing.JButton();
        btnEndProcess = new javax.swing.JButton();
        btnComplete = new javax.swing.JButton();

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

        btnStartProcess.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnStartProcess.setText(bundle.getString("panelApproveRejectChanges.btnStartProcess.text")); // NOI18N
        btnStartProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartProcessActionPerformed(evt);
            }
        });

        btnRestartProcess.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnRestartProcess.setText(bundle.getString("panelApproveRejectChanges.btnRestartProcess.text")); // NOI18N

        btnEndProcess.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnEndProcess.setText(bundle.getString("panelApproveRejectChanges.btnEndProcess.text")); // NOI18N
        btnEndProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEndProcessActionPerformed(evt);
            }
        });

        btnComplete.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnComplete.setText(bundle.getString("panelApproveRejectChanges.btnComplete.text")); // NOI18N

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
                        .addComponent(btnStartProcess)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEndProcess)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRestartProcess)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnComplete))
                    .addComponent(lblDocumentChanges, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scrollDocChanges, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE))
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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scrollMembers, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                        .addGap(154, 154, 154))
                    .addComponent(scrollDocChanges, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStartProcess)
                    .addComponent(btnEndProcess)
                    .addComponent(btnRestartProcess)
                    .addComponent(btnComplete))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnStartProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartProcessActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                /**
                 * We populate the process_documents table here
                 */
                doProcessDocuments();
                //doReport();

            }
        });
    }//GEN-LAST:event_btnStartProcessActionPerformed


    private void btnEndProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEndProcessActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //doReportsAll();
            }
        });
    }//GEN-LAST:event_btnEndProcessActionPerformed


    @Override
    public void updatePanel(final HashMap<String, Object> infomap) {
        super.updatePanel(infomap);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                // final BungeniDocAuthor selAuthor = (BungeniDocAuthor) listMembers.getSelectedValue();
                if (infomap.containsKey("updateListFiles")) {
                   tblDocChanges.setModel(new DocumentApproveRejectChangesTableModel());
                    List<String> odfdocs =
                        (List<String>) infomap.get("updateListFiles");
                    changesInfo.reload(odfdocs.toArray(new String[odfdocs.size()]));
                    ((DocApproveRejectListModel) listMembers.getModel()).resetModel();

                }

                if (infomap.containsKey("updateListFile")) {
                    tblDocChanges.setModel(new DocumentApproveRejectChangesTableModel());
                    String odfdoc = (String) infomap.get("updateListFile");
                    changesInfo.reload(odfdoc);
                    ((DocApproveRejectListModel) listMembers.getModel()).resetModel();
                }
            }
        });

    }

    private void selectAuthorinList(BungeniDocAuthor selAut) {
        DocOwnerListModel model = ((DocOwnerListModel) listMembers.getModel());
        int               nSize = model.getSize();

        for (int i = 0; i < nSize; i++) {
            BungeniDocAuthor autAtIndex = model.getAuthorAtIndex(i);

            if (autAtIndex.equals(selAut)) {
                listMembers.setSelectedIndex(i);

                return;
            }
        }
    }

    class Process {

    }

    /**
     * Common list model class used by derived classes
     */
    class DocApproveRejectListModel extends DefaultListModel {

        // List<BungeniOdfDocumentHelper> listDocs = new ArrayList<BungeniOdfDocumentHelper>(0);
        public DocApproveRejectListModel() {
            super();
        }

        @Override
        public Object getElementAt(int iIndex) {
            ProcessDocument pd = (ProcessDocument) get(iIndex);
            return new BungeniDocAuthor(pd.getDocOwner(), "");
        }

        public ProcessDocument getProcessDocumentAt(int iindex) {
            return (ProcessDocument) get(iindex);
        }

        public void resetModel() {
            clear();
            BungeniClientDB db = BungeniClientDB.defaultConnect();
            QueryResults qrDocs = db.ConnectAndQuery(ProcessQueries.GET_LATEST_DOCS_FOR_BILL(CommonFunctions.getCurrentBillID()));
            qrDocs.resultsIterator(new AbstractQueryResultsIterator(){
                @Override
                public boolean iterateRow(QueryResults mQR, Vector<String> rowData) {
                    boolean bState = false;
                    try {
                    ProcessDocument pd = new ProcessDocument (
                        mQR.getField(rowData, "PROCESS_ID"),
                        mQR.getField(rowData, "DOC_NAME"),
                        mQR.getField(rowData, "DOC_PATH"),
                        mQR.getField(rowData, "DOC_OWNER")
                            );
                        addElement(pd);
                        bState = true;
                    } catch (Exception ex) {
                        log.error("resultsIterator : " + ex.getMessage());
                    }
                        return bState;
                }
            });

        }
    }




    private class DocumentApproveRejectChangesTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable aTable, Object aNumberValue, boolean aIsSelected,
                boolean aHasFocus, int aRow, int aColumn) {
            Component p = super.getTableCellRendererComponent(aTable, aNumberValue, aHasFocus, aHasFocus, aRow,
                              aColumn);
            DocumentApproveRejectChangesTableModel tblModel       = ((DocumentApproveRejectChangesTableModel) tblDocChanges.getModel());
            ProcessAmends   mapchangesInfo = tblModel.getModelBase();
                p.setBackground(null);
            return p;
        }
    }


    private class DocumentApproveRejectChangesTableModel extends AbstractTableModel {
        ProcessAmends processAmends = null;

        private String[]              column_names = {
            bundleBase.getString("panelApproveRejectChanges.tblDocChanges.action.text"),
            bundleBase.getString("panelApproveRejectChanges.tblDocChanges.date.text"),
            bundleBase.getString("panelApproveRejectChanges.tblDocChanges.text.text"),
            bundleBase.getString("panelApproveRejectChanges.tblDocChanges.select.text"),
        };

        public DocumentApproveRejectChangesTableModel() {
            processAmends = new ProcessAmends();
        }

        public DocumentApproveRejectChangesTableModel(int iIndex) {
            final int     sIndex  = iIndex;
            DocApproveRejectListModel upd_model = (DocApproveRejectListModel) listMembers.getModel();
            final ProcessDocument pDocument = upd_model.getProcessDocumentAt(iIndex);
            buildModel(pDocument);
        }

        public ProcessAmends getModelBase() {
            return processAmends;
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return (col==3);
        }

        public void updateModel(int iIndex) {
            final int     sIndex  = iIndex;
            DocApproveRejectListModel upd_model = (DocApproveRejectListModel) listMembers.getModel();
            final ProcessDocument pDocument = upd_model.getProcessDocumentAt(iIndex);

            getContainerInterface().startProgress();

            SwingWorker modelWorker = new SwingWorker() {
                @Override
                protected Object doInBackground() {
                    buildModel(pDocument);

                    return Boolean.TRUE;
                }
                @Override
                protected void done() {
                    try {
                        Object bState = get();
                        fireTableDataChanged();
                        getContainerInterface().stopProgress();
                    } catch (InterruptedException ex) {
                        log.error("done ; "+ ex.getMessage());
                    } catch (ExecutionException ex) {
                        log.error("done ; "+ ex.getMessage());
                    }
                }
            };

            modelWorker.execute();
        }

        private void buildModel(final ProcessDocument pDocument) {

            final String  docAuthor = pDocument.getDocOwner();
            final String  docName = pDocument.getDocName();
            final String  procId = pDocument.getProcessId();
            BungeniClientDB db = BungeniClientDB.defaultConnect();
            QueryResults qrAmends = db.ConnectAndQuery(ProcessQueries.GET_LATEST_AMENDS(procId, docName, docAuthor));
            qrAmends.resultsIterator(new AbstractQueryResultsIterator(){
                @Override
                public boolean iterateRow(QueryResults mQR, Vector<String> rowData) {
                  /**
                   * String cId, String cAction, Date cDate, String cText, Boolean cStatus
                   */
                    ProcessAmend pamend = new ProcessAmend(pDocument,
                                                            mQR.getField(rowData, "CHANGE_ID"),
                                                            mQR.getField(rowData, "CHANGE_ACTION"),
                                                            mQR.getField(rowData, "CHANGE_DATE"),
                                                            mQR.getField(rowData, "CHANGE_TEXT"),
                                                            mQR.getField(rowData, "CHANGE_STATUS")
                                                            );
                    processAmends.addProcessAmend(pamend);
                    return Boolean.TRUE;
                }
            });

            System.out.println("building model for " + docAuthor);

            updateMsgNoOfChanges(processAmends.getProcessAmends().size());
        }

        private void updateMsgNoOfChanges(int nSize) {
            Object[]      values = { new Integer(nSize) };
            MessageFormat format =
                new MessageFormat(bundleBase.getString("panelTrackChangesOverview.lblNoOfChanges.text"));
        }

        @Override
        public int getRowCount() {
            if (processAmends == null) {
                return 0;
            }

            return processAmends.getProcessAmends().size();
        }

        @Override
        public int getColumnCount() {
            return column_names.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            ProcessAmend pAmend = processAmends.getProcessAmends().get(rowIndex);

            switch (columnIndex) {
            case 0 :
                return pAmend.getChangeAction();

            case 1 :
                return pAmend.getChangeDate();

            case 2 :
                return pAmend.getChangeText();

            case 3 :
              return pAmend.getChangeStatus();

            default :
                return new String("");
            }
        }

        @Override
           public void setValueAt(Object value, int row, int col) {
                if (col == 3) {
                    //statusMarks.set(row, (Boolean) value);
                    /** update the table here **/
                    fireTableCellUpdated(row, col);
                }
            }


        @Override
        public Class getColumnClass(int col) {
            if (col == 3) {
                return Boolean.class;
            } else if (col == 1 ) {
                return Date.class;
            }else {
                return String.class;
            }
        }

        @Override
        public String getColumnName(int column) {
            return column_names[column];
        }
    }
}
