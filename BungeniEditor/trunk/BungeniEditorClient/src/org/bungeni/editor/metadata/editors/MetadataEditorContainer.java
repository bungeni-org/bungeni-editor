package org.bungeni.editor.metadata.editors;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.bungeni.editor.config.BaseConfigReader;
import org.bungeni.editor.config.BungeniEditorPropertiesHelper;
import org.bungeni.editor.config.DocTypesReader;
import org.birzeit.editor.metadata.ActMainMetadataModel;
import org.birzeit.editor.metadata.ActResponsibleAuthoritiesModel;
import org.birzeit.editor.metadata.ActSourceModel;
import org.bungeni.editor.metadata.BaseEditorDocMetaModel;
import org.bungeni.editor.metadata.EditorDocMetadataDialogFactory;
import org.bungeni.editor.metadata.IEditorDocMetadataDialog;
import org.birzeit.editor.metadata.editors.ActMainMetadata;
import org.birzeit.editor.metadata.editors.ActResponsibleAuthorities;
import org.birzeit.editor.metadata.editors.ActSource;
import org.birzeit.editor.metadata.editors.ConnectorFunctions;
import org.birzeit.editor.metadata.editors.CourtJudgement_HighCourtofJustice_Constitutional_AntiCorruption;
import org.bungeni.editor.dialogs.editorApplicationController;
import org.birzeit.editor.metadata.JudgementMetadataModel;
import org.bungeni.editor.noa.BungeniNoaTabbedPane;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.extutils.*;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooDocMetadata;
import org.bungeni.ooo.transforms.impl.BungeniTransformationTargetFactory;
import org.bungeni.ooo.transforms.impl.IBungeniDocTransform;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.utils.BungeniFileSavePathFormat;
import org.bungeni.utils.BungeniFrame;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.odftoolkit.odfdom.type.DateTime;

/**
 * This JPanel class is the main container for the metadata tabs for a document
 * type. metadata tabs are loaded based on the metadata_model_editors configured
 * for a particular document type
 *
 * @author Ashok Hariharan
 */
