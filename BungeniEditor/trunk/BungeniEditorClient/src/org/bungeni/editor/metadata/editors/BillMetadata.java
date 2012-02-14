/*
 * DebateRecordMetadata.java
 *
 * Created on November 4, 2008, 1:43 PM
 */

package org.bungeni.editor.metadata.editors;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingWorker;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.element.Bill;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.editor.metadata.BaseEditorDocMetadataDialog;
import org.bungeni.editor.metadata.BillMetaModel;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.extutils.CommonConnectorFunctions;
import org.bungeni.extutils.CommonDateFunctions;
import org.bungeni.utils.BungeniFileSavePathFormat;
import org.bungeni.extutils.CommonStringFunctions;

/**
 *
 * @author  undesa
 */
public class BillMetadata extends BaseEditorDocMetadataDialog {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BillMetadata.class.getName());
    private BillMetaModel docMetaModel = new BillMetaModel();
    private Vector<Vector<String>> resultRows = null ; // stores all the bills as
                            // obtained from the H2 database
     
    
 //   OOComponentHelper ooDocument  = null;
 //   JFrame parentFrame = null;
 //    SelectorDialogModes dlgMode = null;
    
    
    public BillMetadata(){
        super();
        initComponents();
    }
    
    @Override
    public void initialize() {
        super.initialize();
        
        this.docMetaModel.setup();
        initControls();        
           if (theMode == SelectorDialogModes.TEXT_EDIT) {
                //retrieve metadata... and set in controls....
                docMetaModel.loadModel(ooDocument);
               String sBillNo = docMetaModel.getItem("BungeniBillNo");
                String sBillName = docMetaModel.getItem("BungeniBillName");
                String sAssentDate = docMetaModel.getItem("BungeniDateOfAssent");
                String sCommencementDate = docMetaModel.getItem("BungeniDateOfCommencement");
                //official date
                if (!CommonStringFunctions.emptyOrNull(sAssentDate)) {
                    Date assentdate = CommonDateFunctions.parseDate(sAssentDate, BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
                    if (assentdate != null)
                        this.dt_dateofassent.setDate(assentdate);
                }
                //official time
                if (!CommonStringFunctions.emptyOrNull(sCommencementDate) ) {
                    Date commencement = CommonDateFunctions.parseDate(sCommencementDate, BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
                    if (commencement != null)
                        this.dt_dateofcommencement.setDate(commencement);
                }
                if (!CommonStringFunctions.emptyOrNull(sBillNo))
                    this.txtBillNumber.setText(sBillNo);
                if (!CommonStringFunctions.emptyOrNull(sBillName))
                    this.billNameCombobox.setSelectedItem(sBillName);
         
        }
    }

    public Component getPanelComponent() {
        return this;
    }
    
    private void initControls(){
    //    String popupDlgBackColor = BungeniEditorProperties.getEditorProperty("popupDialogBackColor");
    //    this.setBackground(Color.decode(popupDlgBackColor));
    }

    /**
     * (rm, feb 2012) - this method obtains the bill names
     * and updates the billNo text field with the relevant #
     * @return
     */
    private ComboBoxModel setBillsNamesModel()
    {
        DefaultComboBoxModel billsNamesModel = null ;
        String [] billsNames = null ; // stores all the bill Names
        
        // initialise the Bungeni Connector Client
         BungeniConnector client = null ;
        try {
            // initialize the data store client
            client = CommonConnectorFunctions.getDSClient();

            // get the bills from the registry H2 db
            List<Bill> billsList = client.getBills() ;
            billsNames = new String[billsList.size()] ;
            
            // loop through extracting the bills
            for (int i = 0 ; i < billsList.size() ; i ++)
            {
                // get the current bill & extract the bill Name
                Bill currBill = billsList.get(i) ;
                billsNames[i] = currBill.getName() ;
            }

            // create the default bills Names model
            billsNamesModel = new DefaultComboBoxModel(billsNames) ;
            
        } catch (IOException ex) {
            log.error(ex) ;
        }

         return billsNamesModel ;
    }

    public boolean applySelectedMetadata(BungeniFileSavePathFormat spf){
    boolean bState = false;
    try {
    String sBillNo = this.txtBillNumber.getText();
    // (rm, feb 2012) - uses a JComboBox rather than a textfield
     // String sBillName = this.txtBillName.getText();
    String sBillName = (String) this.billNameCombobox.getSelectedItem();
    //get the assent date
       SimpleDateFormat dformatter = new SimpleDateFormat (BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
       final String strDateOfAssent = dformatter.format( this.dt_dateofassent.getDate());
       final String strDateOfCommencement = dformatter.format(this.dt_dateofcommencement.getDate());
       // docMetaModel.updateItem("BungeniParliamentID")
        docMetaModel.updateItem("BungeniBillNo", sBillNo);
        docMetaModel.updateItem("BungeniBillName", sBillName);
        docMetaModel.updateItem("BungeniDateOfAssent", strDateOfAssent);
        docMetaModel.updateItem("BungeniDateOfCommencement", strDateOfCommencement);
        docMetaModel.saveModel(ooDocument);
    bState = true;
    } catch (Exception ex) {
        log.error("applySelectedMetadata : " + ex.getMessage());
        bState = false;
    } finally {
        return bState;
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

        txtBillNumber = new javax.swing.JTextField();
        dt_dateofassent = new org.jdesktop.swingx.JXDatePicker();
        dt_dateofcommencement = new org.jdesktop.swingx.JXDatePicker();
        lblBillNo = new javax.swing.JLabel();
        lblDateOfAssent = new javax.swing.JLabel();
        lblDateOfCommencement = new javax.swing.JLabel();
        billNameCombobox = new javax.swing.JComboBox();
        lblBillNo1 = new javax.swing.JLabel();

        txtBillNumber.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        txtBillNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBillNumberActionPerformed(evt);
            }
        });

        dt_dateofassent.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        dt_dateofcommencement.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        lblBillNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        lblBillNo.setText(bundle.getString("BillMetadata.lblBillNo.text")); // NOI18N

        lblDateOfAssent.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblDateOfAssent.setText(bundle.getString("BillMetadata.lblDateOfAssent.text")); // NOI18N

        lblDateOfCommencement.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblDateOfCommencement.setText(bundle.getString("BillMetadata.lblDateOfCommencement.text")); // NOI18N

        billNameCombobox.setModel(setBillsNamesModel());
        billNameCombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                billNameComboboxActionPerformed(evt);
            }
        });

        lblBillNo1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblBillNo1.setText(bundle.getString("BillMetadata.lblBillNo1.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBillNo1)
                    .addComponent(billNameCombobox, 0, 190, Short.MAX_VALUE)
                    .addComponent(lblBillNo)
                    .addComponent(txtBillNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(lblDateOfAssent)
                    .addComponent(dt_dateofassent, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(lblDateOfCommencement)
                    .addComponent(dt_dateofcommencement, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(lblBillNo1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(billNameCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblBillNo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBillNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDateOfAssent)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dt_dateofassent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDateOfCommencement)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dt_dateofcommencement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * (rm, feb 2012) - This method updates the bill No text field once the relevant 
     * bill is selected from the drop down menu
     * @param evt
     */
    private void billNameComboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_billNameComboboxActionPerformed
        JComboBox cb = (JComboBox) evt.getSource() ;

        // get the selected item and extract id for bill
        // from vector
        final String selectedBill = (String) cb.getSelectedItem() ;

        if( null != selectedBill ) {
            // get the bill & set it to the Bill #
            // textfield
           SwingWorker setBillNo = new SwingWorker() {
                @Override
                protected String doInBackground() throws Exception {
                    String billNo = null ;

                    BungeniConnector client = null ;

                    // initialize the data store client
                    client = CommonConnectorFunctions.getDSClient();

                    // get the bills from the registry H2 db
                    List<Bill> billsList = client.getBills() ;

                    // search for the billNo
                    for (Bill bill : billsList)
                    {
                        if (selectedBill.equals(bill.getName())) {
                            billNo = bill.getId().toString() ;
                            return billNo ;
                        }
                    }

                    return billNo ;
                }

                public void done() {
                    try {
                        // set the bill No
                        txtBillNumber.setText((String) get());
                    } catch (InterruptedException ex) {
                        log.error(ex) ;
                    } catch (ExecutionException ex) {
                        log.error(ex) ;
                    }
                }

           };
           
           setBillNo.execute();
        }

    }//GEN-LAST:event_billNameComboboxActionPerformed

    private void txtBillNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBillNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBillNumberActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox billNameCombobox;
    private org.jdesktop.swingx.JXDatePicker dt_dateofassent;
    private org.jdesktop.swingx.JXDatePicker dt_dateofcommencement;
    private javax.swing.JLabel lblBillNo;
    private javax.swing.JLabel lblBillNo1;
    private javax.swing.JLabel lblDateOfAssent;
    private javax.swing.JLabel lblDateOfCommencement;
    private javax.swing.JTextField txtBillNumber;
    // End of variables declaration//GEN-END:variables


    @Override
    public Dimension getFrameSize() {
        int DIM_X = 229 ; int DIM_Y = 222 ;
        return new Dimension(DIM_X, DIM_Y + 10);
    }

    
    @Override
     public ArrayList<String> validateSelectedMetadata(BungeniFileSavePathFormat spf) {
         addFieldsToValidate (new TreeMap<String,Component>(){
            {
                //  (rm, feb 2012) - bill Names are placed in a JCombobox, replacing defined JTextField
                put(java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle").getString("Bill_Name"), billNameCombobox);
                put(java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle").getString("Bill_No"), txtBillNumber);
              //  put(java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle").getString("Date_of_Assent"), dt_dateofassent);
              //  put (java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle").getString("Date_of_Commencement"), dt_dateofcommencement);
            }
            });
        return super.validateSelectedMetadata(spf);
     }
}
