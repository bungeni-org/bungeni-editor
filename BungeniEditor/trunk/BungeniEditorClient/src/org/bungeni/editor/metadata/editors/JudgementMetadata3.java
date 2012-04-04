package org.bungeni.editor.metadata.editors;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
//import org.bungeni.db.BungeniClientDB;
//import org.bungeni.db.RegistryQueryFactory;
//import org.bungeni.db.registryQueryDialog2;
import org.bungeni.ooo.ooDocMetadataFieldSet;
import org.bungeni.editor.metadata.BaseEditorDocMetadataDialog;
import org.bungeni.editor.metadata.JudgementMetadataModel;
import org.bungeni.editor.metadata.TabularMetadataLoader;
import org.bungeni.editor.metadata.TabularMetadataLoader.TabularMetadataModel;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.ooo.ooDocMetadata;
import org.bungeni.utils.BungeniFileSavePathFormat;
import org.bungeni.extutils.CommonStringFunctions;

/**
 *
 * @author  Ashok
 */
public class JudgementMetadata3 extends BaseEditorDocMetadataDialog {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JudgementMetadata3.class.getName());
    JudgementMetadataModel docMetaModel = new JudgementMetadataModel();
    ///registryQueryDialog2 rqs = null;

    
    public JudgementMetadata3(){
        super();
        initComponents();
        btnAddParty.setVisible(false);
        btnAddJudge.setVisible(false);
      //  this.tabbedJudges.getTabComponentAt(1).setVisible(false);
      //  this.tabbedJudges.getTabComponentAt(2).setVisible(false);
        }
    
    @Override
    public void initialize() {
        super.initialize();

        
        this.docMetaModel.setup();
        initControls();

        
        
           if (theMode == SelectorDialogModes.TEXT_EDIT) {
            try {
                //retrieve metadata... and set in controls....
                docMetaModel.loadModel(ooDocument);
  
                String sJudgementNo = docMetaModel.getItem("BungeniJudgementNo");
                String sCaseNo = docMetaModel.getItem("BungeniCaseNo");
                String sJudgementDate = docMetaModel.getItem("BungeniJudgementDate");
                if (!CommonStringFunctions.emptyOrNull(sJudgementNo))
                    this.txtJudgementNo.setText(sJudgementNo);
                if (!CommonStringFunctions.emptyOrNull(sCaseNo))
                    this.txtCaseNo.setText(sCaseNo);
                 if (!CommonStringFunctions.emptyOrNull(sJudgementDate)) {
                    this.dt_judgement_date.setDate(sdfDateFormat.parse(sJudgementDate));
                }
                this.initJudgesTableModel();
                this.initPartiesTableModel();
            } catch (Exception ex) {
                log.error("initalize()  =  "  + ex.getMessage());
            }
         
        }
        tabbedJudges.removeTabAt(1);
        tabbedJudges.removeTabAt(1);

    }

 
    public DefaultTableModel getRegistryDataAsTableModel(String query) {
        DefaultTableModel model = null;
        try {
        // HashMap<String,String> registryMap = BungeniRegistryFactory.fullConnectionString();
          /**BungeniClientDB dbReg = new BungeniClientDB(registryMap);
          dbReg.Connect();
          QueryResults qr = dbReg.QueryResults(query);
          dbReg.EndConnect();
          System.out.println("has results = " + qr.hasResults());
          if (qr.hasResults()) {
              String[] columns = qr.getColumns();
              int columnCount = columns.length;
              int rowCount = qr.theResults().size();
              Vector<String> vColumns = new Vector<String>();
              Vector<Vector<String>> vData = new Vector<Vector<String>>();
              vData = qr.theResults();
              Collections.addAll(vColumns, columns);
              model = new DefaultTableModel(vData, vColumns);

          } */
        } catch (Exception ex) {
            log.error("getRegistryDataAsTableModel : " + ex.getMessage());
        } 
       return model;
    }
    
    private void fetchParties(String judgementNo) {
       // String searchQuery = RegistryQueryFactory.Q_FETCH_JUDGEMENT_PARTIES(judgementNo);
       // DefaultTableModel partyModel = getRegistryDataAsTableModel(searchQuery);
      //  this.tblParties.setModel(partyModel);
    }

   private void fetchJudges(String judgementNo) {
       /**
        String searchQuery = RegistryQueryFactory.Q_FETCH_JUDGEMENT_JUDGES(judgementNo);
        DefaultTableModel partyModel = getRegistryDataAsTableModel(searchQuery);
        this.tblJudges.setModel(partyModel); **/
       
    }

    private void fetchRelatedRegistryData(String judgementNo) {
            fetchJudges(judgementNo);
            fetchParties(judgementNo);

    }

    private void initJudgementComboSelector(){
        String sJudgementNo = docMetaModel.getItem("BungeniJudgementNo");
        //judgment number isnt in the document, populate the selector combo
        if (CommonStringFunctions.emptyOrNull(sJudgementNo)) {
            //String sQuery = "SELECT ID, NAME, JUDGEMENT_DATE, HEARING_DATE FROM JUDGEMENTS";
            this.txtJudgementNo.setText("");
            this.btnSelectJudgementNo.setEnabled(true);
        } else {
            //set the judgement no in the selector combo and set it to readonly
            this.txtJudgementNo.setText(sJudgementNo);
            this.btnSelectJudgementNo.setEnabled(false);
            //this.cboJudgementNo.addItem(sJudgementNo);
            //this.cboJudgementNo.setEnabled(false);
        }
     }
    
    
    private void initJudgesTableModel(){
        int[] filterIndices = {0};
        TabularMetadataModel model = TabularMetadataLoader.getTabularMetadataTableModel(ooDocument,BUNGENI_JUDGE_META_PREFIX, filterIndices );
        DefaultTableModel tblModel = model.tabularModel;
        this.tblJudges.setModel(tblModel);
       // Vector tblModelVector = this.loadJudgesModelFromDocument();
       // ((DefaultTableModel)this.tblJudges.getModel()).setDataVector(tblModelVector, new Vector(Arrays.asList(tableColumns)));
    }
    
    private void initPartiesTableModel(){
        TabularMetadataModel model = TabularMetadataLoader.getTabularMetadataTableModel(ooDocument,BUNGENI_PARTY_META_PREFIX );
        DefaultTableModel tblModel = model.tabularModel;
        this.tblParties.setModel(tblModel);
        
    }
    
    public Component getPanelComponent() {
        return this;
    }
    
    private void initControls(){
        this.initJudgementComboSelector();
    }
    

