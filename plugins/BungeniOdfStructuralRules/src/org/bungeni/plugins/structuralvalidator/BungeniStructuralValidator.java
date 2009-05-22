
package org.bungeni.plugins.structuralvalidator;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.bungeni.editor.rulesimpl.StructuralError;
import org.bungeni.editor.rulesimpl.StructuralRulesConfig;
import org.bungeni.editor.rulesimpl.StructuralRulesEngine;
import org.bungeni.plugins.IEditorPlugin;
import org.openoffice.odf.doc.OdfDocument;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniStructuralValidator implements IEditorPlugin {
    /**
     * Supported Params :
     * OdfFileURL = url to odf file
     * SettingsFolder = path to settings folder
     * CurrentDocType = current document type
     */
    private HashMap editorParams = null;
    private String odfFileUrl = null;
    private String rulesRootFolder = null;
    private String currentDocType = null;
    
    private static org.apache.log4j.Logger log = Logger.getLogger(BungeniStructuralValidator.class.getName());
    public BungeniStructuralValidator(){
        
    }

    /**
     *
     * @param inputParams
     */
    public void setParams(HashMap inputParams) {
        System.out.println("setting inputparams");
        this.editorParams = inputParams;
        this.odfFileUrl = (String) this.editorParams.get("OdfFileURL");
        this.rulesRootFolder = (String) this.editorParams.get("RulesRootFolder");
        this.currentDocType = (String) this.editorParams.get("CurrentDocType");
    }

    public String exec() {
        String retValue = "";
        try {
            System.out.println("calling exec()");

            StructuralRulesConfig.APPLN_PATH_PREFIX = rulesRootFolder;
            //configure the source files
            String ruleEnginesFile = StructuralRulesConfig.getRuleEnginesPath() + this.currentDocType + ".xml";
            String docRulesFile = StructuralRulesConfig.getDocRulesPath() + this.currentDocType + ".xml";
            //initalize the rules engine
            StructuralRulesEngine sre = new StructuralRulesEngine(docRulesFile, ruleEnginesFile);
            URL oofileurl = new URL(this.odfFileUrl);
            System.out.println("before loading document");

            OdfDocument odoc = OdfDocument.loadDocument(new File(oofileurl.toURI()));
            System.out.println("after loading document");

            sre.processRulesForDocument(odoc);
            System.out.println("processing rules for document");
            ArrayList<StructuralError> errors = sre.getErrors();
            System.out.println("no.of errors = " + errors.size());
        } catch (Exception ex) {
            log.error("exec :" + ex.getMessage());
        } finally {
            return retValue;
        }
    }


    public static void main(String[] args) {
        BungeniStructuralValidator sval = new BungeniStructuralValidator();
        HashMap mmap = new HashMap();
        mmap.put("OdfFileURL", "file:/home/undesa/Projects/Bungeni/BungeniEditor/trunk/BungeniEditorClient/dist/workspace/files/ke/debaterecord/2009-5-22/eng/ke_debaterecord_2009-5-22_eng.odt");
        mmap.put("RulesRootFolder","/home/undesa/Projects/Bungeni/BungeniEditor/trunk/BungeniEditorClient/dist/settings/structural_rules" );
        mmap.put("CurrentDocType", "debaterecord");
        sval.setParams(mmap);
        sval.exec();
    }
}
