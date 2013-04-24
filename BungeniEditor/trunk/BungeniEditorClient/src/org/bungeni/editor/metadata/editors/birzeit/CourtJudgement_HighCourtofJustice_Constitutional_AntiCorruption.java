/*
 * DebateRecordMetadata.java
 *
 * Created on November 4, 2008, 1:43 PM
 */
package org.bungeni.editor.metadata.editors.birzeit;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.element.*;
import org.bungeni.editor.config.BungeniEditorProperties;
import org.bungeni.editor.config.BungeniEditorPropertiesHelper;
import org.bungeni.editor.connectorutils.CommonConnectorFunctions;
import org.bungeni.editor.metadata.BaseEditorDocMetadataDialog;
import org.bungeni.editor.metadata.JudgementMetadataModel;
import org.bungeni.editor.metadata.LanguageCode;
import org.bungeni.editor.metadata.birzeit.CaseType;
import org.bungeni.editor.metadata.birzeit.City;
import org.bungeni.editor.metadata.birzeit.CourtType;
import org.bungeni.editor.metadata.birzeit.Domains;
import org.bungeni.editor.metadata.birzeit.JudgementRegion;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.extutils.CommonStringFunctions;
import org.bungeni.extutils.CommonUIFunctions;
import org.bungeni.utils.BungeniFileSavePathFormat;

/**
 *
 * @author undesa
 */
