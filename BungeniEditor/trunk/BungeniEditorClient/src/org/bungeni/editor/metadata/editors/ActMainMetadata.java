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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.editor.metadata.BaseEditorDocMetadataDialog;
import org.bungeni.editor.metadata.ActMainMetadataModel;
import org.bungeni.editor.metadata.LanguageCode;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.extutils.CommonConnectorFunctions;
import org.bungeni.utils.BungeniFileSavePathFormat;
import org.jdom.xpath.XPath;
/**
 *
 * @author bzuadmin
 */
public class ActMainMetadata extends BaseEditorDocMetadataDialog {
    
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ActMainMetadata.class.getName());
    private ActMainMetadataModel docMetaModel = new ActMainMetadataModel();
    private XPath xpathInstance = null;

    
    private HashMap<String,org.jdom.Document> cachedActNames = new HashMap<String,org.jdom.Document>();


    /**
     * Creates new customizer ActMainMetadata
     */
    public ActMainMetadata() {
        super();
        initComponents();
        CommonUIFunctions.compOrientation(this);     
    }
    
      
   @Override
    public void initialize() {
        super.initialize();
        this.docMetaModel.setup();
        initControls();
        loadActInfo();        
           if (theMode == SelectorDialogModes.TEXT_EDIT) {
                //retrieve metadata... and set in controls....
                docMetaModel.loadModel(ooDocument);
           }
           
           try{
                String sLanguageCode = docMetaModel.getItem("BungeniLanguageCode");
                String sBungeniActType = docMetaModel.getItem("BungeniActType");
                String sBungeniActScope = docMetaModel.getItem("BungeniActScope");
                String sBungeniActState = docMetaModel.getItem("BungeniActState");
                String sBungeniActFamily = docMetaModel.getItem("BungeniActFamily");
                String sBungeniActHistoricalPeriod = docMetaModel.getItem("BungeniActHistoricalPeriod");
                String sEffectiveDate =docMetaModel.getItem("BungeniActEffectiveDate");
               
                
             //   SimpleDateFormat timeFormat = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
                   
                SimpleDateFormat dateFormat = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
                
                 if (!CommonStringFunctions.emptyOrNull(sLanguageCode)){
                    this.cboLanguage.setSelectedItem(findLanguageCode(sLanguageCode));
                }
                 
                if (!CommonStringFunctions.emptyOrNull(sBungeniActType))
                    this.cboActType.setSelectedItem(sBungeniActType);
                
                if (!CommonStringFunctions.emptyOrNull(sBungeniActScope))
                    this.cboActScope.setSelectedItem(sBungeniActScope);
                
                if (!CommonStringFunctions.emptyOrNull(sBungeniActHistoricalPeriod))
                    this.cboActHistoricalPeriod.setSelectedItem(sBungeniActHistoricalPeriod);
                
                if (!CommonStringFunctions.emptyOrNull(sBungeniActFamily))
                    this.cboActFamily.setSelectedItem(sBungeniActFamily);
                
               if (!CommonStringFunctions.emptyOrNull(sBungeniActState))
                    this.txtActState.setText(sBungeniActState);
               
                 if (!CommonStringFunctions.emptyOrNull(sEffectiveDate)) 
                    this.dt_effective_date.setDate(dateFormat.parse(sEffectiveDate));
                
                 
                
           }catch (Exception ex) {
            log.error("initalize()  =  " + ex.getMessage());
           }
    }

     private void initControls(){
       // String popupDlgBackColor = BungeniEditorProperties.getEditorProperty("popupDialogBackColor");
       // this.setBackground(Color.decode(popupDlgBackColor));
        cboLanguage.setModel(new DefaultComboBoxModel(languageCodes));
       
        //set Default selections
        this.cboLanguage.setSelectedItem(findLanguageCode(Locale.getDefault().getLanguage()));  
    }

    public Component getPanelComponent() {
        return this;
    }
    
        private void loadActInfo() {
            BungeniConnector client = null ;
            try {
                client = CommonConnectorFunctions.getDSClient();
                List<MetadataInfo> metadata = client.getMetadataInfo();
                if (metadata != null) {                     
                    for (int i = 0; i < metadata.size(); i++) {
                        if (metadata.get(i).getName().equalsIgnoreCase("BungeniActType")) {
                            docMetaModel.setBungeniActType(metadata.get(i).getValue());
                        } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActScope")) {
                            docMetaModel.setBungeniActScope(metadata.get(i).getValue());
                        } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActHistoricalPeriod")) {
                            docMetaModel.setBungeniActHistoricalPeriod(metadata.get(i).getValue());
                        } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActFamily")) {
                            docMetaModel.setBungeniActFamily(metadata.get(i).getValue());
                        } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActState")) {
                            docMetaModel.setBungeniActState(metadata.get(i).getValue());
                        } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActEffectiveDate")) {
                            docMetaModel.setBungeniActEffectiveDate(metadata.get(i).getValue());
                        } 