public class MetadataEditorContainer extends JPanel {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MetadataEditorContainer.class.getName());
    private String EDIT_MESSAGE = bundle.getString("edit_metadata_with_form");
    OOComponentHelper ooDocument = null;
    JFrame parentFrame = null;
    SelectorDialogModes dlgMode = null;
    private Connection con;
    private ActSourceModel actSourceMetaModel = null;
    private JudgementMetadataModel judgementMetadataModel = null;
    private ActMainMetadataModel actMainMetadataModel = null;
    private ActResponsibleAuthoritiesModel actResponsibleAuthoritiesModel = null;
    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private JComboBox comboBox;
    private String courtJudgementType;
    Statement conStmt = null;
    /**
     * ArrayList to store all the available tabs for the document type.
     */
    ArrayList<IEditorDocMetadataDialog> metaTabs = new ArrayList<IEditorDocMetadataDialog>(0);
    BungeniFileSavePathFormat m_spf = null;
    NextTabAction nextAction = new NextTabAction(bundle.getString("btn_next"));
    PrevTabAction prevAction = new PrevTabAction(bundle.getString("btn_prev"));
    ApplyButtonSaveAction saveAction = new ApplyButtonSaveAction(bundle.getString("msg_save"));

    public MetadataEditorContainer() {
        super();
        InitializeMetadataModels();
        initComponents();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

    }

    public MetadataEditorContainer(OOComponentHelper ooDoc, JFrame parentFrm, SelectorDialogModes dlg) {
        super();

        initComponents();

        ooDocument = ooDoc;
        InitializeMetadataModels();

        parentFrame = parentFrm;
        dlgMode = dlg;
        if (dlgMode.equals(SelectorDialogModes.TEXT_EDIT)) {
            txtMsgArea.setText(EDIT_MESSAGE);
        }

        this.btnSave.setAction(saveAction);
        this.btnNavigateNext.setAction(nextAction);
        this.btnNavigatePrev.setAction(prevAction);
        this.metadataTabContainer.addChangeListener(new MetaTabsChangeListener());
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

    }

    private void InitializeMetadataModels() {


        actSourceMetaModel = ActSource.getDocMetaModel();
        actSourceMetaModel.loadModel(ooDocument);

        actMainMetadataModel = ActMainMetadata.getDocMetaModel();
        actMainMetadataModel.loadModel(ooDocument);

        actResponsibleAuthoritiesModel = ActResponsibleAuthorities.getDocMetaModel();
        actResponsibleAuthoritiesModel.loadModel(ooDocument);

        judgementMetadataModel = CourtJudgement_HighCourtofJustice_Constitutional_AntiCorruption.getDocMetaModel();
        judgementMetadataModel.loadModel(ooDocument);
    }

    class NextTabAction extends AbstractAction {

        public NextTabAction(String text) {
            super(text);
        }

        public void actionPerformed(ActionEvent e) {
            int nIndex = metadataTabContainer.getSelectedIndex();
            int nNoOfTabs = metadataTabContainer.getTabCount();
            if (!(nIndex == (nNoOfTabs - 1))) {
                metadataTabContainer.setSelectedIndex(nIndex + 1);
            }
        }
    }

    class PrevTabAction extends AbstractAction {

        public PrevTabAction(String text) {
            super(text);
        }

        public void actionPerformed(ActionEvent e) {
            int nIndex = metadataTabContainer.getSelectedIndex();
            if (nIndex > 0) {
                metadataTabContainer.setSelectedIndex(nIndex - 1);
            }
        }
    }

    class MetaTabsChangeListener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            int nNoOfTabs = metadataTabContainer.getTabCount();
            int iIndex = metadataTabContainer.getSelectedIndex();
            if (iIndex == 0) {
                btnNavigateNext.setAction(nextAction);
                btnNavigatePrev.setEnabled(false);
            } else if (iIndex == nNoOfTabs - 1) {
                btnNavigateNext.setEnabled(false);
                btnNavigatePrev.setEnabled(true);
                btnNavigatePrev.setAction(prevAction);
            } else {
                btnNavigateNext.setEnabled(true);
                btnNavigatePrev.setEnabled(true);
                btnNavigateNext.setAction(nextAction);
                btnNavigatePrev.setAction(prevAction);
            }
        }
    }

    /**
     * Loads the availabled metadata editors for this document type Retrieves
     * the applicable URI types for this document type
     */
    public void initialize() {
        // if(Locale.getDefault().getLanguage().equals("ar") && Locale.getDefault().getCountry().equals("PS") )
        //    CommonEditorFunctions.compOrientation(this);
        //get the available tabs for this document type
        //CommonUIFunctions.compOrientation(this);
        log.info("calling initialize .....");
        String currentDocType = BungeniEditorPropertiesHelper.getCurrentDocType();
        log.info("initialize : current doc type = " + currentDocType);

        this.metaTabs = EditorDocMetadataDialogFactory.getInstances(currentDocType);

        if ("CourtJudgements".equals(currentDocType)) {

            //        String[] petStrings = {bundle.getString("CourtJudgement_Cassation"), bundle.getString("CourtJudgement_Appeal"), bundle.getString("CourtJudgement_HighCourtofJustice_Constitutional_AntiCorruption")};
            String[] types = {bundle.getString("CourtJudgement_Cassation.text"), bundle.getString("CourtJudgement_Appeal.text"), bundle.getString("CourtJudgement_HighCourtofJustice_Constitutional_AntiCorruption.text"), bundle.getString("CourtJudgement_Talabat.text"), bundle.getString("CourtJudgement_eteradat.text"), bundle.getString("CourtJudgement_HighCourt_Generalbody.text")};
            comboBox = new JComboBox(types);
            comboBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            add(comboBox, BorderLayout.PAGE_START);

            final JFrame frame = new JFrame(bundle.getString("CourtJudgementType.text"));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(comboBox);
            frame.setName("ComboBoxExample");
            frame.pack();
            frame.setAlwaysOnTop(true);
            frame.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
            FrameLauncher.CenterFrame(frame);
            frame.setVisible(true);

            comboBox.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent event) {

                    JComboBox comboBox = (JComboBox) event.getSource();
                    Object selected = comboBox.getSelectedItem();

                    courtJudgementType = selected.toString();

                    for (int i = 0; i < metaTabs.size(); i++) {
                        if (!(metaTabs.get(i).getTabTitle().equals(courtJudgementType))) {
                            metaTabs.remove(i);
                            i--;
                        }
                    }

                    frame.setVisible(false);
                    BungeniFrame frm = editorApplicationController.getBungeniFrame();

                    frm.setVisible(true);
                    CommonUIFunctions.compOrientation(parentFrame);
                    for (IEditorDocMetadataDialog mTab : metaTabs) {
                        if (mTab == null) {
                            log.error("initialize : returned metadata tab is null");
                        } else {
                            mTab.initVariables(ooDocument, parentFrame, dlgMode);
                            log.info("initialize : after calling initVariables");
                            mTab.initialize();
                        }
                    }
                    Element doctypeElem = null;
                    try {
                        //get work, exp, manifestation formats :
                        doctypeElem = DocTypesReader.getInstance().getDocTypeByName(BungeniEditorPropertiesHelper.getCurrentDocType());
                    } catch (JDOMException ex) {
                        log.error("Error getting doctype config", ex);
                    }
                    if (null != doctypeElem) {
                        m_spf = new BungeniFileSavePathFormat(
                                DocTypesReader.getInstance().getWorkUriForDocType(doctypeElem),
                                DocTypesReader.getInstance().getExpUriForDocType(doctypeElem),
                                DocTypesReader.getInstance().getFileNameSchemeForDocType(doctypeElem));
                        //now load the newly created tabs
                        for (IEditorDocMetadataDialog thisTab : metaTabs) {
                            JScrollPane scrollPaneContainer = new JScrollPane();
                            scrollPaneContainer.setViewportView(thisTab.getPanelComponent());
                            metadataTabContainer.add(scrollPaneContainer, thisTab.getTabTitle());
                        }
                    }
                }
            });


        } else {
            BungeniFrame frm = editorApplicationController.getBungeniFrame();
            frm.setVisible(true);
            CommonUIFunctions.compOrientation(this);

            for (IEditorDocMetadataDialog mTab : this.metaTabs) {
                if (mTab == null) {
                    log.error("initialize : returned metadata tab is null");
                } else {
                    mTab.initVariables(ooDocument, parentFrame, dlgMode);
                    log.info("initialize : after calling initVariables");
                    mTab.initialize();
                }
            }

            Element doctypeElem = null;
            try {
                //get work, exp, manifestation formats :
                doctypeElem = DocTypesReader.getInstance().getDocTypeByName(BungeniEditorPropertiesHelper.getCurrentDocType());
            } catch (JDOMException ex) {
                log.error("Error getting doctype config", ex);
            }
            if (null != doctypeElem) {
                m_spf = new BungeniFileSavePathFormat(
                        DocTypesReader.getInstance().getWorkUriForDocType(doctypeElem),
                        DocTypesReader.getInstance().getExpUriForDocType(doctypeElem),
                        DocTypesReader.getInstance().getFileNameSchemeForDocType(doctypeElem));
                //now load the newly created tabs
                for (IEditorDocMetadataDialog thisTab : this.metaTabs) {
                    JScrollPane scrollPaneContainer = new JScrollPane();
                    scrollPaneContainer.setViewportView(thisTab.getPanelComponent());
                    metadataTabContainer.add(scrollPaneContainer, thisTab.getTabTitle());
                }
            }
        }

        CommonUIFunctions.compOrientation(this);
    }

    /**
     * Results iterator for retrieving metadata model info
     */
    /**
     * *
     * class metadataModelInfoIterator implements IQueryResultsIterator {
     *
     * public String WORK_URI, EXP_URI, MANIFESTATION_FORMAT ;
     *
     * public boolean iterateRow(QueryResults mQR, Vector<String> rowData) {
     * WORK_URI = mQR.getField(rowData, "WORK_URI"); EXP_URI =
     * mQR.getField(rowData, "EXP_URI"); MANIFESTATION_FORMAT =
     * mQR.getField(rowData, "FILE_NAME_SCHEME"); return true; } } *
     */
    public Component getPanelComponent() {
        return this;
    }
    ArrayList<String> formErrors = new ArrayList<String>(0);

    /**
     * validateSelectedMetadata
     *
     * @param m_spf
     * @return
     */
    private boolean validateSelectedMetadata(BungeniFileSavePathFormat m_spf) {
        boolean bState = false;
        try {
            formErrors.clear();
            //iterate through the tabs and apply them individually
            for (IEditorDocMetadataDialog mTab : this.metaTabs) {
                ArrayList<String> errors = mTab.validateSelectedMetadata(m_spf);
                if (errors.size() > 0) {
                    formErrors.add(mTab.getTabTitle() + " Tab :-");
                    formErrors.addAll(errors);
                    formErrors.add("");
                }
            }

        } catch (Exception ex) {
        } finally {
            return (formErrors.size() > 0) ? false : true;
        }
    }

    /**
     * Apply the metadata in all the available tabs into the document
     *
     * @param spf
     * @return
     */
    private boolean applySelectedMetadata(BungeniFileSavePathFormat spf) {

        boolean bState = false;
        try {
            //iterate through the tabs and apply them individually
            for (IEditorDocMetadataDialog mTab : this.metaTabs) {
                mTab.applySelectedMetadata(spf);
            }

            bState = true;
        } catch (Exception ex) {
            log.error("applySelectedMetadata : " + ex.getMessage());
            bState = false;
        } finally {
            return bState;
        }

    }

    private boolean SaveActToDB() throws SQLException {

        con = ConnectorFunctions.ConnectMMSM("Muqtafi_test");
        boolean bState = false;

        ResultSet rsMainID = null;
        int insert_CJ_Main = 0;
        int insert_CJ_BodyMain3 = 0;
        int insert_CJ_BodyMain1 = 0;
        int insert_CJ_BodyMain2 = 0;

        try {
            conStmt = con.createStatement();

            con.setAutoCommit(false);
            String sqlStm = "INSERT INTO [LG_Main] ([LG_Main_SrcID], [LG_Main_SrcDateGreg], [LG_Main_SrcDateHijri], [LG_Main_SrcPlace], [LG_Main_SrcIssue], "
                    + " [LG_Main_Title] , [LG_Main_Num], [LG_Main_Year] , [LG_Main_Type], [LG_Main_Status], [LG_Main_Area], [LG_Main_HisPer], [LG_Main_PgNumb], [LG_Main_DateForceGreg], [LG_Main_PgCount] , [LG_Main_Family], [LG_Main_Family_P], [LG_Main_LG_Category_ID], [LG_Main_LG_Basic_ID])"
                    + " VALUES(" + actSourceMetaModel.getItem("BungeniPublicationSrcNameID") + ", '" + actSourceMetaModel.getItem("BungeniPublicationDate")
                    + "' ,'" + actSourceMetaModel.getItem("BungeniPublicationDateHijri") + "','" + actSourceMetaModel.getItem("BungeniPublicationArea")
                    + "','" + actSourceMetaModel.getItem("BungeniSourceNo")
                    + "','" + actMainMetadataModel.getItem("BungeniActName") + "'," + actMainMetadataModel.getItem("BungeniActNo") + "," + actMainMetadataModel.getItem("BungeniActYear")
                    + "," + actMainMetadataModel.getItem("BungeniActTypeID") + ",'" + actMainMetadataModel.getItem("BungeniActState") + "'," + actMainMetadataModel.getItem("BungeniActScopeID")
                    + "," + actMainMetadataModel.getItem("BungeniActHistoricalPeriodID") + "," + actMainMetadataModel.getItem("BungeniPageNo") + ",'" + actMainMetadataModel.getItem("BungeniActEffectiveDate")
                    + "'," + actMainMetadataModel.getItem("BungeniPageCount") + "," + actMainMetadataModel.getItem("BungeniActFamilyID") + ","
                    + ((actMainMetadataModel.getItem("BungeniActFamilyPossibleID") != "") ? actMainMetadataModel.getItem("BungeniActFamilyPossibleID") : 0)
                    + "," + actMainMetadataModel.getItem("BungeniActCategoryID") + "," + actMainMetadataModel.getItem("BungeniActCategoryBasicID")
                    + ")";

            insert_CJ_Main = conStmt.executeUpdate(sqlStm);

            sqlStm = "SELECT max([LG_Main_M_ID]) FROM [LG_Main] ";
            rsMainID = conStmt.executeQuery(sqlStm);

            if (rsMainID.next()) {
                String LG_Main_ID = rsMainID.getString(1);

                actMainMetadataModel.updateItem("BungeniMainDocID", LG_Main_ID);
                actMainMetadataModel.saveModel(ooDocument);

                String sqlStmBodyMain3 = "INSERT INTO [LG_BodyMain] ([LG_BodyMain_M_ID], [LG_BodyMain_LG_Body_ID], [LG_BodyMain_Date], [LG_BodyMain_DateHijri], [LG_BodyMain_Type]) "
                        + "VALUES(" + LG_Main_ID + "," + actResponsibleAuthoritiesModel.getItem("BungeniActReleaseID") + ",'" + actResponsibleAuthoritiesModel.getItem("BungeniActReleaseDate") + "','" + actResponsibleAuthoritiesModel.getItem("BungeniActReleaseDateHijri") + "', 3)";
                insert_CJ_BodyMain3 = conStmt.executeUpdate(sqlStmBodyMain3);

                String sqlStmBodyMain1 = "INSERT INTO [LG_BodyMain] ([LG_BodyMain_M_ID], [LG_BodyMain_LG_Body_ID], [LG_BodyMain_Date], [LG_BodyMain_Type]) "
                        + "VALUES(" + LG_Main_ID + "," + actResponsibleAuthoritiesModel.getItem("BungeniActDeveloped") + ",'" + ((actResponsibleAuthoritiesModel.getItem("BungeniActDevelopedDate") != null) ? actResponsibleAuthoritiesModel.getItem("BungeniActDevelopedDate") : "") + "', 1)";
                insert_CJ_BodyMain1 = conStmt.executeUpdate(sqlStmBodyMain1);

                String sqlStmBodyMain2 = "INSERT INTO [LG_BodyMain] ([LG_BodyMain_M_ID], [LG_BodyMain_LG_Body_ID], [LG_BodyMain_Date], [LG_BodyMain_Type]) "
                        + "VALUES(" + LG_Main_ID + "," + actResponsibleAuthoritiesModel.getItem("BungeniActApproved") + ",'" + ((actResponsibleAuthoritiesModel.getItem("BungeniActApprovedDate") != null) ? actResponsibleAuthoritiesModel.getItem("BungeniActApprovedDate") : "") + "', 2)";
                insert_CJ_BodyMain2 = conStmt.executeUpdate(sqlStmBodyMain2);

            } else {
                MessageBox.OK("Recodr not found");
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
            if (insert_CJ_Main != 0 && insert_CJ_BodyMain3 != 0 && insert_CJ_BodyMain1 != 0 && insert_CJ_BodyMain2 != 0) {
                if (rsMainID != null) {
                    rsMainID.close();
                    con.commit();
                    con.setAutoCommit(true);
                }
            }
        }

        bState = true;
        return bState;
    }

    private boolean SaveCourtJudgementsToDB() throws Exception {

        con = ConnectorFunctions.ConnectMMSM("CourtJudgments2007_test");

        boolean bState = false;

        ResultSet rsMainID = null;
        int insert_CJ_Main = 0;

        try {
            conStmt = con.createStatement();

            con.setAutoCommit(false);
            String sqlStm = "INSERT INTO [CJ_Main] ([CJ_Main_CJ_CaseTypes_ID], [CJ_Main_CJ_Regions_ID], [CJ_Main_CJ_Domains_ID], [CJ_Main_CJ_CourtTypes_ID], [CJ_Main_CJ_Cities_ID], [CJ_Main_NumNo], "
                    + " [CJ_Main_DecDate] , [CJ_Main_NumYr], [CJ_Main_Importance])"
                    + " VALUES(" + judgementMetadataModel.getItem("BungeniCaseTypeID") + "," + judgementMetadataModel.getItem("BungeniRegionID")
                    + "," + judgementMetadataModel.getItem("BungeniDomainID") + "," + judgementMetadataModel.getItem("BungeniCourtTypeID")
                    + "," + judgementMetadataModel.getItem("BungeniCityID") + "," + judgementMetadataModel.getItem("BungeniCaseNo")
                    + ",'" + judgementMetadataModel.getItem("BungeniIssuedOn")
                    + "','" + judgementMetadataModel.getItem("BungeniYear") + "'," + judgementMetadataModel.getItem("BungeniImportance")
                    + ")";

            insert_CJ_Main = conStmt.executeUpdate(sqlStm);

            sqlStm = "SELECT max([CJ_Main_ID]) FROM [CJ_Main] ";
            rsMainID = conStmt.executeQuery(sqlStm);

            if (rsMainID.next()) {
                String CJ_Main_ID = rsMainID.getString(1);

                judgementMetadataModel.updateItem("BungeniMainDocID", CJ_Main_ID);
                judgementMetadataModel.saveModel(ooDocument);

            } else {
                MessageBox.OK("Record not found");
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
                    rsMainID.close();
                    con.commit();
                    con.setAutoCommit(true);
                }
            }
        }

        bState = true;
        return bState;
    }

    private boolean SaveFamilyCourtJudgementsToDB() throws Exception {

        con = ConnectorFunctions.ConnectMMSM("FamilyCourtJudgments_test");

        boolean bState = false;

        ResultSet rsMainID = null;
        int insert_CJ_Main = 0;

        try {
            conStmt = con.createStatement();

            con.setAutoCommit(false);
            String sqlStm = "INSERT INTO [CJ_Main] ([CJ_Main_CJ_CaseTypes_ID], [CJ_Main_CJ_Regions_ID], [CJ_Main_CJ_Domains_ID], [CJ_Main_CJ_CourtTypes_ID], [CJ_Main_CJ_Cities_ID], [CJ_Main_NumNo], "
                    + " [CJ_Main_DecDate] , [CJ_Main_NumYr], [CJ_Main_Importance])"
                    + " VALUES(" + judgementMetadataModel.getItem("BungeniCaseTypeID") + "," + judgementMetadataModel.getItem("BungeniRegionID")
                    + "," + judgementMetadataModel.getItem("BungeniDomainID") + "," + judgementMetadataModel.getItem("BungeniCourtTypeID")
                    + "," + judgementMetadataModel.getItem("BungeniCityID") + "," + judgementMetadataModel.getItem("BungeniCaseNo")
                    + ",'" + judgementMetadataModel.getItem("BungeniIssuedOn")
                    + "','" + judgementMetadataModel.getItem("BungeniYear") + "'," + judgementMetadataModel.getItem("BungeniImportance")
                    + ")";

            insert_CJ_Main = conStmt.executeUpdate(sqlStm);

            sqlStm = "SELECT max([CJ_Main_ID]) FROM [CJ_Main] ";
            rsMainID = conStmt.executeQuery(sqlStm);

            if (rsMainID.next()) {
                String CJ_Main_ID = rsMainID.getString(1);

                judgementMetadataModel.updateItem("BungeniMainDocID", CJ_Main_ID);
                judgementMetadataModel.saveModel(ooDocument);

            } else {
                MessageBox.OK("Record not found");
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
                    rsMainID.close();
                    con.commit();
                    con.setAutoCommit(true);
                }
            }
        }

        bState = true;
        return bState;
    }
    private final static String STORE_TO_URL = "StoreToURL";
    private final static String STORE_AS_URL = "StoreAsURL";

    /**
     * Saves the document to disk
     *
     * @param spf
     * @return
     */
    private boolean saveDocumentToDisk(BungeniFileSavePathFormat spf) {
        boolean bState = false;
        String savedFile = "";
        //1 check if file is already open and saved 
        //if open check if there is a new path for the document different from the current path
        //warn the user provide an option to save to the old path
        //2 if file is not saved - save normally - check if there is a file existing in the path and warn if exists
        try {
            //this is the relative base path where hte files are stored
            log.debug("saveDocumentToDisk: begin");
            //String defaultSavePath = BungeniEditorProperties.getEditorProperty("defaultSavePath");
            String defaultSavePath = BaseConfigReader.getWorkspaceFolder(); //defaultSavePath.replace('/', File.separatorChar);
            log.debug("saveDocumentToDisk: defaultSavePath : " + defaultSavePath);

            //get the absolute path
            String exportPath = defaultSavePath + m_spf.getExpressionFilePath();

            log.debug("saveDocumentToDisk : exportPath = " + exportPath);
            //get the full path to the file
            String fileFullPath = "";
            fileFullPath = exportPath + File.separator + spf.getManifestationName() + ".odt";
            // MessageBox.OK(fileFullPath);
            log.debug("saveDocumentToDisk : fileFullPath = " + fileFullPath);

            File fFile = new File(fileFullPath);
            savedFile = fFile.toURI().toString();
            HashMap<String, Object> saveParams = new HashMap<String, Object>();

            //1
            if (ooDocument.isDocumentOnDisk()) {
                log.debug("saveDocumentToDisk : file is on disk");
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
                    int nConfirm = MessageBox.Confirm(parentFrame, bundle.getString("prompt_doc_save_new_location"), bundle.getString("msg_warning"));
                    if (nConfirm == JOptionPane.YES_OPTION) {
                        //storeAsURL to new path
                        if (fFile.exists()) {
                            //error message and abort
                            NotifyBox.error(bundle.getString("file_exists"));
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
                log.debug("saveDocumentToDisk : file is NOT on disk");

                if (fFile.exists()) {
                    //error message and abort
                    NotifyBox.error(bundle.getString("file_exists2"));
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
            bState = idocTrans.transform(ooDocument);

        } catch (Exception ex) {
            log.error("saveDocumentToDisk : " + ex.getMessage());
            log.error("saveDocumentToDisk :" + CommonExceptionUtils.getStackTrace(ex));
            bState = false;
        }
        if (bState) {
            this.txtMsgArea.setText(EDIT_MESSAGE);
            BungeniRuntimeProperties.setProperty("SAVED_FILE", savedFile);
            /// Update the title of the tab in the noatabbedpane
            String newFrameTitle = OOComponentHelper.getFrameTitle(ooDocument.getTextDocument());
            int nSelected = BungeniNoaTabbedPane.getInstance().getTabbedPane().getSelectedIndex();
            BungeniNoaTabbedPane.getInstance().getTabbedPane().setTitleAt(nSelected, newFrameTitle);
            /// Update - End
            return true;
        } else {
            NotifyBox.error(bundle.getString("doc_not_saved"));
            return false;
        }
    }
    private static final ResourceBundle bundle = ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle");

    class ApplyButtonSaveAction extends AbstractAction {

        public ApplyButtonSaveAction(String text) {
            super(text);
        }

        public void actionPerformed(ActionEvent e) {

            if (validateSelectedMetadata(m_spf)) {
                if (applySelectedMetadata(m_spf)) {
                    applyMetadataSetFlag();

                    if (BungeniEditorPropertiesHelper.getCurrentDocType().equals("act")) {
                        try {
                            SaveActToDB();
                        } catch (SQLException ex) {
                            Logger.getLogger(MetadataEditorContainer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (BungeniEditorPropertiesHelper.getCurrentDocType().equals("CourtJudgements")) {
                        try {
                            SaveCourtJudgementsToDB();
                        } catch (Exception ex) {
                            Logger.getLogger(MetadataEditorContainer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    if (BungeniEditorPropertiesHelper.getCurrentDocType().equals("FamilyCourtJudgements")) {
                        try {
                            SaveFamilyCourtJudgementsToDB();
                        } catch (Exception ex) {
                            Logger.getLogger(MetadataEditorContainer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                if (saveDocumentToDisk(m_spf)) {
                    parentFrame.dispose();
                }
            } else {
                StringBuilder bf = new StringBuilder();
                for (String msg : formErrors) {
                    bf.append(msg).append("\n");
                }
                //MessageBox.OK(parentFrame, bf.toString(), bundle.getString("incomplete_fields"), JOptionPane.ERROR_MESSAGE);
                MessageBox.OK(parentFrame, bf.toString(), bundle.getString("incomplete_fields"), JOptionPane.ERROR_MESSAGE);
            }

        }

        private void applyMetadataSetFlag() {
            ooDocMetadata meta = new ooDocMetadata(ooDocument);
            meta.AddProperty(BaseEditorDocMetaModel.__METADATA_SET_FLAG__, "true");
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

        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMsgArea = new javax.swing.JTextArea();
        metadataTabContainer = new javax.swing.JTabbedPane();
        btnNavigateNext = new javax.swing.JButton();
        btnNavigatePrev = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();

        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setPreferredSize(new java.awt.Dimension(450, 550));

        btnSave.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        btnSave.setText(bundle.getString("MetadataEditorContainer.btnSave.text")); // NOI18N

        btnCancel.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnCancel.setText(bundle.getString("MetadataEditorContainer.btnCancel.text")); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        txtMsgArea.setBackground(java.awt.Color.lightGray);
        txtMsgArea.setColumns(20);
        txtMsgArea.setEditable(false);
        txtMsgArea.setLineWrap(true);
        txtMsgArea.setRows(5);
        txtMsgArea.setText(bundle.getString("MetadataEditorContainer.txtMsgArea.text")); // NOI18N
        txtMsgArea.setWrapStyleWord(true);
        txtMsgArea.setBorder(null);
        jScrollPane1.setViewportView(txtMsgArea);

        btnNavigateNext.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnNavigateNext.setText(bundle.getString("MetadataEditorContainer.btnNavigateNext.text")); // NOI18N

        btnNavigatePrev.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnNavigatePrev.setText(bundle.getString("MetadataEditorContainer.btnNavigatePrev.text")); // NOI18N

        btnDelete.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnDelete.setText(bundle.getString("MetadataEditorContainer.btnDelete.text")); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnEdit.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnEdit.setText(bundle.getString("MetadataEditorContainer.btnEdit.text")); // NOI18N
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(metadataTabContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnNavigatePrev, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnNavigateNext, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(metadataTabContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelete)
                    .addComponent(btnEdit)
                    .addComponent(btnSave))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnNavigateNext)
                    .addComponent(btnNavigatePrev))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
// TODO add your handling code here:
    parentFrame.dispose();
}//GEN-LAST:event_btnCancelActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:

        int rsMain = 0;
        int rsBodyMain = 0;

        try {
            conStmt = con.createStatement();

            con.setAutoCommit(false);
            String sqlStm = "DELETE FROM [LG_Main] WHERE [LG_Main_M_ID]=" + actMainMetadataModel.getItem("BungeniMainDocID");
            rsMain = conStmt.executeUpdate(sqlStm);

            sqlStm = "DELETE FROM [LG_BodyMain] WHERE [LG_BodyMain_M_ID]=" + actMainMetadataModel.getItem("BungeniMainDocID");
            rsBodyMain = conStmt.executeUpdate(sqlStm);

        } catch (SQLException e) {
            if (conStmt != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    con.rollback();
                } catch (SQLException excep) {
                }
            }
        } finally {
            if (rsMain != 0 && rsBodyMain != 0) {
                try {
                    con.commit();
                    con.setAutoCommit(true);
                    MessageBox.OK(bundle.getString("MetadataEditorContainer.btnDeleteMsg.text"));

                } catch (SQLException ex) {
                    Logger.getLogger(MetadataEditorContainer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        if (validateSelectedMetadata(m_spf)) {
            applySelectedMetadata(m_spf);
        }
        int insert_CJ_Main = 0;
        int insert_CJ_BodyMain3 = 0;
        int insert_CJ_BodyMain1 = 0;
        int insert_CJ_BodyMain2 = 0;

        try {
            conStmt = con.createStatement();

            con.setAutoCommit(false);
            String sqlStm = "UPDATE [LG_Main] SET [LG_Main_SrcID]=" + actSourceMetaModel.getItem("BungeniPublicationSrcNameID")
                    + ", [LG_Main_SrcDateGreg]='" + actSourceMetaModel.getItem("BungeniPublicationDate")
                    + "', [LG_Main_SrcDateHijri]='" + actSourceMetaModel.getItem("BungeniPublicationDateHijri")
                    + "', [LG_Main_SrcPlace]='" + actSourceMetaModel.getItem("BungeniPublicationArea")
                    + "', [LG_Main_SrcIssue]=" + actSourceMetaModel.getItem("BungeniSourceNo")
                    + ",[LG_Main_Title]='" + actMainMetadataModel.getItem("BungeniActName")
                    + "',[LG_Main_Num]=" + actMainMetadataModel.getItem("BungeniActNo")
                    + ", [LG_Main_Year]=" + actMainMetadataModel.getItem("BungeniActYear")
                    + ", [LG_Main_Type]=" + actMainMetadataModel.getItem("BungeniActTypeID")
                    + ", [LG_Main_Status]='" + actMainMetadataModel.getItem("BungeniActState")
                    + "', [LG_Main_Area]=" + actMainMetadataModel.getItem("BungeniActScopeID")
                    + ", [LG_Main_HisPer]=" + actMainMetadataModel.getItem("BungeniActHistoricalPeriodID")
                    + ", [LG_Main_PgNumb]=" + actMainMetadataModel.getItem("BungeniPageNo")
                    + ", [LG_Main_DateForceGreg]='" + actMainMetadataModel.getItem("BungeniActEffectiveDate")
                    + "', [LG_Main_PgCount]=" + actMainMetadataModel.getItem("BungeniPageCount")
                    + " , [LG_Main_Family]=" + actMainMetadataModel.getItem("BungeniActFamilyID")
                    + ", [LG_Main_Family_P]=" + actMainMetadataModel.getItem("BungeniActFamilyPossibleID")
                    + ", [LG_Main_LG_Category_ID]=" + actMainMetadataModel.getItem("BungeniActCategoryID")
                    + ", [LG_Main_LG_Basic_ID]=" + actMainMetadataModel.getItem("BungeniActCategoryBasicID") + " WHERE [LG_Main_M_ID]=" + actMainMetadataModel.getItem("BungeniMainDocID") + "";

            insert_CJ_Main = conStmt.executeUpdate(sqlStm);


            String sqlStmBodyMain3 = "UPDATE [LG_BodyMain] SET "
                    + "[LG_BodyMain_LG_Body_ID]=" + actResponsibleAuthoritiesModel.getItem("BungeniActReleaseID")
                    + ", [LG_BodyMain_Date]='" + actResponsibleAuthoritiesModel.getItem("BungeniActReleaseDate")
                    + "', [LG_BodyMain_DateHijri]='" + actResponsibleAuthoritiesModel.getItem("BungeniActReleaseDateHijri")
                    + "' WHERE [LG_BodyMain_M_ID]=" + actMainMetadataModel.getItem("BungeniMainDocID") + "";
            insert_CJ_BodyMain3 = conStmt.executeUpdate(sqlStmBodyMain3);

            String sqlStmBodyMain1 = "UPDATE [LG_BodyMain] SET "
                    + "[LG_BodyMain_LG_Body_ID]=" + actResponsibleAuthoritiesModel.getItem("BungeniActDeveloped")
                    + ", [LG_BodyMain_Date]='" + ((actResponsibleAuthoritiesModel.getItem("BungeniActDevelopedDate") != null) ? actResponsibleAuthoritiesModel.getItem("BungeniActDevelopedDate") : "")
                    + "' WHERE [LG_BodyMain_M_ID]=" + actMainMetadataModel.getItem("BungeniMainDocID") + "";
            insert_CJ_BodyMain1 = conStmt.executeUpdate(sqlStmBodyMain1);

            String sqlStmBodyMain2 = "UPDATE [LG_BodyMain] SET "
                    + "[LG_BodyMain_LG_Body_ID]=" + actResponsibleAuthoritiesModel.getItem("BungeniActApproved")
                    + ", [LG_BodyMain_Date]='" + ((actResponsibleAuthoritiesModel.getItem("BungeniActApprovedDate") != null) ? actResponsibleAuthoritiesModel.getItem("BungeniActApprovedDate") : "")
                    + "' WHERE [LG_BodyMain_M_ID]=" + actMainMetadataModel.getItem("BungeniMainDocID") + "";
            insert_CJ_BodyMain2 = conStmt.executeUpdate(sqlStmBodyMain2);



        } catch (SQLException e) {
            if (conStmt != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    con.rollback();
                } catch (SQLException excep) {
                }
            }
        } finally {
            if (insert_CJ_Main != 0 && insert_CJ_BodyMain3 != 0 && insert_CJ_BodyMain1 != 0 && insert_CJ_BodyMain2 != 0) {
                try {
                    con.commit();
                    con.setAutoCommit(true);
                } catch (SQLException ex) {
                    Logger.getLogger(MetadataEditorContainer.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }



    }//GEN-LAST:event_btnEditActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnNavigateNext;
    private javax.swing.JButton btnNavigatePrev;
    private javax.swing.JButton btnSave;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane metadataTabContainer;
    private javax.swing.JTextArea txtMsgArea;
    // End of variables declaration//GEN-END:variables

    /**
     * Static api to launch metadata editor for a document
     *
     * @param oohc OpenOffice document handle
     * @param dlgMode mode with which to open metadata dialog edit / insert
     * @return Handle to metadata editor window frame
     */
    public static JFrame launchMetadataEditor(OOComponentHelper oohc, SelectorDialogModes dlgMode) {
        String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
        BungeniFrame frm = new BungeniFrame("frameTitle");
        frm.initFrame();
        MetadataEditorContainer meta = new MetadataEditorContainer(oohc, frm, dlgMode);
        meta.initialize();
        frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frm.setSize(meta.getPreferredSize());
        frm.add(meta.getPanelComponent());
        frm.setVisible(true);
        FrameLauncher.CenterFrame(frm);
        //  frm.setAlwaysOnTop(true);
        return frm;
    }
}