public class CourtJudgement_HighCourtofJustice_Constitutional_AntiCorruption extends BaseEditorDocMetadataDialog {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CourtJudgement_Appeal.class.getName());
    JudgementMetadataModel docMetaModel = new JudgementMetadataModel();
    private SimpleDateFormat dtformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
    private SimpleDateFormat savedtformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataSaveDateFormat"));
    private SimpleDateFormat dbdtformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("dataBaseDateFormat"));
    private String dbName = "CourtJudgments2007";
    private ArrayList<CaseType> CaseTypesList = new ArrayList<CaseType>();
    private ArrayList<CourtType> CourtTypesList = new ArrayList<CourtType>();
    private ArrayList<Domains> DomainsList = new ArrayList<Domains>();
    private ArrayList<JudgementRegion> JudgementRegionsList = new ArrayList<JudgementRegion>();
    private ArrayList<City> CitiesList = new ArrayList<City>();
    private Connection con = ConnectorFunctions.ConnectMMSM(dbName);
    private Statement conStmt;

    public CourtJudgement_HighCourtofJustice_Constitutional_AntiCorruption() {
        super();
        
           try {
            conStmt = con.createStatement();
        } catch (SQLException ex) {
                log.error("SQL Exception", ex);
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
                    if (metadata.get(i).getName().equalsIgnoreCase("BungeniFamily")) {
                        docMetaModel.setBungeniFamily(metadata.get(i).getValue());
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
        if (CaseTypesList.isEmpty()) {
            try {
                String sqlStm = "SELECT DISTINCT [CJ_CaseTypes_ID], [CJ_CaseTypes_NameAlternate], [CJ_CaseTypes_NameAlternate_E] FROM [CJ_CaseTypes] WHERE [CJ_CaseTypes_IsAlternateType]=1";
                ResultSet rs = conStmt.executeQuery(sqlStm);

                while (rs.next()) {
                    CaseType caseTypeObj = new CaseType(rs.getString(1), rs.getString(2), rs.getString(3));
                    CaseTypesList.add(caseTypeObj);

                }
            } catch (SQLException ex) {
                log.error("SQL Exception", ex);
            }
        }
        String[] judgementCaseTypes = new String[CaseTypesList.size()];
        for (int i = 0; i < CaseTypesList.size(); i++) {
            judgementCaseTypes[i] = CaseTypesList.get(i).toString();
        }
        // create the default acts Names mode
        judgementCaseTypesModel = new DefaultComboBoxModel(judgementCaseTypes);

        return judgementCaseTypesModel;
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
                log.error("SQL Exception", ex);
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

    private ComboBoxModel setCourtTypesModel() {
        DefaultComboBoxModel courtTypesModel = null;

        try {
            String sqlStm = "SELECT DISTINCT [CJ_CourtTypes_ID], [CJ_CourtTypes_Name], [CJ_CourtTypes_Name_E] FROM [CJ_CourtTypes] WHERE [CJ_CourtTypes_Degree]=5 and [CJ_CourtTypes_IsCourtName]=1";
            ResultSet rs = conStmt.executeQuery(sqlStm);

            while (rs.next()) {
                CourtType ctObj = new CourtType(rs.getString(1), rs.getString(2), rs.getString(3));
                CourtTypesList.add(ctObj);

            }
        } catch (SQLException ex) {
                log.error("SQL Exception", ex);
        }

        String[] courtTypes = new String[CourtTypesList.size()];
        for (int i = 0; i < CourtTypesList.size(); i++) {
            courtTypes[i] = CourtTypesList.get(i).toString();
        }
        // create the default acts Names mode
        courtTypesModel = new DefaultComboBoxModel(courtTypes);

        return courtTypesModel;
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
                log.error("SQL Exception", ex);
        }

        String[] judgementRegions = new String[JudgementRegionsList.size()];
        for (int i = 0; i < JudgementRegionsList.size(); i++) {
            judgementRegions[i] = JudgementRegionsList.get(i).toString();
        }
        // create the default acts Names mode
        judgmentRegionsModel = new DefaultComboBoxModel(judgementRegions);

        return judgmentRegionsModel;

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
                log.error("SQL Exception", ex);
        }

        String[] cities = new String[CitiesList.size()];
        for (int i = 0; i < CitiesList.size(); i++) {
            cities[i] = CitiesList.get(i).toString();
        }

        CitiesModel = new DefaultComboBoxModel(cities);

        return CitiesModel;

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
            String sDate = dbdtformatter.format(dt_official_date.getDate());
            String sYear = this.txtYear.getText();
            String sImportance = this.txtImportance.getText();

            docMetaModel.updateItem("BungeniLanguageCode", selLanguage.getLanguageCodeAlpha2());
            docMetaModel.updateItem("BungeniFamily", sFamily);
            docMetaModel.updateItem("BungeniDomain", sDomain);
            docMetaModel.updateItem("BungeniCaseType", sCaseType);
            docMetaModel.updateItem("BungeniCity", sCity);
            docMetaModel.updateItem("BungeniCaseNo", sCaseNo);
            docMetaModel.updateItem("BungeniIssuedOn", sDate);
            docMetaModel.updateItem("BungeniYear", sYear);
            docMetaModel.updateItem("BungeniImportance", sImportance);


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

        jPanel1 = new javax.swing.JPanel();
        lblDomain2 = new javax.swing.JLabel();
        cboRegion = new javax.swing.JComboBox();
        lblRegion = new javax.swing.JLabel();
        chbJudgeChecked = new javax.swing.JCheckBox();
        cboDomain = new javax.swing.JComboBox();
        lblYear = new javax.swing.JLabel();
        lblOfficialDate = new javax.swing.JLabel();
        slash = new javax.swing.JLabel();
        cboLanguage = new javax.swing.JComboBox();
        cboCourtType = new javax.swing.JComboBox();
        lblCourtType = new javax.swing.JLabel();
        lblCity = new javax.swing.JLabel();
        cboCities = new javax.swing.JComboBox();
        lblCaseType = new javax.swing.JLabel();
        lblImportance = new javax.swing.JLabel();
        cboCaseType = new javax.swing.JComboBox();
        txtCaseNumber = new javax.swing.JTextField();
        dt_official_date = new org.jdesktop.swingx.JXDatePicker();
        lblLanguage = new javax.swing.JLabel();
        txtYear = new javax.swing.JTextField();
        lblCaseNo = new javax.swing.JLabel();
        txtImportance = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(500, 550));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("CourtJudgementMetadata.CourtJudgement.text"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 10))); // NOI18N

        lblDomain2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblDomain2.setText(bundle.getString("CourtJudgementMetadata.lblDomain.text")); // NOI18N

        cboRegion.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboRegion.setModel(setRegionsModel());
        cboRegion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRegionActionPerformed(evt);
            }
        });

        lblRegion.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblRegion.setText(bundle.getString("CourtJudgement_Appeal.lblRegion.text")); // NOI18N

        chbJudgeChecked.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        chbJudgeChecked.setText(bundle.getString("CourtJudgement_Appeal.chbJudgeChecked.text")); // NOI18N
        chbJudgeChecked.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbJudgeCheckedActionPerformed(evt);
            }
        });

        cboDomain.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboDomain.setModel(setDomainsModel());

        lblYear.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblYear.setText(bundle.getString("CourtJudgement_Appeal.lblYear.text")); // NOI18N

        lblOfficialDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblOfficialDate.setText(bundle.getString("CourtJudgement_Appeal.lblOfficialDate.text")); // NOI18N

        slash.setText(bundle.getString("CourtJudgement_Appeal.slash.text")); // NOI18N

        cboLanguage.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboLanguage.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLanguage.setName("fld.BungeniLanguageID"); // NOI18N

        cboCourtType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCourtType.setModel(setCaseTypesModel());

        lblCourtType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCourtType.setText(bundle.getString("CourtJudgement_Appeal.lblCourtType.text")); // NOI18N

        lblCity.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCity.setText(bundle.getString("CourtJudgement_Appeal.lblCity.text")); // NOI18N

        cboCities.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCities.setModel(setCitiesModel());
        cboCities.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCitiesActionPerformed(evt);
            }
        });

        lblCaseType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseType.setText(bundle.getString("CourtJudgement_Appeal.lblCaseType.text")); // NOI18N

        lblImportance.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblImportance.setText(bundle.getString("CourtJudgementMetadata.lblImportance.text")); // NOI18N

        cboCaseType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCaseType.setModel(setCourtTypesModel());

        txtCaseNumber.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtCaseNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCaseNumberActionPerformed(evt);
            }
        });

        dt_official_date.setFormats("dd/MM/yyyy");
        dt_official_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblLanguage.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblLanguage.setText(bundle.getString("CourtJudgement_Appeal.lblLanguage.text")); // NOI18N
        lblLanguage.setName("lbl.BungeniLanguageID"); // NOI18N

        txtYear.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtYearActionPerformed(evt);
            }
        });

        lblCaseNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseNo.setText(bundle.getString("CourtJudgement_Appeal.lblCaseNo.text")); // NOI18N

        txtImportance.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/birzeit/Bundle"); // NOI18N
        txtImportance.setText(bundle1.getString("CourtJudgement_HighCourtofJustice_Constitutional_AntiCorruption.txtImportance.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboCaseType, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCourtType, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCourtType, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCities, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCity, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCaseNo)
                            .addComponent(txtCaseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(slash, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblYear)))
                    .addComponent(dt_official_date, javax.swing.GroupLayout.DEFAULT_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOfficialDate)
                    .addComponent(lblLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCaseType))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboDomain, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboRegion, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(lblDomain2))
                            .addComponent(lblImportance)
                            .addComponent(chbJudgeChecked))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtImportance))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cboCaseType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(lblCaseType)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCourtType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDomain2)
                            .addComponent(lblCourtType))
                        .addGap(9, 9, 9)
                        .addComponent(cboDomain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(lblRegion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboRegion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblCity)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboCities, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chbJudgeChecked))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblOfficialDate)
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dt_official_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtImportance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblImportance)
                        .addGap(26, 26, 26)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCaseNo)
                    .addComponent(lblYear))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCaseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(slash)
                    .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(137, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(193, Short.MAX_VALUE))
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cboCaseType;
    private javax.swing.JComboBox cboCities;
    private javax.swing.JComboBox cboCourtType;
    private javax.swing.JComboBox cboDomain;
    private javax.swing.JComboBox cboLanguage;
    private javax.swing.JComboBox cboRegion;
    private javax.swing.JCheckBox chbJudgeChecked;
    private org.jdesktop.swingx.JXDatePicker dt_official_date;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblCaseNo;
    private javax.swing.JLabel lblCaseType;
    private javax.swing.JLabel lblCity;
    private javax.swing.JLabel lblCourtType;
    private javax.swing.JLabel lblDomain2;
    private javax.swing.JLabel lblImportance;
    private javax.swing.JLabel lblLanguage;
    private javax.swing.JLabel lblOfficialDate;
    private javax.swing.JLabel lblRegion;
    private javax.swing.JLabel lblYear;
    private javax.swing.JLabel slash;
    private javax.swing.JTextField txtCaseNumber;
    private javax.swing.JTextField txtImportance;
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
//                put(lblLanguage.getText().replace("*", ""), cboLanguage);
//                put(lblCourtType.getText().replace("*", ""), cboCourtType);
//                put(lblDomain2.getText().replace("*", ""), cboDomain);
//                put(lblCaseType.getText().replace("*", ""), cboCaseType);
//                put(lblCaseNo.getText().replace("*", ""), txtCaseNumber);
//                put(lblCity.getText().replace("*", ""), cboCities);
//                put(lblOfficialDate.getText().replace("*", ""), dt_official_date);
//                put(lblYear.getText().replace("*", ""), txtYear);
            }
        });
        return super.validateSelectedMetadata(spf);
    }

    public void resetComponents() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
