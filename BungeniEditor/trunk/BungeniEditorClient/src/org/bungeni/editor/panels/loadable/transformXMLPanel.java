/*
 * transformXMLPanel.java
 *
 * Created on May 20, 2008, 10:59 AM
 */
package org.bungeni.editor.panels.loadable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import org.apache.log4j.Logger;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.editor.panels.impl.BaseClassForITabbedPanel;
import org.bungeni.extutils.BungeniFrame;
import org.bungeni.extutils.CommonDocumentUtilFunctions;
import org.bungeni.extutils.CommonEditorFunctions;
import org.bungeni.extutils.CommonFileFunctions;
import org.bungeni.extutils.CommonShellFunctions;
import org.bungeni.extutils.FrameLauncher;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.transforms.impl.BungeniTransformationTarget;
import org.bungeni.ooo.transforms.impl.BungeniTransformationTargetFactory;
import org.bungeni.ooo.transforms.impl.IBungeniDocTransform;
import org.bungeni.extutils.MessageBox;
import org.bungeni.restlet.client.TransformerClient;
import org.bungeni.utils.externalplugin.ExternalPlugin;
import org.bungeni.utils.externalplugin.ExternalPluginLoader;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

/**
 *
 * @author  Administrator
 */
public class transformXMLPanel extends BaseClassForITabbedPanel {

    private static org.apache.log4j.Logger log = Logger.getLogger(transformXMLPanel.class.getName());
    private TransformerClient transformerClient;
    private ImageIcon OFF_ICON = new javax.swing.ImageIcon(getClass().getResource("/gui/btn_off.png"));
    private ImageIcon ON_ICON = new javax.swing.ImageIcon(getClass().getResource("/gui/btn_on.png"));
    private final String _OFF_NAME_ = "off";
    private final String _ON_NAME_ = "on";
    private boolean transformerServerRunning = false;
    private Timer transformerServerTimer;
    private SAXBuilder saxBuilder = null;
    private final HashMap<String, BungeniTransformationTarget> __TRANSFORMATION_TARGETS__ = new HashMap<String, BungeniTransformationTarget>() {

        {
            put("PDF", new BungeniTransformationTarget("PDF", bundle.getString("PDF_Format"), "pdf", "org.bungeni.ooo.transforms.loadable.PDFTransform"));
            put("HTML", new BungeniTransformationTarget("HTML", bundle.getString("HTML_Format"), "html", "org.bungeni.ooo.transforms.loadable.HTMLTransform"));
            put("AN-XML", new BungeniTransformationTarget("AN-XML", bundle.getString("AN_Format"), "xml", "org.bungeni.ooo.transforms.loadable.AnXmlTransform"));
        }
    };
    private  final HashMap<String, exportDestination> __EXPORT_DESTINATIONS__ = new HashMap<String, exportDestination>() {

        {
            put("ToBungeniServer", new exportDestination("ToBungeniServer", bundle.getString("Export_into_a_Bungeni_Server")));
            put("FTP", new exportDestination("FTP", bundle.getString("Export_using_FTP")));
            put("FileSystem", new exportDestination("FileSystem", bundle.getString("Folder_on_your_computer")));
        }
    };

    /** Creates new form transformXMLPanel */
    public transformXMLPanel() {
        initComponents();
        initTransformerClient();
        initTimers();
    }

    /**
     * Start the timer
     */
    private void initTimers() {
        transformerServerTimer = new Timer(5000, new BungeniTransformerServerStateListener());
        transformerServerTimer.setInitialDelay(4000);
        transformerServerTimer.start();
    }