//                        else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActSourceSide")) {
//                            docMetaModel.setBungeniActSourceSide(metadata.get(i).getValue());
//                        } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActDeveloped")) {
//                            docMetaModel.setBungeniActDeveloped(metadata.get(i).getValue());
//                        }else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActDevelopedDate")) {
//                            docMetaModel.setBungeniActDevelopedDate(metadata.get(i).getValue());
//                        }else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActApproved")) {
//                            docMetaModel.setBungeniActApproved(metadata.get(i).getValue());
//                        }else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActAprovedDate")) {
//                            docMetaModel.setBungeniActAprovedDate(metadata.get(i).getValue());
//                        }else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActImplementation")) {
//                            docMetaModel.setBungeniActImplementation(metadata.get(i).getValue());
//                        }else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActImplementationDate")) {
//                            docMetaModel.setBungeniActImplementationDate(metadata.get(i).getValue());
//                        }else if (metadata.get(i).getName().equalsIgnoreCase("BungeniSecondaryActPromulgating")) {
//                            docMetaModel.setBungeniSecondaryActPromulgating(metadata.get(i).getValue());
//                        }else if (metadata.get(i).getName().equalsIgnoreCase("BungeniSecondaryActPromulgatingDate")) {
//                            docMetaModel.setBungeniSecondaryActPromulgatingDate(metadata.get(i).getValue());
//                        }
                        System.out.println(metadata.get(i).getName() + " " + metadata.get(i).getType() + " " + metadata.get(i).getValue());
                    }
                }
                client.closeConnector();
            }catch (IOException ex) {
                log.error("THe connector client could not be initialized" , ex);
            }
    }

       
       private ComboBoxModel setActTypesModel()
       {
            DefaultComboBoxModel actTypesModel = null ;
            String[] actTypes = null ; // stores all the bill Names
            // initialise the Bungeni Connector Client
            BungeniConnector client = null ;
            try {
                // initialize the data store client
                 client = CommonConnectorFunctions.getDSClient();
                // get the acts from the registry H2 db
                List<ActType> actTypesList = client.getActTypes();
                actTypes = new String[actTypesList.size()];
                
                // loop through extracting the acts
                for (int i = 0 ; i < actTypesList.size() ; i ++)
                {
                    // get the current act & extract the act Name
                    ActType currActType = actTypesList.get(i);
                    actTypes[i] = currActType.getNameByLang(Locale.getDefault().getLanguage());
                }
                
                // create the default acts Names model
                actTypesModel = new DefaultComboBoxModel(actTypes) ;

            } catch (IOException ex) {
                log.error(ex) ;
            }
            return actTypesModel ;
    }
          
       private ComboBoxModel setActScopesModel()
       {
            DefaultComboBoxModel actScopesModel = null ;
            String[] actScopes = null ; // stores all the bill Names
            // initialise the Bungeni Connector Client
            BungeniConnector client = null ;
            try {
                // initialize the data store client
                 client = CommonConnectorFunctions.getDSClient();
                // get tactScopeshe acts from the registry H2 db
                List<ActScope> actScopesList = client.getActScopes();
                actScopes = new String[actScopesList.size()];
                
                // loop through extracting the acts
                for (int i = 0 ; i < actScopesList.size() ; i ++)
                {
                    // get the current act & extract the act Name
                    ActScope currActScope = actScopesList.get(i);
                    actScopes[i] = currActScope.getNameByLang(Locale.getDefault().getLanguage());
                }
                
                // create the default acts Names model
                actScopesModel = new DefaultComboBoxModel(actScopes) ;

            } catch (IOException ex) {
                log.error(ex) ;
            }
            return actScopesModel ;
    }
          
       private ComboBoxModel setActHistoricalPeriodsModel()
       {
            DefaultComboBoxModel actHistoricalPeriodsModel = null ;
            String[] actHistoricalPeriods = null ; // stores all the bill Names
            // initialise the Bungeni Connector Client
            BungeniConnector client = null ;
            try {
                // initialize the data store client
                 client = CommonConnectorFunctions.getDSClient();
                // get the acts from the registry H2 db
                List<ActHistoricalPeriod> actHistoricalPeriodsList = client.getActHistoricalPeriods();
                actHistoricalPeriods = new String[actHistoricalPeriodsList.size()];
                
                // loop through extracting the acts
                for (int i = 0 ; i < actHistoricalPeriodsList.size() ; i ++)
                {
                    // get the current act & extract the act Name
                    ActHistoricalPeriod currActHistoricalPeriod = actHistoricalPeriodsList.get(i);
                    actHistoricalPeriods[i] = currActHistoricalPeriod.getNameByLang(Locale.getDefault().getLanguage());
                }
                
                // create the default acts Names model
                actHistoricalPeriodsModel = new DefaultComboBoxModel(actHistoricalPeriods) ;

            } catch (IOException ex) {
                log.error(ex) ;
            }
            return actHistoricalPeriodsModel ;
    }
       
        private ComboBoxModel setActFamiliesModel()
       {
            DefaultComboBoxModel actFamiliesModel = null ;
            String[] actFamilies = null ; // stores all the bill Names
            // initialise the Bungeni Connector Client
            BungeniConnector client = null ;
            try {
                // initialize the data store client
                 client = CommonConnectorFunctions.getDSClient();
                // get the acts from the registry H2 db
                List<ActFamily> actFamiliesList = client.getActFamilies();
                actFamilies = new String[actFamiliesList.size()];
                
                // loop through extracting the acts
                for (int i = 0 ; i < actFamiliesList.size() ; i ++)
                {
                    // get the current act & extract the act Name
                    ActFamily currActFamily = actFamiliesList.get(i);
                    actFamilies[i] = currActFamily.getNameByLang(Locale.getDefault().getLanguage());
                }
                
                // create the default acts Names model
                actFamiliesModel = new DefaultComboBoxModel(actFamilies) ;

            } catch (IOException ex) {
                log.error(ex) ;
            }
            return actFamiliesModel ;
    }
       
       private ComboBoxModel setActClassificationsModel()
       {
            DefaultComboBoxModel actClassificationsModel = null ;
            String[] actClassifications = null ; // stores all the bill Names
            // initialise the Bungeni Connector Client
            BungeniConnector client = null ;
            try {
                // initialize the data store client
                 client = CommonConnectorFunctions.getDSClient();
                // get the acts from the registry H2 db
                List<ActClassification> actClassificationsList = client.getActClassifications();
                actClassifications = new String[actClassificationsList.size()];
                
                // loop through extracting the acts
                for (int i = 0 ; i < actClassificationsList.size() ; i ++)
                {
                    // get the current act & extract the act Name
                    ActClassification currActClassification = actClassificationsList.get(i);
                    actClassifications[i] = currActClassification.getNameByLang(Locale.getDefault().getLanguage());
                }
                
                // create the default acts Names model
                actClassificationsModel = new DefaultComboBoxModel(actClassifications) ;

            } catch (IOException ex) {
                log.error(ex) ;
            }
            return actClassificationsModel ;
    }
         
    public boolean applySelectedMetadata(BungeniFileSavePathFormat spf){
    boolean bState = false;
    try {
        
        SimpleDateFormat dformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
        
        LanguageCode selLanguage = (LanguageCode) this.cboLanguage.getSelectedItem();
        String strEffectiveDate = dformatter.format(dt_effective_date.getDate());
           
        String selBungeniActType = (String)this.cboActType.getSelectedItem();
        String selBungeniActScope = (String)this.cboActScope.getSelectedItem();
        String selBungeniActHistoricalPeriod = (String)this.cboActHistoricalPeriod.getSelectedItem();
        String selBungeniActFamily = (String)this.cboActFamily.getSelectedItem();
        String sBungeniActState = this.txtActState.getText();
        
        docMetaModel.updateItem("BungeniLanguageCode", selLanguage.getLanguageCode2());     
        docMetaModel.updateItem("BungeniActType",selBungeniActType);
        docMetaModel.updateItem("BungeniActScope",selBungeniActScope);
        docMetaModel.updateItem("BungeniActHistoricalPeriod",selBungeniActHistoricalPeriod);
        docMetaModel.updateItem("BungeniActFamily",selBungeniActFamily);
        docMetaModel.updateItem("BungeniActState",sBungeniActState);
        docMetaModel.updateItem("BungeniActEffectiveDate", strEffectiveDate);   
        
        spf.setSaveComponent("LanguageCode", selLanguage.getLanguageCode2());
           
         
        docMetaModel.saveModel(ooDocument);
        bState = true;
    } catch (Exception ex) {
        log.error("applySelectedMetadata : " + ex.getMessage());
        bState = false;
    } finally {
        return bState;
    }
}    

     /**
     * This mehod is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cboActType = new javax.swing.JComboBox();
        lblActType = new javax.swing.JLabel();
        lblEffectiveDate = new javax.swing.JLabel();
        dt_effective_date = new org.jdesktop.swingx.JXDatePicker();
        lblActScope = new javax.swing.JLabel();
        cboActScope = new javax.swing.JComboBox();
        lblActState = new javax.swing.JLabel();
        txtActState = new javax.swing.JTextField();
        lblActHistoricalPeriod = new javax.swing.JLabel();
        cboActHistoricalPeriod = new javax.swing.JComboBox();
        lblActFamily = new javax.swing.JLabel();
        cboActFamily = new javax.swing.JComboBox();
        cboLanguage = new javax.swing.JComboBox();
        lblLanguage = new javax.swing.JLabel();
        lblActClassification = new javax.swing.JLabel();
        cboActClassification = new javax.swing.JComboBox();

        setPreferredSize(new java.awt.Dimension(500, 260));

        cboActScope.setModel(setActScopesModel());
        cboActType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboActType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboActTypeActionPerformed(evt);
            }
        });

        lblActType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        lblActType.setText(bundle.getString("ActMainMetadata.lblActType.text")); // NOI18N

        lblEffectiveDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblEffectiveDate.setText(bundle.getString("ActMainMetadata.lblEffectiveDate.text")); // NOI18N
        lblEffectiveDate.setName("lbl.BungeniLanguageID"); // NOI18N

        dt_effective_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblActScope.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActScope.setText(bundle.getString("ActMainMetadata.lblActScope.text")); // NOI18N

        cboActType.setModel(setActTypesModel());
        cboActScope.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboActScope.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboActScopeActionPerformed(evt);
            }
        });

        lblActState.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActState.setText(bundle.getString("ActMainMetadata.lblActState.text")); // NOI18N

        txtActState.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblActHistoricalPeriod.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActHistoricalPeriod.setText(bundle.getString("ActMainMetadata.lblActHistoricalPeriod.text")); // NOI18N

        cboActHistoricalPeriod.setModel(setActHistoricalPeriodsModel());
        cboActHistoricalPeriod.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboActHistoricalPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboActHistoricalPeriodActionPerformed(evt);
            }
        });

        lblActFamily.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActFamily.setText(bundle.getString("ActMainMetadata.lblActFamily.text")); // NOI18N

        cboActFamily.setModel(setActFamiliesModel());
        cboActFamily.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboActFamily.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboActFamilyActionPerformed(evt);
            }
        });

        cboLanguage.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboLanguage.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLanguage.setMaximumSize(new java.awt.Dimension(107, 21));
        cboLanguage.setMinimumSize(new java.awt.Dimension(107, 21));

        lblLanguage.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblLanguage.setText(bundle.getString("ActMainMetadata.lblLanguage.text")); // NOI18N

        lblActClassification.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActClassification.setText(bundle.getString("ActMainMetadata.lblActClassification.text")); // NOI18N

        cboActClassification.setModel(setActClassificationsModel());
        cboActClassification.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboActClassification.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboActClassificationActionPerformed(evt);
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblLanguage)
                            .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dt_effective_date, javax.swing.GroupLayout.DEFAULT_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblEffectiveDate, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblActHistoricalPeriod)
                            .addComponent(cboActHistoricalPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboActType, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblActScope)
                            .addComponent(lblActType)
                            .addComponent(cboActScope, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cboActClassification, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblActClassification)
                            .addComponent(lblActState, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtActState, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboActFamily, 0, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblActFamily))
                        .addGap(57, 57, 57))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLanguage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblActState, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(txtActState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblActScope)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboActType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblActFamily)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboActFamily, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblActHistoricalPeriod)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboActHistoricalPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblActType)
                    .addComponent(lblActClassification))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboActScope, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboActClassification, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(lblEffectiveDate, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dt_effective_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

     @Override
    public Dimension getFrameSize() {
        int DIM_X = 450 ; int DIM_Y = 500 ;
        return new Dimension(DIM_X, DIM_Y);
    }

    
    @Override
     public ArrayList<String> validateSelectedMetadata(BungeniFileSavePathFormat spf) {
         addFieldsToValidate (new TreeMap<String,Component>(){
            {
                put(lblEffectiveDate.getText().replace("*",""), dt_effective_date);
                put(lblLanguage.getText().replace("*",""), cboLanguage);
                
            }
         });
        return super.validateSelectedMetadata(spf);
     }
                                                
    private void cboActTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActTypeActionPerformed
//        JComboBox cb = (JComboBox) evt.getSource();
//
//        // get the selected item and extract id for Act
//        // from vector
//        final String selectedAct = (String) cb.getSelectedItem();
//
//        if (null != selectedAct) {
//            // get the Act & set it to the Act #
//            // textfield
//            SwingWorker setActName = new SwingWorker() {
//
//                @Override
//                protected String doInBackground() throws Exception {
//                    String ActId = null;
//
//                    BungeniConnector client = null;
//
//                    // initialize the data store client
//                    client = CommonConnectorFunctions.getDSClient();
//
//                    // get the Acts from the registry H2 db
//                    List<ActType> ActsList = client.getActTypes();
//
//                    // search for the ActNo
//                    for (ActType Act : ActsList) {
//                        for(Name name: (List<Name>)Act.getNames()){
//                            if (selectedAct.equals(name)) {
//                                ActId = Act.getId().toString();
//                                return ActId;
//                            } 
//                        }
//                       
//                    }
//
//                    return ActId;
//                }
//
//                public void done() {
//                    try {
//                        // set the Act No
//                        cboActType.setText((String) get());
//                    } catch (InterruptedException ex) {
//                        log.error(ex);
//                    } catch (ExecutionException ex) {
//                        log.error(ex);
//                    }
//                }
//            };
//
//            setActName.execute();
//        }
    }//GEN-LAST:event_cboActTypeActionPerformed

    private void cboActScopeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActScopeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboActScopeActionPerformed

    private void cboActHistoricalPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActHistoricalPeriodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboActHistoricalPeriodActionPerformed

    private void cboActFamilyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActFamilyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboActFamilyActionPerformed

    private void cboActClassificationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActClassificationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboActClassificationActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cboActClassification;
    private javax.swing.JComboBox cboActFamily;
    private javax.swing.JComboBox cboActHistoricalPeriod;
    private javax.swing.JComboBox cboActScope;
    private javax.swing.JComboBox cboActType;
    private javax.swing.JComboBox cboLanguage;
    private org.jdesktop.swingx.JXDatePicker dt_effective_date;
    private javax.swing.JLabel lblActClassification;
    private javax.swing.JLabel lblActFamily;
    private javax.swing.JLabel lblActHistoricalPeriod;
    private javax.swing.JLabel lblActScope;
    private javax.swing.JLabel lblActState;
    private javax.swing.JLabel lblActType;
    private javax.swing.JLabel lblEffectiveDate;
    private javax.swing.JLabel lblLanguage;
    private javax.swing.JTextField txtActState;
    // End of variables declaration//GEN-END:variables
}
