/*
 * transformXMLPanel.java
 *
 * Created on May 20, 2008, 10:59 AM
 */
package org.bungeni.editor.panels.loadable;

import ag.ion.bion.officelayer.application.OfficeApplicationException;
import ag.ion.bion.officelayer.document.DocumentException;
import ag.ion.noa.NOAException;
import com.sun.star.uno.Exception;
import java.util.logging.Level;
import org.bungeni.ooo.transforms.impl.exportDestination;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingWorker;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.bungeni.editor.noa.BungeniNoaFrame;
import org.bungeni.editor.config.BungeniEditorProperties;
import org.bungeni.editor.config.BungeniEditorPropertiesHelper;
import org.bungeni.editor.panels.impl.BaseClassForITabbedPanel;
import org.bungeni.utils.BungeniFrame;
import org.bungeni.extutils.CommonANUtils;
import org.bungeni.extutils.CommonDocumentUtilFunctions;
import org.bungeni.extutils.CommonEditorFunctions;
import org.bungeni.extutils.CommonFileFunctions;
import org.bungeni.extutils.CommonUIFunctions;
import org.bungeni.extutils.CommonXmlConfigParams;
import org.bungeni.extutils.FrameLauncher;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.transforms.impl.BungeniTransformationTarget;
import org.bungeni.ooo.transforms.impl.BungeniTransformationTargetFactory;
import org.bungeni.ooo.transforms.impl.IBungeniDocTransform;
import org.bungeni.extutils.MessageBox;
import org.bungeni.extutils.NotifyBox;
import org.bungeni.ooo.transforms.impl.BungeniTransformationTargets;
import org.bungeni.utils.externalplugin.ExternalPlugin;
import org.bungeni.utils.externalplugin.ExternalPluginLoader;
import org.bungeni.xml.viewer.BungeniXmlViewer;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author  Ashok Hariharan
 */
public class transformXMLPanel extends BaseClassForITabbedPanel {

    private static org.apache.log4j.Logger log = Logger.getLogger(transformXMLPanel.class.getName());
    private ImageIcon OFF_ICON = new javax.swing.ImageIcon(getClass().getResource("/gui/btn_off.png"));
    private ImageIcon ON_ICON = new javax.swing.ImageIcon(getClass().getResource("/gui/btn_on.png"));
    private final String _OFF_NAME_ = "off";
    private final String _ON_NAME_ = "on";
    private SAXBuilder saxBuilder = null;

    /** Creates new form transformXMLPanel */
    public transformXMLPanel() {
        initComponents();
        initButtons();
        //CommonUIFunctions.compOrientation(this);
    }

    private void initButtons() {
        this.btnExportToXML.addActionListener(new transformXmlActionListener());
        this.btnMakePlain.addActionListener(new convertToPlain());
        this.btnExport.addActionListener(new exportToOtherFormats());
    }
    /**
     * handle to resource bundle
     */
    private static final ResourceBundle bundle = ResourceBundle.getBundle("org/bungeni/editor/panels/loadable/Bundle");


    private void initTransfromTargetCombo() {
        BungeniTransformationTarget[] targets = BungeniTransformationTargets.getTransformationTargets().values().toArray(new BungeniTransformationTarget[BungeniTransformationTargets.getTransformationTargets().size()]);
        DefaultComboBoxModel model = new DefaultComboBoxModel(targets);
        this.cboTransformFrom.setModel(model);
    }

    private void initExportDestCombo() {
        exportDestination[] destinations = BungeniTransformationTargets.getExportDestinations().values().toArray(new exportDestination[BungeniTransformationTargets.getExportDestinations().size()]);
        DefaultComboBoxModel model = new DefaultComboBoxModel(destinations);
    }

    public class StructValidatorEventHandler {

        public StructValidatorEventHandler() {
        }

        public void dispatchEvent(String arg0, Object[] arg1) {
            String dispatchSectionName = (String) arg1[0];
            System.out.println(dispatchSectionName);
        }
    }

    class transformXmlActionListener implements ActionListener {

        /**
         * Run the button action in a swingworker thread, so the UI disabling happens immediately
         */
        class buttonActionRunner extends SwingWorker<Boolean, Object> {

