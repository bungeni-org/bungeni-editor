package org.bungeni.plugins.structuralvalidator;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.log4j.Logger;

import org.bungeni.editor.rulesimpl.StructuralError;
import org.bungeni.editor.rulesimpl.StructuralRulesConfig;
import org.bungeni.editor.rulesimpl.StructuralRulesEngine;
import org.bungeni.plugins.IEditorPlugin;

import org.openoffice.odf.doc.OdfDocument;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.bungeni.editor.rules.ui.panelStructuralError;
import org.bungeni.editor.rules.ui.panelStructuralErrorBrowser;
import org.bungeni.editor.rulesimpl.StructuralErrorSerialize;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniStructuralValidator implements IEditorPlugin {
    private static org.apache.log4j.Logger log            =
        Logger.getLogger(BungeniStructuralValidator.class.getName());
    private JFrame                         callerFrame    = null;
    private Object                         callerPanel = null;
    private String                         currentDocType = null;

    /**
     * Supported Params :
     * OdfFileURL = url to odf file
     * SettingsFolder = path to settings folder
     * CurrentDocType = current document type
     */
    private HashMap                      editorParams    = null;
    private String                       odfFileUrl      = null;
    private String                       rulesRootFolder = null;

    public BungeniStructuralValidator() {}

    /**
     *
     * @param inputParams
     */
    public void setParams(HashMap inputParams) {
        try {
        log.debug("setting inputparams");
        this.editorParams    = inputParams;
        this.odfFileUrl      = (String) this.editorParams.get("OdfFileURL");
        this.rulesRootFolder = (String) this.editorParams.get("RulesRootFolder");
        this.currentDocType  = (String) this.editorParams.get("CurrentDocType");
        this.callerPanel = this.editorParams.get("CallerPanel");
        StructuralRulesConfig.APPLN_PATH_PREFIX = rulesRootFolder;

            if (this.editorParams.containsKey("ParentFrame")) {
                this.callerFrame = (JFrame) this.editorParams.get("ParentFrame");
            }
        } catch (Exception ex) {
            log.error("setParams : " + ex.getMessage());
            log.error("setParams : stacktrace : ", ex);
        }
    }

    public void showErrors() {
          javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        panelStructuralErrorBrowser.launchFrame(odfFileUrl, callerFrame, callerPanel);
                    }
          });
    }

    public String exec() {
        String retValue = "";
        try {
            log.debug("calling exec()");

            // configure the source files
            String ruleEnginesFile = StructuralRulesConfig.getRuleEnginesPath() + this.currentDocType + ".xml";
            String docRulesFile    = StructuralRulesConfig.getDocRulesPath() + this.currentDocType + ".xml";
            // initalize the rules engine
            StructuralRulesEngine sre       = new StructuralRulesEngine(docRulesFile, ruleEnginesFile);
            URL                   oofileurl = new URL(this.odfFileUrl);

            log.debug("before loading document");
            OdfDocument odoc = OdfDocument.loadDocument(new File(oofileurl.toURI()));

            log.debug("after loading document");
            sre.processRulesForDocument(odoc);
            log.debug("processing rules for document");

            ArrayList<StructuralError> errors = sre.getErrors();
            if (errors.size() > 0 ) {
                StructuralErrorSerialize seSerialize = new StructuralErrorSerialize(this.odfFileUrl);
                seSerialize.writeErrorsToLog(errors);
                final ArrayList<StructuralError> finalErrors = errors;
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        panelStructuralError.launchFrame(finalErrors, callerFrame, callerPanel);
                    }
                });
                /*
                //Class[] objParams = {String.class};
                //Method mgotoSection = this.callerPanel.getClass().getDeclaredMethod("goToSection", objParams);
                //Object[] funcArgs = { "helloWorld" };
                //mgotoSection.invoke(callerPanel, funcArgs);
                panelStructuralError       pse    = new panelStructuralError(errors, callerPanel);
                pse.setBorder(LineBorder.createGrayLineBorder());

                JDialog floatingFrame = null;

                if (this.callerFrame == null) {
                    floatingFrame = new JDialog(callerFrame);
                } else {
                    floatingFrame = new JDialog();
                }
                floatingFrame.setTitle("Structural Errors");
                floatingFrame.getContentPane().add(pse);
                floatingFrame.setAlwaysOnTop(true);
                if (this.callerFrame != null)
                    floatingFrame.setLocationRelativeTo(null);
                // panelStructuralError pPanel = new panelStructuralError(sErrors, ooDocument);
                pse.setContainerFrame(floatingFrame);
                floatingFrame.add(pse);
                floatingFrame.setSize(465, 320);
                floatingFrame.pack();
                floatingFrame.setVisible(true); */
            } else {
               JOptionPane.showMessageDialog(callerFrame, "No errors were found", "Structural Validator", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            log.error("exec :" + ex.getMessage());
            log.error("exec : stacktrace : ", ex);
        } finally {
            return retValue;
        }
    }

    public static void main(String[] args) {
        BungeniStructuralValidator sval = new BungeniStructuralValidator();
        HashMap                    mmap = new HashMap();

        mmap.put(
            "OdfFileURL",
            "file:/home/undesa/Projects/Bungeni/BungeniEditor/trunk/BungeniEditorClient/dist/workspace/files/ke/debaterecord/2009-5-22/eng/ke_debaterecord_2009-5-22_eng.odt");
        mmap.put(
            "RulesRootFolder",
            "/home/undesa/Projects/Bungeni/BungeniEditor/trunk/BungeniEditorClient/dist/settings/structural_rules");
        mmap.put("CurrentDocType", "debaterecord");
          mmap.put("ParentFrame",null);
                                            mmap.put("CallerPanel", new Object());
      // mmap.put("ParentEventDispatcher", new TestDispatcher());
      // mmap.put("ParentEventDispatcherClass", "org.bungeni.plugins.structuralvalidator.TestDispatcher");

        sval.setParams(mmap);
        sval.showErrors();
    }
}
