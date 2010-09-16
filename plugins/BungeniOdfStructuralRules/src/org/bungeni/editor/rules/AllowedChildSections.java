package org.bungeni.editor.rules;

import java.util.ArrayList;
import org.bungeni.editor.rulesimpl.BaseStructuralRule;
import org.bungeni.editor.rulesimpl.StructuralError;
import org.bungeni.odfdom.section.BungeniOdfSectionHelper;
import org.dom4j.Element;
import org.odftoolkit.odfdom.doc.text.OdfTextSection;

/**
 * Checks a section for allowed child sections.
 * Scans the document for the list of sections - checks if they are allowed
 * based on the rule configuration in the rules file.
 * @author Ashok Hariharan
 */
public class AllowedChildSections extends BaseStructuralRule{
    OdfTextSection xThisSection = null;
    BungeniOdfSectionHelper odfSectionHelper = null;
    String thisSectionName = "";
    String thisSectionType = "";
    ArrayList<Element> allowedSectionTypes = new ArrayList<Element>(0);
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AllowedChildSections.class.getName());

     private static String INVALID_SECTION_TYPE_IN_DOCUMENT = "Invalid section type in document ";
     private static String SECTION_TYPE_INVALID = "This section Type is not allowed at this location";

     public AllowedChildSections() {
         super();
     }

    @Override
    public boolean applyRule(String forThisSectionName) {
       if (setup(forThisSectionName)) {
            return checkAllowedSectionTypes();
       }
       return false;

    }

    private boolean setup(String forThisSectionName){
   //    BungeniOdfSectionHelper bsh = new BungeniOdfSectionHelper(odfDocument);
       odfSectionHelper = new BungeniOdfSectionHelper(odfDocument);
       xThisSection = odfSectionHelper.getSection(forThisSectionName);
       thisSectionType = odfSectionHelper.getSectionType(xThisSection);
       thisSectionName = forThisSectionName;
        //get section type for input section.
       //look for this section type in the rules file


        Element elemSectionType = this.ruleParserEngine.getSectionType(thisSectionType);
        if (elemSectionType != null) { //rule for this section type exists
           //this is the list of allowed child section types for this section
            this.allowedSectionTypes = this.ruleParserEngine.getAllowedSectionTypesForType(thisSectionType);
            //now we have the list of allowed section types ....
            //we now scan the child sections of the document if they have allowed section types
             return true;
        } {
             StructuralError returnError = this.makeStructuralError(false,
                                thisSectionType,
                                "",
                                thisSectionName,
                                "",
                                getName(),INVALID_SECTION_TYPE_IN_DOCUMENT );
             errorLog.add(returnError);
        return false;
        }
    }



    public boolean checkAllowedSectionTypes() {
        ///get all child sections
        boolean bState = true;
        try {
            ArrayList<OdfTextSection> listofSections = odfSectionHelper.getChildSections(xThisSection);
            for (OdfTextSection odfSection : listofSections) {
                String childSectionType = odfSectionHelper.getSectionType(odfSection);
                String childSectionName = odfSection.getTextNameAttribute();
                if (!isSectionTypeAllowed(childSectionType)) {
                    log.info("searchForAllowedSectionTypes :  in " + this.thisSectionName + " of " + this.thisSectionType +
                            " the child section " + childSectionName + " of type :" + childSectionType + " is not allowed");
                    StructuralError returnError = this.makeStructuralError(false,
                                thisSectionType,
                                childSectionType,
                                thisSectionName,
                                childSectionName,
                                getName(), SECTION_TYPE_INVALID);
                    errorLog.add(returnError);
                    bState = false;
                }
            }
        } catch (Exception ex) {
            log.error("checkAllowedSectionTypes : " + ex.getMessage());
            bState = false;
        } 
            return bState;
        
    }
    

    private boolean isSectionTypeAllowed(String childSectionType) {
        boolean found = false;
        for (Element secType : this.allowedSectionTypes) {
            String matchedSectionType = secType.attributeValue("name");
            if (matchedSectionType.equals(childSectionType)) {
                found = true;
                break;
            }
        }
        return found;
    }
}
