/*
 * DebateRecordMetadata.java
 *
 * Created on November 4, 2008, 1:43 PM
 */
package org.birzeit.editor.metadata.editors;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
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
import org.bungeni.extutils.FrameLauncher;
import org.bungeni.extutils.MessageBox;
import org.bungeni.utils.BungeniFileSavePathFormat;
import org.bungeni.utils.BungeniFrame;

/**
 *
 * @author undesa
 */
public class CourtJudgement_HighCourt_Generalbody extends BaseEditorDocMetadataDialog {

    private static java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/birzeit/editor/metadata/editors/Bundle"); // NOI18N
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CourtJudgement_HighCourt_Generalbody.class.getName());
    JudgementMetadataModel docMetaModel = new JudgementMetadataModel();
    private SimpleDateFormat dtformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
    private SimpleDateFormat dbdtformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("dataBaseDateFormat"));
    private SimpleDateFormat savedtformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataSaveDateFormat"));
    private String dbName = "CourtJudgments2007_test";
    private ArrayList<CaseType> CaseTypesList = new ArrayList<CaseType>();
    private ArrayList<Domains> DomainsList = new ArrayList<Domains>();
    private ArrayList<JudgementRegion> JudgementRegionsList = new ArrayList<JudgementRegion>();
    private ArrayList<City> CitiesList = new ArrayList<City>();
    private ArrayList<CourtType> CourtTypesListA = new ArrayList<CourtType>();
    private ArrayList<CourtType> CourtTypesListC = new ArrayList<CourtType>();
    private ArrayList<CourtType> CourtTypesList = new ArrayList<CourtType>();
    private Connection con = ConnectorFunctions.ConnectMMSM(dbName);
    private Statement conStmt;
    private int CJ_Main_ID;

    public CourtJudgement_HighCourt_Generalbody() {
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
//            if (!CommonStringFunctions.emptyOrNull(sFamily)) {
//                this.cboCourtType.setSelectedItem(sFamily);
//            }
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

        cboLanguage2.setModel(new DefaultComboBoxModel(languageCodes));
        this.cboLanguage2.setSelectedItem(findLanguageCodeAlpha2(Locale.getDefault().getLanguage()));

        cboLanguage1.setModel(new DefaultComboBoxModel(languageCodes));
        this.cboLanguage1.setSelectedItem(findLanguageCodeAlpha2(Locale.getDefault().getLanguage()));
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
            //   CourtType sCourtType = CourtTypesList.get(this.cboCourtType.getSelectedIndex());
            Domains sDomain = DomainsList.get(this.cboDomain.getSelectedIndex());
            JudgementRegion sRegion = JudgementRegionsList.get(this.cboRegion.getSelectedIndex());
            CaseType sCaseType = CaseTypesList.get(this.cboCaseType.getSelectedIndex());
            City sCity = CitiesList.get(this.cboCities.getSelectedIndex());
            String sCaseNo = this.txtCaseNumber.getText();
            String sDate = dbdtformatter.format(dt_official_date.getDate());
            String sYear = this.txtYear.getText();
            String sImportance = this.txtImportance.getText();

            docMetaModel.updateItem("BungeniLanguageCode", selLanguage.getLanguageCodeAlpha2());
            //   docMetaModel.updateItem("BungeniCourtType", sCourtType.toString());
            docMetaModel.updateItem("BungeniDomain", sDomain.toString());
            docMetaModel.updateItem("BungeniRegion", sRegion.toString());
            docMetaModel.updateItem("BungeniCaseType", sCaseType.toString());
            docMetaModel.updateItem("BungeniCity", sCity.toString());

            //    docMetaModel.updateItem("BungeniCourtTypeID", sCourtType.getCourtTypeID());
            docMetaModel.updateItem("BungeniDomainID", sDomain.getDomainID());
            docMetaModel.updateItem("BungeniRegionID", sRegion.getJudgementRegionID());
            docMetaModel.updateItem("BungeniCaseTypeID", sCaseType.getCaseTypeID());
            docMetaModel.updateItem("BungeniCityID", sCity.getCityID());


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

        jAppealDataPanel = new javax.swing.JPanel();
        lblCaseNo1 = new javax.swing.JLabel();
        slash2 = new javax.swing.JLabel();
        cboCaseType2 = new javax.swing.JComboBox();
        cboCourtType2 = new javax.swing.JComboBox();
        cboCities2 = new javax.swing.JComboBox();
        lblRegion1 = new javax.swing.JLabel();
        cboRegion2 = new javax.swing.JComboBox();
        cboDomain2 = new javax.swing.JComboBox();
        txtYear2 = new javax.swing.JTextField();
        lblDomain1 = new javax.swing.JLabel();
        lblYear1 = new javax.swing.JLabel();
        chbJudgeChecked2 = new javax.swing.JCheckBox();
        lblCity1 = new javax.swing.JLabel();
        lblCaseType1 = new javax.swing.JLabel();
        lblOfficialDate1 = new javax.swing.JLabel();
        dt_official_date2 = new org.jdesktop.swingx.JXDatePicker();
        lblLanguage1 = new javax.swing.JLabel();
        txtCaseNumber2 = new javax.swing.JTextField();
        lblCourtType1 = new javax.swing.JLabel();
        cboLanguage2 = new javax.swing.JComboBox();
        lblImportance1 = new javax.swing.JLabel();
        txtImportance2 = new javax.swing.JTextField();
        btnNewAppeal1 = new javax.swing.JButton();
        lblSaveAppeal = new javax.swing.JButton();
        lblAppealId = new javax.swing.JLabel();
        jCaseDataPanel = new javax.swing.JPanel();
        lblCaseNo2 = new javax.swing.JLabel();
        slash1 = new javax.swing.JLabel();
        cboCaseType3 = new javax.swing.JComboBox();
        cboCourtType3 = new javax.swing.JComboBox();
        cboCities3 = new javax.swing.JComboBox();
        lblRegion2 = new javax.swing.JLabel();
        cboRegion3 = new javax.swing.JComboBox();
        cboDomain3 = new javax.swing.JComboBox();
        txtYear3 = new javax.swing.JTextField();
        lblDomain2 = new javax.swing.JLabel();
        lblYear2 = new javax.swing.JLabel();
        lblCity2 = new javax.swing.JLabel();
        lblCaseType2 = new javax.swing.JLabel();
        lblOfficialDate2 = new javax.swing.JLabel();
        dt_official_date3 = new org.jdesktop.swingx.JXDatePicker();
        txtCaseNumber3 = new javax.swing.JTextField();
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
        jHighCourtGeneralBodyPanel = new javax.swing.JPanel();
        lblCaseNo = new javax.swing.JLabel();
        cboCaseType = new javax.swing.JComboBox();
        slash = new javax.swing.JLabel();
        cboCities = new javax.swing.JComboBox();
        lblCaseType = new javax.swing.JLabel();
        dt_official_date = new org.jdesktop.swingx.JXDatePicker();
        txtCaseNumber = new javax.swing.JTextField();
        cboLanguage = new javax.swing.JComboBox();
        lblOfficialDate = new javax.swing.JLabel();
        lblCity = new javax.swing.JLabel();
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
        lblSaveMain = new javax.swing.JButton();
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
        jSearchCassationDataPanel = new javax.swing.JPanel();
        lblCaseNoA1 = new javax.swing.JLabel();
        slash5 = new javax.swing.JLabel();
        cboCourtTypeCa = new javax.swing.JComboBox();
        txtYearCa = new javax.swing.JTextField();
        lblYearA1 = new javax.swing.JLabel();
        lblCourtTypeA1 = new javax.swing.JLabel();
        txtCaseNumberCa = new javax.swing.JTextField();
        btnSearchCa = new javax.swing.JButton();
        btnNewCassation = new javax.swing.JButton();
        jCassationDataPanel = new javax.swing.JPanel();
        lblCaseNo3 = new javax.swing.JLabel();
        slash6 = new javax.swing.JLabel();
        cboCaseType1 = new javax.swing.JComboBox();
        cboCourtType1 = new javax.swing.JComboBox();
        cboCities1 = new javax.swing.JComboBox();
        lblRegion3 = new javax.swing.JLabel();
        cboRegion1 = new javax.swing.JComboBox();
        cboDomain1 = new javax.swing.JComboBox();
        txtYear1 = new javax.swing.JTextField();
        lblDomain3 = new javax.swing.JLabel();
        lblYear3 = new javax.swing.JLabel();
        chbJudgeChecked1 = new javax.swing.JCheckBox();
        lblCity3 = new javax.swing.JLabel();
        lblCaseType3 = new javax.swing.JLabel();
        lblOfficialDate3 = new javax.swing.JLabel();
        dt_official_date1 = new org.jdesktop.swingx.JXDatePicker();
        lblLanguage2 = new javax.swing.JLabel();
        txtCaseNumber1 = new javax.swing.JTextField();
        lblCourtType3 = new javax.swing.JLabel();
        cboLanguage1 = new javax.swing.JComboBox();
        lblImportance2 = new javax.swing.JLabel();
        txtImportance1 = new javax.swing.JTextField();
        btnNewCassation1 = new javax.swing.JButton();
        lblSaveAppeal1 = new javax.swing.JButton();
        lblAppealId1 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(400, 1500));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/birzeit/editor/metadata/editors/Bundle"); // NOI18N
        jAppealDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("CourtJudgement_HighCourt_Generalbody.jAppealDataPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 10))); // NOI18N
        jAppealDataPanel.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jAppealDataPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jAppealDataPanelComponentHidden(evt);
            }
        });
        jAppealDataPanel.setVisible(false);

        lblCaseNo1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseNo1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCaseNo1.text")); // NOI18N

        slash2.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.slash2.text")); // NOI18N

        cboCaseType2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCaseType2.setModel(setCaseTypesModel());

        cboCourtType2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCourtType2.setModel(setCourtTypesModel_Appeal());

        cboCities2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCities2.setModel(setCitiesModel());

        lblRegion1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblRegion1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblRegion1.text")); // NOI18N

        cboRegion2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboRegion2.setModel(setRegionsModel());

        cboDomain2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboDomain2.setModel(setDomainsModel());

        txtYear2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblDomain1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblDomain1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblDomain1.text")); // NOI18N

        lblYear1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblYear1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblYear1.text")); // NOI18N

        chbJudgeChecked2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        chbJudgeChecked2.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.chbJudgeChecked2.text")); // NOI18N

        lblCity1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCity1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCity1.text")); // NOI18N

        lblCaseType1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseType1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCaseType1.text")); // NOI18N

        lblOfficialDate1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblOfficialDate1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblOfficialDate1.text")); // NOI18N

        dt_official_date2.setFormats("dd-MM-yyyy");
        dt_official_date2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblLanguage1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblLanguage1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblLanguage1.text")); // NOI18N
        lblLanguage1.setName("lbl.BungeniLanguageID"); // NOI18N

        txtCaseNumber2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblCourtType1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCourtType1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCourtType1.text")); // NOI18N

        cboLanguage2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboLanguage2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLanguage2.setName("fld.BungeniLanguageID"); // NOI18N

        lblImportance1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblImportance1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblImportance1.text")); // NOI18N

        txtImportance2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtImportance2.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.txtImportance2.text")); // NOI18N

        btnNewAppeal1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnNewAppeal1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.btnNewAppeal1.text")); // NOI18N
        btnNewAppeal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewAppeal1ActionPerformed(evt);
            }
        });

        lblSaveAppeal.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSaveAppeal.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblSaveAppeal.text")); // NOI18N
        lblSaveAppeal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblSaveAppealActionPerformed(evt);
            }
        });

        lblAppealId.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblAppealId.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblAppealId.text")); // NOI18N

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
                            .addComponent(txtCaseNumber2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(slash2, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                                .addComponent(lblYear1)
                                .addContainerGap(258, Short.MAX_VALUE))
                            .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                                .addComponent(txtYear2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblSaveAppeal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnNewAppeal1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18))))
                    .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                        .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                                .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboCourtType2, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCourtType1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboCaseType2, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCaseType1))
                                .addGap(18, 18, 18)
                                .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cboDomain2, 0, 151, Short.MAX_VALUE)
                                        .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                                            .addGap(1, 1, 1)
                                            .addComponent(lblDomain1)))
                                    .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cboRegion2, 0, 151, Short.MAX_VALUE)
                                        .addComponent(lblRegion1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(lblAppealId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboLanguage2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                                .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboCities2, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCity1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblLanguage1)
                                    .addComponent(dt_official_date2, javax.swing.GroupLayout.DEFAULT_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblOfficialDate1))
                                .addGap(18, 18, 18)
                                .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chbJudgeChecked2)
                                    .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblImportance1)
                                            .addComponent(txtImportance2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addContainerGap(49, Short.MAX_VALUE))))
        );
        jAppealDataPanelLayout.setVerticalGroup(
            jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAppealId, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLanguage1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(cboLanguage2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                        .addComponent(lblCourtType1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCourtType2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                        .addComponent(lblDomain1)
                        .addGap(9, 9, 9)
                        .addComponent(cboDomain2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                        .addComponent(lblCaseType1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCaseType2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                        .addComponent(lblRegion1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboRegion2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblCity1)
                .addGap(3, 3, 3)
                .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                        .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboCities2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chbJudgeChecked2))
                        .addGap(7, 7, 7)
                        .addComponent(lblOfficialDate1)
                        .addGap(4, 4, 4)
                        .addComponent(dt_official_date2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txtImportance2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jAppealDataPanelLayout.createSequentialGroup()
                            .addComponent(lblImportance1)
                            .addGap(25, 25, 25))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jAppealDataPanelLayout.createSequentialGroup()
                        .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCaseNo1)
                            .addComponent(lblYear1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jAppealDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCaseNumber2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(slash2)
                            .addComponent(txtYear2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jAppealDataPanelLayout.createSequentialGroup()
                        .addComponent(btnNewAppeal1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSaveAppeal))))
        );

        jCaseDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("CourtJudgement_HighCourt_Generalbody.jCaseDataPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 10))); // NOI18N
        jCaseDataPanel.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jCaseDataPanel.setVisible(false);

        lblCaseNo2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseNo2.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCaseNo2.text")); // NOI18N

        slash1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.slash1.text")); // NOI18N

        cboCaseType3.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCaseType3.setModel(setCaseTypesModel());

        cboCourtType3.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCourtType3.setModel(setCourtTypesModel());

        cboCities3.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCities3.setModel(setCitiesModel());

        lblRegion2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblRegion2.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblRegion2.text")); // NOI18N

        cboRegion3.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboRegion3.setModel(setRegionsModel());

        cboDomain3.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboDomain3.setModel(setDomainsModel());

        txtYear3.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblDomain2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblDomain2.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblDomain2.text")); // NOI18N

        lblYear2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblYear2.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblYear2.text")); // NOI18N

        lblCity2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCity2.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCity2.text")); // NOI18N

        lblCaseType2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseType2.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCaseType2.text")); // NOI18N

        lblOfficialDate2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblOfficialDate2.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblOfficialDate2.text")); // NOI18N

        dt_official_date3.setFormats("dd-MM-yyyy");
        dt_official_date3.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        txtCaseNumber3.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblCourtType2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCourtType2.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCourtType2.text")); // NOI18N

        btnNewCase1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnNewCase1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.btnNewCase1.text")); // NOI18N
        btnNewCase1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewCase1ActionPerformed(evt);
            }
        });

        lblSaveCase.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSaveCase.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblSaveCase.text")); // NOI18N
        lblSaveCase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblSaveCaseActionPerformed(evt);
            }
        });

        lblCaseId.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseId.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCaseId.text")); // NOI18N

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
                    .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                        .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCaseNo2)
                            .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                                .addComponent(txtCaseNumber3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(slash1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(5, 5, 5)
                        .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblYear2)
                            .addComponent(txtYear3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(220, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jCaseDataPanelLayout.createSequentialGroup()
                        .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dt_official_date3, javax.swing.GroupLayout.DEFAULT_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblOfficialDate2)
                            .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboCourtType3, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCourtType2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(lblCaseType2)
                                .addComponent(cboCaseType3, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 24, Short.MAX_VALUE)
                        .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboCities3, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCity2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboRegion3, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRegion2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboDomain3, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDomain2))
                        .addGap(45, 45, 45))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jCaseDataPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSaveCase, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNewCase1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
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
                        .addComponent(cboCourtType3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblCaseType2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCaseType3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOfficialDate2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_official_date3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                        .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jCaseDataPanelLayout.createSequentialGroup()
                                .addComponent(lblRegion2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboRegion3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                                .addComponent(lblDomain2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboDomain3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(52, 52, 52)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCity2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCities3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCaseNo2)
                    .addComponent(lblYear2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCaseNumber3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtYear3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(slash1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(btnNewCase1)
                .addGap(4, 4, 4)
                .addComponent(lblSaveCase))
        );

        jSearchCaseDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("CourtJudgement_HighCourt_Generalbody.jSearchCaseDataPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 10))); // NOI18N
        jSearchCaseDataPanel.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jSearchCaseDataPanel.setVisible(false);

        btnSearchC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnSearchC.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.btnSearchC.text")); // NOI18N
        btnSearchC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchCActionPerformed(evt);
            }
        });

        slash3.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.slash3.text")); // NOI18N

        lblNoC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblNoC.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblNoC.text")); // NOI18N

        lblCaseTypeC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseTypeC.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCaseTypeC.text")); // NOI18N

        txtYearC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtYearC.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.txtYearC.text")); // NOI18N

        txtNoC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtNoC.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.txtNoC.text")); // NOI18N

        lblYearC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblYearC.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblYearC.text")); // NOI18N

        cboCourtTypeC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCourtTypeC.setModel(setCourtTypesModel());

        btnNewCase.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnNewCase.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.btnNewCase.text")); // NOI18N
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

        jHighCourtGeneralBodyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("CourtJudgement_Cassation.lblHighCourt_GeneralBody.text"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 10))); // NOI18N

        lblCaseNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseNo.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCaseNo.text")); // NOI18N

        cboCaseType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCaseType.setModel(setCaseTypesModel());

        slash.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.slash.text")); // NOI18N

        cboCities.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCities.setModel(setCitiesModel());
        cboCities.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCitiesActionPerformed(evt);
            }
        });

        lblCaseType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseType.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCaseType.text")); // NOI18N

        dt_official_date.setFormats("dd-MM-yyyy");
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
        lblOfficialDate.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblOfficialDate.text")); // NOI18N

        lblCity.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCity.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCity.text")); // NOI18N

        lblYear.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblYear.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblYear.text")); // NOI18N

        lblLanguage.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblLanguage.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblLanguage.text")); // NOI18N
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
        lblDomain.setText(bundle.getString("CourtJudgementMetadata.lblDomain.text")); // NOI18N

        chbJudgeChecked.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        chbJudgeChecked.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.chbJudgeChecked.text")); // NOI18N
        chbJudgeChecked.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbJudgeCheckedActionPerformed(evt);
            }
        });

        lblRegion.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblRegion.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblRegion.text")); // NOI18N

        lblImportance.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblImportance.setText(bundle.getString("CourtJudgementMetadata.lblImportance.text")); // NOI18N

        txtImportance.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtImportance.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.txtImportance.text")); // NOI18N

        lblSaveMain.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSaveMain.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblSaveMain.text")); // NOI18N
        lblSaveMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblSaveMainActionPerformed(evt);
            }
        });

        lblCassationId.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCassationId.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCassationId.text")); // NOI18N

        javax.swing.GroupLayout jHighCourtGeneralBodyPanelLayout = new javax.swing.GroupLayout(jHighCourtGeneralBodyPanel);
        jHighCourtGeneralBodyPanel.setLayout(jHighCourtGeneralBodyPanelLayout);
        jHighCourtGeneralBodyPanelLayout.setHorizontalGroup(
            jHighCourtGeneralBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jHighCourtGeneralBodyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jHighCourtGeneralBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jHighCourtGeneralBodyPanelLayout.createSequentialGroup()
                        .addGroup(jHighCourtGeneralBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jHighCourtGeneralBodyPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lblSaveMain, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jHighCourtGeneralBodyPanelLayout.createSequentialGroup()
                                .addGroup(jHighCourtGeneralBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboCities, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dt_official_date, javax.swing.GroupLayout.DEFAULT_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblOfficialDate)
                                    .addComponent(lblCity, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jHighCourtGeneralBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblImportance)
                                    .addComponent(txtImportance, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(chbJudgeChecked))
                                .addGap(0, 41, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jHighCourtGeneralBodyPanelLayout.createSequentialGroup()
                        .addGroup(jHighCourtGeneralBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jHighCourtGeneralBodyPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jHighCourtGeneralBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCaseType)
                                    .addComponent(cboCaseType, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(lblCassationId, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblLanguage))
                        .addGap(18, 18, 18)
                        .addGroup(jHighCourtGeneralBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5))
                    .addGroup(jHighCourtGeneralBodyPanelLayout.createSequentialGroup()
                        .addGroup(jHighCourtGeneralBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jHighCourtGeneralBodyPanelLayout.createSequentialGroup()
                                .addGroup(jHighCourtGeneralBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCaseNo)
                                    .addComponent(txtCaseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addComponent(slash, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jHighCourtGeneralBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblYear)
                                    .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(cboDomain, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDomain))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jHighCourtGeneralBodyPanelLayout.setVerticalGroup(
            jHighCourtGeneralBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jHighCourtGeneralBodyPanelLayout.createSequentialGroup()
                .addComponent(lblCassationId, javax.swing.GroupLayout.DEFAULT_SIZE, 14, Short.MAX_VALUE)
                .addGap(4, 4, 4)
                .addComponent(lblLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDomain)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDomain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(jHighCourtGeneralBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cboCaseType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jHighCourtGeneralBodyPanelLayout.createSequentialGroup()
                        .addComponent(lblCaseType)
                        .addGap(26, 26, 26))
                    .addGroup(jHighCourtGeneralBodyPanelLayout.createSequentialGroup()
                        .addComponent(lblRegion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboRegion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jHighCourtGeneralBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jHighCourtGeneralBodyPanelLayout.createSequentialGroup()
                        .addComponent(lblCity)
                        .addGap(7, 7, 7)
                        .addComponent(cboCities, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lblOfficialDate)
                        .addGap(6, 6, 6)
                        .addComponent(dt_official_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jHighCourtGeneralBodyPanelLayout.createSequentialGroup()
                        .addComponent(chbJudgeChecked)
                        .addGap(18, 18, 18)
                        .addGroup(jHighCourtGeneralBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jHighCourtGeneralBodyPanelLayout.createSequentialGroup()
                                .addComponent(lblImportance)
                                .addGap(25, 25, 25))
                            .addComponent(txtImportance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jHighCourtGeneralBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCaseNo)
                    .addComponent(lblYear))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jHighCourtGeneralBodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCaseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(slash)
                    .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSaveMain))
        );

        jSearchAppealDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("CourtJudgement_HighCourt_Generalbody.jSearchAppealDataPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 10))); // NOI18N
        jSearchAppealDataPanel.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jSearchAppealDataPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jSearchAppealDataPanelComponentHidden(evt);
            }
        });
        jSearchAppealDataPanel.setVisible(false);

        lblCaseNoA.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseNoA.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCaseNoA.text")); // NOI18N

        slash4.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.slash4.text")); // NOI18N

        cboCourtTypeA.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCourtTypeA.setModel(setCourtTypesModel_Appeal());

        txtYearA.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblYearA.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblYearA.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblYearA.text")); // NOI18N

        lblCourtTypeA.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCourtTypeA.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCourtTypeA.text")); // NOI18N

        txtCaseNumberA.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        btnSearchA.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnSearchA.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.btnSearchA.text")); // NOI18N
        btnSearchA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchAActionPerformed(evt);
            }
        });

        btnNewAppeal.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnNewAppeal.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.btnNewAppeal.text")); // NOI18N
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(btnNewAppeal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearchA))
        );

        jSearchCassationDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("CourtJudgement_Cassation.lblCassationSearch.text"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 10))); // NOI18N
        jSearchCassationDataPanel.setVisible(false);
        jSearchCassationDataPanel.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jSearchCassationDataPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jSearchCassationDataPanelComponentHidden(evt);
            }
        });

        lblCaseNoA1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseNoA1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCaseNoA1.text")); // NOI18N

        slash5.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.slash5.text")); // NOI18N

        cboCourtTypeCa.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCourtTypeCa.setModel(setCourtTypesModel_Cassation());

        txtYearCa.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblYearA1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblYearA1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblYearA1.text")); // NOI18N

        lblCourtTypeA1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCourtTypeA1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCourtTypeA1.text")); // NOI18N

        txtCaseNumberCa.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        btnSearchCa.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnSearchCa.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.btnSearchCa.text")); // NOI18N
        btnSearchCa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchCaActionPerformed(evt);
            }
        });

        btnNewCassation.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnNewCassation.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.btnNewCassation.text")); // NOI18N
        btnNewCassation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewCassationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jSearchCassationDataPanelLayout = new javax.swing.GroupLayout(jSearchCassationDataPanel);
        jSearchCassationDataPanel.setLayout(jSearchCassationDataPanelLayout);
        jSearchCassationDataPanelLayout.setHorizontalGroup(
            jSearchCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jSearchCassationDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jSearchCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboCourtTypeCa, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCourtTypeA1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jSearchCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCaseNoA1)
                    .addComponent(txtCaseNumberCa, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(slash5, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jSearchCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtYearCa, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblYearA1))
                .addGap(30, 30, 30))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jSearchCassationDataPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jSearchCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSearchCa, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNewCassation, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jSearchCassationDataPanelLayout.setVerticalGroup(
            jSearchCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSearchCassationDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jSearchCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jSearchCassationDataPanelLayout.createSequentialGroup()
                        .addGroup(jSearchCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCaseNoA1)
                            .addComponent(lblYearA1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jSearchCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCaseNumberCa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(slash5)
                            .addComponent(txtYearCa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jSearchCassationDataPanelLayout.createSequentialGroup()
                        .addComponent(lblCourtTypeA1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCourtTypeCa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(btnNewCassation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearchCa))
        );

        jCassationDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("CourtJudgement_Cassation.lblCassation.text"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 10))); // NOI18N
        jCassationDataPanel.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jCassationDataPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jCassationDataPanelComponentHidden(evt);
            }
        });
        jCassationDataPanel.setVisible(false);

        lblCaseNo3.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseNo3.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCaseNo3.text")); // NOI18N

        slash6.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.slash6.text")); // NOI18N

        cboCaseType1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCaseType1.setModel(setCaseTypesModel());

        cboCourtType1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCourtType1.setModel(setCourtTypesModel_Cassation());

        cboCities1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCities1.setModel(setCitiesModel());

        lblRegion3.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblRegion3.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblRegion3.text")); // NOI18N

        cboRegion1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboRegion1.setModel(setRegionsModel());

        cboDomain1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboDomain1.setModel(setDomainsModel());

        txtYear1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblDomain3.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblDomain3.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblDomain3.text")); // NOI18N

        lblYear3.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblYear3.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblYear3.text")); // NOI18N

        chbJudgeChecked1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        chbJudgeChecked1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.chbJudgeChecked1.text")); // NOI18N

        lblCity3.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCity3.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCity3.text")); // NOI18N

        lblCaseType3.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseType3.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCaseType3.text")); // NOI18N

        lblOfficialDate3.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblOfficialDate3.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblOfficialDate3.text")); // NOI18N

        dt_official_date1.setFormats("dd-MM-yyyy");
        dt_official_date1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblLanguage2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblLanguage2.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblLanguage2.text")); // NOI18N
        lblLanguage2.setName("lbl.BungeniLanguageID"); // NOI18N

        txtCaseNumber1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblCourtType3.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCourtType3.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblCourtType3.text")); // NOI18N

        cboLanguage1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboLanguage1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLanguage1.setName("fld.BungeniLanguageID"); // NOI18N

        lblImportance2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblImportance2.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblImportance2.text")); // NOI18N

        txtImportance1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtImportance1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.txtImportance1.text")); // NOI18N

        btnNewCassation1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnNewCassation1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.btnNewCassation1.text")); // NOI18N
        btnNewCassation1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewCassation1ActionPerformed(evt);
            }
        });

        lblSaveAppeal1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSaveAppeal1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblSaveAppeal1.text")); // NOI18N
        lblSaveAppeal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblSaveAppeal1ActionPerformed(evt);
            }
        });

        lblAppealId1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblAppealId1.setText(bundle.getString("CourtJudgement_HighCourt_Generalbody.lblAppealId1.text")); // NOI18N

        javax.swing.GroupLayout jCassationDataPanelLayout = new javax.swing.GroupLayout(jCassationDataPanel);
        jCassationDataPanel.setLayout(jCassationDataPanelLayout);
        jCassationDataPanelLayout.setHorizontalGroup(
            jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jCassationDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jCassationDataPanelLayout.createSequentialGroup()
                        .addGroup(jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblSaveAppeal1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jCassationDataPanelLayout.createSequentialGroup()
                                .addGroup(jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCaseNo3)
                                    .addComponent(txtCaseNumber1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addComponent(slash6, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblYear3)
                                    .addComponent(txtYear1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnNewCassation1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(21, 21, 21))
                    .addGroup(jCassationDataPanelLayout.createSequentialGroup()
                        .addGroup(jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jCassationDataPanelLayout.createSequentialGroup()
                                .addGroup(jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboCourtType1, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCourtType3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboCaseType1, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCaseType3))
                                .addGap(18, 18, 18)
                                .addGroup(jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cboDomain1, 0, 151, Short.MAX_VALUE)
                                        .addGroup(jCassationDataPanelLayout.createSequentialGroup()
                                            .addGap(1, 1, 1)
                                            .addComponent(lblDomain3)))
                                    .addGroup(jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cboRegion1, 0, 151, Short.MAX_VALUE)
                                        .addComponent(lblRegion3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(lblAppealId1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboLanguage1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jCassationDataPanelLayout.createSequentialGroup()
                                .addGroup(jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboCities1, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCity3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblLanguage2)
                                    .addComponent(dt_official_date1, javax.swing.GroupLayout.DEFAULT_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblOfficialDate3))
                                .addGap(18, 18, 18)
                                .addGroup(jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chbJudgeChecked1)
                                    .addGroup(jCassationDataPanelLayout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addGroup(jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblImportance2)
                                            .addComponent(txtImportance1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jCassationDataPanelLayout.setVerticalGroup(
            jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jCassationDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAppealId1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLanguage2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(cboLanguage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jCassationDataPanelLayout.createSequentialGroup()
                        .addComponent(lblCourtType3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCourtType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jCassationDataPanelLayout.createSequentialGroup()
                        .addComponent(lblDomain3)
                        .addGap(9, 9, 9)
                        .addComponent(cboDomain1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jCassationDataPanelLayout.createSequentialGroup()
                        .addComponent(lblCaseType3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCaseType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jCassationDataPanelLayout.createSequentialGroup()
                        .addComponent(lblRegion3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboRegion1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblCity3)
                .addGap(4, 4, 4)
                .addGroup(jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jCassationDataPanelLayout.createSequentialGroup()
                        .addComponent(cboCities1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(lblOfficialDate3)
                        .addGap(4, 4, 4)
                        .addComponent(dt_official_date1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jCassationDataPanelLayout.createSequentialGroup()
                        .addComponent(chbJudgeChecked1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtImportance1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jCassationDataPanelLayout.createSequentialGroup()
                                .addComponent(lblImportance2)
                                .addGap(25, 25, 25)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNewCassation1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jCassationDataPanelLayout.createSequentialGroup()
                        .addGroup(jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCaseNo3)
                            .addComponent(lblYear3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jCassationDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCaseNumber1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(slash6)
                            .addComponent(txtYear1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSaveAppeal1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSearchCaseDataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jAppealDataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSearchAppealDataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jHighCourtGeneralBodyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCaseDataPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSearchCassationDataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCassationDataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jHighCourtGeneralBodyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jSearchCassationDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCassationDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSearchAppealDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jAppealDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSearchCaseDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCaseDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        cboCourtType3.setSelectedIndex(0);
        cboCities3.setSelectedIndex(0);
        cboCaseType3.setSelectedIndex(0);
        cboDomain3.setSelectedIndex(0);
        cboRegion3.setSelectedIndex(0);
        dt_official_date3.setDate(null);
        txtCaseNumber3.setText(null);
        txtYear3.setText(null);
        jSearchCaseDataPanel.setVisible(false);
        jCaseDataPanel.setVisible(true);
    }//GEN-LAST:event_btnNewCase1ActionPerformed

    private void btnSearchCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchCActionPerformed
         CourtType courtTypeObj = CourtTypesListC.get(this.cboCourtTypeC.getSelectedIndex());

        ResultSet rs;
        CJ_Main mainObj = null;

        try {
            String sqlStm = "SELECT CJ_Main_ID, CJ_CaseTypes.CJ_CaseTypes_Name, CJ_Regions.CJ_Regions_Name, CJ_Domains.CJ_Domains_Name, CJ_CourtTypes.CJ_CourtTypes_Name, CJ_Cities.CJ_Cities_Name, [CJ_Main_NumNo], "
                    + " [CJ_Main_DecDate] , [CJ_Main_NumYr], [CJ_Main_Importance] FROM [CJ_Main]"
                    + "INNER JOIN CJ_CaseTypes ON [CJ_Main].CJ_Main_CJ_CaseTypes_ID=CJ_CaseTypes.CJ_CaseTypes_ID "
                    + "INNER JOIN CJ_Cities ON CJ_Main.CJ_Main_CJ_Cities_ID=CJ_Cities.CJ_Cities_ID "
                    + "INNER JOIN CJ_Regions ON CJ_Main.CJ_Main_CJ_Regions_ID=CJ_Regions.CJ_Regions_ID "
                    + "INNER JOIN CJ_Domains ON CJ_Main.CJ_Main_CJ_Domains_ID = CJ_Domains.CJ_Domains_ID "
                    + "INNER JOIN CJ_CourtTypes ON CJ_Main.CJ_Main_CJ_CourtTypes_ID = CJ_CourtTypes.CJ_CourtTypes_ID"
                    + " WHERE [CJ_Main_NumNo]=" + txtNoC.getText()
                    + " and [CJ_Main_NumYr]=" + txtYearC.getText()
                    + " and [CJ_Main_CJ_CourtTypes_ID]=" + courtTypeObj.getCourtTypeID()
                    + "";

            rs = conStmt.executeQuery(sqlStm);
            launchCaseTableFrame(rs);

        } catch (SQLException ex) {
            Logger.getLogger(CourtJudgement_Appeal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSearchCActionPerformed

    
    private void launchCaseTableFrame(ResultSet rs) throws SQLException {

        final BungeniFrame frm = new BungeniFrame(bundle.getString("CourtJudgement_Case_tableData.text"));
        if (rs.next()) {
            ResultSetMetaData meta = rs.getMetaData();
            int numberOfColumns = meta.getColumnCount();

            //  String[] COLUMN_NAMES = new String[]{
            Vector cols = new Vector(numberOfColumns + 1);
            cols.add(" ");
            cols.add(bundle.getString("CourtJudgement_Appeal.lblCaseTypet.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblRegiont.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblDomaint.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblCourtTypet.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblCityt.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblCaseNot.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblOfficialDatet.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblYeart.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblImportancet.text"));
            cols.add(" ___ ");

            Vector data = new Vector();

            Vector row = new Vector(numberOfColumns);
            for (int i = 0; i < numberOfColumns; ++i) {
                Object o = rs.getObject(i + 1);
                if (o instanceof Date) {
                    row.add(dtformatter.format((Date) o));
                } else {
                    row.add(o);
                }
            }
            data.add(row);

            while (rs.next()) {
                row = new Vector(numberOfColumns);
                for (int i = 0; i < numberOfColumns; ++i) {
                    Object o = rs.getObject(i + 1);
                    if (o instanceof Date) {
                        row.add(dtformatter.format((Date) o));
                    } else {
                        row.add(o);
                    }
                }
                data.add(row);
            }

            JTable jtblCaseData = new JTable(data, cols);
            hide(jtblCaseData, 0);

            Action link = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    JTable table = (JTable) e.getSource();
                    int modelRow = Integer.valueOf(e.getActionCommand());
                    Object rsCJ_Main_ID = ((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0);
                    CJ_Main_ID = (Integer) rsCJ_Main_ID;

                    if (linkDoc(CJ_Main_ID)) {
                        frm.setVisible(false);
                        JOptionPane.showMessageDialog(frm, bundle.getString("CourtJudgement_Appeal.docLinked.text"));

                        try {
                            setCaseData(CJ_Main_ID);
                        } catch (ParseException ex) {
                            Logger.getLogger(CourtJudgement_Cassation.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        jSearchCaseDataPanel.setVisible(false);
                        jCaseDataPanel.setVisible(true);
                    }
                }
            };
            ButtonColumn buttonColumn = new ButtonColumn(jtblCaseData, link, 10);
            buttonColumn.setMnemonic(KeyEvent.VK_D);

            jtblCaseData.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            JScrollPane scrollPane = new JScrollPane(jtblCaseData);
            frm.add(scrollPane);
            frm.initFrame();
            frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frm.setSize(800, 100);
            frm.setVisible(true);
            FrameLauncher.CenterFrame(frm);
            frm.setAlwaysOnTop(true);
        } else {
            JOptionPane.showMessageDialog(frm, bundle.getString("CourtJudgement_Appeal.NoResult.text"));
        }
    }

    private void btnNewCaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewCaseActionPerformed
        // TODO add your handling code here:
        cboCourtType3.setSelectedIndex(0);
        cboCities3.setSelectedIndex(0);
        cboCaseType3.setSelectedIndex(0);
        cboDomain3.setSelectedIndex(0);
        cboRegion3.setSelectedIndex(0);
        dt_official_date3.setDate(null);
        txtCaseNumber3.setText(null);
        txtYear3.setText(null);
        jSearchCaseDataPanel.setVisible(false);
        jCaseDataPanel.setVisible(true);
    }//GEN-LAST:event_btnNewCaseActionPerformed

    private void jSearchAppealDataPanelComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jSearchAppealDataPanelComponentHidden
        // TODO add your handling code here:
    }//GEN-LAST:event_jSearchAppealDataPanelComponentHidden

    private void btnNewAppealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewAppealActionPerformed
        // TODO add your handling code here:
        cboCourtType2.setSelectedIndex(0);
        cboCities2.setSelectedIndex(0);
        cboCaseType2.setSelectedIndex(0);
        cboDomain2.setSelectedIndex(0);
        cboRegion2.setSelectedIndex(0);
        dt_official_date2.setDate(null);
        txtCaseNumber2.setText(null);
        txtYear2.setText(null);
        jSearchAppealDataPanel.setVisible(false);
        jAppealDataPanel.setVisible(true);
        jSearchCaseDataPanel.setVisible(true);
    }//GEN-LAST:event_btnNewAppealActionPerformed

    private void btnSearchAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchAActionPerformed
        CourtType courtTypeObj = CourtTypesListA.get(this.cboCourtTypeA.getSelectedIndex());

        ResultSet rs;
        CJ_Main mainObj = null;

        try {
            String sqlStm = "SELECT CJ_Main_ID, CJ_CaseTypes.CJ_CaseTypes_Name, CJ_Regions.CJ_Regions_Name, CJ_Domains.CJ_Domains_Name, CJ_CourtTypes.CJ_CourtTypes_Name, CJ_Cities.CJ_Cities_Name, [CJ_Main_NumNo], "
                    + " [CJ_Main_DecDate] , [CJ_Main_NumYr], [CJ_Main_Importance] FROM [CJ_Main]"
                    + "INNER JOIN CJ_CaseTypes ON [CJ_Main].CJ_Main_CJ_CaseTypes_ID=CJ_CaseTypes.CJ_CaseTypes_ID "
                    + "INNER JOIN CJ_Cities ON CJ_Main.CJ_Main_CJ_Cities_ID=CJ_Cities.CJ_Cities_ID "
                    + "INNER JOIN CJ_Regions ON CJ_Main.CJ_Main_CJ_Regions_ID=CJ_Regions.CJ_Regions_ID "
                    + "INNER JOIN CJ_Domains ON CJ_Main.CJ_Main_CJ_Domains_ID = CJ_Domains.CJ_Domains_ID "
                    + "INNER JOIN CJ_CourtTypes ON CJ_Main.CJ_Main_CJ_CourtTypes_ID = CJ_CourtTypes.CJ_CourtTypes_ID"
                    + " WHERE [CJ_Main_NumNo]=" + txtCaseNumberA.getText()
                    + " and [CJ_Main_NumYr]=" + txtYearA.getText()
                    + " and [CJ_Main_CJ_CourtTypes_ID]=" + courtTypeObj.getCourtTypeID()
                    + "";

            rs = conStmt.executeQuery(sqlStm);
            launchAppealTableFrame(rs);

        } catch (SQLException ex) {
            Logger.getLogger(CourtJudgement_Cassation.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnSearchAActionPerformed

    private void btnNewAppeal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewAppeal1ActionPerformed
        // TODO add your handling code here:
        cboCourtType2.setSelectedIndex(0);
        cboCities2.setSelectedIndex(0);
        cboCaseType2.setSelectedIndex(0);
        cboDomain2.setSelectedIndex(0);
        cboRegion2.setSelectedIndex(0);
        dt_official_date2.setDate(null);
        txtCaseNumber2.setText(null);
        txtYear2.setText(null);
        jSearchAppealDataPanel.setVisible(false);
        jAppealDataPanel.setVisible(true);
    }//GEN-LAST:event_btnNewAppeal1ActionPerformed

    private void lblSaveMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblSaveMainActionPerformed
        // TODO add your handling code here:

        ResultSet rsMainID = null;
        int insert_CJ_Main = 0;

        try {
            conStmt = con.createStatement();

            if (storeMainData()) {
                con.setAutoCommit(false);
                String sqlStm = "INSERT INTO [CJ_Main] ([CJ_Main_CJ_CaseTypes_ID], [CJ_Main_CJ_Regions_ID], [CJ_Main_CJ_Domains_ID], [CJ_Main_CJ_CourtTypes_ID], [CJ_Main_CJ_Cities_ID], [CJ_Main_NumNo], "
                        + " [CJ_Main_DecDate] , [CJ_Main_NumYr], [CJ_Main_Importance])"
                        + " VALUES(" + docMetaModel.getItem("BungeniCaseTypeID") + "," + docMetaModel.getItem("BungeniRegionID")
                        + "," + docMetaModel.getItem("BungeniDomainID") + "," + docMetaModel.getItem("BungeniCourtTypeID")
                        + "," + docMetaModel.getItem("BungeniCityID") + "," + docMetaModel.getItem("BungeniCaseNo")
                        + ",'" + docMetaModel.getItem("BungeniIssuedOn")
                        + "','" + docMetaModel.getItem("BungeniYear") + "'," + docMetaModel.getItem("BungeniImportance")
                        + ")";

                insert_CJ_Main = conStmt.executeUpdate(sqlStm);

                sqlStm = "SELECT max([CJ_Main_ID]) FROM [CJ_Main] ";
                rsMainID = conStmt.executeQuery(sqlStm);

                if (rsMainID.next()) {
                    String CJ_Main_ID = rsMainID.getString(1);

                    docMetaModel.updateItem("BungeniMainDocID", CJ_Main_ID);
                    docMetaModel.saveModel(ooDocument);

                } else {
                    MessageBox.OK("Record not found");
                }
            }

        } catch (SQLException e) {
            if (conStmt != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    con.rollback();
                } catch (SQLException excep) {
                }
            }
        } finally {
            if (insert_CJ_Main != 0) {
                if (rsMainID != null) {
                    try {
                        rsMainID.close();
                        con.commit();
                        con.setAutoCommit(true);
                        JOptionPane.showMessageDialog(new JFrame(), bundle.getString("CourtJudgement_Appeal.ResultSaved.text"));
                        jSearchCassationDataPanel.setVisible(true);

                    } catch (SQLException ex) {
                        Logger.getLogger(CourtJudgement_Appeal.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

    }//GEN-LAST:event_lblSaveMainActionPerformed

    private boolean storeMainData() {
        boolean bState = false;
        try {
            LanguageCode selLanguage = (LanguageCode) this.cboLanguage.getSelectedItem();
            Domains sDomain = DomainsList.get(this.cboDomain.getSelectedIndex());
            JudgementRegion sRegion = JudgementRegionsList.get(this.cboRegion.getSelectedIndex());
            CaseType sCaseType = CaseTypesList.get(this.cboCaseType.getSelectedIndex());
            City sCity = CitiesList.get(this.cboCities.getSelectedIndex());
            String sCaseNo = this.txtCaseNumber.getText();
            String sDate = dbdtformatter.format(dt_official_date.getDate());
            String sYear = this.txtYear.getText();
            String sImportance = this.txtImportance.getText();

            docMetaModel.updateItem("BungeniLanguageCode", selLanguage.getLanguageCodeAlpha2());
            docMetaModel.updateItem("BungeniDomain", sDomain.toString());
            docMetaModel.updateItem("BungeniRegion", sRegion.toString());
            docMetaModel.updateItem("BungeniCaseType", sCaseType.toString());
            docMetaModel.updateItem("BungeniCity", sCity.toString());

            docMetaModel.updateItem("BungeniDomainID", sDomain.getDomainID());
            docMetaModel.updateItem("BungeniRegionID", sRegion.getJudgementRegionID());
            docMetaModel.updateItem("BungeniCaseTypeID", sCaseType.getCaseTypeID());
            docMetaModel.updateItem("BungeniCityID", sCity.getCityID());


            docMetaModel.updateItem("BungeniCaseNo", sCaseNo);
            docMetaModel.updateItem("BungeniIssuedOn", sDate);
            docMetaModel.updateItem("BungeniYear", sYear);
            docMetaModel.updateItem("BungeniImportance", sImportance);

            docMetaModel.saveModel(ooDocument);
            bState = true;

        } catch (Exception ex) {
            log.error("store data : " + ex.getMessage());
            bState = false;
        } finally {
            return bState;
        }

    }

    private boolean storeCassationData() {
        boolean bState = false;
        try {
            LanguageCode selLanguage = (LanguageCode) this.cboLanguage1.getSelectedItem();
            CourtType sCourtType = CourtTypesList.get(this.cboCourtType1.getSelectedIndex());
            Domains sDomain = DomainsList.get(this.cboDomain1.getSelectedIndex());
            JudgementRegion sRegion = JudgementRegionsList.get(this.cboRegion1.getSelectedIndex());
            CaseType sCaseType = CaseTypesList.get(this.cboCaseType1.getSelectedIndex());
            City sCity = CitiesList.get(this.cboCities1.getSelectedIndex());
            String sCaseNo = this.txtCaseNumber1.getText();
            String sDate = dbdtformatter.format(dt_official_date1.getDate());
            String sYear = this.txtYear1.getText();
            String sImportance = this.txtImportance1.getText();

            docMetaModel.updateItem("BungeniLanguageCode", selLanguage.getLanguageCodeAlpha2());
            docMetaModel.updateItem("BungeniCourtType", sCourtType.toString());
            docMetaModel.updateItem("BungeniDomain", sDomain.toString());
            docMetaModel.updateItem("BungeniRegion", sRegion.toString());
            docMetaModel.updateItem("BungeniCaseType", sCaseType.toString());
            docMetaModel.updateItem("BungeniCity", sCity.toString());

            docMetaModel.updateItem("BungeniCourtTypeID", sCourtType.getCourtTypeID());
            docMetaModel.updateItem("BungeniDomainID", sDomain.getDomainID());
            docMetaModel.updateItem("BungeniRegionID", sRegion.getJudgementRegionID());
            docMetaModel.updateItem("BungeniCaseTypeID", sCaseType.getCaseTypeID());
            docMetaModel.updateItem("BungeniCityID", sCity.getCityID());


            docMetaModel.updateItem("BungeniCaseNo", sCaseNo);
            docMetaModel.updateItem("BungeniIssuedOn", sDate);
            docMetaModel.updateItem("BungeniYear", sYear);
            docMetaModel.updateItem("BungeniImportance", sImportance);

            docMetaModel.saveModel(ooDocument);
            bState = true;

        } catch (Exception ex) {
            log.error("store data : " + ex.getMessage());
            bState = false;
        } finally {
            return bState;
        }

    }

    private void lblSaveAppealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblSaveAppealActionPerformed
        // TODO add your handling code here:
//        int CourtTypeId = this.cboCourtType.getSelectedIndex() + 1;
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
            String sqlStm = "INSERT INTO [CJ_Main] ([CJ_Main_CJ_CaseTypes_ID], [CJ_Main_CJ_Regions_ID], "
                    + "[CJ_Main_CJ_Cities_ID], [CJ_Main_CJ_Domains_ID], [CJ_Main_Importance], [CJ_Main_DecDate], "
                    + "[CJ_Main_NumNo], [CJ_Main_NumYr])"
                    + "VALUES(" + CaseTypeId + "," + RegionId
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
//        int CourtTypeId = this.cboCourtType.getSelectedIndex() + 1;
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
            String sqlStm = "INSERT INTO [CJ_Main] ([CJ_Main_CJ_CaseTypes_ID], [CJ_Main_CJ_Regions_ID], "
                    + "[CJ_Main_CJ_Cities_ID], [CJ_Main_CJ_Domains_ID], [CJ_Main_Importance], [CJ_Main_DecDate], "
                    + "[CJ_Main_NumNo], [CJ_Main_NumYr])"
                    + "VALUES(" + CaseTypeId + "," + RegionId
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

    private void btnSearchCaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchCaActionPerformed
        // TODO add your handling code here:
        CourtType courtTypeObj = CourtTypesList.get(this.cboCourtType1.getSelectedIndex());

        ResultSet rs;
        CJ_Main mainObj = null;

        try {
            String sqlStm = "SELECT CJ_Main_ID, CJ_CaseTypes.CJ_CaseTypes_Name, CJ_Regions.CJ_Regions_Name, CJ_Domains.CJ_Domains_Name, CJ_CourtTypes.CJ_CourtTypes_Name, CJ_Cities.CJ_Cities_Name, [CJ_Main_NumNo], "
                    + " [CJ_Main_DecDate] , [CJ_Main_NumYr], [CJ_Main_Importance] FROM [CJ_Main]"
                    + "INNER JOIN CJ_CaseTypes ON [CJ_Main].CJ_Main_CJ_CaseTypes_ID=CJ_CaseTypes.CJ_CaseTypes_ID "
                    + "INNER JOIN CJ_Cities ON CJ_Main.CJ_Main_CJ_Cities_ID=CJ_Cities.CJ_Cities_ID "
                    + "INNER JOIN CJ_Regions ON CJ_Main.CJ_Main_CJ_Regions_ID=CJ_Regions.CJ_Regions_ID "
                    + "INNER JOIN CJ_Domains ON CJ_Main.CJ_Main_CJ_Domains_ID = CJ_Domains.CJ_Domains_ID "
                    + "INNER JOIN CJ_CourtTypes ON CJ_Main.CJ_Main_CJ_CourtTypes_ID = CJ_CourtTypes.CJ_CourtTypes_ID"
                    + " WHERE [CJ_Main_NumNo]=" + txtCaseNumberCa.getText()
                    + " and [CJ_Main_NumYr]=" + txtYearCa.getText()
                    + " and [CJ_Main_CJ_CourtTypes_ID]=" + courtTypeObj.getCourtTypeID()
                    + "";

            rs = conStmt.executeQuery(sqlStm);
            launchCassationTableFrame(rs);

        } catch (SQLException ex) {
            Logger.getLogger(CourtJudgement_Cassation.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnSearchCaActionPerformed

    private void launchCassationTableFrame(ResultSet rs) throws SQLException {
        final BungeniFrame frm = new BungeniFrame(bundle.getString("CourtJudgement_Appeal_tableData.text"));

        if (rs.next()) {
            ResultSetMetaData meta = rs.getMetaData();
            int numberOfColumns = meta.getColumnCount();

            //  String[] COLUMN_NAMES = new String[]{
            Vector cols = new Vector(numberOfColumns + 1);
            cols.add(" ");
            cols.add(bundle.getString("CourtJudgement_Appeal.lblCaseTypet.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblRegiont.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblDomaint.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblCourtTypet.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblCityt.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblCaseNot.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblOfficialDatet.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblYeart.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblImportancet.text"));
            cols.add(" ___ ");

            Vector data = new Vector();

            Vector row = new Vector(numberOfColumns);
            for (int i = 0; i < numberOfColumns; ++i) {
                Object o = rs.getObject(i + 1);
                if (o instanceof Date) {
                    row.add(dtformatter.format((Date) o));
                } else {
                    row.add(o);
                }
            }
            data.add(row);

            while (rs.next()) {
                row = new Vector(numberOfColumns);
                for (int i = 0; i < numberOfColumns; ++i) {
                    Object o = rs.getObject(i + 1);
                    if (o instanceof Date) {
                        row.add(dtformatter.format((Date) o));
                    } else {
                        row.add(o);
                    }
                }
                data.add(row);
            }

            JTable jtblCassationData = new JTable(data, cols);
            hide(jtblCassationData, 0);

            Action link = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    JTable table = (JTable) e.getSource();
                    int modelRow = Integer.valueOf(e.getActionCommand());
                    Object rsCJ_Main_ID = ((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0);
                    CJ_Main_ID = (Integer) rsCJ_Main_ID;

                    if (linkDoc(CJ_Main_ID)) {
                        frm.setVisible(false);
                        JOptionPane.showMessageDialog(frm, bundle.getString("CourtJudgement_Appeal.docLinked.text"));

                        try {
                            setCassationData(CJ_Main_ID);
                        } catch (ParseException ex) {
                            Logger.getLogger(CourtJudgement_Cassation.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        jSearchCaseDataPanel.setVisible(false);
                        jCassationDataPanel.setVisible(true);
                        jSearchAppealDataPanel.setVisible(true);

                    }
                }
            };
            ButtonColumn buttonColumn = new ButtonColumn(jtblCassationData, link, 10);
            buttonColumn.setMnemonic(KeyEvent.VK_D);

            jtblCassationData.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            JScrollPane scrollPane = new JScrollPane(jtblCassationData);
            frm.add(scrollPane);
            frm.initFrame();
            frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frm.setSize(800, 100);
            frm.setVisible(true);
            FrameLauncher.CenterFrame(frm);
            frm.setAlwaysOnTop(true);
        } else {
            JOptionPane.showMessageDialog(frm, bundle.getString("CourtJudgement_Appeal.NoResult.text"));
        }
    }

    private void setCaseData(int CJ_Main_ID) throws ParseException {

        ResultSet rs;
        CJ_Main mainObj = null;

        try {
            String sqlStm = "SELECT CJ_Main_CJ_CaseTypes_ID, CJ_Main_CJ_Cities_ID, CJ_Main_CJ_Regions_ID, CJ_Main_CJ_Domains_ID, CJ_Main_CJ_CourtTypes_ID, CJ_Main_DecDate, CJ_Main_NumNo"
                    + ", CJ_Main_NumYr, CJ_Main_Importance FROM CJ_Main WHERE CJ_Main_ID = " + CJ_Main_ID;

            rs = conStmt.executeQuery(sqlStm);

            while (rs.next()) {
                mainObj = new CJ_Main(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), dbdtformatter.parse(rs.getString(6)), rs.getString(7), rs.getString(8), rs.getString(9));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CourtJudgement_Cassation.class.getName()).log(Level.SEVERE, null, ex);
        }

        docMetaModel.updateItem("BungeniMainDocID", Integer.toString(CJ_Main_ID));
        String sLanguageCode = docMetaModel.getItem("BungeniLanguageCode");
        String sCourtType = mainObj.getCJ_Main_CJ_CourtTypes_ID();
        docMetaModel.updateItem("BungeniCourtType", sCourtType);
        String sDomainType = mainObj.getCJ_Main_CJ_Domains_ID();
        docMetaModel.updateItem("BungeniDomain", sDomainType);
        String sRegion = mainObj.getCJ_Main_CJ_Regions_ID();
        docMetaModel.updateItem("BungeniRegion", sRegion);
        String sCaseType = mainObj.getCJ_Main_CJ_CaseTypes_ID();
        docMetaModel.updateItem("BungeniCaseType", sCaseType);
        String sCity = mainObj.getCJ_Main_CJ_Cities_ID();
        docMetaModel.updateItem("BungeniCity", sCity);
        String sCaseNo = mainObj.getCJ_Main_NumNo();
        docMetaModel.updateItem("BungeniCaseNo", sCaseNo);
        Date sDate = mainObj.getCJ_Main_DecDate();
        docMetaModel.updateItem("BungeniDate", sDate.toString());
        String sYear = mainObj.getCJ_Main_NumYr();
        docMetaModel.updateItem("BungeniYear", sYear);
        String sImportance = mainObj.getCJ_Main_Importance();
        docMetaModel.updateItem("BungeniImportance", sImportance);


        if (!CommonStringFunctions.emptyOrNull(sCourtType)) {
            this.cboCourtType3.setSelectedItem(sCourtType);
        }
        if (!CommonStringFunctions.emptyOrNull(sDomainType)) {
            this.cboDomain3.setSelectedItem(sDomainType);
        }
        if (!CommonStringFunctions.emptyOrNull(sRegion)) {
            this.cboRegion3.setSelectedItem(sDomainType);
        }
        if (!CommonStringFunctions.emptyOrNull(sCaseType)) {
            this.cboCaseType3.setSelectedItem(sCaseType);
        }
        if (!CommonStringFunctions.emptyOrNull(sCity)) {
            this.cboCities3.setSelectedItem(sCity);
        }
        if (!CommonStringFunctions.emptyOrNull(sCaseNo)) {
            this.txtCaseNumber3.setText(sCaseNo);
        }
        if (!CommonStringFunctions.emptyOrNull(sDate.toString())) {
            this.dt_official_date3.setDate(sDate);
        }
        if (!CommonStringFunctions.emptyOrNull(sYear)) {
            this.txtYear3.setText(sYear);
        }
    }

    private void setCassationData(int CJ_Main_ID) throws ParseException {

        ResultSet rs;
        CJ_Main mainObj = null;

        try {
            String sqlStm = "SELECT CJ_Main_CJ_CaseTypes_ID, CJ_Main_CJ_Cities_ID, CJ_Main_CJ_Regions_ID, CJ_Main_CJ_Domains_ID, CJ_Main_CJ_CourtTypes_ID, CJ_Main_DecDate, CJ_Main_NumNo"
                    + ", CJ_Main_NumYr, CJ_Main_Importance FROM CJ_Main WHERE CJ_Main_ID = " + CJ_Main_ID;

            rs = conStmt.executeQuery(sqlStm);

            while (rs.next()) {
                mainObj = new CJ_Main(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), dbdtformatter.parse(rs.getString(6)), rs.getString(7), rs.getString(8), rs.getString(9));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CourtJudgement_Cassation.class.getName()).log(Level.SEVERE, null, ex);
        }

        docMetaModel.updateItem("BungeniMainDocID", Integer.toString(CJ_Main_ID));
        String sLanguageCode = docMetaModel.getItem("BungeniLanguageCode");
        String sCourtType = mainObj.getCJ_Main_CJ_CourtTypes_ID();
        docMetaModel.updateItem("BungeniCourtType", sCourtType);
        String sDomainType = mainObj.getCJ_Main_CJ_Domains_ID();
        docMetaModel.updateItem("BungeniDomain", sDomainType);
        String sRegion = mainObj.getCJ_Main_CJ_Regions_ID();
        docMetaModel.updateItem("BungeniRegion", sRegion);
        String sCaseType = mainObj.getCJ_Main_CJ_CaseTypes_ID();
        docMetaModel.updateItem("BungeniCaseType", sCaseType);
        String sCity = mainObj.getCJ_Main_CJ_Cities_ID();
        docMetaModel.updateItem("BungeniCity", sCity);
        String sCaseNo = mainObj.getCJ_Main_NumNo();
        docMetaModel.updateItem("BungeniCaseNo", sCaseNo);
        Date sDate = mainObj.getCJ_Main_DecDate();
        docMetaModel.updateItem("BungeniDate", sDate.toString());
        String sYear = mainObj.getCJ_Main_NumYr();
        docMetaModel.updateItem("BungeniYear", sYear);
        String sImportance = mainObj.getCJ_Main_Importance();
        docMetaModel.updateItem("BungeniImportance", sImportance);


        if (!CommonStringFunctions.emptyOrNull(sCourtType)) {
            this.cboCourtType1.setSelectedItem(sCourtType);
        }
        if (!CommonStringFunctions.emptyOrNull(sDomainType)) {
            this.cboDomain1.setSelectedItem(sDomainType);
        }
        if (!CommonStringFunctions.emptyOrNull(sRegion)) {
            this.cboRegion1.setSelectedItem(sDomainType);
        }
        if (!CommonStringFunctions.emptyOrNull(sCaseType)) {
            this.cboCaseType1.setSelectedItem(sCaseType);
        }
        if (!CommonStringFunctions.emptyOrNull(sCity)) {
            this.cboCities1.setSelectedItem(sCity);
        }
        if (!CommonStringFunctions.emptyOrNull(sCaseNo)) {
            this.txtCaseNumber1.setText(sCaseNo);
        }
        if (!CommonStringFunctions.emptyOrNull(sDate.toString())) {
            this.dt_official_date1.setDate(sDate);
        }
        if (!CommonStringFunctions.emptyOrNull(sYear)) {
            this.txtYear1.setText(sYear);
        }

        if (!CommonStringFunctions.emptyOrNull(sImportance)) {
            this.txtImportance1.setText(sImportance);
        }

    }

    private void setAppealData(int CJ_Main_ID) throws ParseException {

        ResultSet rs;
        CJ_Main mainObj = null;

        try {
            String sqlStm = "SELECT CJ_Main_CJ_CaseTypes_ID, CJ_Main_CJ_Cities_ID, CJ_Main_CJ_Regions_ID, CJ_Main_CJ_Domains_ID, CJ_Main_CJ_CourtTypes_ID, CJ_Main_DecDate, CJ_Main_NumNo"
                    + ", CJ_Main_NumYr, CJ_Main_Importance FROM CJ_Main WHERE CJ_Main_ID = " + CJ_Main_ID;

            rs = conStmt.executeQuery(sqlStm);

            while (rs.next()) {
                mainObj = new CJ_Main(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), dbdtformatter.parse(rs.getString(6)), rs.getString(7), rs.getString(8), rs.getString(9));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CourtJudgement_Cassation.class.getName()).log(Level.SEVERE, null, ex);
        }

        docMetaModel.updateItem("BungeniMainDocID", Integer.toString(CJ_Main_ID));
        String sLanguageCode = docMetaModel.getItem("BungeniLanguageCode");
        String sCourtType = mainObj.getCJ_Main_CJ_CourtTypes_ID();
        docMetaModel.updateItem("BungeniCourtType", sCourtType);
        String sDomainType = mainObj.getCJ_Main_CJ_Domains_ID();
        docMetaModel.updateItem("BungeniDomain", sDomainType);
        String sRegion = mainObj.getCJ_Main_CJ_Regions_ID();
        docMetaModel.updateItem("BungeniRegion", sRegion);
        String sCaseType = mainObj.getCJ_Main_CJ_CaseTypes_ID();
        docMetaModel.updateItem("BungeniCaseType", sCaseType);
        String sCity = mainObj.getCJ_Main_CJ_Cities_ID();
        docMetaModel.updateItem("BungeniCity", sCity);
        String sCaseNo = mainObj.getCJ_Main_NumNo();
        docMetaModel.updateItem("BungeniCaseNo", sCaseNo);
        Date sDate = mainObj.getCJ_Main_DecDate();
        docMetaModel.updateItem("BungeniDate", sDate.toString());
        String sYear = mainObj.getCJ_Main_NumYr();
        docMetaModel.updateItem("BungeniYear", sYear);
        String sImportance = mainObj.getCJ_Main_Importance();
        docMetaModel.updateItem("BungeniImportance", sImportance);


        if (!CommonStringFunctions.emptyOrNull(sCourtType)) {
            this.cboCourtType2.setSelectedItem(sCourtType);
        }
        if (!CommonStringFunctions.emptyOrNull(sDomainType)) {
            this.cboDomain2.setSelectedItem(sDomainType);
        }
        if (!CommonStringFunctions.emptyOrNull(sRegion)) {
            this.cboRegion2.setSelectedItem(sDomainType);
        }
        if (!CommonStringFunctions.emptyOrNull(sCaseType)) {
            this.cboCaseType2.setSelectedItem(sCaseType);
        }
        if (!CommonStringFunctions.emptyOrNull(sCity)) {
            this.cboCities2.setSelectedItem(sCity);
        }
        if (!CommonStringFunctions.emptyOrNull(sCaseNo)) {
            this.txtCaseNumber2.setText(sCaseNo);
        }
        if (!CommonStringFunctions.emptyOrNull(sDate.toString())) {
            this.dt_official_date2.setDate(sDate);
        }
        if (!CommonStringFunctions.emptyOrNull(sYear)) {
            this.txtYear2.setText(sYear);
        }
        
        if (!CommonStringFunctions.emptyOrNull(sImportance)) {
            this.txtImportance2.setText(sImportance);
        }

    }

    private boolean linkDoc(int CJ_Main_ID) {
        boolean bState = false;

        int update_CJ_Main = 0;

        try {
            conStmt = con.createStatement();

            con.setAutoCommit(false);
            String sqlStm = "UPDATE [CJ_Main] SET [CJ_Main_CJ_Main_ID] = " + CJ_Main_ID
                    + ", [CJ_Main_CJ_CaseTypes_ID] = " + docMetaModel.getItem("BungeniCaseTypeID")
                    + ", [CJ_Main_CJ_Regions_ID] = " + docMetaModel.getItem("BungeniRegionID")
                    + ", [CJ_Main_CJ_Domains_ID] = " + docMetaModel.getItem("BungeniDomainID")
                    + ", [CJ_Main_CJ_CourtTypes_ID] = " + docMetaModel.getItem("BungeniCourtTypeID")
                    + ", [CJ_Main_CJ_Cities_ID] = " + docMetaModel.getItem("BungeniCityID")
                    + ", [CJ_Main_NumNo] = " + docMetaModel.getItem("BungeniCaseNo")
                    + ", [CJ_Main_DecDate] = " + docMetaModel.getItem("BungeniIssuedOn")
                    + ", [CJ_Main_NumYr] = " + docMetaModel.getItem("BungeniYear")
                    + ", [CJ_Main_Importance] = " + docMetaModel.getItem("BungeniImportance")
                    + " WHERE [CJ_Main_ID] = " + docMetaModel.getItem("BungeniMainDocID");

            update_CJ_Main = conStmt.executeUpdate(sqlStm);
            bState = true;

        } catch (SQLException e) {
            if (conStmt != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    con.rollback();
                    bState = false;
                } catch (SQLException excep) {
                }
            }
        } finally {
            if (update_CJ_Main != 0) {
                try {
                    con.commit();
                    con.setAutoCommit(true);
                    return bState;
                } catch (SQLException ex) {
                    Logger.getLogger(CourtJudgement_Cassation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return bState;
    }

    private void hide(JTable jtbl, int index) {
        TableColumn column = jtbl.getColumnModel().getColumn(index);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setWidth(0);
        column.setPreferredWidth(0);
        doLayout();
    }

    private void launchAppealTableFrame(ResultSet rs) throws SQLException {
        final BungeniFrame frm = new BungeniFrame(bundle.getString("CourtJudgement_Appeal_tableData.text"));

        if (rs.next()) {
            ResultSetMetaData meta = rs.getMetaData();
            int numberOfColumns = meta.getColumnCount();

            //  String[] COLUMN_NAMES = new String[]{
            Vector cols = new Vector(numberOfColumns + 1);
            cols.add(" ");
            cols.add(bundle.getString("CourtJudgement_Appeal.lblCaseTypet.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblRegiont.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblDomaint.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblCourtTypet.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblCityt.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblCaseNot.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblOfficialDatet.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblYeart.text"));
            cols.add(bundle.getString("CourtJudgement_Appeal.lblImportancet.text"));
            cols.add(" ___ ");

            Vector data = new Vector();

            Vector row = new Vector(numberOfColumns);
            for (int i = 0; i < numberOfColumns; ++i) {
                Object o = rs.getObject(i + 1);
                if (o instanceof Date) {
                    row.add(dtformatter.format((Date) o));
                } else {
                    row.add(o);
                }
            }
            data.add(row);

            while (rs.next()) {
                row = new Vector(numberOfColumns);
                for (int i = 0; i < numberOfColumns; ++i) {
                    Object o = rs.getObject(i + 1);
                    if (o instanceof Date) {
                        row.add(dtformatter.format((Date) o));
                    } else {
                        row.add(o);
                    }
                }
                data.add(row);
            }

            JTable jtblAppealData = new JTable(data, cols);
            hide(jtblAppealData, 0);

            Action link = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    JTable table = (JTable) e.getSource();
                    int modelRow = Integer.valueOf(e.getActionCommand());
                    Object rsCJ_Main_ID = ((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0);
                    CJ_Main_ID = (Integer) rsCJ_Main_ID;

                    if (linkDoc(CJ_Main_ID)) {
                        frm.setVisible(false);
                        JOptionPane.showMessageDialog(frm, bundle.getString("CourtJudgement_Appeal.docLinked.text"));

                        try {
                            setAppealData(CJ_Main_ID);
                        } catch (ParseException ex) {
                            Logger.getLogger(CourtJudgement_Cassation.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        jSearchAppealDataPanel.setVisible(false);
                        jAppealDataPanel.setVisible(true);
                        jSearchCaseDataPanel.setVisible(true);

                    }
                }
            };
            ButtonColumn buttonColumn = new ButtonColumn(jtblAppealData, link, 10);
            buttonColumn.setMnemonic(KeyEvent.VK_D);

            jtblAppealData.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            JScrollPane scrollPane = new JScrollPane(jtblAppealData);
            frm.add(scrollPane);
            frm.initFrame();
            frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frm.setSize(800, 100);
            frm.setVisible(true);
            FrameLauncher.CenterFrame(frm);
            frm.setAlwaysOnTop(true);
        } else {
            JOptionPane.showMessageDialog(frm, bundle.getString("CourtJudgement_Appeal.NoResult.text"));
        }
    }

    private void btnNewCassationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewCassationActionPerformed
        // TODO add your handling code here:
        cboCourtType2.setSelectedIndex(0);
        cboCities2.setSelectedIndex(0);
        cboCaseType2.setSelectedIndex(0);
        cboDomain2.setSelectedIndex(0);
        cboRegion2.setSelectedIndex(0);
        dt_official_date2.setDate(null);
        txtCaseNumber2.setText(null);
        txtYear2.setText(null);
        jSearchCassationDataPanel.setVisible(false);
        jCassationDataPanel.setVisible(true);
        jSearchAppealDataPanel.setVisible(true);

    }//GEN-LAST:event_btnNewCassationActionPerformed

    private void jSearchCassationDataPanelComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jSearchCassationDataPanelComponentHidden
        // TODO add your handling code here:
    }//GEN-LAST:event_jSearchCassationDataPanelComponentHidden

    private void btnNewCassation1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewCassation1ActionPerformed
        // TODO add your handling code here:
        cboCourtType1.setSelectedIndex(0);
        cboCities1.setSelectedIndex(0);
        cboCaseType1.setSelectedIndex(0);
        cboDomain1.setSelectedIndex(0);
        cboRegion1.setSelectedIndex(0);
        dt_official_date1.setDate(null);
        txtCaseNumber1.setText(null);
        txtYear1.setText(null);
    }//GEN-LAST:event_btnNewCassation1ActionPerformed

    private void lblSaveAppeal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblSaveAppeal1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblSaveAppeal1ActionPerformed

    private void jCassationDataPanelComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jCassationDataPanelComponentHidden
        // TODO add your handling code here:
    }//GEN-LAST:event_jCassationDataPanelComponentHidden
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNewAppeal;
    private javax.swing.JButton btnNewAppeal1;
    private javax.swing.JButton btnNewCase;
    private javax.swing.JButton btnNewCase1;
    private javax.swing.JButton btnNewCassation;
    private javax.swing.JButton btnNewCassation1;
    private javax.swing.JButton btnSearchA;
    private javax.swing.JButton btnSearchC;
    private javax.swing.JButton btnSearchCa;
    private javax.swing.JComboBox cboCaseType;
    private javax.swing.JComboBox cboCaseType1;
    private javax.swing.JComboBox cboCaseType2;
    private javax.swing.JComboBox cboCaseType3;
    private javax.swing.JComboBox cboCities;
    private javax.swing.JComboBox cboCities1;
    private javax.swing.JComboBox cboCities2;
    private javax.swing.JComboBox cboCities3;
    private javax.swing.JComboBox cboCourtType1;
    private javax.swing.JComboBox cboCourtType2;
    private javax.swing.JComboBox cboCourtType3;
    private javax.swing.JComboBox cboCourtTypeA;
    private javax.swing.JComboBox cboCourtTypeC;
    private javax.swing.JComboBox cboCourtTypeCa;
    private javax.swing.JComboBox cboDomain;
    private javax.swing.JComboBox cboDomain1;
    private javax.swing.JComboBox cboDomain2;
    private javax.swing.JComboBox cboDomain3;
    private javax.swing.JComboBox cboLanguage;
    private javax.swing.JComboBox cboLanguage1;
    private javax.swing.JComboBox cboLanguage2;
    private javax.swing.JComboBox cboRegion;
    private javax.swing.JComboBox cboRegion1;
    private javax.swing.JComboBox cboRegion2;
    private javax.swing.JComboBox cboRegion3;
    private javax.swing.JCheckBox chbJudgeChecked;
    private javax.swing.JCheckBox chbJudgeChecked1;
    private javax.swing.JCheckBox chbJudgeChecked2;
    private org.jdesktop.swingx.JXDatePicker dt_official_date;
    private org.jdesktop.swingx.JXDatePicker dt_official_date1;
    private org.jdesktop.swingx.JXDatePicker dt_official_date2;
    private org.jdesktop.swingx.JXDatePicker dt_official_date3;
    private javax.swing.JPanel jAppealDataPanel;
    private javax.swing.JPanel jCaseDataPanel;
    private javax.swing.JPanel jCassationDataPanel;
    private javax.swing.JPanel jHighCourtGeneralBodyPanel;
    private javax.swing.JPanel jSearchAppealDataPanel;
    private javax.swing.JPanel jSearchCaseDataPanel;
    private javax.swing.JPanel jSearchCassationDataPanel;
    private javax.swing.JLabel lblAppealId;
    private javax.swing.JLabel lblAppealId1;
    private javax.swing.JLabel lblCaseId;
    private javax.swing.JLabel lblCaseNo;
    private javax.swing.JLabel lblCaseNo1;
    private javax.swing.JLabel lblCaseNo2;
    private javax.swing.JLabel lblCaseNo3;
    private javax.swing.JLabel lblCaseNoA;
    private javax.swing.JLabel lblCaseNoA1;
    private javax.swing.JLabel lblCaseType;
    private javax.swing.JLabel lblCaseType1;
    private javax.swing.JLabel lblCaseType2;
    private javax.swing.JLabel lblCaseType3;
    private javax.swing.JLabel lblCaseTypeC;
    private javax.swing.JLabel lblCassationId;
    private javax.swing.JLabel lblCity;
    private javax.swing.JLabel lblCity1;
    private javax.swing.JLabel lblCity2;
    private javax.swing.JLabel lblCity3;
    private javax.swing.JLabel lblCourtType1;
    private javax.swing.JLabel lblCourtType2;
    private javax.swing.JLabel lblCourtType3;
    private javax.swing.JLabel lblCourtTypeA;
    private javax.swing.JLabel lblCourtTypeA1;
    private javax.swing.JLabel lblDomain;
    private javax.swing.JLabel lblDomain1;
    private javax.swing.JLabel lblDomain2;
    private javax.swing.JLabel lblDomain3;
    private javax.swing.JLabel lblImportance;
    private javax.swing.JLabel lblImportance1;
    private javax.swing.JLabel lblImportance2;
    private javax.swing.JLabel lblLanguage;
    private javax.swing.JLabel lblLanguage1;
    private javax.swing.JLabel lblLanguage2;
    private javax.swing.JLabel lblNoC;
    private javax.swing.JLabel lblOfficialDate;
    private javax.swing.JLabel lblOfficialDate1;
    private javax.swing.JLabel lblOfficialDate2;
    private javax.swing.JLabel lblOfficialDate3;
    private javax.swing.JLabel lblRegion;
    private javax.swing.JLabel lblRegion1;
    private javax.swing.JLabel lblRegion2;
    private javax.swing.JLabel lblRegion3;
    private javax.swing.JButton lblSaveAppeal;
    private javax.swing.JButton lblSaveAppeal1;
    private javax.swing.JButton lblSaveCase;
    private javax.swing.JButton lblSaveMain;
    private javax.swing.JLabel lblYear;
    private javax.swing.JLabel lblYear1;
    private javax.swing.JLabel lblYear2;
    private javax.swing.JLabel lblYear3;
    private javax.swing.JLabel lblYearA;
    private javax.swing.JLabel lblYearA1;
    private javax.swing.JLabel lblYearC;
    private javax.swing.JLabel slash;
    private javax.swing.JLabel slash1;
    private javax.swing.JLabel slash2;
    private javax.swing.JLabel slash3;
    private javax.swing.JLabel slash4;
    private javax.swing.JLabel slash5;
    private javax.swing.JLabel slash6;
    private javax.swing.JTextField txtCaseNumber;
    private javax.swing.JTextField txtCaseNumber1;
    private javax.swing.JTextField txtCaseNumber2;
    private javax.swing.JTextField txtCaseNumber3;
    private javax.swing.JTextField txtCaseNumberA;
    private javax.swing.JTextField txtCaseNumberCa;
    private javax.swing.JTextField txtImportance;
    private javax.swing.JTextField txtImportance1;
    private javax.swing.JTextField txtImportance2;
    private javax.swing.JTextField txtNoC;
    private javax.swing.JTextField txtYear;
    private javax.swing.JTextField txtYear1;
    private javax.swing.JTextField txtYear2;
    private javax.swing.JTextField txtYear3;
    private javax.swing.JTextField txtYearA;
    private javax.swing.JTextField txtYearC;
    private javax.swing.JTextField txtYearCa;
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
