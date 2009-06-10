/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.rules;

import com.sun.star.text.XTextSection;
import java.util.ArrayList;
import org.bungeni.editor.rulesimpl.BaseStructuralRule;
import org.bungeni.editor.rulesimpl.StructuralError;
import org.bungeni.ooo.ooQueryInterface;
import org.jdom.Element;

/**
 * Checks a section for allowed child sections.
 * Scans the document for the list of sections - checks if they are allowed
 * based on the rule configuration in the rules file.
 * @author Ashok Hariharan
 */
public class AllowedChildSections extends BaseStructuralRule{
    XTextSection xThisSection = null;
    String thisSectionName = "";
    String thisSectionType = "";
    ArrayList<Element> allowedSectionTypes = new ArrayList<Element>(0);
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AllowedChildSections.class.getName());


    @Override
    public boolean applyRule(String forThisSectionName) {
       if (setup(forThisSectionName)) {
            return checkAllowedSectionTypes();
       }
       return false;

    }

    private boolean setup(String forThisSectionName){
       //get section type for input section.
       xThisSection = this.ooDocument.getSection(forThisSectionName);
       thisSectionType = this.ooDocument.getSectionType(xThisSection);
       thisSectionName = forThisSectionName;
        //look for this section type in the rules file
        Element elemSectionType = this.ruleParserEngine.getSectionType(thisSectionType);
        if (elemSectionType != null) { //rule for this section type exists
           //this is the list of allowed child section types for this section
            this.allowedSectionTypes = this.ruleParserEngine.getAllowedSectionTypesForType(thisSectionType);
            //now we have the list of allowed section types ....
            //we now scan the child sections of the document if they have allowed section types
             return true;
        }
        return false;
    }



    public boolean checkAllowedSectionTypes() {
        ///get all child sections
        boolean bState = false;
        try {
            XTextSection[] childSections = xThisSection.getChildSections();
            for (XTextSection childSection: childSections) {
                //get the section type for each child section
                String childSectionType = this.ooDocument.getSectionType(childSection);
                String childSectionName = (ooQueryInterface.XNamed(childSection)).getName();
                //if the section type is not in the allowed list -- flag an error message
                if (!isSectionTypeAllowed(childSectionType)) {
                    log.info("searchForAllowedSectionTypes :  in " + this.thisSectionName + " of " + this.thisSectionType +
                            " the child section " + childSectionName + " of type :" + childSectionType + " is not allowed");
                    StructuralError returnError = this.makeStructuralError(false,
                                thisSectionType,
                                childSectionType,
                                thisSectionName,
                                childSectionName,
                                getName());
                    errorLog.add(returnError);
                }
            }
        } catch (Exception ex) {
            log.error("checkAllowedSectionTypes : " + ex.getMessage());
        } finally {
            return bState;
        }
    }
    

    private boolean isSectionTypeAllowed(String childSectionType) {
        boolean found = false;
        for (Element secType : this.allowedSectionTypes) {
            String matchedSectionType = secType.getAttributeValue("name");
            if (matchedSectionType.equals(childSectionType)) {
                found = true;
                break;
            }
        }
        return found;
    }
}
