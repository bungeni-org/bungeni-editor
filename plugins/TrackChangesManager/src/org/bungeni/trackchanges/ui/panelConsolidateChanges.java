package org.bungeni.trackchanges.ui;


//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.odfdocument.docinfo.BungeniDocAuthor;
import org.bungeni.odfdocument.report.BungeniOdfDocumentReport;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper.StructuredChangeType;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
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

import org.bungeni.odfdocument.report.BungeniOdfUserReport;
import org.bungeni.odfdocument.report.BungeniOdfUserReport.ReportType;
import org.bungeni.odfdocument.report.DocUserReportsListModel;
import org.bungeni.odfdom.utils.BungeniOdfDateHelper;

/**
 *
 * @author Ashok Hariharan
 */
public class panelConsolidateChanges extends panelChangesBase {

    // InfiniteProgressPanel m_glassPane =  null;
    private static String                  __CLERK_NAME__ = "";
    private static org.apache.log4j.Logger log            =
        org.apache.log4j.Logger.getLogger(panelConsolidateChanges.class.getName());

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditTemplate;
    private javax.swing.JButton btnReport;
    private javax.swing.JButton btnReportAll;
    private javax.swing.JLabel lblAvailableReports;
    private javax.swing.JLabel lblDocumentChanges;
    private javax.swing.JLabel lblMembers;
    private javax.swing.JList listMembers;
    private javax.swing.JList listReportTemplates;
    private javax.swing.JScrollPane scrollDocChanges;
    private javax.swing.JScrollPane scrollMembers;
    private javax.swing.JScrollPane scrollReports;
    private javax.swing.JTable tblDocChanges;
    // End of variables declaration//GEN-END:variables

    /** Creates new form panelClerkOverview */
    public panelConsolidateChanges() {
        super();
        initComponents();
    }

    public panelConsolidateChanges(JFrame parentFrm, String pName) {
        super(parentFrm, pName);
        PANEL_FILTER_REVIEW_STAGE = "ClerkConsolidationReview";
        PANEL_REVIEW_STAGE        = "";
        initComponents();
        __CLERK_NAME__ = RuntimeProperties.getDefaultProp("ClerkUser");
        loadFilesFromFolder();
        initialize();

        // m_glassPane = new InfiniteProgressPanel();
        // parentFrame.setGlassPane(m_glassPane);
    }

