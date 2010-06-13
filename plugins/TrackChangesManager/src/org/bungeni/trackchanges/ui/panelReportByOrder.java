package org.bungeni.trackchanges.ui;

import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import net.java.swingfx.waitwithstyle.PerformanceInfiniteProgressPanel;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.QueryResults;
import org.bungeni.odfdocument.report.IBungeniOdfDocumentReportProcess;
import org.bungeni.odfdocument.report.IBungeniOdfDocumentReportUI;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.reports.process.reportEditableChangesByOrder_Queries;
import org.bungeni.trackchanges.utils.CommonFunctions;

/**
 *
 * @author Ashok Hariharan
 */
public class panelReportByOrder extends panelChangesBase implements IBungeniOdfDocumentReportUI {
    private static org.apache.log4j.Logger log            =
        org.apache.log4j.Logger.getLogger(panelReportByOrder.class.getName());
    public static JFrame                   thisFrame = null;
    private IBungeniOdfDocumentReportProcess reportProcess = null;
    private BungeniOdfDocumentHelper[] reportInputDocuments;
    
    /** Creates new form panelReportByOrder */
    public panelReportByOrder() {
        super();
        initComponents();
    }

      public panelReportByOrder(JFrame parentFrm, String pName) {
           super(parentFrm, pName);
           initComponents();
     }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnMoveUp = new javax.swing.JButton();
        btnMoveDown = new javax.swing.JButton();
        btnFinish = new javax.swing.JButton();
        scrollReport = new javax.swing.JScrollPane();
        treeReportByOrder = new javax.swing.JTree();

        btnMoveUp.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnMoveUp.setText("Move UP");

        btnMoveDown.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnMoveDown.setText("Move DOWN");

        btnFinish.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnFinish.setText("Finish");

