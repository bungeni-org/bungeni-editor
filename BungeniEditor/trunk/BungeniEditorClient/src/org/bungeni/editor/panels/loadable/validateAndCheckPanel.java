/*
 * transformXMLPanel.java
 *
 * Created on May 20, 2008, 10:59 AM
 */

package org.bungeni.editor.panels.loadable;

import java.util.HashMap;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.editor.panels.impl.BaseClassForITabbedPanel;
import org.bungeni.extutils.CommonDocumentUtilFunctions;
import org.bungeni.extutils.CommonEditorFunctions;
import org.bungeni.extutils.MessageBox;
import org.bungeni.utils.externalplugin.ExternalPlugin;
import org.bungeni.utils.externalplugin.ExternalPluginLoader;


/**
 *
 * @author  Administrator
 */
public class validateAndCheckPanel extends BaseClassForITabbedPanel{
       private static org.apache.log4j.Logger log = Logger.getLogger(validateAndCheckPanel.class.getName());

    /** Creates new form transformXMLPanel */
    public validateAndCheckPanel() {
        initComponents();
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
    
    
    
    

    public class StructValidatorEventHandler  {
        public StructValidatorEventHandler(){

        }
        
        public void dispatchEvent(String arg0, Object[] arg1) {
                         String dispatchSectionName = (String) arg1[0];
                         System.out.println(dispatchSectionName);
        }

    }

//    final public IEditorPluginEventDispatcher evtDispatcher = new IEditorPluginEventDispatcher(){
   //                                                                 public void dispatchEvent(String arg0, Object[] arg1) {
  //                                                                      String dispatchSectionName = (String) arg1[0];
    //                                                                    System.out.println(dispatchSectionName);
    //                                                                            }
    //};


private Object[] getValidatePluginParams() {
            final String odfFileUrl = ooDocument.getDocumentURL();
            final String currentDocType = BungeniEditorPropertiesHelper.getCurrentDocType();
            final String rulesRootFolder = CommonEditorFunctions.getPathRelativeToRoot(BungeniEditorProperties.getEditorProperty("structuralRulesRootPath"));
            final validateAndCheckPanel ppPanel = this;

            Object[] argSetParams = {new HashMap() {{
                                            put("OdfFileURL", odfFileUrl);
                                            put("CurrentDocType", currentDocType);
                                            put("RulesRootFolder",rulesRootFolder);
                                            put("ParentFrame", parentFrame);
                                            put("CallerPanel", ppPanel);
                                         //   put("ParentEventDispatcher", seHandler);
                                         //   put("ParentEventDispatcherClass", "org.bungeni.editor.panels.loadable.transformXMLPanel$StructValidatorEventHandler");
                                        }}};
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
private void validateStructure() {
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

        btnValidateStructure = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnViewErrorLog = new javax.swing.JButton();
        chkAllowedChildren = new javax.swing.JCheckBox();
        chkOrderOfSections = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        btnValidate = new javax.swing.JButton();
        btnViewValidationErrors = new javax.swing.JButton();
        btnViewXml = new javax.swing.JButton();

        btnValidateStructure.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/panels/loadable/Bundle"); // NOI18N
        btnValidateStructure.setText(bundle.getString("validateAndCheckPanel.btnValidateStructure.text")); // NOI18N
        btnValidateStructure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValidateStructureActionPerformed(evt);
            }
        });

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
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(chkAllowedChildren)
                    .add(chkOrderOfSections)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 172, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 172, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnViewValidationErrors, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(btnValidate, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(btnViewErrorLog, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(btnValidateStructure, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .add(btnViewXml, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(14, 14, 14)
                .add(jLabel1)
                .add(7, 7, 7)
                .add(chkAllowedChildren)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkOrderOfSections)
                .add(8, 8, 8)
                .add(btnValidateStructure)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnViewErrorLog)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnValidate)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnViewValidationErrors)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnViewXml)
                .addContainerGap(63, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnValidateStructureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValidateStructureActionPerformed
        validateStructure();
        //iterate through the document and process the rules for every section
        //processSections(sre);
    }//GEN-LAST:event_btnValidateStructureActionPerformed

    private void btnViewErrorLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewErrorLogActionPerformed
        // TODO add your handling code here:
        viewErrorLog();
    }//GEN-LAST:event_btnViewErrorLogActionPerformed

    @Override
    public void initialize() {
        super.initialize();
        String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
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
