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

import org.bungeni.editor.connectorutils.CommonConnectorFunctions;
import org.bungeni.editor.config.BungeniEditorProperties;
import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.element.*;
import org.bungeni.editor.metadata.BaseEditorDocMetadataDialog;
import org.bungeni.editor.metadata.ActMainMetadataModel;
import org.bungeni.editor.metadata.LanguageCode;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.extutils.*;
import org.bungeni.utils.BungeniFileSavePathFormat;
/**
 *
 * @author bzuadmin
 */
public class ActMainMetadata extends BaseEditorDocMetadataDialog {
    
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ActMainMetadata.class.getName());
    private ActMainMetadataModel docMetaModel = new ActMainMetadataModel();
    private List<ActFamily> actFamiliesList;
    
    /**
     * Creates new customizer ActMainMetadata
     */
    public ActMainMetadata() {
        super();
        initComponents();
        CommonUIFunctions.compOrientation(this);     
    }
    
      
    public static void setBungeniActEffectiveDate(Date effectiveDate){
        dt_effective_date.setDate(effectiveDate);
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
                String sBungeniActName = docMetaModel.getItem("BungeniActName");
                String sBungeniActNo = docMetaModel.getItem("BungeniActNo");
                String sBungeniActYear = docMetaModel.getItem("BungeniActYear");
                
                String sBungeniActScope = docMetaModel.getItem("BungeniActScope");
                String sBungeniActState = docMetaModel.getItem("BungeniActState");
                String sBungeniActFamily = docMetaModel.getItem("BungeniActFamily");
                String sBungeniActHistoricalPeriod = docMetaModel.getItem("BungeniActHistoricalPeriod");
                
                String sEffectiveDate = docMetaModel.getItem("BungeniActEffectiveDate");    
                String sPageNo = docMetaModel.getItem("BungeniPageNo");
                String sPageCount = docMetaModel.getItem("BungeniPageCount");
                
                SimpleDateFormat dateFormat = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
                
                if (!CommonStringFunctions.emptyOrNull(sLanguageCode)){
                    this.cboLanguage.setSelectedItem(findLanguageCodeAlpha2(sLanguageCode));
                }
                 
                if (!CommonStringFunctions.emptyOrNull(sBungeniActType))
                    this.cboActType.setSelectedItem(sBungeniActType);
                
                if (!CommonStringFunctions.emptyOrNull(sBungeniActName))
                    this.txtActName.setText(sBungeniActName);
                
                if (!CommonStringFunctions.emptyOrNull(sBungeniActNo))
                    this.txtActNo.setText(sBungeniActNo);
                
                if (!CommonStringFunctions.emptyOrNull(sBungeniActYear))
                    this.txtActYear.setText(sBungeniActYear);
                
                if (!CommonStringFunctions.emptyOrNull(sBungeniActScope))
                    this.cboActScope.setSelectedItem(sBungeniActScope);
                
                if (!CommonStringFunctions.emptyOrNull(sBungeniActHistoricalPeriod))
                    this.cboActHistoricalPeriod.setSelectedItem(sBungeniActHistoricalPeriod);
                
                if (!CommonStringFunctions.emptyOrNull(sBungeniActFamily))
                    this.cboActFamily.setSelectedItem(sBungeniActFamily);
                
               if (!CommonStringFunctions.emptyOrNull(sBungeniActState))
                    this.txtActState.setText(sBungeniActState);
               
               if (!CommonStringFunctions.emptyOrNull(sEffectiveDate)) 
                    setBungeniActEffectiveDate(dateFormat.parse(sEffectiveDate));
              
               if (!CommonStringFunctions.emptyOrNull(sPageNo))
                    this.txtPageNo.setText(sPageNo);
              
               if (!CommonStringFunctions.emptyOrNull(sPageCount))
                    this.txtPageCount.setText(sPageCount);
                
           }catch (Exception ex) {
            log.error("initalize()  =  " + ex.getMessage());
           }
    }

     private void initControls(){
       // String popupDlgBackColor = BungeniEditorProperties.getEditorProperty("popupDialogBackColor");
       // this.setBackground(Color.decode(popupDlgBackColor));
        cboLanguage.setModel(new DefaultComboBoxModel(languageCodes));
       
        //set Default selections
        this.cboLanguage.setSelectedItem(findLanguageCodeAlpha2(Locale.getDefault().getLanguage()));
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
                        } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActName")) {
                            docMetaModel.setBungeniActName(metadata.get(i).getValue());
                        } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActNo")) {
                            docMetaModel.setBungeniActNo(metadata.get(i).getValue());
                        } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActYear")) {
                            docMetaModel.setBungeniActYear(metadata.get(i).getValue());
                        } 
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
                actFamiliesList = client.getActFamilies();
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
        
       private ComboBoxModel setActSubFamiliesModel(Integer selectedActFamilyIndex)
       {
            DefaultComboBoxModel subFamiliesModel = null ;
            String[] subFamilies = null ; // stores all the bill Names
                   
            ActFamily currActFamily =  actFamiliesList.get(selectedActFamilyIndex);

            List<SubFamily> subFamiliesList = currActFamily.getSubFamiliesByLang(Locale.getDefault().getLanguage());
            subFamilies = new String[subFamiliesList.size()];

             for (int i = 0 ; i < subFamiliesList.size() ; i ++)
                  subFamilies[i] = subFamiliesList.get(i).getValue();
                
             
            // create the default families model
            subFamiliesModel = new DefaultComboBoxModel(subFamilies);

            
            cboActSubFamily.setModel(subFamiliesModel);
            return subFamiliesModel ;
    }
       
        private ComboBoxModel setActPossibleSubFamiliesModel(Integer selectedActFamilyIndex)
       {
            DefaultComboBoxModel subPossibleFamiliesModel = null ;
            String[] subPossibleFamilies = null ; // stores all the bill Names
                   
            ActFamily currActFamily =  actFamiliesList.get(selectedActFamilyIndex);

            List<SubFamily> subPossibleFamiliesList = currActFamily.getSubFamiliesByLang(Locale.getDefault().getLanguage());
            subPossibleFamilies = new String[subPossibleFamiliesList.size()];

             for (int i = 0 ; i < subPossibleFamiliesList.size() ; i ++)
                  subPossibleFamilies[i] = subPossibleFamiliesList.get(i).getValue();
                
             
            // create the default families model
            subPossibleFamiliesModel = new DefaultComboBoxModel(subPossibleFamilies);

            
            cboActPossibleSubFamily.setModel(subPossibleFamiliesModel);
            return subPossibleFamiliesModel ;
    }
       
        
               
       private ComboBoxModel setActCategoriesModel()
       {
            DefaultComboBoxModel actCategoriesModel = null ;
            String[] actCategories = null ; // stores all the bill Names
            // initialise the Bungeni Connector Client
            BungeniConnector client = null ;
            try {
                // initialize the data store client
                 client = CommonConnectorFunctions.getDSClient();
                // get the acts from the registry H2 db
                List<ActCategory> actCategoriesList = client.getActCategories();
                actCategories = new String[actCategoriesList.size()];
                
                // loop through extracting the acts
                for (int i = 0 ; i < actCategoriesList.size() ; i ++)
                {
                    // get the current act & extract the act Name
                    ActCategory currActCategory = actCategoriesList.get(i);
                    actCategories[i] = currActCategory.getNameByLang(Locale.getDefault().getLanguage());
                }
                
                // create the default acts Names model
                actCategoriesModel = new DefaultComboBoxModel(actCategories) ;

            } catch (IOException ex) {
                log.error(ex) ;
            }
            return actCategoriesModel ;
    }
       
        private ComboBoxModel setActCategoriesBasicModel()
       {
            DefaultComboBoxModel actCategoriesBasicModel = null ;
            String[] actCategoriesBasic = null ; // stores all the bill Names
            // initialise the Bungeni Connector Client
            BungeniConnector client = null ;
            try {
                // initialize the data store client
                 client = CommonConnectorFunctions.getDSClient();
                // get the acts from the registry H2 db
                List<ActCategoryBasic> actCategoriesBasicList = client.getActCategoriesBasic();
                actCategoriesBasic = new String[actCategoriesBasicList.size()];
                
                // loop through extracting the acts
                for (int i = 0 ; i < actCategoriesBasicList.size() ; i ++)
                {
                    // get the current act & extract the act Name
                    ActCategoryBasic currActCategoryBasic = actCategoriesBasicList.get(i);
                    actCategoriesBasic[i] = currActCategoryBasic.getNameByLang(Locale.getDefault().getLanguage());
                }
                
                // create the default acts Names model
                actCategoriesBasicModel = new DefaultComboBoxModel(actCategoriesBasic) ;

            } catch (IOException ex) {
                log.error(ex) ;
            }
            return actCategoriesBasicModel ;
    }
         
    public boolean applySelectedMetadata(BungeniFileSavePathFormat spf){
    boolean bState = false;
    try {
        
        SimpleDateFormat dformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
        
        LanguageCode selLanguage = (LanguageCode) this.cboLanguage.getSelectedItem();
        String strEffectiveDate = dformatter.format(dt_effective_date.getDate());
         
        String sBungeniActName = this.txtActName.getText();
        String sBungeniActNo = this.txtActNo.getText();
        String sBungeniActYear = this.txtActYear.getText();
        String selBungeniActType = (String)this.cboActType.getSelectedItem();
        String selBungeniActScope = (String)this.cboActScope.getSelectedItem();
        String selBungeniActHistoricalPeriod = (String)this.cboActHistoricalPeriod.getSelectedItem();
        String selBungeniActFamily = (String)this.cboActFamily.getSelectedItem();
        String selBungeniActCategory = (String)this.cboActCategory.getSelectedItem() + " - " + (String)this.cboActCategoryBasic.getSelectedItem();
        String sBungeniActState = this.txtActState.getText();
        String strPageNo = this.txtPageNo.getText();
        String strPageCount = this.txtPageCount.getText();
           
        docMetaModel.updateItem("BungeniLanguageCode", selLanguage.getLanguageCodeAlpha3());
        docMetaModel.updateItem("BungeniActName",sBungeniActName);
        docMetaModel.updateItem("BungeniActNo",sBungeniActNo);
        docMetaModel.updateItem("BungeniActYear",sBungeniActYear);
        docMetaModel.updateItem("BungeniActType",selBungeniActType);
        docMetaModel.updateItem("BungeniActScope",selBungeniActScope);
        docMetaModel.updateItem("BungeniActHistoricalPeriod",selBungeniActHistoricalPeriod);
        docMetaModel.updateItem("BungeniActFamily",selBungeniActFamily);
        docMetaModel.updateItem("BungeniActState",sBungeniActState);
        docMetaModel.updateItem("BungeniActEffectiveDate", strEffectiveDate);   
        docMetaModel.updateItem("BungeniActCategory",selBungeniActCategory);
        docMetaModel.updateItem("BungeniPageNo", strPageNo);
        docMetaModel.updateItem("BungeniPageCount", strPageCount);
            
        spf.setSaveComponent("LanguageCode", selLanguage.getLanguageCodeAlpha3());
           
         
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
        lblActCategory = new javax.swing.JLabel();
        cboActCategoryBasic = new javax.swing.JComboBox();
        cboActPossibleFamily = new javax.swing.JComboBox();
        lblActPossibleFamily = new javax.swing.JLabel();
        cboActPossibleSubFamily = new javax.swing.JComboBox();
        cboActSubFamily = new javax.swing.JComboBox();
        cboActCategory = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        lblPageNo = new javax.swing.JLabel();
        txtPageNo = new javax.swing.JTextField();
        txtPageCount = new javax.swing.JTextField();
        lblPageCount = new javax.swing.JLabel();
        btn30days = new javax.swing.JButton();
        btn90days = new javax.swing.JButton();
        lblActName = new javax.swing.JLabel();
        txtActName = new javax.swing.JTextField();
        lblActNo = new javax.swing.JLabel();
        txtActNo = new javax.swing.JTextField();
        lblActYear = new javax.swing.JLabel();
        txtActYear = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(500, 550));

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

        lblActCategory.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActCategory.setText(bundle.getString("ActMainMetadata.lblActCategory.text")); // NOI18N

        cboActCategoryBasic.setModel(setActCategoriesBasicModel());
        cboActCategoryBasic.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboActCategoryBasic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboActCategoryBasicActionPerformed(evt);
            }
        });

        cboActPossibleFamily.setModel(setActFamiliesModel());
        cboActPossibleFamily.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboActPossibleFamily.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboActPossibleFamilyActionPerformed(evt);
            }
        });

        lblActPossibleFamily.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActPossibleFamily.setText(bundle.getString("ActMainMetadata.lblActPossibleFamily.text")); // NOI18N

        cboActPossibleSubFamily.setModel(setActPossibleSubFamiliesModel(cboActPossibleFamily.getSelectedIndex()));
        cboActPossibleSubFamily.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboActPossibleSubFamily.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboActPossibleSubFamilyActionPerformed(evt);
            }
        });

        cboActSubFamily.setModel(setActSubFamiliesModel(cboActFamily.getSelectedIndex()));
        cboActSubFamily.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboActSubFamily.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboActSubFamilyActionPerformed(evt);
            }
        });

        cboActCategory.setModel(setActCategoriesModel());
        cboActCategory.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboActCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboActCategoryActionPerformed(evt);
            }
        });

        lblPageNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblPageNo.setText(bundle.getString("ActMainMetadata.lblPageNo.text")); // NOI18N

        txtPageNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtPageNo.setMaximumSize(new java.awt.Dimension(107, 21));
        txtPageNo.setMinimumSize(new java.awt.Dimension(107, 21));

        txtPageCount.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtPageCount.setMaximumSize(new java.awt.Dimension(107, 21));
        txtPageCount.setMinimumSize(new java.awt.Dimension(107, 21));

        lblPageCount.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblPageCount.setText(bundle.getString("ActMainMetadata.lblPageCount.text")); // NOI18N

        btn30days.setFont(new java.awt.Font("DejaVu Sans", 0, 8)); // NOI18N
        btn30days.setText(bundle.getString("ActMainMetadata.30days.text")); // NOI18N
        btn30days.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn30daysActionPerformed(evt);
            }
        });

        btn90days.setFont(new java.awt.Font("DejaVu Sans", 0, 8)); // NOI18N
        btn90days.setText(bundle.getString("ActMainMetadata.90days.text")); // NOI18N
        btn90days.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn90daysActionPerformed(evt);
            }
        });

        lblActName.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActName.setText(bundle.getString("ActMainMetadata.lblActName.text")); // NOI18N

        txtActName.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblActNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActNo.setText(bundle.getString("ActMainMetadata.lblActNo.text")); // NOI18N

        txtActNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblActYear.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActYear.setText(bundle.getString("ActMainMetadata.lblActYear.text")); // NOI18N

        txtActYear.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblActName, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboActCategoryBasic, 0, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboActPossibleFamily, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboActFamily, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboActSubFamily, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboActPossibleSubFamily, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboActCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblActYear, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtActYear, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblActState, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtActState, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboActHistoricalPeriod, 0, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblActHistoricalPeriod)
                                    .addComponent(dt_effective_date, javax.swing.GroupLayout.DEFAULT_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblEffectiveDate, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btn30days, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btn90days, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(txtActName, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 97, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblActNo, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtActNo, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblActType)
                            .addComponent(lblPageNo)
                            .addComponent(txtPageNo, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPageCount)
                            .addComponent(cboActType, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboActScope, 0, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblActScope)
                            .addComponent(txtPageCount, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblLanguage)
                            .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblActFamily)
                            .addComponent(lblActCategory)
                            .addComponent(lblActPossibleFamily))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLanguage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(lblActName, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtActName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblActNo, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtActNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(lblActType)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboActScope, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblActScope)
                        .addGap(2, 2, 2)
                        .addComponent(cboActType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblPageNo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPageNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPageCount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPageCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblActYear, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtActYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(lblActState, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtActState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblActHistoricalPeriod)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboActHistoricalPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(lblEffectiveDate, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_effective_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn30days)
                            .addComponent(btn90days))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblActFamily)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cboActFamily, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblActPossibleFamily)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboActPossibleFamily, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblActCategory)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboActCategoryBasic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cboActSubFamily, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(cboActPossibleSubFamily, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(cboActCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

     @Override
    public Dimension getFrameSize() {
        int DIM_X = 450 ; int DIM_Y = 600 ;
        return new Dimension(DIM_X, DIM_Y);
    }

    
    @Override
     public ArrayList<String> validateSelectedMetadata(BungeniFileSavePathFormat spf) {
         addFieldsToValidate (new TreeMap<String,Component>(){
            {
                put(lblLanguage.getText().replace("*",""), cboLanguage);
                put(lblActName.getText().replace("*",""), txtActName);
                put(lblActNo.getText().replace("*",""), txtActNo);
                put(lblActYear.getText().replace("*",""), txtActYear);
                put(lblActScope.getText().replace("*",""), cboActScope);
                put(lblActType.getText().replace("*",""), cboActType);
                put(lblActHistoricalPeriod.getText().replace("*",""), cboActHistoricalPeriod);
                put(lblEffectiveDate.getText().replace("*",""), dt_effective_date);
                put(lblActFamily.getText().replace("*",""), cboActFamily);
                put(lblActFamily.getText().replace("*",""), cboActSubFamily);
                put(lblActPossibleFamily.getText().replace("*",""), cboActPossibleFamily);
                put(lblActPossibleFamily.getText().replace("*",""), cboActPossibleSubFamily);
                put(lblActCategory.getText().replace("*",""), cboActCategoryBasic);
                put(lblActCategory.getText().replace("*",""), cboActCategory);
                
            }
         });
        return super.validateSelectedMetadata(spf);
     }
                                                
    private void cboActTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActTypeActionPerformed

    }//GEN-LAST:event_cboActTypeActionPerformed

    private void cboActScopeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActScopeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboActScopeActionPerformed

    private void cboActHistoricalPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActHistoricalPeriodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboActHistoricalPeriodActionPerformed

    private void cboActFamilyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActFamilyActionPerformed
        JComboBox cb = (JComboBox) evt.getSource();

        // get the selected item and extract id for Act
        // from vector
//        final String selectedActFamily = (String) cb.getSelectedItem();
        final Integer  selectedActFamilyIndex = cb.getSelectedIndex();
        setActSubFamiliesModel(selectedActFamilyIndex);
    }//GEN-LAST:event_cboActFamilyActionPerformed

    private void cboActCategoryBasicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActCategoryBasicActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboActCategoryBasicActionPerformed

    private void cboActPossibleFamilyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActPossibleFamilyActionPerformed
         JComboBox cb = (JComboBox) evt.getSource();

        // get the selected item and extract id for Act
        // from vector
        final Integer  selectedActPossibleFamilyIndex = cb.getSelectedIndex();
        setActPossibleSubFamiliesModel(selectedActPossibleFamilyIndex);
        
    }//GEN-LAST:event_cboActPossibleFamilyActionPerformed

    private void cboActPossibleSubFamilyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActPossibleSubFamilyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboActPossibleSubFamilyActionPerformed

    private void cboActSubFamilyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActSubFamilyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboActSubFamilyActionPerformed

    private void cboActCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboActCategoryActionPerformed

    private void btn30daysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn30daysActionPerformed
        Date effectiveDate = dt_effective_date.getDate();
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(effectiveDate);
        cal.add(Calendar.DATE, 30);
        
        dt_effective_date.setDate(cal.getTime());
    }//GEN-LAST:event_btn30daysActionPerformed

    private void btn90daysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn90daysActionPerformed
         Date effectiveDate = dt_effective_date.getDate();
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(effectiveDate);
        cal.add(Calendar.DATE, 90);
        
        dt_effective_date.setDate(cal.getTime());
    }//GEN-LAST:event_btn90daysActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn30days;
    private javax.swing.JButton btn90days;
    private javax.swing.JComboBox cboActCategory;
    private javax.swing.JComboBox cboActCategoryBasic;
    private javax.swing.JComboBox cboActFamily;
    private javax.swing.JComboBox cboActHistoricalPeriod;
    private javax.swing.JComboBox cboActPossibleFamily;
    private javax.swing.JComboBox cboActPossibleSubFamily;
    private javax.swing.JComboBox cboActScope;
    private javax.swing.JComboBox cboActSubFamily;
    private javax.swing.JComboBox cboActType;
    private javax.swing.JComboBox cboLanguage;
    private static org.jdesktop.swingx.JXDatePicker dt_effective_date;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblActCategory;
    private javax.swing.JLabel lblActFamily;
    private javax.swing.JLabel lblActHistoricalPeriod;
    private javax.swing.JLabel lblActName;
    private javax.swing.JLabel lblActNo;
    private javax.swing.JLabel lblActPossibleFamily;
    private javax.swing.JLabel lblActScope;
    private javax.swing.JLabel lblActState;
    private javax.swing.JLabel lblActType;
    private javax.swing.JLabel lblActYear;
    private javax.swing.JLabel lblEffectiveDate;
    private javax.swing.JLabel lblLanguage;
    private javax.swing.JLabel lblPageCount;
    private javax.swing.JLabel lblPageNo;
    private javax.swing.JTextField txtActName;
    private javax.swing.JTextField txtActNo;
    private javax.swing.JTextField txtActState;
    private javax.swing.JTextField txtActYear;
    private javax.swing.JTextField txtPageCount;
    private javax.swing.JTextField txtPageNo;
    // End of variables declaration//GEN-END:variables
}
