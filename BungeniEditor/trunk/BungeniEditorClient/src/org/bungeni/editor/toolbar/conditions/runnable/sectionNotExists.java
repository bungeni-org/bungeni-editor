/*
 * sectionExists.java
 *
 * Created on January 26, 2008, 10:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.toolbar.conditions.runnable;

import org.bungeni.editor.config.DocTypesReader;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.editor.toolbar.conditions.BungeniToolbarCondition;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.utils.CommonExceptionUtils;

/**
 *
 * @author Administrator
 */
public class sectionNotExists  extends baseRunnableCondition {
//    private OOComponentHelper ooDocument;
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(sectionNotExists.class.getName());
        
    /** Creates a new instance of sectionExists */
    public sectionNotExists() {
    }


    boolean check_sectionNotExists (OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
        boolean bResult = false;
        try {
        //synchronized(ooDocument) {
        String sectionToActUpon =  condition.getConditionValue();
        if (sectionToActUpon.equals(BungeniEditorPropertiesHelper.getDocumentRoot())) {
           log.debug("sectionNotExists: before activeDoc"); 
           String activeDoc =  BungeniEditorProperties.getEditorProperty("activeDocumentMode");
           log.debug("sectionNotExists: activeDocumentMode = " + activeDoc);
           sectionToActUpon = DocTypesReader.getInstance().getRootForDocType(activeDoc);
           log.debug("sectionNotExists: sectionToActUpon = " + sectionToActUpon);
           
        }
        log.debug("sectionNotExists: before hasSection");
        if (ooDocument == null ) {
            log.debug("sectionNotExists: ooDocument is null");
        }
        if (ooDocument.isXComponentValid()) {
            log.debug("sectionNotExists : xcomponent valid");
        }
        log.debug("sectionNotExists, after xcomponent validity");
        if (ooDocument.getTextSections() == null )
            return false;
        String[] sections = ooDocument.getTextSections().getElementNames();
        bResult = true;
        for (String thesection : sections) {
            if (thesection.matches(sectionToActUpon)) {
                bResult = false;
                break;
            }
        }
        /*
        if (ooDocument.hasSection(sectionToActUpon)) {
            log.debug("sectionNotExists, sectionToActUpon :  "+sectionToActUpon + " exists" );
            bResult= false;
        } else {
            log.debug("sectionNotExists, sectionToActUpon :  "+sectionToActUpon + " does not exists" );
            bResult= true;
        }
         */
        //}
        } catch (Exception ex) {
            log.error("check_sectionNotExists:"+ ex.getMessage());
            log.error("check_sectionNotExists, stack:" + CommonExceptionUtils.getStackTrace(ex));
        } finally {
            return bResult;
        }
    }
    

    @Override
    public boolean runCondition(OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
        return check_sectionNotExists(ooDocument, condition);
    }
        


 }
