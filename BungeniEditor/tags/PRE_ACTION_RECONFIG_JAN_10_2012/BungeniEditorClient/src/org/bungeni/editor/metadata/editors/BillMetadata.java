/*
 * DebateRecordMetadata.java
 *
 * Created on November 4, 2008, 1:43 PM
 */

package org.bungeni.editor.metadata.editors;

import java.awt.Component;
import java.awt.Dimension;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.editor.metadata.BaseEditorDocMetadataDialog;
import org.bungeni.editor.metadata.BillMetaModel;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.extutils.CommonDateFunctions;
import org.bungeni.extutils.CommonFormFunctions;
import org.bungeni.utils.BungeniFileSavePathFormat;
import org.bungeni.extutils.CommonStringFunctions;

/**
 *
 * @author  undesa
 */
public class BillMetadata extends BaseEditorDocMetadataDialog {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BillMetadata.class.getName());
    BillMetaModel docMetaModel = new BillMetaModel();
     
    
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
                    this.txtBillName.setText(sBillName);
         
        }
    }

    public Component getPanelComponent() {
        return this;
    }
    
    private void initControls(){
    //    String popupDlgBackColor = BungeniEditorProperties.getEditorProperty("popupDialogBackColor");
    //    this.setBackground(Color.decode(popupDlgBackColor));
    }
    

public boolean applySelectedMetadata(BungeniFileSavePathFormat spf){
    boolean bState = false;
    try {
    String sBillNo = this.txtBillNumber.getText();
    String sBillName = this.txtBillName.getText();
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
        txtBillName = new javax.swing.JTextField();
        lblBillName = new javax.swing.JLabel();
        dt_dateofassent = new org.jdesktop.swingx.JXDatePicker();
        dt_dateofcommencement = new org.jdesktop.swingx.JXDatePicker();
        lblBillNo = new javax.swing.JLabel();
        lblDateOfAssent = new javax.swing.JLabel();
        lblDateOfCommencement = new javax.swing.JLabel();

        txtBillNumber.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        txtBillName.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        lblBillName.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        lblBillName.setText(bundle.getString("BillMetadata.lblBillName.text")); // NOI18N

        dt_dateofassent.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        dt_dateofcommencement.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        lblBillNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblBillNo.setText(bundle.getString("BillMetadata.lblBillNo.text")); // NOI18N

        lblDateOfAssent.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblDateOfAssent.setText(bundle.getString("BillMetadata.lblDateOfAssent.text")); // NOI18N

        lblDateOfCommencement.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblDateOfCommencement.setText(bundle.getString("BillMetadata.lblDateOfCommencement.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBillNo)
                    .addComponent(lblBillName)
                    .addComponent(lblDateOfAssent)
                    .addComponent(dt_dateofcommencement, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(dt_dateofassent, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(txtBillName, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(txtBillNumber, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(lblDateOfCommencement))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBillNo)
                .addGap(4, 4, 4)
                .addComponent(txtBillNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblBillName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBillName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDateOfAssent)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dt_dateofassent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(lblDateOfCommencement)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dt_dateofcommencement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXDatePicker dt_dateofassent;
    private org.jdesktop.swingx.JXDatePicker dt_dateofcommencement;
    private javax.swing.JLabel lblBillName;
    private javax.swing.JLabel lblBillNo;
    private javax.swing.JLabel lblDateOfAssent;
    private javax.swing.JLabel lblDateOfCommencement;
    private javax.swing.JTextField txtBillName;
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
                put(java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle").getString("Bill_Name"), txtBillName);
                put(java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle").getString("Bill_No"), txtBillNumber);
              //  put(java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle").getString("Date_of_Assent"), dt_dateofassent);
              //  put (java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle").getString("Date_of_Commencement"), dt_dateofcommencement);
            }
            });
        return super.validateSelectedMetadata(spf);
     }
   


   


}
