package org.bungeni.editor.dialogs;

import ag.ion.bion.officelayer.application.OfficeApplicationException;
import ag.ion.bion.officelayer.document.DocumentException;
import ag.ion.noa.NOAException;
import com.sun.star.lang.XComponent;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import org.bungeni.editor.noa.BungeniNoaApp;
import org.bungeni.editor.SplashPage;
import org.bungeni.editor.config.BaseConfigReader;
import org.bungeni.editor.config.DocTypesReader;
import org.bungeni.editor.config.PluggableConfigReader;
import org.bungeni.editor.config.PluggableConfigReader.PluggableConfig;
import org.bungeni.editor.locales.BungeniEditorLocale;
import org.bungeni.editor.locales.BungeniEditorLocalesFactory;
import org.bungeni.utils.BungeniEditorProperties;
import org.bungeni.utils.BungeniEditorPropertiesHelper;
import org.bungeni.editor.metadata.editors.MetadataEditorContainer;
import org.bungeni.editor.noa.BungeniNoaFrame;
import org.bungeni.editor.noa.BungeniNoaFrame.DocumentComposition;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.utils.BungeniFrame;
import org.bungeni.extutils.BungeniRuntimeProperties;
import org.bungeni.extutils.CommonFileFunctions;
import org.bungeni.extutils.CommonUIFunctions;
import org.bungeni.extutils.FrameLauncher;
import org.bungeni.extutils.MessageBox;

import org.bungeni.ooo.BungenioOoHelper;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.utils.CommonEditorXmlUtils;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * Controller Class for Main Swing Openoffice UNO management application
 * @author Ashok Hariharan
 */
public class editorApplicationController extends javax.swing.JPanel {

    //private Installation m_installObject;
    private static String __WINDOW_TITLE__ = "Bungeni Editor Client ";
    private JFrame parentFrame;
    //path to settings.properties


    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(editorApplicationController.class.getName());

    private String m_currentMode = "";
 
    private documentType[] m_documentTypes = null;

    private static final ResourceBundle bundle = ResourceBundle.getBundle("org/bungeni/editor/dialogs/Bundle");

    /**
     * Main constructor for the class, initializes Panels and oOo environment
     * @param context Openoffice component context
     */
    public editorApplicationController(JFrame pFrame) {
        log.debug("in constructor");
        log.debug("launch locale language = " + Locale.getDefault().getLanguage());
    
            try {
                BungeniNoaApp noaAppInstance = BungeniNoaApp.getInstance();
                if (noaAppInstance != null) {
                    BungenioOoHelper helperObj = org.bungeni.ooo.BungenioOoHelper.getInstance();
                    if (helperObj.getComponentContext() == null ) {
                         helperObj.init(noaAppInstance.
                                getOfficeApp().
                                getOfficeConnection().
                                getXComponentContext());
                    }
                }
            } catch (Throwable ex) {
                log.error("Error while initializing NOA" , ex);
            }
      
        initComponents();
        this.parentFrame = pFrame;

        //temporarily remove these two tabs


    }

    public void cleanup() {
        if (editorTabbedPanel.getInstance() != null) {
            editorTabbedPanel.getInstance().cleanup();
        }
    }

    public void init() {
        CommonFileFunctions cfsObject = new CommonFileFunctions();
        //m_installObject = new Installation();
        File dir = CommonFileFunctions.getInstallDirectory();
        //code to read properties file

        initLocales();
        initConfigs();
        
        initDocumentTypesModel();
        //init panels
        initPanels(dir);
        //initWebDav();
        //initDataReader();
        initLaunchLabels();

        CommonUIFunctions.compOrientation(this);
    }

    private void initLaunchLabels() {

        try {
            Properties props = new Properties();
            props.load(getClass().getResourceAsStream("/version.properties"));
            String versionInfo = (String) props.get("version");
            replaceTextinLabel(lblApplnTitle, versionInfo);
        } catch (IOException ex) {
            log.error("unable to load version info :", ex);
        }

    }

    class ODTFileFilter extends FileFilter {

