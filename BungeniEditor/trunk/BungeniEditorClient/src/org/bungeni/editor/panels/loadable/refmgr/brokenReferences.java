/*
 * browseReferences.java
 *
 * Created on July 14, 2008, 3:43 PM
 */

package org.bungeni.editor.panels.loadable.refmgr;

import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.container.XNameAccess;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextField;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.uno.AnyConverter;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import org.bungeni.editor.numbering.ooo.OOoNumberingHelper;
import org.bungeni.editor.panels.impl.BaseLaunchablePanel;
import org.bungeni.ooo.ooQueryInterface;

/**
 *
 * @author  undesa
 */
public class brokenReferences extends BaseLaunchablePanel {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(brokenReferences.class.getName());
    private final static String __TITLE__ = "Broken References";      
    
    
    /** Creates new form browseReferences */
    public brokenReferences() {
       // initComponents();
    }


    private void init(){
        initTableModel();
    }
    
    private void initTableModel(){
        //lazy load of tree....
        BrokenReferencesTableModelAgent rtmAgent = new BrokenReferencesTableModelAgent(this.tblBrokenReferences);
        rtmAgent.execute();
        
        tblBrokenReferences.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    //doublic clicked
                    int nRow = tblBrokenReferences.getSelectedRow();
                    if (nRow != -1) {
                        BrokenReferencesTableModel model = (BrokenReferencesTableModel)tblBrokenReferences.getModel();
                        BrokenReferenceContainer ref = model.getRowData(nRow);
                        String refName = ref.Name;
                        //move the cursor lazily
                        MoveCursorToRefRangeAgent moveAgent = new MoveCursorToRefRangeAgent(ref.ReferenceField.getAnchor());
                        moveAgent.execute();
                    }
                }
            }
        });
    }
    
    
    class MoveCursorToRefRangeAgent extends SwingWorker<Boolean, Void>{
       XTextRange thisRange ;
        MoveCursorToRefRangeAgent (XTextRange refrange) {
            thisRange = refrange;
        }
        @Override
        protected Boolean doInBackground() throws Exception{
              if (thisRange != null){
                    ooDocument.getViewCursor().gotoRange(thisRange, false);
           }
           return true;
        }
        
    }
    
    class FilterSettings {
        String Name;
        String DisplayText;
        
        FilterSettings(String n, String d) {
            Name = n;
            DisplayText = d;
        }
        
        @Override
        public String toString(){
            return DisplayText;
        }
    }
    
   /* 
    private void applyInsertCrossReference() {
        final int nSelectedRow = this.tblBrokenReferences.getSelectedRow();
        if (nSelectedRow !=  -1) { //nothing was selected
            int nRow = tblBrokenReferences.getSelectedRow();
            final BrokenReferencesTableModel model = (BrokenReferencesTableModel) tblBrokenReferences.getModel();
            final DocumentInternalReference ref = model.getRowData(nRow);
            insertCrossRef (new ArrayList<String>() {
                {  add(ref.getActualReferenceName()) ;}
            });
        }
    }*/
    
    private boolean insertCrossRef(ArrayList<String> referenceChain) {
          final int lastIndex = referenceChain.size() - 1;  
          boolean bState = false;
          XTextViewCursor viewCursor = ooDocument.getViewCursor();
          XTextDocument xDoc = ooDocument.getTextDocument();

          try {
              for (int i = lastIndex ; i >= 0 ; i--) {
                  Object oRefField=ooDocument.createInstance("com.sun.star.text.TextField.GetReference");
                  XPropertySet refFieldProps = ooQueryInterface.XPropertySet(oRefField);
                    refFieldProps.setPropertyValue("ReferenceFieldSource", com.sun.star.text.ReferenceFieldSource.REFERENCE_MARK);
                    refFieldProps.setPropertyValue("SourceName", referenceChain.get(i));
                    refFieldProps.setPropertyValue("ReferenceFieldPart", com.sun.star.text.ReferenceFieldPart.TEXT);
                    XTextContent fieldContent = ooQueryInterface.XTextContent(oRefField);  
                    xDoc.getText().insertTextContent(viewCursor, fieldContent, true);
                    if (i > 0 ) { //no comma for 
                        xDoc.getText().insertString(viewCursor, ",", false);
                    }
              }

              //XRefreshable xRefresh = ooQueryInterface.XRefreshable(xDoc);
              //xRefresh.refresh();
              ooDocument.textFieldsRefresh();
              bState = true;
            } catch (WrappedTargetException ex) {
                log.error("insertCrossRef ("+ex.getClass().getName() +") " + ex.getMessage());
            } catch (UnknownPropertyException ex) {
                log.error("insertCrossRef ("+ex.getClass().getName() +") " + ex.getMessage());
            } catch (com.sun.star.lang.IllegalArgumentException ex) {
                log.error("insertCrossRef ("+ex.getClass().getName() +") " + ex.getMessage());
            } finally {
                return bState;
            }
    }

    /**
     * SwingWorker agent to do a lazy load of the references tree...
     * The document references are gathered using threaded agent
     * and the tree is loaded once all the references have been gathered.
     */
    class BrokenReferencesTableModelAgent extends SwingWorker<BrokenReferencesTableModel, Void>{
        JTable updateThisTable = null;
        BrokenReferencesTableModelAgent(JTable inputTable){
           // tableModel = model;
            updateThisTable = inputTable;
        }
        @Override
        protected BrokenReferencesTableModel doInBackground() throws Exception {
            BrokenReferencesTableModel rtm = buildBrokenReferencesTableModel();
            return rtm;
        }
        
        @Override
        protected void done(){
            try {
                BrokenReferencesTableModel rtm = get();
                if (rtm != null) {
                    updateThisTable.setModel(rtm);
                }
            } catch (InterruptedException ex) {
                log.error("ReferencesTableModelAgent : " + ex.getMessage());
            } catch (ExecutionException ex) {
                log.error("ReferencesTableModelAgent : " + ex.getMessage());
            }
            
        }
        
    }
    
     private ArrayList<BrokenReferenceContainer> findBrokenReferences(){
            ArrayList<BrokenReferenceContainer> brokenRefs = new ArrayList<BrokenReferenceContainer>();
            try {
              XTextDocument oDoc = ooDocument.getTextDocument();
              //get reference mark objects
              XNameAccess refMarks = ooDocument.getReferenceMarks();
              //get text fields
              XEnumerationAccess refFields = ooDocument.getTextFields(); 
              XEnumeration fieldEnum = refFields.createEnumeration();
              while (fieldEnum.hasMoreElements()) {
                  Object objField = fieldEnum.nextElement();
                  XServiceInfo servInfo = ooDocument.getServiceInfo(objField);
                  if (servInfo.supportsService("com.sun.star.text.TextField.GetReference")) {
                      XTextField foundField = ooQueryInterface.XTextField(objField);
                      XPropertySet propSet = ooDocument.getObjectPropertySet(foundField);
                      short refFieldSource = AnyConverter.toShort(propSet.getPropertyValue("ReferenceFieldSource"));
                            String refSourceName;
                                refSourceName = AnyConverter.toString(propSet.getPropertyValue("SourceName"));
                      switch (refFieldSource) {
                          case com.sun.star.text.ReferenceFieldSource.REFERENCE_MARK :
                               if (!refMarks.hasByName(refSourceName)) {
                                    //this is a dead reference
                                   brokenRefs.add(new BrokenReferenceContainer(foundField));
                               }
                              break;
                          case com.sun.star.text.ReferenceFieldSource.BOOKMARK :
                              break;
                          case com.sun.star.text.ReferenceFieldSource.SEQUENCE_FIELD :
                              break;
                          case com.sun.star.text.ReferenceFieldSource.FOOTNOTE :
                              break;
                          case com.sun.star.text.ReferenceFieldSource.ENDNOTE :
                              break;
                      }
                  }
          }
      } catch (WrappedTargetException ex) {
            log.error(ex.getClass().getName() + " - " + ex.getMessage());
      } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error(ex.getClass().getName() + " - " + ex.getMessage());
      } catch (UnknownPropertyException ex) {
            log.error(ex.getClass().getName() + " - " + ex.getMessage());
      } catch (com.sun.star.container.NoSuchElementException ex) {
            log.error(ex.getClass().getName() + " - " + ex.getMessage());
      } finally {
         return brokenRefs;
      }
  }
        
     private BrokenReferencesTableModel buildBrokenReferencesTableModel(){
        //get an arraylist of broken references
         ArrayList<BrokenReferenceContainer> brokenRefs = findBrokenReferences();
         BrokenReferencesTableModel rtm = new BrokenReferencesTableModel(brokenRefs);
         return rtm;
    }
     
     class BrokenReferenceContainer {
         String Name;
         String Value;
         XTextField ReferenceField;
         BrokenReferenceContainer(XTextField refObj){
             Name = refObj.getPresentation(true).trim();
             ReferenceField = refObj;
             String metaName = OOoNumberingHelper.INTERNAL_REF_PREFIX+Name;
             String metaValue = "";
            try {
                metaValue = ooDocument.getPropertyValue(metaName);
            } catch (UnknownPropertyException ex) {
                log.error("BrokenReferenceContainer : UnknownPropertyException : " + ex.getMessage());
            }
             Value = metaValue;
         }
         
     }
     
     
     class BrokenReferencesTableModel extends AbstractTableModel {
         
         ArrayList<BrokenReferenceContainer> documentReferences = new ArrayList<BrokenReferenceContainer>();
         ArrayList<BrokenReferenceContainer> filteredDocumentReferences = new ArrayList<BrokenReferenceContainer>();
         
         public BrokenReferencesTableModel (ArrayList<BrokenReferenceContainer> dref) {
            super();
            documentReferences = dref;
            //make a memcopy of the dref variable since refArr will be changing
           filteredDocumentReferences = (ArrayList<BrokenReferenceContainer>) dref.clone();
         }
        
         public void resetModel() {
            synchronized(filteredDocumentReferences) {
                filteredDocumentReferences = (ArrayList<BrokenReferenceContainer>) documentReferences.clone();
            }
            fireTableDataChanged();
         }
         
         public void filter(String filterRefTo) {
             filterRefTo = filterRefTo.toLowerCase();
             synchronized(filteredDocumentReferences) {
                 filteredDocumentReferences.clear();
                 for (BrokenReferenceContainer brokeRef : documentReferences){
                     String matchWithThis = brokeRef.Value.toLowerCase();
                     if (matchWithThis.contains(filterRefTo)) {
                         filteredDocumentReferences.add(brokeRef);
                     }
                 }
             }
             fireTableDataChanged();
             /*
             filterRefTo = filterRefTo.toLowerCase();
             log.debug("filterByType : filter for : " + filterRefTo);
             FilterSettings filterBy = (FilterSettings) cboFilterSettings.getSelectedItem();
             synchronized(filteredDocumentReferences) {
                 filteredDocumentReferences.clear();
                 for (XTextField dref : documentReferences) {
                     String matchWithThis = "";
                      if (filterBy.Name.equals("by-container")) {
                         matchWithThis = dref.ParentType.toLowerCase();
                      } else if (filterBy.Name.equals("by-type")) {
                         matchWithThis = dref.ReferenceType.toLowerCase();  
                      } else if (filterBy.Name.equals("by-reftext")) {
                         matchWithThis = dref.ReferenceText.toLowerCase();
                      }
                    log.debug("filterByType : filter by : " + matchWithThis);
                    if (matchWithThis.contains(filterRefTo)) {
                        //matching table model
                        filteredDocumentReferences.add(dref);
                    }
                }
             }
             fireTableDataChanged();
              */ 
         }
         
        private String[] columns = {"Ref Source Name",  "Ref Text"};
        private Class[] column_class = {String.class, String.class };
        
        @Override
        public String getColumnName(int col) {
            return columns[col];
        }
        
        @Override
        public Class getColumnClass(int col) {
            return column_class[col];
        }
        
        public int getRowCount() {
            return filteredDocumentReferences.size();
        }

        public int getColumnCount() {
            return columns.length;
        }

        public Object getValueAt(int row, int col) {
           BrokenReferenceContainer rfObj = filteredDocumentReferences.get(row);
           //DocumentInternalReference rfObj = documentReferences.get(keys[row]);
           switch (col) {
               case 0: 
                   return rfObj.Name;
               case 1:
                   return rfObj.Value;
               /*
               case 2:
                   return rfObj.ReferenceType;
               case 3:
                   return rfObj.ReferenceText;*/
               default:
                   return rfObj.Name;
           }
        }
         
        public BrokenReferenceContainer getRowData (int row) {
            return this.filteredDocumentReferences.get(row);
        } 
     }

     private void filterTableModel(){
         String fieldFilter = this.txtFilterReferences.getText();
         BrokenReferencesTableModel model = (BrokenReferencesTableModel) this.tblBrokenReferences.getModel();
         model.filter(fieldFilter);
     }
     
     
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnFixBroken = new javax.swing.JButton();
        btnViewAll = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBrokenReferences = new javax.swing.JTable();
        txtFilterReferences = new javax.swing.JTextField();
        lblFilterReferences = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();

        btnFixBroken.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        btnFixBroken.setText("Fix Broken...");
        btnFixBroken.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFixBrokenActionPerformed(evt);
            }
        });

        btnViewAll.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        btnViewAll.setText("View All References");
        btnViewAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewAllActionPerformed(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        tblBrokenReferences.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        tblBrokenReferences.setModel(new javax.swing.table.DefaultTableModel(
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
        tblBrokenReferences.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblBrokenReferences);

        txtFilterReferences.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        txtFilterReferences.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFilterReferencesKeyPressed(evt);
            }
        });

        lblFilterReferences.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblFilterReferences.setText("Filter Broken References");

        btnRefresh.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
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
                        .addComponent(btnFixBroken, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnViewAll, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblFilterReferences, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                        .addGap(136, 136, 136))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtFilterReferences, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(lblFilterReferences)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefresh)
                    .addComponent(txtFilterReferences, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClose)
                    .addComponent(btnFixBroken)
                    .addComponent(btnViewAll))
                .addContainerGap(13, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void btnFixBrokenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFixBrokenActionPerformed
// TODO add your handling code here:
   //  applyInsertCrossReference();
}//GEN-LAST:event_btnFixBrokenActionPerformed

private void btnViewAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewAllActionPerformed
// TODO add your handling code here:
   // parentFrame.dispose();
}//GEN-LAST:event_btnViewAllActionPerformed

private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
// TODO add your handling code here:
    parentFrame.dispose();
}//GEN-LAST:event_btnCloseActionPerformed

private void txtFilterReferencesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFilterReferencesKeyPressed
// TODO add your handling code here:
    if (evt.getKeyCode() == KeyEvent.VK_ENTER){
       filterTableModel();
    }
    
}//GEN-LAST:event_txtFilterReferencesKeyPressed

private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
// TODO add your handling code here:
    BrokenReferencesTableModel model = (BrokenReferencesTableModel) this.tblBrokenReferences.getModel();
    model.resetModel();
}//GEN-LAST:event_btnRefreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnFixBroken;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnViewAll;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFilterReferences;
    private javax.swing.JTable tblBrokenReferences;
    private javax.swing.JTextField txtFilterReferences;
    // End of variables declaration//GEN-END:variables

    @Override
    public Component getObjectHandle() {
        return this;
    }

    @Override
    public void initUI() {
        initComponents();
        init();
    }

    @Override
    public String getPanelTitle() {
        return __TITLE__;
    }

}
