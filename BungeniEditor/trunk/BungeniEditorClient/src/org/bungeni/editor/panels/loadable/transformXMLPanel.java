/*
 * transformXMLPanel.java
 *
 * Created on May 20, 2008, 10:59 AM
 */

package org.bungeni.editor.panels.loadable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
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
import org.bungeni.extutils.CommonDocumentUtilFunctions;
import org.bungeni.extutils.CommonEditorFunctions;
import org.bungeni.extutils.CommonFileFunctions;
import org.bungeni.extutils.CommonShellFunctions;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.transforms.impl.BungeniTransformationTarget;
import org.bungeni.ooo.transforms.impl.BungeniTransformationTargetFactory;
import org.bungeni.ooo.transforms.impl.IBungeniDocTransform;
import org.bungeni.extutils.MessageBox;
import org.bungeni.restlet.client.TransformerClient;
import org.bungeni.utils.externalplugin.ExternalPlugin;
import org.bungeni.utils.externalplugin.ExternalPluginLoader;


/**
 *
 * @author  Administrator
 */
public class transformXMLPanel extends BaseClassForITabbedPanel{
       private static org.apache.log4j.Logger log = Logger.getLogger(transformXMLPanel.class.getName());
       private TransformerClient transformerClient ;
       private ResourceBundle panelBundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/panels/loadable/Bundle");
       private ImageIcon OFF_ICON = new javax.swing.ImageIcon(getClass().getResource("/gui/btn_off.png"));
       private ImageIcon ON_ICON = new javax.swing.ImageIcon(getClass().getResource("/gui/btn_on.png"));
       private final String _OFF_NAME_ = "off";
       private final String _ON_NAME_ = "on";
       private boolean transformerServerRunning = false;
       private Timer transformerServerTimer ;

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
    private void initTransformerClient(){
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

          public transformerServerStateRunner () {
          }

         @Override
          public Boolean doInBackground() {
                Boolean bState = false;
                try {
                    if (transformerClient != null) {
                        if (transformerClient.isServerRunning())
                           bState = true;
                         else
                           bState = false;
                    }
                } catch (Exception ex) {
                        log.error("doInBackround : " + ex.getMessage());
                        log.error("doInBackground : ", ex);
                }
                return bState;
            }

            @Override
            protected void done(){
            try {
                Boolean bState = get();
                transformerServerRunning = bState;
                if (!bState) {
                    //server is not running
                    btnTransformerServer.setIcon(OFF_ICON);
                    txtServerMsg.setText("The Transformer Server is currently not running");
                } else {
                    btnTransformerServer.setIcon(ON_ICON);
                    txtServerMsg.setText("The Transformer Server is running on port "+ BungeniEditorProperties.getEditorProperty("transformerServerPort"));
                }
            } catch (InterruptedException ex) {
               log.error("done ", ex);
            } catch (ExecutionException ex) {
               log.error("done ", ex);      }
            }
        }


      /**
       * Timer task that checks if the transformer server is running
       */
 class BungeniTransformerServerStateListener implements ActionListener {
       

        public BungeniTransformerServerStateListener(){
           
        }

        public void actionPerformed(ActionEvent e) {
            (new transformerServerStateRunner()).execute();
         //   processActionStatesForSelectedTab();
        }
    }

    class exportDestination extends Object {
        String exportDestName;
        String exportDestDesc;
        
        exportDestination(String name, String desc) {
            this.exportDestDesc = desc;
            this.exportDestName = name;
        }

        @Override
        public String toString(){
            return this.exportDestDesc;
        }
    }
    
    private void initTransfromTargetCombo(){
        ArrayList<BungeniTransformationTarget> transArr = new ArrayList<BungeniTransformationTarget>(0);
        transArr.add(new BungeniTransformationTarget("PDF", "PDF (Portable Document Format)", "pdf" , "org.bungeni.ooo.transforms.loadable.PDFTransform"));
        transArr.add(new BungeniTransformationTarget("HTML", "HTML (Hypertext Web Document)", "html", "org.bungeni.ooo.transforms.loadable.HTMLTransform"));
        transArr.add(new BungeniTransformationTarget("AN-XML", "AN (AkomaNtoso XML Document)", "xml", "org.bungeni.ooo.transforms.loadable.AnXmlTransform"));
        DefaultComboBoxModel model = new DefaultComboBoxModel(transArr.toArray());
        this.cboTransformFrom.setModel(model);
    }
    
    private void initExportDestCombo(){
        ArrayList<exportDestination> transArr = new ArrayList<exportDestination>(0);
        transArr.add(new exportDestination("FileSystem", "Folder on your computer"));
        transArr.add(new exportDestination("ToBungeniServer", "Export into a Bungeni Server"));
        transArr.add(new exportDestination("FTP", "Export using FTP"));
        DefaultComboBoxModel model = new DefaultComboBoxModel(transArr.toArray());
        this.cboExportTo.setModel(model);
    }