    private void initialize() {
        initialize_listBoxes();
        initialize_Tables();
    }

   
    private void initialize_listBoxes() {

        /**
         *
         * Members list box
         *
         *
         */
        DocConsolidateListModel consModel = new DocConsolidateListModel();

        consModel.resetModel();
        listMembers.setModel(consModel);
        listMembers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ListSelectionModel lsm = listMembers.getSelectionModel();

        lsm.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent lse) {
                ListSelectionModel lsm = (ListSelectionModel) lse.getSource();

                if (lse.getValueIsAdjusting()) {
                    return;
                }

                int firstIndex = lse.getFirstIndex();
                int lastIndex  = lse.getLastIndex();

                if (lsm.isSelectionEmpty()) {
                    return;
                } else {

                    // Find out which indexes are selected.
                    int nIndex = lsm.getMinSelectionIndex();

              
                    // do struff here
                    displayChangesInfo(nIndex);
                }
            }
        });

        /**
         *
         * Available reports
         *
         */
        this.listReportTemplates.setModel(new DocUserReportsListModel()); //new DocReportTemplateListModel());
        this.listReportTemplates.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void initialize_Tables() {
        this.tblDocChanges.setModel(new DocumentChangesTableModel());

        TableColumnModel tcmModel = this.tblDocChanges.getColumnModel();

        this.tblDocChanges.setDefaultRenderer(String.class, new DocumentChangesTableCellRenderer());

        TextAreaRenderer textAreaRenderer = new TextAreaRenderer();

        tcmModel.getColumn(2).setCellRenderer(textAreaRenderer);

         this.tblDocChanges.getTableHeader().setFont(this.tblDocChanges.getFont());
    }

    private void displayChangesInfo(int index) {
        DocumentChangesTableModel tblModel = (DocumentChangesTableModel) this.tblDocChanges.getModel();

        tblModel.updateModel(index, false);
    }

   private String generateReport (BungeniOdfUserReport userReport, BungeniOdfDocumentHelper aDoc) {
       BungeniOdfDocumentReport reportObject = userReport.getReportProcess().generateReport(userReport.getReportTemplate(), aDoc);
       String reportPath = reportObject.getReportPath();
       return reportPath;
   }


   
    private boolean doReportsAll() {
        boolean                      bReturn    = false;
        final List<BungeniDocAuthor> docAuthors = new ArrayList<BungeniDocAuthor>() {
            {
                for (int i = 0; i < listMembers.getModel().getSize(); i++) {
                    add((BungeniDocAuthor) listMembers.getModel().getElementAt(i));
                }
            }
        };
        final BungeniOdfUserReport selReport =
            (BungeniOdfUserReport) this.listReportTemplates.getSelectedValue();
        if (docAuthors.size() == 0) {
            JOptionPane.showMessageDialog(
                parentFrame,
                java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle").getString(
                    "no_dox_for_consolidation"));
            bReturn = false;
        }

        if (selReport == null) {
            JOptionPane.showMessageDialog(
                parentFrame,
                java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle").getString("no_report_selected"));
            bReturn = false;

            return false;
        } else {
            //check if the report has a UI
            //if it has a UI the report processing is done by the report UI
            if (selReport.hasReportUI()) {
                selReport.getReportUI().setInputDocuments(changesInfo.getDocuments().toArray(new BungeniOdfDocumentHelper[changesInfo.getDocuments().size()]));
                selReport.getReportUI().showUI(parentFrame);
                selReport.getReportUI().initUI();
            } else {
                //no UI so process inplace
                getContainerInterface().startProgress();
                this.btnReportAll.setEnabled(false);

                AllReportsWorker reportAllWorker = new AllReportsWorker(selReport, docAuthors);
                reportAllWorker.execute();

                bReturn = true;
            }
        }

        return bReturn;
    }

    class AllReportsWorker extends SwingWorker {

            BungeniOdfUserReport selReport = null;
            List<BungeniDocAuthor> docAuthors = null;
                public AllReportsWorker (BungeniOdfUserReport aReport, List<BungeniDocAuthor> lauthors) {
                        this.docAuthors = lauthors;
                        this.selReport = aReport;
                }

                @Override
                protected Object doInBackground() throws Exception {
                    List<BungeniOdfDocumentHelper> docs = changesInfo.getDocuments();
                    List<String> reports = new ArrayList<String>(0);
                    if (selReport.getReportType().equals(ReportType.MultiInputSingleReport)) {
                        BungeniOdfDocumentReport dMReport = selReport.runProcess(docs.toArray(new BungeniOdfDocumentHelper[docs.size()]));
                        reports.add(dMReport.getReportPath());
                    }
                    if (selReport.getReportType().equals(ReportType.SingleInputSingleReport)) {
                        //  BungeniOdfDocumentReport docReport = selReport.runProcess(docs.toArray(new BungeniOdfDocumentHelper[docs.size()]));
                        for (BungeniOdfDocumentHelper bungeniOdfDocumentHelper : docs) {
                            BungeniOdfDocumentHelper[] arrProcDocHelper = {
                                bungeniOdfDocumentHelper
                            };
                            BungeniOdfDocumentReport dSReport = selReport.runProcess(arrProcDocHelper);
                            reports.add(dSReport.getReportPath());
                            //
                            //String reportDoc = generateReport (selReport,bungeniOdfDocumentHelper );
                            //reports.add(reportDoc);
                        }
                    }
                    return reports;
                }
                @Override
                protected void done() {
                    try {

                        // get the return denvelope
                        List<String> docs = (List<String>) this.get();
                        Thread.sleep(2000);
                        getContainerInterface().stopProgress();
                        btnReportAll.setEnabled(true);

                        boolean errorCondition = false;

                        if (docs != null) {
                            if (docs.size() > 0) {
                                StringBuffer sbBuffer = new StringBuffer();

                                for (BungeniDocAuthor anAuthor : docAuthors) {
                                    sbBuffer.append(anAuthor.toString() + "\n");
                                }

                                JOptionPane
                                    .showMessageDialog(parentFrame,
                                                       java.util.ResourceBundle
                                                           .getBundle("org/bungeni/trackchanges/Bundle")
                                                           .getString("reports_generated_ok") + sbBuffer
                                                           .toString(), java.util.ResourceBundle
                                                           .getBundle("org/bungeni/trackchanges/Bundle")
                                                           .getString("report_gen_title"), JOptionPane
                                                           .INFORMATION_MESSAGE);
                            } else {
                                errorCondition = true;
                            }
                        } else {
                            errorCondition = true;
                        }

                        if (errorCondition) {
                            JOptionPane.showMessageDialog(
                                parentFrame, bundleBase.getString(
                                    "report_gen_error_occured"), bundleBase.getString(
                                    "report_gen_title"), JOptionPane.WARNING_MESSAGE);
                        }

                        // viewConsolidatedDocument(envelope);
                    } catch (InterruptedException ex) {
                        log.error("consolidateWorkerAll = " + ex.getMessage());
                    } catch (ExecutionException ex) {
                        log.error("consolidateWorkerAll = " + ex.getMessage());
                    }
                }

    }

    private boolean doReport() {
        boolean bReturn = false;

        // get the selected MP
        // get the selected document index
        final int                              selIndex  = this.listMembers.getSelectedIndex();
        final BungeniOdfUserReport selReport =
            (BungeniOdfUserReport) this.listReportTemplates.getSelectedValue();
        //check if only the global report is valid for this report type
        if (selReport.getReportType().equals(ReportType.MultiInputSingleReport)) {
            JOptionPane.showMessageDialog(
                parentFrame,
                java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle").getString(
                    "only_global_report"));
            bReturn = false;
            return false;
        }
        final BungeniOdfDocumentHelper selDocument = this.changesInfo.getDocuments().get(selIndex);
        if (-1 == selIndex) {
            JOptionPane.showMessageDialog(
                parentFrame,
                java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle").getString(
                    "no_document_selected"));
            bReturn = false;

            return false;
        }

        if (selReport == null) {
            JOptionPane.showMessageDialog(
                parentFrame,
                java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle").getString("no_report_selected"));
            bReturn = false;

            return false;
        } else {
            final BungeniDocAuthor selectedAuthor = (BungeniDocAuthor) this.listMembers.getSelectedValue();

            getContainerInterface().startProgress();
            btnReport.setEnabled(false);

            SwingWorker reportWorker = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    ///change this to use an array
                    BungeniOdfDocumentHelper[] arrDocHelper = {
                        selDocument
                    } ;
                    BungeniOdfDocumentReport dReport = selReport.runProcess(arrDocHelper);
                    //String reportdoc = generateReport(selReport, arrDocHelper) ;

                    return dReport.getReportPath();
                }

                @Override
                protected void done() {
                    try {
                        String reportdoc = (String) get();
                        getContainerInterface().stopProgress();
                        btnReport.setEnabled(true);

                        if (reportdoc != null) {
                            JOptionPane
                                .showMessageDialog(parentFrame,
                                                   java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle")
                                                       .getString("report_generated_for") + selectedAuthor
                                                       .toString(), java.util.ResourceBundle
                                                       .getBundle("org/bungeni/trackchanges/Bundle")
                                                       .getString("report_gen_title"), JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(
                                parentFrame, java.util.ResourceBundle.getBundle(
                                    "org/bungeni/trackchanges/Bundle").getString(
                                    "report_gen_error_occured"), java.util.ResourceBundle.getBundle(
                                    "org/bungeni/trackchanges/Bundle").getString(
                                    "report_gen_title"), JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (InterruptedException ex) {
                        log.error("consolidateWorker = " + ex.getMessage());
                    } catch (ExecutionException ex) {
                        log.error("consolidateWorker = " + ex.getMessage());
                    }
                }
            };

            reportWorker.execute();
        }

        return true;
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
        btnReport = new javax.swing.JButton();
        btnEditTemplate = new javax.swing.JButton();
        btnReportAll = new javax.swing.JButton();
        scrollReports = new javax.swing.JScrollPane();
        listReportTemplates = new javax.swing.JList();
        lblAvailableReports = new javax.swing.JLabel();

        listMembers.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        listMembers.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Tinoula Awopetu", "Mashinski Murigi", "Raul Obwacha", "Felix Kerstengor" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        scrollMembers.setViewportView(listMembers);

        lblMembers.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblMembers.setLabelFor(listMembers);
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

        btnReport.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnReport.setText(bundle.getString("panelConsolidateChanges.btnReport.text")); // NOI18N
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportActionPerformed(evt);
            }
        });

        btnEditTemplate.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnEditTemplate.setText(bundle.getString("panelConsolidateChanges.btnEditTemplate.text")); // NOI18N

        btnReportAll.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnReportAll.setText(bundle.getString("panelConsolidateChanges.btnReportAll.text")); // NOI18N
        btnReportAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportAllActionPerformed(evt);
            }
        });

        listReportTemplates.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        listReportTemplates.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Report 1", "Report 2" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        scrollReports.setViewportView(listReportTemplates);

        lblAvailableReports.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblAvailableReports.setText(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollMembers, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                    .addComponent(lblMembers)
                    .addComponent(scrollReports, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                    .addComponent(lblAvailableReports))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnReport)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReportAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditTemplate))
                    .addComponent(lblDocumentChanges, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scrollDocChanges, javax.swing.GroupLayout.DEFAULT_SIZE, 741, Short.MAX_VALUE))
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
                        .addComponent(scrollMembers, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAvailableReports)
                        .addGap(9, 9, 9)
                        .addComponent(scrollReports, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))
                    .addComponent(scrollDocChanges, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReport)
                    .addComponent(btnReportAll)
                    .addComponent(btnEditTemplate))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                doReport();
            }
        });
    }//GEN-LAST:event_btnReportActionPerformed


    private void btnReportAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportAllActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                doReportsAll();
            }
        });
    }//GEN-LAST:event_btnReportAllActionPerformed


    @Override
    public void updatePanel(final HashMap<String, Object> infomap) {
        super.updatePanel(infomap);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                // final BungeniDocAuthor selAuthor = (BungeniDocAuthor) listMembers.getSelectedValue();
                if (infomap.containsKey("updateListFiles")) {
                   tblDocChanges.setModel(new DocumentChangesTableModel());
                    List<String> odfdocs =
                        (List<String>) infomap.get("updateListFiles");
                    changesInfo.reload(odfdocs.toArray(new String[odfdocs.size()]));
                    ((DocConsolidateListModel) listMembers.getModel()).resetModel();

                }

                if (infomap.containsKey("updateListFile")) {
                    tblDocChanges.setModel(new DocumentChangesTableModel());
                    String odfdoc = (String) infomap.get("updateListFile");
                    changesInfo.reload(odfdoc);
                    ((DocConsolidateListModel) listMembers.getModel()).resetModel();
                }
            }
        });

        /*
         * final BungeniDocAuthor selAuthor = (BungeniDocAuthor) listMembers.getSelectedValue();
         * //clear the list pre-emptively since we are going to reload it in the swing-worker thread
         * listMembers.setModel(new DefaultListModel());
         * SwingWorker panelUpdateWorker = new SwingWorker() {
         *    @Override
         *    protected Object doInBackground() throws Exception {
         *            loadFilesFromFolder();
         *            return Boolean.TRUE;
         *    }
         *
         *    @Override
         *    protected void done(){
         *        listMembers.setModel(new DocOwnerListModel());
         *        if (infomap.size() == 0 ) {
         *            if (selAuthor != null) {
         *                selectAuthorinList(selAuthor);
         *            }
         *        } else {
         *             if (infomap.containsKey("selectedAuthor")) {
         *                BungeniDocAuthor selAut  = (BungeniDocAuthor) infomap.get("selectedAuthor");
         *                selectAuthorinList(selAut);
         *            }
         *      }
         *    }
         * };
         * panelUpdateWorker.execute();
         */

        /*
         * SwingUtilities.invokeLater(new Runnable() {
         *    public void run() {
         *         if (infomap.size() == 0) {
         *            BungeniDocAuthor selAuthor = (BungeniDocAuthor) listMembers.getSelectedValue();
         *            loadFilesFromFolder();
         *            DocOwnerListModel docOwnersModel = new DocOwnerListModel();
         *            listMembers.setModel(new DocOwnerListModel());
         *            listMembers.updateUI();
         *            if (selAuthor != null) {
         *                selectAuthorinList(selAuthor);
         *            }
         *
         *         } else {
         *             loadFilesFromFolder();
         *             listMembers.setModel(new DocOwnerListModel());
         *             listMembers.updateUI();
         *             if (infomap.containsKey("selectedAuthor")) {
         *                BungeniDocAuthor selAut  = (BungeniDocAuthor) infomap.get("selectedAuthor");
         *                selectAuthorinList(selAut);
         *            }
         *         }
         *    }
         * });
         */
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

    /**
     * Common list model class used by derived classes
     */
    class DocConsolidateListModel extends DefaultListModel {

        // List<BungeniOdfDocumentHelper> listDocs = new ArrayList<BungeniOdfDocumentHelper>(0);
        public DocConsolidateListModel() {
            super();
        }

        @Override
        public Object getElementAt(int iIndex) {
            BungeniOdfDocumentHelper docHelper = (BungeniOdfDocumentHelper) get(iIndex);

            return new BungeniDocAuthor(
                docHelper.getPropertiesHelper().getUserDefinedPropertyValue("BungeniDocAuthor"), "");
        }

        public void resetModel() {
            clear();

            ArrayList<BungeniOdfDocumentHelper> listdocs = changesInfo.getDocuments();

            for (BungeniOdfDocumentHelper bungeniOdfDocumentHelper : listdocs) {
                this.addElement(bungeniOdfDocumentHelper);
            }
        }
    }




    private class DocumentChangesTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable aTable, Object aNumberValue, boolean aIsSelected,
                boolean aHasFocus, int aRow, int aColumn) {
            Component p = super.getTableCellRendererComponent(aTable, aNumberValue, aHasFocus, aHasFocus, aRow,
                              aColumn);
            DocumentChangesTableModel tblModel       = ((DocumentChangesTableModel) tblDocChanges.getModel());
            HashMap<String, Object>   mapchangesInfo = tblModel.getModelBase().get(aRow);
            String                    dcCreator      = mapchangesInfo.get("dcCreator").toString();

            if (dcCreator.equals(__CLERK_NAME__)) {
                p.setBackground(Color.magenta);
            } else {
                p.setBackground(null);
            }

            return p;
        }
    }


    private class DocumentChangesTableModel extends AbstractTableModel {
        List<HashMap<String, Object>> changeMarks  = new ArrayList<HashMap<String, Object>>(0);
        private String[]              column_names = {
            bundleBase.getString("panelTrackChanges.tblDocChanges.action.text"),
            bundleBase.getString("panelTrackChanges.tblDocChanges.date.text"),
            bundleBase.getString("panelTrackChanges.tblDocChanges.text.text")

            // rBundle.getString("panelTrackChanges.tblDocChanges.position.text"),
            // rBundle.getString("panelTrackChanges.tblDocChanges.status.text")
        };

        public DocumentChangesTableModel() {
            changeMarks = new ArrayList<HashMap<String, Object>>(0);
        }

        public DocumentChangesTableModel(int iIndex, boolean bFilterbyAuthor) {
            buildModel(iIndex, bFilterbyAuthor);
        }

        public List<HashMap<String, Object>> getModelBase() {
            return changeMarks;
        }

        public void updateModel(int iIndex, boolean bFilterbyAuthor) {
            final int     sIndex  = iIndex;
            final boolean bFilter = bFilterbyAuthor;

            getContainerInterface().startProgress();

            SwingWorker modelWorker = new SwingWorker() {
                @Override
                protected Object doInBackground() {
                    buildModel(sIndex, bFilter);

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

        private void buildModel(int iIndex, boolean bFilterByAuthor) {
            changeMarks.clear();

            BungeniOdfDocumentHelper        docHelper       = changesInfo.getDocuments().get(iIndex);
            String                          docAuthor       =
                docHelper.getPropertiesHelper().getUserDefinedPropertyValue("BungeniDocAuthor");
           
            BungeniOdfTrackedChangesHelper  changeHelper    = docHelper.getChangesHelper();
            Element                         changeContainer = changeHelper.getTrackedChangeContainer();
            ArrayList<OdfTextChangedRegion> changes         = new ArrayList<OdfTextChangedRegion>(0);

            if (!bFilterByAuthor) {
                changes = changeHelper.getChangedRegions(changeContainer);
            } else {
                changes = changeHelper.getChangedRegionsByCreator(changeContainer, docAuthor);
            }

            for (OdfTextChangedRegion odfTextChangedRegion : changes) {
                StructuredChangeType    scType     = changeHelper.getStructuredChangeType(odfTextChangedRegion);
                HashMap<String, Object> changeMark = changeHelper.getChangeInfo(scType);

                changeMarks.add(changeMark);
            }

            updateMsgNoOfChanges(changeMarks.size());
        }

        private void updateMsgNoOfChanges(int nSize) {
            Object[]      values = { new Integer(nSize) };
            MessageFormat format =
                new MessageFormat(bundleBase.getString("panelTrackChangesOverview.lblNoOfChanges.text"));
        }

        @Override
        public int getRowCount() {
            if (changeMarks == null) {
                return 0;
            }

            return changeMarks.size();
        }

        @Override
        public int getColumnCount() {
            return column_names.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            HashMap<String, Object> changeMark = changeMarks.get(rowIndex);

            

            switch (columnIndex) {
            case 0 :
                return changeMark.get("changeType");

            case 1 :
                return BungeniOdfDateHelper.odfDateToPresentationDate(changeMark.get("dcDate").toString());

            case 2 :
                if (changeMark.containsKey("changeText")) {
                    return changeMark.get("changeText");
                } else {
                    return new String("");
                }

            // case 3 :
            // return true;
            default :
                return new String("");
            }
        }

        @Override
        public Class getColumnClass(int col) {
            if (col == 3) {
                return Boolean.class;
            } else {
                return String.class;
            }
        }

        @Override
        public String getColumnName(int column) {
            return column_names[column];
        }
    }
}
