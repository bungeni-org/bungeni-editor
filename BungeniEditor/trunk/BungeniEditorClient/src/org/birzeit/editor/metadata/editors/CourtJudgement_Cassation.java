/*
 * DebateRecordMetadata.java
 *
 * Created on November 4, 2008, 1:43 PM
 */
package org.birzeit.editor.metadata.editors;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
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
import org.birzeit.editor.metadata.CJ_Main;
import org.birzeit.editor.metadata.CaseType;
import org.birzeit.editor.metadata.City;
import org.birzeit.editor.metadata.CourtType;
import org.birzeit.editor.metadata.Domains;
import org.birzeit.editor.metadata.JudgementRegion;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.extutils.CommonStringFunctions;
import org.bungeni.extutils.CommonUIFunctions;
import org.bungeni.extutils.MessageBox;
import org.bungeni.utils.BungeniFileSavePathFormat;

/**
 *
 * @author undesa
 */
public class CourtJudgement_Cassation extends BaseEditorDocMetadataDialog {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CourtJudgement_Cassation.class.getName());
    JudgementMetadataModel docMetaModel = new JudgementMetadataModel();
    private SimpleDateFormat dtformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
    private SimpleDateFormat dbdtformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("dataBaseDateFormat"));
    private SimpleDateFormat savedtformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataSaveDateFormat"));
    private String dbName = "CourtJudgments2007";
    private ArrayList<CaseType> CaseTypesList = new ArrayList<CaseType>();
    private ArrayList<Domains> DomainsList = new ArrayList<Domains>();
    private ArrayList<JudgementRegion> JudgementRegionsList = new ArrayList<JudgementRegion>();
    private ArrayList<City> CitiesList = new ArrayList<City>();
    private ArrayList<CourtType> CourtTypesListA = new ArrayList<CourtType>();
    private ArrayList<CourtType> CourtTypesListC = new ArrayList<CourtType>();
    private ArrayList<CourtType> CourtTypesList = new ArrayList<CourtType>();
    private Connection con = ConnectorFunctions.ConnectMMSM(dbName);
    private Statement conStmt;

    public CourtJudgement_Cassation() {
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
    private ComboBoxModel setCourtTypesModel_Cassation() {
        DefaultComboBoxModel courtTypesModel = null;

        try {
            String sqlStm = "SELECT DISTINCT [CJ_CourtTypes_ID], [CJ_CourtTypes_Name], [CJ_CourtTypes_Name_E] FROM [CJ_CourtTypes] WHERE [CJ_CourtTypes_Degree]=3 and [CJ_CourtTypes_IsCourtName]=1";
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

    private ComboBoxModel setCourtTypesModel_Appeal() {

        DefaultComboBoxModel courtTypeeModel = null;
        if (CourtTypesListA.isEmpty()) {
            try {
                String sqlStm = "SELECT DISTINCT [CJ_CourtTypes_ID], [CJ_CourtTypes_Name], [CJ_CourtTypes_Name_E] FROM [CJ_CourtTypes] WHERE [CJ_CourtTypes_Degree]=2 and [CJ_CourtTypes_IsCourtName]=1";
                ResultSet rs = conStmt.executeQuery(sqlStm);

                while (rs.next()) {
                    CourtType ctObj = new CourtType(rs.getString(1), rs.getString(2), rs.getString(3));
                    CourtTypesListA.add(ctObj);

                }
            } catch (SQLException ex) {
                log.error("SQL Exception", ex);
            }
        }
        String[] courtTypes = new String[CourtTypesListA.size()];
        for (int i = 0; i < CourtTypesListA.size(); i++) {
            courtTypes[i] = CourtTypesListA.get(i).toString();
        }
        // create the default acts Names mode
        courtTypeeModel = new DefaultComboBoxModel(courtTypes);
        return courtTypeeModel;
    }

    private ComboBoxModel setCaseTypesModel() {

        DefaultComboBoxModel judgementCaseTypesModel = null;
        if (CaseTypesList.isEmpty()) {
            try {
                String sqlStm = "SELECT [CJ_CaseTypes_ID], [CJ_CaseTypes_Name], [CJ_CaseTypes_Name_E] FROM [CJ_CaseTypes] WHERE [CJ_CaseTypes_IsAlternateType] = 1 ";
                ResultSet rs = conStmt.executeQuery(sqlStm);

                while (rs.next()) {
                    CaseType ctObj = new CaseType(rs.getString(1), rs.getString(2), rs.getString(3));
                    CaseTypesList.add(ctObj);

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
        if (DomainsList.isEmpty()) {
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
        }
        String[] judgementDomains = new String[DomainsList.size()];
        for (int i = 0; i < DomainsList.size(); i++) {
            judgementDomains[i] = DomainsList.get(i).toString();
        }
        // create the default acts Names mode
        judgementDomainsModel = new DefaultComboBoxModel(judgementDomains);

        return judgementDomainsModel;
    }

    private ComboBoxModel setCourtTypesModel() {

        DefaultComboBoxModel courtTypesModel = null;

        if (CourtTypesListC.isEmpty()) {
            try {
                String sqlStm = "SELECT DISTINCT [CJ_CourtTypes_ID], [CJ_CourtTypes_Name], [CJ_CourtTypes_Name_E] FROM [CJ_CourtTypes] WHERE [CJ_CourtTypes_Degree]=1 ";
                ResultSet rs = conStmt.executeQuery(sqlStm);

                while (rs.next()) {
                    CourtType ctObj = new CourtType(rs.getString(1), rs.getString(2), rs.getString(3));
                    CourtTypesListC.add(ctObj);

                }
            } catch (SQLException ex) {
                log.error("SQL Exception", ex);

            }
        }
        String[] courtTypes = new String[CourtTypesListC.size()];
        for (int i = 0; i < CourtTypesListC.size(); i++) {
            courtTypes[i] = CourtTypesListC.get(i).toString();
        }
        // create the default acts Names mode
        courtTypesModel = new DefaultComboBoxModel(courtTypes);
        return courtTypesModel;
    }

    private ComboBoxModel setRegionsModel() {
        DefaultComboBoxModel judgmentRegionsModel = null;
        if (JudgementRegionsList.isEmpty()) {
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
        if (CitiesList.isEmpty()) {
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

        jAppealDataPanel = new javax.swing.JPanel();
        lblCaseNo1 = new javax.swing.JLabel();
        slash2 = new javax.swing.JLabel();
        cboCaseType1 = new javax.swing.JComboBox();
        cboCourtType1 = new javax.swing.JComboBox();
        cboCities1 = new javax.swing.JComboBox();
        lblRegion1 = new javax.swing.JLabel();
        cboRegion1 = new javax.swing.JComboBox();
        cboDomain1 = new javax.swing.JComboBox();
        txtYear1 = new javax.swing.JTextField();
        lblDomain1 = new javax.swing.JLabel();
        lblYear1 = new javax.swing.JLabel();
        chbJudgeChecked1 = new javax.swing.JCheckBox();
        lblCity1 = new javax.swing.JLabel();
        lblCaseType1 = new javax.swing.JLabel();
        lblOfficialDate1 = new javax.swing.JLabel();
        dt_official_date1 = new org.jdesktop.swingx.JXDatePicker();
        lblLanguage1 = new javax.swing.JLabel();
        txtCaseNumber1 = new javax.swing.JTextField();
        lblCourtType1 = new javax.swing.JLabel();
        cboLanguage1 = new javax.swing.JComboBox();
        lblImportance1 = new javax.swing.JLabel();
        txtImportance1 = new javax.swing.JTextField();
        btnNewAppeal1 = new javax.swing.JButton();
        lblSaveAppeal = new javax.swing.JButton();
        lblAppealId = new javax.swing.JLabel();
        jCaseDataPanel = new javax.swing.JPanel();
        lblCaseNo2 = new javax.swing.JLabel();
        slash1 = new javax.swing.JLabel();
        cboCaseType2 = new javax.swing.JComboBox();
        cboCourtType2 = new javax.swing.JComboBox();
        cboCities2 = new javax.swing.JComboBox();
        lblRegion2 = new javax.swing.JLabel();
        cboRegion2 = new javax.swing.JComboBox();
        cboDomain2 = new javax.swing.JComboBox();
        txtYear2 = new javax.swing.JTextField();
        lblDomain2 = new javax.swing.JLabel();
        lblYear2 = new javax.swing.JLabel();
        lblCity2 = new javax.swing.JLabel();
        lblCaseType2 = new javax.swing.JLabel();
        lblOfficialDate2 = new javax.swing.JLabel();
        dt_official_date2 = new org.jdesktop.swingx.JXDatePicker();
        txtCaseNumber2 = new javax.swing.JTextField();
        lblCourtType2 = new javax.swing.JLabel();
        btnNewCase1 = new javax.swing.JButton();
        lblSaveCase = new javax.swing.JButton();
        lblCaseId = new javax.swing.JLabel();
        jSearchCaseDataPanel = new javax.swing.JPanel();
        btnSearchC = new javax.swing.JButton();
        slash3 = new javax.swing.JLabel();
        lblNoC = new javax.swing.JLabel();
        lblCaseTypeC = new javax.swing.JLabel();
        txtYearC = new javax.swing.JTextField();
        txtNoC = new javax.swing.JTextField();
        lblYearC = new javax.swing.JLabel();
        cboCourtTypeC = new javax.swing.JComboBox();
        btnNewCase = new javax.swing.JButton();
        jCassationPanel = new javax.swing.JPanel();
        lblCaseNo = new javax.swing.JLabel();
        cboCourtType = new javax.swing.JComboBox();
        cboCaseType = new javax.swing.JComboBox();
        slash = new javax.swing.JLabel();
        cboCities = new javax.swing.JComboBox();
        lblCaseType = new javax.swing.JLabel();
        dt_official_date = new org.jdesktop.swingx.JXDatePicker();
        txtCaseNumber = new javax.swing.JTextField();
        cboLanguage = new javax.swing.JComboBox();
        lblOfficialDate = new javax.swing.JLabel();
        lblCity = new javax.swing.JLabel();
        lblCourtType = new javax.swing.JLabel();
        lblYear = new javax.swing.JLabel();
        lblLanguage = new javax.swing.JLabel();
        txtYear = new javax.swing.JTextField();
        cboDomain = new javax.swing.JComboBox();
        cboRegion = new javax.swing.JComboBox();
        lblDomain = new javax.swing.JLabel();
        chbJudgeChecked = new javax.swing.JCheckBox();
        lblRegion = new javax.swing.JLabel();
        lblImportance = new javax.swing.JLabel();
        txtImportance = new javax.swing.JTextField();
        lblSaveCassation = new javax.swing.JButton();
        lblCassationId = new javax.swing.JLabel();
        jSearchAppealDataPanel = new javax.swing.JPanel();
        lblCaseNoA = new javax.swing.JLabel();
        slash4 = new javax.swing.JLabel();
        cboCourtTypeA = new javax.swing.JComboBox();
        txtYearA = new javax.swing.JTextField();
        lblYearA = new javax.swing.JLabel();
        lblCourtTypeA = new javax.swing.JLabel();
        txtCaseNumberA = new javax.swing.JTextField();
        btnSearchA = new javax.swing.JButton();
        btnNewAppeal = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(450, 1500));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/birzeit/editor/metadata/editors/Bundle"); // NOI18N
        jAppealDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("CourtJudgement_Cassation.jAppealDataPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 10))); // NOI18N
        jAppealDataPanel.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jAppealDataPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jAppealDataPanelComponentHidden(evt);
            }
        });
        jAppealDataPanel.setVisible(false);

        lblCaseNo1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseNo1.setText(bundle.getString("CourtJudgement_Cassation.lblCaseNo1.text")); // NOI18N

        slash2.setText(bundle.getString("CourtJudgement_Cassation.slash2.text")); // NOI18N

        cboCaseType1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCaseType1.setModel(setCaseTypesModel());

        cboCourtType1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCourtType1.setModel(setCourtTypesModel_Appeal());

        cboCities1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCities1.setModel(setCitiesModel());

        lblRegion1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblRegion1.setText(bundle.getString("CourtJudgement_Cassation.lblRegion1.text")); // NOI18N

        cboRegion1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboRegion1.setModel(setRegionsModel());

        cboDomain1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboDomain1.setModel(setDomainsModel());

        txtYear1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblDomain1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblDomain1.setText(bundle.getString("CourtJudgement_Cassation.lblDomain1.text")); // NOI18N

        lblYear1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblYear1.setText(bundle.getString("CourtJudgement_Cassation.lblYear1.text")); // NOI18N

        chbJudgeChecked1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        chbJudgeChecked1.setText(bundle.getString("CourtJudgement_Cassation.chbJudgeChecked1.text")); // NOI18N

        lblCity1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCity1.setText(bundle.getString("CourtJudgement_Cassation.lblCity1.text")); // NOI18N

        lblCaseType1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseType1.setText(bundle.getString("CourtJudgement_Cassation.lblCaseType1.text")); // NOI18N

        lblOfficialDate1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblOfficialDate1.setText(bundle.getString("CourtJudgement_Cassation.lblOfficialDate1.text")); // NOI18N

        dt_official_date1.setFormats("dd/MM/yyyy");
        dt_official_date1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblLanguage1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblLanguage1.setText(bundle.getString("CourtJudgement_Cassation.lblLanguage1.text")); // NOI18N
        lblLanguage1.setName("lbl.BungeniLanguageID"); // NOI18N

        txtCaseNumber1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblCourtType1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCourtType1.setText(bundle.getString("CourtJudgement_Cassation.lblCourtType1.text")); // NOI18N

        cboLanguage1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboLanguage1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLanguage1.setName("fld.BungeniLanguageID"); // NOI18N

        lblImportance1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblImportance1.setText(bundle.getString("CourtJudgement_Cassation.lblImportance1.text")); // NOI18N

        txtImportance1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtImportance1.setText(bundle.getString("CourtJudgement_Cassation.txtImportance1.text")); // NOI18N

        btnNewAppeal1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnNewAppeal1.setText(bundle.getString("CourtJudgement_Cassation.btnNewAppeal1.text")); // NOI18N
        btnNewAppeal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewAppeal1ActionPerformed(evt);
            }
        });

        lblSaveAppeal.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSaveAppeal.setText(bundle.getString("CourtJudgement_Cassation.lblSaveAppeal.text")); // NOI18N
        lblSaveAppeal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblSaveAppealActionPerformed(evt);
            }
        });

        lblAppealId.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblAppealId.setText(bundle.getString("CourtJudgement_Cassation.lblAppealId.text")); // NOI18N

        javax.swing.GroupLayout jAppealDataPanelLayout = new javax.swing.GroupLayout(jAppealDataPanel);
        jAppealDataPanel.setLayout(jAppealDataPanelLayout);
        jAppealDataPanelLayout.setHorizontalGroup(
            jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                        .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCaseNo1)
                            .addComponent(txtCaseNumber1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(slash2, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                                .addComponent(lblYear1)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                                .addComponent(txtYear1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnNewAppeal1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblSaveAppeal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21))))
                    .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                        .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                                .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboCourtType1, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCourtType1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboCaseType1, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCaseType1))
                                .addGap(18, 18, 18)
                                .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cboDomain1, 0, 151, Short.MAX_VALUE)
                                        .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                                            .addGap(1, 1, 1)
                                            .addComponent(lblDomain1)))
                                    .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cboRegion1, 0, 151, Short.MAX_VALUE)
                                        .addComponent(lblRegion1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(lblAppealId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboLanguage1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                                .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboCities1, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCity1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblLanguage1)
                                    .addComponent(dt_official_date1, javax.swing.GroupLayout.DEFAULT_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblOfficialDate1))
                                .addGap(18, 18, 18)
                                .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chbJudgeChecked1)
                                    .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblImportance1)
                                            .addComponent(txtImportance1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addContainerGap())))
        );
        jAppealDataPanelLayout.setVerticalGroup(
            jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAppealId, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLanguage1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(cboLanguage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                        .addComponent(lblCourtType1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCourtType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                        .addComponent(lblDomain1)
                        .addGap(9, 9, 9)
                        .addComponent(cboDomain1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                        .addComponent(lblCaseType1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCaseType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                        .addComponent(lblRegion1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboRegion1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblCity1)
                .addGap(4, 4, 4)
                .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                        .addComponent(cboCities1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(lblOfficialDate1)
                        .addGap(4, 4, 4)
                        .addComponent(dt_official_date1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                        .addComponent(chbJudgeChecked1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtImportance1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                                .addComponent(lblImportance1)
                                .addGap(25, 25, 25)))))
                .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                        .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCaseNo1)
                            .addComponent(lblYear1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCaseNumber1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(slash2)
                            .addComponent(txtYear1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 36, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jAppealDataPanelLayout.createSequentialGroup()
                        .addComponent(btnNewAppeal1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSaveAppeal)))
                .addContainerGap())
        );

        jCaseDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("CourtJudgement_Cassation.jCaseDataPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 10))); // NOI18N
        jCaseDataPanel.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jCaseDataPanel.setVisible(false);

        lblCaseNo2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseNo2.setText(bundle.getString("CourtJudgement_Cassation.lblCaseNo2.text")); // NOI18N

        slash1.setText(bundle.getString("CourtJudgement_Cassation.slash1.text")); // NOI18N

        cboCaseType2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCaseType2.setModel(setCaseTypesModel());

        cboCourtType2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCourtType2.setModel(setCourtTypesModel());

        cboCities2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCities2.setModel(setCitiesModel());

        lblRegion2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblRegion2.setText(bundle.getString("CourtJudgement_Cassation.lblRegion2.text")); // NOI18N

        cboRegion2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboRegion2.setModel(setRegionsModel());

        cboDomain2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboDomain2.setModel(setDomainsModel());

        txtYear2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblDomain2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblDomain2.setText(bundle.getString("CourtJudgement_Cassation.lblDomain2.text")); // NOI18N

        lblYear2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblYear2.setText(bundle.getString("CourtJudgement_Cassation.lblYear2.text")); // NOI18N

        lblCity2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCity2.setText(bundle.getString("CourtJudgement_Cassation.lblCity2.text")); // NOI18N

        lblCaseType2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseType2.setText(bundle.getString("CourtJudgement_Cassation.lblCaseType2.text")); // NOI18N

        lblOfficialDate2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblOfficialDate2.setText(bundle.getString("CourtJudgement_Cassation.lblOfficialDate2.text")); // NOI18N

        dt_official_date1.setFormats("dd/MM/yyyy");
        dt_official_date2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        txtCaseNumber2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblCourtType2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCourtType2.setText(bundle.getString("CourtJudgement_Cassation.lblCourtType2.text")); // NOI18N

        btnNewCase1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnNewCase1.setText(bundle.getString("CourtJudgement_Cassation.btnNewCase1.text")); // NOI18N
        btnNewCase1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewCase1ActionPerformed(evt);
            }
        });

        lblSaveCase.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSaveCase.setText(bundle.getString("CourtJudgement_Cassation.lblSaveCase.text")); // NOI18N
        lblSaveCase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblSaveCaseActionPerformed(evt);
            }
        });

        lblCaseId.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseId.setText(bundle.getString("CourtJudgement_Cassation.lblCaseId.text")); // NOI18N

        javax.swing.GroupLayout jCaseDataPanelLayout = new javax.swing.GroupLayout(jCaseDataPanel);
        jCaseDataPanel.setLayout(jCaseDataPanelLayout);
        jCaseDataPanelLayout.setHorizontalGroup(
            jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                        .addComponent(lblCaseId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jCaseDataPanelLayout.createSequentialGroup()
                        .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                                .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCaseNo2)
                                    .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                                        .addComponent(txtCaseNumber2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(slash1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(5, 5, 5)
                                .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblYear2)
                                    .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                                        .addComponent(txtYear2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblSaveCase, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnNewCase1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                                .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dt_official_date2, javax.swing.GroupLayout.DEFAULT_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblOfficialDate2)
                                    .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cboCourtType2, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblCourtType2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(lblCaseType2)
                                        .addComponent(cboCaseType2, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboCities2, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCity2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboRegion2, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblRegion2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboDomain2, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblDomain2))))
                        .addGap(45, 45, 45))))
        );
        jCaseDataPanelLayout.setVerticalGroup(
            jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jCaseDataPanelLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(lblCaseId, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                        .addComponent(lblCourtType2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCourtType2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblCaseType2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCaseType2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOfficialDate2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_official_date2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                        .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jCaseDataPanelLayout.createSequentialGroup()
                                .addComponent(lblRegion2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboRegion2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                                .addComponent(lblDomain2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboDomain2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(52, 52, 52)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCity2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCities2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCaseNo2)
                            .addComponent(lblYear2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCaseNumber2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtYear2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(slash1))
                        .addContainerGap(55, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jCaseDataPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnNewCase1)
                        .addGap(4, 4, 4)
                        .addComponent(lblSaveCase)
                        .addContainerGap())))
        );

        jSearchCaseDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("CourtJudgement_Cassation.jSearchCaseDataPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 10))); // NOI18N
        jSearchCaseDataPanel.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jSearchCaseDataPanel.setVisible(false);

        btnSearchC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnSearchC.setText(bundle.getString("CourtJudgement_Cassation.btnSearchC.text")); // NOI18N
        btnSearchC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchCActionPerformed(evt);
            }
        });

        slash3.setText(bundle.getString("CourtJudgement_Cassation.slash3.text")); // NOI18N

        lblNoC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblNoC.setText(bundle.getString("CourtJudgement_Cassation.lblNoC.text")); // NOI18N

        lblCaseTypeC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseTypeC.setText(bundle.getString("CourtJudgement_Cassation.lblCaseTypeC.text")); // NOI18N

        txtYearC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtYearC.setText(bundle.getString("CourtJudgement_Cassation.txtYearC.text")); // NOI18N

        txtNoC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtNoC.setText(bundle.getString("CourtJudgement_Cassation.txtNoC.text")); // NOI18N

        lblYearC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblYearC.setText(bundle.getString("CourtJudgement_Cassation.lblYearC.text")); // NOI18N

        cboCourtTypeC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCourtTypeC.setModel(setCourtTypesModel());

        btnNewCase.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnNewCase.setText(bundle.getString("CourtJudgement_Cassation.btnNewCase.text")); // NOI18N
        btnNewCase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewCaseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jSearchCaseDataPanelLayout = new javax.swing.GroupLayout(jSearchCaseDataPanel);
        jSearchCaseDataPanel.setLayout(jSearchCaseDataPanelLayout);
        jSearchCaseDataPanelLayout.setHorizontalGroup(
            jSearchCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jSearchCaseDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jSearchCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jSearchCaseDataPanelLayout.createSequentialGroup()
                        .addGroup(jSearchCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboCourtTypeC, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCaseTypeC))
                        .addGap(18, 18, 18)
                        .addGroup(jSearchCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNoC)
                            .addGroup(jSearchCaseDataPanelLayout.createSequentialGroup()
                                .addComponent(txtNoC, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(slash3, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jSearchCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblYearC)
                            .addComponent(txtYearC, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jSearchCaseDataPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jSearchCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnNewCase, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSearchC, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(22, 22, 22))
        );
        jSearchCaseDataPanelLayout.setVerticalGroup(
            jSearchCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSearchCaseDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jSearchCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jSearchCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jSearchCaseDataPanelLayout.createSequentialGroup()
                            .addGroup(jSearchCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblNoC)
                                .addComponent(lblYearC))
                            .addGap(23, 23, 23))
                        .addGroup(jSearchCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(slash3)
                            .addComponent(txtNoC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtYearC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jSearchCaseDataPanelLayout.createSequentialGroup()
                        .addComponent(lblCaseTypeC)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCourtTypeC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(btnNewCase)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearchC)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        jCassationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle1.getString("CourtJudgement_Cassation.lblCassation.text"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 10))); // NOI18N

        lblCaseNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseNo.setText(bundle.getString("CourtJudgement_Cassation.lblCaseNo.text")); // NOI18N

        cboCourtType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCourtType.setModel(setCourtTypesModel_Cassation());

        cboCaseType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCaseType.setModel(setCaseTypesModel());

        slash.setText(bundle.getString("CourtJudgement_Cassation.slash.text")); // NOI18N

        cboCities.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCities.setModel(setCitiesModel());
        cboCities.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCitiesActionPerformed(evt);
            }
        });

        lblCaseType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseType.setText(bundle.getString("CourtJudgement_Cassation.lblCaseType.text")); // NOI18N

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
        lblOfficialDate.setText(bundle.getString("CourtJudgement_Cassation.lblOfficialDate.text")); // NOI18N

        lblCity.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCity.setText(bundle.getString("CourtJudgement_Cassation.lblCity.text")); // NOI18N

        lblCourtType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCourtType.setText(bundle.getString("CourtJudgement_Cassation.lblCourtType.text")); // NOI18N

        lblYear.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblYear.setText(bundle.getString("CourtJudgement_Cassation.lblYear.text")); // NOI18N

        lblLanguage.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblLanguage.setText(bundle.getString("CourtJudgement_Cassation.lblLanguage.text")); // NOI18N
        lblLanguage.setName("lbl.BungeniLanguageID"); // NOI18N

        txtYear.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtYearActionPerformed(evt);
            }
        });

        cboDomain.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboDomain.setModel(setDomainsModel());

        cboRegion.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboRegion.setModel(setRegionsModel());
        cboRegion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRegionActionPerformed(evt);
            }
        });

        lblDomain.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblDomain.setText(bundle1.getString("CourtJudgementMetadata.lblDomain.text")); // NOI18N

        chbJudgeChecked.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        chbJudgeChecked.setText(bundle.getString("CourtJudgement_Cassation.chbJudgeChecked.text")); // NOI18N
        chbJudgeChecked.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbJudgeCheckedActionPerformed(evt);
            }
        });

        lblRegion.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblRegion.setText(bundle.getString("CourtJudgement_Cassation.lblRegion.text")); // NOI18N

        lblImportance.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblImportance.setText(bundle1.getString("CourtJudgementMetadata.lblImportance.text")); // NOI18N

        txtImportance.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtImportance.setText(bundle.getString("CourtJudgement_Cassation.txtImportance.text")); // NOI18N

        lblSaveCassation.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSaveCassation.setText(bundle.getString("CourtJudgement_Cassation.lblSaveCassation.text")); // NOI18N
        lblSaveCassation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblSaveCassationActionPerformed(evt);
            }
        });

        lblCassationId.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCassationId.setText(bundle.getString("CourtJudgement_Cassation.lblCassationId.text")); // NOI18N

        javax.swing.GroupLayout jCassationPanelLayout = new javax.swing.GroupLayout(jCassationPanel);
        jCassationPanel.setLayout(jCassationPanelLayout);
        jCassationPanelLayout.setHorizontalGroup(
            jCassationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jCassationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jCassationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jCassationPanelLayout.createSequentialGroup()
                        .addGroup(jCassationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jCassationPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lblSaveCassation, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jCassationPanelLayout.createSequentialGroup()
                                .addGroup(jCassationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboCities, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dt_official_date, javax.swing.GroupLayout.DEFAULT_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblOfficialDate)
                                    .addComponent(lblCity, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jCassationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblImportance)
                                    .addComponent(txtImportance, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(chbJudgeChecked))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jCassationPanelLayout.createSequentialGroup()
                        .addGroup(jCassationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboCourtType, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCourtType, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jCassationPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jCassationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCaseType)
                                    .addComponent(cboCaseType, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(lblCassationId, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jCassationPanelLayout.createSequentialGroup()
                                .addGroup(jCassationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCaseNo)
                                    .addComponent(txtCaseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addComponent(slash, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jCassationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblYear)
                                    .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblLanguage))
                        .addGap(18, 18, 18)
                        .addGroup(jCassationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboDomain, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDomain))
                        .addGap(5, 5, 5))))
        );
        jCassationPanelLayout.setVerticalGroup(
            jCassationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jCassationPanelLayout.createSequentialGroup()
                .addComponent(lblCassationId, javax.swing.GroupLayout.DEFAULT_SIZE, 14, Short.MAX_VALUE)
                .addGap(4, 4, 4)
                .addComponent(lblLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(jCassationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jCassationPanelLayout.createSequentialGroup()
                        .addComponent(lblCourtType)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCourtType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jCassationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboCaseType, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jCassationPanelLayout.createSequentialGroup()
                                .addComponent(lblCaseType)
                                .addGap(26, 26, 26))))
                    .addGroup(jCassationPanelLayout.createSequentialGroup()
                        .addComponent(lblDomain)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboDomain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblRegion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboRegion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jCassationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jCassationPanelLayout.createSequentialGroup()
                        .addComponent(lblCity)
                        .addGap(7, 7, 7)
                        .addComponent(cboCities, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lblOfficialDate)
                        .addGap(6, 6, 6)
                        .addComponent(dt_official_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jCassationPanelLayout.createSequentialGroup()
                        .addComponent(chbJudgeChecked)
                        .addGap(18, 18, 18)
                        .addGroup(jCassationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jCassationPanelLayout.createSequentialGroup()
                                .addComponent(lblImportance)
                                .addGap(25, 25, 25))
                            .addComponent(txtImportance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jCassationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCaseNo)
                    .addComponent(lblYear))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jCassationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCaseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(slash)
                    .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSaveCassation))
        );

        jSearchAppealDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("CourtJudgement_Cassation.jSearchAppealDataPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 10))); // NOI18N
        jSearchAppealDataPanel.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jSearchAppealDataPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jSearchAppealDataPanelComponentHidden(evt);
            }
        });

        lblCaseNoA.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseNoA.setText(bundle.getString("CourtJudgement_Cassation.lblCaseNoA.text")); // NOI18N

        slash4.setText(bundle.getString("CourtJudgement_Cassation.slash4.text")); // NOI18N

        cboCourtTypeA.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCourtTypeA.setModel(setCourtTypesModel_Appeal());

        txtYearA.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblYearA.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblYearA.setText(bundle.getString("CourtJudgement_Cassation.lblYearA.text")); // NOI18N

        lblCourtTypeA.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCourtTypeA.setText(bundle.getString("CourtJudgement_Cassation.lblCourtTypeA.text")); // NOI18N

        txtCaseNumberA.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        btnSearchA.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnSearchA.setText(bundle.getString("CourtJudgement_Cassation.btnSearchA.text")); // NOI18N
        btnSearchA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchAActionPerformed(evt);
            }
        });

        btnNewAppeal.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnNewAppeal.setText(bundle.getString("CourtJudgement_Cassation.btnNewAppeal.text")); // NOI18N
        btnNewAppeal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewAppealActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jSearchAppealDataPanelLayout = new javax.swing.GroupLayout(jSearchAppealDataPanel);
        jSearchAppealDataPanel.setLayout(jSearchAppealDataPanelLayout);
        jSearchAppealDataPanelLayout.setHorizontalGroup(
            jSearchAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jSearchAppealDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jSearchAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboCourtTypeA, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCourtTypeA))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jSearchAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCaseNoA)
                    .addComponent(txtCaseNumberA, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(slash4, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jSearchAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtYearA, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblYearA))
                .addGap(30, 30, 30))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jSearchAppealDataPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jSearchAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSearchA, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNewAppeal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jSearchAppealDataPanelLayout.setVerticalGroup(
            jSearchAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSearchAppealDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jSearchAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jSearchAppealDataPanelLayout.createSequentialGroup()
                        .addGroup(jSearchAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCaseNoA)
                            .addComponent(lblYearA))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jSearchAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCaseNumberA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(slash4)
                            .addComponent(txtYearA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jSearchAppealDataPanelLayout.createSequentialGroup()
                        .addComponent(lblCourtTypeA)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCourtTypeA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(btnNewAppeal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearchA))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jSearchCaseDataPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jAppealDataPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSearchAppealDataPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCassationPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCaseDataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jCassationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSearchAppealDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jAppealDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSearchCaseDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCaseDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(154, Short.MAX_VALUE))
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

    private void jAppealDataPanelComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jAppealDataPanelComponentHidden
        // TODO add your handling code here:
    }//GEN-LAST:event_jAppealDataPanelComponentHidden

    private void btnNewCase1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewCase1ActionPerformed
        // TODO add your handling code here:
        cboCourtType2.setSelectedIndex(0);
        cboCities2.setSelectedIndex(0);
        cboCaseType2.setSelectedIndex(0);
        cboDomain2.setSelectedIndex(0);
        cboRegion2.setSelectedIndex(0);
        dt_official_date2.setDate(null);
        txtCaseNumber2.setText(null);
        txtYear2.setText(null);
        jSearchCaseDataPanel.setVisible(false);
        jCaseDataPanel.setVisible(true);
    }//GEN-LAST:event_btnNewCase1ActionPerformed

    private void btnSearchCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchCActionPerformed
        // TODO add your handling code here:
        CourtType courtTypeObj = CourtTypesListC.get(this.cboCourtTypeC.getSelectedIndex());

        ResultSet rs;
        CJ_Main mainObj = null;

        try {
            String sqlStm = "SELECT [CJ_Main_ID] FROM [CJ_Main] WHERE [CJ_Main_NumNo]=" + txtNoC.getText()
                    + " and [CJ_Main_NumYr]=" + txtYearC.getText()
                    + " and [CJ_Main_CJ_CourtTypes_ID]=" + courtTypeObj.getCourtTypeID()
                    + "";

            rs = conStmt.executeQuery(sqlStm);
            if (rs.next()) {
                try {
                    String queryMain = "SELECT [CJ_Main_NumNo], [CJ_Main_NumYr], [CJ_Main_DecDate], [CJ_Main_CJ_CourtTypes_ID], [CJ_Main_CJ_Cities_ID] FROM [CJ_Main] WHERE [CJ_Main_ID]=" + rs.getString(1) + "";
                    ResultSet resMain = conStmt.executeQuery(queryMain);

                    while (resMain.next()) {
                        mainObj = new CJ_Main(rs.getString(1), resMain.getString(1), resMain.getString(2), new SimpleDateFormat("yyyy-MM-dd").parse(resMain.getString(3)), resMain.getString(4), resMain.getString(5));
                    }
                } catch (ParseException ex) {
                    log.error("SQL Exception", ex);
                }

                cboCourtType1.setSelectedItem(courtTypeObj);

                int i = Integer.parseInt(mainObj.getCJ_Main_CJ_Cities_ID());
                cboCities2.setSelectedIndex(i - 1);
                dt_official_date2.setDate(mainObj.getCJ_Main_DecDate());
                txtCaseNumber2.setText(mainObj.getCJ_Main_NumNo());
                txtYear2.setText(mainObj.getCJ_Main_NumYr());
                jSearchCaseDataPanel.setVisible(false);
                jCaseDataPanel.setVisible(true);
            } else {
                MessageBox.OK("Recodr not found");
            }

        } catch (SQLException ex) {
                log.error("SQL Exception", ex);
        }
    }//GEN-LAST:event_btnSearchCActionPerformed

    private void btnNewCaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewCaseActionPerformed
        // TODO add your handling code here:
        cboCourtType2.setSelectedIndex(0);
        cboCities2.setSelectedIndex(0);
        cboCaseType2.setSelectedIndex(0);
        cboDomain2.setSelectedIndex(0);
        cboRegion2.setSelectedIndex(0);
        dt_official_date2.setDate(null);
        txtCaseNumber2.setText(null);
        txtYear2.setText(null);
        jSearchCaseDataPanel.setVisible(false);
        jCaseDataPanel.setVisible(true);
    }//GEN-LAST:event_btnNewCaseActionPerformed

    private void jSearchAppealDataPanelComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jSearchAppealDataPanelComponentHidden
        // TODO add your handling code here:
    }//GEN-LAST:event_jSearchAppealDataPanelComponentHidden

    private void btnNewAppealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewAppealActionPerformed
        // TODO add your handling code here:
        cboCourtType1.setSelectedIndex(0);
        cboCities1.setSelectedIndex(0);
        cboCaseType1.setSelectedIndex(0);
        cboDomain1.setSelectedIndex(0);
        cboRegion1.setSelectedIndex(0);
        dt_official_date1.setDate(null);
        txtCaseNumber1.setText(null);
        txtYear1.setText(null);
        jSearchAppealDataPanel.setVisible(false);
        jAppealDataPanel.setVisible(true);
    }//GEN-LAST:event_btnNewAppealActionPerformed

    private void btnSearchAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchAActionPerformed
        // TODO add your handling code here:
        CourtType courtTypeObj = CourtTypesListA.get(this.cboCourtTypeA.getSelectedIndex());

        ResultSet rs;
        CJ_Main mainObj = null;

        try {
            String sqlStm = "SELECT [CJ_Main_ID] FROM [CJ_Main] WHERE [CJ_Main_NumNo]=" + txtCaseNumberA.getText()
                    + " and [CJ_Main_NumYr]=" + txtYearA.getText()
                    + " and [CJ_Main_CJ_CourtTypes_ID]=" + courtTypeObj.getCourtTypeID()
                    + "";

            rs = conStmt.executeQuery(sqlStm);
            if (rs.next()) {
                try {
                    String queryMain = "SELECT [CJ_Main_NumNo], [CJ_Main_NumYr], [CJ_Main_DecDate], [CJ_Main_CJ_CourtTypes_ID], [CJ_Main_CJ_Cities_ID] FROM [CJ_Main] WHERE [CJ_Main_ID]=" + rs.getString(1) + "";
                    ResultSet resMain = conStmt.executeQuery(queryMain);


                    while (resMain.next()) {
                        mainObj = new CJ_Main(rs.getString(1), resMain.getString(1), resMain.getString(2), new SimpleDateFormat("yyyy-MM-dd").parse(resMain.getString(3)), resMain.getString(4), resMain.getString(5));
                    }
                } catch (ParseException ex) {
                    log.error("SQL Exception", ex);
                }

                cboCourtType1.setSelectedItem(courtTypeObj);

                int i = Integer.parseInt(mainObj.getCJ_Main_CJ_Cities_ID());
                cboCities1.setSelectedIndex(i - 1);
                dt_official_date1.setDate(mainObj.getCJ_Main_DecDate());
                txtCaseNumber1.setText(mainObj.getCJ_Main_NumNo());
                txtYear1.setText(mainObj.getCJ_Main_NumYr());
                jSearchAppealDataPanel.setVisible(false);
                jAppealDataPanel.setVisible(true);
            } else {
                MessageBox.OK("Recodr not found");
            }

        } catch (SQLException ex) {
                log.error("SQL Exception", ex);
        }

    }//GEN-LAST:event_btnSearchAActionPerformed

    private void btnNewAppeal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewAppeal1ActionPerformed
        // TODO add your handling code here:
        cboCourtType1.setSelectedIndex(0);
        cboCities1.setSelectedIndex(0);
        cboCaseType1.setSelectedIndex(0);
        cboDomain1.setSelectedIndex(0);
        cboRegion1.setSelectedIndex(0);
        dt_official_date1.setDate(null);
        txtCaseNumber1.setText(null);
        txtYear1.setText(null);
        jSearchAppealDataPanel.setVisible(false);
        jAppealDataPanel.setVisible(true);
    }//GEN-LAST:event_btnNewAppeal1ActionPerformed

    private void lblSaveCassationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblSaveCassationActionPerformed
        // TODO add your handling code here:

        int CourtTypeId = this.cboCourtType.getSelectedIndex() + 1;
        int DomainId = this.cboDomain.getSelectedIndex() + 1;
        int RegionId = this.cboRegion.getSelectedIndex() + 1;
        int CaseTypeId = this.cboCaseType.getSelectedIndex() + 1;
        int CityId = this.cboCities.getSelectedIndex() + 1;
        String sCaseNo = this.txtCaseNumber.getText();
        String sDate = dbdtformatter.format(dt_official_date.getDate());
        String sYear = this.txtYear.getText();
        String sImportance = this.txtImportance.getText();

        ResultSet rs;

        try {
            String sqlStm = "INSERT INTO [CJ_Main] ([CJ_Main_CJ_CaseTypes_ID], [CJ_Main_CJ_CourtTypes_ID], [CJ_Main_CJ_Regions_ID], "
                    + "[CJ_Main_CJ_Cities_ID], [CJ_Main_CJ_Domains_ID], [CJ_Main_Importance], [CJ_Main_DecDate], "
                    + "[CJ_Main_NumNo], [CJ_Main_NumYr])"
                    + "VALUES(" + CaseTypeId + "," + CourtTypeId + "," + RegionId
                    + "," + CityId + "," + DomainId + "," + sImportance + ",'" + sDate + "'," + sCaseNo + ",'" + sYear
                    + "')";

            rs = conStmt.executeQuery(sqlStm);

            sqlStm = "SELECT max(CJ_Main_ID) FROM [CJ_Main] ";
            rs = conStmt.executeQuery(sqlStm);
            if (rs.next()) {
                lblCassationId.setText(rs.getString(1));
            } else {
                MessageBox.OK("Recodr not found");
            }

        } catch (SQLException ex) {
            log.error("SQL Exception", ex);
        }

    }//GEN-LAST:event_lblSaveCassationActionPerformed

    private void lblSaveAppealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblSaveAppealActionPerformed
        // TODO add your handling code here:
        int CourtTypeId = this.cboCourtType.getSelectedIndex() + 1;
        int DomainId = this.cboDomain.getSelectedIndex() + 1;
        int RegionId = this.cboRegion.getSelectedIndex() + 1;
        int CaseTypeId = this.cboCaseType.getSelectedIndex() + 1;
        int CityId = this.cboCities.getSelectedIndex() + 1;
        String sCaseNo = this.txtCaseNumber.getText();
        String sDate = dbdtformatter.format(dt_official_date.getDate());
        String sYear = this.txtYear.getText();
        String sImportance = this.txtImportance.getText();

        ResultSet rs;

        try {
            String sqlStm = "INSERT INTO [CJ_Main] ([CJ_Main_CJ_CaseTypes_ID], [CJ_Main_CJ_CourtTypes_ID], [CJ_Main_CJ_Regions_ID], "
                    + "[CJ_Main_CJ_Cities_ID], [CJ_Main_CJ_Domains_ID], [CJ_Main_Importance], [CJ_Main_DecDate], "
                    + "[CJ_Main_NumNo], [CJ_Main_NumYr])"
                    + "VALUES(" + CaseTypeId + "," + CourtTypeId + "," + RegionId
                    + "," + CityId + "," + DomainId + "," + sImportance + ",'" + sDate + "'," + sCaseNo + ",'" + sYear
                    + "')";

            rs = conStmt.executeQuery(sqlStm);

            sqlStm = "SELECT max(CJ_Main_ID) FROM [CJ_Main] ";
            rs = conStmt.executeQuery(sqlStm);
            if (rs.next()) {
                lblAppealId.setText(rs.getString(1));
            } else {
                MessageBox.OK("Recodr not found");
            }

        } catch (SQLException ex) {
                log.error("SQL Exception", ex);

        }

    }//GEN-LAST:event_lblSaveAppealActionPerformed

    private void lblSaveCaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblSaveCaseActionPerformed
        // TODO add your handling code here:
        int CourtTypeId = this.cboCourtType.getSelectedIndex() + 1;
        int DomainId = this.cboDomain.getSelectedIndex() + 1;
        int RegionId = this.cboRegion.getSelectedIndex() + 1;
        int CaseTypeId = this.cboCaseType.getSelectedIndex() + 1;
        int CityId = this.cboCities.getSelectedIndex() + 1;
        String sCaseNo = this.txtCaseNumber.getText();
        String sDate = dbdtformatter.format(dt_official_date.getDate());
        String sYear = this.txtYear.getText();
        String sImportance = this.txtImportance.getText();

        ResultSet rs;

        try {
            String sqlStm = "INSERT INTO [CJ_Main] ([CJ_Main_CJ_CaseTypes_ID], [CJ_Main_CJ_CourtTypes_ID], [CJ_Main_CJ_Regions_ID], "
                    + "[CJ_Main_CJ_Cities_ID], [CJ_Main_CJ_Domains_ID], [CJ_Main_Importance], [CJ_Main_DecDate], "
                    + "[CJ_Main_NumNo], [CJ_Main_NumYr])"
                    + "VALUES(" + CaseTypeId + "," + CourtTypeId + "," + RegionId
                    + "," + CityId + "," + DomainId + "," + sImportance + ",'" + sDate + "'," + sCaseNo + ",'" + sYear
                    + "')";

            rs = conStmt.executeQuery(sqlStm);

            sqlStm = "SELECT max(CJ_Main_ID) FROM [CJ_Main] ";
            rs = conStmt.executeQuery(sqlStm);
            if (rs.next()) {
                lblCaseId.setText(rs.getString(1));
            } else {
                MessageBox.OK("Recodr not found");
            }

        } catch (SQLException ex) {
                log.error("SQL Exception", ex);
        }
    }//GEN-LAST:event_lblSaveCaseActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNewAppeal;
    private javax.swing.JButton btnNewAppeal1;
    private javax.swing.JButton btnNewCase;
    private javax.swing.JButton btnNewCase1;
    private javax.swing.JButton btnSearchA;
    private javax.swing.JButton btnSearchC;
    private javax.swing.JComboBox cboCaseType;
    private javax.swing.JComboBox cboCaseType1;
    private javax.swing.JComboBox cboCaseType2;
    private javax.swing.JComboBox cboCities;
    private javax.swing.JComboBox cboCities1;
    private javax.swing.JComboBox cboCities2;
    private javax.swing.JComboBox cboCourtType;
    private javax.swing.JComboBox cboCourtType1;
    private javax.swing.JComboBox cboCourtType2;
    private javax.swing.JComboBox cboCourtTypeA;
    private javax.swing.JComboBox cboCourtTypeC;
    private javax.swing.JComboBox cboDomain;
    private javax.swing.JComboBox cboDomain1;
    private javax.swing.JComboBox cboDomain2;
    private javax.swing.JComboBox cboLanguage;
    private javax.swing.JComboBox cboLanguage1;
    private javax.swing.JComboBox cboRegion;
    private javax.swing.JComboBox cboRegion1;
    private javax.swing.JComboBox cboRegion2;
    private javax.swing.JCheckBox chbJudgeChecked;
    private javax.swing.JCheckBox chbJudgeChecked1;
    private org.jdesktop.swingx.JXDatePicker dt_official_date;
    private org.jdesktop.swingx.JXDatePicker dt_official_date1;
    private org.jdesktop.swingx.JXDatePicker dt_official_date2;
    private javax.swing.JPanel jAppealDataPanel;
    private javax.swing.JPanel jCaseDataPanel;
    private javax.swing.JPanel jCassationPanel;
    private javax.swing.JPanel jSearchAppealDataPanel;
    private javax.swing.JPanel jSearchCaseDataPanel;
    private javax.swing.JLabel lblAppealId;
    private javax.swing.JLabel lblCaseId;
    private javax.swing.JLabel lblCaseNo;
    private javax.swing.JLabel lblCaseNo1;
    private javax.swing.JLabel lblCaseNo2;
    private javax.swing.JLabel lblCaseNoA;
    private javax.swing.JLabel lblCaseType;
    private javax.swing.JLabel lblCaseType1;
    private javax.swing.JLabel lblCaseType2;
    private javax.swing.JLabel lblCaseTypeC;
    private javax.swing.JLabel lblCassationId;
    private javax.swing.JLabel lblCity;
    private javax.swing.JLabel lblCity1;
    private javax.swing.JLabel lblCity2;
    private javax.swing.JLabel lblCourtType;
    private javax.swing.JLabel lblCourtType1;
    private javax.swing.JLabel lblCourtType2;
    private javax.swing.JLabel lblCourtTypeA;
    private javax.swing.JLabel lblDomain;
    private javax.swing.JLabel lblDomain1;
    private javax.swing.JLabel lblDomain2;
    private javax.swing.JLabel lblImportance;
    private javax.swing.JLabel lblImportance1;
    private javax.swing.JLabel lblLanguage;
    private javax.swing.JLabel lblLanguage1;
    private javax.swing.JLabel lblNoC;
    private javax.swing.JLabel lblOfficialDate;
    private javax.swing.JLabel lblOfficialDate1;
    private javax.swing.JLabel lblOfficialDate2;
    private javax.swing.JLabel lblRegion;
    private javax.swing.JLabel lblRegion1;
    private javax.swing.JLabel lblRegion2;
    private javax.swing.JButton lblSaveAppeal;
    private javax.swing.JButton lblSaveCase;
    private javax.swing.JButton lblSaveCassation;
    private javax.swing.JLabel lblYear;
    private javax.swing.JLabel lblYear1;
    private javax.swing.JLabel lblYear2;
    private javax.swing.JLabel lblYearA;
    private javax.swing.JLabel lblYearC;
    private javax.swing.JLabel slash;
    private javax.swing.JLabel slash1;
    private javax.swing.JLabel slash2;
    private javax.swing.JLabel slash3;
    private javax.swing.JLabel slash4;
    private javax.swing.JTextField txtCaseNumber;
    private javax.swing.JTextField txtCaseNumber1;
    private javax.swing.JTextField txtCaseNumber2;
    private javax.swing.JTextField txtCaseNumberA;
    private javax.swing.JTextField txtImportance;
    private javax.swing.JTextField txtImportance1;
    private javax.swing.JTextField txtNoC;
    private javax.swing.JTextField txtYear;
    private javax.swing.JTextField txtYear1;
    private javax.swing.JTextField txtYear2;
    private javax.swing.JTextField txtYearA;
    private javax.swing.JTextField txtYearC;
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
//                put(lblDomain.getText().replace("*", ""), cboDomain);
//                put(lblCaseType.getText().replace("*", ""), cboCaseType);
//                put(lblCaseNo.getText().replace("*", ""), txtCaseNumber);
//                put(lblCity.getText().replace("*", ""), cboCities);
//                put(lblOfficialDate.getText().replace("*", ""), dt_official_date);
//                put(lblYear.getText().replace("*", ""), txtYear);
            }
        });
        return super.validateSelectedMetadata(spf);
    }

    public void getDBValues(ArrayList values) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