        @Override
        public boolean accept(File arg0) {
            if (arg0.isDirectory()) {
                return true;
            }

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

        private String getExtension(File fname) {
            String filename = fname.getName();
            int i = filename.lastIndexOf(".");
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
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
        lblCreateNewDoc = new javax.swing.JLabel();
        lblOpenCurrentDoc = new javax.swing.JLabel();
        lblApplnTitle = new javax.swing.JLabel();
        cboLocale = new javax.swing.JComboBox();
        lblLocale = new javax.swing.JLabel();
        cboConfiguration = new org.bungeni.extutils.WideComboBox();
        lblConfiguration = new javax.swing.JLabel();

        editorAppTabbedPane.setFont(new java.awt.Font("DejaVu Sans", 0, 11));

        createNewDocument.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/dialogs/Bundle"); // NOI18N
        createNewDocument.setText(bundle.getString("editorApplicationController.createNewDocument.text")); // NOI18N
        createNewDocument.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                launchFrameActionPerformed(evt);
            }
        });

        lblCurrentTemplate.setText(bundle.getString("editorApplicationController.lblCurrentTemplate.text")); // NOI18N

        cboDocumentTypes.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        cboDocumentTypes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

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

        lblCreateNewDoc.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblCreateNewDoc.setText(bundle.getString("editorApplicationController.lblCreateNewDoc.text")); // NOI18N

