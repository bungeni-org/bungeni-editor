
package org.bungeni.editor.panels.loadable;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import org.bungeni.editor.config.BungeniEditorProperties;
import org.bungeni.editor.config.BungeniEditorPropertiesHelper;
import org.bungeni.editor.panels.impl.BaseClassForITabbedPanel;
import org.bungeni.extutils.CommonDocumentUtilFunctions;
import org.bungeni.extutils.CommonEditorFunctions;
import org.bungeni.extutils.CommonXmlUtils;
import org.bungeni.extutils.MessageBox;
import org.bungeni.extutils.CommonEditorXmlUtils;
import org.bungeni.utils.externalplugin.ExternalPlugin;
import org.bungeni.utils.externalplugin.ExternalPluginLoader;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

/**
 *
 * @author  Administrator
 */
public class validateAndCheckPanel extends BaseClassForITabbedPanel {

    private static org.apache.log4j.Logger log = Logger.getLogger(validateAndCheckPanel.class.getName());
    private static final String ENGINE_RULES_FOLDER = "engine_rules";

    /** Creates new form transformXMLPanel */
    public validateAndCheckPanel() {
        initComponents();
        this.btnValidateStructure.addActionListener(new validateStructureActionListener());
    }

    class exportDestination extends Object {

        String exportDestName;
        String exportDestDesc;

        exportDestination(String name, String desc) {
            this.exportDestDesc = desc;
            this.exportDestName = name;
        }

        @Override
        public String toString() {
            return this.exportDestDesc;
        }
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


    private ArrayList<engineRule> loadRuleEngines() {
        FileReader freader = null;
        ArrayList<engineRule> engineRules = new ArrayList<engineRule>(0);

        try {
            final String currentDocType = BungeniEditorPropertiesHelper.getCurrentDocType();
            final String rulesRootFolder = CommonEditorFunctions.getPathRelativeToRoot(BungeniEditorProperties.getEditorProperty("structuralRulesRootPath"));
            freader = new FileReader(rulesRootFolder + File.separator + ENGINE_RULES_FOLDER + File.separator + currentDocType + ".xml");
            SAXBuilder builder = CommonEditorXmlUtils.getNonValidatingSaxBuilder();
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
          ex.printStackTrace();
        } catch (IOException ex) {
          ex.printStackTrace();
        } finally {
            try {
                freader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return engineRules;
    }

    private Object[] getValidatePluginParams() {
        final String odfFileUrl = ooDocument.getDocumentURL();
        final String currentDocType = BungeniEditorPropertiesHelper.getCurrentDocType();
        final String rulesRootFolder = CommonEditorFunctions.getPathRelativeToRoot(BungeniEditorProperties.getEditorProperty("structuralRulesRootPath"));
        final validateAndCheckPanel ppPanel = this;

        Object[] argSetParams = {new HashMap() {

        {
            put("OdfFileURL", odfFileUrl);
            put("CurrentDocType", currentDocType);
            put("RulesRootFolder", rulesRootFolder);
            put("ParentFrame", parentFrame);
            put("CallerPanel", ppPanel);
        //   put("ParentEventDispatcher", seHandler);
        //   put("ParentEventDispatcherClass", "org.bungeni.editor.panels.loadable.transformXMLPanel$StructValidatorEventHandler");
        }
    }};
        return argSetParams;
    }

    private void viewErrorLog() {
        if (ooDocument.isDocumentOnDisk()) {
            ExternalPluginLoader ep = new ExternalPluginLoader();
            final StructValidatorEventHandler seHandler = new StructValidatorEventHandler();
            ExternalPlugin rulesValidator = ep.loadPlugin("StructuralRulesValidator");
            Object[] argSetParams = getValidatePluginParams();
            rulesValidator.callMethod("setParams", argSetParams);
            Object[] argParams = {};
            Object[] argExec = {argParams};
            Object retValue = rulesValidator.callMethod("exec2", argExec);
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
            }

            protected Integer doInBackground() throws Exception {
                //check if server is running .. if running stop it
                int nRet = -1;
                nRet = validateStructure();
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
            //   if (bState)
            //        MessageBox.OK(parentFrame, bundle.getString("Document_was_successfully_Exported_to_the_workspace_folder"));
            //    else
            //        MessageBox.OK(parentFrame, bundle.getString("Document_export_failed"));
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
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    validateStructure();
                    sourceButton.setEnabled(true);
                    parentFrame.setCursor(Cursor.getDefaultCursor());
                }
            });
        //(new buttonActionRunner(sourceButton)).execute();
        }
    }

