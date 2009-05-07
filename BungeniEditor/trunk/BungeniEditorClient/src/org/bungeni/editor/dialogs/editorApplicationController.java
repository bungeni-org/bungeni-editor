package org.bungeni.editor.dialogs;

import com.sun.star.awt.XWindow;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.frame.XModel;
import com.sun.star.lang.XComponent;
import com.sun.star.uno.XComponentContext;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.editor.metadata.editors.MetadataEditorContainer;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.ooo.BungenioOoHelper;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;
import org.bungeni.extutils.BungeniFrame;
import org.bungeni.extutils.CommonFileFunctions;
import org.bungeni.utils.FileTableModel;
import org.bungeni.extutils.FrameLauncher;
import org.bungeni.utils.Installation;
import org.bungeni.utils.WebDavStore;
import org.bungeni.utils.WebDavTableModel;
import org.bungeni.utils.WorkspaceFolderTableModel;

/**
 * Controller Class for Main Swing Openoffice UNO management application
 * @author Ashok Hariharan
 */
public class editorApplicationController extends javax.swing.JPanel {
    private  XComponentContext m_xContext;
    private java.util.Properties m_propSettings;
    private Installation m_installObject;
    private static String __WINDOW_TITLE__ = "Bungeni Editor Client ";
    private WebDavStore m_dav;
    private JFrame parentFrame;

    //path to settings.properties
    private String m_iniFilePath;
    
    private String m_settings_WorkspacePath;
    private String m_settings_ServerIP;
    private String m_settings_ServerPort;
    private String m_settings_ServerPath;
    private String m_settings_ServerUser;
    private String m_settings_ServerPassword;
   
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(editorApplicationController.class.getName());
    private String m_FullWorkspacePath ;
    private String m_FullTemplatesPath ;
    private String m_currentTemplate;  
    private String m_currentSelectedWorkspaceFile;
    private String m_settings_CurrentTemplate;
    private String m_currentMode ;
    private static String normalizedTemplatePath = "";
           
    private org.bungeni.editor.dialogs.editorTabbedPanel panel = null;
    private String m_FullFilesPath;
    private org.bungeni.ooo.BungenioOoHelper openofficeObject = null;
    private documentType[] m_documentTypes = null;
    /**
     * Constructor for editorApplicationController Class
     */
    /*
    public editorApplicationController() {
        initComponents();
    }*/
    
    /**
     * Main constructor for the class, initializes Panels and oOo environment
     * @param context Openoffice component context
     */
    public editorApplicationController(JFrame pFrame) {
        log.debug("in constructor");
        try {
            m_xContext = Bootstrap.bootstrap();
        } catch (BootstrapException ex) {
           log.error("editorApplicationController bootstrap : " + ex.getMessage());
        }
        m_FullWorkspacePath = "";
        m_FullTemplatesPath = "";
        m_currentSelectedWorkspaceFile = "";
        m_iniFilePath = "";
        m_propSettings = new java.util.Properties();
        m_dav = new WebDavStore();
        initComponents();
        this.parentFrame = pFrame;
        //temporarily remove these two tabs
        this.editorAppTabbedPane.remove(tabSettings);
        this.editorAppTabbedPane.remove(tabServer);
        this.editorAppTabbedPane.remove(this.tabAbout);

    }

    public void init() {
        CommonFileFunctions cfsObject=new CommonFileFunctions();
         m_installObject = new Installation();
        File dir =Installation.getInstallDirectory(this.getClass());
        //code to read properties file

        initDocumentTypesModel();
        initProperties(dir);
         //init panels
        initFileTableModels(dir);
        initWorkspaceFolderModels(dir);
        initPanels(dir);
        //initWebDav();
        initDataReader();
    }
   
 
    class ODTFileFilter extends FileFilter {