            JButton sourceButton;

            public buttonActionRunner(JButton origButton) {
                this.sourceButton = origButton;
            }

            protected Boolean doInBackground() throws Exception {
                //check if server is running .. if running stop it
                boolean bState = exportToXml();
                return new Boolean(bState);
            }

            @Override
            public void done() {
                boolean bState = false;
                try {
                    sourceButton.setEnabled(true);
                    parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    bState = get();
                } catch (InterruptedException ex) {
                    log.error("done(),", ex);
                } catch (ExecutionException ex) {
                    log.error("done(),", ex);
                }
                if (bState) {
                    NotifyBox.info(bundle.getString("Document_was_successfully_Exported_to_the_workspace_folder"));
                } else {
                    NotifyBox.error(bundle.getString("Document_export_failed"));
                }
            }
        }

        public synchronized void actionPerformed(ActionEvent e) {
            //get the button originating the event
            final JButton sourceButton = (JButton) e.getSource();
            //disable the button immediately
            sourceButton.setEnabled(false);
            //set the wait cursor
            parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //call the swingworker thread for the button event
            (new buttonActionRunner(sourceButton)).execute();
        }
    }

    /**
     * Convert documents to Plain documents
     */
    class convertToPlain implements ActionListener {

        /**
         * Run the button action in a swingworker thread, so the UI disabling happens immediately
         */
        class buttonActionRunner extends SwingWorker<String, Object> {

            JButton sourceButton;

            public buttonActionRunner(JButton origButton) {
                this.sourceButton = origButton;
            }

            protected String doInBackground() throws Exception {
                //check if server is running .. if running stop it
                String retValue = convertToPlain();
                log.info("......................................doInBackground()");
                return retValue;
            }