public boolean applySelectedMetadata(BungeniFileSavePathFormat spf){
    boolean bState = false;
    try {
        String sJudgementNo = this.txtJudgementNo.getText();
        String sCaseNo = this.txtCaseNo.getText();
        Date dJudgementDate = this.dt_judgement_date.getDate();
        String sJudgementDate = this.sdfDateFormat.format(dJudgementDate);
        //get the assent date
       // docMetaModel.updateItem("BungeniParliamentID")
        docMetaModel.updateItem("BungeniJudgementNo", sJudgementNo);
        docMetaModel.updateItem("BungeniCaseNo", sCaseNo);
        docMetaModel.updateItem("BungeniJudgementDate", sJudgementDate);
        //this saves the judges listing in the table into the document
        System.out.println("docmetamodel size before = " + docMetaModel.getMetaNames().size());
        this.saveJudgesMetadataToDocument();
        this.savePartiesMetadataToDocument();
        System.out.println("docmetamodel size after = " + docMetaModel.getMetaNames().size());
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

        tabbedJudges = new javax.swing.JTabbedPane();
        panelJudgementInfo = new javax.swing.JPanel();
        lblJudgementNo = new javax.swing.JLabel();
        lblCaseNo = new javax.swing.JLabel();
        txtCaseNo = new javax.swing.JTextField();
        lblJudgementDate = new javax.swing.JLabel();
        dt_judgement_date = new org.jdesktop.swingx.JXDatePicker();
        txtJudgementNo = new javax.swing.JTextField();
        btnSelectJudgementNo = new javax.swing.JButton();
        panelJudges = new javax.swing.JPanel();
        scrollJudges = new javax.swing.JScrollPane();
        tblJudges = new javax.swing.JTable();
        btnAddJudge = new javax.swing.JButton();
        panelParties = new javax.swing.JPanel();
        scrollJudges1 = new javax.swing.JScrollPane();
        tblParties = new javax.swing.JTable();
        btnAddParty = new javax.swing.JButton();

        tabbedJudges.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        lblJudgementNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        lblJudgementNo.setText(bundle.getString("JudgementMetadata3.lblJudgementNo.text")); // NOI18N

        lblCaseNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblCaseNo.setText(bundle.getString("JudgementMetadata3.lblCaseNo.text")); // NOI18N

        txtCaseNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        lblJudgementDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblJudgementDate.setText(bundle.getString("JudgementMetadata3.lblJudgementDate.text")); // NOI18N

        dt_judgement_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        txtJudgementNo.setEditable(false);
        txtJudgementNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        btnSelectJudgementNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnSelectJudgementNo.setText(bundle.getString("Select_dotdot")); // NOI18N
        btnSelectJudgementNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectJudgementNoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelJudgementInfoLayout = new javax.swing.GroupLayout(panelJudgementInfo);
        panelJudgementInfo.setLayout(panelJudgementInfoLayout);
        panelJudgementInfoLayout.setHorizontalGroup(
            panelJudgementInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelJudgementInfoLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(panelJudgementInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelJudgementInfoLayout.createSequentialGroup()
                        .addComponent(txtJudgementNo, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelectJudgementNo))
                    .addGroup(panelJudgementInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtCaseNo, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblJudgementDate, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblJudgementNo, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblCaseNo, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(dt_judgement_date, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        panelJudgementInfoLayout.setVerticalGroup(
            panelJudgementInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelJudgementInfoLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(lblJudgementNo)
                .addGap(4, 4, 4)
                .addGroup(panelJudgementInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSelectJudgementNo)
                    .addGroup(panelJudgementInfoLayout.createSequentialGroup()
                        .addComponent(txtJudgementNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCaseNo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCaseNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(lblJudgementDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_judgement_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(113, Short.MAX_VALUE))
        );

        tabbedJudges.addTab(bundle.getString("JudgementMetadata3.panelJudgementInfo.TabConstraints.tabTitle"), panelJudgementInfo); // NOI18N

        tblJudges.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        tblJudges.setModel(new javax.swing.table.DefaultTableModel(
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
        scrollJudges.setViewportView(tblJudges);

        btnAddJudge.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnAddJudge.setText(bundle.getString("Add_Judge")); // NOI18N
        btnAddJudge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddJudgeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelJudgesLayout = new javax.swing.GroupLayout(panelJudges);
        panelJudges.setLayout(panelJudgesLayout);
        panelJudgesLayout.setHorizontalGroup(
            panelJudgesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelJudgesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelJudgesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollJudges, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                    .addComponent(btnAddJudge))
                .addContainerGap())
        );
        panelJudgesLayout.setVerticalGroup(
            panelJudgesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelJudgesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAddJudge)
                .addGap(3, 3, 3)
                .addComponent(scrollJudges, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(127, Short.MAX_VALUE))
        );

        tabbedJudges.addTab(bundle.getString("JudgementMetadata3.panelJudges.TabConstraints.tabTitle"), panelJudges); // NOI18N

        tblParties.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        tblParties.setModel(new javax.swing.table.DefaultTableModel(
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
        scrollJudges1.setViewportView(tblParties);

        btnAddParty.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnAddParty.setText(bundle.getString("Add_Party")); // NOI18N
        btnAddParty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPartyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPartiesLayout = new javax.swing.GroupLayout(panelParties);
        panelParties.setLayout(panelPartiesLayout);
        panelPartiesLayout.setHorizontalGroup(
            panelPartiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPartiesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPartiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollJudges1, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                    .addComponent(btnAddParty))
                .addContainerGap())
        );
        panelPartiesLayout.setVerticalGroup(
            panelPartiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPartiesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAddParty)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollJudges1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(124, Short.MAX_VALUE))
        );

        tabbedJudges.addTab(bundle.getString("JudgementMetadata3.panelParties.TabConstraints.tabTitle"), panelParties); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedJudges, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedJudges, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

private void btnAddJudgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddJudgeActionPerformed
// TODO add your handling code here:
    /***
        rqs = new registryQueryDialog2(java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle").getString("Select_A_Judge"), "Select FIRST_NAME, LAST_NAME , URI from JUDGES order by LAST_NAME", parentFrame);
        rqs.setMultiSelect(true);
        rqs.show();
        Vector selectionData = rqs.getData();
        String[] selectionColumns = rqs.getDataColumns();
        Vector vColumns = new Vector(Arrays.asList(selectionColumns));
        Vector existingData = ((DefaultTableModel)this.tblJudges.getModel()).getDataVector();
        existingData.addAll(selectionData);
        ((DefaultTableModel)this.tblJudges.getModel()).setDataVector(existingData, vColumns); */
       // this.tblJudges.setModel(arg0)
}//GEN-LAST:event_btnAddJudgeActionPerformed

private void btnSelectJudgementNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectJudgementNoActionPerformed
/***        try {
// TODO add your handling code here:
      
            rqs = new registryQueryDialog2(java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle").getString("Select_A_Judgement_No"), RegistryQueryFactory.Q_FETCH_JUDGEMENTS(), parentFrame);
            rqs.setMultiSelect(false);
            rqs.show();
            if (rqs.applyClicked()) {
                Vector selectionData = rqs.getData();
                Vector selectVector = (Vector)selectionData.elementAt(0);
                String judgementNo = (String) selectVector.elementAt(0);
                this.txtJudgementNo.setText(judgementNo);
                String judgementName = (String) selectVector.elementAt(1);
                String judgementDate = (String) selectVector.elementAt(2);
                this.dt_judgement_date.setDate(sdfDateFormat.parse(judgementDate));
                String judgementHearingDate = (String) selectVector.elementAt(3);
                //populate all the other data
                fetchRelatedRegistryData(judgementNo); 
            }
        } catch (ParseException ex) {
            log.error(ex.getMessage());
        }
        //populate all the other data ***/
}//GEN-LAST:event_btnSelectJudgementNoActionPerformed


private void btnAddPartyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPartyActionPerformed
       /***
        rqs = new registryQueryDialog2(java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle").getString("Select_A_Party"), "Select ID, PARTY_NAME , PARTY_TYPE from PARTIES order by PARTY_NAME", parentFrame);
        rqs.setMultiSelect(true);
        rqs.show();
        Vector selectionData = rqs.getData();
        String[] selectionColumns = rqs.getDataColumns();
        Vector vColumns = new Vector(Arrays.asList(selectionColumns));
        Vector existingData = ((DefaultTableModel)this.tblParties.getModel()).getDataVector();
        existingData.addAll(selectionData);
        ((DefaultTableModel)this.tblParties.getModel()).setDataVector(existingData, vColumns); ***/
}//GEN-LAST:event_btnAddPartyActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddJudge;
    private javax.swing.JButton btnAddParty;
    private javax.swing.JButton btnSelectJudgementNo;
    private org.jdesktop.swingx.JXDatePicker dt_judgement_date;
    private javax.swing.JLabel lblCaseNo;
    private javax.swing.JLabel lblJudgementDate;
    private javax.swing.JLabel lblJudgementNo;
    private javax.swing.JPanel panelJudgementInfo;
    private javax.swing.JPanel panelJudges;
    private javax.swing.JPanel panelParties;
    private javax.swing.JScrollPane scrollJudges;
    private javax.swing.JScrollPane scrollJudges1;
    private javax.swing.JTabbedPane tabbedJudges;
    private javax.swing.JTable tblJudges;
    private javax.swing.JTable tblParties;
    private javax.swing.JTextField txtCaseNo;
    private javax.swing.JTextField txtJudgementNo;
    // End of variables declaration//GEN-END:variables


    @Override
    public Dimension getFrameSize() {
        int DIM_X = 229 ; int DIM_Y = 222 ;
        return new Dimension(DIM_X, DIM_Y + 10);
    }
    
    private int getColNoByName(String[] colArr, String colName) {
        for (int i = 0; i < colArr.length; i++) {
            String string = colArr[i];
            if (string.equals(colName)) {
                return i;
            }
        }
        return -1;
    }
    
    private String getValueByName(String[] colArr, Vector selRow, String colname) {
        int nColNo = getColNoByName(colArr, colname);
        if (nColNo == -1) return null;
        String foundValue = (String)selRow.elementAt(nColNo);
        return foundValue;
        
    }
    
    private static final String BUNGENI_JUDGE_META_PREFIX = "BungeniJudgeName:";
    
    private void saveJudgesMetadataToDocument(){
        DefaultTableModel model = (DefaultTableModel)this.tblJudges.getModel();
        Vector vData = model.getDataVector();
        for (int i = 0; i < vData.size(); i++) {
            Vector rowData = (Vector) vData.elementAt(i);
            addJudgeMetadataVariable(rowData);
           // buildMetadataVariable()
        }
    }

    private static final String BUNGENI_PARTY_META_PREFIX = "BungeniPartyName:";
    
    private void savePartiesMetadataToDocument(){
        DefaultTableModel model = (DefaultTableModel)this.tblParties.getModel();
        Vector vData = model.getDataVector();
        for (int i = 0; i < vData.size(); i++) {
            Vector rowData = (Vector) vData.elementAt(i);
            addPartyMetadataVariable(rowData);
           // buildMetadataVariable()
        }
        
    }

        
    private void addPartyMetadataVariable(Vector rowData) {
        String partyURI = (String) rowData.elementAt(0);
        String partyName = (String) rowData.elementAt(1);
        String partyType = (String) rowData.elementAt(2);
        String partyId = partyURI.replaceAll("[^a-zA-Z0-9_]", "");
        String metadataVariable = BUNGENI_PARTY_META_PREFIX + partyId;
        String metadataValue = partyId + "~" + partyURI + "~"  + partyName + "~" + partyType;
        //add metadata variable to model
        docMetaModel.addItem(metadataVariable, metadataValue);
    }

    
    private void addJudgeMetadataVariable(Vector rowData) {
        String firstName = (String) rowData.elementAt(0);
        String lastName = (String) rowData.elementAt(1);
        String uri = (String) rowData.elementAt(2);
        String metadataVariable = BUNGENI_JUDGE_META_PREFIX + uri;
        //metadata is stored in the form :
        //id ~ first name ~ last name ~ uri
        //is is a shortende duri
        String sId = uri.replaceAll("[^a-zA-Z0-9_]", "");
        String metadataValue = sId  + "~" + firstName + "~" + lastName + "~" + uri;
        //add metadata variable to model
        docMetaModel.addItem(metadataVariable, metadataValue);
    }

    /**
     * This is a custom metadata model loader that loads all the Judgement related metadata
     * @return
     */
    private Vector loadJudgesModelFromDocument(){
        String findMetaPrefix = BUNGENI_JUDGE_META_PREFIX.replaceAll(":", "");
        ArrayList<ooDocMetadataFieldSet> metaObjectByType = ooDocMetadata.getMetadataObjectsByType(ooDocument, findMetaPrefix);
        Vector vTableModel = new Vector();
        for (Iterator<ooDocMetadataFieldSet> it = metaObjectByType.iterator(); it.hasNext();) {
            ooDocMetadataFieldSet docMetadataFieldSet = it.next();
            String metaName = docMetadataFieldSet.getMetadataName();
            String metaValue = docMetadataFieldSet.getMetadataValue();
            String[] judgeMetaValues = metaValue.split("~");
            Vector vRow = new Vector(Arrays.asList(judgeMetaValues));
            //we remove the first column containing judge id
            //since its an internal reserved value used during translation
            //it is never shown to the user
            vRow.remove(0);
            vTableModel.add(vRow);
            //add first name, last name , 
        }
        return vTableModel;
        
    }

       @Override
     public ArrayList<String> validateSelectedMetadata(BungeniFileSavePathFormat spf) {
         addFieldsToValidate (new TreeMap<String,Component>(){
            {
                put(lblJudgementNo.getText().replace("*",""), txtJudgementNo);
                put(lblCaseNo.getText().replace("*", ""), txtCaseNo);
                put(lblJudgementDate.getText().replace("*", ""), dt_judgement_date);
            }
            });
        return super.validateSelectedMetadata(spf);
     }




}
