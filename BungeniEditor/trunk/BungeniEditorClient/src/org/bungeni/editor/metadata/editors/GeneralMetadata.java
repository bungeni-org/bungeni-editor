/*
 * DebateRecordMetadata.java
 *
 * Created on November 4, 2008, 1:43 PM
 */

package org.bungeni.editor.metadata.editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.editor.metadata.BaseEditorDocMetadataDialog;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.utils.BungeniFileSavePathFormat;
import org.bungeni.editor.metadata.LanguageCode;
import org.bungeni.editor.metadata.CountryCode;
import org.bungeni.editor.metadata.DocumentPart;
import org.bungeni.editor.metadata.GeneralMetadataModel;
import org.bungeni.extutils.CommonStringFunctions;

/**
 *
 * @author  undesa
 */
public class GeneralMetadata extends BaseEditorDocMetadataDialog {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GeneralMetadata.class.getName());
    GeneralMetadataModel docMetaModel = new GeneralMetadataModel();
     

    
    public GeneralMetadata(){
        super();
        initComponents();
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
  
               String sParlId = docMetaModel.getItem("BungeniParliamentID");
                String sParlSitting = docMetaModel.getItem("BungeniParliamentSitting");
                String sParlSession = docMetaModel.getItem("BungeniParliamentSession");
                String sCountryCode = docMetaModel.getItem("BungeniCountryCode");
                String sLanguageCode = docMetaModel.getItem("BungeniLanguageCode");
                String sOfficDate =docMetaModel.getItem("BungeniOfficialDate");
                String sOfficTime = docMetaModel.getItem("BungeniOfficialTime");
                String sPartName = docMetaModel.getItem("BungeniDocPart");
                String sPublicationName = docMetaModel.getItem("BungeniPublicationName");
                String sPublicationDate = docMetaModel.getItem("BungeniPublicationDate");
                //official date
                if (!CommonStringFunctions.emptyOrNull(sOfficDate)) {
                SimpleDateFormat formatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
                this.dt_official_date.setDate(formatter.parse(sOfficDate));
                }
                //official time
                if (!CommonStringFunctions.emptyOrNull(sOfficTime) ) {
                    SimpleDateFormat timeFormat = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataTimeFormat"));
                    dt_official_time.setValue(timeFormat.parse(sOfficTime));
                }
                if (!CommonStringFunctions.emptyOrNull(sPublicationDate)) {
                    SimpleDateFormat timeFormat = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
                    this.dt_publication_date.setDate(timeFormat.parse(sPublicationDate));
                }
                if (!CommonStringFunctions.emptyOrNull(sParlId))
                    this.BungeniParliamentID.setText(sParlId);
                if (!CommonStringFunctions.emptyOrNull(sParlSession))
                    this.txtParliamentSession.setText(sParlSession);
                if (!CommonStringFunctions.emptyOrNull(sParlSitting))
                    this.txtParliamentSitting.setText(sParlSitting);
                if (!CommonStringFunctions.emptyOrNull(sCountryCode))
                    this.cboCountry.setSelectedItem(findCountryCode(sCountryCode));
                if (!CommonStringFunctions.emptyOrNull(sLanguageCode))
                    this.cboLanguage.setSelectedItem(findLanguageCode(sLanguageCode));
                if (!CommonStringFunctions.emptyOrNull(sPartName)) 
                    this.cboDocumentPart.setSelectedItem(findDocumentPart(sPartName));
                if (!CommonStringFunctions.emptyOrNull(sPublicationName)) {
                    this.txtPublicationName.setText(sPublicationName);
                }
                
            } catch (ParseException ex) {
                log.error("initalize()  =  "  + ex.getMessage());
            }
         
        }
    }

    public Component getPanelComponent() {
        return this;
    }
        
    
    private void initControls(){
        String popupDlgBackColor = BungeniEditorProperties.getEditorProperty("popupDialogBackColor");
        this.setBackground(Color.decode(popupDlgBackColor));
        cboCountry.setModel(new DefaultComboBoxModel(countryCodes.toArray()));
        cboLanguage.setModel(new DefaultComboBoxModel(languageCodes.toArray()));
        cboDocumentPart.setModel(new DefaultComboBoxModel(documentParts.toArray()));
         dt_official_time.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.HOUR));
        dt_official_time.setEditor(new JSpinner.DateEditor(dt_official_time, BungeniEditorProperties.getEditorProperty("metadataTimeFormat")));
        ((JSpinner.DefaultEditor)dt_official_time.getEditor()).getTextField().setEditable(false);
 
    }
    
    /*
    private void setURIattributes(){
        BungeniURI workURI = new BungeniURI(this.mWorkURI);
        workURI.setURIComponent(tabTitle, tabTitle);     
        workURI.setURIComponent(tabTitle, tabTitle);     
        workURI.setURIComponent(tabTitle, tabTitle);     

        BungeniURI expURI = new BungeniURI(this.mExpURI);
        BungeniManifestationName fullFileName = new BungeniManifestationName(this.mFileSavePathFormat);
    }
*/
   
    
    
