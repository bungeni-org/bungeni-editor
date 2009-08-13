
package org.bungeni.editor.panels.loadable;

import com.thoughtworks.xstream.XStream;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import org.apache.log4j.Logger;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.editor.panels.impl.BaseClassForITabbedPanel;
import org.bungeni.editor.panels.loadable.structuralerror.StructuralError;
import org.bungeni.editor.panels.loadable.structuralerror.StructuralErrorSerialize;
import org.bungeni.editor.panels.loadable.structuralerror.panelStructuralError;
import org.bungeni.editor.panels.loadable.structuralerror.panelStructuralErrorBrowser;
import org.bungeni.extutils.CommonANUtils;
import org.bungeni.extutils.CommonDocumentUtilFunctions;
import org.bungeni.extutils.CommonEditorFunctions;
import org.bungeni.extutils.CommonFileFunctions;
import org.bungeni.extutils.CommonXmlUtils;
import org.bungeni.extutils.MessageBox;
import org.bungeni.ooo.transforms.impl.BungeniTransformationTarget;
import org.bungeni.ooo.transforms.impl.BungeniTransformationTargetFactory;
import org.bungeni.ooo.transforms.impl.BungeniTransformationTargets;
import org.bungeni.ooo.transforms.impl.IBungeniDocTransform;
import org.bungeni.utils.externalplugin.ExternalPlugin;
import org.bungeni.utils.externalplugin.ExternalPluginLoader;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

/**
 *
 * @author Ashok Hariharan
 */
public class validateAndCheckPanel2 extends BaseClassForITabbedPanel {

    private static org.apache.log4j.Logger log = Logger.getLogger(validateAndCheckPanel2.class.getName());
    private SAXBuilder saxBuilder = null;

    /** Creates new form transformXMLPanel */
    public validateAndCheckPanel2() {
        initComponents();
        this.btnValidateStructure.addActionListener(new validateStructureActionListener());
    }

    /**
     * The name of the folder containing the engine rules
     */
   private static final String ENGINE_RULES_FOLDER = "engine_rules";

   /**
    * Describes a structural rule 
    */
    class engineRule {
        String engineName;
        String engineDesc;

        public engineRule (String eName, String eDesc) {
            this.engineName = eName;
            this.engineDesc = eDesc;
        }

        @Override
        public String toString(){
            return this.engineDesc;
        }

        public String getName(){
            return this.engineName;
        }

    }


    /**
     * Returns the list of selected structural check names
     * @return
     */
    private String[] getSelectedChecks() {
        ArrayList<String> checkNames = new ArrayList<String>(0);
        if (this.chkStructural.isSelected()) {
            for (JCheckBox aBox : this.availableRules) {
                if (aBox.isSelected()) {
                    checkNames.add(aBox.getName());
                }
            }
        }
        return checkNames.toArray(new String[checkNames.size()]);
    }

    private JPanel getThisPanel() {
        return this;
    }
    /**
     * Loads the available rule engines from the engine root fodler
     * engine Rule description files are always named after the document type and are present in the engine rules folder
     * @return
     */
    private ArrayList<engineRule> loadRuleEngines() {
        FileReader freader = null;
        ArrayList<engineRule> engineRules = new ArrayList<engineRule>(0);

        try {
            final String currentDocType = BungeniEditorPropertiesHelper.getCurrentDocType();
            final String rulesRootFolder = CommonEditorFunctions.getPathRelativeToRoot(BungeniEditorProperties.getEditorProperty("structuralRulesRootPath"));
            freader = new FileReader(rulesRootFolder + File.separator + ENGINE_RULES_FOLDER + File.separator + currentDocType + ".xml");
            SAXBuilder builder = CommonXmlUtils.getNonValidatingSaxBuilder();
            Document engineDoc = builder.build(freader);
            //get available engines
            XPath engines = XPath.newInstance("/rules/engine");
            List foundNodes = engines.selectNodes(engineDoc);
            for (int i = 0; i < foundNodes.size() ; i++) {
                Element foundElement = (Element) foundNodes.get(i);
                String foundElemName = foundElement.getAttributeValue("name");
                String foundElemDesc = foundElement.getAttributeValue("desc");
                engineRule eRule = new engineRule (foundElemName, foundElemDesc);
                engineRules.add(eRule);
            }
        } catch (JDOMException ex) {
            log.error("loadRuleEngines", ex);
        } catch (IOException ex) {
            log.error("loadRuleEngines", ex);
        } finally {
            try {
                freader.close();
            } catch (IOException ex) {
                log.error("loadRuleEngines", ex);
            }
        }
        return engineRules;
    }

    
    public class StructValidatorEventHandler {

        public StructValidatorEventHandler() {
        }

