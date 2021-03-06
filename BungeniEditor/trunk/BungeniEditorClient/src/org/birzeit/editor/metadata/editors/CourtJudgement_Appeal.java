/*
 * DebateRecordMetadata.java
 *
 * Created on November 4, 2008, 1:43 PM
 */
package org.birzeit.editor.metadata.editors;

import org.bungeni.editor.connectorutils.CommonConnectorFunctions;
import org.bungeni.editor.config.BungeniEditorProperties;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.element.*;
import org.bungeni.editor.config.BungeniEditorPropertiesHelper;
import org.bungeni.editor.metadata.BaseEditorDocMetadataDialog;
import org.birzeit.editor.metadata.CJ_Main;
import org.birzeit.editor.metadata.CaseType;
import org.birzeit.editor.metadata.City;
import org.birzeit.editor.metadata.CourtType;
import org.birzeit.editor.metadata.Domains;
import org.birzeit.editor.metadata.JudgementRegion;
import org.birzeit.editor.metadata.JudgementMetadataModel;
import org.bungeni.editor.metadata.LanguageCode;
import org.bungeni.editor.metadata.editors.MetadataEditorContainer;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.extutils.*;
import org.bungeni.utils.BungeniFileSavePathFormat;
import org.bungeni.utils.BungeniFrame;

/**
 *
 * @author undesa
 */
public class CourtJudgement_Appeal extends BaseEditorDocMetadataDialog {