    private int validateStructure() {
        if (ooDocument.isDocumentOnDisk()) {
            //  generatePlainDocument();
            ExternalPluginLoader ep = new ExternalPluginLoader();

            final StructValidatorEventHandler seHandler = new StructValidatorEventHandler();
            ExternalPlugin rulesValidator = ep.loadPlugin("StructuralRulesValidator");

            /* final String odfFileUrl = ooDocument.getDocumentURL();
            final String currentDocType = BungeniEditorPropertiesHelper.getCurrentDocType();
            final String rulesRootFolder = CommonEditorFunctions.getPathRelativeToRoot(BungeniEditorProperties.getEditorProperty("structuralRulesRootPath"));
            final validateAndCheckPanel ppPanel = this;*/

            /*Object[] argSetParams = {new HashMap() {{
            put("OdfFileURL", odfFileUrl);
            put("CurrentDocType", currentDocType);
            put("RulesRootFolder",rulesRootFolder);
            put("ParentFrame", parentFrame);
            put("CallerPanel", ppPanel);
            //   put("ParentEventDispatcher", seHandler);
            //   put("ParentEventDispatcherClass", "org.bungeni.editor.panels.loadable.transformXMLPanel$StructValidatorEventHandler");
            }}};*/
            Object[] argSetParams = getValidatePluginParams();
            rulesValidator.callMethod("setParams", argSetParams);
            Object[] argExec = {};
            Object retValue = rulesValidator.callMethod("exec", argExec);
            if (retValue != null) {
                String outputFilePath = (String) retValue;
                // MessageBox.OK(parentFrame, "A plain document was generated, it can be found at : \n" + outputFilePath, "Plain Document generation", JOptionPane.INFORMATION_MESSAGE);
                return 0;
            } else {
                MessageBox.OK(parentFrame, bundle.getString("problem_running_struct_checker"), bundle.getString("save_the_document"), JOptionPane.ERROR_MESSAGE);
                return -2;
            }
        } else {
            MessageBox.OK(parentFrame, bundle.getString("save_document_before_proceeding"), bundle.getString("save_the_document"), JOptionPane.ERROR_MESSAGE);
            return -1;

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
        jLabel1 = new javax.swing.JLabel();
        btnViewErrorLog = new javax.swing.JButton();
        chkAllowedChildren = new javax.swing.JCheckBox();
        chkOrderOfSections = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        btnValidate = new javax.swing.JButton();
        btnViewValidationErrors = new javax.swing.JButton();
        btnViewXml = new javax.swing.JButton();

        btnValidateStructure.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/panels/loadable/Bundle"); // NOI18N
        btnValidateStructure.setText(bundle.getString("validateAndCheckPanel.btnValidateStructure.text")); // NOI18N

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 10));
        jLabel1.setText(bundle.getString("validateAndCheckPanel.jLabel1.text")); // NOI18N

        btnViewErrorLog.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnViewErrorLog.setText(bundle.getString("validateAndCheckPanel.btnViewErrorLog.text")); // NOI18N
        btnViewErrorLog.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewErrorLogActionPerformed(evt);
            }
        });

        chkAllowedChildren.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        chkAllowedChildren.setSelected(true);
        chkAllowedChildren.setText(bundle.getString("validateAndCheckPanel.chkAllowedChildren.text")); // NOI18N

        chkOrderOfSections.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        chkOrderOfSections.setSelected(true);
        chkOrderOfSections.setText(bundle.getString("validateAndCheckPanel.chkOrderOfSections.text")); // NOI18N

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 10));
        jLabel2.setText(bundle.getString("validateAndCheckPanel.jLabel2.text")); // NOI18N

        btnValidate.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnValidate.setText(bundle.getString("validateAndCheckPanel.btnValidate.text")); // NOI18N

        btnViewValidationErrors.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnViewValidationErrors.setText(bundle.getString("validateAndCheckPanel.btnViewValidationErrors.text")); // NOI18N

        btnViewXml.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnViewXml.setText(bundle.getString("validateAndCheckPanel.btnViewXml.text")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(chkAllowedChildren).add(chkOrderOfSections).add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 172, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 172, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(btnViewValidationErrors, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(btnValidate, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(btnViewErrorLog, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(btnValidateStructure, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE).add(btnViewXml, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(14, 14, 14).add(jLabel1).add(7, 7, 7).add(chkAllowedChildren).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkOrderOfSections).add(8, 8, 8).add(btnValidateStructure).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnViewErrorLog).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnValidate).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnViewValidationErrors).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnViewXml).addContainerGap(63, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewErrorLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewErrorLogActionPerformed
        // TODO add your handling code here:
        viewErrorLog();
    }//GEN-LAST:event_btnViewErrorLogActionPerformed

    @Override
    public void initialize() {
        super.initialize();
        String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
        ArrayList<engineRule> rules = this.loadRuleEngines();
        for (engineRule rule : rules) {
            System.out.println(rule);
        }

    }

    public void refreshPanel() {
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnValidate;
    private javax.swing.JButton btnValidateStructure;
    private javax.swing.JButton btnViewErrorLog;
    private javax.swing.JButton btnViewValidationErrors;
    private javax.swing.JButton btnViewXml;
    private javax.swing.JCheckBox chkAllowedChildren;
    private javax.swing.JCheckBox chkOrderOfSections;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