    public class StructValidatorEventHandler  {
        public StructValidatorEventHandler(){

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
          JButton sourceButton ;

          public buttonActionRunner (JButton origButton) {
              this.sourceButton = origButton;
          }

          protected Boolean doInBackground() throws Exception {
                    //check if server is running .. if running stop it

                        return new Boolean(true);
            }

            @Override
          public void done(){
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
     
            Object[] argSetParams = {new HashMap() {{
                                            put("OdfFileURL", odfFileUrl);
                                            put("CurrentDocType", currentDocType);
                                            put("RulesRootFolder",rulesRootFolder);
                                            put("ParentFrame", parentFrame);
                                            put("CallerPanel", ppPanel);
                                            put("ParentEventDispatcher", seHandler);
                                            put("ParentEventDispatcherClass", "org.bungeni.editor.panels.loadable.transformXMLPanel$StructValidatorEventHandler");
                                        }}};
            rulesValidator.callMethod("setParams", argSetParams);
            Object[] argExec = {};
            Object retValue = rulesValidator.callMethod("exec", argExec);
            if (retValue != null) {
                    String outputFilePath = (String) retValue;
                   // MessageBox.OK(parentFrame, "A plain document was generated, it can be found at : \n" + outputFilePath, "Plain Document generation", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            MessageBox.OK(parentFrame, "Please save the document before proceeding !", "Save the document", JOptionPane.ERROR_MESSAGE);
        }

    }

public void goToSectionPosition(String sectionName) {
    System.out.println(sectionName);
    CommonDocumentUtilFunctions.selectSection(ooDocument, sectionName);
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
        lblExportTo = new javax.swing.JLabel();
        lblTransformFrom = new javax.swing.JLabel();
        btnExport = new javax.swing.JButton();
        checkChangeColumns = new javax.swing.JCheckBox();
        btnMakePlain = new javax.swing.JButton();
        btnTransformerServer = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtServerMsg = new javax.swing.JTextArea();

        cboTransformFrom.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        cboTransformFrom.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Portable Document Format (PDF)", "AkomaNtoso XML", "XHTML - eXtensible HTML", "Marginalia-safe HTML export" }));

        cboExportTo.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        cboExportTo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Export to File-System path", "Export to Server" }));

        lblExportTo.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/panels/loadable/Bundle"); // NOI18N
        lblExportTo.setText(bundle.getString("transformXMLPanel.lblExportTo.text")); // NOI18N

        lblTransformFrom.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
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

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 210, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(btnTransformerServer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 210, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblExportTo)
                            .add(checkChangeColumns, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 180, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblTransformFrom)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, cboTransformFrom, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, cboExportTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 202, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(layout.createSequentialGroup()
                                .add(27, 27, 27)
                                .add(btnExport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 143, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(btnMakePlain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 210, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(btnTransformerServer)
                .add(1, 1, 1)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnMakePlain)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblExportTo)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(checkChangeColumns)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(lblTransformFrom)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cboTransformFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(25, 25, 25)
                .add(cboExportTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(btnExport)
                .addContainerGap(30, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void checkChangeColumnsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkChangeColumnsActionPerformed
// TODO add your handling code here:
        short columns = ooDocument.getPageColumns();
        if (this.checkChangeColumns.isSelected()) {
            if (2 != columns ) {
            ooDocument.setPageColumns((short)2);
            }
        } else {
            if (1 != columns ) {
                ooDocument.setPageColumns((short)1);
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
        exportPath = exportPath + File.separatorChar + OOComponentHelper.getFrameTitle(ooDocument.getTextDocument()).trim()+"."+transform.targetExt;
        File fileExp = new File(exportPath);
        String exportPathURL = "";
            exportPathURL = fileExp.toURI().toString();
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("StoreToUrl", exportPathURL);
        
        iTransform.setParams(params);
        boolean bState= iTransform.transform(ooDocument);
        if (bState ) {
            MessageBox.OK(parentFrame, "Document was successfully Exported to the workspace folder");
        } else
            MessageBox.OK(parentFrame, "Document export failed");
    }//GEN-LAST:event_btnExportActionPerformed



    private void btnMakePlainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMakePlainActionPerformed
          //  generatePlainDocument();
        if (ooDocument.isDocumentOnDisk()) {
          //  generatePlainDocument();
            ExternalPluginLoader ep = new ExternalPluginLoader();
            ExternalPlugin convertToPlain = ep.loadPlugin("ConvertToPlain");
            Object[] argSetParams = {
                                    new HashMap() {
                                        { put("OdfFileURL", ooDocument.getDocumentURL()); }
                                        }
                                    };
            convertToPlain.callMethod("setParams", argSetParams);
            Object[] argExec = {};
            Object retValue = convertToPlain.callMethod("exec", argExec);
            if (retValue != null) {
                    String outputFilePath = (String) retValue;
                    File fFile = CommonFileFunctions.convertUrlToFile(outputFilePath);
                    int nRet = MessageBox.Confirm(parentFrame, "Click Yes to open the plain document \n Click No to close this window", "Document Successfully Converted!");
                    if (nRet == JOptionPane.YES_OPTION) {
                        OOComponentHelper.openExistingDocument(fFile.getAbsolutePath());
                    }
                    //MessageBox.OK(parentFrame, "A plain document was generated, it can be found at : \n" + outputFilePath, "Plain Document generation", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            MessageBox.OK(parentFrame, "Please save the document before proceeding !", "Save the document", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnMakePlainActionPerformed

    private void btnTransformerServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransformerServerActionPerformed
        // TODO add your handling code here:
        if (this.transformerServerRunning)
                MessageBox.OK(parentFrame, "Transformer Server is Running");
        else
                MessageBox.OK(parentFrame, "Transformer Server is down");
    }//GEN-LAST:event_btnTransformerServerActionPerformed

    

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
    private javax.swing.JButton btnMakePlain;
    private javax.swing.JButton btnTransformerServer;
    private javax.swing.JComboBox cboExportTo;
    private javax.swing.JComboBox cboTransformFrom;
    private javax.swing.JCheckBox checkChangeColumns;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblExportTo;
    private javax.swing.JLabel lblTransformFrom;
    private javax.swing.JTextArea txtServerMsg;
    // End of variables declaration//GEN-END:variables
    
}
