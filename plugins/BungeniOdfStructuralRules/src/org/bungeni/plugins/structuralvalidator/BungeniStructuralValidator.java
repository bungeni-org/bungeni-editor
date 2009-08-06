package org.bungeni.plugins.structuralvalidator;

//~--- non-JDK imports --------------------------------------------------------

import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;

import org.bungeni.editor.rulesimpl.StructuralError;
import org.bungeni.editor.rulesimpl.StructuralRulesConfig;
import org.bungeni.editor.rulesimpl.StructuralRulesEngine;
import org.bungeni.plugins.IEditorPlugin;

import org.openoffice.odf.doc.OdfDocument;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.io.StringWriter;
import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
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
     * RunChecks = which checks are to be run
     */
    private HashMap                      editorParams    = null;
    private String                       odfFileUrl      = null;
    private String                       rulesRootFolder = null;
    private String[]                     checksToRun = null;

    public BungeniStructuralValidator() {}

    /**
     *
     * @param inputParams
     */
    public void setParams(HashMap inputParams) {
        try {
        log.debug("setting inputparams");
        System.out.println("setting input params");
        this.editorParams    = inputParams;
        this.odfFileUrl      = (String) this.editorParams.get("OdfFileURL");
        this.rulesRootFolder = (String) this.editorParams.get("RulesRootFolder");
        this.currentDocType  = (String) this.editorParams.get("CurrentDocType");
        this.checksToRun = (String[]) this.editorParams.get("RunChecks");
        this.callerPanel = this.editorParams.get("CallerPanel");
        StructuralRulesConfig.APPLN_PATH_PREFIX = rulesRootFolder;

            if (this.editorParams.containsKey("ParentFrame")) {
                this.callerFrame = (JFrame) this.editorParams.get("ParentFrame");
            }
        System.out.println("finishing input params");

        } catch (Exception ex) {
            log.error("setParams : " + ex.getMessage());
            log.error("setParams : stacktrace : ", ex);
            ex.printStackTrace(System.out);
        }
    }

    public Object exec2(Object[] params) {
        String retValue = "";
        System.out.println("calling showErrors");
          javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        System.out.println("calling launchframe");
                        panelStructuralErrorBrowser.launchFrame(odfFileUrl, callerFrame, callerPanel);
                    }
          });
       return retValue ;
    }

    /**
     * Return rule errros as an XMl document
     * @return
     */
    public String exec() {
        String retValue = "";

        try {
            log.debug("calling exec2()");
            
            // configure the source files
            String ruleEnginesFile = StructuralRulesConfig.getRuleEnginesPath() + this.currentDocType + ".xml";
            String docRulesFile    = StructuralRulesConfig.getDocRulesPath() + this.currentDocType + ".xml";
            // initalize the rules engine
            StructuralRulesEngine sre       = new StructuralRulesEngine(docRulesFile, ruleEnginesFile, this.checksToRun);
            URL                   oofileurl = new URL(this.odfFileUrl);

            log.debug("before loading document");
            OdfDocument odoc = OdfDocument.loadDocument(new File(oofileurl.toURI()));

            log.debug("after loading document");
            sre.processRulesForDocument(odoc);
            log.debug("processing rules for document");

            ArrayList<StructuralError> errors = sre.getErrors();
            StringBuilder sBuilder = new StringBuilder();
            sBuilder.append("<structuralErrors>");
            if (errors.size() > 0 ) {
                //if there were errros we write them to the log file
                StructuralErrorSerialize seSerialize = new StructuralErrorSerialize(this.odfFileUrl);
                seSerialize.writeErrorsToLog(errors);
                //then we extract the string representation of the errors and return them to the caller client
                XStream xst = new XStream();
                StringWriter swLogFile = new StringWriter();
                xst.toXML(errors, swLogFile);
                sBuilder.append(swLogFile.toString());
                //javax.swing.SwingUtilities.invokeLater(new Runnable() {
                //    public void run() {
                //        panelStructuralError.launchFrame(finalErrors, callerFrame, callerPanel);
                //    }
                //});


            }
            sBuilder.append("</structuralErrors>");
            retValue = sBuilder.toString();
        } catch (Exception ex) {
            log.error("exec :" + ex.getMessage());
            log.error("exec : stacktrace : ", ex);
        } 
        return retValue;
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
       // sval.showErrors();
    }
}