        treeReportByOrder.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Loading...");
        treeReportByOrder.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        scrollReport.setViewportView(treeReportByOrder);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnMoveUp, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMoveDown)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFinish, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(scrollReport, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(scrollReport, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMoveUp)
                    .addComponent(btnMoveDown)
                    .addComponent(btnFinish)))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFinish;
    private javax.swing.JButton btnMoveDown;
    private javax.swing.JButton btnMoveUp;
    private javax.swing.JScrollPane scrollReport;
    private javax.swing.JTree treeReportByOrder;
    // End of variables declaration//GEN-END:variables

    private void initialize() {
        PANEL_FILTER_REVIEW_STAGE = "ClerkConsolidationReview";
        PANEL_REVIEW_STAGE        = "";
       // loadFilesFromFolder();
        startProgress();
        loadTreeWorker treeLoader = new loadTreeWorker();
        treeLoader.execute();
    }


    class loadTreeWorker extends SwingWorker {

        @Override
        protected Object doInBackground() throws Exception {
                getProcessHook().prepareProcess(reportInputDocuments);
                return Boolean.TRUE;
        }

        @Override
        protected void done(){
                try {
                    Boolean bProcessDocs = (Boolean) get();
                    if (bProcessDocs == true ) 
                        loadTree();
                    else
                        log.error("loadTreeWorker : prepare process returned FALSE");
                    stopProgress();
                } catch (InterruptedException ex) {
                    log.error("Error while loading tree ", ex);
                } catch (ExecutionException ex) {
                    log.error("Error while loading tree ", ex);
                }
        }

        private void loadTree(){
            //the tree is loaded here
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new GroupedChange("root", "bill"));
            String groupedQuery = reportEditableChangesByOrder_Queries.GET_CHANGE_GROUPS_BILL_BY_MANUAL_ORDER(CommonFunctions.getCurrentBillID());
            BungeniClientDB db = BungeniClientDB.defaultConnect();
            QueryResults qr = db.ConnectAndQuery(groupedQuery);
            String[] colgroupBy = qr.getSingleColumnResult("GROUP_BY");
            colgroupBy = CommonFunctions.removeDuplicatesStringArray(colgroupBy);
            for (String groupby : colgroupBy) {
                    String []arrgrp = groupby.split("\\.");
                    String sectionType = arrgrp[0];
                    String sectionName = arrgrp[1];
                    GroupedChange gparentNodeChange = new GroupedChange(sectionType, sectionName);
                    DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(gparentNodeChange);
                    rootNode.add(aNode);
                        //now add changes for each of these sections
                    String qryChangsInDocOrder = reportEditableChangesByOrder_Queries.GET_CHANGES_BY_GROUP_IN_DOC_ORDER (CommonFunctions.getCurrentBillID(), groupby);
                    //SELECT doc_name, change_id, change_type, path_start, path_end, order_in_doc
                    QueryResults qr2 = db.ConnectAndQuery(qryChangsInDocOrder);
                    if (qr2.hasResults()) {
                        Iterator<Vector<String>> results = qr2.theResultsIterator();
                        while (results.hasNext()) {
                            Vector<String> row = results.next();
                            DocumentChange docChange = new DocumentChange(
                            qr2.getField(row, "DOC_NAME"),
                            qr2.getField(row, "CHANGE_ID"),
                            qr2.getField(row, "CHANGE_TYPE"),
                            qr2.getField(row, "PATH_START"),
                            qr2.getField(row, "PATH_END"),
                            qr2.getField(row, "ORDER_IN_DOC"),
                            qr2.getField(row, "OWNER") ,
                            qr2.getField(row, "CHANGE_DATE") ,
                            qr2.getField(row, "CHANGE_TEXT") ,
                            Integer.parseInt(qr2.getField(row, "ORDER_WEIGHT").toString())
                                    );
                            GroupedChange groupedChange = new GroupedChange (sectionType, sectionName, docChange);
                            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(groupedChange);
                            aNode.add(childNode);
                            if (!gparentNodeChange.getSectionDeleteChange())
                                if (groupedChange.getObjType() == GroupedChange.OBJECT_TYPE.CHANGE) {
                                    if (groupedChange.getDocumentChange().getOrderWeight() == 0 ) {
                                        gparentNodeChange.setSectionDeleteChange(true);
                                        aNode.setUserObject(gparentNodeChange);
                                    }
                                }
                        }
                    }

                        
            }
          DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
          treeReportByOrder.setCellRenderer(new reportCellRenderer());
          treeReportByOrder.setModel(treeModel);
        }

    }

     public  void createAndShowGUI(JFrame parentFrame) {
                if (panelReportByOrder.thisFrame == null) {
                    panelReportByOrder.thisFrame = new JFrame("Report");
                    panelReportByOrder.thisFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    this.setParentFrame(parentFrame);
                    this.setPanelName("ReportByOrder");
                    panelReportByOrder.thisFrame.getContentPane().add(this);
                    panelReportByOrder.thisFrame.pack();
                    panelReportByOrder.thisFrame.setAlwaysOnTop(true);
                    panelReportByOrder.thisFrame.setVisible(true);
                } else {
                    if (thisFrame.isVisible()) {
                        thisFrame.requestFocus();
                    } else {
                        thisFrame.setVisible(true);
                        thisFrame.requestFocus();
                    }
                }
    }

    public boolean showUI(JFrame parentf) {
       createAndShowGUI(parentf);
       return true;
    }

    public boolean hasUICompleted() {
       return (!thisFrame.isVisible());
    }

    public boolean setProcessHook(IBungeniOdfDocumentReportProcess processHook) {
        this.reportProcess = processHook;
        return true;
    }

    public boolean setInputDocuments(BungeniOdfDocumentHelper[] dhelpers) {
        this.reportInputDocuments = dhelpers;
        return true;
    }

    public IBungeniOdfDocumentReportProcess getProcessHook(){
        return this.reportProcess;
    }

    private void initialize_listBoxes() {
       
    }

        
    public boolean initUI() {
            initialize();
            return true;
    }

    Component originalPane;

      /**
     * This covers the panel with a glass pane and runs the infinit progress bar.
     * Must be called before invoking the worker thread
     * @return
     */
    public boolean startProgress(){
        boolean bState = false;
        try {
            originalPane = thisFrame.getGlassPane();
            PerformanceInfiniteProgressPanel glassPane;
            thisFrame.setGlassPane(glassPane = new PerformanceInfiniteProgressPanel());
            glassPane.setVisible(true);
            bState = true;
        } catch (Exception ex) {
            log.error("startProgress : " + ex.getMessage());
        }
        return bState;
    }


    /**
     * This must be called only after a startProgress() api has been called.
     * stopProgress() is usually called when the worker threard completes , usually after
     * the get() api in SwingWorker.done().
     * @return
     */
    public boolean stopProgress() {
        boolean bState = false;
        try {
          final PerformanceInfiniteProgressPanel pPanel =
                  (PerformanceInfiniteProgressPanel) thisFrame.getGlassPane();
                   pPanel.setVisible(false);
                   thisFrame.setGlassPane(originalPane);
                   bState = true;
        } catch (Exception ex) {
            log.error("endProgress : " + ex.getMessage());
        }
        return bState;
    }


    class reportCellRenderer  extends DefaultTreeCellRenderer{

        Color deletedColor = Color.ORANGE;
        Color insertedColor = Color.BLUE;
        Color sectionColor = Color.MAGENTA;
        Color rootColor = Color.BLACK;
        Color zeroWeight = Color.RED;

        @Override
        public Component getTreeCellRendererComponent(JTree jtree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component c = super.getTreeCellRendererComponent(jtree, value, selected, expanded, leaf, row, hasFocus);
                if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
                       DefaultMutableTreeNode dmt = ((DefaultMutableTreeNode) value);
                       Object userObject = dmt.getUserObject();
                       if (userObject instanceof GroupedChange) {
                           GroupedChange gc = (GroupedChange) userObject;
                           if (gc.getObjType() == GroupedChange.OBJECT_TYPE.CHANGE) {
                            /*    if (gc.getDocumentChange().getOrderWeight() == 0 ) {
                                    c.setForeground(zeroWeight);
                                    DefaultMutableTreeNode dmtParent = ((DefaultMutableTreeNode)dmt.getParent());
                                    GroupedChange gcParent = (GroupedChange) dmtParent.getUserObject();
                                    gcParent.setSectionDeleteChange(true);
                                    dmtParent.setUserObject(gcParent);
                                } else { */
                                    String cType = gc.getDocumentChange().getChangeType();
                                    if (cType.equals("deletion")) {
                                         c.setForeground(deletedColor);
                                    } else {
                                         c.setForeground(insertedColor);
                                    }
                                /*} */
                         } else
                           if (gc.getObjType() == GroupedChange.OBJECT_TYPE.ROOT) {
                                c.setForeground(rootColor);
                           } else
                           if (gc.getObjType() == GroupedChange.OBJECT_TYPE.SECTION) {
                              if (gc.getSectionDeleteChange()) {
                                    c.setForeground(zeroWeight);
                              } else {
                                   c.setForeground(sectionColor);
                              }
                           }
                       }
                }
                return c;
        }

    }
}
