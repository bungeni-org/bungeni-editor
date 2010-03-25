
package org.bungeni.trackchanges;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FilenameFilter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import javax.swing.AbstractListModel;
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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import org.bungeni.odfdocument.docinfo.BungeniDocAuthor;
import org.bungeni.odfdocument.report.BungeniOdfDocumentReport;
import org.bungeni.odfdocument.report.BungeniOdfDocumentReportTemplate;
import org.bungeni.odfdocument.report.BungeniOdfReportLine;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfChangeContext;
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
import org.w3c.dom.Node;

/**
 *
 * @author Ashok Hariharan
 */
public class panelConsolidateChanges extends panelChangesBase {

 
   
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(panelConsolidateChanges.class.getName());
    //InfiniteProgressPanel m_glassPane =  null;
    private static String __CLERK_NAME__ = "";

    /** Creates new form panelClerkOverview */
    public panelConsolidateChanges() {
        super();
        initComponents();
    }

    public panelConsolidateChanges(JFrame parentFrm) {
        super(parentFrm);
        PANEL_FILTER_REVIEW_STAGE = "ClerkConsolidationReview";
        PANEL_REVIEW_STAGE = "";
        initComponents();
        __CLERK_NAME__ = RuntimeProperties.getDefaultProp("ClerkUser");
        initialize();
        loadFilesFromFolder();

      //  m_glassPane = new InfiniteProgressPanel();
      //  parentFrame.setGlassPane(m_glassPane);

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
               Pattern pat = Pattern.compile(ReviewDocuments.getReviewStage(PANEL_FILTER_REVIEW_STAGE).getDocumentFilterPattern()) ; //("clerk_u[0-9][0-9][0-9][0-9]([a-z0-9_-]*?).odt");
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

    private void initialize_listBoxes(){
        /**
         *
         * Members list box
         *
         * 
         */

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
                    System.out.println("loading table changes");
                    //do struff here
                    displayChangesInfo(nIndex);
                }

            }

        });

        /**
         *
         * Available reports 
         *
         */

        this.listReportTemplates.setModel(new DocReportTemplateListModel());
        this.listReportTemplates.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
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
        tblModel.updateModel(index, false);
    }

    private BungeniOdfDocumentHelper generateReport(int selIndex, BungeniDocAuthor anAuthor, BungeniOdfDocumentReportTemplate reportTemplate){
        BungeniOdfDocumentHelper reportOdfDoc = null;
        try {
            //generate the report from the MP's document
            String billReviewFolder = CommonFunctions.getWorkspaceForBill((String) AppProperties.getProperty("CurrentBillID"));
            String templatesFolder = CommonFunctions.getTemplateFolder();
            //get the author name
            String sAuthor = anAuthor.toString();
            File freportFile = new File(billReviewFolder + File.separator + CommonFunctions.normalizeName(sAuthor) + "_report.odt");
            //BungeniOdfDocumentReportTemplate reportTemplate = new BungeniOdfDocumentReportTemplate(templatesFolder + File.separator + "changes-by-user.odt");
            BungeniOdfDocumentHelper docHelper = changesInfo.getDocuments().get(selIndex);
            //create an xpath object
            XPath docXpath = docHelper.getOdfDocument().getXPath();
            //get the changes helper ont he merged document
            BungeniOdfTrackedChangesHelper changesHelper = docHelper.getChangesHelper();
            //get all the change regions created by the mp
            List<OdfTextChangedRegion> changedRegions = changesHelper.getChangedRegionsByCreator(changesHelper.getTrackedChangeContainer(), sAuthor);
            //iterate through all the changed regions
            //build the report lines
            ArrayList<BungeniOdfReportLine> reportLines = new ArrayList<BungeniOdfReportLine>(0);
            for (OdfTextChangedRegion odfTextChangedRegion : changedRegions) {
                //get the change map for the change - to be sent to the report line object
                StructuredChangeType scType = changesHelper.getStructuredChangeType(odfTextChangedRegion);
                HashMap<String, String> mapOfChange = changesHelper.getChangeInfo(scType);
                String changeType = mapOfChange.get("changeType");
                String changeText = mapOfChange.get("changeText");
                String changeId = mapOfChange.get("changeId");
                //below we build the change context object for the change
                Node foundNodeStart = null;
                Node foundNodeEnd = null;
                Node foundNode = null;
                BungeniOdfChangeContext changeContext = null;

                    if (changeType.equals("insertion")) {
                        //look for text:change-start[@text:change-id
                        String matchNodeStart = "//text:change-start[@text:change-id='" + changeId + "']";
                        String matchNodeEnd = "//text:change-end[@text:change-id='" + changeId + "']";
                        foundNodeStart = (Node) docXpath.evaluate(matchNodeStart, docHelper.getOdfDocument().getContentDom(), XPathConstants.NODE);
                        foundNodeEnd = (Node) docXpath.evaluate(matchNodeEnd, docHelper.getOdfDocument().getContentDom(), XPathConstants.NODE);
                        changeContext = new BungeniOdfChangeContext(foundNodeStart, foundNodeEnd, docHelper);
                    } else if (changeType.equals("deletion")) {
                        //look for text:change [@text:change-id
                        String matchNode = "//text:change[@text:change-id='" + changeId + "']";
                        foundNode = (Node) docXpath.evaluate(matchNode, docHelper.getOdfDocument().getContentDom(), XPathConstants.NODE);
                        changeContext = new BungeniOdfChangeContext(foundNode, docHelper);
                    }
                    BungeniOdfReportLine reportLine = new BungeniOdfReportLine(changeContext, mapOfChange);
                    reportLines.add(reportLine);

            }
            BungeniOdfDocumentReport reportObject = new BungeniOdfDocumentReport(freportFile, reportTemplate);
            //reportObject.addReportVariable("REPORT_HEADER", "Bill Amendments Report");
            //TODO : get the bill name from them MP documents
            reportObject.addReportVariable("BILL_NAME", getBillName());
            //reportObject.addReportVariable("REPORT_FOOTER", "Bill Amendments Report");
            //
            reportObject.addReportVariable("NO_OF_AMENDMENTS", reportLines.size());
            reportObject.addReportVariable("MEMBER_OF_PARLIAMENT", sAuthor);
            reportObject.addReportVariable("OFFICIAL_DATE", reportTemplate.getReportDateFormat().format(Calendar.getInstance().getTime()));
            reportObject.addReportLines(reportLines);
            reportObject.generateReport();
            reportObject.saveReport();
            reportOdfDoc = reportObject.getReportDocument();
        } catch (Exception ex) {
             log.error("doReport : " + ex.getMessage(), ex);
        }
        return reportOdfDoc;
    }

    private String getBillName(){
        String aBillId = (String) AppProperties.getProperty("CurrentBillID");
        String billTitle = "";
        List<BungeniBill> bungeniBills = BungeniBillDataProvider.getData();
        for (BungeniBill bungeniBill : bungeniBills) {
            if (bungeniBill.getID().equals(aBillId)) {
                billTitle = bungeniBill.getTitle();
            }
        }
        if (billTitle.length() == 0 ) {
            return "Unknown Bill Title";
        }
        return billTitle;
    }

    private boolean doReportsAll(){
      boolean bReturn = false;
        final List<BungeniDocAuthor> docAuthors = new ArrayList<BungeniDocAuthor>(){{
                for (int i = 0; i < listMembers.getModel().getSize() ; i++) {
                        add((BungeniDocAuthor)listMembers.getModel().getElementAt(i));
                }
        }};
        final BungeniOdfDocumentReportTemplate selReport = (BungeniOdfDocumentReportTemplate) this.listReportTemplates.getSelectedValue();
        if (docAuthors.size() == 0 ) {
                JOptionPane.showMessageDialog(parentFrame, java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle").getString("no_dox_for_consolidation"));
                bReturn = false;

        }

        if (selReport == null) {
                JOptionPane.showMessageDialog(parentFrame, java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle").getString("no_report_selected"));
                bReturn = false;
                return false;
         } else {
            getContainerInterface().startProgress();
            this.btnReportAll.setEnabled(false);
            SwingWorker reportAllWorker = new SwingWorker(){
                    @Override
                    protected Object doInBackground() throws Exception {
                        List<BungeniOdfDocumentHelper> reportDocs = new ArrayList<BungeniOdfDocumentHelper>(0);
                        for (int i = 0; i < docAuthors.size(); i++) {
                             BungeniDocAuthor docAuthor = docAuthors.get(i);
                             BungeniOdfDocumentHelper reportodfDoc = generateReport(i, docAuthor, selReport);
                             reportDocs.add(reportodfDoc);
                        }
                        return reportDocs;
                    }

                     @Override
                     protected void done() {
                    try {
                        // get the return denvelope
                        List<BungeniOdfDocumentHelper> docs = (List<BungeniOdfDocumentHelper>) this.get();
                        getContainerInterface().stopProgress();
                        btnReportAll.setEnabled(true);
                        boolean errorCondition = false;
                        if (docs != null ) {
                            if (docs.size() > 0 ) {
                                StringBuffer sbBuffer = new StringBuffer();
                                for (BungeniDocAuthor anAuthor : docAuthors) {
                                    sbBuffer.append(anAuthor.toString() + "\n");
                                }
                                JOptionPane.showMessageDialog(parentFrame, java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle").getString("reports_generated_ok") + sbBuffer.toString(), java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle").getString("report_gen_title"), JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                errorCondition = true;
                            }
                        } else {
                            errorCondition = true;
                        }

                        if (errorCondition ) {
                                JOptionPane.showMessageDialog(parentFrame, java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle").getString("report_gen_error_occured") , java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle").getString("report_gen_title"), JOptionPane.WARNING_MESSAGE);
                        }

                        //viewConsolidatedDocument(envelope);
                    } catch (InterruptedException ex) {
                        log.error("consolidateWorkerAll = " + ex.getMessage());
                    } catch (ExecutionException ex) {
                        log.error("consolidateWorkerAll = " + ex.getMessage());
                    }

                     }
            };
            reportAllWorker.execute();
           bReturn = true;
         }
    return bReturn;
    }


    private boolean doReport() {
        boolean bReturn = false;
        //get the selected MP
       // get the selected document index
         final int selIndex = this.listMembers.getSelectedIndex();
         final BungeniOdfDocumentReportTemplate selReport = (BungeniOdfDocumentReportTemplate) this.listReportTemplates.getSelectedValue();

         if (-1 == selIndex) {
                JOptionPane.showMessageDialog(parentFrame, java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle").getString("no_document_selected"));
                bReturn = false;
                return false;
         }
         if (selReport == null) {
                JOptionPane.showMessageDialog(parentFrame, java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle").getString("no_report_selected"));
                bReturn = false;
                return false;
         } else {
            final BungeniDocAuthor selectedAuthor = (BungeniDocAuthor) this.listMembers.getSelectedValue();
            getContainerInterface().startProgress();
            btnReport.setEnabled(false);
            SwingWorker reportWorker = new SwingWorker(){
                    @Override
                    protected Object doInBackground() throws Exception {
                        BungeniOdfDocumentHelper reportdoc  = generateReport(selIndex, selectedAuthor, selReport);
                        return reportdoc;
                    }

                     @Override
                     protected void done() {
                    try {
                        BungeniOdfDocumentHelper reportdoc = (BungeniOdfDocumentHelper) get();
                        getContainerInterface().stopProgress();
                        btnReport.setEnabled(true);
                        if (reportdoc != null) {
                            JOptionPane.showMessageDialog(parentFrame, java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle").getString("report_generated_for") + selectedAuthor.toString(), java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle").getString("report_gen_title"), JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(parentFrame, java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle").getString("report_gen_error_occured") , java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle").getString("report_gen_title"), JOptionPane.WARNING_MESSAGE);
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
        btnReport = new javax.swing.JButton();
        btnEditTemplate = new javax.swing.JButton();
        btnReportAll = new javax.swing.JButton();
        scrollReports = new javax.swing.JScrollPane();
        listReportTemplates = new javax.swing.JList();
        lblAvailableReports = new javax.swing.JLabel();
        btnRefreshDocs = new javax.swing.JButton();

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

        lblDocumentChanges.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblDocumentChanges.setText(bundle.getString("panelTrackChangesOverview.jLabel1.text")); // NOI18N

        btnReport.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
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

        listReportTemplates.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        listReportTemplates.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Tinoula Awopetu", "Mashinski Murigi", "Raul Obwacha", "Felix Kerstengor" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        scrollReports.setViewportView(listReportTemplates);

        lblAvailableReports.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblAvailableReports.setText(bundle.getString("panelConsolidateChanges.lblAvailableReports.text")); // NOI18N

        btnRefreshDocs.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnRefreshDocs.setText(bundle.getString("panelConsolidateChanges.btnRefreshDocs.text")); // NOI18N
        btnRefreshDocs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshDocsActionPerformed(evt);
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
                    .addComponent(lblMembers)
                    .addComponent(scrollReports, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                    .addComponent(lblAvailableReports)
                    .addComponent(btnRefreshDocs))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnReport)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReportAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditTemplate))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAvailableReports)
                        .addGap(9, 9, 9)
                        .addComponent(scrollReports, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE))
                    .addComponent(scrollDocChanges, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReport)
                    .addComponent(btnReportAll)
                    .addComponent(btnEditTemplate)
                    .addComponent(btnRefreshDocs))
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

    private void btnRefreshDocsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshDocsActionPerformed
         final BungeniDocAuthor selAuthor = (BungeniDocAuthor) listMembers.getSelectedValue();
         initialize_listBoxes();
         this.loadFilesFromFolder();
         System.out.println("After intListBox");
         if (selAuthor != null) {
                 selectAuthorinList(selAuthor);
         }
    }//GEN-LAST:event_btnRefreshDocsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditTemplate;
    private javax.swing.JButton btnRefreshDocs;
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

    @Override
    public void updatePanel(final HashMap<String, Object> infomap) {

       super.updatePanel(infomap);
       /*
       final BungeniDocAuthor selAuthor = (BungeniDocAuthor) listMembers.getSelectedValue();
       //clear the list pre-emptively since we are going to reload it in the swing-worker thread
       listMembers.setModel(new DefaultListModel());
       SwingWorker panelUpdateWorker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                    loadFilesFromFolder();
                    return Boolean.TRUE;
            }

            @Override
            protected void done(){
                listMembers.setModel(new DocOwnerListModel());
                if (infomap.size() == 0 ) {
                    if (selAuthor != null) {
                        selectAuthorinList(selAuthor);
                    }
                } else {
                     if (infomap.containsKey("selectedAuthor")) {
                        BungeniDocAuthor selAut  = (BungeniDocAuthor) infomap.get("selectedAuthor");
                        selectAuthorinList(selAut);
                    }
              }
            }
       };
       panelUpdateWorker.execute(); */
       /*
       SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                 if (infomap.size() == 0) {
                    BungeniDocAuthor selAuthor = (BungeniDocAuthor) listMembers.getSelectedValue();
                    loadFilesFromFolder();
                    DocOwnerListModel docOwnersModel = new DocOwnerListModel();
                    listMembers.setModel(new DocOwnerListModel());
                    listMembers.updateUI();
                    if (selAuthor != null) {
                        selectAuthorinList(selAuthor);
                    }

                 } else {
                     loadFilesFromFolder();
                     listMembers.setModel(new DocOwnerListModel());
                     listMembers.updateUI();
                     if (infomap.containsKey("selectedAuthor")) {
                        BungeniDocAuthor selAut  = (BungeniDocAuthor) infomap.get("selectedAuthor");
                        selectAuthorinList(selAut);
                    }
                 }
            }
        }); */
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
            HashMap<String,String> mapchangesInfo = tblModel.getModelBase().get(aRow);
            String dcCreator = mapchangesInfo.get("dcCreator");
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
            final int sIndex = iIndex; final boolean bFilter = bFilterbyAuthor;
            getContainerInterface().startProgress();
            SwingWorker modelWorker = new SwingWorker(){
                @Override
                protected Object doInBackground()  {
                     buildModel(sIndex, bFilter);
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


     class DocReportTemplateListModel extends AbstractListModel {
         List<BungeniOdfDocumentReportTemplate> reportTemplates = new ArrayList<BungeniOdfDocumentReportTemplate>(0);

         public DocReportTemplateListModel(){
             super();
             buildModel();
         }

         private void buildModel(){
             //get section name of reports in app.ini
             List<String> reportRefs = CommonFunctions.getAvailableReportReferences();
             reportTemplates.clear();
             for (String reportRef : reportRefs) {
                try {
                    //get the report template
                    List<String> reportTemplateFile = RuntimeProperties.getSectionProp(reportRef, "report.template");
                    String pathToTemplateFile = CommonFunctions.getTemplateFolder() + File.separator + reportTemplateFile.get(0);
                    BungeniOdfDocumentReportTemplate rptTemplate = new BungeniOdfDocumentReportTemplate(pathToTemplateFile);
                    reportTemplates.add(rptTemplate);
                } catch (Exception ex) {
                    log.error("buildModel : " + ex.getMessage(), ex);
                }
             }

         }

         public int getSize() {
          return reportTemplates.size();
        }

        @Override
        public Object getElementAt(int arg0) {
            return reportTemplates.get(arg0);
        }

    }


}
