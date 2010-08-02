package org.bungeni.trackchanges.ui;
//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.odfdocument.docinfo.BungeniDocAuthor;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper.StructuredChangeType;
import org.bungeni.trackchanges.registrydata.BungeniBill;
import org.bungeni.trackchanges.ui.support.TextAreaRenderer;
import org.bungeni.trackchanges.uno.UnoOdfFile;
import org.bungeni.trackchanges.uno.UnoOdfOpenFiles;
import org.bungeni.trackchanges.utils.AppProperties;
import org.bungeni.trackchanges.utils.CommonFunctions;
import org.bungeni.trackchanges.utils.ReviewDocuments;

import org.odftoolkit.odfdom.doc.text.OdfTextChangedRegion;

import org.w3c.dom.Element;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FilenameFilter;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import org.bungeni.odfdom.utils.BungeniOdfDateHelper;
import org.bungeni.trackchanges.init.TrackChangesInit;
import org.bungeni.trackchanges.rss.BungeniBillDataProvider;

/**
 * This is the first tab / workflow in the bill amendments for the clerk
 * This tab loads the documents and provides options for the clerk to review
 * the documents submitted by the users
 * @author  Ashok Hariharan
 */
public class panelTrackChangesOverview extends panelChangesBase {
    private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(panelTrackChangesOverview.class.getName());
    String                 __CURRENT_BILL_FOLDER__ = "";
    String                 __TEST_FOLDER__         = "";
    ArrayList<BungeniBill> bungeniBills            =  new ArrayList<BungeniBill>(0);