        lblOpenCurrentDoc.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblOpenCurrentDoc.setText(bundle.getString("editorApplicationController.lblOpenCurrentDoc.text")); // NOI18N

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
                        .add(btnOpenExisting, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 210, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblOpenCurrentDoc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 278, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
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
                .add(tabCurrentFileLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblOpenCurrentDoc, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .add(btnOpenExisting, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        editorAppTabbedPane.addTab(bundle.getString("editorApplicationController.tabCurrentFile.TabConstraints.tabTitle"), tabCurrentFile); // NOI18N

        lblApplnTitle.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblApplnTitle.setText(bundle.getString("editorApplicationController.lblApplnTitle.text")); // NOI18N

        cboLocale.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboLocale.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblLocale.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        lblLocale.setLabelFor(cboLocale);
        lblLocale.setText(bundle.getString("editorApplicationController.lblLocale.text")); // NOI18N

        cboConfiguration.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboConfiguration.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblConfiguration.setLabelFor(cboConfiguration);
        lblConfiguration.setText(bundle.getString("editorApplicationController.lblConfiguration.text")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(lblApplnTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 368, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(layout.createSequentialGroup()
                    .add(lblConfiguration, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 113, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                    .add(cboConfiguration, 0, 109, Short.MAX_VALUE)
                    .add(18, 18, 18)
                    .add(lblLocale, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(18, 18, 18)
                    .add(cboLocale, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 189, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(org.jdesktop.layout.GroupLayout.LEADING, editorAppTabbedPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 542, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(27, 27, 27)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblLocale)
                    .add(lblConfiguration))
                .add(233, 233, 233))
            .add(layout.createSequentialGroup()
                .add(lblApplnTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(1, 1, 1)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cboLocale, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                    .add(cboConfiguration, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(editorAppTabbedPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 207, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    private void initConfigs(){
        List<PluggableConfig> configs =  PluggableConfigReader.getInstance().getConfigs();
        DefaultComboBoxModel model = new DefaultComboBoxModel(configs.toArray());
        this.cboConfiguration.setModel(model);
        for (int i = 0; i < model.getSize(); i++) {
               PluggableConfig cfg = (PluggableConfig) model.getElementAt(i);
               if (cfg.configDefault ) {
                  this.cboConfiguration.setSelectedItem(cfg);
               }
         }
    }

    /**
     * Sets the default locale in the locale selection combobox
     */
    private void initLocales() {
        DefaultComboBoxModel cboModel = new DefaultComboBoxModel(BungeniEditorLocalesFactory.getAvailableLocales());
        this.cboLocale.setModel(cboModel);
        //get the current default locale which was set at startup in the class BungeniEditorClient
        BungeniEditorLocale defLocale = new BungeniEditorLocale(Locale.getDefault());
        cboLocale.setSelectedItem(defLocale);
        cboLocale.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    updateLocaleIni();
                } catch (FileNotFoundException ex) {
                    log.error("while updating editor.ini", ex);
                } catch (IOException ex) {
                    log.error("while updating editor.ini", ex);
                }
            }
        });

    }

    private void updateLocaleIni() throws FileNotFoundException, IOException {
        log.info("Updating locale ini");
        Properties pEditorIni = new Properties();
        pEditorIni.load(new FileInputStream(BungeniRuntimeProperties.getProperty("EDITOR_INI")));
        Object[] objects = cboLocale.getSelectedObjects();
        BungeniEditorLocale selectedLocale = (BungeniEditorLocale) objects[0];
        pEditorIni.setProperty("lang", selectedLocale.getLocale().getLanguage());
        pEditorIni.setProperty("region", selectedLocale.getLocale().getCountry());
        pEditorIni.store(new FileOutputStream(BungeniRuntimeProperties.getProperty("EDITOR_INI")), "Updating locale in editor.ini");
        MessageBox.OK(this.parentFrame, bundle.getString("change_locale"));
    }

    class documentType extends Object {

        String docType;
        String typeDesc;
        String templatePath;

        @Override
        public String toString() {
            return typeDesc;
        }

        /**
         * Templates are always relative to the configs folder
         * You can specify absolute paths as file:// urls 
         * @return
         */
        public String templatePathNormalized() {
                if (templatePath.startsWith("file:/")) {
                    return CommonFileFunctions.convertUrlToFile(templatePath).getAbsolutePath();
                } else {
                    //path relative to configs folder
                    String normalizedPath = templatePath.replace('/', File.separatorChar);
                    normalizedPath = BaseConfigReader.CONFIGS_FOLDER + File.separator + normalizedPath;
                    return normalizedPath;
                }
        }
    }

    private void initDocumentTypesModel() {
        documentType[] dtArr = null;
        List<Element> doctypeList = null;
        try {
            doctypeList = DocTypesReader.getInstance().getActiveDocTypes();
        } catch (JDOMException ex) {
            log.error("Error while initializeing doctype list");
        }
        if (null != doctypeList ) {
            dtArr = new documentType[doctypeList.size()];
            int i=0;
            for (Element doctypeElem : doctypeList) {
                dtArr[i] = new documentType();
                dtArr[i].docType = doctypeElem.getAttributeValue("name");
                dtArr[i].typeDesc = CommonEditorXmlUtils.getLocalizedChildElementValue(
                        BungeniEditorPropertiesHelper.getLangAlpha3Part2(),
                        doctypeElem,
                        "title");
                dtArr[i].templatePath = doctypeElem.getAttributeValue("template");
                BungeniEditorProperties.setPropertyInMap(dtArr[i].docType + "_template", dtArr[i].templatePathNormalized());
                i++;
            }
        }

      
        if (dtArr != null) {
            this.m_documentTypes = dtArr;
            this.cboDocumentTypes.setModel(new DefaultComboBoxModel(m_documentTypes));
            cboDocumentTypes.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    updateCurrentDocTypeMode();
                }
            });
        }

    }

    private void updateCurrentDocTypeMode() {
        documentType selectedDocType = (documentType) cboDocumentTypes.getSelectedItem();
        BungeniEditorProperties.setEditorProperty("activeDocumentMode", selectedDocType.docType);
        for (documentType dt : m_documentTypes) {
            if (dt.docType.equals(selectedDocType.docType)) {
                setLabelTexts(dt.typeDesc);
            }
        }
    }


    private void initPanels(File currentDir) {
        //tabSettings.....
     m_currentMode = BungeniEditorPropertiesHelper.getCurrentDocType();
        for (documentType dt : m_documentTypes) {
            if (dt.docType.equals(m_currentMode)) {
                this.cboDocumentTypes.getModel().setSelectedItem(dt);
                //this.lblCurrentActiveMode.setText("CURRENT : " + dt.typeDesc);
                setLabelTexts(dt.typeDesc);
                break;
            }
        }
    }


    public static int OPENOFFICE_HEIGHT_OFFSET = 60;
    public static int WIDTH_OOo_SCROLLBAR = 25;

 
    private static int WINDOW_X = 0;
    private static int WINDOW_Y = 0;

    public static Point getFrameWindowDimension() {
        return new Point(WINDOW_X, WINDOW_Y);
    }

    private void launchDocumentInFrame(String templatePath, boolean isTemplate) {

        String templateURL = BungenioOoHelper.convertPathToURL(templatePath);
       
        log.debug("template URL= " + templateURL);

        BungeniNoaFrame frame = BungeniNoaFrame.getInstance();
        frame.setTitle(bundle.getString("editorTabbedPanel.panel.Title"));
        DocumentComposition dc = null;
        try {
            dc = frame.loadDocumentInPanel(templatePath, isTemplate);
        } catch (OfficeApplicationException ex) {
            log.error("Error while loading document !!!", ex);
        } catch (NOAException ex) {
            log.error("Error while loading document !!!", ex);
        } catch (DocumentException ex) {
            log.error("Error while loading document !!!", ex);
        }
        XComponent xopenedDocument = dc.getDocument().getXComponent();

        editorTabbedPanel panel = editorTabbedPanel.getInstance();
        panel.init(dc);

        // (rm, feb 2012) -let the tabbed pane grow to fill all its
        // available space minus the max size of the noaPanel
        // which is all the available space minus the preferred size
        // of the tabbedPane
        frame.getBasePanel().add(panel, "grow");
        frame.validate();
        frame.setVisible(true);
        frame.setSize(800, 600);
        frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
        LaunchMetadataSetter(xopenedDocument, isTemplate);
        //launch the metadata setter here
       

       }

    /**
     * Launches the initial metadata setter screen for a document
     * @param xComp
     * @param isTemplate
     */
    private void LaunchMetadataSetter(XComponent xComp, boolean isTemplate) {
        OOComponentHelper oohc = new OOComponentHelper(xComp, BungenioOoHelper.getInstance().getComponentContext());

        if (oohc.propertyExists("__BungeniDocMeta")) {
            String docMetaValue = "";
            try {
                docMetaValue = oohc.getPropertyValue("__BungeniDocMeta");
            } catch (Exception ex) {
                log.error("LaunchDebateMetadataSetter : " + ex.getLocalizedMessage());
            }
            if (docMetaValue.equals("true")) //document already has metadata... 
            {
                return;
            }
        }


        String docType = BungeniEditorPropertiesHelper.getCurrentDocType();

        BungeniFrame frm = new BungeniFrame(docType + " Metadata");
        frm.initFrame();
        /**
         * If it is a template we launch it in insertion mode,
         * else we launch it in edit mode -- i.e. the document already exists
         */
        MetadataEditorContainer meta = new MetadataEditorContainer(oohc,
                frm,
                isTemplate ? SelectorDialogModes.TEXT_INSERTION : SelectorDialogModes.TEXT_EDIT );

        meta.initialize();
        frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frm.setSize(meta.getPreferredSize());
        frm.add(meta.getPanelComponent());
        frm.setVisible(true);
        FrameLauncher.CenterFrame(frm);
        frm.setAlwaysOnTop(true);
    }


    private void replaceTextinLabel(JLabel lbl, String newText) {
        String lblText = lbl.getText();
        int nIndex = lblText.indexOf(":");
        String newLblText = lblText.substring(0, nIndex + 1);
        newLblText = newLblText + " " + newText;
        lbl.setText(newLblText);
    }

    private void setLabelTexts(String desc) {

        replaceTextinLabel(lblCurrentActiveMode, desc);
        replaceTextinLabel(lblOpenCurrentDoc, desc);
        replaceTextinLabel(lblCreateNewDoc, desc);
        // replaceTextinLabel(lblLaunchAndAccquire, desc);

    }

    
    private boolean launchDocumentType(documentType thisDocType, String launchMode) throws IOException {

        //if it is a new document ....
        if (launchMode.equals("new")) {
            //check if the editor panel is open -- this will be non-null in all cases except for the first initial launch
            if (editorTabbedPanel.isInstanceNull() ) {
                //open the document and init the frame
                String templateURL = "";
                final String templatePathNormalized = thisDocType.templatePathNormalized();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        launchDocumentInFrame(templatePathNormalized, true);
                    }
                });
                return true;
            } else {
                //open the document in the current panel
                editorTabbedPanel.getInstance().newDocumentInPanel();
                return true;
            }
        }

      if (launchMode.equals("edit")){ //edit
            if (editorTabbedPanel.isInstanceNull()) {
                String basePath = BaseConfigReader.getWorkspaceFolder();
                //String basePath =  BaseConfigReader.//CommonFileFunctions.getAbsoluteInstallDir() + File.separator + "workspace" + File.separator + "files";
                File openFile = CommonFileFunctions.getFileFromChooser(basePath, new org.bungeni.utils.fcfilter.ODTFileFilter(), JFileChooser.FILES_ONLY, null);
                if (openFile != null) {
                    final String fullPathToFile = openFile.getAbsolutePath();
                    SwingUtilities.invokeLater(new Runnable(){
                        public void run(){
                            launchDocumentInFrame(fullPathToFile, false);
                        }
                    });
                    return true;
                } else {
                    return false;
                }
            } else {
                //open the document in the current panel
                editorTabbedPanel.getInstance().loadDocumentFromFileSystemInPanel();
                return true;
            }
        }
      return false;
    }
   
    public void launchDocumentType(String docType, String launchMode)  {
        documentType thisDocType = null;
        for (int i = 0; i < cboDocumentTypes.getModel().getSize(); i++) {
            documentType curdocType = (documentType) cboDocumentTypes.getModel().getElementAt(i);
            if (curdocType.docType.equals(docType)) {
                thisDocType = curdocType;
                break;
            }
        }
        if (thisDocType == null) {
            //TODO throw exception
            return;
        }
        //!+TEMP_DISABLE
        BungeniEditorProperties.setEditorProperty("activeDocumentMode", thisDocType.docType);
        try {
            launchDocumentType(thisDocType, launchMode);
        } catch (IOException ex) {
            log.error("Error while loading doc type, possible workspace was not found" , ex);
        }


    }

    private void launchFrameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_launchFrameActionPerformed
        //use template defined in m_settings_CurrentTemplate
        //m_FullTemplatesPath
        //m_settings_CurrentTemplate = "hansard.ott";
        createNewDocument.setEnabled(false);
        documentType selectedDocType = (documentType) cboDocumentTypes.getSelectedItem();
        try {
            launchDocumentType(selectedDocType, "new");
        } catch (IOException ex) {
            log.error("Error while launching new document, possibly workspace was not found", ex);
        }

        createNewDocument.setEnabled(true);
        hideWindow(false);

    }//GEN-LAST:event_launchFrameActionPerformed