            @Override
            public void done() {
                String retValue = "";
                try {
                    sourceButton.setEnabled(true);
                    parentFrame.setCursor(Cursor.getDefaultCursor());
                    retValue = get();
                    if (retValue != null) {
                        if (retValue.equals("save_the_document")) {
                            NotifyBox.error(bundle.getString("Please_save_the_document"), bundle.getString("Save_the_document"));
                        } else {
                            String outputFilePath = (String) retValue;
                            File fFile = CommonFileFunctions.convertUrlToFile(outputFilePath);
                            int nRet = MessageBox.Confirm(parentFrame, bundle.getString("Yes_to_open_No_to_close"), bundle.getString("Document_Successfully_Converted!"));
                            if (nRet == JOptionPane.YES_OPTION) {
                                try {
                                    BungeniNoaFrame.getInstance().loadDocumentInPanel(outputFilePath, false);
                                } catch (OfficeApplicationException ex) {
                                    java.util.logging.Logger.getLogger(transformXMLPanel.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (NOAException ex) {
                                    java.util.logging.Logger.getLogger(transformXMLPanel.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (DocumentException ex) {
                                    java.util.logging.Logger.getLogger(transformXMLPanel.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }
                } catch (InterruptedException ex) {
                    log.error("done(),", ex);
                } catch (ExecutionException ex) {
                    log.error("done(),", ex);
                }
            }
        }

        public synchronized void actionPerformed(ActionEvent e) {
            //check if document has been saved
            if (ooDocument.documentRequiresSaving()) {
                NotifyBox.error(bundle.getString("Please_save_the_document"), bundle.getString("Save_the_document"));
                return;
            }
            //get the button originating the event
            final JButton sourceButton = (JButton) e.getSource();
            //disable the button immediately
            sourceButton.setEnabled(false);
            //set the wait cursor
            parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //call the swingworker thread for the button event
            (new buttonActionRunner(sourceButton)).execute();
        }
    }

    /**
     * Export to Other formats
     */
    class exportToOtherFormats implements ActionListener {

        /**
         * Run the button action in a swingworker thread, so the UI disabling happens immediately
         */
        class buttonActionRunner extends SwingWorker<String, Object> {

            JButton sourceButton;
            BungeniTransformationTarget tTarget;

            public buttonActionRunner(JButton origButton) {
                this.sourceButton = origButton;
                tTarget = (BungeniTransformationTarget) cboTransformLocation.getSelectedItem();
            }

            protected String doInBackground() throws Exception {
                //check if server is running .. if running stop it
                String retValue = exportToSelectedFormat(tTarget);
                return retValue;
            }

            @Override
            public void done() {
                String retValue = "";
                try {
                    sourceButton.setEnabled(true);
                    parentFrame.setCursor(Cursor.getDefaultCursor());
                    retValue = get();
                    if (retValue != null) {
                        NotifyBox.info(bundle.getString(retValue));
                    }
                } catch (InterruptedException ex) {
                    log.error("done(),", ex);
                } catch (ExecutionException ex) {
                    log.error("done(),", ex);
                }
            }
        }

        public synchronized void actionPerformed(ActionEvent e) {
            //check if document has been saved
            if (ooDocument.documentRequiresSaving()) {
                NotifyBox.error(bundle.getString("Please_save_the_document"), bundle.getString("Save_the_document"));
                return;
            }
            //get the button originating the event
            final JButton sourceButton = (JButton) e.getSource();
            //disable the button immediately
            sourceButton.setEnabled(false);
            //set the wait cursor
            parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //call the swingworker thread for the button event
            (new buttonActionRunner(sourceButton)).execute();
        }
    }

    private void validateStructure() {
        if (ooDocument.isDocumentOnDisk()) {
            //  generatePlainDocument();
            ExternalPluginLoader ep = new ExternalPluginLoader();

            final StructValidatorEventHandler seHandler = new StructValidatorEventHandler();
            ExternalPlugin rulesValidator = ep.loadPlugin("StructuralRulesValidator");

            final String odfFileUrl = ooDocument.getDocumentURL();
            final String currentDocType = BungeniEditorPropertiesHelper.getCurrentDocType();
            final String rulesRootFolder = CommonEditorFunctions.getPathRelativeToRoot(BungeniEditorProperties.getEditorProperty("structuralRulesRootPath"));
            final transformXMLPanel ppPanel = this;

            Object[] argSetParams = {new HashMap() {

            {
                put("OdfFileURL", odfFileUrl);
                put("CurrentDocType", currentDocType);
                put("RulesRootFolder", rulesRootFolder);
                put("ParentFrame", parentFrame);
                put("CallerPanel", ppPanel);
                put("ParentEventDispatcher", seHandler);
                put("ParentEventDispatcherClass", "org.bungeni.editor.panels.loadable.transformXMLPanel$StructValidatorEventHandler");
            }
        }};
            rulesValidator.callMethod("setParams", argSetParams);
            Object[] argExec = {};
            Object retValue = rulesValidator.callMethod("exec", argExec);
            if (retValue != null) {
                String outputFilePath = (String) retValue;
                // MessageBox.OK(parentFrame, "A plain document was generated, it can be found at : \n" + outputFilePath, "Plain Document generation", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            NotifyBox.error(bundle.getString("Please_save_the_document"), bundle.getString("Save_the_document"));
        }

    }

    public void goToSectionPosition(String sectionName) {
        System.out.println(sectionName);
        CommonDocumentUtilFunctions.selectSection(ooDocument, sectionName);
    }

    private boolean exportToXml() {        //get a handle to the AN xml transformer
        BungeniTransformationTarget transform = BungeniTransformationTargets.getTransformationTargets().get("AN-XML");
        IBungeniDocTransform iTransform = BungeniTransformationTargetFactory.getTransformClass(transform);
        HashMap<String, Object> params = new HashMap<String, Object>();
        iTransform.setParams(params);
        boolean bState = iTransform.transform(ooDocument);
        return bState;
    }

    private JMenuItem setupMenuItem(String key, String title, ActionListener listener) {
        JMenuItem item = new JMenuItem(title);
        item.setActionCommand(key);
        item.addActionListener(listener);
        return item;

    }

    class viewXmlListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("ANXML")) {
                File fXml = CommonANUtils.getComponentFromFile(ooDocument.getDocumentURL(), "xml");
                
                if (fXml.exists()) {
                    if (fXml.length() != 0) {
                        try {
                            BungeniXmlViewer.launchXmlViewer("Xml Viewer", fXml);
                            return;
                        } catch (ParserConfigurationException ex) {
                            log.error("viewXmlDoc ", ex);
                            NotifyBox.error(bundle.getString("xml_not_wellformed"), bundle.getString("xml_viewer_error"));
                            return;
                        }
                    }
                }
                NotifyBox.error(bundle.getString("xml_does_not_exist"), bundle.getString("xml_viewer_error"));
                return;
            }
            if (e.getActionCommand().equals("METALEX")) {
                String sFilePrefix = CommonANUtils.getFilePrefix(ooDocument.getDocumentURL()) + "_metalex";
                String sFileExt = "xml";
                String sFileMlx = sFilePrefix + ((sFileExt.length() > 0) ? "." + sFileExt : "");
                File fMlx = CommonFileFunctions.convertUrlToFile(sFileMlx);
                if (fMlx.exists()) {
                    if (fMlx.length() != 0) {
                        try {
                            BungeniXmlViewer.launchXmlViewer("Xml Viewer", fMlx);
                            return;
                        } catch (ParserConfigurationException ex) {
                            log.error("viewXmlDoc ", ex);
                            NotifyBox.error(bundle.getString("xml_not_wellformed"), bundle.getString("xml_viewer_error"));
                            return;
                        }
                    }
                }
                NotifyBox.error(bundle.getString("xml_does_not_exist"), bundle.getString("xml_viewer_error"));
                return;
            }
        }
    }

    private JPopupMenu getPopupMenu() {
        JPopupMenu menu = new JPopupMenu();
        //menu.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        viewXmlListener listener = new viewXmlListener();

        // !+(rm, feb 2012) - MINOR FIX
        // JMenuItem menuItemXml = setupMenuItem("ANXML", "AkomaNtososo", listener);
        JMenuItem menuItemXml = setupMenuItem("ANXML", "AkomaNtoso", listener);
        menuItemXml.setFont(this.btnViewXmlDoc.getFont());
        JMenuItem menuItemMtlx = setupMenuItem("METALEX", "MetaLEX", listener);
        menuItemMtlx.setFont(this.btnViewXmlDoc.getFont());
        menu.add(menuItemXml);
        menu.add(menuItemMtlx);
        return menu;
    }
    JPopupMenu xmlPopupMenu = null;

    private void viewXmlDoc(MouseEvent evt) {
        if (ooDocument.isDocumentOnDisk()) {
            String sUrl = ooDocument.getDocumentURL();
            if (xmlPopupMenu == null) {
                xmlPopupMenu = getPopupMenu();
            }
            xmlPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());

        } else {
            NotifyBox.error(bundle.getString("save_document_before_transform"));
        }
    }

    private String exportToSelectedFormat(BungeniTransformationTarget transform) {

        IBungeniDocTransform iTransform = BungeniTransformationTargetFactory.getTransformClass(transform);

        String exportPath = BungeniEditorProperties.getEditorProperty("defaultExportPath");
        exportPath = exportPath.replace('/', File.separatorChar);
        exportPath = CommonFileFunctions.getAbsoluteInstallDir() + File.separator + exportPath;
        exportPath = exportPath + File.separatorChar + OOComponentHelper.getFrameTitle(ooDocument.getTextDocument()).trim() + "." + transform.targetExt;
        File fileExp = new File(exportPath);
        String exportPathURL = "";
        exportPathURL = fileExp.toURI().toString();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("StoreToUrl", exportPathURL);

        iTransform.setParams(params);
        boolean bState = iTransform.transform(ooDocument);

        if (bState) {
            return "Document_was_successfully_Exported_to_the_workspace_folder";
        } else {
            return "Document_export_failed";
        }

    }

    private String convertToPlain() {
        //boolean bState = false;
        if (ooDocument.isDocumentOnDisk()) {
            //  generatePlainDocument();
            ExternalPluginLoader ep = new ExternalPluginLoader();
            ExternalPlugin convertToPlain = ep.loadPlugin("ConvertToPlain");
            Object[] argSetParams = {
                new HashMap() {

                    {
                        put("OdfFileURL", ooDocument.getDocumentURL());
                    }
                }
            };

            // (rm, feb 2012) - the methods org.bungeni.utils.externalplugin.ExternalPlugin
            // setParams() and exec() check that the document has a valid structure
            convertToPlain.callMethod("setParams", argSetParams);
            Object[] argExec = {};
            Object retValue = convertToPlain.callMethod("exec", argExec);
            return (String) retValue;
        } else {
            return "save_the_document";
        }
    }

    private void viewExportErrors() {
        //check if errors file exists
        if (ooDocument.isDocumentOnDisk()) {
            boolean bNoErrors = true;
            String sUrl = ooDocument.getDocumentURL();
            File fErrors = CommonANUtils.getNamedComponentFromFile(sUrl, "errors.xml");
            //File fFile = CommonFileFunctions.convertUrlToFile(sUrl);
            //File fErrors = new File(fFile.getParent() + File.separator + "errors.xml");
            if (fErrors.exists()) {
                if (saxBuilder == null) {
                    saxBuilder = new SAXBuilder(CommonXmlConfigParams.SAX_PARSER_DRIVER, false);
                }
                Document xmlErrors = null;
                try {
                    BufferedReader errReader = CommonFileFunctions.getFileasBufferedReader(fErrors);
                    xmlErrors = saxBuilder.build(errReader);
                } catch (JDOMException ex) {
                    log.error("viewXmErrors ", ex);
                } catch (IOException ex) {
                    log.error("viewXmErrors ", ex);
                }
                if (xmlErrors != null) {
                    if (validationErrorHelper.errorsExist(xmlErrors)) {
                        bNoErrors = false;
                        launchErrorPanel(xmlErrors);
                        return;
                    }
                }
            }
            if (bNoErrors) {
                NotifyBox.info(bundle.getString("no_validation_errors"));
            }

        } else {
            NotifyBox.error(bundle.getString("save_document_before_transform"));
        }
    }

    private void launchErrorPanel(Document xmlDoc) {
        BungeniFrame frame = new BungeniFrame("Validation Errors");
        validationErrorPanel vpanel = new validationErrorPanel(this, frame, xmlDoc);
        frame.add(vpanel);
        frame.setSize(529, 298);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        FrameLauncher.LaunchFrame(frame, true, true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cboTransformLocation = new javax.swing.JComboBox();
        lblTransformFrom = new javax.swing.JLabel();
        btnExport = new javax.swing.JButton();
        checkChangeColumns = new javax.swing.JCheckBox();
        btnMakePlain = new javax.swing.JButton();
        btnExportToXML = new javax.swing.JButton();
        btnViewValidationErrors = new javax.swing.JButton();
        btnViewXmlDoc = new javax.swing.JButton();
        cboTransformFrom = new javax.swing.JComboBox();
        lblLocation = new javax.swing.JLabel();

        cboTransformLocation.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        cboTransformLocation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Portable Document Format (PDF)", "XHTML - eXtensible HTML", "Marginalia-safe HTML export" }));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/panels/loadable/Bundle"); // NOI18N
        cboTransformLocation.setToolTipText(bundle.getString("transformXMLPanel.cboTransformLocation.toolTipText")); // NOI18N

        lblTransformFrom.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblTransformFrom.setText(bundle.getString("transformXMLPanel.lblTransformFrom.text")); // NOI18N

        btnExport.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnExport.setText(bundle.getString("transformXMLPanel.btnExport.text")); // NOI18N
        btnExport.setToolTipText(bundle.getString("transformXMLPanel.btnExport.toolTipText")); // NOI18N

        checkChangeColumns.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        checkChangeColumns.setText(bundle.getString("transformXMLPanel.checkChangeColumns.text")); // NOI18N
        checkChangeColumns.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkChangeColumns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkChangeColumnsActionPerformed(evt);
            }
        });

        btnMakePlain.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnMakePlain.setText(bundle.getString("transformXMLPanel.btnMakePlain.text")); // NOI18N
        btnMakePlain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMakePlainActionPerformed(evt);
            }
        });