    /** Creates new form panelTrackChanges */
    public panelTrackChangesOverview(JFrame parentFrame, String pName) {
        super(parentFrame, pName);
        PANEL_FILTER_REVIEW_STAGE = "Default";
        PANEL_REVIEW_STAGE = "ClerkReview";
        initComponents();
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

                    displayDocInfo(nIndex);
                    displayChangesInfo(nIndex);
                }
            }
        });
    }

    private void initialize_comboBoxes() {
        this.bungeniBills = (ArrayList<BungeniBill>) BungeniBillDataProvider.getData();
        this.cboBills.setModel(new BillsComboBoxModel(bungeniBills.toArray(new BungeniBill[bungeniBills.size()])));
        this.cboBills.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BungeniBill bBill  = (BungeniBill) cboBills.getSelectedItem();
                String      billId = bBill.getID();
                log.error("cboBill.actionPerformed  :" + billId);
                __CURRENT_BILL_FOLDER__ = CommonFunctions.getWorkspaceForBill(billId);
                log.error("cboBill.current_bill_folder  :" + __CURRENT_BILL_FOLDER__);
                AppProperties.setProperty("CurrentBillID", billId);
                loadFilesFromFolder();
            }
        });
        this.cboBills.setSelectedItem(bungeniBills.get(0));
    }

    private void initialize_listBoxes() {
        listMembers.setModel(new DocOwnerListModel());
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

                    displayDocInfo(nIndex);
                    displayChangesInfo(nIndex);
                }
            }
        });
    }

    private void initialize_Tables() {
        this.tblDocChanges.setModel(new DocumentChangesTableModel());

        TableColumnModel tcmModel         = this.tblDocChanges.getColumnModel();
        TextAreaRenderer textAreaRenderer = new TextAreaRenderer();

        tcmModel.getColumn(2).setCellRenderer(textAreaRenderer);
        this.tblDocChanges.getTableHeader().setFont(this.tblDocChanges.getFont());
    }

    

    private void displayDocInfo(int index) {
        BungeniOdfDocumentHelper docHelper = changesInfo.getDocuments().get(index);
        StringBuffer             sbDoc     = new StringBuffer();
        File                     fFile     = new File(docHelper.getOdfDocument().getBaseURI());

        sbDoc.append("Name:" + fFile.getName() + "\n");
        sbDoc.append("Created on: " + docHelper.getPropertiesHelper().getMetaCreationDate() + "\n");
        sbDoc.append("No. of times edited : " + docHelper.getPropertiesHelper().getMetaEditingCycles() + "\n");
        sbDoc.append("Editing duration : " + docHelper.getPropertiesHelper().getMetaEditingDuration() + "\n");
        this.txtareaDocInfo.setText(sbDoc.toString());
    }

    private void displayChangesInfo(int index) {
        DocumentChangesTableModel tblModel = (DocumentChangesTableModel) this.tblDocChanges.getModel();

        tblModel.updateModel(index);
    }

    private BungeniOdfDocumentHelper getSelectedDocument() {
        Object selectedValue = this.listMembers.getSelectedValue();

        if (selectedValue != null) {
            return (BungeniOdfDocumentHelper) selectedValue;
        }

        return null;
    }

    private void doReview() {
        try {

            // get the selected document index
            final int selIndex = this.listMembers.getSelectedIndex();

            if (-1 == selIndex) {
                JOptionPane.showMessageDialog(parentFrame,
                                              "No document was selected. Please select a document for review");

                return;
            }


            BungeniOdfDocumentHelper docHelper = changesInfo.getDocuments().get(selIndex);

            // create a clerk review document of the mp's document
            // this is a copy of the MP's document
            ReviewDocuments                rvd            = new ReviewDocuments(docHelper, this.PANEL_REVIEW_STAGE);    // TODO

            if (rvd.reviewCopyExists()) {
                boolean bOpen = true;

                int nConfirm = JOptionPane.showConfirmDialog(this.parentFrame,
                        "There is an existing review copy, to overwrite it select Yes, otherwise select No to review the existing copy",
                        "Review Document",
                        JOptionPane.YES_NO_CANCEL_OPTION);
                   switch (nConfirm) {
                       case JOptionPane.YES_OPTION :
                            rvd.deleteReviewCopyFile();
                            //do overwrite
                            break;
                       case JOptionPane.NO_OPTION :
                            // open the review copy

                           break;
                       case JOptionPane.CANCEL_OPTION :
                            // do nothing and return
                           bOpen = false;
                           break;
                }
                if (!bOpen) {
                    return;
                }
            }

            final BungeniOdfDocumentHelper reviewDoc      = rvd.getReviewCopy();
            final BungeniDocAuthor         selectedAuthor = (BungeniDocAuthor) this.listMembers.getSelectedValue();

            // invoke openoffice in a runnable thread
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                        try {
                            UnoOdfFile odfFile = UnoOdfOpenFiles.getFile(reviewDoc);
                        } catch (Exception ex) {
                            if (ex.getClass().getName().equals("com.sun.star.lang.DisposedException")) {
                                log.error("doReview: Bridge disposed exception was caused !!!!");
                                //trying to recreate the bootrstrap handle and init.
                                log.info("doReview: Retrying - bootstrapping OOo again ");
                                TrackChangesInit.bootstrapOOo();
                                log.info("doReview: OOo was bootrstrapped ");
                                UnoOdfFile odfFile = UnoOdfOpenFiles.getFile(reviewDoc);
                                log.info("doReview: File was successfully opened");
                            }
                        }

                    // open review document in openoffice
                }
            });
            ((JTabbedPane) getParent()).setSelectedIndex(1);

            // switch to the review tab
            // get the selected author in the message map
            HashMap<String, Object> messageMap = new HashMap<String, Object>() {
                {
                    put("selectedAuthor", selectedAuthor);
                }
            };

            getContainerInterface().updateCurrentPanel(messageMap);
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    @Override
    public void updatePanel(HashMap<String, Object> infomap) {

        // to be implemented
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
        lblNoOfChanges = new javax.swing.JLabel();

        lblMembers.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblMembers.setText(null);

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
        lblDocInfo.setText(null);

        tblDocChanges.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
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
        jLabel1.setText(null);

        btnReview.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle"); // NOI18N
        btnReview.setText(bundle.getString("panelTrackChangesOverview.btnReview.text")); // NOI18N
        btnReview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReviewActionPerformed(evt);
            }
        });

        cboBills.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        cboBills.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Finance Bill", "Auto Bill" }));

        lblSelectBill.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblSelectBill.setText(null);

        lblNoOfChanges.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblNoOfChanges.setForeground(java.awt.Color.blue);
        lblNoOfChanges.setText(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollDocInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                    .addComponent(scrollMembers, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                    .addComponent(cboBills, 0, 238, Short.MAX_VALUE)
                    .addComponent(lblMembers)
                    .addComponent(lblSelectBill, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDocInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollDocChanges, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                    .addComponent(btnReview)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNoOfChanges, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblSelectBill)
                    .addComponent(lblNoOfChanges))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(scrollDocChanges, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReview, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(cboBills, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblMembers)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollMembers, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblDocInfo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollDocInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnReviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReviewActionPerformed
        try {
            btnReview.setEnabled(false);

            // do the review in the review tab
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
    private javax.swing.JLabel lblNoOfChanges;
    private javax.swing.JLabel lblSelectBill;
    private javax.swing.JList listMembers;
    private javax.swing.JScrollPane scrollDocChanges;
    private javax.swing.JScrollPane scrollDocInfo;
    private javax.swing.JScrollPane scrollMembers;
    private javax.swing.JTable tblDocChanges;
    private javax.swing.JTextArea txtareaDocInfo;
    // End of variables declaration//GEN-END:variables



    private class BillsComboBoxModel extends DefaultComboBoxModel {
        public BillsComboBoxModel(BungeniBill[] bills) {
            super(bills);
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

        public DocumentChangesTableModel(int iIndex) {
            buildModel(iIndex);
        }

        public void updateModel(final int iIndex) {
            getContainerInterface().startProgress();
            SwingWorker modelWorker = new SwingWorker(){
                @Override
                protected Object doInBackground() throws Exception {
                        buildModel(iIndex);
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

        private void buildModel(int iIndex) {
            changeMarks.clear();

            BungeniOdfDocumentHelper        docHelper       = changesInfo.getDocuments().get(iIndex);
            String                          docAuthor       =
                docHelper.getPropertiesHelper().getUserDefinedPropertyValue("BungeniDocAuthor");
            BungeniOdfTrackedChangesHelper  changeHelper    = docHelper.getChangesHelper();
            Element                         changeContainer = changeHelper.getTrackedChangeContainer();
            ArrayList<OdfTextChangedRegion> changes         = changeHelper.getChangedRegionsByCreator(changeContainer,
                                                                  docAuthor);

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
            String sValue = format.format(values);

            lblNoOfChanges.setText(sValue);
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
                return changeMark.get("changeType").toString();

            case 1 :
                return BungeniOdfDateHelper.odfDateToPresentationDate(changeMark.get("dcDate").toString());

            case 2 :
                if (changeMark.containsKey("changeText")) {
                    return changeMark.get("changeText").toString();
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