    private static java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/birzeit/editor/metadata/editors/Bundle"); // NOI18N
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CourtJudgement_Appeal.class.getName());
    JudgementMetadataModel docMetaModel = new JudgementMetadataModel();
    private SimpleDateFormat dtformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
    private SimpleDateFormat savedtformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataSaveDateFormat"));
    private SimpleDateFormat dbdtformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("dataBaseDateFormat"));
    private String dbName = "CourtJudgments2007_test";
    private ArrayList<CaseType> CaseTypesList = new ArrayList<CaseType>();
    private ArrayList<Domains> DomainsList = new ArrayList<Domains>();
    private ArrayList<JudgementRegion> JudgementRegionsList = new ArrayList<JudgementRegion>();
    private ArrayList<City> CitiesList = new ArrayList<City>();
    private ArrayList<CourtType> CourtTypesList = new ArrayList<CourtType>();
    private ArrayList<CourtType> CourtTypesListC = new ArrayList<CourtType>();
    private Connection con = ConnectorFunctions.ConnectMMSM(dbName);
    private Statement conStmt;
    private final BungeniFrame frm = new BungeniFrame(bundle.getString("CourtJudgement_Appeal_tableData.text"));

    public CourtJudgement_Appeal() {
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
                this.cboCaseType1.setSelectedItem(sFamily);
            }
            if (!CommonStringFunctions.emptyOrNull(sDomainType)) {
                this.cboDomain1.setSelectedItem(sDomainType);
            }
            if (!CommonStringFunctions.emptyOrNull(sCaseType)) {
                this.cboCourtType1.setSelectedItem(sCaseType);
            }
            if (!CommonStringFunctions.emptyOrNull(sCity)) {
                this.cboCities1.setSelectedItem(sCity);
            }
            if (!CommonStringFunctions.emptyOrNull(sCaseNo)) {
                this.txtCaseNumber1.setText(sCaseNo);
            }
            if (!CommonStringFunctions.emptyOrNull(sDate)) {
                this.dt_official_date1.setDate(dtformatter.parse(sDate));
            }
            if (!CommonStringFunctions.emptyOrNull(sYear)) {
                this.txtYear1.setText(sYear);
            }
        } catch (Exception ex) {
            log.error("initalize()  =  " + ex.getMessage());
        }
    }

    private void loadActInfo() {

        String sLanguageCode = docMetaModel.getItem("BungeniLanguageCode");
        String sCourtType = docMetaModel.getItem("BungeniCourtType");
        String sDomainType = docMetaModel.getItem("BungeniDomain");
        String sRegion = docMetaModel.getItem("BungeniRegion");
        String sCaseType = docMetaModel.getItem("BungeniCaseType");
        String sCity = docMetaModel.getItem("BungeniCity");
        String sCaseNo = docMetaModel.getItem("BungeniCaseNo");
        String sDate = docMetaModel.getItem("BungeniDate");
        String sYear = docMetaModel.getItem("BungeniYear");
        String sImportance = docMetaModel.getItem("BungeniImportance");

        if (!CommonStringFunctions.emptyOrNull(sLanguageCode)) {
            this.cboLanguage.setSelectedItem(findLanguageCodeAlpha2(sLanguageCode));
        }
        if (!CommonStringFunctions.emptyOrNull(sCourtType)) {
            this.cboCourtType.setSelectedItem(sCourtType);
        }
        if (!CommonStringFunctions.emptyOrNull(sDomainType)) {
            this.cboDomain.setSelectedItem(sDomainType);
        }
        if (!CommonStringFunctions.emptyOrNull(sRegion)) {
            this.cboRegion.setSelectedItem(sDomainType);
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
            try {
                this.dt_official_date.setDate(dtformatter.parse(sDate));
            } catch (ParseException ex) {
                Logger.getLogger(CourtJudgement_Appeal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (!CommonStringFunctions.emptyOrNull(sYear)) {
            this.txtYear.setText(sYear);
        }

        if (!CommonStringFunctions.emptyOrNull(sImportance)) {
            this.txtImportance.setText(sImportance);
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
    private ComboBoxModel setCourtTypesModel_Appeal() {
        DefaultComboBoxModel courtTypesModel = null;

        try {
            String sqlStm = "SELECT DISTINCT [CJ_CourtTypes_ID], [CJ_CourtTypes_Name], [CJ_CourtTypes_Name_E] FROM [CJ_CourtTypes] WHERE [CJ_CourtTypes_Degree]=2 and [CJ_CourtTypes_IsCourtName]=1";
            ResultSet rs = conStmt.executeQuery(sqlStm);

            while (rs.next()) {
                CourtType ctObj = new CourtType(rs.getString(1), rs.getString(2), rs.getString(3));
                CourtTypesList.add(ctObj);

            }
        } catch (SQLException ex) {
            Logger.getLogger(CourtJudgement_Appeal.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] courtTypes = new String[CourtTypesList.size()];
        for (int i = 0; i < CourtTypesList.size(); i++) {
            courtTypes[i] = CourtTypesList.get(i).toString();
        }
        // create the default acts Names mode
        courtTypesModel = new DefaultComboBoxModel(courtTypes);

        return courtTypesModel;
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
                Logger.getLogger(ActMainMetadata.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ActMainMetadata.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ActMainMetadata.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ActMainMetadata.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String[] cities = new String[CitiesList.size()];
        for (int i = 0; i < CitiesList.size(); i++) {
            cities[i] = CitiesList.get(i).toString();
        }

        CitiesModel = new DefaultComboBoxModel(cities);

        return CitiesModel;
    }

    private ComboBoxModel setCourtTypesModel() {
        DefaultComboBoxModel courtTypesModel = null;
        if (CourtTypesListC.isEmpty()) {
            try {
                String sqlStm = "SELECT DISTINCT [CJ_CourtTypes_ID], [CJ_CourtTypes_Name], [CJ_CourtTypes_Name_E] FROM [CJ_CourtTypes] WHERE [CJ_CourtTypes_Degree]=1";
                ResultSet rs = conStmt.executeQuery(sqlStm);

                while (rs.next()) {
                    CourtType ctObj = new CourtType(rs.getString(1), rs.getString(2), rs.getString(3));
                    CourtTypesListC.add(ctObj);

                }
            } catch (SQLException ex) {
                Logger.getLogger(CourtJudgement_Appeal.class.getName()).log(Level.SEVERE, null, ex);
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

    private boolean storeAppealData() {
        boolean bState = false;
        try {
            LanguageCode selLanguage = (LanguageCode) this.cboLanguage.getSelectedItem();
            CourtType sCourtType = CourtTypesList.get(this.cboCourtType.getSelectedIndex());
            Domains sDomain = DomainsList.get(this.cboDomain.getSelectedIndex());
            JudgementRegion sRegion = JudgementRegionsList.get(this.cboRegion.getSelectedIndex());
            CaseType sCaseType = CaseTypesList.get(this.cboCaseType.getSelectedIndex());
            City sCity = CitiesList.get(this.cboCities.getSelectedIndex());
            String sCaseNo = this.txtCaseNumber.getText();
            String sDate = dbdtformatter.format(dt_official_date.getDate());
            String sYear = this.txtYear.getText();
            String sImportance = this.txtImportance.getText();

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

    public boolean applySelectedMetadata(BungeniFileSavePathFormat spf) {
        boolean bState = false;
        try {
            LanguageCode selLanguage = (LanguageCode) this.cboLanguage.getSelectedItem();
            CourtType sCourtType = CourtTypesList.get(this.cboCourtType.getSelectedIndex());
            Domains sDomain = DomainsList.get(this.cboDomain.getSelectedIndex());
            JudgementRegion sRegion = JudgementRegionsList.get(this.cboRegion.getSelectedIndex());
            CaseType sCaseType = CaseTypesList.get(this.cboCaseType.getSelectedIndex());
            City sCity = CitiesList.get(this.cboCities.getSelectedIndex());
            String sCaseNo = this.txtCaseNumber.getText();
            String sDate = dbdtformatter.format(dt_official_date.getDate());
            String sYear = this.txtYear.getText();
            String sImportance = this.txtImportance.getText();

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


            spf.setSaveComponent("DocumentType", BungeniEditorPropertiesHelper.getCurrentDocType());
            spf.setSaveComponent("LanguageCode", Locale.getDefault().getLanguage());
            spf.setSaveComponent("CountryCode", Locale.getDefault().getCountry());

            Date dtHansardDate = dt_official_date1.getDate();
            GregorianCalendar debateCal = new GregorianCalendar();
            debateCal.setTime(dtHansardDate);
            spf.setSaveComponent("Year", debateCal.get(Calendar.YEAR));
            spf.setSaveComponent("Month", debateCal.get(Calendar.MONTH) + 1);
            spf.setSaveComponent("Day", debateCal.get(Calendar.DAY_OF_MONTH));
            spf.setSaveComponent("PartName", "main");
            spf.setSaveComponent("Year", sYear);
            spf.setSaveComponent("Num", sCaseNo);

            CaseType selectedCaseType = CaseTypesList.get(this.cboCourtType1.getSelectedIndex());
            String strBungeniCaseType = selectedCaseType.getCaseTypeName_E();
            spf.setSaveComponent("JCaseType", strBungeniCaseType);

            City selectedCity = CitiesList.get(this.cboCities1.getSelectedIndex());
            String strBungeniCity = selectedCity.getCityName_E();
            spf.setSaveComponent("JCity", strBungeniCity);

            spf.setSaveComponent("JIssuedOn", savedtformatter.format(dt_official_date1.getDate()));

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

        jCaseDataPanel = new javax.swing.JPanel();
        slash1 = new javax.swing.JLabel();
        lblNoC = new javax.swing.JLabel();
        lblCourtTypeC = new javax.swing.JLabel();
        txtYearC = new javax.swing.JTextField();
        txtNoC = new javax.swing.JTextField();
        lblYearC = new javax.swing.JLabel();
        cboCourtTypeC = new javax.swing.JComboBox();
        btnNewCase = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        jCaseFullDataPanel = new javax.swing.JPanel();
        lblCaseNo1 = new javax.swing.JLabel();
        slash = new javax.swing.JLabel();
        cboCaseType1 = new javax.swing.JComboBox();
        cboCourtType1 = new javax.swing.JComboBox();
        cboCities1 = new javax.swing.JComboBox();
        lblRegion1 = new javax.swing.JLabel();
        cboRegion1 = new javax.swing.JComboBox();
        cboDomain1 = new javax.swing.JComboBox();
        txtYear1 = new javax.swing.JTextField();
        lblDomain1 = new javax.swing.JLabel();
        lblYear1 = new javax.swing.JLabel();
        lblCity1 = new javax.swing.JLabel();
        lblCaseType1 = new javax.swing.JLabel();
        lblOfficialDate1 = new javax.swing.JLabel();
        dt_official_date1 = new org.jdesktop.swingx.JXDatePicker();
        txtCaseNumber1 = new javax.swing.JTextField();
        lblCourtType1 = new javax.swing.JLabel();
        btnNewCase1 = new javax.swing.JButton();
        btnSaveCase = new javax.swing.JButton();
        jAppealDataPanel1 = new javax.swing.JPanel();
        lblCaseNo = new javax.swing.JLabel();
        slash2 = new javax.swing.JLabel();
        cboCaseType = new javax.swing.JComboBox();
        cboCourtType = new javax.swing.JComboBox();
        cboCities = new javax.swing.JComboBox();
        lblRegion = new javax.swing.JLabel();
        cboRegion = new javax.swing.JComboBox();
        cboDomain = new javax.swing.JComboBox();
        txtYear = new javax.swing.JTextField();
        lblDomain = new javax.swing.JLabel();
        lblYear = new javax.swing.JLabel();
        chbJudgeChecked = new javax.swing.JCheckBox();
        lblCity = new javax.swing.JLabel();
        lblCaseType = new javax.swing.JLabel();
        lblOfficialDate = new javax.swing.JLabel();
        dt_official_date = new org.jdesktop.swingx.JXDatePicker();
        lblLanguage = new javax.swing.JLabel();
        txtCaseNumber = new javax.swing.JTextField();
        lblCourtType = new javax.swing.JLabel();
        cboLanguage = new javax.swing.JComboBox();
        lblImportance = new javax.swing.JLabel();
        txtImportance = new javax.swing.JTextField();
        btnSaveAppeal = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(400, 1000));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/birzeit/editor/metadata/editors/Bundle"); // NOI18N
        jCaseDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("CourtJudgement_Appeal.lblCaseData.text"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 10))); // NOI18N
        jCaseDataPanel.setVisible(false);
        jCaseDataPanel.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        slash1.setText(bundle.getString("CourtJudgement_Appeal.slash1.text")); // NOI18N

        lblNoC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblNoC.setText(bundle.getString("CourtJudgement_Appeal.lblNoC.text")); // NOI18N

        lblCourtTypeC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCourtTypeC.setText(bundle.getString("CourtJudgement_Appeal.lblCourtTypeC.text")); // NOI18N

        txtYearC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtYearC.setText(bundle.getString("CourtJudgement_Appeal.txtYearC.text")); // NOI18N

        txtNoC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtNoC.setText(bundle.getString("CourtJudgement_Appeal.txtNoC.text")); // NOI18N

        lblYearC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblYearC.setText(bundle.getString("CourtJudgement_Appeal.lblYearC.text")); // NOI18N

        cboCourtTypeC.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCourtTypeC.setModel(setCourtTypesModel());

        btnNewCase.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnNewCase.setText(bundle.getString("CourtJudgement_Appeal.btnNewCase.text")); // NOI18N
        btnNewCase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewCaseActionPerformed(evt);
            }
        });

        btnSearch.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnSearch.setText(bundle.getString("CourtJudgement_Appeal.btnSearch.text")); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jCaseDataPanelLayout = new javax.swing.GroupLayout(jCaseDataPanel);
        jCaseDataPanel.setLayout(jCaseDataPanelLayout);
        jCaseDataPanelLayout.setHorizontalGroup(
            jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                        .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboCourtTypeC, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCourtTypeC))
                        .addGap(18, 18, 18)
                        .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNoC)
                            .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                                .addComponent(txtNoC, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(slash1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblYearC)
                            .addComponent(txtYearC, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jCaseDataPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSearch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNewCase, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jCaseDataPanelLayout.setVerticalGroup(
            jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                            .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblNoC)
                                .addComponent(lblYearC))
                            .addGap(23, 23, 23))
                        .addGroup(jCaseDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(slash1)
                            .addComponent(txtNoC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtYearC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jCaseDataPanelLayout.createSequentialGroup()
                        .addComponent(lblCourtTypeC)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCourtTypeC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(btnNewCase)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch))
        );

        jCaseFullDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("CourtJudgement_Appeal.lblCaseData.text"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 10))); // NOI18N
        jCaseFullDataPanel.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jCaseFullDataPanel.setVisible(false);

        lblCaseNo1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseNo1.setText(bundle.getString("CourtJudgement_Appeal.lblCaseNo.text")); // NOI18N

        slash.setText(bundle.getString("CourtJudgement_Appeal.slash.text_1")); // NOI18N

        cboCaseType1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCaseType1.setModel(setCaseTypesModel());

        cboCourtType1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCourtType1.setModel(setCourtTypesModel());

        cboCities1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCities1.setModel(setCitiesModel());

        lblRegion1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblRegion1.setText(bundle.getString("CourtJudgement_Appeal.lblRegion.text")); // NOI18N

        cboRegion1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboRegion1.setModel(setRegionsModel());

        cboDomain1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboDomain1.setModel(setDomainsModel());

        txtYear1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblDomain1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblDomain1.setText(bundle.getString("CourtJudgement_Appeal.lblDomain1.text")); // NOI18N

        lblYear1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblYear1.setText(bundle.getString("CourtJudgement_Appeal.lblYear.text")); // NOI18N

        lblCity1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCity1.setText(bundle.getString("CourtJudgement_Appeal.lblCity.text")); // NOI18N

        lblCaseType1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseType1.setText(bundle.getString("CourtJudgement_Appeal.lblCaseType.text")); // NOI18N

        lblOfficialDate1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblOfficialDate1.setText(bundle.getString("CourtJudgement_Appeal.lblOfficialDate.text")); // NOI18N

        dt_official_date1.setFormats("yyyy-MM-dd");
        dt_official_date1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        txtCaseNumber1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblCourtType1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCourtType1.setText(bundle.getString("CourtJudgement_Appeal.lblCourtType.text")); // NOI18N

        btnNewCase1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnNewCase1.setText(bundle.getString("CourtJudgement_Appeal.btnNewCase1.text")); // NOI18N
        btnNewCase1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewCase1ActionPerformed(evt);
            }
        });

        btnSaveCase.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnSaveCase.setText(bundle.getString("CourtJudgement_Appeal.btnSaveCase.text")); // NOI18N

        javax.swing.GroupLayout jCaseFullDataPanelLayout = new javax.swing.GroupLayout(jCaseFullDataPanel);
        jCaseFullDataPanel.setLayout(jCaseFullDataPanelLayout);
        jCaseFullDataPanelLayout.setHorizontalGroup(
            jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jCaseFullDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jCaseFullDataPanelLayout.createSequentialGroup()
                        .addGroup(jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboCourtType1, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCourtType1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jCaseFullDataPanelLayout.createSequentialGroup()
                                .addGroup(jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboDomain1, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jCaseFullDataPanelLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(lblDomain1)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jCaseFullDataPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnSaveCase, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnNewCase1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jCaseFullDataPanelLayout.createSequentialGroup()
                        .addGroup(jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jCaseFullDataPanelLayout.createSequentialGroup()
                                .addGroup(jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jCaseFullDataPanelLayout.createSequentialGroup()
                                        .addGroup(jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblCaseNo1)
                                            .addGroup(jCaseFullDataPanelLayout.createSequentialGroup()
                                                .addComponent(txtCaseNumber1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(slash, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(5, 5, 5)
                                        .addGroup(jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtYear1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblYear1)))
                                    .addComponent(dt_official_date1, javax.swing.GroupLayout.DEFAULT_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblOfficialDate1))
                                .addGap(18, 18, 18)
                                .addGroup(jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboCities1, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCity1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jCaseFullDataPanelLayout.createSequentialGroup()
                                .addGroup(jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCaseType1)
                                    .addComponent(cboCaseType1, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20)
                                .addGroup(jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboRegion1, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblRegion1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jCaseFullDataPanelLayout.setVerticalGroup(
            jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jCaseFullDataPanelLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jCaseFullDataPanelLayout.createSequentialGroup()
                        .addComponent(lblCourtType1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCourtType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jCaseFullDataPanelLayout.createSequentialGroup()
                        .addComponent(lblDomain1)
                        .addGap(9, 9, 9)
                        .addComponent(cboDomain1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jCaseFullDataPanelLayout.createSequentialGroup()
                        .addComponent(lblCaseType1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCaseType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jCaseFullDataPanelLayout.createSequentialGroup()
                        .addComponent(lblRegion1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboRegion1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCity1)
                    .addComponent(lblOfficialDate1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboCities1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dt_official_date1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCaseNo1)
                    .addComponent(lblYear1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jCaseFullDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCaseNumber1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtYear1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(slash))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnNewCase1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSaveCase))
        );

        jAppealDataPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("CourtJudgement_Appeal.jAppealDataPanel1.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 0, 10))); // NOI18N
        jAppealDataPanel1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jAppealDataPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jAppealDataPanel1ComponentHidden(evt);
            }
        });

        lblCaseNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseNo.setText(bundle.getString("CourtJudgement_Appeal.lblCaseNo.text")); // NOI18N

        slash2.setText(bundle.getString("CourtJudgement_Appeal.slash2.text")); // NOI18N

        cboCaseType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCaseType.setModel(setCaseTypesModel());

        cboCourtType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCourtType.setModel(setCourtTypesModel_Appeal());

        cboCities.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboCities.setModel(setCitiesModel());

        lblRegion.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblRegion.setText(bundle.getString("CourtJudgement_Appeal.lblRegion.text")); // NOI18N

        cboRegion.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboRegion.setModel(setRegionsModel());

        cboDomain.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboDomain.setModel(setDomainsModel());

        txtYear.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblDomain.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblDomain.setText(bundle.getString("CourtJudgement_Appeal.lblDomain.text")); // NOI18N

        lblYear.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblYear.setText(bundle.getString("CourtJudgement_Appeal.lblYear.text")); // NOI18N

        chbJudgeChecked.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        chbJudgeChecked.setText(bundle.getString("CourtJudgement_Appeal.chbJudgeChecked.text")); // NOI18N

        lblCity.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCity.setText(bundle.getString("CourtJudgement_Appeal.lblCity.text")); // NOI18N

        lblCaseType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCaseType.setText(bundle.getString("CourtJudgement_Appeal.lblCaseType.text")); // NOI18N

        lblOfficialDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblOfficialDate.setText(bundle.getString("CourtJudgement_Appeal.lblOfficialDate.text")); // NOI18N

        dt_official_date.setFormats("yyyy-MM-dd");
        dt_official_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblLanguage.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblLanguage.setText(bundle.getString("CourtJudgement_Appeal.lblLanguage.text")); // NOI18N
        lblLanguage.setName("lbl.BungeniLanguageID"); // NOI18N

        txtCaseNumber.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblCourtType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblCourtType.setText(bundle.getString("CourtJudgement_Appeal.lblCourtType.text")); // NOI18N

        cboLanguage.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboLanguage.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLanguage.setName("fld.BungeniLanguageID"); // NOI18N

        lblImportance.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblImportance.setText(bundle.getString("CourtJudgement_Appeal.lblImportance.text")); // NOI18N

        txtImportance.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtImportance.setText(bundle.getString("CourtJudgement_Appeal.txtImportance.text")); // NOI18N

        btnSaveAppeal.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnSaveAppeal.setText(bundle.getString("CourtJudgement_Appeal.btnSaveAppeal.text")); // NOI18N
        btnSaveAppeal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveAppealActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jAppealDataPanel1Layout = new javax.swing.GroupLayout(jAppealDataPanel1);
        jAppealDataPanel1.setLayout(jAppealDataPanel1Layout);
        jAppealDataPanel1Layout.setHorizontalGroup(
            jAppealDataPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jAppealDataPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jAppealDataPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jAppealDataPanel1Layout.createSequentialGroup()
                        .addGroup(jAppealDataPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCaseNo)
                            .addComponent(txtCaseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(slash2, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jAppealDataPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblYear)))
                    .addGroup(jAppealDataPanel1Layout.createSequentialGroup()
                        .addGroup(jAppealDataPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboCourtType, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboCaseType, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboCities, 0, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCity, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblLanguage)
                            .addComponent(dt_official_date, javax.swing.GroupLayout.DEFAULT_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblOfficialDate)
                            .addComponent(lblCaseType)
                            .addComponent(lblCourtType, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jAppealDataPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cboDomain, 0, 151, Short.MAX_VALUE)
                            .addComponent(cboRegion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jAppealDataPanel1Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(lblDomain))
                            .addComponent(lblImportance)
                            .addComponent(chbJudgeChecked)
                            .addComponent(txtImportance))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jAppealDataPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSaveAppeal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jAppealDataPanel1Layout.setVerticalGroup(
            jAppealDataPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jAppealDataPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(cboLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jAppealDataPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jAppealDataPanel1Layout.createSequentialGroup()
                        .addComponent(lblCourtType)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCourtType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jAppealDataPanel1Layout.createSequentialGroup()
                        .addComponent(lblDomain)
                        .addGap(9, 9, 9)
                        .addComponent(cboDomain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jAppealDataPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jAppealDataPanel1Layout.createSequentialGroup()
                        .addComponent(lblCaseType)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCaseType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jAppealDataPanel1Layout.createSequentialGroup()
                        .addComponent(lblRegion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboRegion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addComponent(lblCity)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jAppealDataPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboCities, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chbJudgeChecked))
                .addGap(5, 5, 5)
                .addGroup(jAppealDataPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jAppealDataPanel1Layout.createSequentialGroup()
                        .addComponent(lblOfficialDate)
                        .addGap(4, 4, 4)
                        .addGroup(jAppealDataPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dt_official_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtImportance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jAppealDataPanel1Layout.createSequentialGroup()
                        .addComponent(lblImportance)
                        .addGap(26, 26, 26)))
                .addGap(11, 11, 11)
                .addGroup(jAppealDataPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCaseNo)
                    .addComponent(lblYear))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jAppealDataPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCaseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(slash2)
                    .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSaveAppeal))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jAppealDataPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCaseDataPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jCaseFullDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jAppealDataPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCaseDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCaseFullDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(212, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
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
            launchTableFrame(rs);

//            if (rs.next()) {
//                try {
//                    String queryMain = "SELECT [CJ_Main_NumNo], [CJ_Main_NumYr], [CJ_Main_DecDate], [CJ_Main_CJ_CourtTypes_ID], [CJ_Main_CJ_Cities_ID] FROM [CJ_Main] WHERE [CJ_Main_ID]=" + rs.getString(1) + "";
//                    ResultSet resMain = conStmt.executeQuery(queryMain);
//
//
//                    while (resMain.next()) {
//                        mainObj = new CJ_Main(rs.getString(1), resMain.getString(1), resMain.getString(2), new SimpleDateFormat("yyyy-MM-dd").parse(resMain.getString(3)), resMain.getString(4), resMain.getString(5));
//                    }
//                } catch (ParseException ex) {
//                    Logger.getLogger(CourtJudgement_Appeal.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//                cboCourtType1.setSelectedItem(courtTypeObj);
//
//                int i = Integer.parseInt(mainObj.getCJ_Main_CJ_Cities_ID());
//                cboCities1.setSelectedIndex(i - 1);
//                dt_official_date1.setDate(mainObj.getCJ_Main_DecDate());
//                txtCaseNumber1.setText(mainObj.getCJ_Main_NumNo());
//                txtYear1.setText(mainObj.getCJ_Main_NumYr());
//                jCaseDataPanel.setVisible(false);
//                jCaseFullDataPanel.setVisible(true);
//            } else {
//                MessageBox.OK("Recodr not found");
//            }

        } catch (SQLException ex) {
            Logger.getLogger(CourtJudgement_Appeal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSearchActionPerformed
    private void launchTableFrame(ResultSet rs) throws SQLException {

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

            Action delete = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    JTable table = (JTable) e.getSource();
                    int modelRow = Integer.valueOf(e.getActionCommand());
                    Object rsCJ_Main_ID = ((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0);
                    int CJ_Main_ID = (Integer) rsCJ_Main_ID;

                    if (linkDoc(CJ_Main_ID)) {
                        frm.setVisible(false);
                        JOptionPane.showMessageDialog(frm, bundle.getString("CourtJudgement_Appeal.docLinked.text"));

                        try {
                            setCaseData(CJ_Main_ID);
                        } catch (ParseException ex) {
                            Logger.getLogger(CourtJudgement_Cassation.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        jCaseDataPanel.setVisible(false);
                        jCaseFullDataPanel.setVisible(true);
                    }
                }
            };
            ButtonColumn buttonColumn = new ButtonColumn(jtblCaseData, delete, 10);
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

    }
    private void hide(JTable jtbl, int index) {
        TableColumn column = jtbl.getColumnModel().getColumn(index);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setWidth(0);
        column.setPreferredWidth(0);
        doLayout();
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
                    Logger.getLogger(CourtJudgement_Appeal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return bState;

    }

    private void btnNewCaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewCaseActionPerformed
        // TODO add your handling code here:
        jCaseDataPanel.setVisible(false);
        jCaseFullDataPanel.setVisible(true);
    }//GEN-LAST:event_btnNewCaseActionPerformed

    private void btnNewCase1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewCase1ActionPerformed
        // TODO add your handling code here:
        cboCourtType1.setSelectedIndex(0);
        cboCities1.setSelectedIndex(0);
        cboCaseType1.setSelectedIndex(0);
        cboDomain1.setSelectedIndex(0);
        cboRegion1.setSelectedIndex(0);
        dt_official_date1.setDate(null);
        txtCaseNumber1.setText(null);
        txtYear1.setText(null);
    }//GEN-LAST:event_btnNewCase1ActionPerformed

    private void jAppealDataPanel1ComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jAppealDataPanel1ComponentHidden
        // TODO add your handling code here:
    }//GEN-LAST:event_jAppealDataPanel1ComponentHidden

    private void btnSaveAppealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveAppealActionPerformed
        // TODO add your handling code here:
        ResultSet rsMainID = null;
        int insert_CJ_Main = 0;

        try {
            conStmt = con.createStatement();

            if (storeAppealData()) {
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
                        JOptionPane.showMessageDialog(frm, bundle.getString("CourtJudgement_Appeal.ResultSaved.text"));
                        jCaseDataPanel.setVisible(true);

                    } catch (SQLException ex) {
                        Logger.getLogger(CourtJudgement_Appeal.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

    }//GEN-LAST:event_btnSaveAppealActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNewCase;
    private javax.swing.JButton btnNewCase1;
    private javax.swing.JButton btnSaveAppeal;
    private javax.swing.JButton btnSaveCase;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox cboCaseType;
    private javax.swing.JComboBox cboCaseType1;
    private javax.swing.JComboBox cboCities;
    private javax.swing.JComboBox cboCities1;
    private javax.swing.JComboBox cboCourtType;
    private javax.swing.JComboBox cboCourtType1;
    private javax.swing.JComboBox cboCourtTypeC;
    private javax.swing.JComboBox cboDomain;
    private javax.swing.JComboBox cboDomain1;
    private javax.swing.JComboBox cboLanguage;
    private javax.swing.JComboBox cboRegion;
    private javax.swing.JComboBox cboRegion1;
    private javax.swing.JCheckBox chbJudgeChecked;
    private org.jdesktop.swingx.JXDatePicker dt_official_date;
    private org.jdesktop.swingx.JXDatePicker dt_official_date1;
    private javax.swing.JPanel jAppealDataPanel1;
    private javax.swing.JPanel jCaseDataPanel;
    private javax.swing.JPanel jCaseFullDataPanel;
    private javax.swing.JLabel lblCaseNo;
    private javax.swing.JLabel lblCaseNo1;
    private javax.swing.JLabel lblCaseType;
    private javax.swing.JLabel lblCaseType1;
    private javax.swing.JLabel lblCity;
    private javax.swing.JLabel lblCity1;
    private javax.swing.JLabel lblCourtType;
    private javax.swing.JLabel lblCourtType1;
    private javax.swing.JLabel lblCourtTypeC;
    private javax.swing.JLabel lblDomain;
    private javax.swing.JLabel lblDomain1;
    private javax.swing.JLabel lblImportance;
    private javax.swing.JLabel lblLanguage;
    private javax.swing.JLabel lblNoC;
    private javax.swing.JLabel lblOfficialDate;
    private javax.swing.JLabel lblOfficialDate1;
    private javax.swing.JLabel lblRegion;
    private javax.swing.JLabel lblRegion1;
    private javax.swing.JLabel lblYear;
    private javax.swing.JLabel lblYear1;
    private javax.swing.JLabel lblYearC;
    private javax.swing.JLabel slash;
    private javax.swing.JLabel slash1;
    private javax.swing.JLabel slash2;
    private javax.swing.JTextField txtCaseNumber;
    private javax.swing.JTextField txtCaseNumber1;
    private javax.swing.JTextField txtImportance;
    private javax.swing.JTextField txtNoC;
    private javax.swing.JTextField txtYear;
    private javax.swing.JTextField txtYear1;
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
//                put(lblCourtType1.getText().replace("*", ""), cboCaseType1);
//                put(lblDomain1.getText().replace("*", ""), cboDomain1);
//                put(lblCaseType1.getText().replace("*", ""), cboCourtType1);
//                put(lblCaseNo1.getText().replace("*", ""), txtCaseNumber1);
//                put(lblCity1.getText().replace("*", ""), cboCities1);
//                put(lblOfficialDate1.getText().replace("*", ""), dt_official_date1);
//                put(lblYear1.getText().replace("*", ""), txtYear1);
            }
        });
        return super.validateSelectedMetadata(spf);
    }
}
