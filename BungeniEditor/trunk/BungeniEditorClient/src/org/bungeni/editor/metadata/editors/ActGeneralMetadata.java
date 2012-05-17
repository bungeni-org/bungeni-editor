/*
 * Copyright (C) 2012 bzuadmin
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.bungeni.editor.metadata.editors;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.SwingUtilities;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.element.SourceType;
import org.bungeni.editor.metadata.ActGeneralMetadataModel;
import org.bungeni.editor.metadata.BaseEditorDocMetadataDialog;
import org.bungeni.editor.metadata.LanguageCode;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.extutils.*;
import org.bungeni.utils.BungeniFileSavePathFormat;

/**
 *
 * @author bzuadmin
 */
public class ActGeneralMetadata extends BaseEditorDocMetadataDialog{
    
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GeneralMetadata.class.getName());
   ActGeneralMetadataModel docMetaModel = new ActGeneralMetadataModel();

    /**
     * Creates new customizer ActGeneralMetadata
     */
    public ActGeneralMetadata() {
         super();
        initComponents();
        CommonUIFunctions.compOrientation(this);
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
  
                String sLanguageCode = docMetaModel.getItem("BungeniLanguageCode");
                String sPublicationName = docMetaModel.getItem("BungeniPublicationName");
                String sPublicationDate = docMetaModel.getItem("BungeniPublicationDate");
                String sPublicationArea = docMetaModel.getItem("BungeniPublicationArea");
                String sSourceType = docMetaModel.getItem("BungeniSourceType");
                String sSourceNo = docMetaModel.getItem("BungeniSourceNo");
                
                if (!CommonStringFunctions.emptyOrNull(sLanguageCode)){
                    this.cboLanguage.setSelectedItem(findLanguageCode(sLanguageCode));
                }
                //effective date
               
                if (!CommonStringFunctions.emptyOrNull(sPublicationDate)) {
                    SimpleDateFormat timeFormat = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
                    this.dt_publication_date.setDate(timeFormat.parse(sPublicationDate));
                }
                if (!CommonStringFunctions.emptyOrNull(sPublicationName)) {
                    this.txtPublicationName.setText(sPublicationName);
                }
                 if (!CommonStringFunctions.emptyOrNull(sPublicationArea)) {
                    this.txtPublicationArea.setText(sPublicationArea);
                }
               
                if (!CommonStringFunctions.emptyOrNull(sSourceType)) {
                    this.cboSourceType.setSelectedItem(sSourceType);
                }
                if (!CommonStringFunctions.emptyOrNull(sSourceNo)) {
                    this.txtSourceNo.setText(sSourceNo);
                }
                
            } catch (ParseException ex) {
                log.error("initalize()  =  "  + ex.getMessage());
            }
         
        }
    }
     
     private void initControls(){
       // String popupDlgBackColor = BungeniEditorProperties.getEditorProperty("popupDialogBackColor");
       // this.setBackground(Color.decode(popupDlgBackColor));
        cboLanguage.setModel(new DefaultComboBoxModel(languageCodes));
       
        //set Default selections
        this.cboLanguage.setSelectedItem(findLanguageCode(Locale.getDefault().getLanguage()));  
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblLanguage = new javax.swing.JLabel();
        cboLanguage = new javax.swing.JComboBox();
        dt_publication_date = new org.jdesktop.swingx.JXDatePicker();
        txtPublicationName = new javax.swing.JTextField();
        txtPublicationArea = new javax.swing.JTextField();
        txtSourceNo = new javax.swing.JTextField();
        cboSourceType = new javax.swing.JComboBox();
        lblPublicationDate = new javax.swing.JLabel();
        lblPublicationName = new javax.swing.JLabel();
        lblPublicationArea = new javax.swing.JLabel();
        lblSourceNo = new javax.swing.JLabel();
        lblSourceType = new javax.swing.JLabel();

        lblLanguage.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        lblLanguage.setText(bundle.getString("GeneralMetadata.lblLanguage.text")); // NOI18N

        cboLanguage.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboLanguage.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLanguage.setMaximumSize(new java.awt.Dimension(107, 21));
        cboLanguage.setMinimumSize(new java.awt.Dimension(107, 21));

        dt_publication_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        txtPublicationName.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtPublicationName.setMaximumSize(new java.awt.Dimension(107, 21));
        txtPublicationName.setMinimumSize(new java.awt.Dimension(107, 21));

        txtPublicationArea.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtPublicationArea.setMaximumSize(new java.awt.Dimension(107, 21));
        txtPublicationArea.setMinimumSize(new java.awt.Dimension(107, 21));

        txtSourceNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtSourceNo.setMaximumSize(new java.awt.Dimension(107, 21));
        txtSourceNo.setMinimumSize(new java.awt.Dimension(107, 21));

        cboSourceType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboSourceType.setModel(setSourceTypesNamesModel());
        cboSourceType.setMaximumSize(new java.awt.Dimension(107, 21));
        cboSourceType.setMinimumSize(new java.awt.Dimension(107, 21));

        lblPublicationDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblPublicationDate.setText(bundle.getString("GeneralMetadata.lblPublicationDate.text")); // NOI18N

        lblPublicationName.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblPublicationName.setText(bundle.getString("GeneralMetadata.lblPublicationName.text")); // NOI18N

        lblPublicationArea.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblPublicationArea.setText(bundle.getString("GeneralMetadata.lblPublicationArea.text")); // NOI18N

        lblSourceNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSourceNo.setText(bundle.getString("GeneralMetadata.lblSourceNo.text")); // NOI18N

        lblSourceType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSourceType.setText(bundle.getString("GeneralMetadata.lblSourceType.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblLanguage)
                            .addComponent(lblPublicationDate)
                            .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dt_publication_date, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblSourceNo)
                                .addGap(110, 110, 110)
                                .addComponent(lblSourceType))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblPublicationName)
                                    .addComponent(txtPublicationName, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                                    .addComponent(txtSourceNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(53, 53, 53)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblPublicationArea)
                                    .addComponent(cboSourceType, 0, 130, Short.MAX_VALUE)
                                    .addComponent(txtPublicationArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(0, 98, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblLanguage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPublicationDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dt_publication_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPublicationName)
                    .addComponent(lblPublicationArea))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPublicationName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPublicationArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSourceNo)
                    .addComponent(lblSourceType))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSourceNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSourceType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(79, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cboLanguage;
    private javax.swing.JComboBox cboSourceType;
    private org.jdesktop.swingx.JXDatePicker dt_publication_date;
    private javax.swing.JLabel lblLanguage;
    private javax.swing.JLabel lblPublicationArea;
    private javax.swing.JLabel lblPublicationDate;
    private javax.swing.JLabel lblPublicationName;
    private javax.swing.JLabel lblSourceNo;
    private javax.swing.JLabel lblSourceType;
    private javax.swing.JTextField txtPublicationArea;
    private javax.swing.JTextField txtPublicationName;
    private javax.swing.JTextField txtSourceNo;
    // End of variables declaration//GEN-END:variables

    @Override
    public Component getPanelComponent() {
        return this;
    }

    @Override
    public Dimension getFrameSize() {
         int DIM_X = 450 ; int DIM_Y = 350 ;
        return new Dimension(DIM_X, DIM_Y);
    }

    public boolean applySelectedMetadata(BungeniFileSavePathFormat spf)
        {
             boolean bState = false;
        try {
            LanguageCode selLanguage = (LanguageCode) this.cboLanguage.getSelectedItem();
        
            //get the official time
    //        SimpleDateFormat tformatter = new SimpleDateFormat (BungeniEditorProperties.getEditorProperty("metadataTimeFormat"));
    //        Object timeValue = this.dt_official_time.getValue();
    //        Date hansardTime = (Date) timeValue;
    //        final String strTimeOfHansard = tformatter.format(hansardTime);
            //get the offical date
            SimpleDateFormat dformatter = new SimpleDateFormat (BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
                //get the current date
            Calendar cal = Calendar.getInstance();
            final String strCurrentDate = dformatter.format(cal.getTime());
                //get the publication date
            final String strPubDate = dformatter.format( this.dt_publication_date.getDate());
            final String strPubName = this.txtPublicationName.getText();
            final String strPubArea = this.txtPublicationArea.getText();
            final String strSrcNo = this.txtSourceNo.getText();
            String selSrcType = (String) this.cboSourceType.getSelectedItem();

            docMetaModel.updateItem("BungeniWorkDate", strPubDate);
            docMetaModel.updateItem("BungeniDocAuthor", "Ashok");
            
            // General Metadata
            docMetaModel.updateItem("BungeniLanguageCode", selLanguage.getLanguageCode2());
            docMetaModel.updateItem("BungeniPublicationDate", strPubDate);
            docMetaModel.updateItem("BungeniPublicationName", strPubName);
            docMetaModel.updateItem("BungeniPublicationArea", strPubArea);
            docMetaModel.updateItem("BungeniSourceType", selSrcType);
            docMetaModel.updateItem("BungeniSourceNo", strSrcNo);
                       
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
            spf.setSaveComponent("CountryCode", Locale.getDefault().getCountry());
            spf.setSaveComponent("LanguageCode", selLanguage.getLanguageCode2());
            Date dtHansardDate = dt_publication_date.getDate();
            GregorianCalendar debateCal = new GregorianCalendar(); 
            debateCal.setTime(dtHansardDate);
       
            spf.setSaveComponent("Year", debateCal.get(Calendar.YEAR));
            spf.setSaveComponent("Month", debateCal.get(Calendar.MONTH) + 1);
            spf.setSaveComponent("Day", debateCal.get(Calendar.DAY_OF_MONTH));
            spf.parseComponents();

            docMetaModel.updateItem("BungeniWorkURI", spf.getWorkURI());
            docMetaModel.updateItem("BungeniExpURI", spf.getExpressionURI());
            docMetaModel.updateItem("BungeniManURI", spf.getExpressionURI() + ".xml");

            //docMetaModel.updateItem("__BungeniDocMeta", "true");
            docMetaModel.saveModel(ooDocument);
            bState = true;
        } catch (Exception ex) {
            log.error("applySelectedMetadata : " + ex.getMessage());
            bState = false;
        } finally {
            return bState;
        }
        }
       
    private ComboBoxModel setSourceTypesNamesModel()
    {
         DefaultComboBoxModel sourceTypesNamesModel = null ;
        String [] sourceTypesNames = null; // stores all the bill Names

        // initialise the Bungeni Connector Client
        BungeniConnector client = null;
        
        try {
                // initialize the data store client
                 client = CommonConnectorFunctions.getDSClient();

                // get the acts from the registry H2 db
                List<SourceType> SourceTypesList = client.getSourceTypes();
                sourceTypesNames = new String[SourceTypesList.size()];

                // loop through extracting the acts
                for (int i = 0 ; i < SourceTypesList.size() ; i ++)
                {
                    // get the current act & extract the act Name
                    SourceType currSourceType = SourceTypesList.get(i);
                    sourceTypesNames[i] = currSourceType.getNameByLang(Locale.getDefault().getLanguage());
                }
                // create the default acts Names model
                sourceTypesNamesModel = new DefaultComboBoxModel(sourceTypesNames) ;
            } catch (IOException ex) {
                log.error(ex) ;
            }

        return sourceTypesNamesModel;
    }
      
      @Override
    public ArrayList<String> validateSelectedMetadata(BungeniFileSavePathFormat spf) {
         addFieldsToValidate (new TreeMap<String,Component>(){
            {
                put(lblLanguage.getText().replace("*",""), cboLanguage);
                put(lblPublicationArea.getText().replace("*",""), txtPublicationArea);
                put(lblPublicationDate.getText().replace("*",""), dt_publication_date);
                put(lblPublicationName.getText().replace("*",""), txtPublicationName);
                put(lblSourceType.getText().replace("*",""), cboSourceType);
                }
            });
        return super.validateSelectedMetadata(spf);
    }
}
