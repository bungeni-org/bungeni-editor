/*
 * DebateRecordMetadata.java
 *
 * Created on November 4, 2008, 1:43 PM
 */

package org.bungeni.editor.metadata.editors;
import org.jdom.Element;
import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingWorker;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.element.*;
import org.bungeni.db.registryQueryDialog2;
import org.bungeni.editor.config.DatasourceReader;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.editor.metadata.BaseEditorDocMetadataDialog;
import org.bungeni.editor.metadata.JudgementMetadataModel;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.extutils.*;
import org.bungeni.utils.BungeniFileSavePathFormat;

/**
 *
 * @author  undesa
 */
public class JudgemenMetadataExtra extends BaseEditorDocMetadataDialog {

  
  
    
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JudgemenMetadataExtra.class.getName());
    JudgementMetadataModel docMetaModel = new JudgementMetadataModel();
    registryQueryDialog2 rqs = null;
    
     // private Vector<Vector<String>> resultRows = null ; // stores all the bills as
                            // obtained from the H2 database
     
    
 //   OOComponentHelper ooDocument  = null;
 //   JFrame parentFrame = null;
 //    SelectorDialogModes dlgMode = null;
    
    
    public JudgemenMetadataExtra(){
        super();
        initComponents();
        //if(Locale.getDefault().getLanguage().equals("ar") && Locale.getDefault().getCountry().equals("PS") )
        //   CommonEditorFunctions.compOrientation(this);
    }
    
    @Override
    public void initialize() {
        
        super.initialize();
        loadActInfo();
        this.docMetaModel.setup();
        //initControls();
         if (theMode == SelectorDialogModes.TEXT_INSERTION) {
        } else if (theMode == SelectorDialogModes.TEXT_EDIT) {
            docMetaModel.loadModel(ooDocument);
        }
        try {
            String sCaseNo = docMetaModel.getItem("BungeniCaseNo");
            String sDomain = docMetaModel.getItem("BungeniDomain");
            String sCourtType = docMetaModel.getItem("BungeniCourtType");
            String sIssuedOn = docMetaModel.getItem("BungeniIssuedOn");
            String sRegionName = docMetaModel.getItem("BungeniRegionName");
            String sLitigationType = docMetaModel.getItem("BungeniLitigationType");
           // String sLitigationType = docMetaModel.getItem("BungeniLitigationType");
          
           
            if (!CommonStringFunctions.emptyOrNull(sCaseNo)) {
                this.txtCaseNumber.setText(sCaseNo);
            }
            
            if (!CommonStringFunctions.emptyOrNull(sRegionName)) {
                this.regionNameCombobox.addItem(sRegionName);
            }
            
             if (!CommonStringFunctions.emptyOrNull(sLitigationType)) {
                this.litigationTybeCombobox.addItem(sLitigationType);
            }
            
            
            
            
            if (!CommonStringFunctions.emptyOrNull(sDomain)) {
                 this.domain.addItem(sDomain);
            }
            
             if (!CommonStringFunctions.emptyOrNull(sCourtType)) {
                 this.courtNameCombobox.addItem(sCourtType);
            }
             
            
             
              if (!CommonStringFunctions.emptyOrNull(sIssuedOn)) {
                SimpleDateFormat formatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
                this.dt_issuedOn.setDate(formatter.parse(sIssuedOn));
             }
            
            
            
           
           /* String sDateOfAssent = docMetaModel.getItem("BungeniDateOfAssent");
            if (!CommonStringFunctions.emptyOrNull(sDateOfAssent)) {
                SimpleDateFormat formatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
                this.dt_dateofassent.setDate(formatter.parse(sDateOfAssent));
             }*/
           
            
            
           

        } catch (Exception ex) {
            log.error("initalize()  =  " + ex.getMessage());
        }
           
    }
    
    private void loadActInfo() {
        BungeniConnector client = null ;
        try {
            client = CommonConnectorFunctions.getDSClient();
            List<MetadataInfo> metadata = client.getMetadataInfo();
            if (metadata != null) {
                for (int i = 0; i < metadata.size(); i++) {
                    if (metadata.get(i).getName().equalsIgnoreCase("BungeniCaseNo")) {
                        docMetaModel.setBungeniCaseNo(metadata.get(i).getValue());
                    }
                    
                    if (metadata.get(i).getName().equalsIgnoreCase("BungeniRegionName")) {
                        docMetaModel.setBungeniRegionName(metadata.get(i).getValue());
                    }
                    
                    if (metadata.get(i).getName().equalsIgnoreCase("BungeniLitigationType")) {
                        docMetaModel.setBungeniLitigationType(metadata.get(i).getValue());
                    }
                    
                    
                    
                    
                    if (metadata.get(i).getName().equalsIgnoreCase("BungeniDomain")) {
                        docMetaModel.setBungeniDomain(metadata.get(i).getValue());
                    }
                    
                    if (metadata.get(i).getName().equalsIgnoreCase("BungeniCourtType")) {
                        docMetaModel.setBungeniCourtType(metadata.get(i).getValue());
                    }
                    
                    if (metadata.get(i).getName().equalsIgnoreCase("BungeniIssuedOn")) {
                        docMetaModel.setBungeniIssuedOn(metadata.get(i).getValue());
                    } 
                    
                   
                    
                   /*  if (metadata.get(i).getName().equalsIgnoreCase("BungeniLitigationType")) {
                        docMetaModel.setBungeniLitigationType(metadata.get(i).getValue());
                    } */
                    
                    System.out.println(metadata.get(i).getName() + " " + metadata.get(i).getType() + " " + metadata.get(i).getValue());
                }
            }
            client.closeConnector();
        } catch (IOException ex) {
            log.error("THe connector client could not be initialized" , ex);
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
    
    private ComboBoxModel setCourtsNamesModel()
    {
        DefaultComboBoxModel judgementCourtsNamesModel = null ;
        String [] judgementCourtsNames = null ; // stores all the bill Names
        
        // initialise the Bungeni Connector Client
         BungeniConnector client = null ;
        try {
            // initialize the data store client
            client = CommonConnectorFunctions.getDSClient();

            // get the bills from the registry H2 db
            List<JudgementCourt> judgementCourtsList = client.getJudgementCourts() ;
            judgementCourtsNames = new String[judgementCourtsList.size()] ;
            
            // loop through extracting the bills
            for (int i = 0 ; i < judgementCourtsList.size() ; i ++)
            {
                // get the current bill & extract the bill Name
                JudgementCourt currJudgementCourt = judgementCourtsList.get(i) ;
                judgementCourtsNames[i] = currJudgementCourt.getNameByLang(Locale.getDefault().getLanguage());
            }

            // create the default bills Names model
            judgementCourtsNamesModel = new DefaultComboBoxModel(judgementCourtsNames) ;
            
        } catch (IOException ex) {
            log.error(ex) ;
        }

         return judgementCourtsNamesModel ;
    }
    
    
    
    
    /* private ComboBoxModel setDomainNamesModel_()
    {
        ReadAndPrintXMLFile xmlFileDomain= new ReadAndPrintXMLFile();
        String [] Domain = null; 
        xmlFileDomain.setFilePath("settings/datasource/xml/judgementDomain.xml");
        xmlFileDomain.setParent("domain");
        xmlFileDomain.setID("id");
        xmlFileDomain.setLabel("label");
        
        DefaultComboBoxModel DomainNamesModel = null ;
        Domain =xmlFileDomain.read();
        DomainNamesModel = new DefaultComboBoxModel(Domain) ;
        return DomainNamesModel ;
        //System.out.println("toz : " +  Arrays.toString(courtType));

    }*/
     
     private ComboBoxModel setDomainNamesModel()
    {
        DefaultComboBoxModel judgementDomainsNamesModel = null ;
        String [] judgementDomainsNames = null ; // stores all the bill Names
        
        // initialise the Bungeni Connector Client
         BungeniConnector client = null ;
        try {
            // initialize the data store client
            client = CommonConnectorFunctions.getDSClient();

            // get the bills from the registry H2 db
            List<JudgementDomain> judgementDomainsList = client.getJudgementDomains() ;
            judgementDomainsNames = new String[judgementDomainsList.size()] ;
            
            // loop through extracting the bills
            for (int i = 0 ; i < judgementDomainsList.size() ; i ++)
            {
                // get the current bill & extract the bill Name
                JudgementDomain currJudgementDomain = judgementDomainsList.get(i) ;
                judgementDomainsNames[i] = currJudgementDomain.getNameByLang(Locale.getDefault().getLanguage());
            }

            // create the default bills Names model
            judgementDomainsNamesModel = new DefaultComboBoxModel(judgementDomainsNames) ;
            
        } catch (IOException ex) {
            log.error(ex) ;
        }

         return judgementDomainsNamesModel ;
    }
     
     
      private ComboBoxModel  setLitigationNamesModel()
    {
        DefaultComboBoxModel judgementLitigationTypesNamesModel = null ;
        String [] judgementLitigationTypesNames = null ; // stores all the bill Names
        
        // initialise the Bungeni Connector Client
         BungeniConnector client = null ;
        try {
            // initialize the data store client
            client = CommonConnectorFunctions.getDSClient();

            // get the bills from the registry H2 db
            List<JudgementLitigationType> judgementLitigationTypesList = client.getJudgementLitigationTypes() ;
            judgementLitigationTypesNames = new String[judgementLitigationTypesList.size()] ;
            
            // loop through extracting the bills
            for (int i = 0 ; i < judgementLitigationTypesList.size() ; i ++)
            {
                // get the current bill & extract the bill Name
                JudgementLitigationType currJudgementLitigationType = judgementLitigationTypesList.get(i) ;
                judgementLitigationTypesNames[i] = currJudgementLitigationType.getNameByLang(Locale.getDefault().getLanguage());
            }

            // create the default bills Names model
            judgementLitigationTypesNamesModel = new DefaultComboBoxModel(judgementLitigationTypesNames) ;
            
        } catch (IOException ex) {
            log.error(ex) ;
        }

         return judgementLitigationTypesNamesModel ;
    }
      
      
      
      
    private ComboBoxModel  settRegionNamesModel()
    {
        DefaultComboBoxModel judgementRegionsNamesModel = null ;
        String [] judgementRegionsNames = null ; // stores all the bill Names
        
        // initialise the Bungeni Connector Client
         BungeniConnector client = null ;
        try {
            // initialize the data store client
            client = CommonConnectorFunctions.getDSClient();

            // get the bills from the registry H2 db
            List<JudgementRegion> judgementRegionsList = client.getJudgementRegions() ;
            judgementRegionsNames = new String[judgementRegionsList.size()] ;
            
            // loop through extracting the bills
            for (int i = 0 ; i < judgementRegionsList.size() ; i ++)
            {
                // get the current bill & extract the bill Name
                JudgementRegion currJudgementRegion = judgementRegionsList.get(i) ;
                judgementRegionsNames[i] = currJudgementRegion.getNameByLang(Locale.getDefault().getLanguage());
            }

            // create the default bills Names model
            judgementRegionsNamesModel = new DefaultComboBoxModel(judgementRegionsNames) ;
            
        } catch (IOException ex) {
            log.error(ex) ;
        }

         return judgementRegionsNamesModel ;
    }  
     
     
    

    public boolean applySelectedMetadata(BungeniFileSavePathFormat spf){
    boolean bState = false;
    try {
    String sCaseNo = this.txtCaseNumber.getText();
    String sRegionName = (String) this.regionNameCombobox.getSelectedItem();
    String sLitigationType = (String) this.litigationTybeCombobox.getSelectedItem();
    
    
    String sDomain  = (String) this.domain.getSelectedItem();
    String scourtType = (String) this.courtNameCombobox.getSelectedItem();
    
    SimpleDateFormat dformatter = new SimpleDateFormat (BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
    final String sIssuedOn = dformatter.format(dt_issuedOn.getDate());
   // String sLitigationType  = (String) this.cb_litigationType.getSelectedItem(); 
   
      
    //get the date of assent
     //SimpleDateFormat dformatter = new SimpleDateFormat (BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
     //final String sDateOfAssent = dformatter.format(dt_dateofassent.getDate());
   
    // (rm, feb 2012) - uses a JComboBox rather than a textfield
     // String sBillName = this.txtBillName.getText();
   // String sBillName = (String) this.billNameCombobox.getSelectedItem();
    //get the assent date
      // SimpleDateFormat dformatter = new SimpleDateFormat (BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
      // final String strDateOfAssent = dformatter.format( this.dt_dateofassent.getDate());
      // final String strDateOfCommencement = dformatter.format(this.dt_dateofcommencement.getDate());
       // docMetaModel.updateItem("BungeniParliamentID")
        docMetaModel.updateItem("BungeniCaseNo", sCaseNo);
        docMetaModel.updateItem("BungeniRegionName", sRegionName);
        docMetaModel.updateItem("BungeniLitigationType", sLitigationType);
        
        
        
        docMetaModel.updateItem("BungeniDomain", sDomain);
        docMetaModel.updateItem("BungeniCourtType", scourtType);
        docMetaModel.updateItem("BungeniIssuedOn", sIssuedOn);
        
        
      //  docMetaModel.updateItem("BungeniLitigationType", sLitigationType);
       
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

        txtCaseNumber = new javax.swing.JTextField();
        dt_issuedOn = new org.jdesktop.swingx.JXDatePicker();
        courtNameCombobox = new javax.swing.JComboBox();
        lblDomainNo = new javax.swing.JLabel();
        domain = new javax.swing.JComboBox();
        lblCourtType = new javax.swing.JLabel();
        lblCaseNo = new javax.swing.JLabel();
        lblIssuedOn = new javax.swing.JLabel();
        litigationTybeCombobox = new javax.swing.JComboBox();
        regionNameCombobox = new javax.swing.JComboBox();
        lblLitigationType = new javax.swing.JLabel();
        lblRegion = new javax.swing.JLabel();

        txtCaseNumber.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtCaseNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCaseNumberActionPerformed(evt);
            }
        });

        dt_issuedOn.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        courtNameCombobox.setModel(setCourtsNamesModel());
        courtNameCombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                courtNameComboboxActionPerformed(evt);
            }
        });

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        lblDomainNo.setText(bundle.getString("JudgemenMetadataExtra.lblDomainNo.text")); // NOI18N

        domain.setModel(setDomainNamesModel());

        lblCourtType.setText(bundle.getString("JudgemenMetadataExtra.lblCourtType.text")); // NOI18N

        lblCaseNo.setText(bundle.getString("JudgemenMetadataExtra.lblCaseNo.text")); // NOI18N

        lblIssuedOn.setText(bundle.getString("JudgemenMetadataExtra.lblIssuedOn.text")); // NOI18N

        litigationTybeCombobox.setModel(setLitigationNamesModel());

        regionNameCombobox.setModel(settRegionNamesModel());

        lblLitigationType.setText(bundle.getString("JudgemenMetadataExtra.lblLitigationType.text")); // NOI18N

        lblRegion.setText(bundle.getString("JudgemenMetadataExtra.lblRegion.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dt_issuedOn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblIssuedOn)
                            .addComponent(txtCaseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCaseNo))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblLitigationType)
                            .addComponent(lblCourtType)
                            .addComponent(litigationTybeCombobox, 0, 123, Short.MAX_VALUE)
                            .addComponent(courtNameCombobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblRegion)
                            .addComponent(lblDomainNo)
                            .addComponent(domain, 0, 108, Short.MAX_VALUE)
                            .addComponent(regionNameCombobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(36, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCourtType)
                    .addComponent(lblDomainNo))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(courtNameCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(domain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLitigationType)
                    .addComponent(lblRegion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(litigationTybeCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(regionNameCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lblCaseNo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCaseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblIssuedOn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dt_issuedOn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * (rm, feb 2012) - This method updates the bill No text field once the relevant 
     * bill is selected from the drop down menu
     * @param evt
     */
    private void courtNameComboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courtNameComboboxActionPerformed
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
                        if (selectedBill.equals(bill.getNameByLang(Locale.getDefault().getLanguage()))) {
                            billNo = bill.getId().toString() ;
                            return billNo ;
                        }
                    }

                    return billNo ;
                }

                public void done() {
                    try {
                        // set the bill No
                        txtCaseNumber.setText((String) get());
                    } catch (InterruptedException ex) {
                        log.error(ex) ;
                    } catch (ExecutionException ex) {
                        log.error(ex) ;
                    }
                }

           };
           
           setBillNo.execute();
        }

    }//GEN-LAST:event_courtNameComboboxActionPerformed

    private void txtCaseNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCaseNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCaseNumberActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox courtNameCombobox;
    private javax.swing.JComboBox domain;
    private org.jdesktop.swingx.JXDatePicker dt_issuedOn;
    private javax.swing.JLabel lblCaseNo;
    private javax.swing.JLabel lblCourtType;
    private javax.swing.JLabel lblDomainNo;
    private javax.swing.JLabel lblIssuedOn;
    private javax.swing.JLabel lblLitigationType;
    private javax.swing.JLabel lblRegion;
    private javax.swing.JComboBox litigationTybeCombobox;
    private javax.swing.JComboBox regionNameCombobox;
    private javax.swing.JTextField txtCaseNumber;
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
                put(java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle").getString("Bill_Name"), courtNameCombobox);
                put(java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle").getString("Bill_No"), txtCaseNumber);
              //  put(java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle").getString("Date_of_Assent"), dt_dateofassent);
              //  put (java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle").getString("Date_of_Commencement"), dt_dateofcommencement);
            }
            });
        return super.validateSelectedMetadata(spf);
     }
}