    /**
     * Setup the transformer client
     */
    private void initTransformerClient() {
        final ClassLoader savedClassLoader = Thread.currentThread().getContextClassLoader();
        String serverName = BungeniEditorProperties.getEditorProperty("transformerServerName");
        String serverPort = BungeniEditorProperties.getEditorProperty("transformerServerPort");
        try {
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            TransformerClient.setServerName(serverName);
            TransformerClient.setServerPort(serverPort);
            transformerClient = new TransformerClient();
            //check if server is running ... if its not start it
            if (!transformerClient.isServerRunning()) {
                System.out.println("Server NOT running");
                if (startTransformationServer()) {
                    System.out.println("SERVER WAS STARTED");
                }

            } else {
                System.out.println("Server already running");
            }

        } catch (Exception ex) {
            System.out.println("printing errors ....");
            ex.printStackTrace(System.out);
        } finally {
            Thread.currentThread().setContextClassLoader(savedClassLoader);
        }
    }

    /**
     * Starts the transformation server via a shell call
     * @return
     */
    private boolean startTransformationServer() {
        boolean bState = false;
        try {
            //first build the command string
            // java -jar
            ArrayList<String> commands = new ArrayList<String>(0);
            String javaHome = System.getProperty("java.home");
            String javaBinary = javaHome + File.separator + "bin" + File.separator + "java";
            commands.add(javaBinary);
            commands.add("-jar");
            String transformerJar = BungeniEditorProperties.getEditorProperty("transformerJar");
            transformerJar = CommonFileFunctions.convertRelativePathToFullPath(transformerJar);
            commands.add(transformerJar);
            String transformerWorkingDir = BungeniEditorProperties.getEditorProperty("transformerWorkingDir");
            transformerWorkingDir = CommonFileFunctions.convertRelativePathToFullPath(transformerWorkingDir) + File.separator;
            commands.add(transformerWorkingDir);
            String runString = javaBinary + " -jar " + transformerJar + " " + transformerWorkingDir;
            //now run the command
            boolean output = CommonShellFunctions.runCommand(commands, transformerWorkingDir, true);
            this.transformerServerRunning = true;
            bState = true;
        } catch (Exception ex) {
            log.error("startTransformationServer :", ex);
            ex.printStackTrace(System.out);
        }
        return bState;
    }

    /**
     * Runs a check in the background thread to see if the server is runing
     */
    class transformerServerStateRunner extends SwingWorker<Boolean, Void> {

        public transformerServerStateRunner() {
        }

        @Override
        public Boolean doInBackground() {
            Boolean bState = false;
            try {
                if (transformerClient != null) {
                    if (transformerClient.isServerRunning()) {
                        bState = true;
                    } else {
                        bState = false;
                    }
                }
            } catch (Exception ex) {
                log.error("doInBackround : " + ex.getMessage());
                log.error("doInBackground : ", ex);
            }
            return bState;
        }

        @Override
        protected void done() {
            try {
                Boolean bState = get();
                transformerServerRunning = bState;
                if (!bState) {
                    //server is not running
                    btnTransformerServer.setIcon(OFF_ICON);
                    txtServerMsg.setText(bundle.getString("The_Transformer_Server_is_currently_not_running"));
                } else {
                    btnTransformerServer.setIcon(ON_ICON);
                    txtServerMsg.setText(bundle.getString("The_Transformer_Server_is_running_on_port") + BungeniEditorProperties.getEditorProperty("transformerServerPort"));
                }
            } catch (InterruptedException ex) {
                log.error("done ", ex);
            } catch (ExecutionException ex) {
                log.error("done ", ex);
            }
        }
    }
    /**
     * handle to resource bundle
     */
    private static final ResourceBundle bundle = ResourceBundle.getBundle("org/bungeni/editor/panels/loadable/Bundle");

    /**
     * Timer task that checks if the transformer server is running
     */
    class BungeniTransformerServerStateListener implements ActionListener {

        public BungeniTransformerServerStateListener() {
        }

        public void actionPerformed(ActionEvent e) {
            (new transformerServerStateRunner()).execute();
        //   processActionStatesForSelectedTab();
        }
    }