        @Override
        public boolean accept(File arg0) {
            if (arg0.isDirectory()) return true;
       
            String extension = getExtension(arg0);
            if (extension.equals("odt")) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String getDescription() {
           return "OpenDocument files";
        }
        
        private String getExtension(File fname){
            String filename = fname.getName();
            int i = filename.lastIndexOf(".");
            if (i > 0 && i < filename.length() - 1)
                return filename.substring(i+1).toLowerCase();
            return "";
        }
       
        
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        editorAppTabbedPane = new javax.swing.JTabbedPane();
        tabCurrentFile = new javax.swing.JPanel();
        createNewDocument = new javax.swing.JButton();
        lblCurrentTemplate = new javax.swing.JLabel();
        lblCurrentTemplateText = new javax.swing.JLabel();
        cboDocumentTypes = new javax.swing.JComboBox();
        lblDocumentTypes = new javax.swing.JLabel();
        btnOpenExisting = new javax.swing.JButton();
        lblCurrentActiveMode = new javax.swing.JLabel();
        btnStartAndAccquire = new javax.swing.JButton();
        lblCreateNewDoc = new javax.swing.JLabel();
        lblOpenCurrentDoc = new javax.swing.JLabel();
        lblLaunchAndAccquire = new javax.swing.JLabel();
        btnImportNew = new javax.swing.JButton();
        tabTemplates = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTemplatesList = new javax.swing.JTable();
        lblTemplatePath = new javax.swing.JLabel();
        lblSelectedTemplate = new javax.swing.JLabel();
        btnSetCurrentTemplate = new javax.swing.JButton();
        tabWorkspace = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblWorkspaceFolder = new javax.swing.JTable();
        lblSelectedFile = new javax.swing.JLabel();
        btnEditWorkspaceDocument = new javax.swing.JButton();
        tabSettings = new javax.swing.JPanel();
        lblWorkspacePath = new javax.swing.JLabel();
        txtWorkspacePath = new javax.swing.JTextField();
        btnBrowseWorkspacePath = new javax.swing.JButton();
        lblServerIP = new javax.swing.JLabel();
        txtServerIp = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        lblServerPort = new javax.swing.JLabel();
        txtServerPort = new javax.swing.JTextField();
        lblServerHomeDir = new javax.swing.JLabel();
        txtServerHomeDir = new javax.swing.JTextField();
        lblCurrentDirectory = new javax.swing.JLabel();
        checkBoxConnectOnStartup = new javax.swing.JCheckBox();
        btnSaveSettings = new javax.swing.JButton();
        tabServer = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblServerFiles = new javax.swing.JTable();
        progressServerFiles = new javax.swing.JProgressBar();
        btnBackOneFolder = new javax.swing.JButton();
        tabAbout = new javax.swing.JPanel();
        lblApplnTitle = new javax.swing.JLabel();

        editorAppTabbedPane.setFont(new java.awt.Font("DejaVu Sans", 0, 11));

        createNewDocument.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/dialogs/Bundle"); // NOI18N
        createNewDocument.setText(bundle.getString("editorApplicationController.createNewDocument.text")); // NOI18N
        createNewDocument.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                launchFrameActionPerformed(evt);
            }
        });

        lblCurrentTemplate.setFont(new java.awt.Font("Tahoma", 0, 11));
        lblCurrentTemplate.setText(bundle.getString("editorApplicationController.lblCurrentTemplate.text")); // NOI18N

        cboDocumentTypes.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        cboDocumentTypes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblDocumentTypes.setFont(new java.awt.Font("Tahoma", 0, 11));
        lblDocumentTypes.setText(bundle.getString("editorApplicationController.lblDocumentTypes.text")); // NOI18N

        btnOpenExisting.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        btnOpenExisting.setText(bundle.getString("editorApplicationController.btnOpenExisting.text")); // NOI18N
        btnOpenExisting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenExistingActionPerformed(evt);
            }
        });

        lblCurrentActiveMode.setBackground(java.awt.Color.lightGray);
        lblCurrentActiveMode.setFont(lblCurrentActiveMode.getFont().deriveFont(lblCurrentActiveMode.getFont().getStyle() | java.awt.Font.BOLD, lblCurrentActiveMode.getFont().getSize()+4));
        lblCurrentActiveMode.setText(bundle.getString("editorApplicationController.lblCurrentActiveMode.text")); // NOI18N
        lblCurrentActiveMode.setOpaque(true);

        btnStartAndAccquire.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        btnStartAndAccquire.setText(bundle.getString("editorApplicationController.btnStartAndAccquire.text")); // NOI18N

        lblCreateNewDoc.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblCreateNewDoc.setText(bundle.getString("editorApplicationController.lblCreateNewDoc.text")); // NOI18N

        lblOpenCurrentDoc.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblOpenCurrentDoc.setText(bundle.getString("editorApplicationController.lblOpenCurrentDoc.text")); // NOI18N

        lblLaunchAndAccquire.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblLaunchAndAccquire.setText(bundle.getString("editorApplicationController.lblLaunchAndAccquire.text")); // NOI18N

        btnImportNew.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        btnImportNew.setText(bundle.getString("editorApplicationController.btnImportNew.text")); // NOI18N
        btnImportNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportNewActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout tabCurrentFileLayout = new org.jdesktop.layout.GroupLayout(tabCurrentFile);
        tabCurrentFile.setLayout(tabCurrentFileLayout);
        tabCurrentFileLayout.setHorizontalGroup(
            tabCurrentFileLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabCurrentFileLayout.createSequentialGroup()
                .addContainerGap()
                .add(tabCurrentFileLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(tabCurrentFileLayout.createSequentialGroup()
                        .add(tabCurrentFileLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblCurrentTemplate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 455, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(tabCurrentFileLayout.createSequentialGroup()
                                .add(cboDocumentTypes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 210, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(48, 48, 48)
                                .add(lblCurrentActiveMode, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(lblCurrentTemplateText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 405, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(tabCurrentFileLayout.createSequentialGroup()
                                .add(createNewDocument, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 210, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblCreateNewDoc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 311, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(lblDocumentTypes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 245, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(250, 250, 250))
                    .add(tabCurrentFileLayout.createSequentialGroup()
                        .add(tabCurrentFileLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(tabCurrentFileLayout.createSequentialGroup()
                                .add(btnOpenExisting, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 210, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblOpenCurrentDoc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 418, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(tabCurrentFileLayout.createSequentialGroup()
                                .add(btnStartAndAccquire, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 210, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblLaunchAndAccquire, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 311, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(13, Short.MAX_VALUE))
                    .add(tabCurrentFileLayout.createSequentialGroup()
                        .add(btnImportNew, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 210, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(443, Short.MAX_VALUE))))
        );
        tabCurrentFileLayout.setVerticalGroup(
            tabCurrentFileLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabCurrentFileLayout.createSequentialGroup()
                .addContainerGap()
                .add(tabCurrentFileLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(tabCurrentFileLayout.createSequentialGroup()
                        .add(lblDocumentTypes)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(tabCurrentFileLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(cboDocumentTypes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(tabCurrentFileLayout.createSequentialGroup()
                                .add(6, 6, 6)
                                .add(lblCurrentTemplateText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                    .add(lblCurrentActiveMode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblCurrentTemplate)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tabCurrentFileLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(lblCreateNewDoc, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(createNewDocument, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnImportNew, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(4, 4, 4)
                .add(tabCurrentFileLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnOpenExisting, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblOpenCurrentDoc, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tabCurrentFileLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnStartAndAccquire, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblLaunchAndAccquire, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .add(70, 70, 70))
        );

        editorAppTabbedPane.addTab(bundle.getString("editorApplicationController.tabCurrentFile.TabConstraints.tabTitle"), tabCurrentFile); // NOI18N

        tblTemplatesList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblTemplatesList);

        btnSetCurrentTemplate.setText(bundle.getString("editorApplicationController.btnSetCurrentTemplate.text")); // NOI18N
        btnSetCurrentTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetCurrentTemplateActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout tabTemplatesLayout = new org.jdesktop.layout.GroupLayout(tabTemplates);
        tabTemplates.setLayout(tabTemplatesLayout);
        tabTemplatesLayout.setHorizontalGroup(
            tabTemplatesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabTemplatesLayout.createSequentialGroup()
                .addContainerGap()
                .add(tabTemplatesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblTemplatePath, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE)
                    .add(lblSelectedTemplate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 367, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, btnSetCurrentTemplate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 191, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        tabTemplatesLayout.setVerticalGroup(
            tabTemplatesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabTemplatesLayout.createSequentialGroup()
                .addContainerGap()
                .add(lblTemplatePath)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblSelectedTemplate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(21, 21, 21)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 154, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnSetCurrentTemplate)
                .addContainerGap(59, Short.MAX_VALUE))
        );

        editorAppTabbedPane.addTab(bundle.getString("editorApplicationController.tabTemplates.TabConstraints.tabTitle"), tabTemplates); // NOI18N

        tblWorkspaceFolder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblWorkspaceFolder);

        btnEditWorkspaceDocument.setText(bundle.getString("editorApplicationController.btnEditWorkspaceDocument.text")); // NOI18N
        btnEditWorkspaceDocument.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditWorkspaceDocumentActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout tabWorkspaceLayout = new org.jdesktop.layout.GroupLayout(tabWorkspace);
        tabWorkspace.setLayout(tabWorkspaceLayout);
        tabWorkspaceLayout.setHorizontalGroup(
            tabWorkspaceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabWorkspaceLayout.createSequentialGroup()
                .addContainerGap()
                .add(tabWorkspaceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE)
                    .add(lblSelectedFile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 367, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, btnEditWorkspaceDocument, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 197, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        tabWorkspaceLayout.setVerticalGroup(
            tabWorkspaceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabWorkspaceLayout.createSequentialGroup()
                .addContainerGap()
                .add(lblSelectedFile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(17, 17, 17)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 177, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnEditWorkspaceDocument)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        editorAppTabbedPane.addTab(bundle.getString("editorApplicationController.tabWorkspace.TabConstraints.tabTitle"), tabWorkspace); // NOI18N

        tabSettings.setFont(new java.awt.Font("Tahoma", 1, 11));

        lblWorkspacePath.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblWorkspacePath.setText(bundle.getString("editorApplicationController.lblWorkspacePath.text")); // NOI18N

        btnBrowseWorkspacePath.setText(bundle.getString("editorApplicationController.btnBrowseWorkspacePath.text")); // NOI18N
        btnBrowseWorkspacePath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseWorkspacePathActionPerformed(evt);
            }
        });

        lblServerIP.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblServerIP.setText(bundle.getString("editorApplicationController.lblServerIP.text")); // NOI18N

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        lblServerPort.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblServerPort.setText(bundle.getString("editorApplicationController.lblServerPort.text")); // NOI18N

        lblServerHomeDir.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblServerHomeDir.setText(bundle.getString("editorApplicationController.lblServerHomeDir.text")); // NOI18N

        checkBoxConnectOnStartup.setFont(new java.awt.Font("Tahoma", 1, 11));
        checkBoxConnectOnStartup.setText(bundle.getString("editorApplicationController.checkBoxConnectOnStartup.text")); // NOI18N
        checkBoxConnectOnStartup.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        btnSaveSettings.setText(bundle.getString("editorApplicationController.btnSaveSettings.text")); // NOI18N

        org.jdesktop.layout.GroupLayout tabSettingsLayout = new org.jdesktop.layout.GroupLayout(tabSettings);
        tabSettings.setLayout(tabSettingsLayout);
        tabSettingsLayout.setHorizontalGroup(
            tabSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .add(tabSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(tabSettingsLayout.createSequentialGroup()
                        .add(lblCurrentDirectory, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 443, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .add(tabSettingsLayout.createSequentialGroup()
                        .add(tabSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(tabSettingsLayout.createSequentialGroup()
                                .add(txtWorkspacePath, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 334, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnBrowseWorkspacePath))
                            .add(lblWorkspacePath, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 174, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(tabSettingsLayout.createSequentialGroup()
                                .add(tabSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(lblServerIP)
                                    .add(txtServerIp, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
                                .add(28, 28, 28)
                                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 62, Short.MAX_VALUE)
                                .add(tabSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(txtServerPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 107, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(lblServerPort)
                                    .add(checkBoxConnectOnStartup))))
                        .add(92, 92, 92))
                    .add(tabSettingsLayout.createSequentialGroup()
                        .add(lblServerHomeDir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 186, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(467, Short.MAX_VALUE))
                    .add(tabSettingsLayout.createSequentialGroup()
                        .add(txtServerHomeDir, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                        .add(388, 388, 388))))
            .add(tabSettingsLayout.createSequentialGroup()
                .add(208, 208, 208)
                .add(btnSaveSettings, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 145, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(312, Short.MAX_VALUE))
        );
        tabSettingsLayout.setVerticalGroup(
            tabSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .add(tabSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(btnBrowseWorkspacePath)
                    .add(tabSettingsLayout.createSequentialGroup()
                        .add(lblWorkspacePath)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtWorkspacePath, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblCurrentDirectory, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tabSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jSeparator1)
                    .add(tabSettingsLayout.createSequentialGroup()
                        .add(lblServerIP)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtServerIp, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(tabSettingsLayout.createSequentialGroup()
                        .add(lblServerPort)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtServerPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(21, 21, 21)
                .add(lblServerHomeDir)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tabSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtServerHomeDir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(checkBoxConnectOnStartup))
                .add(31, 31, 31)
                .add(btnSaveSettings)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        editorAppTabbedPane.addTab(bundle.getString("editorApplicationController.tabSettings.TabConstraints.tabTitle"), tabSettings); // NOI18N

        tblServerFiles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tblServerFiles);

        btnBackOneFolder.setText(bundle.getString("editorApplicationController.btnBackOneFolder.text")); // NOI18N
        btnBackOneFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackOneFolder_Clicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout tabServerLayout = new org.jdesktop.layout.GroupLayout(tabServer);
        tabServer.setLayout(tabServerLayout);
        tabServerLayout.setHorizontalGroup(
            tabServerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabServerLayout.createSequentialGroup()
                .addContainerGap()
                .add(tabServerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(progressServerFiles, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE)
                    .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE)
                    .add(btnBackOneFolder))
                .addContainerGap())
        );
        tabServerLayout.setVerticalGroup(
            tabServerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabServerLayout.createSequentialGroup()
                .add(20, 20, 20)
                .add(btnBackOneFolder)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 175, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(progressServerFiles, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        editorAppTabbedPane.addTab(bundle.getString("editorApplicationController.tabServer.TabConstraints.tabTitle"), tabServer); // NOI18N

        org.jdesktop.layout.GroupLayout tabAboutLayout = new org.jdesktop.layout.GroupLayout(tabAbout);
        tabAbout.setLayout(tabAboutLayout);
        tabAboutLayout.setHorizontalGroup(
            tabAboutLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 665, Short.MAX_VALUE)
        );
        tabAboutLayout.setVerticalGroup(
            tabAboutLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );

        editorAppTabbedPane.addTab(bundle.getString("editorApplicationController.tabAbout.TabConstraints.tabTitle"), tabAbout); // NOI18N

        lblApplnTitle.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblApplnTitle.setText(bundle.getString("editorApplicationController.lblApplnTitle.text")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(lblApplnTitle, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 669, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, editorAppTabbedPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 669, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(lblApplnTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(editorAppTabbedPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 328, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditWorkspaceDocumentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditWorkspaceDocumentActionPerformed
// TODO add your handling code here:
        
        //JOptionPane.showMessageDialog(null, m_currentSelectedWorkspaceFile);
        SwingUtilities.invokeLater(new Runnable(){
       
            public void run() {
                  initoOoAndLaunchFrame(m_currentSelectedWorkspaceFile, false);
            }

        });
        
    }//GEN-LAST:event_btnEditWorkspaceDocumentActionPerformed

    private void btnSetCurrentTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetCurrentTemplateActionPerformed
// TODO add your handling code here:
        String currentlySelectedTemplateinTable = m_settings_CurrentTemplate;
        FileOutputStream fso;
        try {
            fso = new FileOutputStream(m_iniFilePath);
        
        m_propSettings.setProperty("workspace.currenttemplate", currentlySelectedTemplateinTable );
            m_propSettings.store(fso, "" );
        }     catch (FileNotFoundException ex){
             log.debug(m_iniFilePath +" file not found : " + ex.getLocalizedMessage(), ex); 
          } 
               catch (IOException ex) {
            log.debug(ex.getLocalizedMessage(), ex);
        }
    
        
        
    }//GEN-LAST:event_btnSetCurrentTemplateActionPerformed

    private void btnBackOneFolder_Clicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackOneFolder_Clicked
        // TODO add your handling code here:
        //
       WebDavTableModel davModel = (WebDavTableModel) tblServerFiles.getModel();
        String parentPath="";
        try {
            parentPath = davModel.getParentPath("");
        } catch (java.lang.Exception ex) {
            if (ex.getMessage().equals("root-reached")){
                log.info("back one folder - root folder was reached");
                JOptionPane.showMessageDialog(this, "You cannot browser beyond the home folder");
                return;
            }
        }
       log.debug("setting path on prev click = "+ parentPath);
       davModel.setPath(parentPath); 
    }//GEN-LAST:event_btnBackOneFolder_Clicked

    private void btnBrowseWorkspacePathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseWorkspacePathActionPerformed
// TODO add your handling code here:
        //launch browse folders dialog box...
        //Create a file chooser
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        int nReturnVal = fc.showOpenDialog(this);
        
        if (nReturnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
               //log.info("Opening: " + file.getName() + ".\n");
               txtWorkspacePath.setText(file.getName());
            } else {
                //log.info("Open command cancelled by user.\n");
            }

    }//GEN-LAST:event_btnBrowseWorkspacePathActionPerformed

    class documentType extends Object {
        String docType;
        String typeDesc;
        String templatePath;
        
        @Override
        public String toString(){
            return typeDesc;
        }
        
        public String templatePathNormalized() {
               String normalizedPath = templatePath.replace('/', File.separatorChar);
               normalizedPath = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator + normalizedPath;       
               return normalizedPath;
        }
    }
    
    private void initDocumentTypesModel(){
        documentType[] dtArr = null;
        BungeniClientDB instance = new BungeniClientDB (DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
        String query = SettingsQueryFactory.Q_FETCH_ALL_DOCUMENT_TYPES();
        instance.Connect();
        QueryResults qr = instance.QueryResults(query);
        instance.EndConnect();    
        if (qr.hasResults()) {
              Vector<Vector<String>> resultRows  = new Vector<Vector<String>>();
              resultRows = qr.theResults();
              dtArr = new documentType[resultRows.size()];
              int i=0;
                for (Vector<String> resultRow: resultRows) {
                    dtArr[i] = new documentType();
                    dtArr[i].docType =  qr.getField(resultRow, "DOC_TYPE");
                    dtArr[i].typeDesc = qr.getField(resultRow, "DESCRIPTION");
                    dtArr[i].templatePath = qr.getField(resultRow, "TEMPLATE_PATH");
                    BungeniEditorProperties.setPropertyInMap(dtArr[i].docType+"_template", dtArr[i].templatePathNormalized());
                    i++;
                }
        }
        
        if (dtArr != null) {
            this.m_documentTypes = dtArr;
            this.cboDocumentTypes.setModel(new DefaultComboBoxModel(m_documentTypes));
            cboDocumentTypes.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent arg0) {
                   updateCurrentDocTypeMode();
                }
            });
        }
        
    }

    private void updateCurrentDocTypeMode(){
                documentType selectedDocType = (documentType) cboDocumentTypes.getSelectedItem();
                BungeniEditorProperties.setEditorProperty("activeDocumentMode", selectedDocType.docType);
                for (documentType dt : m_documentTypes) {
                    if (dt.docType.equals(selectedDocType.docType)){
                        setLabelTexts(dt.typeDesc);
                    }
                }
    }

    
    private void initWorkspaceFolderModels(File dirStruct){
    log.debug("initializing workspace folder");
    tblWorkspaceFolder.setRowSelectionAllowed(true);
    tblWorkspaceFolder.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tblWorkspaceFolder.getSelectionModel().addListSelectionListener(new tblWorkspaceFolderRowListener());
    
    String filesFolder = dirStruct.getAbsolutePath()+File.separator+m_settings_WorkspacePath+File.separator+"files";
    m_FullFilesPath= filesFolder;
//   m_FullWorkspacePath = dirStruct.getAbsolutePath()+dirStruct.separator+m_settings_WorkspacePath;
    log.debug("files folder = "+filesFolder);
    File filesWSFolder = new File(filesFolder);
    WorkspaceFolderTableModel dirModel = new WorkspaceFolderTableModel(filesWSFolder);
    tblWorkspaceFolder.setModel(dirModel);
}

private void initFileTableModels(File dirStruct ) {
     //lblTemplatePath.setText(dirStruct.getAbsolutePath()+m_settings_WorkspacePath+File.separator+"templates");
       //initialize Table in tab...
       log.debug("initializing templates list table");
        tblTemplatesList.setRowSelectionAllowed(true);
        tblTemplatesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblTemplatesList.getSelectionModel().addListSelectionListener(new tblTemplatesListRowListener());
        /*******set up table models******/ 
        // build path to templates file
        String templatesFolder = dirStruct.getAbsolutePath()+File.separator+m_settings_WorkspacePath+File.separator+"templates";
        m_FullTemplatesPath = templatesFolder;
        m_FullWorkspacePath = dirStruct.getAbsolutePath()+File.separator+m_settings_WorkspacePath;
        log.debug("templates folder = "+templatesFolder);
        File fileTemplateFolder = new File(templatesFolder);
        FileTableModel dirModel = new FileTableModel(fileTemplateFolder);
        tblTemplatesList.setModel(dirModel);
        
        
}

private void initPanels(File currentDir){
    //tabSettings.....

    txtServerIp.setText(m_settings_ServerIP);
    txtServerPort.setText(m_settings_ServerPort);
    txtServerHomeDir.setText(m_settings_ServerPath);
    txtWorkspacePath.setText( m_settings_WorkspacePath);
    lblCurrentDirectory.setText("Current Directory : "+currentDir.getAbsolutePath());
    m_currentMode = BungeniEditorPropertiesHelper.getCurrentDocType();
    for (documentType dt : m_documentTypes) {
        if (dt.docType.equals(m_currentMode)){
                this.cboDocumentTypes.getModel().setSelectedItem(dt);
                //this.lblCurrentActiveMode.setText("CURRENT : " + dt.typeDesc);
                setLabelTexts(dt.typeDesc);
                break;
        }
    }
}

private void initWebDav(){
    //log.info("initialiazing WebDav");
   // WebDavStore wds = new WebDavStore();
    m_dav.setConnectionUrl("http://" + m_settings_ServerIP);
    m_dav.setConnectionPort(new Integer(m_settings_ServerPort));
    m_dav.setConnectionUsername(m_settings_ServerUser);
    m_dav.setConnectionPassword(m_settings_ServerPassword);
    m_dav.setConnectionBaseDirectory(m_settings_ServerPath);
    
     m_dav.connect("");
    
    /*
    m_dav.connect("");
    WebdavResource dav =wds.getResourceHandle();
    if (dav != null){
        log.info("Successfully connected using Webdav");
        initWebdavTableModel(dav);
    }
    else
        log.debug("Webdav connection failed...");
     */
    initWebdavTableModel();
}



private void initWebdavTableModel(){
    //log.info("initializing webdav table model");
    Component[] cpx = tabTemplates.getComponents();
    for (int i=0; i < cpx.length; i++){
        log.debug("component name = "+ cpx[i].getClass().getName());
    }
    WebDavTableModel davTable = new WebDavTableModel(m_dav, m_settings_ServerPath, tblServerFiles, progressServerFiles);
    davTable.brains();
    tblServerFiles.setModel(davTable);
    tblServerFiles.addMouseListener(new tblServerFilesMouseAdapter());
}


private void initProperties(java.io.File currentFolder) {
        try {
            String iniFilePath = currentFolder.getAbsolutePath()+File.separator+"settings" +File.separator +"settings.properties";
            m_iniFilePath = iniFilePath;
            log.debug("Inifile path ="+ iniFilePath);
            FileInputStream fsi = new FileInputStream(iniFilePath);
            log.debug("no. of bytes available = "+ fsi.available() );
            m_propSettings.load(fsi);
            if (m_propSettings.isEmpty()) { log.debug("Empty Settings");}
            else { log.debug( "size is = " + m_propSettings.size()); }
            String firstLaunch = "";
            log.debug("First Launch = "+m_propSettings.getProperty("firstlaunch"));
            firstLaunch = m_propSettings.getProperty("firstlaunch");
            m_settings_WorkspacePath=m_propSettings.getProperty("workspace.folder");
            m_settings_ServerIP=m_propSettings.getProperty("server.ip");
            m_settings_ServerPort=m_propSettings.getProperty("server.port");
            m_settings_ServerPath=m_propSettings.getProperty("server.path");
            m_settings_ServerUser=m_propSettings.getProperty("server.user");
            m_settings_ServerPassword=m_propSettings.getProperty("server.password");
            m_settings_CurrentTemplate = m_propSettings.getProperty("workspace.currenttemplate");
            
            log.debug(" all 4 settings variables "+ m_settings_WorkspacePath  + ", " + m_settings_ServerIP+ ","+ m_settings_ServerPort);
            
        } catch (IOException ex) {
            //log.error(ex.getMessage(), ex);
            }

}

public static int OPENOFFICE_HEIGHT_OFFSET =60;

private void initFrame(XComponent component){
            BungeniFrame frame = new BungeniFrame("Control Panel");
            //set the dimensions for the frame;
            frame.setSize(270, 655);
            //frame position information
            //position frame
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension windowSize = frame.getSize();
            log.debug("screen size = "+ screenSize);
            log.debug("window size = "+ windowSize);
            
            int windowX = 5; //Math.max(0, (screenSize.width  - windowSize.width));
            int windowY = Math.max(0, (screenSize.height - windowSize.height) / 2) + OPENOFFICE_HEIGHT_OFFSET;
            WINDOW_X = windowX;
            WINDOW_Y = windowY;
            
            XModel xModel = ooQueryInterface.XModel(component);
            XWindow xCompWindow = xModel.getCurrentController().getFrame().getComponentWindow();
            XWindow xContWindow = xModel.getCurrentController().getFrame().getContainerWindow();
            com.sun.star.awt.Rectangle rSize = xCompWindow.getPosSize();
            com.sun.star.awt.Rectangle rContSize = xContWindow.getPosSize();
            int coordX = rSize.X + rContSize.X;
            int coordY =  rContSize.Y + rSize.Y + 40;
            
            editorTabbedPanel.coordX = coordX;
            editorTabbedPanel.coordY = coordY;
            
            panel = new org.bungeni.editor.dialogs.editorTabbedPanel(component, this.openofficeObject, frame);
            //panel.setOOoHelper(this.openofficeObject);
           // frame.removeMinMaxClose();
          
            frame.add(panel);
            WindowListener tabbedPanelListener = new WindowAdapter(){
                @Override
                   public void windowClosing(WindowEvent e) {
                        WindowEvent we = new WindowEvent(parentFrame, WindowEvent.WINDOW_CLOSING);
                        parentFrame.dispatchEvent(we);
                    }

                @Override
                    public void windowDeiconified(WindowEvent e) {
                            panel.bringEditorWindowToFront();
                            //deiconize all floating panels
                            HashMap<String,org.bungeni.editor.panels.impl.IFloatingPanel> panelMap = panel.getFloatingPanelMap();
                            java.util.Iterator<String> iterPanels = panelMap.keySet().iterator();
                            while (iterPanels.hasNext()) {
                                final org.bungeni.editor.panels.impl.IFloatingPanel p = panelMap.get(iterPanels.next());
                                SwingUtilities.invokeLater(new Runnable(){
                                public void run() {
                                        JFrame fr= p.getParentWindowHandle();
                                        System.out.println("maximizing  other window");
                                        fr.setExtendedState(JFrame.NORMAL);
                                        fr.setVisible(true);
                                }

                                });
                            }
                        }

                @Override
                        public void windowIconified(WindowEvent e) {
                            System.out.println("panel minimized....");
                            HashMap<String,org.bungeni.editor.panels.impl.IFloatingPanel> panelMap = panel.getFloatingPanelMap();
                            java.util.Iterator<String> iterPanels = panelMap.keySet().iterator();
                            while (iterPanels.hasNext()) {
                                final org.bungeni.editor.panels.impl.IFloatingPanel p = panelMap.get(iterPanels.next());
                                SwingUtilities.invokeLater(new Runnable(){
                                    public void run() {
                                        JFrame fr= p.getParentWindowHandle();
                                        System.out.println("minimizing other window");
                                        fr.setExtendedState(JFrame.ICONIFIED);
                                        fr.setVisible(false);                                }
                                });

                            }

                        }
            };
            frame.addWindowListener(tabbedPanelListener);
            //frame.setSize(243, 650);
            frame.setResizable(false);
            frame.setAlwaysOnTop(true);
            frame.setVisible(true);
            //prevent closing of main editor panel
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setLocation(editorTabbedPanel.coordX, editorTabbedPanel.coordY);
            //frame.setLocation(windowX, windowY );  // Don't use "f." inside constructor.
}

private static int WINDOW_X = 0;
private static int WINDOW_Y = 0;

public static Point getFrameWindowDimension(){
    return new Point(WINDOW_X, WINDOW_Y);
}



private void initoOoAndLaunchFrame(String templatePath, boolean isTemplate){
            openofficeObject = new org.bungeni.ooo.BungenioOoHelper(m_xContext);
            openofficeObject.initoOo();
            
            String templateURL = BungenioOoHelper.convertPathToURL(templatePath);
            XComponent xComponent;
            log.debug("template URL= "+ templateURL);
            if (isTemplate) {
                xComponent = openofficeObject.newDocument(templateURL);
                initMeta(xComponent);
            }
            else
                xComponent = openofficeObject.openDocument(templateURL);
               initFrame(xComponent);
              // testFrame(xComponent);
}

public void testFrame(XComponent xComp){
     BungeniFrame frame = new BungeniFrame("BungeniEditor Control Panel @@@");
            //set the dimensions for the frame;
            frame.setSize(270, 400);
            //frame position information
            //position frame
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension windowSize = frame.getSize();
            log.debug("screen size = "+ screenSize);
            log.debug("window size = "+ windowSize);
            
            int windowX = 5; //Math.max(0, (screenSize.width  - windowSize.width));
            int windowY = Math.max(0, (screenSize.height - windowSize.height) / 2) + OPENOFFICE_HEIGHT_OFFSET;
            WINDOW_X = windowX;
            WINDOW_Y = windowY;
            XModel xModel = ooQueryInterface.XModel(xComp);
            XWindow xCompWindow = xModel.getCurrentController().getFrame().getComponentWindow();
            XWindow xContWindow = xModel.getCurrentController().getFrame().getContainerWindow();
            com.sun.star.awt.Rectangle rSize = xCompWindow.getPosSize();
            com.sun.star.awt.Rectangle rContSize = xContWindow.getPosSize();
            
            frame.setLocation(rSize.X + rContSize.X, rSize.Y + rContSize.Y + 40);
            
            frame.setResizable(false);
            frame.setAlwaysOnTop(true);
           //s frame.pack();
            frame.setVisible(true);
            //prevent closing of main editor panel
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
}

private void initMeta(XComponent xComp){
    LaunchDebateMetadataSetter(xComp);
}

private void LaunchDebateMetadataSetter(XComponent xComp){
        OOComponentHelper oohc = new OOComponentHelper (xComp, this.m_xContext );
    
        if (oohc.propertyExists("__BungeniDocMeta")) {
            String docMetaValue = "";
            try {
                docMetaValue = oohc.getPropertyValue("__BungeniDocMeta");
            } catch (Exception ex) {
                log.error("LaunchDebateMetadataSetter : " + ex.getLocalizedMessage());
            }
            if (docMetaValue.equals("true")) //document already has metadata... 
                return;
        }
        

        String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
     
        BungeniFrame frm = new BungeniFrame(docType + " Metadata");
     /*   IEditorDocMetadataDialog metaDlg = EditorDocMetadataDialogFactory.getInstance(BungeniEditorPropertiesHelper.getCurrentDocType());
        metaDlg.initVariables(oohc, frm, SelectorDialogModes.TEXT_EDIT);
        metaDlg.initialize();*/
        MetadataEditorContainer meta = new MetadataEditorContainer(oohc, frm, SelectorDialogModes.TEXT_EDIT);
        meta.initialize();
        //DebateRecordMetadata meta = new DebateRecordMetadata(oohc, frm, SelectorDialogModes.TEXT_EDIT);
        frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frm.setSize(meta.getFrameSize());
        frm.add(meta.getPanelComponent());
        frm.setVisible(true);
        FrameLauncher.CenterFrame(frm);
        frm.setAlwaysOnTop(true);
}

class RunOpenOffice implements Runnable {
        XComponent returnComponent = null;
        boolean isTemplate = false;
        BungenioOoHelper helper;
        String urlToFile;
        public RunOpenOffice(BungenioOoHelper obj, String openURL, boolean bState) {
            isTemplate = bState;
            helper = obj;
            urlToFile = openURL;
        }
        public void run() {
            try {
            if (isTemplate)
                returnComponent = helper.newDocument(urlToFile);
            else
                returnComponent = helper.openDocument(urlToFile);
            } catch (Exception ex) {
                log.error("RunOpenOffice: run : " + ex.getMessage());
            }
        }
    
}

public void initDataReader(){

/**
     BungeniDataReader rds = new BungeniDataReader();
    Vector<String[]> vMpData = new Vector<String[]>();
    vMpData = rds.read("mps.data");
    System.out.println("size of mp.data = "+ vMpData.size());

 *
 */
}

private void replaceTextinLabel(JLabel lbl, String newText) {
   String lblText =  lbl.getText();
   int nIndex = lblText.indexOf(":");
   String newLblText  = lblText.substring(0, nIndex+1 );
   newLblText = newLblText + " " + newText;
   lbl.setText(newLblText);
}

private void setLabelTexts (String desc) {

    replaceTextinLabel(lblCurrentActiveMode, desc);
    replaceTextinLabel(lblOpenCurrentDoc, desc);
    replaceTextinLabel(lblCreateNewDoc, desc);
    replaceTextinLabel(lblLaunchAndAccquire, desc);
    
}


private void launchFrameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_launchFrameActionPerformed
            //use template defined in m_settings_CurrentTemplate
            //m_FullTemplatesPath
            //m_settings_CurrentTemplate = "hansard.ott";
            createNewDocument.setEnabled(false);

            if (panel == null ) {
                String templateURL = "";
                log.debug("Current Template file :" + m_FullTemplatesPath+File.separatorChar+m_settings_CurrentTemplate);
                
                documentType selectedDocType = (documentType) cboDocumentTypes.getSelectedItem();
                
                /*  BungeniEditorProperties.setEditorProperty("activeDocumentMode", selectedDocType.docType);
                for (documentType dt : m_documentTypes) {
                    if (dt.docType.equals(selectedDocType.docType)){
                        setLabelTexts(dt.typeDesc);
                    }
                }
                 */ 
                final String templatePathNormalized = selectedDocType.templatePathNormalized();
                
                this.createNewDocument.setEnabled(false);
                SwingUtilities.invokeLater(new Runnable(){

                public void run() {
                    
                    initoOoAndLaunchFrame(templatePathNormalized, true); 
                    
                }

                });
            } else {
                panel.newDocumentInPanel();
            }
           createNewDocument.setEnabled(true);
            
}//GEN-LAST:event_launchFrameActionPerformed

private void btnOpenExistingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenExistingActionPerformed
// TODO add your handling code here:
    this.btnOpenExisting.setEnabled(false);
    if (panel == null) {
            String basePath = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH()+File.separator+"workspace"+File.separator+"files";
            File openFile = CommonFileFunctions.getFileFromChooser(basePath, new org.bungeni.utils.fcfilter.ODTFileFilter(), JFileChooser.FILES_ONLY, null);
            if (openFile != null) {
                String fullPathToFile = openFile.getAbsolutePath();        
                initoOoAndLaunchFrame(fullPathToFile, false);
            }
     } else {
        panel.loadDocumentInPanel();
     }
    this.btnOpenExisting.setEnabled(true);
}//GEN-LAST:event_btnOpenExistingActionPerformed

private void importDocument(String documentPath){
    
            if (openofficeObject == null ) {
                openofficeObject = new org.bungeni.ooo.BungenioOoHelper(m_xContext);
                openofficeObject.initoOo();
            }
            
            String templateURL = BungenioOoHelper.convertPathToURL(documentPath);
           XComponent xComponent;
           log.debug("import URL= "+ templateURL);
           xComponent = openofficeObject.openDocument(templateURL);
           //    initFrame(xComponent);
              // testFrame(xComponent);
}


private void btnImportNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportNewActionPerformed
// TODO add your handling code here:
    // TODO add your handling code here:
    this.btnImportNew.setEnabled(false);
    String basePath = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH()+File.separator+"workspace"+File.separator+"files";
    File openFile = CommonFileFunctions.getFileFromChooser(basePath, new org.bungeni.utils.fcfilter.ODTFileFilter(), JFileChooser.FILES_ONLY, null);
    if (openFile != null) {
                String fullPathToFile = openFile.getAbsolutePath();        
            }
    
    this.btnImportNew.setEnabled(true);
}//GEN-LAST:event_btnImportNewActionPerformed


/**
 * Listener Classes Listed below
 **/



/**
 * Listener Class for JTable listing templates
 * @author Ashok Hariharan
 */
   private class tblTemplatesListRowListener implements ListSelectionListener {
        
       public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            
            String strTemplateName = "";
            strTemplateName = (String) tblTemplatesList.getValueAt(tblTemplatesList.getSelectedRow(), 0);
            m_settings_CurrentTemplate = strTemplateName;
            lblSelectedTemplate.setText("Active Template: " + strTemplateName);
            //  output.append("ROW SELECTION EVENT. ");
          //  outputSelection();
        }
    }

 /**
 * Listener Class for JTable listing templates
 * @author Ashok Hariharan
 */
  private class tblWorkspaceFolderRowListener implements ListSelectionListener {
        
       public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            System.out.println("selected : " + event.getLastIndex());
            //  output.append("ROW SELECTION EVENT. ");
          //  outputSelection();
        }
    }

  
  class editorTabbedPanelFrameWindowListener implements WindowListener {
        public void windowOpened(WindowEvent e) {
        }

        public void windowClosing(WindowEvent e) {
            //System.out.println("windowClosing....");
            panel.cleanup();
        }

        public void windowClosed(WindowEvent e) {
        }

        public void windowIconified(WindowEvent e) {
        }

        public void windowDeiconified(WindowEvent e) {
            panel.bringEditorWindowToFront();
        }

        public void windowActivated(WindowEvent e) {
          //  System.out.println("Window Was Activated!");
        }

        public void windowDeactivated(WindowEvent e) {
         //   System.out.println("Window was De-Activated!");
        }
      
  }
/**
 *  Mouse Listener Class for Server file listing 
 *  It checks for "double cick" on the JTable control listing the files from the webdav server
 * @author Ashok Hariharan
 */
private class tblServerFilesMouseAdapter implements MouseListener {
    
    public void mouseClicked(MouseEvent e){
        if (e.getClickCount() == 2){
            Point p = e.getPoint();
            int row = tblServerFiles.rowAtPoint(p);
            String fileName = (String)tblServerFiles.getValueAt(row, 0);
            String fileType = (String) tblServerFiles.getValueAt(row, 3);
            if (fileType.equals("folder")) {
                log.debug("folder: "+fileName + "was clicked");
                //switch to the clicked folder
                //first get the table model
                WebDavTableModel davModel = (WebDavTableModel) tblServerFiles.getModel();
                davModel.brains();
                davModel.setPathRelative(fileName);
            }
            else
                log.debug("file was clicked");
        }
    }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

}
   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBackOneFolder;
    private javax.swing.JButton btnBrowseWorkspacePath;
    private javax.swing.JButton btnEditWorkspaceDocument;
    private javax.swing.JButton btnImportNew;
    private javax.swing.JButton btnOpenExisting;
    private javax.swing.JButton btnSaveSettings;
    private javax.swing.JButton btnSetCurrentTemplate;
    private javax.swing.JButton btnStartAndAccquire;
    private javax.swing.JComboBox cboDocumentTypes;
    private javax.swing.JCheckBox checkBoxConnectOnStartup;
    private javax.swing.JButton createNewDocument;
    private javax.swing.JTabbedPane editorAppTabbedPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblApplnTitle;
    private javax.swing.JLabel lblCreateNewDoc;
    private javax.swing.JLabel lblCurrentActiveMode;
    private javax.swing.JLabel lblCurrentDirectory;
    private javax.swing.JLabel lblCurrentTemplate;
    private javax.swing.JLabel lblCurrentTemplateText;
    private javax.swing.JLabel lblDocumentTypes;
    private javax.swing.JLabel lblLaunchAndAccquire;
    private javax.swing.JLabel lblOpenCurrentDoc;
    private javax.swing.JLabel lblSelectedFile;
    private javax.swing.JLabel lblSelectedTemplate;
    private javax.swing.JLabel lblServerHomeDir;
    private javax.swing.JLabel lblServerIP;
    private javax.swing.JLabel lblServerPort;
    private javax.swing.JLabel lblTemplatePath;
    private javax.swing.JLabel lblWorkspacePath;
    private javax.swing.JProgressBar progressServerFiles;
    private javax.swing.JPanel tabAbout;
    private javax.swing.JPanel tabCurrentFile;
    private javax.swing.JPanel tabServer;
    private javax.swing.JPanel tabSettings;
    private javax.swing.JPanel tabTemplates;
    private javax.swing.JPanel tabWorkspace;
    private javax.swing.JTable tblServerFiles;
    private javax.swing.JTable tblTemplatesList;
    private javax.swing.JTable tblWorkspaceFolder;
    private javax.swing.JTextField txtServerHomeDir;
    private javax.swing.JTextField txtServerIp;
    private javax.swing.JTextField txtServerPort;
    private javax.swing.JTextField txtWorkspacePath;
    // End of variables declaration//GEN-END:variables

    
 
    
}