public boolean applySelectedMetadata(BungeniFileSavePathFormat spf){
    boolean bState = false;
    try {
        String sParliamentID = this.BungeniParliamentID.getText();
        String sParliamentSitting = this.txtParliamentSitting.getText();
        String sParliamentSession = this.txtParliamentSession.getText();
        CountryCode selCountry = (CountryCode)this.cboCountry.getSelectedItem();
        LanguageCode selLanguage = (LanguageCode) this.cboLanguage.getSelectedItem();
        DocumentPart selPart = (DocumentPart) this.cboDocumentPart.getSelectedItem();
    //ooDocMetadata docMeta = new ooDocMetadata(ooDocument);
    //get the official time
        SimpleDateFormat tformatter = new SimpleDateFormat (BungeniEditorProperties.getEditorProperty("metadataTimeFormat"));
       Object timeValue = this.dt_official_time.getValue();
       Date hansardTime = (Date) timeValue;
    final String strTimeOfHansard = tformatter.format(hansardTime);
    //get the offical date
       SimpleDateFormat dformatter = new SimpleDateFormat (BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
       final String strDebateDate = dformatter.format( dt_official_date.getDate());
    //get the current date
       Calendar cal = Calendar.getInstance();
       final String strCurrentDate = dformatter.format(cal.getTime());
    //get the publication date
       final String strPubDate = dformatter.format( this.dt_publication_date.getDate());
       final String strPubName = this.txtPublicationName.getText();
       
   // docMetaModel.updateItem("BungeniParliamentID")
    docMetaModel.updateItem("BungeniParliamentID", sParliamentID);
    docMetaModel.updateItem("BungeniParliamentSitting", sParliamentSitting);
    docMetaModel.updateItem("BungeniParliamentSession", sParliamentSession);
    docMetaModel.updateItem("BungeniCountryCode", selCountry.countryCode);
    docMetaModel.updateItem("BungeniLanguageCode", selLanguage.languageCode);
    docMetaModel.updateItem("BungeniOfficialDate", strDebateDate);
    docMetaModel.updateItem("BungeniWorkDate", strDebateDate);
    docMetaModel.updateItem("BungeniExpDate", strCurrentDate);
    docMetaModel.updateItem("BungeniManDate", strCurrentDate);
    docMetaModel.updateItem("BungeniPublicationDate", strPubDate);
    docMetaModel.updateItem("BungeniPublicationName", strPubName);
    docMetaModel.updateItem("BungeniDocAuthor", "Ashok");
    
    docMetaModel.updateItem("BungeniOfficialTime", strTimeOfHansard);
    docMetaModel.updateItem("BungeniDocPart", selPart.PartName);
    
    //other metadata
        docMetaModel.updateItem("BungeniWorkAuthor", "user.Ashok");
        docMetaModel.updateItem("BungeniWorkAuthorURI", "user.Ashok");
        docMetaModel.updateItem("BungeniWorkDateName","workDate");
        //expression
        docMetaModel.updateItem("BungeniExpAuthor", "user.Ashok");
        docMetaModel.updateItem("BungeniExpAuthorURI", "user.Ashok");
        docMetaModel.updateItem("BungeniExpDateName","expDate");
        //manifestation
        docMetaModel.updateItem("BungeniManAuthor", "user.Ashok");
        docMetaModel.updateItem("BungeniManAuthorURI", "user.Ashok");
        docMetaModel.updateItem("BungeniManDateName","manDate");
   
    spf.setSaveComponent("DocumentType", BungeniEditorPropertiesHelper.getCurrentDocType());
    spf.setSaveComponent("CountryCode", selCountry.countryCode);
    spf.setSaveComponent("LanguageCode", selLanguage.languageCode);
    Date dtHansardDate = dt_official_date.getDate();
    GregorianCalendar debateCal = new GregorianCalendar();
    debateCal.setTime(dtHansardDate);
    spf.setSaveComponent("Year", debateCal.get(Calendar.YEAR));
    spf.setSaveComponent("Month", debateCal.get(Calendar.MONTH) + 1);
    spf.setSaveComponent("Day", debateCal.get(Calendar.DAY_OF_MONTH));
    spf.setSaveComponent("PartName", selPart.PartName);
   //  spf.setSaveComponent("FileName", spf.getFileName());
    
    docMetaModel.updateItem("__BungeniDocMeta", "true");
    docMetaModel.saveModel(ooDocument);
    bState = true;
    } catch (Exception ex) {
        log.error("applySelectedMetadata : " + ex.getMessage());
        bState = false;
    } finally {
        return bState;
    }
}    


private final static String STORE_TO_URL = "StoreToURL";
private final static String STORE_AS_URL = "StoreAsURL";

/*private boolean saveDocumentToDisk(BungeniFileSavePathFormat spf){
        boolean bState = false; 
        //1 check if file is already open and saved 
            //if open check if there is a new path for the document different from the current path
            //warn the user provide an option to save to the old path
        //2 if file is not saved - save normally - check if there is a file existing in the path and warn if exists
        try {
        String exportPath = BungeniEditorProperties.getEditorProperty("defaultSavePath");
        exportPath = exportPath.replace('/', File.separatorChar);
        exportPath = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator + exportPath + File.separator + spf.getSavePath();
        
        String fileFullPath = "";
        fileFullPath = exportPath + File.separator + spf.getFileName();

        File fFile = new File(fileFullPath);

        HashMap<String,Object> saveParams = new HashMap<String,Object>();
        
        //1
        if (ooDocument.isDocumentOnDisk()) {
            //document already exists... we just need to save it
            //check generated URL 
            URL genURL = fFile.toURI().toURL();
            //get current open url
            URL curURL = new URL(ooDocument.getDocumentURL());
            if (curURL.toString().equals(genURL.toString())) {
                //the generated and current urls are euals... we can simply sav
                //storeToURL
                 saveParams.put(STORE_TO_URL, fFile.toURI().toString());

            } else {
                int nConfirm = MessageBox.Confirm(parentFrame, "The document will be saved to a new location, as the URI of the document has changed \n Click Yes to save to a new location, Click No to continue saving the document at the current location", "Warning");
                if (nConfirm == JOptionPane.YES_OPTION) {
                 //storeAsURL to new path
                  if (fFile.exists()) {
                        //error message and abort
                        MessageBox.OK(parentFrame, "A file with these attributes already exists, \n please edit the existing file or amend the attributes to save as a different file");
                        bState = false;
                        return false;
                  } else {    
                    File fDir = new File(exportPath);
                    if (!fDir.exists()) {
                        //if path does not exist, create it
                        fDir.mkdirs();
                    }
                    saveParams.put(STORE_AS_URL, genURL.toURI().toString());
                  }
                } else {
                    saveParams.put(STORE_TO_URL, curURL.toURI().toString());
                    //storeTOURL to old path
                }
             }
        } else {
            //check if there is an existing file at the generated path
            if (fFile.exists()) {
                //error message and abort
                MessageBox.OK(parentFrame, "A file with these attributes already exists, \n please edit the existing file or amend the attributes to save as a different file");
                bState = false;
                return false;
            } else {
                //save to new path
                 File fDir = new File(exportPath);
                if (!fDir.exists()) {
                    //if path does not exist, create it
                    fDir.mkdirs();
                    }
                    saveParams.put(STORE_AS_URL, fFile.toURI().toString());
            }
        }
        
        IBungeniDocTransform idocTrans = BungeniTransformationTargetFactory.getDocTransform("ODT");
        idocTrans.setParams(saveParams);
        bState= idocTrans.transform(ooDocument);

        } catch (Exception ex) {
            log.error("saveDocumentToDisk : " + ex.getMessage());
            bState = false;
        } finally {
        
        if (bState) {
            MessageBox.OK(parentFrame, "Document was Saved!");
            return true;
        } else {
            MessageBox.OK(parentFrame, "The Document could not be saved!");
            return false;
        }
        }
}
  */  




    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        cboLanguage = new javax.swing.JComboBox();
        cboCountry = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        cboDocumentPart = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        dt_publication_date = new org.jdesktop.swingx.JXDatePicker();
        jLabel9 = new javax.swing.JLabel();
        txtPublicationName = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        BungeniParliamentID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtParliamentSession = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtParliamentSitting = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        dt_official_date = new org.jdesktop.swingx.JXDatePicker();
        jLabel14 = new javax.swing.JLabel();
        dt_official_time = new javax.swing.JSpinner();
        jLabel15 = new javax.swing.JLabel();

        setBackground(java.awt.Color.lightGray);

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        jLabel2.setText("Language");
        jLabel2.setName("lbl.BungeniLanguageID"); // NOI18N

        cboLanguage.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        cboLanguage.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLanguage.setName("fld.BungeniLanguageID"); // NOI18N

        cboCountry.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCountry.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboCountry.setName("fld.BungeniCountryCode"); // NOI18N

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        jLabel1.setText("Country");
        jLabel1.setName("lbl.BungeniCountryCode"); // NOI18N

        cboDocumentPart.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        cboDocumentPart.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel8.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        jLabel8.setText("Document Part");
        jLabel8.setName("lbl.BungeniLanguageID"); // NOI18N

        dt_publication_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        jLabel9.setText("Publication Date");

        txtPublicationName.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        jLabel10.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        jLabel10.setText("Publication Name");

        BungeniParliamentID.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        BungeniParliamentID.setName("fld.BungeniParliamentID"); // NOI18N

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        jLabel3.setText("Parliament ID");
        jLabel3.setName("lbl.BungeniParliamentID"); // NOI18N

        txtParliamentSession.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        txtParliamentSession.setName("fld.BungeniParliamentSession"); // NOI18N

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        jLabel4.setText("Parliament Session");
        jLabel4.setName("lbl.BungeniParliamentSession"); // NOI18N

        txtParliamentSitting.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        txtParliamentSitting.setName("BungeniParliamentSitting"); // NOI18N

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        jLabel5.setText("Parliament Sitting");
        jLabel5.setName("lbl.BungeniParliamentSitting"); // NOI18N

        dt_official_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        jLabel14.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        jLabel14.setText("Date");

        jLabel15.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        jLabel15.setText("Time");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(BungeniParliamentID, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(txtParliamentSitting, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel4)
                    .addComponent(txtParliamentSession, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboDocumentPart, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(cboCountry, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dt_publication_date, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPublicationName, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(dt_official_date, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dt_official_time, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCountry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDocumentPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_publication_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPublicationName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BungeniParliamentID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtParliamentSitting, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25))
                            .addComponent(txtParliamentSession, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_official_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_official_time, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField BungeniParliamentID;
    private javax.swing.JComboBox cboCountry;
    private javax.swing.JComboBox cboDocumentPart;
    private javax.swing.JComboBox cboLanguage;
    private org.jdesktop.swingx.JXDatePicker dt_official_date;
    private javax.swing.JSpinner dt_official_time;
    private org.jdesktop.swingx.JXDatePicker dt_publication_date;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtParliamentSession;
    private javax.swing.JTextField txtParliamentSitting;
    private javax.swing.JTextField txtPublicationName;
    // End of variables declaration//GEN-END:variables



    @Override
    public Dimension getFrameSize() {
        int DIM_X = 347 ; int DIM_Y = 294 ;
        return new Dimension(DIM_X, DIM_Y + 10);
    }



   


}