    private void btnOpenExistingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenExistingActionPerformed
// TODO add your handling code here:
        this.btnOpenExisting.setEnabled(false);
        documentType selectedDocType = (documentType) cboDocumentTypes.getSelectedItem();
        try {
            if (launchDocumentType(selectedDocType, "edit")) {
                this.hideWindow(false);
            }
        } catch (IOException ex) {
            log.error("Error while launching doc type possibly workspace folder was not found", ex);
        }
        this.btnOpenExisting.setEnabled(true);
    }//GEN-LAST:event_btnOpenExistingActionPerformed

    SplashPage page = new SplashPage(5000);

    private void hideWindow(boolean bState) {
        this.parentFrame.setVisible(bState);
    }

    /**
     * Listener Classes Listed below
     **/



    /**
     *  Mouse Listener Class for Server file listing
     *  It checks for "double cick" on the JTable control listing the files from the webdav server
     * @author Ashok Hariharan
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOpenExisting;
    private javax.swing.JComboBox cboConfiguration;
    private javax.swing.JComboBox cboDocumentTypes;
    private javax.swing.JComboBox cboLocale;
    private javax.swing.JButton createNewDocument;
    private javax.swing.JTabbedPane editorAppTabbedPane;
    private javax.swing.JLabel lblApplnTitle;
    private javax.swing.JLabel lblConfiguration;
    private javax.swing.JLabel lblCreateNewDoc;
    private javax.swing.JLabel lblCurrentActiveMode;
    private javax.swing.JLabel lblCurrentTemplate;
    private javax.swing.JLabel lblCurrentTemplateText;
    private javax.swing.JLabel lblDocumentTypes;
    private javax.swing.JLabel lblLocale;
    private javax.swing.JLabel lblOpenCurrentDoc;
    private javax.swing.JPanel tabCurrentFile;
    // End of variables declaration//GEN-END:variables
}