    private void initTransfromTargetCombo() {
        BungeniTransformationTarget[] targets =  __TRANSFORMATION_TARGETS__.values().toArray(new BungeniTransformationTarget[__TRANSFORMATION_TARGETS__.size()]);
        DefaultComboBoxModel model = new DefaultComboBoxModel(targets);
        this.cboTransformFrom.setModel(model);
    }

    private void initExportDestCombo() {
        exportDestination[] destinations = __EXPORT_DESTINATIONS__.values().toArray(new exportDestination[__EXPORT_DESTINATIONS__.size()]);
        DefaultComboBoxModel model = new DefaultComboBoxModel(destinations);
        this.cboExportTo.setModel(model);
    }

    public class StructValidatorEventHandler {

        public StructValidatorEventHandler() {
        }

        public void dispatchEvent(String arg0, Object[] arg1) {
            String dispatchSectionName = (String) arg1[0];
            System.out.println(dispatchSectionName);
        }
    }

    class transformerServerActionListener implements ActionListener {

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

                return new Boolean(true);
            }

            @Override
            public void done() {
                btnTransformerServer.setEnabled(true);
            }
        }

        public synchronized void actionPerformed(ActionEvent e) {
            //get the button originating the event
            final JButton sourceButton = (JButton) e.getSource();
            //disable the button immediately
            sourceButton.setEnabled(false);
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
            MessageBox.OK(parentFrame, bundle.getString("Please_save_the_document"), bundle.getString("Save_the_document"), JOptionPane.ERROR_MESSAGE);
        }

    }

    public void goToSectionPosition(String sectionName) {
        System.out.println(sectionName);
        CommonDocumentUtilFunctions.selectSection(ooDocument, sectionName);
    }

    private void exportToXml() {

        //get a handle to the AN xml transformer
        BungeniTransformationTarget transform = __TRANSFORMATION_TARGETS__.get("AN-XML");
        IBungeniDocTransform iTransform = BungeniTransformationTargetFactory.getTransformClass(transform);
        HashMap<String, Object> params = new HashMap<String, Object>();
        iTransform.setParams(params);
        boolean bState = iTransform.transform(ooDocument);
        if (bState) {
            MessageBox.OK(parentFrame, bundle.getString("Document_was_successfully_Exported_to_the_workspace_folder"));
        } else {
            MessageBox.OK(parentFrame, bundle.getString("Document_export_failed"));
        }
    }

    private boolean errorsExist(Document xmlDoc) {
        boolean bState = false;
        try {
            XPath xmlPath = XPath.newInstance("/validationErrors/validationError");
            xmlPath.selectSingleNode(xmlDoc);
            bState = true;
        } catch (JDOMException ex) {
            bState = false;
        }
        return bState;

    }


    private void viewExportErrors() {
        //check if errors file exists
        if (ooDocument.isDocumentOnDisk()) {
            boolean bNoErrors = true;
            String sUrl = ooDocument.getDocumentURL();
            File fFile = CommonFileFunctions.convertUrlToFile(sUrl);
            File fErrors = new File(fFile.getParent() + File.separator + "errors.xml");
            if (fErrors.exists()) {
                if (saxBuilder == null ) saxBuilder = new SAXBuilder(BungeniEditorProperties.SAX_PARSER_DRIVER, false);
                Document xmlErrors = null;
                try {
                    xmlErrors = saxBuilder.build(fErrors);
                } catch (JDOMException ex) {
                   log.error("viewXmErrors ", ex);
                } catch (IOException ex) {
                   log.error("viewXmErrors ", ex);
                }
                if (xmlErrors != null) {
                    if (errorsExist(xmlErrors)) {
                        bNoErrors = false;
                        launchErrorPanel(xmlErrors);
                        return;
                    }
                }
            }
            if (bNoErrors) {
                MessageBox.OK(parentFrame, "There were no validation errors !");
            }

        } else {
            MessageBox.OK(parentFrame, "The document has not been saved or transformed to XML. \n" +
                    "Please save the document and attempt a transformation to XML before trying to view errors");
        }
    }

    private void launchErrorPanel(Document xmlDoc) {
        BungeniFrame frame = new BungeniFrame("Validation Errors");
        validationErrorPanel vpanel = new validationErrorPanel(this, frame, xmlDoc);
        frame.add(vpanel);
        frame.setSize(436, 298);
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

        cboTransformFrom = new javax.swing.JComboBox();
        cboExportTo = new javax.swing.JComboBox();
        lblTransformFrom = new javax.swing.JLabel();
        btnExport = new javax.swing.JButton();
        checkChangeColumns = new javax.swing.JCheckBox();
        btnMakePlain = new javax.swing.JButton();
        btnTransformerServer = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtServerMsg = new javax.swing.JTextArea();
        btnExportToXML = new javax.swing.JButton();
        btnViewValidationErrors = new javax.swing.JButton();
        btnViewXmlDoc = new javax.swing.JButton();

        cboTransformFrom.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        cboTransformFrom.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Portable Document Format (PDF)", "AkomaNtoso XML", "XHTML - eXtensible HTML", "Marginalia-safe HTML export" }));

        cboExportTo.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        cboExportTo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Export to File-System path", "Export to Server" }));

        lblTransformFrom.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/panels/loadable/Bundle"); // NOI18N
        lblTransformFrom.setText(bundle.getString("transformXMLPanel.lblTransformFrom.text")); // NOI18N

        btnExport.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnExport.setText(bundle.getString("transformXMLPanel.btnExport.text")); // NOI18N
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });

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

        btnTransformerServer.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnTransformerServer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/btn_off.png"))); // NOI18N
        btnTransformerServer.setText(bundle.getString("transformXMLPanel.btnTransformerServer.text")); // NOI18N
        btnTransformerServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransformerServerActionPerformed(evt);
            }
        });

        txtServerMsg.setColumns(20);
        txtServerMsg.setEditable(false);
        txtServerMsg.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        txtServerMsg.setLineWrap(true);
        txtServerMsg.setRows(5);
        txtServerMsg.setWrapStyleWord(true);
        jScrollPane1.setViewportView(txtServerMsg);

        btnExportToXML.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnExportToXML.setText(bundle.getString("transformXMLPanel.btnExportToXML.text")); // NOI18N
        btnExportToXML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportToXMLActionPerformed(evt);
            }
        });

        btnViewValidationErrors.setFont(new java.awt.Font("DejaVu Sans", 0, 9)); // NOI18N
        btnViewValidationErrors.setText(bundle.getString("transformXMLPanel.btnViewValidationErrors.text")); // NOI18N
        btnViewValidationErrors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewValidationErrorsActionPerformed(evt);
            }
        });

        btnViewXmlDoc.setFont(new java.awt.Font("DejaVu Sans", 0, 9)); // NOI18N
        btnViewXmlDoc.setText(bundle.getString("transformXMLPanel.btnViewXmlDoc.text")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(checkChangeColumns, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 180, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, lblTransformFrom, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, btnViewValidationErrors, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .add(5, 5, 5)
                        .add(btnViewXmlDoc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 96, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, btnTransformerServer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                            .add(btnMakePlain, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(btnExportToXML, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(cboExportTo, 0, 222, Short.MAX_VALUE)
                            .add(cboTransformFrom, 0, 222, Short.MAX_VALUE))))
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .add(btnExport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(35, 35, 35))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(btnTransformerServer)
                .add(1, 1, 1)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnMakePlain)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(checkChangeColumns)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnExportToXML)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnViewValidationErrors)
                    .add(btnViewXmlDoc))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 23, Short.MAX_VALUE)
                .add(lblTransformFrom)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cboTransformFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(cboExportTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(btnExport)
                .addContainerGap())
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

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
// TODO add your handling code here:
        BungeniTransformationTarget transform = (BungeniTransformationTarget) this.cboTransformFrom.getSelectedItem();
        IBungeniDocTransform iTransform = BungeniTransformationTargetFactory.getTransformClass(transform);

        String exportPath = BungeniEditorProperties.getEditorProperty("defaultExportPath");
        exportPath = exportPath.replace('/', File.separatorChar);
        exportPath = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator + exportPath;
        exportPath = exportPath + File.separatorChar + OOComponentHelper.getFrameTitle(ooDocument.getTextDocument()).trim() + "." + transform.targetExt;
        File fileExp = new File(exportPath);
        String exportPathURL = "";
        exportPathURL = fileExp.toURI().toString();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("StoreToUrl", exportPathURL);

        iTransform.setParams(params);
        boolean bState = iTransform.transform(ooDocument);
        if (bState) {
            MessageBox.OK(parentFrame, bundle.getString("Document_was_successfully_Exported_to_the_workspace_folder"));
        } else {
            MessageBox.OK(parentFrame, bundle.getString("Document_export_failed"));
        }
    }//GEN-LAST:event_btnExportActionPerformed

    private void btnMakePlainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMakePlainActionPerformed
        //  generatePlainDocument();
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
            convertToPlain.callMethod("setParams", argSetParams);
            Object[] argExec = {};
            Object retValue = convertToPlain.callMethod("exec", argExec);
            if (retValue != null) {
                String outputFilePath = (String) retValue;
                File fFile = CommonFileFunctions.convertUrlToFile(outputFilePath);
                int nRet = MessageBox.Confirm(parentFrame, bundle.getString("Click_Yes_to_open_the_plain_document_\n_Click_No_to_close_this_window"), bundle.getString("Document_Successfully_Converted!"));
                if (nRet == JOptionPane.YES_OPTION) {
                    OOComponentHelper.openExistingDocument(fFile.getAbsolutePath());
                }
            //MessageBox.OK(parentFrame, "A plain document was generated, it can be found at : \n" + outputFilePath, "Plain Document generation", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            MessageBox.OK(parentFrame, bundle.getString("Please_save_the_document"), bundle.getString("Save_the_document"), JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnMakePlainActionPerformed

    private void btnTransformerServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransformerServerActionPerformed
        // TODO add your handling code here:
        if (this.transformerServerRunning) {
            MessageBox.OK(parentFrame, "Transformer Server is Running");
        } else {
            MessageBox.OK(parentFrame, "Transformer Server is down");
        }
    }//GEN-LAST:event_btnTransformerServerActionPerformed

    private void btnExportToXMLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportToXMLActionPerformed
        exportToXml();
    }//GEN-LAST:event_btnExportToXMLActionPerformed

    private void btnViewValidationErrorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewValidationErrorsActionPerformed
        // TODO add your handling code here:
        viewExportErrors();
    }//GEN-LAST:event_btnViewValidationErrorsActionPerformed

    @Override
    public void initialize() {
        super.initialize();
        this.initTransfromTargetCombo();
        this.initExportDestCombo();
        String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
    // if (docType.equalsIgnoreCase("debaterecord")) {
    //     this.checkboxMakePlain.setVisible(true);
    // } else
    //     this.checkboxMakePlain.setVisible(false);
    }

    public void refreshPanel() {
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnExportToXML;
    private javax.swing.JButton btnMakePlain;
    private javax.swing.JButton btnTransformerServer;
    private javax.swing.JButton btnViewValidationErrors;
    private javax.swing.JButton btnViewXmlDoc;
    private javax.swing.JComboBox cboExportTo;
    private javax.swing.JComboBox cboTransformFrom;
    private javax.swing.JCheckBox checkChangeColumns;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTransformFrom;
    private javax.swing.JTextArea txtServerMsg;
    // End of variables declaration//GEN-END:variables
}
