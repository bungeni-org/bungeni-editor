/*
 * DebateRecordMetadata.java
 *
 * Created on November 4, 2008, 1:43 PM
 */
package org.birzeit.editor.metadata.editors;

import org.bungeni.editor.metadata.editors.*;
import org.bungeni.editor.connectorutils.CommonConnectorFunctions;
import org.bungeni.editor.config.BungeniEditorProperties;
import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingWorker;
import org.birzeit.editor.metadata.CaseType;
import org.birzeit.editor.metadata.City;
import org.birzeit.editor.metadata.Domains;
import org.birzeit.editor.metadata.Importance;
import org.birzeit.editor.metadata.JudgementRegion;
import org.birzeit.editor.metadata.LitigationType;
import org.birzeit.editor.metadata.editors.ActMainMetadata;
import org.birzeit.editor.metadata.editors.ActSource;
import org.birzeit.editor.metadata.editors.ConnectorFunctions;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.element.MetadataInfo;
import org.bungeni.editor.config.BungeniEditorPropertiesHelper;
import org.bungeni.editor.metadata.BaseEditorDocMetadataDialog;
import org.birzeit.editor.metadata.JudgementMetadataModel;
import org.bungeni.editor.metadata.LanguageCode;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.extutils.*;
import org.bungeni.utils.BungeniFileSavePathFormat;

/**
 *
 * @author undesa
 */