        public void dispatchEvent(String arg0, Object[] arg1) {
            String dispatchSectionName = (String) arg1[0];
            System.out.println(dispatchSectionName);
        }
    }
    private static final ResourceBundle bundle = ResourceBundle.getBundle("org/bungeni/editor/panels/loadable/Bundle");

    private Object[] getValidatePluginParams() {
        final String odfFileUrl = ooDocument.getDocumentURL();
        final String currentDocType = BungeniEditorPropertiesHelper.getCurrentDocType();
        final String rulesRootFolder = CommonEditorFunctions.getPathRelativeToRoot(BungeniEditorProperties.getEditorProperty("structuralRulesRootPath"));
        final validateAndCheckPanel2 ppPanel = this;
        final String[] runTheseChecks = getSelectedChecks();

        Object[] argSetParams = {new HashMap() {

        {
            put("OdfFileURL", odfFileUrl);
            put("CurrentDocType", currentDocType);
            put("RulesRootFolder", rulesRootFolder);
            //which checks must be run ? if empty no tests are run
            put("RunChecks", runTheseChecks);
            put("ParentFrame", parentFrame);
            put("CallerPanel", ppPanel);
        }
    }};
        return argSetParams;
    }

    private void viewErrorLog() {
        if (ooDocument.isDocumentOnDisk()) {
            panelStructuralErrorBrowser.launchFrame(ooDocument.getDocumentURL(), this.parentFrame, this);
             /*
            ExternalPluginLoader ep = new ExternalPluginLoader();
            final StructValidatorEventHandler seHandler = new StructValidatorEventHandler();
            ExternalPlugin rulesValidator = ep.loadPlugin("StructuralRulesValidator");
            Object[] argSetParams = getValidatePluginParams();
            rulesValidator.callMethod("setParams", argSetParams);
            Object[] argParams = {};
            Object[] argExec = {argParams};
            Object retValue = rulesValidator.callMethod("exec2", argExec);
              */


        }
    }

    class validateStructureActionListener implements ActionListener {

        /**
         * Run the button action in a swingworker thread, so the UI disabling happens immediately
         */
        class buttonActionRunner extends SwingWorker<Integer, Object> {

            JButton sourceButton;
           
            public buttonActionRunner(JButton origButton) {
                this.sourceButton = origButton;
                //change the curssor to the wait cursor
                sourceButton.setEnabled(false);
                 //set the wait cursor
                parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            }

            protected Integer doInBackground() throws Exception {
                //check if server is running .. if running stop it
                int nRet = -1;
               /// nRet = validateStructure();
                return nRet;
            }

            @Override
            public void done() {
                int nRet = -2;
                try {
                    sourceButton.setEnabled(true);
                    parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    nRet = get();
                } catch (InterruptedException ex) {
                    log.error("done(),", ex);
                } catch (ExecutionException ex) {
                    log.error("done(),", ex);
                }
                switch (nRet) {
                    case -1:
                        MessageBox.OK(parentFrame, bundle.getString("problem_running_struct_checker"), bundle.getString("save_the_document"), JOptionPane.ERROR_MESSAGE);
                        break;
                    case 0:
                        break;
                    case -2:
                    default:
                        MessageBox.OK(parentFrame, bundle.getString("save_document_before_proceeding"), bundle.getString("save_the_document"), JOptionPane.ERROR_MESSAGE);
                        break;

                }
            }
        }

        Object structuralCheckReturnValue = null;
        
        public synchronized void actionPerformed(ActionEvent e) {
            //get the button originating the event
            final JButton sourceButton = (JButton) e.getSource();
            //disable the button immediately
            sourceButton.setEnabled(false);
            //set the wait cursor
            parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //eliminate all the error conditions
            if (!chkStructural.isSelected() && !chkValidation.isSelected()) {
                MessageBox.OK(getThisPanel(), "You have not selected any validation options !", "No Validation Options Selected", JOptionPane.ERROR_MESSAGE);
                sourceButton.setEnabled(true);
                //set the wait cursor
                parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return;
            }

            if (chkStructural.isSelected() && !chkValidation.isSelected()) {
                if (getSelectedChecks().length == 0) {
                    MessageBox.OK(getThisPanel(), "You have not selected any structural validation options !", "No Validation Options Selected", JOptionPane.ERROR_MESSAGE);
                    sourceButton.setEnabled(true);
                    //set the wait cursor
                    parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return;
                }
            }
            
            //call the swingworker thread for the button event
            SwingUtilities.invokeLater(new Runnable() {

             public void run() {

                 if (chkStructural.isSelected()) {
                    structuralCheckReturnValue = validateStructure();
                  } else
                    structuralCheckReturnValue = null;

                  if (chkValidation.isSelected()) {
                      validateXml();
                  }

                  if (structuralCheckReturnValue != null) {
                      //process the error output
                     ArrayList<StructuralError> listErrors =  prepareStructuralErrorsOutput((String)structuralCheckReturnValue);
                     Document xmlErrors = prepareXmlValidationErrorOutput();
                  }

                 sourceButton.setEnabled(true);
                  parentFrame.setCursor(Cursor.getDefaultCursor());
                }
            });
            
           // (new buttonActionRunner(sourceButton)).execute();
        }
    }

