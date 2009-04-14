/*
 * DebateRecordMetadata.java
 *
 * Created on November 4, 2008, 1:43 PM
 */

package org.bungeni.editor.metadata.editors;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DateEditor;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
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
       // String popupDlgBackColor = BungeniEditorProperties.getEditorProperty("popupDialogBackColor");
       // this.setBackground(Color.decode(popupDlgBackColor));
        cboCountry.setModel(new DefaultComboBoxModel(countryCodes.toArray()));
        cboLanguage.setModel(new DefaultComboBoxModel(languageCodes.toArray()));
        cboDocumentPart.setModel(new DefaultComboBoxModel(documentParts.toArray()));
        dt_official_time.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.HOUR));
        dt_official_time.setEditor(new JSpinner.DateEditor(dt_official_time, BungeniEditorProperties.getEditorProperty("metadataTimeFormat")));
        ((DateEditor)dt_official_time.getEditor()).getTextField().addFocusListener(new FocusAdapter(){
            @Override
            public void focusGained(FocusEvent e) {
                 Component c = e.getComponent();
                 if (c instanceof JFormattedTextField) {
                     selectItLater(c);
                 } else if (c instanceof JTextField) {
                     ((JTextField)c).selectAll();
                 }
            }
        });

 
    }

    protected void selectItLater(Component c) {
        if (c instanceof JFormattedTextField) {
            final JFormattedTextField ftf = (JFormattedTextField) c;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ftf.selectAll();
                }
            });
        }
    }
    
    
public boolean applySelectedMetadata(BungeniFileSavePathFormat spf){
    boolean bState = false;
    try {
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






    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblLanguage = new javax.swing.JLabel();
        cboLanguage = new javax.swing.JComboBox();
        cboCountry = new javax.swing.JComboBox();
        lblCountry = new javax.swing.JLabel();
        cboDocumentPart = new javax.swing.JComboBox();
        lblDocumentPart = new javax.swing.JLabel();
        dt_publication_date = new org.jdesktop.swingx.JXDatePicker();
        lblPublicationDate = new javax.swing.JLabel();
        txtPublicationName = new javax.swing.JTextField();
        lblPublicationName = new javax.swing.JLabel();
        dt_official_date = new org.jdesktop.swingx.JXDatePicker();
        lblOfficialDate = new javax.swing.JLabel();
        dt_official_time = new javax.swing.JSpinner();
        lblOfficialTime = new javax.swing.JLabel();

        lblLanguage.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        lblLanguage.setText(bundle.getString("GeneralMetadata.lblLanguage.text")); // NOI18N
        lblLanguage.setName("lbl.BungeniLanguageID"); // NOI18N

        cboLanguage.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        cboLanguage.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLanguage.setName("fld.BungeniLanguageID"); // NOI18N

        cboCountry.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        cboCountry.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboCountry.setName("fld.BungeniCountryCode"); // NOI18N

        lblCountry.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCountry.setText(bundle.getString("GeneralMetadata.lblCountry.text")); // NOI18N
        lblCountry.setName("lbl.BungeniCountryCode"); // NOI18N

        cboDocumentPart.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        cboDocumentPart.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblDocumentPart.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblDocumentPart.setText(bundle.getString("GeneralMetadata.lblDocumentPart.text")); // NOI18N
        lblDocumentPart.setName("lbl.BungeniLanguageID"); // NOI18N

        dt_publication_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        lblPublicationDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblPublicationDate.setText(bundle.getString("GeneralMetadata.lblPublicationDate.text")); // NOI18N

        txtPublicationName.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        lblPublicationName.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblPublicationName.setText(bundle.getString("GeneralMetadata.lblPublicationName.text")); // NOI18N

        dt_official_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        lblOfficialDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblOfficialDate.setText(bundle.getString("GeneralMetadata.lblOfficialDate.text")); // NOI18N

        lblOfficialTime.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblOfficialTime.setText(bundle.getString("GeneralMetadata.lblOfficialTime.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDocumentPart, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboDocumentPart, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCountry)
                            .addComponent(cboCountry, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblLanguage)
                            .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dt_publication_date, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPublicationDate, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPublicationName, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPublicationName, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblOfficialDate)
                            .addComponent(dt_official_date, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dt_official_time, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblOfficialTime))))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblCountry, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCountry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addComponent(lblDocumentPart, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDocumentPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblPublicationDate, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_publication_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblPublicationName, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPublicationName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblOfficialDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_official_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblOfficialTime)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_official_time, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(85, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cboCountry;
    private javax.swing.JComboBox cboDocumentPart;
    private javax.swing.JComboBox cboLanguage;
    private org.jdesktop.swingx.JXDatePicker dt_official_date;
    private javax.swing.JSpinner dt_official_time;
    private org.jdesktop.swingx.JXDatePicker dt_publication_date;
    private javax.swing.JLabel lblCountry;
    private javax.swing.JLabel lblDocumentPart;
    private javax.swing.JLabel lblLanguage;
    private javax.swing.JLabel lblOfficialDate;
    private javax.swing.JLabel lblOfficialTime;
    private javax.swing.JLabel lblPublicationDate;
    private javax.swing.JLabel lblPublicationName;
    private javax.swing.JTextField txtPublicationName;
    // End of variables declaration//GEN-END:variables



    @Override
    public Dimension getFrameSize() {
        int DIM_X = 347 ; int DIM_Y = 294 ;
        return new Dimension(DIM_X, DIM_Y + 10);
    }

    @Override
    public ArrayList<String> validateSelectedMetadata(BungeniFileSavePathFormat spf) {
         addFieldsToValidate (new TreeMap<String,Component>(){
            {
                put("Publication Date", dt_publication_date );
                put("Publication Name", txtPublicationName);
                put ("Official Date", dt_official_date);
                put ("Official Time", dt_official_time);
            }
            });
        return super.validateSelectedMetadata(spf);
    }



   


}
