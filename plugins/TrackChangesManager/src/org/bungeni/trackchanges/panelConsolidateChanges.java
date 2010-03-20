
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
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import org.bungeni.odfdocument.docinfo.BungeniDocAuthor;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfChangeContext;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper.StructuredChangeType;
import org.bungeni.trackchanges.ui.support.TextAreaRenderer;
import org.bungeni.trackchanges.utils.AppProperties;
import org.bungeni.trackchanges.utils.CommonFunctions;
import org.bungeni.trackchanges.utils.ReviewDocuments;
import org.odftoolkit.odfdom.doc.text.OdfTextChangedRegion;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Ashok Hariharan
 */
public class panelConsolidateChanges extends panelChangesBase {

 
   
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(panelConsolidateChanges.class.getName());



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
               Pattern pat = Pattern.compile(ReviewDocuments.getReviewStage(PANEL_FILTER_REVIEW_STAGE).getDocumentFilterPattern()) ; //("clerk_u[0-9][0-9][0-9][0-9]([a-z0-9_-]*?).odt");
                    public boolean accept(File dir, String name) {
                        if (pat.matcher(name).matches()) {
                            return true;
                        }
                        return false;
                    }

                });

              changesInfo.reload(files);
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
        tblModel.updateModel(index, false);
    }


    private boolean doReport() {
        boolean bReturn = false;
        //get the selected MP
       // get the selected document index
            final int selIndex = this.listMembers.getSelectedIndex();
            if (-1 == selIndex) {
                JOptionPane.showMessageDialog(parentFrame, "No document was selected. Please select a document for review");
                bReturn = false;
                return false;
            }
        //generate the report from the MP's document
         String sAuthor =    (String) this.listMembers.getSelectedValue();
         BungeniOdfDocumentHelper docHelper = changesInfo.getDocuments().get(selIndex);
         XPath docXpath = docHelper.getOdfDocument().getXPath();
         BungeniOdfTrackedChangesHelper changesHelper = docHelper.getChangesHelper();
         List<OdfTextChangedRegion> changedRegions = changesHelper.getChangedRegionsByCreator(changesHelper.getTrackedChangeContainer(), sAuthor);
         for (OdfTextChangedRegion odfTextChangedRegion : changedRegions) {
            StructuredChangeType scType = changesHelper.getStructuredChangeType(odfTextChangedRegion);
            HashMap<String,String> mapOfChange = changesHelper.getChangeInfo(scType);
            String changeType = mapOfChange.get("changeType");
            String changeText = mapOfChange.get("changeText");
            String changeId = mapOfChange.get("changeId");
            Node foundNodeStart = null, foundNodeEnd = null, foundNode = null;
            try {
                if (changeType.equals("insertion")) {
                        //look for text:change-start[@text:change-id
                        String matchNodeStart = "//text:change-start[@text:change-id='" + changeId + "']";
                        String matchNodeEnd = "//text:change-end[@text:change-id='" + changeId + "']";
                        foundNodeStart = (Node) docXpath.evaluate(matchNodeStart, docHelper.getOdfDocument().getContentDom(), XPathConstants.NODE);
                        foundNodeEnd = (Node) docXpath.evaluate(matchNodeEnd, docHelper.getOdfDocument().getContentDom(), XPathConstants.NODE);
                        BungeniOdfChangeContext changeContext = new BungeniOdfChangeContext(foundNodeStart, foundNodeEnd, docHelper );

                } else if (changeType.equals("deletion")) {
                        //look for text:change [@text:change-id
                        String matchNode = "//text:change[@text:change-id='" + changeId + "']";
                        foundNode = (Node) docXpath.evaluate(matchNode, docHelper.getOdfDocument().getContentDom(), XPathConstants.NODE);
                        BungeniOdfChangeContext changeContext = new BungeniOdfChangeContext(foundNode, docHelper );
                }
            } catch (Exception ex) {
                log.error("doReport : " + ex.getMessage(), ex);
            }
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

        btnReport.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnReport.setText(bundle.getString("panelConsolidateChanges.btnReport.text")); // NOI18N
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportActionPerformed(evt);
            }
        });

        btnEditTemplate.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnEditTemplate.setText(bundle.getString("panelConsolidateChanges.btnEditTemplate.text")); // NOI18N

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
                        .addComponent(btnReport)
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(scrollDocChanges, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnReport)
                            .addComponent(btnEditTemplate))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scrollMembers, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                        .addGap(209, 209, 209))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportActionPerformed
        // TODO add your handling code here:
        doReport();
    }//GEN-LAST:event_btnReportActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditTemplate;
    private javax.swing.JButton btnReport;
    private javax.swing.JLabel lblDocumentChanges;
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