public class CourtJudgementMetadata extends BaseEditorDocMetadataDialog {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CourtJudgementMetadata.class.getName());
    JudgementMetadataModel docMetaModel = new JudgementMetadataModel();
    private SimpleDateFormat dtformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
    private SimpleDateFormat savedtformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataSaveDateFormat"));
    private String dbName = "CourtJudgments2007_test";
    private ArrayList<CaseType> CaseTypesList = new ArrayList<CaseType>();
    private ArrayList<LitigationType> LitigationTypesList = new ArrayList<LitigationType>();
    private ArrayList<Domains> DomainsList = new ArrayList<Domains>();
    private ArrayList<JudgementRegion> JudgementRegionsList = new ArrayList<JudgementRegion>();
    private ArrayList<City> CitiesList = new ArrayList<City>();
    private ArrayList<Importance> ImportanceList = new ArrayList<Importance>();
    private Connection con = ConnectorFunctions.ConnectMMSM(dbName);
    private Statement conStmt;

    public CourtJudgementMetadata() {
        super();
          try {
            conStmt = con.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(ActSource.class.getName()).log(Level.SEVERE, null, ex);
        }
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

        try {

            String sLanguageCode = docMetaModel.getItem("BungeniLanguageCode");
            String sFamily = docMetaModel.getItem("BungeniFamily");
            String sDomainType = docMetaModel.getItem("BungeniDomain");
            String sCaseType = docMetaModel.getItem("BungeniCaseType");
            String sCity = docMetaModel.getItem("BungeniCity");
            String sCaseNo = docMetaModel.getItem("BungeniCaseNo");
            String sDate = docMetaModel.getItem("BungeniDate");
            String sYear = docMetaModel.getItem("BungeniYear");

            if (!CommonStringFunctions.emptyOrNull(sLanguageCode)) {
                this.cboLanguage.setSelectedItem(findLanguageCodeAlpha2(sLanguageCode));
            }
            if (!CommonStringFunctions.emptyOrNull(sFamily)) {
                this.cboCourtType.setSelectedItem(sFamily);
            }
            if (!CommonStringFunctions.emptyOrNull(sDomainType)) {
                this.cboDomain.setSelectedItem(sDomainType);
            }
            if (!CommonStringFunctions.emptyOrNull(sCaseType)) {
                this.cboCaseType.setSelectedItem(sCaseType);
            }
            if (!CommonStringFunctions.emptyOrNull(sCity)) {
                this.cboCities.setSelectedItem(sCity);
            }
            if (!CommonStringFunctions.emptyOrNull(sCaseNo)) {
                this.txtCaseNumber.setText(sCaseNo);
            }
            if (!CommonStringFunctions.emptyOrNull(sDate)) {
                this.dt_official_date.setDate(dtformatter.parse(sDate));
            }
            if (!CommonStringFunctions.emptyOrNull(sYear)) {
                this.txtYear.setText(sYear);
            }
        } catch (Exception ex) {
            log.error("initalize()  =  " + ex.getMessage());
        }
    }

    private void loadActInfo() {
        BungeniConnector client = null;
        try {
            client = CommonConnectorFunctions.getDSClient();
            List<MetadataInfo> metadata = client.getMetadataInfo();
            if (metadata != null) {
                for (int i = 0; i < metadata.size(); i++) {

                    if (metadata.get(i).getName().equalsIgnoreCase("BungeniLanguageCode")) {
                        docMetaModel.setBungeniLanguageCode(metadata.get(i).getValue());
                    }
                    if (metadata.get(i).getName().equalsIgnoreCase("BungeniRegion")) {
                        docMetaModel.setBungeniRegion(metadata.get(i).getValue());
                    }
                    if (metadata.get(i).getName().equalsIgnoreCase("BungeniDomain")) {
                        docMetaModel.setBungeniDomain(metadata.get(i).getValue());
                    }
                    if (metadata.get(i).getName().equalsIgnoreCase("BungeniCaseType")) {
                        docMetaModel.setBungeniCaseType(metadata.get(i).getValue());
                    }
                    if (metadata.get(i).getName().equalsIgnoreCase("BungeniCity")) {
                        docMetaModel.setBungeniCity(metadata.get(i).getValue());
                    }
                    if (metadata.get(i).getName().equalsIgnoreCase("BungeniCaseNo")) {
                        docMetaModel.setBungeniCaseNo(metadata.get(i).getValue());
                    }
                    if (metadata.get(i).getName().equalsIgnoreCase("BungeniDate")) {
                        docMetaModel.setBungeniDate(metadata.get(i).getValue());
                    }
                    if (metadata.get(i).getName().equalsIgnoreCase("BungeniYear")) {
                        docMetaModel.setBungeniYear(metadata.get(i).getValue());
                    }

                    System.out.println(metadata.get(i).getName() + " " + metadata.get(i).getType() + " " + metadata.get(i).getValue());
                }
            }
            client.closeConnector();
        } catch (IOException ex) {
            log.error("THe connector client could not be initialized", ex);
        }
    }

    public Component getPanelComponent() {
        return this;
    }

    private void initControls() {
        cboLanguage.setModel(new DefaultComboBoxModel(languageCodes));
        this.cboLanguage.setSelectedItem(findLanguageCodeAlpha2(Locale.getDefault().getLanguage()));

    }

    /**
     * (rm, feb 2012) - this method obtains the bill names and updates the
     * billNo text field with the relevant #
     *
     * @return
     */
    private ComboBoxModel setCaseTypesModel() {

        DefaultComboBoxModel judgementCaseTypesModel = null;

        try {
            String sqlStm = "SELECT [CJ_CaseTypes_ID], [CJ_CaseTypes_Name], [CJ_CaseTypes_Name_E] FROM [CJ_CaseTypes] WHERE [CJ_CaseTypes_IsAlternateType] != 1 ";
            ResultSet rs = conStmt.executeQuery(sqlStm);

            while (rs.next()) {
                CaseType ctObj = new CaseType(rs.getString(1), rs.getString(2), rs.getString(3));
                CaseTypesList.add(ctObj);

            }
        } catch (SQLException ex) {
            Logger.getLogger(ActMainMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] judgementCaseTypes = new String[CaseTypesList.size()];
        for (int i = 0; i < CaseTypesList.size(); i++) {
            judgementCaseTypes[i] = CaseTypesList.get(i).toString();
        }
        // create the default acts Names mode
        judgementCaseTypesModel = new DefaultComboBoxModel(judgementCaseTypes);

//        DefaultComboBoxModel judgementCaseTypesModel = null;
//        String[] judgementCaseTypes = null; // stores all the bill Names
//
//        // initialise the Bungeni Connector Client
//        BungeniConnector client = null;
//        try {
//            // initialize the data store client
//            client = CommonConnectorFunctions.getDSClient();
//
//            // get the bills from the registry H2 db
//            List<JudgementCase> judgementCasesList = client.getJudgementCases();
//            judgementCaseTypes = new String[judgementCasesList.size()];
//
//            // loop through extracting the bills
//            for (int i = 0; i < judgementCasesList.size(); i++) {
//                // get the current bill & extract the bill Name
//                JudgementCase currJudgementCase = judgementCasesList.get(i);
//                judgementCaseTypes[i] = currJudgementCase.getNameByLang(Locale.getDefault().getLanguage());
//            }
//
//            // create the default bills Names model
//            judgementCaseTypesModel = new DefaultComboBoxModel(judgementCaseTypes);
//
//        } catch (IOException ex) {
//            log.error(ex);
//        }

        return judgementCaseTypesModel;
    }

    private ComboBoxModel setImportanceModel() {
        DefaultComboBoxModel ImportanceModel = null;

        try {
            String sqlStm = "SELECT [CJ_Importance_ID], [CJ_Importance_Name] FROM [CJ_Importance] ";
            ResultSet rs = conStmt.executeQuery(sqlStm);

            while (rs.next()) {
                Importance impObj = new Importance(rs.getString(1), rs.getString(2));
                ImportanceList.add(impObj);

            }
        } catch (SQLException ex) {
            Logger.getLogger(ActMainMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] importance = new String[ImportanceList.size()];
        for (int i = 0; i < ImportanceList.size(); i++) {
            importance[i] = ImportanceList.get(i).toString();
        }

        ImportanceModel = new DefaultComboBoxModel(importance);

        return ImportanceModel;
    }

    private ComboBoxModel setDomainsModel() {
        DefaultComboBoxModel judgementDomainsModel = null;

        try {
            String sqlStm = "SELECT [CJ_Domains_ID], [CJ_Domains_Name], [CJ_Domains_Name_E] FROM CJ_Domains WHERE [CJ_Domains_Name] != ''";
            ResultSet rs = conStmt.executeQuery(sqlStm);

            while (rs.next()) {
                Domains dObj = new Domains(rs.getString(1), rs.getString(2), rs.getString(3));
                DomainsList.add(dObj);

            }
        } catch (SQLException ex) {
            Logger.getLogger(ActMainMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] judgementDomains = new String[DomainsList.size()];
        for (int i = 0; i < DomainsList.size(); i++) {
            judgementDomains[i] = DomainsList.get(i).toString();
        }
        // create the default acts Names mode
        judgementDomainsModel = new DefaultComboBoxModel(judgementDomains);

        return judgementDomainsModel;
//        DefaultComboBoxModel judgementDomainsNamesModel = null;
//        String[] judgementDomainsNames = null; // stores all the bill Names
//
//        // initialise the Bungeni Connector Client
//        BungeniConnector client = null;
//        try {
//            // initialize the data store client
//            client = CommonConnectorFunctions.getDSClient();
//
//            // get the bills from the registry H2 db
//            List<JudgementDomain> judgementDomainsList = client.getJudgementDomains();
//            judgementDomainsNames = new String[judgementDomainsList.size()];
//
//            // loop through extracting the bills
//            for (int i = 0; i < judgementDomainsList.size(); i++) {
//                // get the current bill & extract the bill Name
//                JudgementDomain currJudgementDomain = judgementDomainsList.get(i);
//                judgementDomainsNames[i] = currJudgementDomain.getNameByLang(Locale.getDefault().getLanguage());
//            }
//
//            // create the default bills Names model
//            judgementDomainsNamesModel = new DefaultComboBoxModel(judgementDomainsNames);
//
//        } catch (IOException ex) {
//            log.error(ex);
//        }
//
//        return judgementDomainsNamesModel;
    }

    private ComboBoxModel setLitigationTypesModel() {

        DefaultComboBoxModel judgementLitigationTypesModel = null;

        try {
            String sqlStm = "SELECT DISTINCT [CJ_CaseTypes_NameAlternate], [CJ_CaseTypes_NameAlternate_E] FROM [CJ_CaseTypes]";
            ResultSet rs = conStmt.executeQuery(sqlStm);

            while (rs.next()) {
                LitigationType lgObj = new LitigationType(rs.getString(1), rs.getString(2));
                LitigationTypesList.add(lgObj);

            }
        } catch (SQLException ex) {
            Logger.getLogger(ActMainMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] judgementLitigationTypes = new String[LitigationTypesList.size()];
        for (int i = 0; i < LitigationTypesList.size(); i++) {
            judgementLitigationTypes[i] = LitigationTypesList.get(i).toString();
        }
        // create the default acts Names mode
        judgementLitigationTypesModel = new DefaultComboBoxModel(judgementLitigationTypes);

//        DefaultComboBoxModel judgementCaseTypesModel = null;
//        String[] judgementCaseTypes = null; // stores all the bill Names
//
//        // initialise the Bungeni Connector Client
//        BungeniConnector client = null;
//        try {
//            // initialize the data store client
//            client = CommonConnectorFunctions.getDSClient();
//
//            // get the bills from the registry H2 db
//            List<JudgementCase> judgementCasesList = client.getJudgementCases();
//            judgementCaseTypes = new String[judgementCasesList.size()];
//
//            // loop through extracting the bills
//            for (int i = 0; i < judgementCasesList.size(); i++) {
//                // get the current bill & extract the bill Name
//                JudgementCase currJudgementCase = judgementCasesList.get(i);
//                judgementCaseTypes[i] = currJudgementCase.getNameByLang(Locale.getDefault().getLanguage());
//            }
//
//            // create the default bills Names model
//            judgementCaseTypesModel = new DefaultComboBoxModel(judgementCaseTypes);
//
//        } catch (IOException ex) {
//            log.error(ex);
//        }

        return judgementLitigationTypesModel;
    }

    private ComboBoxModel setRegionsModel() {
        DefaultComboBoxModel judgmentRegionsModel = null;

        try {
            String sqlStm = "SELECT [CJ_Regions_ID], [CJ_Regions_Name], [CJ_Regions_Name_E] FROM [CJ_Regions] WHERE [CJ_Regions_Name] != '' ";
            ResultSet rs = conStmt.executeQuery(sqlStm);

            while (rs.next()) {
                JudgementRegion regObj = new JudgementRegion(rs.getString(1), rs.getString(2), rs.getString(3));
                JudgementRegionsList.add(regObj);

            }
        } catch (SQLException ex) {
            Logger.getLogger(ActMainMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] judgementRegions = new String[JudgementRegionsList.size()];
        for (int i = 0; i < JudgementRegionsList.size(); i++) {
            judgementRegions[i] = JudgementRegionsList.get(i).toString();
        }
        // create the default acts Names mode
        judgmentRegionsModel = new DefaultComboBoxModel(judgementRegions);

        return judgmentRegionsModel;

//        DefaultComboBoxModel familyNamesModel = null;
//        String[] familyNames = null; // stores all the bill Names
//        
//        // initialise the Bungeni Connector Client
//         BungeniConnector client = null ;
//        try {
//            // initialize the data store client
//            client = CommonConnectorFunctions.getDSClient();
//
//            // get the bills from the registry H2 db
//            List<Family> familyList = client.getFamiles();
//            familyNames = new String[familyList.size()] ;
//            
//            // loop through extracting the bills
//            for (int i = 0 ; i < familyList.size() ; i ++)
//            {
//                Family fam = familyList.get(i) ;
//                familyNames[i] = fam.getNameByLang(Locale.getDefault().getLanguage());
//               
//            }
//
//            // create the default bills Names model
//            familyNamesModel = new DefaultComboBoxModel(familyNames) ;
//            
//        } catch (IOException ex) {
//            log.error(ex) ;
//        }

//        return familyNamesModel;
    }

    private ComboBoxModel setCitiesModel() {
        DefaultComboBoxModel CitiesModel = null;

        try {
            String sqlStm = "SELECT CJ_Cities_ID, CJ_Cities_Name, CJ_Cities_Name_E FROM CJ_Cities WHERE [CJ_Cities_Name] != '' ";
            ResultSet rs = conStmt.executeQuery(sqlStm);

            while (rs.next()) {
                City cityObj = new City(rs.getString(1), rs.getString(2), rs.getString(3));
                CitiesList.add(cityObj);

            }
        } catch (SQLException ex) {
            Logger.getLogger(ActMainMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] cities = new String[CitiesList.size()];
        for (int i = 0; i < CitiesList.size(); i++) {
            cities[i] = CitiesList.get(i).toString();
        }

        CitiesModel = new DefaultComboBoxModel(cities);

        return CitiesModel;

//        DefaultComboBoxModel CitiesNamesModel = null;
//        String[] CitiesNames = null; // stores all the bill Names

        // initialise the Bungeni Connector Client
//         BungeniConnector client = null ;
//        try {
//            // initialize the data store client
//            client = CommonConnectorFunctions.getDSClient();
//
//            // get the bills from the registry H2 db
//            List<Cities> CitiesList = client.getCities();
//            CitiesNames = new String[CitiesList.size()] ;
//            
//            // loop through extracting the bills
//            for (int i = 0 ; i < CitiesList.size() ; i ++)
//            {
//                // get the current bill & extract the bill Name
//                Cities currJudgementRegion = CitiesList.get(i) ;
//                CitiesNames[i] = currJudgementRegion.getNameByLang(Locale.getDefault().getLanguage());
//           //    CitiesNames[i] = currJudgementRegion.getNameByLang("en");
//               
//            }
//
//            // create the default bills Names model
//            CitiesNamesModel = new DefaultComboBoxModel(CitiesNames) ;
//            
//        } catch (IOException ex) {
//            log.error(ex) ;
//        }

//        return CitiesNamesModel;
    }

    public boolean applySelectedMetadata(BungeniFileSavePathFormat spf) {
        boolean bState = false;
        try {
            LanguageCode selLanguage = (LanguageCode) this.cboLanguage.getSelectedItem();
            String sFamily = (String) this.cboCourtType.getSelectedItem();
            String sDomain = (String) this.cboDomain.getSelectedItem();
            String sCaseType = (String) this.cboCaseType.getSelectedItem();
            String sCity = (String) this.cboCities.getSelectedItem();
            String sCaseNo = this.txtCaseNumber.getText();
            String sDate = dtformatter.format(dt_official_date.getDate());
            String sYear = this.txtYear.getText();

            docMetaModel.updateItem("BungeniLanguageCode", selLanguage.getLanguageCodeAlpha2());
            docMetaModel.updateItem("BungeniFamily", sFamily);
            docMetaModel.updateItem("BungeniDomain", sDomain);
            docMetaModel.updateItem("BungeniCaseType", sCaseType);
            docMetaModel.updateItem("BungeniCity", sCity);
            docMetaModel.updateItem("BungeniCaseNo", sCaseNo);
            docMetaModel.updateItem("BungeniIssuedOn", sDate);
            docMetaModel.updateItem("BungeniYear", sYear);

            spf.setSaveComponent("DocumentType", BungeniEditorPropertiesHelper.getCurrentDocType());
            spf.setSaveComponent("LanguageCode", Locale.getDefault().getLanguage());
            spf.setSaveComponent("CountryCode", Locale.getDefault().getCountry());

            Date dtHansardDate = dt_official_date.getDate();
            GregorianCalendar debateCal = new GregorianCalendar();
            debateCal.setTime(dtHansardDate);
            spf.setSaveComponent("Year", debateCal.get(Calendar.YEAR));
            spf.setSaveComponent("Month", debateCal.get(Calendar.MONTH) + 1);
            spf.setSaveComponent("Day", debateCal.get(Calendar.DAY_OF_MONTH));
            spf.setSaveComponent("PartName", "main");
            spf.setSaveComponent("Year", sYear);
            spf.setSaveComponent("Num", sCaseNo);

            CaseType selectedCaseType = CaseTypesList.get(this.cboCaseType.getSelectedIndex());
            String strBungeniCaseType = selectedCaseType.getCaseTypeName_E();
            spf.setSaveComponent("JCaseType", strBungeniCaseType);

            City selectedCity = CitiesList.get(this.cboCities.getSelectedIndex());
            String strBungeniCity = selectedCity.getCityName_E();
            spf.setSaveComponent("JCity", strBungeniCity);

            spf.setSaveComponent("JIssuedOn", savedtformatter.format(dt_official_date.getDate()));

            spf.setSaveComponent("JCaseNo", sCaseNo);

            //other metadata
            docMetaModel.updateItem("BungeniWorkAuthor", "pna");
            docMetaModel.updateItem("BungeniWorkAuthorAs", "author");
            docMetaModel.updateItem("BungeniWorkAuthorURI", "user.Samar");
            docMetaModel.updateItem("BungeniWorkDateName", "Issuing");
            docMetaModel.updateItem("BungeniWorkDate", sDate);

            docMetaModel.updateItem("BungeniDocPart", "main");

            //expression
            docMetaModel.updateItem("BungeniExpAuthor", "pna");
            docMetaModel.updateItem("BungeniExpAuthorAs", "editor");
            docMetaModel.updateItem("BungeniExpAuthorURI", "user.Samar");
            docMetaModel.updateItem("BungeniExpDateName", "Issuing");
            docMetaModel.updateItem("BungeniExpDate", sDate);
            //manifestation
            docMetaModel.updateItem("BungeniManAuthor", "IoL");
            docMetaModel.updateItem("BungeniManAuthorAs", "publisher");
            docMetaModel.updateItem("BungeniManAuthorURI", "user.Samar");
            docMetaModel.updateItem("BungeniManDateName", "Generation");
            docMetaModel.updateItem("BungeniManDate", sDate);

//            spf.setSaveComponent("Domain", engDomian);
//            spf.setSaveComponent("Case", engCaseType);
//            spf.setSaveComponent("MainType", finalMainPart);
            spf.parseComponents();

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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cboImportance = new javax.swing.JComboBox();
        lblImportance = new javax.swing.JLabel();
        lblCaseNo = new javax.swing.JLabel();
        cboCourtType = new javax.swing.JComboBox();
        slash = new javax.swing.JLabel();
        cboCities = new javax.swing.JComboBox();
        cboCaseType = new javax.swing.JComboBox();
        lblCaseType = new javax.swing.JLabel();
        dt_official_date = new org.jdesktop.swingx.JXDatePicker();
        txtCaseNumber = new javax.swing.JTextField();
        cboLanguage = new javax.swing.JComboBox();
        lblOfficialDate = new javax.swing.JLabel();
        lblCity = new javax.swing.JLabel();
        lblCourtType = new javax.swing.JLabel();
        lblLanguage = new javax.swing.JLabel();
        txtYear = new javax.swing.JTextField();
        cboDomain = new javax.swing.JComboBox();
        lblYear = new javax.swing.JLabel();
        lblDomain = new javax.swing.JLabel();
        cboRegion = new javax.swing.JComboBox();
        lblRegion = new javax.swing.JLabel();
        chbJudgeChecked = new javax.swing.JCheckBox();
        cboImportance1 = new javax.swing.JComboBox();
        lblImportance1 = new javax.swing.JLabel();

        cboImportance.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboImportance.setModel(setImportanceModel());
        cboImportance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboImportanceActionPerformed(evt);
            }
        });

        lblImportance.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        lblImportance.setText(bundle.getString("CourtJudgementMetadata.lblImportance.text")); // NOI18N

        setPreferredSize(new java.awt.Dimension(380, 296));

        lblCaseNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("org/birzeit/editor/metadata/editors/Bundle"); // NOI18N
        lblCaseNo.setText(bundle1.getString("CourtJudgementMetadata.lblCaseNo.text")); // NOI18N

        cboCourtType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCourtType.setModel(setLitigationTypesModel());

        slash.setText(bundle.getString("CourtJudgementMetadata.slash.text")); // NOI18N

        cboCities.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCities.setModel(setCitiesModel());
        cboCities.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCitiesActionPerformed(evt);
            }
        });

        cboCaseType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCaseType.setModel(setCaseTypesModel());

        lblCaseType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseType.setText(bundle1.getString("CourtJudgementMetadata.lblCaseType.text")); // NOI18N

        dt_official_date.setFormats("dd/MM/yyyy");
        dt_official_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        txtCaseNumber.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtCaseNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCaseNumberActionPerformed(evt);
            }
        });

        cboLanguage.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboLanguage.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLanguage.setName("fld.BungeniLanguageID"); // NOI18N

        lblOfficialDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblOfficialDate.setText(bundle1.getString("CourtJudgementMetadata.lblOfficialDate.text")); // NOI18N

        lblCity.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCity.setText(bundle1.getString("CourtJudgementMetadata.lblCity.text")); // NOI18N

        lblCourtType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCourtType.setText(bundle1.getString("CourtJudgementMetadata.lblCourtType.text")); // NOI18N

        lblLanguage.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblLanguage.setText(bundle1.getString("CourtJudgementMetadata.lblLanguage.text")); // NOI18N
        lblLanguage.setName("lbl.BungeniLanguageID"); // NOI18N

        txtYear.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtYearActionPerformed(evt);
            }
        });

        cboDomain.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboDomain.setModel(setDomainsModel());

        lblYear.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblYear.setText(bundle1.getString("CourtJudgementMetadata.lblYear.text")); // NOI18N

        lblDomain.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblDomain.setText(bundle1.getString("CourtJudgementMetadata.lblDomain.text")); // NOI18N

        cboRegion.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboRegion.setModel(setRegionsModel());
        cboRegion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRegionActionPerformed(evt);
            }
        });

        lblRegion.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblRegion.setText(bundle1.getString("CourtJudgementMetadata.lblRegion.text")); // NOI18N

        chbJudgeChecked.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        chbJudgeChecked.setText(bundle1.getString("CourtJudgementMetadata.chbJudgeChecked.text")); // NOI18N
        chbJudgeChecked.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbJudgeCheckedActionPerformed(evt);
            }
        });

        cboImportance1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboImportance1.setModel(setImportanceModel());
        cboImportance1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboImportance1ActionPerformed(evt);
            }
        });

        lblImportance1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblImportance1.setText(bundle1.getString("CourtJudgementMetadata.lblImportance.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCaseType)
                    .addComponent(cboCaseType, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCourtType, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCourtType, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCities, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCity, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLanguage)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCaseNo)
                            .addComponent(txtCaseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(slash, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblYear)))
                    .addComponent(dt_official_date, javax.swing.GroupLayout.DEFAULT_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOfficialDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboDomain, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboRegion, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblDomain))
                    .addComponent(cboImportance1, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblImportance1)
                    .addComponent(chbJudgeChecked))
                .addGap(23, 23, 23))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblCaseType)
                        .addGap(9, 9, 9)
                        .addComponent(cboCaseType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblCourtType)
                        .addGap(9, 9, 9)
                        .addComponent(cboCourtType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblDomain)
                        .addGap(9, 9, 9)
                        .addComponent(cboDomain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(lblRegion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboRegion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblCity)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboCities, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chbJudgeChecked))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblOfficialDate)
                        .addGap(6, 6, 6)
                        .addComponent(dt_official_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblImportance1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboImportance1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCaseNo)
                    .addComponent(lblYear))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCaseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(slash)
                    .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cboCitiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCitiesActionPerformed
    }//GEN-LAST:event_cboCitiesActionPerformed

    private void txtCaseNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCaseNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCaseNumberActionPerformed

    private void txtYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtYearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtYearActionPerformed

    private void cboRegionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRegionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboRegionActionPerformed

    private void chbJudgeCheckedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbJudgeCheckedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chbJudgeCheckedActionPerformed

    private void cboImportanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboImportanceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboImportanceActionPerformed

    private void cboImportance1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboImportance1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboImportance1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cboCaseType;
    private javax.swing.JComboBox cboCities;
    private javax.swing.JComboBox cboCourtType;
    private javax.swing.JComboBox cboDomain;
    private javax.swing.JComboBox cboImportance;
    private javax.swing.JComboBox cboImportance1;
    private javax.swing.JComboBox cboLanguage;
    private javax.swing.JComboBox cboRegion;
    private javax.swing.JCheckBox chbJudgeChecked;
    private org.jdesktop.swingx.JXDatePicker dt_official_date;
    private javax.swing.JLabel lblCaseNo;
    private javax.swing.JLabel lblCaseType;
    private javax.swing.JLabel lblCity;
    private javax.swing.JLabel lblCourtType;
    private javax.swing.JLabel lblDomain;
    private javax.swing.JLabel lblImportance;
    private javax.swing.JLabel lblImportance1;
    private javax.swing.JLabel lblLanguage;
    private javax.swing.JLabel lblOfficialDate;
    private javax.swing.JLabel lblRegion;
    private javax.swing.JLabel lblYear;
    private javax.swing.JLabel slash;
    private javax.swing.JTextField txtCaseNumber;
    private javax.swing.JTextField txtYear;
    // End of variables declaration//GEN-END:variables

    @Override
    public Dimension getFrameSize() {
        int DIM_X = 229;
        int DIM_Y = 222;
        return new Dimension(DIM_X, DIM_Y + 10);
    }

    @Override
    public ArrayList<String> validateSelectedMetadata(BungeniFileSavePathFormat spf) {
        addFieldsToValidate(new TreeMap<String, Component>() {
            {
                put(lblLanguage.getText().replace("*", ""), cboLanguage);
                put(lblCourtType.getText().replace("*", ""), cboCourtType);
                put(lblDomain.getText().replace("*", ""), cboDomain);
                put(lblCaseType.getText().replace("*", ""), cboCaseType);
                put(lblCaseNo.getText().replace("*", ""), txtCaseNumber);
                put(lblCity.getText().replace("*", ""), cboCities);
                put(lblOfficialDate.getText().replace("*", ""), dt_official_date);
                put(lblYear.getText().replace("*", ""), txtYear);
            }
        });
        return super.validateSelectedMetadata(spf);
    }

    public void getDBValues(ArrayList values) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