    private Object validateStructure() {
        if (!ooDocument.documentRequiresSaving()) {
            //  generatePlainDocument();
            ExternalPluginLoader ep = new ExternalPluginLoader();

            final StructValidatorEventHandler seHandler = new StructValidatorEventHandler();
            ExternalPlugin rulesValidator = ep.loadPlugin("StructuralRulesValidator");

            Object[] argSetParams = getValidatePluginParams();
            rulesValidator.callMethod("setParams", argSetParams);
            Object[] argExec = {};
            Object retValue = rulesValidator.callMethod("exec", argExec);
            if (retValue != null) {
                System.out.println("error size = " + ((String)retValue).length());
                //String outputFilePath = (String) retValue;
                //processErrorOutput((String) retValue);
                // MessageBox.OK(parentFrame, "A plain document was generated, it can be found at : \n" + outputFilePath, "Plain Document generation", JOptionPane.INFORMATION_MESSAGE);
                return retValue;
            } else {
                MessageBox.OK(parentFrame, bundle.getString("problem_running_struct_checker"), bundle.getString("save_the_document"), JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } else {
            MessageBox.OK(parentFrame, bundle.getString("save_document_before_proceeding"), bundle.getString("save_the_document"), JOptionPane.ERROR_MESSAGE);
            return null;

        }

    }


    private void validateXml(){
        boolean transformState = false;
        transformState = transformXml();
    }

    private boolean transformXml() {
       BungeniTransformationTarget transform = BungeniTransformationTargets.getTransformationTargets().get("AN-XML");
        IBungeniDocTransform iTransform = BungeniTransformationTargetFactory.getTransformClass(transform);
        HashMap<String, Object> params = new HashMap<String, Object>();
        iTransform.setParams(params);
        boolean bState = iTransform.transform(ooDocument);
        return bState;
    }

    private Document prepareXmlValidationErrorOutput() {
          boolean bNoErrors = true;
          Document xmlErrors = null;

          String sUrl = ooDocument.getDocumentURL();
          xmlErrors = validationErrorHelper.getValidationErrors(sUrl);
          if (xmlErrors != null) {
              if (validationErrorHelper.errorsExist(xmlErrors)) {
                return xmlErrors;
              }
          }
          return null;
    }

    private ArrayList<StructuralError> prepareStructuralErrorsOutput(String outError) {
        StringReader sr = new StringReader(outError);
        ArrayList<StructuralError> listErrors = new ArrayList<StructuralError>(0);
        //System.out.println(outError);
        try {
            SAXBuilder builder = CommonXmlUtils.getNonValidatingSaxBuilder();
            Document engineDoc = builder.build(sr);
            //get available engines
            XPath engines = XPath.newInstance("/structuralErrors/list");
            Element foundNode = (Element) engines.selectSingleNode(engineDoc);
            if (foundNode != null ) {
                XMLOutputter outer = new XMLOutputter();
                StringWriter srw = new StringWriter();
                outer.output(foundNode,srw );
                InputStream is = new ByteArrayInputStream(srw.toString().getBytes());
                XStream xst = new XStream();
                xst.alias("structuralError", StructuralError.class);
                listErrors  = (ArrayList<StructuralError>) xst.fromXML(is);
                System.out.println("output error list size =  "+ listErrors.size());
                if (listErrors.size() > 0 ) {
                    StructuralErrorSerialize sez = new StructuralErrorSerialize(ooDocument.getDocumentURL());
                    //write errors to log
                    sez.writeErrorsToLog(listErrors);
                   //  panelStructuralError.launchFrame(listErrors, parentFrame, parentPanel);
                }
            } 
        } catch (JDOMException ex) {
            log.error("processErrorOutput", ex);
        } catch (IOException ex) {
            log.error("processErrorOutput", ex);
        }
        return listErrors;
    }
    
    private void processErrorOutput(String outError) {
        StringReader sr = new StringReader(outError);
        //System.out.println(outError);
        try {
            SAXBuilder builder = CommonXmlUtils.getNonValidatingSaxBuilder();
            Document engineDoc = builder.build(sr);
            //get available engines
            XPath engines = XPath.newInstance("/structuralErrors/list");
            Element foundNode = (Element) engines.selectSingleNode(engineDoc);
            if (foundNode != null ) {
                XMLOutputter outer = new XMLOutputter();
                StringWriter srw = new StringWriter();
                outer.output(foundNode,srw );
                InputStream is = new ByteArrayInputStream(srw.toString().getBytes());
                XStream xst = new XStream();
                xst.alias("structuralError", StructuralError.class);
                ArrayList<StructuralError> listErrors  = (ArrayList<StructuralError>) xst.fromXML(is);
                System.out.println("output error list size =  "+ listErrors.size());
                if (listErrors.size() > 0 ) {
                    StructuralErrorSerialize sez = new StructuralErrorSerialize(ooDocument.getDocumentURL());
                    sez.writeErrorsToLog(listErrors);
                    panelStructuralError.launchFrame(listErrors, parentFrame, parentPanel);
                }
            } else {
                //no errors were found
            }
        } catch (JDOMException ex) {
            log.error("processErrorOutput", ex);
        } catch (IOException ex) {
            log.error("processErrorOutput", ex);
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

        btnValidateStructure = new javax.swing.JButton();
        lblSelect = new javax.swing.JLabel();
        btnViewErrorLog = new javax.swing.JButton();
        chkStructural = new javax.swing.JCheckBox();
        chkValidation = new javax.swing.JCheckBox();
        scrollChecks = new javax.swing.JScrollPane();

        btnValidateStructure.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/panels/loadable/Bundle"); // NOI18N
        btnValidateStructure.setText(bundle.getString("validateAndCheckPanel2.btnValidateStructure.text")); // NOI18N

        lblSelect.setFont(new java.awt.Font("DejaVu Sans", 1, 10));
        lblSelect.setText(bundle.getString("validateAndCheckPanel2.lblSelect.text")); // NOI18N

        btnViewErrorLog.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnViewErrorLog.setText(bundle.getString("validateAndCheckPanel2.btnViewErrorLog.text")); // NOI18N
        btnViewErrorLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewErrorLogActionPerformed(evt);
            }
        });

        chkStructural.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        chkStructural.setText(bundle.getString("validateAndCheckPanel2.chkStructural.text")); // NOI18N
        chkStructural.setToolTipText(bundle.getString("validateAndCheckPanel2.chkStructural.toolTipText")); // NOI18N
        chkStructural.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        chkStructural.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkStructuralItemStateChanged(evt);
            }
        });

        chkValidation.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        chkValidation.setText(bundle.getString("validateAndCheckPanel2.chkValidation.text")); // NOI18N
        chkValidation.setToolTipText(bundle.getString("validateAndCheckPanel2.chkValidation.toolTipText")); // NOI18N

        scrollChecks.setToolTipText(bundle.getString("validateAndCheckPanel2.scrollChecks.toolTipText")); // NOI18N
        scrollChecks.setAutoscrolls(true);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(lblSelect, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 172, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(chkStructural)
                    .add(chkValidation)
                    .add(layout.createSequentialGroup()
                        .add(12, 12, 12)
                        .add(scrollChecks, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 168, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(btnValidateStructure, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(18, 18, 18)
                        .add(btnViewErrorLog, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(lblSelect)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkStructural)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(scrollChecks, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(chkValidation)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 84, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnValidateStructure)
                    .add(btnViewErrorLog, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewErrorLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewErrorLogActionPerformed
        // TODO add your handling code here:
        viewErrorLog();
    }//GEN-LAST:event_btnViewErrorLogActionPerformed

    private void chkStructuralItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkStructuralItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            for (JCheckBox jCheckBox : availableRules) {
                jCheckBox.setEnabled(true);
            }
        } else {
            for (JCheckBox jCheckBox : availableRules) {
                jCheckBox.setEnabled(false);
            }

        }
    }//GEN-LAST:event_chkStructuralItemStateChanged


    private ArrayList<JCheckBox> availableRules = new ArrayList<JCheckBox>(0);


    @Override
    public void initialize() {
        super.initialize();
        String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
        ArrayList<engineRule> rules = this.loadRuleEngines();
        java.awt.Font checkboxFont = new java.awt.Font("DejaVu Sans", 0, 10);
        JPanel checkBoxPanel = new JPanel();
        Border empty = new EmptyBorder(0,0,0,0);
        scrollChecks.setBorder(empty);
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
      //  this.scrollChecks.setLayout(ScrollPaneLayout.);
        for (engineRule rule : rules) {
            JCheckBox box = new JCheckBox(rule.engineDesc);
            box.setName(rule.engineName);
            box.setFont(checkboxFont);
            checkBoxPanel.add(box);
            availableRules.add(box);
        }
        this.scrollChecks.getViewport().add(checkBoxPanel);
        this.chkStructural.setSelected(false);
    }

    public void refreshPanel() {
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnValidateStructure;
    private javax.swing.JButton btnViewErrorLog;
    private javax.swing.JCheckBox chkStructural;
    private javax.swing.JCheckBox chkValidation;
    private javax.swing.JLabel lblSelect;
    private javax.swing.JScrollPane scrollChecks;
    // End of variables declaration//GEN-END:variables
}
