/*
 * sectionExists.java
 *
 * Created on January 26, 2008, 10:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.toolbar.conditions.runnable;

import com.sun.star.text.XTextSection;
import org.bungeni.editor.toolbar.conditions.BungeniToolbarCondition;
import org.bungeni.ooo.OOComponentHelper;

/**
 * 
 * Contextual evaluator that checks if a particular section exists in the document.
 * e.g. sectionsExists:section_name
 * will evaluate to true if a section called section_name exists the document
 * will evaluate to false if a section called section_name does not exist the document
 * @author Administrator
 */
public class sectionHasChildType extends baseRunnableCondition {
    /** Creates a new instance of sectionExists */
    public sectionHasChildType() {
    }

    synchronized boolean check_sectionHasChildWithType (OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
        boolean hasChildWithType = false;
        String childWithType =  condition.getConditionValue();
        XTextSection currentSection = ooDocument.currentSection();
        if (currentSection != null ) {
            XTextSection childSection = ooDocument.getChildSectionByType(currentSection, childWithType);
            if (childSection != null ) {
                hasChildWithType = true;
            }
        }
        return hasChildWithType;
    }

    @Override
    public boolean runCondition(OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
        return check_sectionHasChildWithType (ooDocument, condition);
    }
 }
