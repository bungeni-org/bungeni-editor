/*
 * TabledDocuments.java
 *
 * Created on August 8, 2008, 1:52 PM
 */

package org.bungeni.editor.selectors.debaterecord.tableddocuments;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XIndexAccess;
import com.sun.star.container.XIndexReplace;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.style.NumberingType;
import com.sun.star.text.XParagraphCursor;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextRange;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.AbstractButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.element.Document;
import org.bungeni.editor.selectors.BaseMetadataPanel;
import org.bungeni.extutils.CommonXmlConfigParams;
import org.bungeni.editor.connectorutils.CommonConnectorFunctions;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;

/**
 *
 * @author  undesa
 */
public class TabledDocuments extends BaseMetadataPanel {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TabledDocuments.class.getName());
  protected static final ResourceBundle bundle = ResourceBundle.getBundle("org/bungeni/editor/selectors/debaterecord/tableddocuments/Bundle");

    /** Creates new form TabledDocuments */
    public TabledDocuments() {
        super();
        initComponents();
        initTable();
    }
 

    class TabledDocumentsModel extends DefaultTableModel {
        private boolean cellsEditable = false;

        public TabledDocumentsModel(){
            super();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return cellsEditable;
        }

        public void setModelEditable(boolean bState) {
            this.cellsEditable = bState;
        }
    }

    // !+ (rm, feb 2012) - deprecated method - access to the database is now
    // via the BungeniConnector
    /**
    protected String getTableQuery(){
        return new String("select document_title, document_uri, document_date from tabled_documents");
    }
    **/

    // !+BUNGENI CONNECTOR (rm, 17 jan 2012) - edited this method to ensure that
    // it picks table date from the TABLED_DOCUMENTS via the BungeniConnector
    // as directly trying to acces the table is deprecated
    protected void initTable(){
        // create the columns vector
        String [] columns = {"Id", "Title", "Date", "Source", "URI", "Sitting ID"} ;
        Vector <String> columnNames = new Vector<String>(Arrays.asList(columns));

        // initialise the Bungeni Client
        BungeniConnector client = null;
        try {
            // initialize the data store client
            client = CommonConnectorFunctions.getDSClient();

            // get the list of all the tabled documents
            List<Document> documentsList = client.getDocuments();

            // fill the vector<vector> object with the results of the
            // query for the list of tabled documents
            // var to store thefinal res
            Vector<Vector<String>> resultRows = new Vector<Vector<String>>();

            for (int i = 0; i < documentsList.size(); i++) {
                Document doc = documentsList.get(i);

                Vector <String> currRes = new Vector<String>();

                // add the current document properties
                // to the vector
                currRes.add(doc.getId());
                currRes.add(doc.getTitle());
                currRes.add(doc.getDate());
                currRes.add(doc.getSource());
                currRes.add(doc.getUri());
                currRes.add(doc.getSitting());

                // add the curr vector to result
                resultRows.add(currRes);
            }

            // create a table model 
            TabledDocumentsModel mdl = new TabledDocumentsModel() ;
            mdl.setDataVector(resultRows, columnNames);

            // Add table model to table
            tbl_tabledDocs.setModel(mdl);

            // (rm, feb 2012) - adjust the width of the columns of the table
            // to display the title of the tabled documents more predominantly
            // dimension  the id col
            dimensionColumn(tbl_tabledDocs, 0,30) ;
            dimensionColumn(tbl_tabledDocs, 1, 300) ;
            dimensionColumn(tbl_tabledDocs, 2, 120) ;
            
            ((TabledDocumentsModel)this.tbl_tabledDocs.getModel()).setModelEditable(false);
            enableButtons(false);

        } catch (IOException ex) {
            log.error("Error initializing the BungeniConnectorClient " + ex) ;
        }

        /**
        HashMap<String,String> registryMap = BungeniRegistryFactory.fullConnectionString();
            BungeniClientDB dbInstance = new BungeniClientDB(registryMap);
            dbInstance.Connect();
            QueryResults qr = dbInstance.QueryResults(getTableQuery());
            dbInstance.EndConnect();
            if (qr != null ) {
                if (qr.hasResults()) {
                    Vector<Vector<String>> resultRows = new Vector<Vector<String>>();
                    resultRows = qr.theResults();
                    TabledDocumentsModel mdl = new TabledDocumentsModel() ;
                    mdl.setDataVector(resultRows, qr.getColumnsAsVector());
                    tbl_tabledDocs.setModel(mdl);
                     ((TabledDocumentsModel)this.tbl_tabledDocs.getModel()).setModelEditable(false);
                     enableButtons(false);
                    }
            }
         **/
     }

    /**
     * (rm, feb 2012) - This method dimensions the columns for the JTable
     * @param b
     */
    private void dimensionColumn(JTable table, int columnIndex, int columnWidth)
    {
        TableColumn col = table.getColumnModel().getColumn(columnIndex) ;

        if(null!=col)
        {
            col.setPreferredWidth(columnWidth);
        }
    }

    private void enableButtons(boolean b) {
        Enumeration<AbstractButton> buttons = this.grpEditButtons.getElements();
        while (buttons.hasMoreElements()) {
            AbstractButton abButton = buttons.nextElement();
            abButton.setEnabled(b);
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

        grpEditButtons = new javax.swing.ButtonGroup();
        lbl_tabledDocs = new javax.swing.JLabel();
        scrollTabledDocs = new javax.swing.JScrollPane();
        tbl_tabledDocs = new javax.swing.JTable();
        chkEditTable = new javax.swing.JCheckBox();
        btnClear = new javax.swing.JButton();
        btnDeleteSelected = new javax.swing.JButton();
        btnAddRow = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();

        lbl_tabledDocs.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/selectors/debaterecord/tableddocuments/Bundle"); // NOI18N
        lbl_tabledDocs.setText(bundle.getString("TabledDocuments.lbl_tabledDocs.text")); // NOI18N

        tbl_tabledDocs.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        tbl_tabledDocs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrollTabledDocs.setViewportView(tbl_tabledDocs);

        chkEditTable.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        chkEditTable.setText(bundle.getString("TabledDocuments.chkEditTable.text")); // NOI18N
        chkEditTable.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkEditTableItemStateChanged(evt);
            }
        });

        btnClear.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnClear.setText(bundle.getString("TabledDocuments.btnClear.text")); // NOI18N
        grpEditButtons.add(btnClear);
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnDeleteSelected.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnDeleteSelected.setText(bundle.getString("TabledDocuments.btnDeleteSelected.text")); // NOI18N
        grpEditButtons.add(btnDeleteSelected);
        btnDeleteSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSelectedActionPerformed(evt);
            }
        });

        btnAddRow.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnAddRow.setText(bundle.getString("TabledDocuments.btnAddRow.text")); // NOI18N
        grpEditButtons.add(btnAddRow);
        btnAddRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRowActionPerformed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnReset.setText(bundle.getString("TabledDocuments.btnReset.text")); // NOI18N
        grpEditButtons.add(btnReset);
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scrollTabledDocs, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                        .addGap(20, 20, 20))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl_tabledDocs, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                        .addGap(128, 128, 128)
                        .addComponent(chkEditTable))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnClear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteSelected)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddRow)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_tabledDocs)
                    .addComponent(chkEditTable))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollTabledDocs, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClear)
                    .addComponent(btnDeleteSelected)
                    .addComponent(btnAddRow)
                    .addComponent(btnReset)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void chkEditTableItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkEditTableItemStateChanged
        // TODO add your handling code here:
        switch (evt.getStateChange()) {
            case ItemEvent.SELECTED:
                ((TabledDocumentsModel)this.tbl_tabledDocs.getModel()).setModelEditable(true);
                enableButtons(true);
                break;
            case ItemEvent.DESELECTED:
                ((TabledDocumentsModel)this.tbl_tabledDocs.getModel()).setModelEditable(false);
                enableButtons(false);
                break;
            default:
                return;
        }
    }//GEN-LAST:event_chkEditTableItemStateChanged

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
       TabledDocumentsModel model =  ((TabledDocumentsModel)this.tbl_tabledDocs.getModel());
        for (int i =  model.getRowCount() -1; i >= 0; i--) {
            model.removeRow(i);
        }
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnDeleteSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSelectedActionPerformed
        // TODO add your handling code here:
        int[] arrSelected = this.tbl_tabledDocs.getSelectedRows();
        TabledDocumentsModel model =  ((TabledDocumentsModel)this.tbl_tabledDocs.getModel());
        if (arrSelected.length > 0) {
            //remove the rows in descending order
            Arrays.sort(arrSelected);
            for (int i = arrSelected.length -1; i >= 0;  i--) {
               model.removeRow(arrSelected[i]);
            }
        }

    }//GEN-LAST:event_btnDeleteSelectedActionPerformed

    private void btnAddRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRowActionPerformed
        // TODO add your handling code here:
        TabledDocumentsModel model =  ((TabledDocumentsModel)this.tbl_tabledDocs.getModel());
        final int nColumns = model.getColumnCount();
        String[] newRow = new String[nColumns];
        Arrays.fill(newRow, "");
        model.addRow(newRow);
    }//GEN-LAST:event_btnAddRowActionPerformed


    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        initTable();
        ((TabledDocumentsModel)this.tbl_tabledDocs.getModel()).setModelEditable(true);

    }//GEN-LAST:event_btnResetActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddRow;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDeleteSelected;
    private javax.swing.JButton btnReset;
    protected javax.swing.JCheckBox chkEditTable;
    protected javax.swing.ButtonGroup grpEditButtons;
    private javax.swing.JLabel lbl_tabledDocs;
    protected javax.swing.JScrollPane scrollTabledDocs;
    protected javax.swing.JTable tbl_tabledDocs;
    // End of variables declaration//GEN-END:variables

    public String getPanelName() {
        return "TabledDocuments";
    }

    public Component getPanelComponent() {
        return this;
    }

      public HashMap<String,ArrayList<String>> getTableSelection() {

         int[] selectedRows = tbl_tabledDocs.getSelectedRows();
         ArrayList<String> docTitles = new ArrayList<String>();
         ArrayList<String> docURIs = new ArrayList<String>();
             for (int i=0; i < selectedRows.length; i++) {
                 //AH-18-01-11 -- fixed bug in array referencing
                 String docTitle = (String)tbl_tabledDocs.getModel().getValueAt(selectedRows[i], 0 );
                 String docURI = (String) tbl_tabledDocs.getModel().getValueAt(selectedRows[i], 1);
                 docTitles.add(docTitle);
                 docURIs.add(docURI);
             }
        HashMap<String,ArrayList<String>> tblData = new HashMap<String,ArrayList<String>>();
        tblData.put("tabled_document_titles", docTitles);
        tblData.put("tabled_document_uris", docURIs);
        return tblData;
    
      }
        
        
    @Override
    public boolean preFullEdit() {
        return true;
    }

    @Override
    public boolean processFullEdit() {
        return true;
    }

    @Override
    public boolean postFullEdit() {
        return true;
    }

    @Override
    public boolean preFullInsert() {
        return true;
    }

    @Override
    public boolean processFullInsert() {
        return true;
    }

    @Override
    public boolean postFullInsert() {
        return true;
    }

    @Override
    public boolean preSelectEdit() {
        return true;
    }

    @Override
    public boolean processSelectEdit() {
        return true;
    }

    @Override
    public boolean postSelectEdit() {
        return true;
    }

    private void applyBulletedList() {
        try {
        HashMap<String, ArrayList<String>> arrTableSelection = this.getTableSelection();
        OOComponentHelper ooDocument = getContainerPanel().getOoDocument();
        //oStart = docComponent.currentcontroller.getViewCursor().getStart()
        XTextRange xStartRange = ooDocument.getViewCursor().getStart();
        //oText = docComponent.currentController.getviewcursor().getText()
        XText xCursorText = ooDocument.getViewCursor().getText();
        //
        XTextCursor startCur=xCursorText.createTextCursorByRange(xStartRange);
        ArrayList<String> tblDocTitles = arrTableSelection.get("tabled_document_titles");
        ArrayList<String> tblDocURIs = arrTableSelection.get("tabled_document_uris");
       //For i=LBound(selectItemsArray) to UBound(selectItemsArray)
        for (int i=0; i < tblDocTitles.size(); i++) {
                //AH-18-01-11 Fixed url not being written problem
                XPropertySet xCurProps = ooQueryInterface.XPropertySet(startCur);
                xCursorText.insertString(startCur, tblDocTitles.get(i), true);
                xCurProps.setPropertyValue("HyperLinkURL", CommonXmlConfigParams.ODF_URI_PREFIX + tblDocURIs.get(i));
                xCurProps.setPropertyValue("HyperLinkName", bundle.getString("URI_TABLED_DOCUMENTS") );
                startCur.gotoRange(startCur.getEnd(), false);
                startCur.goRight((short)1, false);
                // if (!(i == tblDocTitles.size() -1 ))
                // xCursorText.insertControlCharacter(startCur, com.sun.star.text.ControlCharacter.PARAGRAPH_BREAK, false);
        }
        //AH-18-01-11 - remove auto bulleting -- the user has to bullet it
        //setNumberingRules(ooDocument, startCur);
        
            } catch (UnknownPropertyException ex) {
                log.error("applyBulletedList : " + ex.getMessage());
            } catch (PropertyVetoException ex) {
                log.error("applyBulletedList : " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                log.error("applyBulletedList : " + ex.getMessage());
            } catch (WrappedTargetException ex) {
                log.error("applyBulletedList : " + ex.getMessage());            
            }
        
    }
    
    public void setNumberingRules(OOComponentHelper ooDocument, XTextCursor xCursor ){
        try {
            Object objNumeringRules = ooDocument.createInstance("com.sun.star.text.NumberingRules");
            XIndexAccess numIndexAccess = ooQueryInterface.XIndexAccess(objNumeringRules);
            XIndexReplace indexReplace = ooQueryInterface.XIndexReplace(numIndexAccess);
            PropertyValue[] pvProps = (PropertyValue[]) numIndexAccess.getByIndex(0);
            for (int j = 0; j < pvProps.length; j++) {
                if (pvProps[j].Name.equals("NumberingType")) {
                    pvProps[j].Value = new Short(NumberingType.CHAR_SPECIAL);
                    indexReplace.replaceByIndex(0, pvProps);
                }
            }
            XParagraphCursor xParaCursor = ooQueryInterface.XParagraphCursor(xCursor);
            int selSize = getTableSelection().get("tabled_document_titles").size();
            for (int i=0 ; i < selSize; i++) {
                if (i == 0 )
                    xParaCursor.gotoPreviousParagraph(false);
                else
                    xParaCursor.gotoPreviousParagraph(true);
            }
            //xParaCursor.gotoPreviousParagraph(false);
            //xParaCursor.gotoRange(xStartRange, true);
            XPropertySet xCurProps = ooQueryInterface.XPropertySet(xCursor);
            xCurProps.setPropertyValue("NumberingRules", numIndexAccess);
        } catch (UnknownPropertyException ex) {
           log.error("getNumberingRules : " + ex.getMessage());
        } catch (PropertyVetoException ex) {
           log.error("getNumberingRules : " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
           log.error("getNumberingRules : " + ex.getMessage());
        } catch (IndexOutOfBoundsException ex) {
           log.error("getNumberingRules : " + ex.getMessage());
        } catch (WrappedTargetException ex) {
           log.error("getNumberingRules : " + ex.getMessage());
        }
  
    }
    
    @Override
    public boolean preSelectInsert() {
        HashMap<String,ArrayList<String>> tblData = new HashMap<String,ArrayList<String>>();
        
        
        return true;
    }

    @Override
    public boolean processSelectInsert() {
        applyBulletedList();
        return true;
    }

    @Override
    public boolean postSelectInsert() {
        return true;
    }

    @Override
    public boolean validateSelectedEdit() {
        return true;
    }

    @Override
    public boolean validateSelectedInsert() {
        //validate the tabled documents
        //all rows in table need to be full.
        boolean bState = true;

        if (this.tbl_tabledDocs.getSelectedRowCount() == 0) {
            addErrorMessage(this.tbl_tabledDocs, bundle.getString("no_row_selected"));
            bState = false;
        }  else {
            //validate selected rows for empty data
            int[] nRows = this.tbl_tabledDocs.getSelectedRows();
            int nCols = this.tbl_tabledDocs.getColumnCount();

            for (int i = 0; i < nRows.length; i++) {
                for (int j = 0; j < nCols; j++) {
                   String sValue = (String) this.tbl_tabledDocs.getValueAt(nRows[i], j);
                   if (sValue.trim().length() == 0) {
                       Object[] values = {Integer.toString(nRows[i]+1), Integer.toString(j+1)};
                       String formattedMsg = MessageFormat.format(bundle.getString("tableValidationError"),
                               values);
                       this.addErrorMessage(this.tbl_tabledDocs, formattedMsg);
                       bState = false;
                   }
                }
            }
        }


  
        return bState;
    }

    @Override
    public boolean validateFullInsert() {
        return true;
    }

    @Override
    public boolean validateFullEdit() {
        return true;
    }

    @Override
    public boolean doCancel() {
        return true;
    }

    @Override
    public boolean doReset() {
        return true;
    }
    @Override
    protected void initFieldsSelectedEdit() {
        return;
    }

    @Override
    protected void initFieldsSelectedInsert() {
      //  BungeniClientDB dbnew = new BungeniClientDB();
        return;
    }

    @Override
    protected void initFieldsInsert() {
        return;
    }

    @Override
    protected void initFieldsEdit() {
        return;
    }
}