        btnExportToXML.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnExportToXML.setText(bundle.getString("transformXMLPanel.btnExportToXML.text")); // NOI18N

        btnViewValidationErrors.setFont(new java.awt.Font("DejaVu Sans", 0, 9));
        btnViewValidationErrors.setText(bundle.getString("transformXMLPanel.btnViewValidationErrors.text")); // NOI18N
        btnViewValidationErrors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewValidationErrorsActionPerformed(evt);
            }
        });

        btnViewXmlDoc.setFont(new java.awt.Font("DejaVu Sans", 0, 9));
        btnViewXmlDoc.setText(bundle.getString("transformXMLPanel.btnViewXmlDoc.text")); // NOI18N
        btnViewXmlDoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnViewXmlDocMousePressed(evt);
            }
        });

        cboTransformFrom.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        cboTransformFrom.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Portable Document Format (PDF)", "XHTML - eXtensible HTML", "Marginalia-safe HTML export" }));
        cboTransformFrom.setToolTipText(bundle.getString("transformXMLPanel.cboTransformFrom.toolTipText")); // NOI18N

        lblLocation.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblLocation.setText(bundle.getString("transformXMLPanel.lblLocation.text")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(cboTransformLocation, 0, 239, Short.MAX_VALUE)
                        .addContainerGap())
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(btnExport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(35, 35, 35))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, btnMakePlain)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, checkChangeColumns, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 134, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(110, 110, 110))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(btnViewValidationErrors, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 95, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 48, Short.MAX_VALUE)
                                .add(btnViewXmlDoc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 96, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, btnExportToXML, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE))
                        .addContainerGap())
                    .add(layout.createSequentialGroup()
                        .add(lblTransformFrom)
                        .addContainerGap(128, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(lblLocation)
                        .add(128, 128, 128))))
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .addContainerGap()
                    .add(cboTransformFrom, 0, 239, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(btnExportToXML)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnViewValidationErrors)
                    .add(btnViewXmlDoc))
                .add(39, 39, 39)
                .add(btnMakePlain)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(checkChangeColumns)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 69, Short.MAX_VALUE)
                .add(lblLocation)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cboTransformLocation, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(lblTransformFrom)
                .add(66, 66, 66)
                .add(btnExport)
                .addContainerGap())
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(279, Short.MAX_VALUE)
                    .add(cboTransformFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(69, 69, 69)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void checkChangeColumnsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkChangeColumnsActionPerformed
// TODO add your handling code here:
        short columns = ooDocument.getPageColumns();
        if (this.checkChangeColumns.isSelected()) {
            if (2 != columns) {
                ooDocument.setPageColumns((short) 2);
            }
        } else {
            if (1 != columns) {
                ooDocument.setPageColumns((short) 1);
            }
        }
    }//GEN-LAST:event_checkChangeColumnsActionPerformed

    private void btnViewValidationErrorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewValidationErrorsActionPerformed
        // TODO add your handling code here:
        viewExportErrors();
    }//GEN-LAST:event_btnViewValidationErrorsActionPerformed

    private void btnViewXmlDocMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewXmlDocMousePressed
        // TODO add your handling code here:
        viewXmlDoc(evt);
    }//GEN-LAST:event_btnViewXmlDocMousePressed

    private void btnMakePlainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMakePlainActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMakePlainActionPerformed

    @Override
    public void initialize() {
        super.initialize();
        CommonUIFunctions.compOrientation(this);
        this.initTransfromTargetCombo();
        this.initExportDestCombo();
        String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
        // if (docType.equalsIgnoreCase("debaterecord")) {
        //     this.checkboxMakePlain.setVisible(true);
        // } else
        //     this.checkboxMakePlain.setVisible(false);
    }

    @Override
    public void cleanup() {
        //
    }

    public void refreshPanel() {
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnExportToXML;
    private javax.swing.JButton btnMakePlain;
    private javax.swing.JButton btnViewValidationErrors;
    private javax.swing.JButton btnViewXmlDoc;
    private javax.swing.JComboBox cboTransformFrom;
    private javax.swing.JComboBox cboTransformLocation;
    private javax.swing.JCheckBox checkChangeColumns;
    private javax.swing.JLabel lblLocation;
    private javax.swing.JLabel lblTransformFrom;
    // End of variables declaration//GEN-END:variables
}
